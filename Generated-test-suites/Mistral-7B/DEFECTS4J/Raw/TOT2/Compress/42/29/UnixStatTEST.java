package org.junit.platform.commons.testing.testrule;

import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.*;

@RunWith(JUnit4.class)
public class UnixStatTest {

    @Test
    public void testUnixStatConstants() {
        assertEquals(07777, UnixStat.PERM_MASK);
        assertEquals(0120000, UnixStat.LINK_FLAG);
        assertEquals(0100000, UnixStat.FILE_FLAG);
        assertEquals(040000, UnixStat.DIR_FLAG);
        assertEquals(0777, UnixStat.DEFAULT_LINK_PERM);
        assertEquals(0755, UnixStat.DEFAULT_DIR_PERM);
        assertEquals(0644, UnixStat.DEFAULT_FILE_PERM);

        // Test invalid permissions
        try {
            int invalidPerm = -1;
            fail("Expecting IllegalArgumentException with invalid permission: " + invalidPerm);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test invalid flags
        try {
            int invalidFlag = -1;
            fail("Expecting IllegalArgumentException with invalid flag: " + invalidFlag);
        } catch (IllegalArgumentException e) {
            // Expected
        }

        // Test out of range constants
        try {
            int outOfRangeConstant = 0xFFFFF;
            fail("Expecting IllegalArgumentException with out of range constant: " + outOfRangeConstant);
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }
}