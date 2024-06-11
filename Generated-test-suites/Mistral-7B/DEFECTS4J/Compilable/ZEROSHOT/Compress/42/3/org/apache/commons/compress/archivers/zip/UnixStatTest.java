package org.apache.commons.compress.archivers.zip;
import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

 @Test
 public void testUnixStatConstants() {
 // Test UnixStat interface constants
 assertEquals("Incorrect value for PERM_MASK", 07777, UnixStat.PERM_MASK);
 assertEquals("Incorrect value for LINK_FLAG", 0120000, UnixStat.LINK_FLAG);
 assertEquals("Incorrect value for FILE_FLAG", 0100000, UnixStat.FILE_FLAG);
 assertEquals("Incorrect value for DIR_FLAG", 040000, UnixStat.DIR_FLAG);
 assertEquals("Incorrect value for DEFAULT_LINK_PERM", 0777, UnixStat.DEFAULT_LINK_PERM);
 assertEquals("Incorrect value for DEFAULT_DIR_PERM", 0755, UnixStat.DEFAULT_DIR_PERM);
 assertEquals("Incorrect value for DEFAULT_FILE_PERM", 0644, UnixStat.DEFAULT_FILE_PERM);
 }
}