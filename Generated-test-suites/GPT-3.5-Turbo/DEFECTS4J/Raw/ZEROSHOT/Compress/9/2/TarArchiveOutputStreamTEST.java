import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;

public class TarArchiveOutputStreamTest {

    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        outputStream.setLongFileMode(0);
        assertEquals(0, outputStream.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());

        // Test when finish is called without any unclosed entries
        outputStream.finish();
        assertTrue(outputStream.isFinished());
        // Test when finish is called with unclosed entries
        outputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
        boolean exceptionThrown = false;
        try {
            outputStream.finish();
        } catch (IOException e) {
            exceptionThrown = true;
        }
        assertTrue(exceptionThrown);
    }

    @Test
    public void testClose() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());

        // Test when close is called without calling finish
        outputStream.close();
        assertTrue(outputStream.isClosed());
        // Test when close is called after calling finish
        outputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
        outputStream.finish();
        outputStream.close();
        assertTrue(outputStream.isClosed());
    }

    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, outputStream.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(byteArrayOutputStream);
        outputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));

        // Check if the entry was written correctly
        byte[] data = byteArrayOutputStream.toByteArray();
        assertEquals("test.txt", new String(data, 0, 100).trim());
    }

    @Test(expected = IOException.class)
    public void testPutArchiveEntryFinished() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        outputStream.finish();
        outputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(byteArrayOutputStream);
        outputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
        outputStream.closeArchiveEntry();
        outputStream.putArchiveEntry(new TarArchiveEntry("test2.txt"));

        // Check if the previous entry is closed correctly and the new entry is written correctly
        byte[] data = byteArrayOutputStream.toByteArray();
        assertEquals("test.txt", new String(data, 0, 100).trim());
        assertEquals("test2.txt", new String(data, 512, 100).trim());
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryNoCurrentEntry() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        outputStream.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryFinished() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        outputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
        outputStream.finish();
        outputStream.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(byteArrayOutputStream);
        outputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
        outputStream.write("Hello, World!".getBytes());
        outputStream.closeArchiveEntry();

        // Check if the content was written correctly
        byte[] data = byteArrayOutputStream.toByteArray();
        assertEquals("Hello, World!", new String(data, 1024, 13));
    }

    @Test(expected = IOException.class)
    public void testWriteExceedsSize() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(byteArrayOutputStream);
        outputStream.putArchiveEntry(new TarArchiveEntry("test.txt", 10));
        outputStream.write("Hello, World!".getBytes());
        outputStream.closeArchiveEntry();
    }

    @Test
    public void testWriteWithAssembledBuffer() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(byteArrayOutputStream);
        outputStream.putArchiveEntry(new TarArchiveEntry("test.txt"));
        outputStream.write("Hello,".getBytes());
        outputStream.write(" ".getBytes());
        outputStream.write("World!".getBytes());
        outputStream.closeArchiveEntry();

        // Check if the content was written correctly
        byte[] data = byteArrayOutputStream.toByteArray();
        assertEquals("Hello, World!", new String(data, 1024, 13));
    }

    @Test
    public void testWriteWithMultipleRecords() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(byteArrayOutputStream);
        outputStream.putArchiveEntry(new TarArchiveEntry("test.txt", 100));
        outputStream.write(new byte[300]);
        outputStream.closeArchiveEntry();

        // Check if the content was written correctly
        byte[] data = byteArrayOutputStream.toByteArray();
        assertEquals(300, data.length - 1024);
    }
    
    @Test
    public void testCreateArchiveEntry() throws IOException {
        TarArchiveOutputStream outputStream = new TarArchiveOutputStream(new ByteArrayOutputStream());
        File inputFile = new File("test.txt");
        ArchiveEntry entry = outputStream.createArchiveEntry(inputFile, inputFile.getName());

        assertEquals(inputFile.getName(), entry.getName());
    }
}