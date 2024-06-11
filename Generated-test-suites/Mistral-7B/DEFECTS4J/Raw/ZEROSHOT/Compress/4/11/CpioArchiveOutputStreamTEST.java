import org.apache.commons.compress.archivers.CpioArchiveEntry;
import org.apache.commons.compress.archivers.cpio.CpioArchiveOutputStream;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class CpioArchiveOutputStreamTest {

    private File testFile;
    private CpioArchiveOutputStream out;
    private ArchiveEntry entry;
    private InputStream input;
    private ByteArrayOutputStream expectedOutput;

    @Before
    public void setUp() {
        testFile = new File("test.cpio");
        out = new CpioArchiveOutputStream(new FileOutputStream(testFile), CpioConstants.FORMAT_NEW);
        expectedOutput = new ByteArrayOutputStream();
        entry = new CpioArchiveEntry("test", "testFileContent".getBytes());
    }

    @After
    public void tearDown() throws IOException {
        if (out != null) {
            out.close();
        }
        if (testFile != null && testFile.exists()) {
            testFile.delete();
        }
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        out.putArchiveEntry(entry);
        out.write("testFileContent".getBytes());
        out.closeArchiveEntry();
        byte[] output = testFile.getBytes();
        assertEquals(expectedOutput.toByteArray(), output);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithDuplicateEntry() throws IOException {
        out.putArchiveEntry(entry);
        out.write("testFileContent".getBytes());
        out.closeArchiveEntry();

        out.putArchiveEntry(entry);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPutArchiveEntryWithInvalidFormat() throws IOException {
        out.putArchiveEntry(entry);
        out = new CpioArchiveOutputStream(new FileOutputStream(testFile), (short) 7);
        out.putArchiveEntry(entry);
    }

    @Test
    public void testPutArchiveEntryWithDefaultFormat() throws IOException {
        out.putArchiveEntry(entry);
        out.write("testFileContent".getBytes());
        out.closeArchiveEntry();

        out = new CpioArchiveOutputStream(new FileOutputStream(testFile));
        out.putArchiveEntry(entry);
        out.write("testFileContent".getBytes());
        out.closeArchiveEntry();

        byte[] output = testFile.getBytes();
        assertEquals(expectedOutput.toByteArray(), output);
    }

    @Test
    public void testPutArchiveEntryWithTrailer() throws IOException {
        out.putArchiveEntry(new CpioArchiveEntry(CPIO_TRAILER));
        out.finish();
        byte[] output = testFile.getBytes();
        byte[] expectedTrailer = ArchiveUtils.toByteArray(CpioConstants.MAGIC_NEW_CRC + CpioConstants.C_ISFile + "TRAILER".getBytes());
        assertTrue(ArchiveUtils.startsWith(output, expectedTrailer));
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithNullEntry() throws IOException {
        out.putArchiveEntry(null);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithClassCastException() throws IOException {
        out.putArchiveEntry(new Object() {});
    }

    @Test
    public void testWrite() throws IOException {
        out.putArchiveEntry(entry);
        out.write("testFileContent".getBytes());
        out.closeArchiveEntry();

        out.putArchiveEntry(entry);
        byte[] inputBytes = "otherTestFileContent".getBytes();
        out.write(inputBytes);
        out.closeArchiveEntry();

        out.finish();
        Map<String, CpioArchiveEntry> entries = new HashMap<>();
        InputStream inputStream = new FileInputStream(testFile);
        ArchiveEntry archiveEntry;
        while ((archiveEntry = ArchiveUtils.readArchiveEntry(inputStream)) != null) {
            entries.put(archiveEntry.getName(), (CpioArchiveEntry) archiveEntry);
            input = archiveEntry.getInputStream();
            byte[] testFileContent = new byte[archiveEntry.getSize()];
            InputStreamUtils.copy(input, testFileContent);
            assertEquals("testFileContent".getBytes(), testFileContent);
            input.close();
        }
        inputStream.close();

        assertEquals(2, entries.size());
        CpioArchiveEntry firstEntry = entries.values().iterator().next();
        assertEquals("test", firstEntry.getName());
        assertEquals("testFileContent".getBytes().length, firstEntry.getSize());
        CpioArchiveEntry secondEntry = entries.values().iterator().next();
        assertEquals("otherTestFileContent".getBytes().length, secondEntry.getSize());
    }

    @Test
    public void testClose() throws IOException {
        out.putArchiveEntry(entry);
        out.write("testFileContent".getBytes());
        out.closeArchiveEntry();
        out.close();

        out.putArchiveEntry(entry);
        out.write("testFileContent".getBytes());
        out.closeArchiveEntry();
        out.finish();

        Map<String, CpioArchiveEntry> entries = new HashMap<>();
        InputStream inputStream = new FileInputStream(testFile);
        ArchiveEntry archiveEntry;
        while ((archiveEntry = ArchiveUtils.readArchiveEntry(inputStream)) != null) {
            entries.put(archiveEntry.getName(), (CpioArchiveEntry) archiveEntry);
            input = archiveEntry.getInputStream();
            byte[] testFileContent = new byte[archiveEntry.getSize()];
            InputStreamUtils.copy(input, testFileContent);
            assertEquals("testFileContent".getBytes(), testFileContent);
            input.close();
        }
        inputStream.close();

        assertEquals(2, entries.size());
        CpioArchiveEntry firstEntry = entries.values().iterator().next();
        assertEquals("test", firstEntry.getName());
        assertFalse(firstEntry.isClosed());
        CpioArchiveEntry secondEntry = entries.values().iterator().next();
        assertEquals("otherTestFileContent".getBytes().length, secondEntry.getSize());
        assertTrue(secondEntry.isClosed());
    }

    @Test
    public void testFinish() throws IOException {
        out.putArchiveEntry(entry);
        out.write("testFileContent".getBytes());
        out.closeArchiveEntry();
        out.finish();

        out.putArchiveEntry(entry);
        byte[] inputBytes = "otherTestFileContent".getBytes();
        out.write(inputBytes);
        out.closeArchiveEntry();

        Map<String, CpioArchiveEntry> entries = new HashMap<>();
        InputStream inputStream = new FileInputStream(testFile);
        ArchiveEntry archiveEntry;
        while ((archiveEntry = ArchiveUtils.readArchiveEntry(inputStream)) != null) {
            entries.put(archiveEntry.getName(), (CpioArchiveEntry) archiveEntry);
            input = archiveEntry.getInputStream();
            byte[] testFileContent = new byte[archiveEntry.getSize()];
            InputStreamUtils.copy(input, testFileContent);
            assertEquals("testFileContent".getBytes(), testFileContent);
            input.close();
        }
        inputStream.close();

        assertEquals(2, entries.size());
        CpioArchiveEntry firstEntry = entries.values().iterator().next();
        assertEquals("test", firstEntry.getName());
        assertNull(firstEntry.getNextEntry());
        CpioArchiveEntry secondEntry = entries.values().iterator().next();
        assertEquals("otherTestFileContent".getBytes().length, secondEntry.getSize());
        assertNull(secondEntry.getNextEntry());
    }

    @Test
    public void testWriteWithTrailer() throws IOException {
        out.putArchiveEntry(new CpioArchiveEntry(CPIO_TRAILER));
        out.write("testFileContent".getBytes());
        out.closeArchiveEntry();
        out.finish();

        byte[] output = testFile.getBytes();
        byte[] expectedTrailer = ArchiveUtils.toByteArray(CpioConstants.MAGIC_NEW_CRC + CpioConstants.C_ISFile + "TRAILER".getBytes());
        assertTrue(ArchiveUtils.startsWith(output, expectedTrailer));
    }

    @Test
    public void testWriteWithEmptyEntry() throws IOException {
        out.putArchiveEntry(new CpioArchiveEntry(""));
        out.write("");
        out.closeArchiveEntry();
        out.finish();

        byte[] output = testFile.getBytes();
        assertEquals(ArchiveUtils.toByteArray(CpioConstants.MAGIC_NEW), output, 0, 8);
    }
}