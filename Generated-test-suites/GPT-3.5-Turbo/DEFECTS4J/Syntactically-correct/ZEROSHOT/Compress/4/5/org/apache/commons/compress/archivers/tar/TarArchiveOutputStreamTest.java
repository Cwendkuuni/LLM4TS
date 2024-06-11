import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class TarArchiveOutputStreamTest {
    
    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
        tarArchiveOutputStream.setLongFileMode(0);
        assertEquals(0, tarArchiveOutputStream.longFileMode);
    }
    
    @Test
    public void testFinish() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
        try {
            tarArchiveOutputStream.finish();
        } catch (IOException e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void testClose() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
        try {
            tarArchiveOutputStream.close();
        } catch (IOException e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
        assertEquals(TarBuffer.DEFAULT_RCDSIZE, tarArchiveOutputStream.getRecordSize());
    }
    
    @Test
    public void testPutArchiveEntry() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
        TarArchiveEntry archiveEntry = new TarArchiveEntry("test");
        try {
            tarArchiveOutputStream.putArchiveEntry(archiveEntry);
        } catch (IOException e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void testCloseArchiveEntry() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
        try {
            tarArchiveOutputStream.closeArchiveEntry();
        } catch (IOException e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void testWrite() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
        byte[] wBuf = new byte[10];
        try {
            tarArchiveOutputStream.write(wBuf, 0, wBuf.length);
        } catch (IOException e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void testWriteEOFRecord() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
        try {
            tarArchiveOutputStream.writeEOFRecord();
        } catch (IOException e) {
            assertTrue(false);
        }
    }
    
    @Test
    public void testCreateArchiveEntry() {
        File inputFile = new File("test");
        String entryName = "test";
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
        try {
            tarArchiveOutputStream.createArchiveEntry(inputFile, entryName);
        } catch (IOException e) {
            assertTrue(false);
        }
    }
    
}