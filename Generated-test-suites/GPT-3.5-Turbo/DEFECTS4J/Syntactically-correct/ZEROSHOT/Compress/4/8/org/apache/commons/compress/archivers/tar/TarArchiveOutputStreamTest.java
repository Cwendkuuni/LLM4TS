import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(os);
        assertNotNull(tao);
    }

    @Test
    public void testConstructorWithBlockSize() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(os, 1024);
        assertNotNull(tao);
    }

    @Test
    public void testConstructorWithBlockSizeAndRecordSize() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(os, 1024, 512);
        assertNotNull(tao);
    }

    @Test
    public void testSetLongFileMode() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(os);
        tao.setLongFileMode(TarArchiveOutputStream.LONGFILE_GNU);
        assertEquals(TarArchiveOutputStream.LONGFILE_GNU, tao.getLongFileMode());
    }

    @Test
    public void testGetRecordSize() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(os);
        tao.setRecordSize(1024);
        assertEquals(1024, tao.getRecordSize());
    }

    @Test
    public void testPutArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(os);
        File file = new File("test.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file);
        tao.putArchiveEntry(entry);
        tao.write("test data".getBytes());
        tao.closeArchiveEntry();
        tao.close();
        // Verify tar file contents
        FileInputStream fis = new FileInputStream(new File("test.tar"));
        byte[] buffer = new byte[1024];
        fis.read(buffer);
        fis.close();
        String tarContents = new String(buffer).trim();
        String expected = "test.txt";
        assertTrue(tarContents.contains(expected));
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithoutPutArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(os);
        tao.closeArchiveEntry();
    }

    @Test(expected = IOException.class)
    public void testCloseArchiveEntryBeforeWritingCompleteEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(os);
        File file = new File("test.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file);
        tao.putArchiveEntry(entry);
        tao.closeArchiveEntry();
    }

    @Test
    public void testWrite() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(os);
        File file = new File("test.txt");
        TarArchiveEntry entry = new TarArchiveEntry(file);
        tao.putArchiveEntry(entry);
        tao.write("test data".getBytes());
        tao.closeArchiveEntry();
        tao.close();
        // Verify tar file contents
        FileInputStream fis = new FileInputStream(new File("test.tar"));
        byte[] buffer = new byte[1024];
        fis.read(buffer);
        fis.close();
        String tarContents = new String(buffer).trim();
        String expected = "test data";
        assertTrue(tarContents.contains(expected));
    }

    @Test
    public void testFinish() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(os);
        tao.finish();
        tao.close();
        // Verify tar file contents
        FileInputStream fis = new FileInputStream(new File("test.tar"));
        byte[] buffer = new byte[1024];
        fis.read(buffer);
        fis.close();
        String tarContents = new String(buffer).trim();
        String expected = new String(new byte[1024]);
        assertTrue(tarContents.endsWith(expected));
    }

    @Test
    public void testClose() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(os);
        tao.close();
        assertTrue(tao.isClosed());
    }

    @Test
    public void testCreateArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tao = new TarArchiveOutputStream(os);
        File inputFile = new File("test.txt");
        String entryName = "test.txt";
        ArchiveEntry entry = tao.createArchiveEntry(inputFile, entryName);
        assertNotNull(entry);
        assertEquals("test.txt", entry.getName());
    }

}