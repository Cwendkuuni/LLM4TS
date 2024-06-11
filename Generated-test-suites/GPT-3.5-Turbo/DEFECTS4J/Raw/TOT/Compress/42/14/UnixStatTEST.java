import org.junit.Test;
import static org.junit.Assert.*;

public class UnixStatTest {

  // Test cases for PERM_MASK

  /**
   * Test case:
   * Input: PERM_MASK = 07777
   * Output: Expected output is 07777
   * This test case checks if the PERM_MASK constant is set to the correct value.
   */
  @Test
  public void testPermMaskValue() {
    assertEquals(07777, UnixStat.PERM_MASK);
  }
  
  // Test cases for LINK_FLAG

  /**
   * Test case:
   * Input: LINK_FLAG = 0120000
   * Output: Expected output is 0120000
   * This test case checks if the LINK_FLAG constant is set to the correct value.
   */
  @Test
  public void testLinkFlagValue() {
    assertEquals(0120000, UnixStat.LINK_FLAG);
  }
  
  // Test cases for FILE_FLAG

  /**
   * Test case:
   * Input: FILE_FLAG = 0100000
   * Output: Expected output is 0100000
   * This test case checks if the FILE_FLAG constant is set to the correct value.
   */
  @Test
  public void testFileFlagValue() {
    assertEquals(0100000, UnixStat.FILE_FLAG);
  }
  
  // Test cases for DIR_FLAG
  
  /**
   * Test case:
   * Input: DIR_FLAG = 040000
   * Output: Expected output is 040000
   * This test case checks if the DIR_FLAG constant is set to the correct value.
   */
  @Test
  public void testDirFlagValue() {
    assertEquals(040000, UnixStat.DIR_FLAG);
  }

  // Test cases for DEFAULT_LINK_PERM

  /**
   * Test case:
   * Input: DEFAULT_LINK_PERM = 0777
   * Output: Expected output is 0777
   * This test case checks if the DEFAULT_LINK_PERM constant is set to the correct value.
   */
  @Test
  public void testDefaultLinkPermValue() {
    assertEquals(0777, UnixStat.DEFAULT_LINK_PERM);
  }
  
  // Test cases for DEFAULT_DIR_PERM

  /**
   * Test case:
   * Input: DEFAULT_DIR_PERM = 0755
   * Output: Expected output is 0755
   * This test case checks if the DEFAULT_DIR_PERM constant is set to the correct value.
   */
  @Test
  public void testDefaultDirPermValue() {
    assertEquals(0755, UnixStat.DEFAULT_DIR_PERM);
  }
  
  // Test cases for DEFAULT_FILE_PERM

  /**
   * Test case:
   * Input: DEFAULT_FILE_PERM = 0644
   * Output: Expected output is 0644
   * This test case checks if the DEFAULT_FILE_PERM constant is set to the correct value.
   */
  @Test
  public void testDefaultFilePermValue() {
    assertEquals(0644, UnixStat.DEFAULT_FILE_PERM);
  }

}