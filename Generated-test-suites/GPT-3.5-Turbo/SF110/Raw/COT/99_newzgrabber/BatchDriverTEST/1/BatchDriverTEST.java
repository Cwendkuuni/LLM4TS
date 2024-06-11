import org.junit.*;
import static org.junit.Assert.*;

public class BatchDriverTest {

  @Test
  public void testBatchDriver() {
    try {
      BatchDriver bd = new BatchDriver("newzini.txt", "batchfile.txt");
      assertNotNull(bd);
    } catch (Exception e) {
      fail("Failed to create BatchDriver object");
    }
  }

  @Test
  public void testGetGroups() {
    try {
      BatchDriver bd = new BatchDriver("newzini.txt", "batchfile.txt");
      String[] groups = bd.getGroups();
      assertNotNull(groups);
      assertEquals(2, groups.length);
      assertEquals("group1", groups[0]);
      assertEquals("group2", groups[1]);
    } catch (Exception e) {
      fail("Failed to get groups");
    }
  }

  @Test
  public void testGetBatchJob() {
    try {
      BatchDriver bd = new BatchDriver("newzini.txt", "batchfile.txt");
      BatchJob bj = bd.getBatchJob("group1");
      assertNotNull(bj);
      assertEquals("server1", bj.getServer());
      assertEquals("directory1", bj.getDirectory());
      assertEquals(1, bj.getFirstId());
      assertEquals(100, bj.getLastId());
    } catch (Exception e) {
      fail("Failed to get batch job for group1");
    }
  }

  @Test
  public void testGetSubjectLines() {
    try {
      BatchDriver bd = new BatchDriver("newzini.txt", "batchfile.txt");
      BatchJob bj = bd.getBatchJob("group1");
      String[] subjectLines = bd.getSubjectLines();
      assertNotNull(subjectLines);
      assertEquals(5, subjectLines.length);
      assertEquals("Subject line 1", subjectLines[0]);
      assertEquals("Subject line 2", subjectLines[1]);
      assertEquals("Subject line 3", subjectLines[2]);
      assertEquals("Subject line 4", subjectLines[3]);
      assertEquals("Subject line 5", subjectLines[4]);
    } catch (Exception e) {
      fail("Failed to get subject lines");
    }
  }

}