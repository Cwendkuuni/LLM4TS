import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TarArchiveOutputStreamTest {

    private File testFile;
    private TarArchiveOutputStream tarOutputStream;
    private ByteArrayOutputStream outputStream;

    @BeforeEach
    public void setUp() throws Exception {
        testFile = File.createTempFile("test", "tar");
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(outputStream), 512, 512);
    }

    @AfterEach
    public void tearDown() throws Exception {
        tarOutputStream.close();
        testFile.delete();
    }

    @Test
    public void testConstruction() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new ByteArrayOutputStream()));
        assertEquals(TarBuffer.DEFAULT_BLKSIZE, tarOutputStream.getBlockSize());
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarOutputStream.getRecordSize());
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(outputStream), 512, 512);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write("Test content".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        assertEquals(1513, outputStream.size());
        assertEquals(1, Arrays.stream(readTarFile("test.tar")).count());
    }

    @Test
    public void testFinishWithUnclosedEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write("Test content".getBytes());
        assertThrows(IOException.class, () -> tarOutputStream.finish());
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write("Test content".getBytes());
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.close();
        assertEquals(1513, outputStream.size());
        assertEquals(1, Arrays.stream(readTarFile("test.tar")).count());
    }

    @Test
    public void testCloseWithUnclosedEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write("Test content".getBytes());
        tarOutputStream.close();
        assertThrows(IOException.class, () -> tarOutputStream.closeArchiveEntry());
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write("Test content".getBytes());
        tarOutputStream.closeArchiveEntry();
        assertEquals(1513, outputStream.size());
        assertEquals(1, Arrays.stream(readTarFile("test.tar")).count());
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test content".getBytes());
        InputStream testInputStream = new TarInputStream(new FileInputStream(testFile));
        assertEquals(TestContent.class.getBytes(StandardCharsets.UTF_8), IOUtils.toByteArray(inputStream));
        assertEquals(TestContent.class.getBytes(StandardCharsets.UTF_8), IOUtils.toByteArray(testInputStream));
    }

    @Test
    public void testWriteLongFileName() throws IOException {
        TarArchiveOutputStream tarOutputStream = new TarArchiveOutputStream(new CountingOutputStream(outputStream), 512, 512);
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        TarArchiveEntry entry = new TarArchiveEntry("test/longFileName.txt");
        tarOutputStream.putArchiveEntry(entry);
        String longFileName = "test/longFileName/longFileName2.txt";
        tarOutputStream.write(longFileName.getBytes());
        tarOutputStream.write(0);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        assertEquals(1539, outputStream.size());
        assertEquals(1, Arrays.stream(readTarFile("test.tar")).count());
    }

    @Test
    public void testPutArchiveEntryWithUnclosedEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write("Test content".getBytes());
        TarArchiveEntry entry2 = new TarArchiveEntry("test2.txt");
        assertThrows(IOException.class, () -> tarOutputStream.putArchiveEntry(entry2));
    }

    @Test
    public void testPutArchiveEntryWithClosedEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write("Test content".getBytes());
        tarOutputStream.closeArchiveEntry();
        TarArchiveEntry entry2 = new TarArchiveEntry("test2.txt");
        tarOutputStream.putArchiveEntry(entry2);
        tarOutputStream.finish();
        assertEquals(1513 + 1513, outputStream.size());
        assertEquals(2, Arrays.stream(readTarFile("test.tar")).count());
    }

    @Test
    public void testWriteLongFile() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarOutputStream.putArchiveEntry(entry);
        byte[] longContent = new byte[1024 * 1024];
        tarOutputStream.write(longContent);
        tarOutputStream.closeArchiveEntry();
        tarOutputStream.finish();
        assertEquals(1550 + 1024 * 1024, outputStream.size());
        assertEquals(1, Arrays.stream(readTarFile("test.tar")).count());
    }

    private byte[][] readTarFile(String fileName) throws IOException {
        InputStream inputStream = new FileInputStream(fileName);
        return TarUtils.readTar(inputStream);
    }

    private static class TestContent {
        static final String TEST_CONTENT = "Test content";
    }
}