import org.junit.*;
import java.io.*;
import org.apache.commons.compress.archivers.tar.*;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor1() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        Assert.assertNotNull(tarOut);
    }

    @Test
    public void testConstructor2() {
        OutputStream os = new ByteArrayOutputStream();
        int blockSize = 512;
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os, blockSize);
        Assert.assertNotNull(tarOut);
    }

    @Test
    public void testConstructor3() {
        OutputStream os = new ByteArrayOutputStream();
        int blockSize = 512;
        int recordSize = 1024;
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os, blockSize, recordSize);
        Assert.assertNotNull(tarOut);
    }

    @Test
    public void testSetLongFileMode() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        int longFileMode = TarArchiveOutputStream.LONGFILE_TRUNCATE;
        tarOut.setLongFileMode(longFileMode);
        Assert.assertEquals(longFileMode, tarOut.getLongFileMode());
    }

    @Test
    public void testFinish() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        tarOut.finish();
        String expected = "";
        Assert.assertEquals(expected, os.toString());
    }

    @Test
    public void testClose() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        tarOut.close();
        Assert.assertTrue(tarOut.isClosed());
    }

    @Test
    public void testGetRecordSize() {
        OutputStream os = new ByteArrayOutputStream();
        int blockSize = 512;
        int recordSize = 1024;
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os, blockSize, recordSize);
        int result = tarOut.getRecordSize();
        Assert.assertEquals(recordSize, result);
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOut.putArchiveEntry(archiveEntry);
        String expected = "test.txt";
        Assert.assertEquals(expected, tarOut.getCurrentEntry().getName());
    }

    @Test
    public void testCloseArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOut.putArchiveEntry(archiveEntry);
        tarOut.closeArchiveEntry();
        Assert.assertNull(tarOut.getCurrentEntry());
    }

    @Test
    public void testWrite() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOut.putArchiveEntry(archiveEntry);
        byte[] bytes = "Hello world".getBytes();
        tarOut.write(bytes);
        tarOut.closeArchiveEntry();
        String expected = "Hello world";
        Assert.assertEquals(expected, os.toString());
    }

    @Test
    public void testWrite2() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOut.putArchiveEntry(archiveEntry);
        byte[] bytes = "Hello world".getBytes();
        tarOut.write(bytes, 0, bytes.length);
        tarOut.closeArchiveEntry();
        String expected = "Hello world";
        Assert.assertEquals(expected, os.toString());
    }

    @Test
    public void testWrite3() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOut.putArchiveEntry(archiveEntry);
        int b = 65; // A
        tarOut.write(b);
        tarOut.closeArchiveEntry();
        String expected = "A";
        Assert.assertEquals(expected, os.toString());
    }

    @Test
    public void testWrite4() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOut.putArchiveEntry(archiveEntry);
        byte[] bytes = "Hello".getBytes();
        tarOut.write(bytes, 0, 3);
        tarOut.write(bytes, 3, 2);
        tarOut.closeArchiveEntry();
        String expected = "Hel";
        Assert.assertEquals(expected, os.toString());
    }

    @Test
    public void testWrite5() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        ArchiveEntry archiveEntry = new TarArchiveEntry("test.txt");
        tarOut.putArchiveEntry(archiveEntry);
        byte[] bytes = "Hello".getBytes();
        tarOut.write(bytes, 0, 2);
        tarOut.write(bytes, 2, 2);
        tarOut.write(bytes, 4, 1);
        tarOut.closeArchiveEntry();
        String expected = "Hele";
        Assert.assertEquals(expected, os.toString());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        File inputFile = new File("test.txt");
        String entryName = "test.txt";
        ArchiveEntry archiveEntry = tarOut.createArchiveEntry(inputFile, entryName);
        Assert.assertTrue(archiveEntry instanceof TarArchiveEntry);
        Assert.assertEquals(entryName, archiveEntry.getName());
    }
    
}