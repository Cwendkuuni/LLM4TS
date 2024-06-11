import org.junit.Test;
import static org.junit.Assert.*;

public class TarArchiveOutputStreamTest {
    
    @Test
    public void testConstructor1() {
        OutputStream os = new OutputStream();
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
        assertNotNull(tarOs);
    }
    
    @Test
    public void testConstructor2() {
        OutputStream os = new OutputStream();
        int blockSize = 512;
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os, blockSize);
        assertNotNull(tarOs);
    }
    
    @Test
    public void testConstructor3() {
        OutputStream os = new OutputStream();
        int blockSize = 512;
        int recordSize = 1024;
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os, blockSize, recordSize);
        assertNotNull(tarOs);
    }
    
    @Test
    public void testSetLongFileMode() {
        OutputStream os = new OutputStream();
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
        int longFileMode = TarArchiveOutputStream.LONGFILE_GNU;
        tarOs.setLongFileMode(longFileMode);
        assertEquals(longFileMode, tarOs.getLongFileMode());
    }
    
    @Test
    public void testGetRecordSize() {
        OutputStream os = new OutputStream();
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
        int blockSize = tarOs.getRecordSize();
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, blockSize);
    }
    
    @Test
    public void testPutArchiveEntry() {
        OutputStream os = new OutputStream();
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
        TarArchiveEntry entry = new TarArchiveEntry();
        tarOs.putArchiveEntry(entry);
        assertNotNull(entry);
    }
    
    @Test
    public void testCloseArchiveEntry() {
        OutputStream os = new OutputStream();
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
        tarOs.closeArchiveEntry();
        assertEquals(false, tarOs.haveUnclosedEntry);
    }
    
    @Test
    public void testWrite() {
        OutputStream os = new OutputStream();
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
        byte[] wBuf = new byte[10];
        int wOffset = 0;
        int numToWrite = 5;
        try {
            tarOs.write(wBuf, wOffset, numToWrite);
        } catch (IOException e) {
            fail(e.getMessage());
        }
        assertEquals(numToWrite, tarOs.count());
    }
    
    @Test
    public void testFinish() {
        OutputStream os = new OutputStream();
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
        try {
            tarOs.finish();
        } catch (IOException e) {
            fail(e.getMessage());
        }
        assertEquals(true, tarOs.finished);
    }
    
    @Test
    public void testClose() {
        OutputStream os = new OutputStream();
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
        try {
            tarOs.close();
        } catch (IOException e) {
            fail(e.getMessage());
        }
        assertEquals(true, tarOs.closed);
    }
    
    @Test
    public void testCreateArchiveEntry() {
        OutputStream os = new OutputStream();
        TarArchiveOutputStream tarOs = new TarArchiveOutputStream(os);
        File inputFile = new File("testFile.txt");
        String entryName = "testEntry";
        try {
            ArchiveEntry entry = tarOs.createArchiveEntry(inputFile, entryName);
            assertEquals(inputFile, entry.getFile());
            assertEquals(entryName, entry.getName());
        } catch (IOException e) {
            fail(e.getMessage());
        }
    }
}