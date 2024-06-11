package org.apache.commons.compress.archivers.zip;
import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

 /**
 * Test for constant PERM_MASK value.
 */
 @Test
 public void testPermMask() {
 int permMask = UnixStat.PERM_MASK;
 assertEquals(07777, permMask);
 }

 /**
 * Test for constant LINK_FLAG value.
 */
 @Test
 public void testLinkFlag() {
 int linkFlag = UnixStat.LINK_FLAG;
 assertEquals(0120000, linkFlag);
 }

 /**
 * Test for constant FILE_FLAG value.
 */
 @Test
 public void testFileFlag() {
 int fileFlag = UnixStat.FILE_FLAG;
 assertEquals(0100000, fileFlag);
 }

 /**
 * Test for constant DIR_FLAG value.
 */
 @Test
 public void testDirFlag() {
 int dirFlag = UnixStat.DIR_FLAG;
 assertEquals(040000, dirFlag);
 }

 /**
 * Test for constant DEFAULT_LINK_PERM value.
 */
 @Test
 public void testDefaultLinkPerm() {
 int defaultLinkPerm = UnixStat.DEFAULT_LINK_PERM;
 assertEquals(0777, defaultLinkPerm);
 }

 /**
 * Test for constant DEFAULT_DIR_PERM value.
 */
 @Test
 public void testDefaultDirPerm() {
 int defaultDirPerm = UnixStat.DEFAULT_DIR_PERM;
 assertEquals(0755, defaultDirPerm);
 }

 /**
 * Test for constant DEFAULT_FILE_PERM value.
 */
 @Test
 public void testDefaultFilePerm() {
 int defaultFilePerm = UnixStat.DEFAULT_FILE_PERM;
 assertEquals(0644, defaultFilePerm);
 }
}