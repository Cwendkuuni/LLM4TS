package org.apache.commons.compress.archivers.zip;
import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

 /**
 * Test method for {@link UnixStat#PERM_MASK}
 */
 @Test
 public void testPermMask() {
 int permMask = UnixStat.PERM_MASK;
 // Typical use case: Check if the permMask value is as expected
 assertEquals(07777, permMask);

 // Edge case: Check if the value is less than 0
 assertFalse(permMask < 0);

 // Error scenario: Check if the value is negative
 assertFalse(permMask < 0);
 }

 /**
 * Test method for {@link UnixStat#LINK_FLAG}
 */
 @Test
 public void testLinkFlag() {
 int linkFlag = UnixStat.LINK_FLAG;
 // Typical use case: Check if the linkFlag value is as expected
 assertEquals(0120000, linkFlag);

 // Edge case: Check if the value is less than 0
 assertFalse(linkFlag < 0);

 // Error scenario: Check if the value is negative
 assertFalse(linkFlag < 0);
 }

 /**
 * Test method for {@link UnixStat#FILE_FLAG}
 */
 @Test
 public void testFileFlag() {
 int fileFlag = UnixStat.FILE_FLAG;
 // Typical use case: Check if the fileFlag value is as expected
 assertEquals(0100000, fileFlag);

 // Edge case: Check if the value is less than 0
 assertFalse(fileFlag < 0);

 // Error scenario: Check if the value is negative
 assertFalse(fileFlag < 0);
 }

 /**
 * Test method for {@link UnixStat#DIR_FLAG}
 */
 @Test
 public void testDirFlag() {
 int dirFlag = UnixStat.DIR_FLAG;
 // Typical use case: Check if the dirFlag value is as expected
 assertEquals(040000, dirFlag);

 // Edge case: Check if the value is less than 0
 assertFalse(dirFlag < 0);

 // Error scenario: Check if the value is negative
 assertFalse(dirFlag < 0);
 }

 /**
 * Test method for {@link UnixStat#DEFAULT_LINK_PERM}
 */
 @Test
 public void testDefaultLinkPerm() {
 int defaultLinkPerm = UnixStat.DEFAULT_LINK_PERM;
 // Typical use case: Check if the defaultLinkPerm value is as expected
 assertEquals(0777, defaultLinkPerm);

 // Edge case: Check if the value is less than 0
 assertFalse(defaultLinkPerm < 0);

 // Error scenario: Check if the value is negative
 assertFalse(defaultLinkPerm < 0);
 }

 /**
 * Test method for {@link UnixStat#DEFAULT_DIR_PERM}
 */
 @Test
 public void testDefaultDirPerm() {
 int defaultDirPerm = UnixStat.DEFAULT_DIR_PERM;
 // Typical use case: Check if the defaultDirPerm value is as expected
 assertEquals(0755, defaultDirPerm);

 // Edge case: Check if the value is less than 0
 assertFalse(defaultDirPerm < 0);

 // Error scenario: Check if the value is negative
 assertFalse(defaultDirPerm < 0);
 }

 /**
 * Test method for {@link UnixStat#DEFAULT_FILE_PERM}
 */
 @Test
 public void testDefaultFilePerm() {
 int defaultFilePerm = UnixStat.DEFAULT_FILE_PERM;
 // Typical use case: Check if the defaultFilePerm value is as expected
 assertEquals(0644, defaultFilePerm);

 // Edge case: Check if the value is less than 0
 assertFalse(defaultFilePerm < 0);

 // Error scenario: Check if the value is negative
 assertFalse(defaultFilePerm < 0);
 }
}