package org.apache.commons.compress.archivers.zip;

import static org.apache. CommonsLang3.builder.EqualsBuilder.reflectEquals;
import static org.apache. CommonsLang3.builder.ToStringBuilder.reflectionToString;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.compress.archivers.zip.UnixStat;

import org.junit.Test;

/**
 * Test class for UnixStat interface.
 */
public class UnixStatTest {

    /**
     * Test methods for UnixStat interface.
     */
    @Test
    public void testUnixStatMethods() {
        // Test constants
        assertEquals(07777, UnixStat.PERM_MASK);
        assertEquals(0120000, UnixStat.LINK_FLAG);
        assertEquals(0100000, UnixStat.FILE_FLAG);
        assertEquals(040000, UnixStat.DIR_FLAG);
        assertEquals(0777, UnixStat.DEFAULT_LINK_PERM);
        assertEquals(0755, UnixStat.DEFAULT_DIR_PERM);
        assertEquals(0644, UnixStat.DEFAULT_FILE_PERM);

        // Test bitwise AND with UnixStat.PERM_MASK
        int perm = 0666;
        int linkPerm = 0777 & UnixStat.PERM_MASK;
        int dirPerm = 0755 & UnixStat.PERM_MASK;
        int filePerm = 0644 & UnixStat.PERM_MASK;

        assertEquals(0666, perm);
        assertEquals(0777, linkPerm);
        assertEquals(0755, dirPerm);
        assertEquals(0644, filePerm);

        // Test checking for symbolic link, plain file and directory
        int symbolicLink = UnixStat.LINK_FLAG | UnixStat.FILE_PERM;
        int plainFile = UnixStat.FILE_FLAG | UnixStat.DEFAULT_FILE_PERM;
        int directory = UnixStat.DIR_FLAG | UnixStat.DEFAULT_DIR_PERM;

        assertTrue(hasLinkFlag(symbolicLink));
        assertFalse(hasLinkFlag(plainFile));
        assertFalse(hasLinkFlag(directory));

        assertTrue(isFile(symbolicLink));
        assertFalse(isFile(plainFile));
        assertTrue(isDirectory(directory));
        assertFalse(isDirectory(plainFile));

        // Test equals and toString methods
        UnixStat unixStat1 = UnixStat.LINK_FLAG | UnixStat.FILE_PERM;
        UnixStat unixStat2 = UnixStat.LINK_FLAG | UnixStat.FILE_PERM;
        UnixStat unixStat3 = UnixStat.DIR_FLAG | UnixStat.DEFAULT_DIR_PERM;

        assertEquals(unixStat1, unixStat2);
        assertEquals(unixStat1, reflectionEquals(unixStat1));

        String unixStatString1 = reflectionToString(unixStat1).toString();
        String unixStatString2 = "UnixStat{linkFlag=true, filePerm=644}";

        assertEquals(unixStatString1, unixStatString2);

        // Test hasLinkFlag method
        assertTrue(hasLinkFlag(UnixStat.LINK_FLAG));
        assertFalse(hasLinkFlag(UnixStat.FILE_FLAG));
        assertFalse(hasLinkFlag(UnixStat.DIR_FLAG));

        // Test isDirectory method
        assertFalse(isDirectory(UnixStat.LINK_FLAG));
        assertFalse(isDirectory(UnixStat.FILE_FLAG));
        assertTrue(isDirectory(UnixStat.DIR_FLAG));

        // Test isFile method
        assertFalse(isFile(UnixStat.LINK_FLAG));
        assertTrue(isFile(UnixStat.FILE_FLAG));
        assertFalse(isFile(UnixStat.DIR_FLAG));

        // Test UnixStat values and their combinations
        int testValue1 = UnixStat.PERM_MASK | UnixStat.LINK_FLAG | UnixStat.FILE_PERM;
        int testValue2 = UnixStat.PERM_MASK | UnixStat.LINK_FLAG | UnixStat.DIR_PERM;
        int testValue3 = UnixStat.PERM_MASK | UnixStat.FILE_PERM | UnixStat.DIR_PERM;

        assertTrue(hasLinkFlag(testValue1));
        assertTrue(hasLinkFlag(testValue2));
        assertFalse(hasLinkFlag(testValue3));

        assertTrue(isFile(testValue1));
        assertTrue(isFile(testValue3));
        assertTrue(isDirectory(testValue2));

        // Test combining symbolic link, plain file and directory
        int combinedValue = UnixStat.LINK_FLAG | UnixStat.FILE_PERM | UnixStat.DIR_FLAG;

        assertTrue(hasLinkFlag(combinedValue));
        assertTrue(isFile(combinedValue));
        assertTrue(isDirectory(combinedValue));
    }

    /**
     * Checks if given UnixStat value has link flag.
     *
     * @param unixStat UnixStat value to check
     * @return true if given UnixStat value has link flag, false otherwise
     */
    private boolean hasLinkFlag(int unixStat) {
        return (unixStat & UnixStat.LINK_FLAG) == UnixStat.LINK_FLAG;
    }

    /**
     * Checks if given UnixStat value represents a plain file.
     *
     * @param unixStat UnixStat value to check
     * @return true if given UnixStat value represents a plain file, false
     *         otherwise
     */
    private boolean isFile(int unixStat) {
        return (unixStat & UnixStat.FILE_FLAG) == UnixStat.FILE_FLAG;
    }

    /**
     * Checks if given UnixStat value represents a directory.
     *
     * @param unixStat UnixStat value to check
     * @return true if given UnixStat value represents a directory, false
     *         otherwise
     */
    private boolean isDirectory(int unixStat) {
        return (unixStat & UnixStat.DIR_FLAG) == UnixStat.DIR_FLAG;
    }
}