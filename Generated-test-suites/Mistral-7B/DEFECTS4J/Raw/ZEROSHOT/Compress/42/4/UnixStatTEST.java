import org.apache.commons.compress.archivers.zip.UnixStat;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

 @Test
 public void testConstantValues() {
 // Test constant values
 assertEquals(07777, UnixStat.PERM_MASK);
 assertEquals(0120000, UnixStat.LINK_FLAG);
 assertEquals(0100000, UnixStat.FILE_FLAG);
 assertEquals(040000, UnixStat.DIR_FLAG);
 }

 @Test
 public void testDefaultPermissions() {
 // Test default permissions
 assertEquals(0777, UnixStat.DEFAULT_LINK_PERM);
 assertEquals(0755, UnixStat.DEFAULT_DIR_PERM);
 assertEquals(0644, UnixStat.DEFAULT_FILE_PERM);
 }

 @Test(expected = IllegalStateException.class)
 public void testUnknownConstant() {
 try {
 int unknownConstant = UnixStat.UNKNOWN_FLAG;
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // Expected exception
 }
 }
}