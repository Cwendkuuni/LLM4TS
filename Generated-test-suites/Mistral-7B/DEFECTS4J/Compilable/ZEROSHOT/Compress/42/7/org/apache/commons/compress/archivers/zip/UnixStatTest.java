package org.apache.commons.compress.archivers.zip;
import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UnixStatTest {

 @Test
 public void testUnixStatConstants() {
 // Test UnixStat constants

 assertEquals("PERM_MASK should be 07777", UnixStat.PERM_MASK, 07777);
 assertEquals("LINK_FLAG should be 0120000", UnixStat.LINK_FLAG, 0120000);
 assertEquals("FILE_FLAG should be 0100000", UnixStat.FILE_FLAG, 0100000);
 assertEquals("DIR_FLAG should be 040000", UnixStat.DIR_FLAG, 040000);

 // Test default permissions
 assertEquals("DEFAULT_LINK_PERM should be 0777", UnixStat.DEFAULT_LINK_PERM, 0777);
 assertEquals("DEFAULT_DIR_PERM should be 0755", UnixStat.DEFAULT_DIR_PERM, 0755);
 assertEquals("DEFAULT_FILE_PERM should be 0644", UnixStat.DEFAULT_FILE_PERM, 0644);
 }
}