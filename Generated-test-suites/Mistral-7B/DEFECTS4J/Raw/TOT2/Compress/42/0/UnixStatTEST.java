
package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.*;
import org.junit.Test;

public class UnixStatTest {

    @Test
    public void testUnixStatConstants() {
        // Test PERM_MASK
        int expectedPermMask = 07777;
        int actualPermMask = UnixStat.PERM_MASK;
        assertEquals(expectedPermMask, actualPermMask);

        // Test LINK_FLAG
        int expectedLinkFlag = 0120000;
        int actualLinkFlag = UnixStat.LINK_FLAG;
        assertEquals(expectedLinkFlag, actualLinkFlag);

        // Test FILE_FLAG
        int expectedFileFlag = 0100000;
        int actualFileFlag = UnixStat.FILE_FLAG;
        assertEquals(expectedFileFlag, actualFileFlag);

        // Test DIR_FLAG
        int expectedDirFlag = 040000;
        int actualDirFlag = UnixStat.DIR_FLAG;
        assertEquals(expectedDirFlag, actualDirFlag);

        // Test edge cases for PERM_MASK
        int permMaskMin = 0;
        int permMaskMax = Integer.MAX_VALUE;
        assertFalse("Expected PERM_MASK to be greater than 0", permMask < 0);
        assertFalse("Expected PERM_MASK to be less than " + permMaskMin, permMask < permMaskMin);
        assertFalse("Expected PERM_MASK to be greater than " + permMaskMax, permMask > permMaskMax);

        // Test edge cases for LINK_FLAG
        int linkFlagMin = 0;
        int linkFlagMax = Integer.MAX_VALUE;
        assertFalse("Expected LINK_FLAG to be less than 0", linkFlag < 0);
        assertFalse("Expected LINK_FLAG to be greater than " + linkFlagMax, linkFlag > linkFlagMax);

        // Test edge cases for FILE_FLAG
        int fileFlagMin = 0;
        int fileFlagMax = Integer.MAX_VALUE;
        assertFalse("Expected FILE_FLAG to be less than 0", fileFlag < 0);
        assertFalse("Expected FILE_FLAG to be greater than " + fileFlagMax, fileFlag > fileFlagMax);

        // Test edge cases for DIR_FLAG
        int dirFlagMin = 0;
        int dirFlagMax = Integer.MAX_VALUE;
        assertFalse("Expected DIR_FLAG to be less than 0", dirFlag < 0);
        assertFalse("Expected DIR_FLAG to be greater than " + dirFlagMax, dirFlag > dirFlagMax);

        // Test exception handling for invalid constants
        try {
            int unknownConstant = 9999;
            int actualVal = UnixStat.valueOf(Integer.toBinaryString(unknownConstant).substring(1));
            fail("Expected IllegalArgumentException for unknown constant");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}
