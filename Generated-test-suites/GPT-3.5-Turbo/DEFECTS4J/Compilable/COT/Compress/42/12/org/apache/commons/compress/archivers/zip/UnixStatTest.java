package org.apache.commons.compress.archivers.zip;
import org.junit.Assert;
import org.junit.Test;

public class UnixStatTest {

    @Test
    public void testPermMask() {
        Assert.assertEquals(07777, UnixStat.PERM_MASK);
    }

    @Test
    public void testLinkFlag() {
        Assert.assertEquals(0120000, UnixStat.LINK_FLAG);
    }

    @Test
    public void testFileFlag() {
        Assert.assertEquals(0100000, UnixStat.FILE_FLAG);
    }

    @Test
    public void testDirFlag() {
        Assert.assertEquals(040000, UnixStat.DIR_FLAG);
    }

    @Test
    public void testDefaultLinkPerm() {
        Assert.assertEquals(0777, UnixStat.DEFAULT_LINK_PERM);
    }

    @Test
    public void testDefaultDirPerm() {
        Assert.assertEquals(0755, UnixStat.DEFAULT_DIR_PERM);
    }

    @Test
    public void testDefaultFilePerm() {
        Assert.assertEquals(0644, UnixStat.DEFAULT_FILE_PERM);
    }

}