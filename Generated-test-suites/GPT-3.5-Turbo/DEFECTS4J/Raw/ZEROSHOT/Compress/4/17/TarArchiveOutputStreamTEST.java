import static org.junit.Assert.*;
import org.junit.Test;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.io.IOException;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;

public class TarArchiveOutputStreamTest {

    @Test
    public void testConstructor1() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        assertNotNull(tarOut);
    }
    
    @Test
    public void testConstructor2() {
        OutputStream os = new ByteArrayOutputStream();
        int blockSize = 512;
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os, blockSize);
        assertNotNull(tarOut);
    }
    
    @Test
    public void testConstructor3() {
        OutputStream os = new ByteArrayOutputStream();
        int blockSize = 512;
        int recordSize = 4;
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os, blockSize, recordSize);
        assertNotNull(tarOut);
    }
    
    @Test
    public void testSetLongFileMode() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        tarOut.setLongFileMode(1);
        assertEquals(1, tarOut.longFileMode);
    }
    
    @Test
    public void testFinish() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        tarOut.finish();
        tarOut.flush();
        assertEquals(0, ((ByteArrayOutputStream) os).toByteArray().length);
    }
    
    @Test(expected = IOException.class)
    public void testFinishWithUnclosedEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarOut.putArchiveEntry(entry);
        tarOut.finish();
    }
    
    @Test
    public void testClose() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        tarOut.close();
        tarOut.flush();
        assertTrue(tarOut.closed);
        assertTrue(tarOut.buffer.isClosed());
    }
    
    @Test
    public void testGetRecordSize() {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        int recordSize = tarOut.getRecordSize();
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, recordSize);
    }
    
    @Test
    public void testPutArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarOut.putArchiveEntry(entry);
        assertTrue(tarOut.haveUnclosedEntry);
    }
    
    @Test
    public void testCloseArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarOut.putArchiveEntry(entry);
        tarOut.closeArchiveEntry();
        assertFalse(tarOut.haveUnclosedEntry);
    }
    
    @Test(expected = IOException.class)
    public void testCloseArchiveEntryWithIncompleteData() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarOut.putArchiveEntry(entry);
        tarOut.closeArchiveEntry();
        tarOut.closeArchiveEntry();
    }
    
    @Test
    public void testWrite() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        tarOut.putArchiveEntry(entry);
        byte[] content = "Hello, World!".getBytes();
        tarOut.write(content);
        tarOut.closeArchiveEntry();
        tarOut.close();
        assertEquals(content.length + TarConstants.DEFAULT_RCDSIZE * 2, ((ByteArrayOutputStream) os).toByteArray().length);
    }
    
    @Test(expected = IOException.class)
    public void testWriteExceedMaxSize() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        TarArchiveEntry entry = new TarArchiveEntry("file.txt");
        entry.setSize(10);
        tarOut.putArchiveEntry(entry);
        byte[] content = "Hello, World!".getBytes();
        tarOut.write(content);
        tarOut.closeArchiveEntry();
        tarOut.close();
    }
    
    @Test
    public void testWriteEOFRecord() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        tarOut.writeEOFRecord();
        tarOut.flush();
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, ((ByteArrayOutputStream) os).toByteArray().length);
    }
    
    @Test
    public void testCreateArchiveEntry() throws IOException {
        OutputStream os = new ByteArrayOutputStream();
        TarArchiveOutputStream tarOut = new TarArchiveOutputStream(os);
        File file = new File("test.txt");
        ArchiveEntry entry = tarOut.createArchiveEntry(file, "test.txt");
        assertNotNull(entry);
        assertEquals(file.getName(), entry.getName());
    }

}