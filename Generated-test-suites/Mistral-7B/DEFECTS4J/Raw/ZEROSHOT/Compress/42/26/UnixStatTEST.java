package org.apache.commons.compress.archivers.zip;

import org.apache.commons.compress.utils.TestUtils;
import org.junit.Assert;
import org.junit.Test;

public class UnixStatTest {

    @Test
    public void testConstants() {
        // Test UnixStat constants
        Assert.assertEquals(07777, UnixStat.PERM_MASK);
        Assert.assertEquals(0120000, UnixStat.LINK_FLAG);
        Assert.assertEquals(0100000, UnixStat.FILE_FLAG);
        Assert.assertEquals(040000, UnixStat.DIR_FLAG);

        Assert.assertEquals(0777, UnixStat.DEFAULT_LINK_PERM);
        Assert.assertEquals(0755, UnixStat.DEFAULT_DIR_PERM);
        Assert.assertEquals(0644, UnixStat.DEFAULT_FILE_PERM);
    }

    @Test
    public void testCombinePermissions() {
        // Test UnixStat.combinePermissions
        int permissions = UnixStat.DEFAULT_FILE_PERM;
        permissions = UnixStat.combinePermissions(permissions, UnixStat.PERM_MASK);

        Assert.assertEquals(0644, permissions & UnixStat.PERM_MASK);

        permissions = UnixStat.combinePermissions(permissions, UnixStat.DIR_FLAG);
        Assert.assertTrue((permissions & UnixStat.DIR_FLAG) == UnixStat.DIR_FLAG);

        permissions = UnixStat.combinePermissions(permissions, UnixStat.LINK_FLAG);
        Assert.assertTrue((permissions & UnixStat.LINK_FLAG) == UnixStat.LINK_FLAG);
    }

    @Test
    public void testIsDirectory() {
        // Test UnixStat.isDirectory
        int testPermissions = UnixStat.FILE_FLAG | UnixStat.DIR_FLAG;
        boolean isDirectory = UnixStat.isDirectory(testPermissions);
        Assert.assertTrue(isDirectory);

        testPermissions = UnixStat.FILE_FLAG;
        isDirectory = UnixStat.isDirectory(testPermissions);
        Assert.assertFalse(isDirectory);

        testPermissions = UnixStat.DIR_FLAG;
        isDirectory = UnixStat.isDirectory(testPermissions);
        Assert.assertTrue(isDirectory);

        testPermissions = UnixStat.LINK_FLAG;
        isDirectory = UnixStat.isDirectory(testPermissions);
        Assert.assertFalse(isDirectory);
    }

    @Test
    public void testIsFile() {
        // Test UnixStat.isFile
        int testPermissions = UnixStat.FILE_FLAG;
        boolean isFile = UnixStat.isFile(testPermissions);
        Assert.assertTrue(isFile);

        testPermissions = UnixStat.DIR_FLAG;
        isFile = UnixStat.isFile(testPermissions);
        Assert.assertFalse(isFile);

        testPermissions = UnixStat.LINK_FLAG;
        isFile = UnixStat.isFile(testPermissions);
        Assert.assertFalse(isFile);
    }

    @Test
    public void testIsSymbolicLink() {
        // Test UnixStat.isSymbolicLink
        int testPermissions = UnixStat.LINK_FLAG;
        boolean isSymbolicLink = UnixStat.isSymbolicLink(testPermissions);
        Assert.assertTrue(isSymbolicLink);

        testPermissions = UnixStat.FILE_FLAG;
        isSymbolicLink = UnixStat.isSymbolicLink(testPermissions);
        Assert.assertFalse(isSymbolicLink);

        testPermissions = UnixStat.DIR_FLAG;
        isSymbolicLink = UnixStat.isSymbolicLink(testPermissions);
        Assert.assertFalse(isSymbolicLink);
    }

    @Test
    public void testIsReadable() {
        // Test UnixStat.isReadable
        int testPermissions = UnixStat.DEFAULT_FILE_PERM;
        boolean isReadable = UnixStat.isReadable(testPermissions);
        Assert.assertTrue(isReadable);

        testPermissions = UnixStat.DEFAULT_FILE_PERM & ~UnixStat.PERM_READ;
        isReadable = UnixStat.isReadable(testPermissions);
        Assert.assertFalse(isReadable);
    }

    @Test
    public void testIsWritable() {
        // Test UnixStat.isWritable
        int testPermissions = UnixStat.DEFAULT_FILE_PERM;
        boolean isWritable = UnixStat.isWritable(testPermissions);
        Assert.assertTrue(isWritable);

        testPermissions = UnixStat.DEFAULT_FILE_PERM & ~UnixStat.PERM_WRITE;
        isWritable = UnixStat.isWritable(testPermissions);
        Assert.assertFalse(isWritable);
    }

    @Test
    public void testIsExecutable() {
        // Test UnixStat.isExecutable
        int testPermissions = UnixStat.DEFAULT_FILE_PERM;
        boolean isExecutable = UnixStat.isExecutable(testPermissions);
        Assert.assertFalse(isExecutable);

        testPermissions = testPermissions | UnixStat.PERM_EXEC;
        isExecutable = UnixStat.isExecutable(testPermissions);
        Assert.assertTrue(isExecutable);
    }

    @Test
    public void testCombinePermissionsWithDefaults() {
        // Test UnixStat.combinePermissions with defaults
        int permissions = UnixStat.DEFAULT_FILE_PERM;
        permissions = UnixStat.combinePermissions(permissions, UnixStat.PERM_MASK | UnixStat.DIR_FLAG);

        Assert.assertTrue((permissions & UnixStat.DIR_FLAG) == UnixStat.DIR_FLAG);
        Assert.assertTrue((permissions & UnixStat.PERM_MASK) == (UnixStat.PERM_MASK & ~UnixStat.PERM_WRITE));
    }

    @Test
    public void testCombinePermissionsWithCustomDefaults() {
        // Test UnixStat.combinePermissions with custom defaults
        int customDefaults = UnixStat.DEFAULT_FILE_PERM | UnixStat.PERM_EXEC;
        int permissions = UnixStat.combinePermissions(customDefaults, UnixStat.PERM_MASK | UnixStat.DIR_FLAG);

        Assert.assertTrue((permissions & UnixStat.DIR_FLAG) == UnixStat.DIR_FLAG);
        Assert.assertTrue((permissions & UnixStat.PERM_MASK) == (UnixStat.PERM_MASK & ~UnixStat.PERM_WRITE));
    }

    @Test
    public void testToString() {
        // Test UnixStat.toString
        int testPermissions = UnixStat.DEFAULT_FILE_PERM;
        String stringRepresentation = UnixStat.toString(testPermissions);

        Assert.assertEquals("r--r--r--", stringRepresentation);

        testPermissions = testPermissions | UnixStat.PERM_EXEC;
        stringRepresentation = UnixStat.toString(testPermissions);

        Assert.assertEquals("r--r--r--", stringRepresentation); // Not updated on Unix, but expected behaviour for JUnit test

        testPermissions = testPermissions | UnixStat.PERM_WRITE;
        stringRepresentation = UnixStat.toString(testPermissions);

        Assert.assertEquals("-r-w-r--", stringRepresentation);

        testPermissions = testPermissions | UnixStat.DIR_FLAG;
        stringRepresentation = UnixStat.toString(testPermissions);

        Assert.assertEquals("d-----", stringRepresentation);

        testPermissions = testPermissions | UnixStat.LINK_FLAG;
        stringRepresentation = UnixStat.toString(testPermissions);

        Assert.assertEquals("lrwx----", stringRepresentation);
    }
}