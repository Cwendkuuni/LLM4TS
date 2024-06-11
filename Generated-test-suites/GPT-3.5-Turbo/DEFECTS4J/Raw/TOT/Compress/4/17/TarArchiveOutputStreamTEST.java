import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.*;
import org.apache.commons.compress.archivers.tar.*;

public class TarArchiveOutputStreamTest {

    private ByteArrayOutputStream outputStream;
    private TarArchiveOutputStream tarOutputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        tarOutputStream = new TarArchiveOutputStream(outputStream);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test-file.txt");
        entry.setSize(10);
        tarOutputStream.putArchiveEntry(entry);
        assertEquals("test-file.txt", tarOutputStream.currName);
        assertTrue(tarOutputStream.haveUnclosedEntry);
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntry_Error() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test-file.txt");
        entry.setSize(10);
        tarOutputStream.putArchiveEntry(entry);
        assertEquals("test-file.txt", tarOutputStream.currName);
        assertTrue(tarOutputStream.haveUnclosedEntry);

        tarOutputStream.closeArchiveEntry();
        tarOutputStream.putArchiveEntry(entry); // Should throw IOException
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test-file.txt");
        entry.setSize(10);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.closeArchiveEntry();
        assertFalse(tarOutputStream.haveUnclosedEntry);
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntry_Error() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test-file.txt");
        entry.setSize(10);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write(new byte[5]);
        tarOutputStream.closeArchiveEntry(); // Should throw IOException
    }

    @Test
    public void testWrite() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test-file.txt");
        entry.setSize(10);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write("Hello".getBytes());
        tarOutputStream.write("World".getBytes());
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.finish();

        byte[] resultBytes = outputStream.toByteArray();
        String result = new String(resultBytes);

        assertTrue(result.contains("HelloWorld"));
    }

    @Test
    public void testWrite_ExceedSize() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test-file.txt");
        entry.setSize(10);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.write("HelloWorld".getBytes()); // Exceeds size
        tarOutputStream.closeArchiveEntry();

        tarOutputStream.finish();

        byte[] resultBytes = outputStream.toByteArray();
        String result = new String(resultBytes);

        assertFalse(result.contains("HelloWorld"));
    }

    @Test
    public void testSetLongFileMode() {
        tarOutputStream.setLongFileMode(TarArchiveOutputStream.LONGFILE_TRUNCATE);
        assertEquals(TarArchiveOutputStream.LONGFILE_TRUNCATE, tarOutputStream.longFileMode);
    }

    @Test
    public void testGetRecordSize() {
        int recordSize = tarOutputStream.getRecordSize();
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, recordSize);
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test-file.txt");
        entry.setSize(10);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish();

        byte[] resultBytes = outputStream.toByteArray();
        String result = new String(resultBytes);

        assertTrue(result.contains("test-file.txt"));
    }

    @Test(expected = IOException.class)
    public void testFinish_UnclosedEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test-file.txt");
        entry.setSize(10);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.finish(); // Should throw IOException
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test-file.txt");
        entry.setSize(10);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.close();

        assertTrue(tarOutputStream.closed);
    }

    @Test(expected = IOException.class)
    public void testClose_UnclosedEntry() throws IOException {
        TarArchiveEntry entry = new TarArchiveEntry("test-file.txt");
        entry.setSize(10);
        tarOutputStream.putArchiveEntry(entry);
        tarOutputStream.close(); // Should throw IOException
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        File inputFile = new File("test-file.txt");
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(inputFile, "test-file.txt");
        assertTrue(entry instanceof TarArchiveEntry);
    }

    @Test(expected = IOException.class)
    public void testCreateArchiveEntry_Error() throws IOException {
        File inputFile = new File("test-file.txt");
        ArchiveEntry entry = tarOutputStream.createArchiveEntry(inputFile, "test-file.txt");
        tarOutputStream.close();
        tarOutputStream.createArchiveEntry(inputFile, "test-file.txt"); // Should throw IOException
    }

}