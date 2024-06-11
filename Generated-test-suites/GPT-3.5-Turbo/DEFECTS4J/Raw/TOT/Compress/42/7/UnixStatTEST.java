import org.junit.Assert;
import org.junit.Test;

public class UnixStatTest {

    @Test
    public void testPermmask() {
        int expected = 07777;
        int result = UnixStat.PERM_MASK;
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testLinkFlag() {
        int expected = 0120000;
        int result = UnixStat.LINK_FLAG;
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testFileFlag() {
        int expected = 0100000;
        int result = UnixStat.FILE_FLAG;
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testDirFlag() {
        int expected = 040000;
        int result = UnixStat.DIR_FLAG;
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testDefaultLinkPerm() {
        int expected = 0777;
        int result = UnixStat.DEFAULT_LINK_PERM;
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testDefaultDirPerm() {
        int expected = 0755;
        int result = UnixStat.DEFAULT_DIR_PERM;
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testDefaultFilePerm() {
        int expected = 0644;
        int result = UnixStat.DEFAULT_FILE_PERM;
        Assert.assertEquals(expected, result);
    }

}