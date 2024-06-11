package org.apache.commons.compress.archivers.zip;
import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

 @Test
 public void testUnixStatConstants() {
 // Test UnixStat constants
 assertEquals(07777, UnixStat.PERM_MASK);
 assertEquals(0120000, UnixStat.LINK_FLAG);
 assertEquals(0100000, UnixStat.FILE_FLAG);
 assertEquals(040000, UnixStat.DIR_FLAG);
 assertEquals(0777, UnixStat.DEFAULT_LINK_PERM);
 assertEquals(0755, UnixStat.DEFAULT_DIR_PERM);
 assertEquals(0644, UnixStat.DEFAULT_FILE_PERM);
 }

 @Test
 public void testUnixStatBitwiseOperations() {
 // Test UnixStat bitwise operations
 int filePermission = UnixStat.DEFAULT_FILE_PERM | UnixStat.PERM_MASK;
 int directoryPermission = UnixStat.DEFAULT_DIR_PERM | UnixStat.PERM_MASK | UnixStat.DIR_FLAG;
 int symbolicLinkPermission = UnixStat.DEFAULT_LINK_PERM | UnixStat.PERM_MASK | UnixStat.LINK_FLAG;

 assertEquals(0644 & 07777, filePermission & UnixStat.PERM_MASK);
 assertEquals(0755 & 07777, directoryPermission & UnixStat.PERM_MASK);
 assertEquals(07777 & 07777, UnixStat.PERM_MASK);

 assertFalse((filePermission & UnixStat.DIR_FLAG) > 0);
 assertFalse((filePermission & UnixStat.LINK_FLAG) > 0);
 assertTrue((filePermission & UnixStat.FILE_FLAG) > 0);

 assertFalse((directoryPermission & UnixStat.LINK_FLAG) > 0);
 assertTrue((directoryPermission & UnixStat.DIR_FLAG) > 0);
 assertTrue((directoryPermission & UnixStat.FILE_FLAG) > 0);

 assertFalse((symbolicLinkPermission & UnixStat.DIR_FLAG) > 0);
 assertFalse((symbolicLinkPermission & UnixStat.FILE_FLAG) > 0);
 assertTrue((symbolicLinkPermission & UnixStat.LINK_FLAG) > 0);
 }
}