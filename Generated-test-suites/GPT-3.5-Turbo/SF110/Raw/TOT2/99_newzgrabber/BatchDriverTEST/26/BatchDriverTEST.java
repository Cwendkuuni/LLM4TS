import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;

public class BatchDriverTest {
  
  @Test
  public void testConstructor() {
    try {
      BatchDriver batchDriver = new BatchDriver("groups.ini", "batch.ini");
      assertNotNull(batchDriver);
    } catch (Exception e) {
      fail("Constructor test failed");
    }
  }
  
  @Test
  public void testGetGroups() {
    BatchDriver batchDriver = new BatchDriver("groups.ini", "batch.ini");
    String[] groups = batchDriver.getGroups();
    assertNotNull(groups);
  }
  
  @Test
  public void testGetBatchJob() {
    BatchDriver batchDriver = new BatchDriver("groups.ini", "batch.ini");
    String group = "testGroup";
    BatchJob batchJob = batchDriver.getBatchJob(group);
    assertNotNull(batchJob);
  }
  
  @Test
  public void testGetSubjectLines() {
    BatchDriver batchDriver = new BatchDriver("groups.ini", "batch.ini");
    String[] subjectLines = batchDriver.getSubjectLines();
    assertNotNull(subjectLines);
  }

}