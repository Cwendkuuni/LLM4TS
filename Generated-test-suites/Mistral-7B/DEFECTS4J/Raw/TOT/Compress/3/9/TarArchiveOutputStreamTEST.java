import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class TarArchiveOutputStreamTest {

    private OutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() throws IOException {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream, 512, 512);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
    }

    @After
    public void tearDown() throws IOException {
        tarOutputStream.close();
        IOUtils.closeQuietly(outputStream);
    }

    @Test
    public void testPutArchiveEntryAndWriteAndClose() throws IOException {
        File inputFile = new File("testFile.txt");
        TarArchiveEntry archiveEntry = tarOutputStream.createArchiveEntry(inputFile, "testFile.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        String testContent = "Test content";
        byte[] testBytes = testContent.getBytes(StandardCharsets.UTF_8);
        tarOutputStream.write(testBytes, 0, testBytes.length);
        tarOutputStream.closeArchiveEntry();
        byte[] expectedTarRecord = generateExpectedTarRecord("testFile.txt", testContent.length(), testContent.getBytes(StandardCharsets.UTF_8));
        assertEquals(expectedTarRecord, outputStream.toByteArray());
        assertNull(archiveEntry.isWriteCompleted());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithoutCloseArchiveEntry() throws IOException {
        File inputFile = new File("testFile.txt");
        TarArchiveEntry archiveEntry = tarOutputStream.createArchiveEntry(inputFile, "testFile.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        assertNull(archiveEntry.isWriteCompleted());
        assertThrows(IOException.class, () -> tarOutputStream.write(new byte[0], 0, 0));
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry archiveEntry = tarOutputStream.createArchiveEntry(new File("nonExistentFile"), "nonExistentFile.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        tarOutputStream.finish();
        byte[] expectedTarRecord = generateExpectedTarRecord("nonExistentFile.txt", 0, null);
        byte[] actualTarRecord = new byte[outputStream.size()];
        System.arraycopy(outputStream.toByteArray(), 0, actualTarRecord, 0, outputStream.size());
        assertEquals(expectedTarRecord, actualTarRecord);
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveEntry archiveEntry = tarOutputStream.createArchiveEntry(new File("testFile.txt"), "testFile.txt");
        tarOutputStream.putArchiveEntry(archiveEntry);
        byte[] testContent = "Test content".getBytes(StandardCharsets.UTF_8);
        tarOutputStream.write(testContent, 0, testContent.length);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();
        byte[] expectedTarArchive = generateExpectedTarArchive(testContent);
        byte[] actualTarArchive = new ByteArrayInputStream(outputStream.toByteArray()).readAllBytes();
        assertEquals(expectedTarArchive, actualTarArchive);
    }

    private byte[] generateExpectedTarRecord(String fileName, int fileSize, byte[] content) {
        byte[] header = new TarArchiveEntry(fileName, fileSize, 0, 0, 0, TarConstants.MAGIC, TarConstants.VERSION).getHeaderBytes();
        byte[] record = new byte[512];
        System.arraycopy(header, 0, record, 0, header.length);
        System.arraycopy(content == null ? new byte[fileSize] : content, 0, record, header.length, fileSize);
        return record;
    }

    private byte[] generateExpectedTarArchive(byte[] content) {
        byte[] header = new TarArchiveEntry("testFile.txt", 22, 0, 0, 22, TarConstants.MAGIC, TarConstants.VERSION).getHeaderBytes();
        byte[] record = new byte[512];
        System.arraycopy(header, 0, record, 0, header.length);
        System.arraycopy(content, 0, record, header.length, content.length);
        byte[] eof = new byte[512];
        for (int i = 0; i < eof.length; ++i) {
            eof[i] = 0;
        }
        byte[] tarArchive = new byte[512 * 3 + content.length];
        System.arraycopy(header, 0, tarArchive, 0, header.length);
        System.arraycopy(record, 0, tarArchive, header.length, record.length);
        System.arraycopy(eof, 0, tarArchive, header.length + record.length, eof.length);
        System.arraycopy(record, 0, tarArchive, header.length + record.length + eof.length, record.length);
        return tarArchive;
    }
}