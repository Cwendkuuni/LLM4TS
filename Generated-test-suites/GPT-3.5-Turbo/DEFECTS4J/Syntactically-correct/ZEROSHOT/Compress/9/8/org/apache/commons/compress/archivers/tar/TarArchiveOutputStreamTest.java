import org.junit.Test;
import org.junit.Assert;

public class TarArchiveOutputStreamTest {
    
    @Test
    public void testSetLongFileMode() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
        tarArchiveOutputStream.setLongFileMode(1);
        Assert.assertEquals(1, tarArchiveOutputStream.longFileMode);
    }
    
    @Test
    public void testFinish() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
        try {
            tarArchiveOutputStream.finish();
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
        Assert.assertTrue(tarArchiveOutputStream.finished);
    }
    
    @Test
    public void testClose() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
        try {
            tarArchiveOutputStream.close();
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
        Assert.assertTrue(tarArchiveOutputStream.closed);
    }
    
    @Test
    public void testGetRecordSize() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
        int recordSize = tarArchiveOutputStream.getRecordSize();
        Assert.assertEquals(0, recordSize);
    }
    
    @Test
    public void testPutArchiveEntry() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
        try {
            tarArchiveOutputStream.putArchiveEntry(null);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
        Assert.assertTrue(tarArchiveOutputStream.haveUnclosedEntry);
    }
    
    @Test
    public void testCloseArchiveEntry() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
        try {
            tarArchiveOutputStream.closeArchiveEntry();
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
        Assert.assertFalse(tarArchiveOutputStream.haveUnclosedEntry);
    }
    
    @Test
    public void testWrite() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
        try {
            tarArchiveOutputStream.write(null, 0, 0);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void testCreateArchiveEntry() {
        TarArchiveOutputStream tarArchiveOutputStream = new TarArchiveOutputStream(null);
        try {
            tarArchiveOutputStream.createArchiveEntry(null, null);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }
}