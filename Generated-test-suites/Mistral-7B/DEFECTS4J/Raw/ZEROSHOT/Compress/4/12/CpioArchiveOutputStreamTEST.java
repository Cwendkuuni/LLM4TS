import org.apache.commons.compress.archivers.cpio.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class CpioArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private CpioArchiveOutputStream cpioArchiveOutputStream;

    @Before
    public void setUp() throws Exception {
        outputStream = new ByteArrayOutputStream();
        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, CpioConstants.FORMAT_OLD_ASCII);
    }

    @After
    public void tearDown() throws Exception {
        if (cpioArchiveOutputStream != null) {
            cpioArchiveOutputStream.close();
        }
    }

    @Test
    public void testPutArchiveEntryWithValidInput() throws IOException {
        String fileName = "testFile";
        String fileContent = "Test File Content";

        byte[] fileBytes = fileContent.getBytes(StandardCharsets.UTF_8);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(fileName);
        entry.setFileSize(fileBytes.length);
        entry.setMode(CpioConstants.C_ISREG);

        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(fileBytes, 0, fileBytes.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveEntry archiveEntry = cpioArchiveOutputStream.createArchiveEntry(new File("testFile"), fileName);

        assertEquals(archiveEntry.getName(), fileName);
        assertEquals(archiveEntry.getSize(), fileBytes.length);

        byte[] readBytes = new byte[fileBytes.length];
        int bytesRead = inputStream.read(readBytes, 0, fileBytes.length);
        assertEquals(bytesRead, fileBytes.length);
        assertEquals(new String(readBytes, StandardCharsets.UTF_8), fileContent);
    }

    @Test
    public void testPutArchiveEntryWithDuplicateEntry() throws IOException {
        String fileName = "testFile";
        String fileContent = "Test File Content";

        byte[] fileBytes = fileContent.getBytes(StandardCharsets.UTF_8);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(fileName);
        entry.setFileSize(fileBytes.length);
        entry.setMode(CpioConstants.C_ISREG);

        cpioArchiveOutputStream.putArchiveEntry(entry);
        cpioArchiveOutputStream.write(fileBytes, 0, fileBytes.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        assertThrows(IOException.class, () -> cpioArchiveOutputStream.putArchiveEntry(entry));
    }

    @Test
    public void testPutArchiveEntryWithInvalidFormat() throws IOException {
        String fileName = "testFile";
        String fileContent = "Test File Content";

        byte[] fileBytes = fileContent.getBytes(StandardCharsets.UTF_8);

        CpioArchiveEntry entry = new CpioArchiveEntry();
        entry.setName(fileName);
        entry.setFileSize(fileBytes.length);
        entry.setMode(CpioConstants.C_ISREG);

        cpioArchiveOutputStream = new CpioArchiveOutputStream(outputStream, (short) 0x10);

        assertThrows(IllegalArgumentException.class, () -> cpioArchiveOutputStream.putArchiveEntry(entry));
    }

    @Test
    public void testPutArchiveEntryWithNullEntry() throws IOException {
        cpioArchiveOutputStream.putArchiveEntry(null);
        assertNull(cpioArchiveOutputStream.getNextEntry());
    }

    @Test
    public void testPutArchiveEntryWithInvalidEntry() throws IOException {
        CpioArchiveEntry entry = new CpioArchiveEntry();

        assertThrows(ClassCastException.class, () -> cpioArchiveOutputStream.putArchiveEntry(entry));
    }

    @Test
    public void testFinish() throws IOException {
        cpioArchiveOutputStream.finish();
        assertTrue(cpioArchiveOutputStream.getNextEntry() == null);
        Map<String, ArchiveEntry> entries = new HashMap<>();
        ArchiveEntry entry;
        while ((entry = cpioArchiveOutputStream.getNextEntry()) != null) {
            entries.put(entry.getName(), entry);
        }
        assertEquals(1, entries.size());
    }

    @Test
    public void testClose() throws IOException {
        cpioArchiveOutputStream.close();
        assertFalse(cpioArchiveOutputStream.isOpen());
    }

    @Test
    public void testWriteMultipleEntries() throws IOException {
        String fileName1 = "testFile1";
        String fileContent1 = "Test File Content 1";

        String fileName2 = "testFile2";
        String fileContent2 = "Test File Content 2";

        byte[] fileBytes1 = fileContent1.getBytes(StandardCharsets.UTF_8);
        byte[] fileBytes2 = fileContent2.getBytes(StandardCharsets.UTF_8);

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName(fileName1);
        entry1.setFileSize(fileBytes1.length);
        entry1.setMode(CpioConstants.C_ISREG);

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName(fileName2);
        entry2.setFileSize(fileBytes2.length);
        entry2.setMode(CpioConstants.C_ISREG);

        cpioArchiveOutputStream.putArchiveEntry(entry1);
        cpioArchiveOutputStream.write(fileBytes1, 0, fileBytes1.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.putArchiveEntry(entry2);
        cpioArchiveOutputStream.write(fileBytes2, 0, fileBytes2.length);
        cpioArchiveOutputStream.finish();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveEntry archiveEntry1 = cpioArchiveOutputStream.createArchiveEntry(new File("testFile1"), fileName1);
        ArchiveEntry archiveEntry2 = cpioArchiveOutputStream.createArchiveEntry(new File("testFile2"), fileName2);

        assertEquals(archiveEntry1.getName(), fileName1);
        assertEquals(archiveEntry1.getSize(), fileBytes1.length);
        assertEquals(archiveEntry2.getName(), fileName2);
        assertEquals(archiveEntry2.getSize(), fileBytes2.length);

        byte[] readBytes1 = new byte[fileBytes1.length];
        int bytesRead1 = inputStream.read(readBytes1, 0, fileBytes1.length);
        assertEquals(bytesRead1, fileBytes1.length);
        assertEquals(new String(readBytes1, StandardCharsets.UTF_8), fileContent1);

        byte[] readBytes2 = new byte[fileBytes2.length];
        int bytesRead2 = inputStream.read(readBytes2, 0, fileBytes2.length);
        assertEquals(bytesRead2, fileBytes2.length);
        assertEquals(new String(readBytes2, StandardCharsets.UTF_8), fileContent2);
    }

    @Test
    public void testWriteMultipleEntriesWithInvalidFileSize() throws IOException {
        String fileName1 = "testFile1";
        String fileContent1 = "Test File Content 1";

        String fileName2 = "testFile2";
        String fileContent2 = "Test File Content 2";

        byte[] fileBytes1 = fileContent1.getBytes(StandardCharsets.UTF_8);
        byte[] fileBytes2 = fileContent2.getBytes(StandardCharsets.UTF_8);

        CpioArchiveEntry entry1 = new CpioArchiveEntry();
        entry1.setName(fileName1);
        entry1.setFileSize(fileBytes1.length + 1); // Incorrect file size
        entry1.setMode(CpioConstants.C_ISREG);

        CpioArchiveEntry entry2 = new CpioArchiveEntry();
        entry2.setName(fileName2);
        entry2.setFileSize(fileBytes2.length);
        entry2.setMode(CpioConstants.C_ISREG);

        cpioArchiveOutputStream.putArchiveEntry(entry1);
        cpioArchiveOutputStream.write(fileBytes1, 0, fileBytes1.length);
        cpioArchiveOutputStream.closeArchiveEntry();

        cpioArchiveOutputStream.putArchiveEntry(entry2);
        cpioArchiveOutputStream.finish();

        InputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ArchiveEntry archiveEntry1 = cpioArchiveOutputStream.createArchiveEntry(new File("testFile1"), fileName1);
        ArchiveEntry archiveEntry2 = cpioArchiveOutputStream.createArchiveEntry(new File("testFile2"), fileName2);

        assertEquals(archiveEntry1.getName(), fileName1);
        assertEquals(archiveEntry1.getSize(), fileBytes1.length);
        assertEquals(archiveEntry2.getName(), fileName2);
        assertEquals(archiveEntry2.getSize(), fileBytes2.length);

        byte[] readBytes1 = new byte[fileBytes1.length];
        int bytesRead1 = inputStream.read(readBytes1, 0, fileBytes1.length);
        assertEquals(bytesRead1, fileBytes1.length);
        assertEquals(new String(readBytes1, StandardCharsets.UTF_8), fileContent1);

        byte[] readBytes2 = new byte[fileBytes2.length];
        int bytesRead2 = inputStream.read(readBytes2, 0, fileBytes2.length);
        assertEquals(bytesRead2, fileBytes2.length);
        assertEquals(new String(readBytes2, StandardCharsets.UTF_8), fileContent2);

        InputStream invalidEntryStream = new ByteArrayInputStream(new byte[1]); // Empty byte array
        ArchiveEntry invalidEntry = cpioArchiveOutputStream.createArchiveEntry(new File("testFile3"), "testFile3");
        assertTrue(ArchiveUtils.isStreamCorrupted(invalidEntryStream));
        assertEquals(invalidEntry.getSize(), 0);
    }
}