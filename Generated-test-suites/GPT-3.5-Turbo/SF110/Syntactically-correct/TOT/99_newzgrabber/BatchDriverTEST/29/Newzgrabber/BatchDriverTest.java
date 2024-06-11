import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import java.util.*;

public class BatchDriverTest {
  
  private BatchDriver batchDriver;
  private String newzini;
  private String batchfile;
  
  @Before
  public void setUp() {
    newzini = "path/to/newzini";
    batchfile = "path/to/batchfile";
    batchDriver = new BatchDriver(newzini, batchfile);
  }
  
  @Test
  public void testGetGroupsValid() {
    String[] expectedGroups = {"group1", "group2", "group3"};
    String[] actualGroups = batchDriver.getGroups();
    assertArrayEquals(expectedGroups, actualGroups);
  }
  
  @Test
  public void testGetGroupsEmpty() {
    batchDriver.BatchgroupsVector = new Vector();
    String[] expectedGroups = {};
    String[] actualGroups = batchDriver.getGroups();
    assertArrayEquals(expectedGroups, actualGroups);
  }
  
  @Test(expected = ArrayIndexOutOfBoundsException.class)
  public void testGetGroupsOutOfBounds() {
    batchDriver.BatchgroupsVector = new Vector();
    batchDriver.getGroups();
  }
  
  @Test
  public void testGetBatchJobValid() {
    String group = "group1";
    BatchJob expectedBatchJob = new BatchJob("server1", "group1", "directory1", 0, 100);
    BatchJob actualBatchJob = batchDriver.getBatchJob(group);
    assertEquals(expectedBatchJob, actualBatchJob);
  }
  
  @Test
  public void testGetBatchJobMissingServer() {
    String group = "group1";
    batchDriver.OptionsHash.remove("server");
    BatchJob actualBatchJob = batchDriver.getBatchJob(group);
    assertNull(actualBatchJob);
  }
  
  @Test
  public void testGetBatchJobMissingDirectory() {
    String group = "group2";
    batchDriver.BatchgroupsHash.remove(group);
    BatchJob actualBatchJob = batchDriver.getBatchJob(group);
    assertNull(actualBatchJob);
  }
  
  @Test
  public void testGetBatchJobMissingUsernameAndPassword() {
    String group = "group2";
    batchDriver.OptionsHash.remove("username");
    batchDriver.OptionsHash.remove("password");
    BatchJob actualBatchJob = batchDriver.getBatchJob(group);
    assertNull(actualBatchJob.getUsername());
    assertNull(actualBatchJob.getPassword());
  }
  
  @Test
  public void testGetBatchJobInvalidNumberOfThreads() {
    String group = "group1";
    batchDriver.OptionsHash.put("threads", "invalid");
    BatchJob actualBatchJob = batchDriver.getBatchJob(group);
    assertNotNull(actualBatchJob);
    assertEquals(1, actualBatchJob.getThreadLimit());
  }
  
  @Test
  public void testGetBatchJobInvalidTimeout() {
    String group = "group1";
    batchDriver.OptionsHash.put("timeout", "invalid");
    BatchJob actualBatchJob = batchDriver.getBatchJob(group);
    assertNotNull(actualBatchJob);
    assertEquals(-1, Newzbatch.TIMEOUTMAX);
  }
  
  @Test
  public void testGetBatchJobInvalidGroupInfo() {
    String group = "group1";
    batchDriver.NewsgroupsHash.clear();
    String expectedMessage = "Could not get server and directory for " + group;
    BatchJob actualBatchJob = batchDriver.getBatchJob(group);
    System.out = new PrintStream(new ByteArrayOutputStream());
    assertNull(actualBatchJob);
    assertEquals(expectedMessage, outContent.toString().trim());
  }
  
  @Test
  public void testGetSubjectLines() {
    String[] expectedSubjectLines = {"subject1", "subject2", "subject3"};
    batchDriver.subjectlines = expectedSubjectLines;
    String[] actualSubjectLines = batchDriver.getSubjectLines();
    assertArrayEquals(expectedSubjectLines, actualSubjectLines);
  }
  
  
  @After
  public void tearDown() {
    newzini = null;
    batchfile = null;
    batchDriver = null;
  }
}