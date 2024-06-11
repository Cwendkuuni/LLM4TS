import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
    }

    @Test
    public void testConstructor() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(outputStream);
        assertEquals(TarArchiveOutputStream.DEFAULT_BLKSIZE, tarStream.getBlockSize());
        assertEquals(TarArchiveOutputStream.DEFAULT_RCDSIZE, tarStream.getRecordSize());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorNullOutputStream() throws IOException {
        new TarArchiveOutputStream(null);
    }

    @Test
    public void testSetLongFileMode() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(outputStream);
        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(outputStream);
        tarStream.finish();
        assertEquals(2 * TarConstants.RECORD_LENGTH, outputStream.size());
    }

    @Test(expected = IOException.class)
    public void testFinishAlreadyFinished() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(outputStream);
        tarStream.finish();
        tarStream.finish();
    }

    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test content");
        tarStream.putArchiveEntry(entry);
        tarStream.finish();
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(outputStream);
        tarStream.close();
        assertEquals(0, outputStream.size());
    }

    @Test(expected = IOException.class)
    public void testCloseAlreadyClosed() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(outputStream);
        tarStream.close();
        tarStream.close();
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test content");
        tarStream.putArchiveEntry(entry);
        assertEquals("test.txt\000*\000test content\000*\000\000\000",
                new String(outputStream.toByteArray(), StandardCharsets.US_ASCII));
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileName() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test/long/path/test.txt", "test content");
        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarStream.putArchiveEntry(entry);
        assertTrue(outputStream.toByteArray().length > TarConstants.NAMELEN);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameAndErrorMode() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test/long/path/test.txt", "test content");
        tarStream.putArchiveEntry(entry);
        assertEquals(0, outputStream.size());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryWithLongFileNameAndTruncateMode() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test/long/path/test.txt", "test content");
        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarStream.putArchiveEntry(entry);
        assertEquals("test/long/path/test.txt\000*\000test content\000*\000\000\000",
                new String(outputStream.toByteArray(), StandardCharsets.US_ASCII));
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test content");
        tarStream.putArchiveEntry(entry);
        tarStream.write(new byte[10]);
        tarStream.closeArchiveEntry();
        assertEquals("test.txt\000*\000test content\000*\000\000\001\002\003\004\005\006\007\008\009",
                new String(outputStream.toByteArray(), StandardCharsets.US_ASCII));
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test content");
        tarStream.putArchiveEntry(entry);
        tarStream.write("Hello world!".getBytes());
        tarStream.closeArchiveEntry();
        assertEquals("test.txt\000*\000test content\000*\000Hello world!\001\002\003\004\005\006\007" +
                "\008\009\010\011\012\013\014\015",
                new String(outputStream.toByteArray(), StandardCharsets.US_ASCII));
    }

    @Test
    public void testWriteWithLongFileName() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test/long/path/test.txt", "test content");
        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        tarStream.putArchiveEntry(entry);
        tarStream.write("Hello world!".getBytes());
        tarStream.closeArchiveEntry();
        assertTrue(outputStream.toByteArray().length > TarConstants.NAMELEN);
    }

    @Test
    public void testWriteWithLongFileNameAndErrorMode() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test/long/path/test.txt", "test content");
        tarStream.putArchiveEntry(entry);
        tarStream.write("Hello world!".getBytes());
        assertEquals(0, outputStream.size());
    }

    @Test
    public void testWriteWithLongFileNameAndTruncateMode() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test/long/path/test.txt", "test content");
        tarStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        tarStream.putArchiveEntry(entry);
        tarStream.write("Hello world!".getBytes());
        assertEquals("test/long/path/test.txt\000*\000test content\000*\000Hello world!\001\002\003\004" +
                "\005\006\007\008\009\010\011\012\013\014\015\016\017\018\019\020",
                new String(outputStream.toByteArray(), StandardCharsets.US_ASCII));
    }

    @Test
    public void testFlush() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = new TarArchiveEntry("test.txt", "test content");
        tarStream.putArchiveEntry(entry);
        tarStream.write(new byte[10]);
        tarStream.flush();
        assertEquals("test.txt\000*\000test content\000*\000\001\002\003\004\005\006\007\008\009",
                new String(outputStream.toByteArray(), StandardCharsets.US_ASCII));
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(outputStream);
        File file = new File("test.txt");
        TarArchiveEntry entry = tarStream.createArchiveEntry(file, "test.txt");
        assertEquals("test.txt\000*\000*\000\000\000", new String(entry.getHeaderRecord().getData(), StandardCharsets.US_ASCII));
    }

    @Test
    public void testCreateArchiveEntryNullInputFile() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(outputStream);
        assertNull(tarStream.createArchiveEntry(null, "test.txt"));
    }

    @Test
    public void testCreateArchiveEntryWithNullFileName() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = tarStream.createArchiveEntry(new File("test.txt"), null);
        assertEquals("test.txt\000*\000test.txt\000*\000\000\000", new String(entry.getHeaderRecord().getData(), StandardCharsets.US_ASCII));
    }

    @Test
    public void testCreateArchiveEntryWithEmptyFileName() throws IOException {
        TarArchiveOutputStream tarStream = new TarArchiveOutputStream(outputStream);
        TarArchiveEntry entry = tarStream.createArchiveEntry(new File("test.txt"), "");
        assertEquals("test.txt\000*\000*\000\000\000", new String(entry.getHeaderRecord().getData(), StandardCharsets.US_ASCII));
    }

    @After
    public void tearDown() throws Exception {
        outputStream.close();
    }
}