package org.apache.commons.compress.archivers.zip;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class UnixStatTest {

    @Test
    public void testConstants() {
        // Test UnixStat constants
        assertEquals(07777, UnixStat.PERM_MASK);
        assertEquals(0120000, UnixStat.LINK_FLAG);
        assertEquals(0100000, UnixStat.FILE_FLAG);
        assertEquals(040000, UnixStat.DIR_FLAG);
        assertEquals(0777, UnixStat.DEFAULT_LINK_PERM);
        assertEquals(0755, UnixStat.DEFAULT_DIR_PERM);
        assertEquals(0644, UnixStat.DEFAULT_FILE_PERM);

        // Test different combinations of FLAGS
        assertEquals(UnixStat.LINK_FLAG | UnixStat.FILE_FLAG, 0120001);
        assertEquals(UnixStat.LINK_FLAG | UnixStat.DIR_FLAG, 0160000);
        assertEquals(UnixStat.FILE_FLAG | UnixStat.DIR_FLAG, 0140000);
        assertEquals(UnixStat.PERM_MASK & ~UnixStat.LINK_FLAG, 0177771);
    }

    @Test
    public void testCheckFileType() {
        // Test different combinations of FLAGS and check file type
        assertEquals(isLinkType(0120001), true);
        assertEquals(isLinkType(0160000), true);
        assertEquals(isLinkType(0120000), false); // not a link, only has FILE_FLAG
        assertEquals(isLinkType(0140000), false); // only has DIR_FLAG
        assertEquals(isLinkType(0177771), false); // no LINK_FLAG
    }

    private boolean isLinkType(int fileType) {
        return (fileType & UnixStat.LINK_FLAG) != 0;
    }

    @Test
    public void testDefaultPermissions() {
        assertEquals(UnixStat.DEFAULT_FILE_PERM, 0644);
        assertEquals(String.format("r--r--r--"), OctalPermissionsToSymbolic(UnixStat.DEFAULT_FILE_PERM));

        assertEquals(UnixStat.DEFAULT_DIR_PERM, 0755);
        assertEquals(String.format("rwxr-xr-x"), OctalPermissionsToSymbolic(UnixStat.DEFAULT_DIR_PERM));

        assertEquals(UnixStat.DEFAULT_LINK_PERM, 0777);
        assertEquals(String.format("rwxrwxrwx"), OctalPermissionsToSymbolic(UnixStat.DEFAULT_LINK_PERM));
    }

    private String OctalPermissionsToSymbolic(int octalPermissions) {
        String permissions = "";
        if ((octalPermissions & 00004) > 0) permissions += "r";
        if ((octalPermissions & 00002) > 0) permissions += "w";
        if ((octalPermissions & 00001) > 0) permissions += "x";

        permissions += " ";

        if ((octalPermissions & 00014) > 0) permissions += "r";
        if ((octalPermissions & 00012) > 0) permissions += "w";
        if ((octalPermissions & 00011) > 0) permissions += "x";

        permissions += " ";

        if ((octalPermissions & 00114) > 0) permissions += "r";
        if ((octalPermissions & 00112) > 0) permissions += "w";
        if ((octalPermissions & 00111) > 0) permissions += "x";

        return permissions;
    }
}