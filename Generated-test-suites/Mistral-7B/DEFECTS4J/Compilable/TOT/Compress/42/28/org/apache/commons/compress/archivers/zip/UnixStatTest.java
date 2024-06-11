package org.apache.commons.compress.archivers.zip;
import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

    @Test
    public void testLinkFlag() {
        int linkStat = UnixStat.LINK_FLAG;
        assertTrue(isSetBit(linkStat, 12));
        assertTrue(isSetBit(linkStat, 10));
        assertFalse(isSetBit(linkStat, 7));
        assertFalse(isSetBit(linkStat, 6));
        assertFalse(isSetBit(linkStat, 5));
        assertFalse(isSetBit(linkStat, 4));
        assertFalse(isSetBit(linkStat, 3));
        assertFalse(isSetBit(linkStat, 2));
        assertFalse(isSetBit(linkStat, 1));
        assertFalse(isSetBit(linkStat, 0));
    }

    @Test
    public void testFileFlag() {
        int fileStat = UnixStat.FILE_FLAG;
        assertTrue(isSetBit(fileStat, 10));
        assertFalse(isSetBit(fileStat, 12));
        assertFalse(isSetBit(fileStat, 7));
        assertFalse(isSetBit(fileStat, 6));
        assertFalse(isSetBit(fileStat, 5));
        assertFalse(isSetBit(fileStat, 4));
        assertFalse(isSetBit(fileStat, 3));
        assertFalse(isSetBit(fileStat, 2));
        assertFalse(isSetBit(fileStat, 1));
        assertFalse(isSetBit(fileStat, 0));
    }

    @Test
    public void testDirFlag() {
        int dirStat = UnixStat.DIR_FLAG;
        assertTrue(isSetBit(dirStat, 10));
        assertFalse(isSetBit(dirStat, 12));
        assertTrue(isSetBit(dirStat, 7));
        assertFalse(isSetBit(dirStat, 6));
        assertFalse(isSetBit(dirStat, 5));
        assertFalse(isSetBit(dirStat, 4));
        assertFalse(isSetBit(dirStat, 3));
        assertFalse(isSetBit(dirStat, 2));
        assertFalse(isSetBit(dirStat, 1));
        assertTrue(isSetBit(dirStat, 0));
    }

    @Test
    public void testDefaultPermissions() {
        int linkDefaultPerm = UnixStat.DEFAULT_LINK_PERM;
        int dirDefaultPerm = UnixStat.DEFAULT_DIR_PERM;
        int fileDefaultPerm = UnixStat.DEFAULT_FILE_PERM;

        int linkMask = UnixStat.PERM_MASK & ~UnixStat.LINK_FLAG;
        int dirMask = UnixStat.PERM_MASK & ~UnixStat.DIR_FLAG;
        int fileMask = UnixStat.PERM_MASK & ~UnixStat.FILE_FLAG;

        assertEquals(0777 & 0777, linkDefaultPerm);
        assertEquals(0755 & 0755, dirDefaultPerm);
        assertEquals(0644 & 0644, fileDefaultPerm);

        assertEquals((linkMask & linkDefaultPerm) >> 6, 06);
        assertEquals((linkMask & linkDefaultPerm) >> 3, 07);
        assertEquals((linkMask & linkDefaultPerm), linkMask);

        assertEquals((dirMask & dirDefaultPerm) >> 6, 07);
        assertEquals((dirMask & dirDefaultPerm) >> 3, 05);
        assertEquals((dirMask & dirDefaultPerm), dirMask);

        assertEquals((fileMask & fileDefaultPerm) >> 6, 06);
        assertEquals((fileMask & fileDefaultPerm) >> 3, 04);
        assertEquals((fileMask & fileDefaultPerm), fileMask);
    }

    private boolean isSetBit(int number, int bit) {
        return (number & (1 << bit)) != 0;
    }
}