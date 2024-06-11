import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.compress.utils.CountingOutputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private File tempFile;
    private TarArchiveOutputStream tarArchiveOutputStream;

    @Before
    public void setUp() throws IOException {
        tempFile = File.createTempFile("test", ".tar");
        tarArchiveOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream(tempFile)));
    }

    @After
    public void tearDown() throws IOException {
        tarArchiveOutputStream.close();
        IOUtils.closeQuietly(new FileInputStream(tempFile));
        tempFile.delete();
    }

    @Test
    public void testConstructor() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream(new File("output.tar"))));
        assertNull(tarArchiveOutputStream.getBuffer());
        assertNull(tarArchiveOutputStream.getAssemblerBuffer());
        assertFalse(tarArchiveOutputStream.isFinished());
        assertFalse(tarArchiveOutputStream.isClosed());
        assertFalse(tarArchiveOutputStream.hasUnclosedEntry());
        assertEquals(0, tarArchiveOutputStream.length());
    }

    @Test
    public void testSetLongFileMode() {
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarArchiveOutputStream.getLongFileMode());
        tarArchiveOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tarArchiveOutputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
        tarArchiveOutputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.finish();
        assertEquals(33, tarArchiveOutputStream.length()); // 1 header + 1 data + 1 header
        assertTrue(tarArchiveOutputStream.isFinished());
    }

    @Test(expected = IOException.class)
    public void testFinishBeforePutArchiveEntry() throws IOException {
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
    }

    @Test(expected = IOException.class)
    public void testFinishBeforeClose() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
        tarArchiveOutputStream.finish();
        tarArchiveOutputStream.close();
    }

    @Test
    public void testClose() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
        tarArchiveOutputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.close();
        assertFalse(tarArchiveOutputStream.isFinished());
        assertTrue(tarArchiveOutputStream.isClosed());
        assertEquals(33, tarArchiveOutputStream.length());
    }

    @Test(expected = IOException.class)
    public void testCloseBeforeFinish() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
        tarArchiveOutputStream.close();
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
        tarArchiveOutputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.closeArchiveEntry();
        assertFalse(tarArchiveOutputStream.hasUnclosedEntry());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryBeforePutArchiveEntry() throws IOException {
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryBeforeWrite() throws IOException {
        tarArchiveOutputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
        tarArchiveOutputStream.closeArchiveEntry();
        tarArchiveOutputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        byte[] data = "Hello World!".getBytes(StandardCharsets.UTF_8);
        tarArchiveOutputStream.write(data, 0, data.length);
        byte[] readData = new byte[data.length];
        InputStream inputStream = new ByteArrayInputStream(data);
        IOUtils.copy(inputStream, tarArchiveOutputStream);
        inputStream.close();
        IOUtils.closeQuietly(tarArchiveOutputStream.getInputStream(entry));
        byte[] outputData = new byte[data.length];
        IOUtils.readFully(tarArchiveOutputStream.getInputStream(entry), outputData);
        assertSame(data, outputData);
    }

    @Test(expected = IOException.class)
    public void testWriteBeforePutArchiveEntry() throws IOException {
        tarArchiveOutputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
    }

    @Test(expected = IOException.class)
    public void testWriteBeforeCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testWriteBeforeFinish() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testLength() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.write("Hello World!".getBytes(StandardCharsets.UTF_8));
        tarArchiveOutputStream.closeArchiveEntry();
        assertEquals(33, tarArchiveOutputStream.length());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        assertEquals("test.txt", tarArchiveOutputStream.getCurrentName());
        assertNull(tarArchiveOutputStream.getCurrentEntry());
    }

    @Test
    public void testPutArchiveEntryWithInputStream() throws IOException {
        File inputFile = new File("src/test/resources/test.txt");
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", inputFile);
        tarArchiveOutputStream.putArchiveEntry(entry);
        assertEquals("test.txt", tarArchiveOutputStream.getCurrentName());
        assertSame(entry, tarArchiveOutputStream.getCurrentEntry());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryBeforeClose() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.close();
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryBeforeFinish() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test.txt");
        tarArchiveOutputStream.putArchiveEntry(entry);
        tarArchiveOutputStream.finish();
    }

    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(new CountingOutputStream(new FileOutputStream(new File("output.tar"))));
        assertEquals(512, tarArchiveOutputStream.getRecordSize());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("src/test/resources/test.txt");
        TarArchiveEntry entry = tarArchiveOutputStream.createArchiveEntry(inputFile, "test.txt");
        assertEquals("test.txt", entry.getName());
        assertEquals(inputFile.length(), entry.getSize());
    }
}