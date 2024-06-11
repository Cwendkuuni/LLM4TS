import org.junit.*;
import static org.junit.Assert.*;

public class BatchDriverTest {
  
  @Test
  public void testConstructor() {
    try {
      BatchDriver driver = new BatchDriver(null, null);
      assertNotNull(driver);
    } catch(Exception e) {
      fail("Constructor threw an exception: " + e.getMessage());
    }
  }
  
  @Test
  public void testConstructor_NullPointerException() {
    try {
      BatchDriver driver = new BatchDriver(null, "batchfile");
      fail("Constructor did not throw a NullPointerException");
    } catch (NullPointerException e) {
      // expected
    } catch (Exception e) {
      fail("Constructor threw an unexpected exception: " + e.getMessage());
    }
  }
  
  @Test
  public void testConstructor_IOException() {
    try {
      BatchDriver driver = new BatchDriver("newzini", null);
      fail("Constructor did not throw an IOException");
    } catch (IOException e) {
      // expected
    } catch (Exception e) {
      fail("Constructor threw an unexpected exception: " + e.getMessage());
    }
  }
  
  @Test
  public void testGetGroups() {
    try {
      BatchDriver driver = new BatchDriver("newzini", "batchfile");
      String[] groups = driver.getGroups();
      
      assertNotNull(groups);
      assertEquals(0, groups.length);
    } catch (Exception e) {
      fail("testGetGroups threw an exception: " + e.getMessage());
    }
  }
  
  @Test
  public void testGetGroups_ArrayIndexOutOfBoundsException() {
    try {
      BatchDriver driver = new BatchDriver("newzini", "batchfile");
      driver.getGroups();
      
      String[] groups = driver.getGroups();
      fail("testGetGroups did not throw an ArrayIndexOutOfBoundsException");
    } catch (ArrayIndexOutOfBoundsException e) {
      // expected
    } catch (Exception e) {
      fail("testGetGroups threw an unexpected exception: " + e.getMessage());
    }
  }
  
  @Test
  public void testGetBatchJob() {
    try {
      BatchDriver driver = new BatchDriver("newzini", "batchfile");
      BatchJob job = driver.getBatchJob("group");
      
      assertNull(job);
    } catch (Exception e) {
      fail("testGetBatchJob threw an exception: " + e.getMessage());
    }
  }
  
  @Test
  public void testGetBatchJob_Exception() {
    try {
      BatchDriver driver = new BatchDriver("newzini", "batchfile");
      BatchJob job = driver.getBatchJob(null);
      
      assertNull(job);
    } catch (Exception e) {
      fail("testGetBatchJob_Exception threw an exception: " + e.getMessage());
    }
  }
  
  @Test
  public void testGetSubjectLines() {
    try {
      BatchDriver driver = new BatchDriver("newzini", "batchfile");
      
      assertNull(driver.getSubjectLines());
    } catch (Exception e) {
      fail("testGetSubjectLines threw an exception: " + e.getMessage());
    }
  }
}