import org.junit.Test;

public class UnixStatTest {

   @Test
   public void testPERM_MASK() {
      int expectedResult = 07777;
      Assert.assertEquals(expectedResult, UnixStat.PERM_MASK);
   }

   @Test
   public void testLINK_FLAG() {
      int expectedResult = 0120000;
      Assert.assertEquals(expectedResult, UnixStat.LINK_FLAG);
   }

   @Test
   public void testFILE_FLAG() {
      int expectedResult = 0100000;
      Assert.assertEquals(expectedResult, UnixStat.FILE_FLAG);
   }

   @Test
   public void testDIR_FLAG() {
      int expectedResult = 040000;
      Assert.assertEquals(expectedResult, UnixStat.DIR_FLAG);
   }

   @Test
   public void testDEFAULT_LINK_PERM() {
      int expectedResult = 0777;
      Assert.assertEquals(expectedResult, UnixStat.DEFAULT_LINK_PERM);
   }

   @Test
   public void testDEFAULT_DIR_PERM() {
      int expectedResult = 0755;
      Assert.assertEquals(expectedResult, UnixStat.DEFAULT_DIR_PERM);
   }

   @Test
   public void testDEFAULT_FILE_PERM() {
      int expectedResult = 0644;
      Assert.assertEquals(expectedResult, UnixStat.DEFAULT_FILE_PERM);
   }
}