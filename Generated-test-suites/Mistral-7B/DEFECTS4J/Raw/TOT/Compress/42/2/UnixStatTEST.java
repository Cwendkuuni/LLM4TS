import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

 @Test
 public void testLinkFlag() {
 int linkPerm = UnixStat.DEFAULT_LINK_PERM;
 int linkFlag = UnixStat.LINK_FLAG;
 int expected = linkPerm | linkFlag;
 assertEquals(expected, (linkPerm | linkFlag));
 }

 @Test
 public void testFileFlag() {
 int filePerm = UnixStat.DEFAULT_FILE_PERM;
 int fileFlag = UnixStat.FILE_FLAG;
 int expected = filePerm | fileFlag;
 assertEquals(expected, (filePerm | fileFlag));
 }

 @Test
 public void testDirFlag() {
 int dirPerm = UnixStat.DEFAULT_DIR_PERM;
 int dirFlag = UnixStat.DIR_FLAG;
 int expected = dirPerm | dirFlag;
 assertEquals(expected, (dirPerm | dirFlag));
 }

 @Test
 public void testPermissionsWithAllFlags() {
 int linkPerm = UnixStat.DEFAULT_LINK_PERM;
 int filePerm = UnixStat.DEFAULT_FILE_PERM;
 int dirPerm = UnixStat.DEFAULT_DIR_PERM;
 int linkFlag = UnixStat.LINK_FLAG;
 int fileFlag = UnixStat.FILE_FLAG;
 int dirFlag = UnixStat.DIR_FLAG;
 int expectedLink = UnixStat.DEFAULT_LINK_PERM | UnixStat.LINK_FLAG;
 int expectedFile = UnixStat.DEFAULT_FILE_PERM | UnixStat.FILE_FLAG;
 int expectedDir = UnixStat.DEFAULT_DIR_PERM | UnixStat.DIR_FLAG;
 int allExpected = expectedLink | expectedFile | expectedDir;
 assertEquals(allExpected, (expectedLink | expectedFile | expectedDir));
 }

 @Test
 public void testNegativePermissions() {
 int linkPerm = UnixStat.PERM_MASK & ~UnixStat.LINK_FLAG;
 int filePerm = UnixStat.PERM_MASK & ~UnixStat.FILE_FLAG;
 int dirPerm = UnixStat.PERM_MASK & ~UnixStat.DIR_FLAG;
 int linkFlag = UnixStat.LINK_FLAG;
 int fileFlag = UnixStat.FILE_FLAG;
 int dirFlag = UnixStat.DIR_FLAG;
 int expectedLink = UnixStat.PERM_MASK & (~linkFlag);
 int expectedFile = UnixStat.PERM_MASK & (~fileFlag);
 int expectedDir = UnixStat.PERM_MASK & (~dirFlag);
 int allExpected = expectedLink | expectedFile | expectedDir;
 assertEquals(allExpected, (expectedLink | expectedFile | expectedDir));
 }
}