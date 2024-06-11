
package Newzgrabber;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

public class BatchDriverTest {
  
  private BatchDriver batchDriver;
  
  @Before
  public void setup() throws NullPointerException, IOException {
    String newzini = "newz.ini";
    String batchfile = "batch.ini";
    batchDriver = new BatchDriver(newzini, batchfile);
  }
  
  @Test
  public void testGetGroups() {
    String[] groups = batchDriver.getGroups();
    assertNotNull(groups);
    assertEquals(3, groups.length);
    assertEquals("group1", groups[0]);
    assertEquals("group2", groups[1]);
    assertEquals("group3", groups[2]);
  }
  
  @Test
  public void testGetBatchJob() {
    BatchJob batchJob = batchDriver.getBatchJob("group1");
    assertNotNull(batchJob);
    assertEquals("server1", batchJob.getServer());
    assertEquals("directory1", batchJob.getDirectory());
    assertEquals(1, batchJob.getFirstId());
    assertEquals(10, batchJob.getLastId());
    assertNull(batchJob.getUsername());
    assertNull(batchJob.getPassword());
    
    batchJob = batchDriver.getBatchJob("group2");
    assertNotNull(batchJob);
    assertEquals("server2", batchJob.getServer());
    assertEquals("directory2", batchJob.getDirectory());
    assertEquals(50, batchJob.getFirstId());
    assertEquals(100, batchJob.getLastId());
    assertEquals("username2", batchJob.getUsername());
    assertEquals("password2", batchJob.getPassword());
    
    batchJob = batchDriver.getBatchJob("group3");
    assertNull(batchJob);
  }
  
  @Test
  public void testGetSubjectLines() {
    batchDriver.getBatchJob("group1");
    String[] subjectLines = batchDriver.getSubjectLines();
    assertNotNull(subjectLines);
    assertEquals(3, subjectLines.length);
    assertEquals("subject1", subjectLines[0]);
    assertEquals("subject2", subjectLines[1]);
    assertEquals("subject3", subjectLines[2]);
    
    batchDriver.getBatchJob("group2");
    subjectLines = batchDriver.getSubjectLines();
    assertNotNull(subjectLines);
    assertEquals(0, subjectLines.length);
    
    batchDriver.getBatchJob("group3");
    subjectLines = batchDriver.getSubjectLines();
    assertNull(subjectLines);
  }
}
