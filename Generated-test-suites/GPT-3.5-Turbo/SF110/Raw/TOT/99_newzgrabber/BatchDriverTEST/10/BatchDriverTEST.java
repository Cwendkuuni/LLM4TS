import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;

public class BatchDriverTest {
  
  private BatchDriver batchDriver;
  
  @Before
  public void setUp() {
    try {
      batchDriver = new BatchDriver("newzini.txt", "batchfile.txt");
    } catch (IOException e) {
      fail("IOException should not be thrown");
    }
  }

  @Test
  public void testGetGroups() {
    String[] expected = {"Group1", "Group2", "Group3"};
    try {
      String[] groups = batchDriver.getGroups();
      assertArrayEquals(expected, groups);
    } catch (ArrayIndexOutOfBoundsException e) {
      fail("ArrayIndexOutOfBoundsException should not be thrown");
    }
  }
  
  @Test
  public void testGetGroups_EmptyBatchgroupsVector() {
    batchDriver.BatchgroupsVector.removeAllElements();
    try {
      batchDriver.getGroups();
      fail("ArrayIndexOutOfBoundsException should be thrown");
    } catch (ArrayIndexOutOfBoundsException e) {
      // Pass
    }
  }
  
  @Test
  public void testGetGroups_NullBatchgroupsVector() {
    batchDriver.BatchgroupsVector = null;
    try {
      batchDriver.getGroups();
      fail("ArrayIndexOutOfBoundsException should be thrown");
    } catch (ArrayIndexOutOfBoundsException e) {
      // Pass
    }
  }
  
  @Test
  public void testGetBatchJob() {
    BatchJob job = batchDriver.getBatchJob("Group1");
    assertNotNull(job);
    assertEquals("Server1", job.getServer());
    assertEquals("Directory1", job.getDirectory());
    assertEquals("Username1", job.getUsername());
    assertEquals("Password1", job.getPassword());
    assertEquals(3, job.getNThreads());
    assertEquals(500, job.getTimeout());
    assertEquals(true, job.isTimeoutEnabled());
    assertEquals("GroupInfo1", job.getGroupInfo());
    assertEquals(100, job.getFirstId());
    assertEquals(200, job.getLastId());
  }
  
  @Test
  public void testGetBatchJob_GroupNotFound() {
    BatchJob job = batchDriver.getBatchJob("NonExistingGroup");
    assertNull(job);
  }
  
  @Test
  public void testGetBatchJob_NullOptionsHash() {
    batchDriver.OptionsHash = null;
    BatchJob job = batchDriver.getBatchJob("Group1");
    assertNull(job);
  }
  
  @Test
  public void testGetBatchJob_NullNewsSocket() {
    Newzgrabber.nf.setNewsSocket(null);
    BatchJob job = batchDriver.getBatchJob("Group1");
    assertNull(job);
  }
  
  @Test
  public void testGetBatchJob_NullUsername() {
    batchDriver.OptionsHash.put("username", null);
    BatchJob job = batchDriver.getBatchJob("Group1");
    assertNotNull(job);
    assertNull(job.getUsername());
  }
  
  @Test
  public void testGetBatchJob_NullPassword() {
    batchDriver.OptionsHash.put("password", null);
    BatchJob job = batchDriver.getBatchJob("Group1");
    assertNotNull(job);
    assertNull(job.getPassword());
  }
  
  @Test
  public void testGetBatchJob_InvalidThreads() {
    batchDriver.OptionsHash.put("threads", "not a number");
    BatchJob job = batchDriver.getBatchJob("Group1");
    assertNotNull(job);
    assertEquals(1, job.getNThreads());
  }
  
  @Test
  public void testGetBatchJob_InvalidTimeout() {
    batchDriver.OptionsHash.put("timeout", "not a number");
    BatchJob job = batchDriver.getBatchJob("Group1");
    assertNotNull(job);
    assertEquals(-1, job.getTimeout());
  }
  
  @Test
  public void testGetSubjectLines() {
    String[] expected = {"Subject1", "Subject2", "Subject3"};
    String[] subjectLines = batchDriver.getSubjectLines();
    assertArrayEquals(expected, subjectLines);
  }
  
  @Test
  public void testGetSubjectLines_NullSubjectLines() {
    batchDriver.subjectlines = null;
    String[] subjectLines = batchDriver.getSubjectLines();
    assertNull(subjectLines);
  }
}