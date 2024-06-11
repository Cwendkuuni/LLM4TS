package org.apache.commons.compress.archivers.zip;
import org.junit.Assert;
import org.junit.Test;

public class UnixStatTest {
  
  @Test
  public void testPERM_MASK() {
    int expectedResult = 07777;
    int actualResult = UnixStat.PERM_MASK;
    Assert.assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testLINK_FLAG() {
    int expectedResult = 0120000;
    int actualResult = UnixStat.LINK_FLAG;
    Assert.assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testFILE_FLAG() {
    int expectedResult = 0100000;
    int actualResult = UnixStat.FILE_FLAG;
    Assert.assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testDIR_FLAG() {
    int expectedResult = 040000;
    int actualResult = UnixStat.DIR_FLAG;
    Assert.assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testDEFAULT_LINK_PERM() {
    int expectedResult = 0777;
    int actualResult = UnixStat.DEFAULT_LINK_PERM;
    Assert.assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testDEFAULT_DIR_PERM() {
    int expectedResult = 0755;
    int actualResult = UnixStat.DEFAULT_DIR_PERM;
    Assert.assertEquals(expectedResult, actualResult);
  }
  
  @Test
  public void testDEFAULT_FILE_PERM() {
    int expectedResult = 0644;
    int actualResult = UnixStat.DEFAULT_FILE_PERM;
    Assert.assertEquals(expectedResult, actualResult);
  }
}