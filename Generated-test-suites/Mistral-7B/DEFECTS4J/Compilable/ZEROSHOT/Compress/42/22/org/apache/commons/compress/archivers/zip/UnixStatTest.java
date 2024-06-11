package org.apache.commons.compress.archivers.zip;
import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

 @Test
 public void testLinkFlag() {
 int linkFlag = UnixStat.LINK_FLAG;
 int expected = 0120000;
 assertEquals("Link flag value mismatch", expected, linkFlag);
 }

 @Test
 public void testFileFlag() {
 int fileFlag = UnixStat.FILE_FLAG;
 int expected = 0100000;
 assertEquals("File flag value mismatch", expected, fileFlag);
 }

 @Test
 public void testDirFlag() {
 int dirFlag = UnixStat.DIR_FLAG;
 int expected = 040000;
 assertEquals("Directory flag value mismatch", expected, dirFlag);
 }

 @Test
 public void testDefaultLinkPerm() {
 int defaultLinkPerm = UnixStat.DEFAULT_LINK_PERM;
 int expected = 0777;
 assertEquals("Default link permissions mismatch", expected, defaultLinkPerm);
 }

 @Test
 public void testDefaultDirPerm() {
 int defaultDirPerm = UnixStat.DEFAULT_DIR_PERM;
 int expected = 0755;
 assertEquals("Default directory permissions mismatch", expected, defaultDirPerm);
 }

 @Test
 public void testDefaultFilePerm() {
 int defaultFilePerm = UnixStat.DEFAULT_FILE_PERM;
 int expected = 0644;
 assertEquals("Default file permissions mismatch", expected, defaultFilePerm);
 }

 @Test
 public void testPermMask() {
 int permMask = UnixStat.PERM_MASK;
 int expected = 07777;
 assertEquals("Permission mask value mismatch", expected, permMask);
 }

}