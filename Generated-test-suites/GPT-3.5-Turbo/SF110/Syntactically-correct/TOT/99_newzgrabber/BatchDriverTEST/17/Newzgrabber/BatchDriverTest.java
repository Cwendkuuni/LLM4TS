import org.junit.Test;
import static org.junit.Assert.*;

public class BatchDriverTest {
   
   @Test
   public void testConstructor_NullPointerExcepion() {
      try {
         BatchDriver batchdriver = new BatchDriver(null, null);
         fail("NullPointerException not thrown");
      } catch (NullPointerException e) {
         // Passed
      } catch (Exception e) {
         fail("Wrong exception thrown");
      }
   }
   
   @Test
   public void testConstructor_IOException() {
      try {
         BatchDriver batchdriver = new BatchDriver("newzini", "batchfile");
         fail("IOException not thrown");
      } catch (IOException e) {
         // Passed
      } catch (Exception e) {
         fail("Wrong exception thrown");
      }
   }
   
   @Test
   public void testGetGroups() {
      BatchDriver batchdriver = new BatchDriver("newzini", "batchfile");
      String[] groups = batchdriver.getGroups();
      
      // Check if number of groups is correct
      assertEquals(groups.length, batchdriver.BatchgroupsVector.size());
      
      // Check if each group is correct
      for (int i = 0; i < groups.length; i++) {
         assertEquals(groups[i], batchdriver.BatchgroupsVector.elementAt(i)[0]);
      }
   }
   
   @Test
   public void testGetBatchJob_InvalidGroup() {
      BatchDriver batchdriver = new BatchDriver("newzini", "batchfile");
      BatchJob job = batchdriver.getBatchJob("invalid_group");
      assertNull(job);
   }
   
   @Test
   public void testGetBatchJob_NullServerAndDirectory() {
      BatchDriver batchdriver = new BatchDriver("newzini", "batchfile");
      BatchJob job = batchdriver.getBatchJob("group1");
      assertNotNull(job);
      assertNull(job.getServer());
      assertNull(job.getDirectory());
   }
   
   @Test
   public void testGetBatchJob_ValidServerAndDirectory() {
      BatchDriver batchdriver = new BatchDriver("newzini", "batchfile");
      BatchJob job = batchdriver.getBatchJob("group2");
      assertNotNull(job);
      assertEquals("server_value", job.getServer());
      assertEquals("directory_value", job.getDirectory());
   }
   
   @Test
   public void testGetBatchJob_NullUsernameAndPassword() {
      BatchDriver batchdriver = new BatchDriver("newzini", "batchfile");
      BatchJob job = batchdriver.getBatchJob("group1");
      assertNotNull(job);
      assertNull(job.getUsername());
      assertNull(job.getPassword());
   }
   
   @Test
   public void testGetBatchJob_ValidUsernameAndPassword() {
      BatchDriver batchdriver = new BatchDriver("newzini", "batchfile");
      BatchJob job = batchdriver.getBatchJob("group3");
      assertNotNull(job);
      assertEquals("username_value", job.getUsername());
      assertEquals("password_value", job.getPassword());
   }
   
   @Test
   public void testGetBatchJob_InvalidThreadsValue() {
      BatchDriver batchdriver = new BatchDriver("newzini", "batchfile");
      BatchJob job = batchdriver.getBatchJob("group1");
      assertNotNull(job);
      assertEquals(1, job.getNThreads());
   }
   
   @Test
   public void testGetBatchJob_ValidThreadsValue() {
      BatchDriver batchdriver = new BatchDriver("newzini", "batchfile");
      BatchJob job = batchdriver.getBatchJob("group4");
      assertNotNull(job);
      assertEquals(5, job.getNThreads());
   }
   
   @Test
   public void testGetBatchJob_InvalidTimeoutValue() {
      BatchDriver batchdriver = new BatchDriver("newzini", "batchfile");
      BatchJob job = batchdriver.getBatchJob("group1");
      assertNotNull(job);
      assertFalse(job.isTimeoutEnabled());
      assertEquals(-1, job.getTimeoutMax());
   }
   
   @Test
   public void testGetBatchJob_ValidTimeoutValue() {
      BatchDriver batchdriver = new BatchDriver("newzini", "batchfile");
      BatchJob job = batchdriver.getBatchJob("group5");
      assertNotNull(job);
      assertTrue(job.isTimeoutEnabled());
      assertEquals(1000, job.getTimeoutMax());
   }
   
   @Test
   public void testGetBatchJob_NewsSocketIsNull() {
      BatchDriver batchdriver = new BatchDriver("newzini", "batchfile");
      BatchJob job = batchdriver.getBatchJob("group6");
      assertNull(job);
   }
   
   @Test
   public void testGetBatchJob_NewsSocketIsNotNull() {
      BatchDriver batchdriver = new BatchDriver("newzini", "batchfile");
      BatchJob job = batchdriver.getBatchJob("group3");
      assertNotNull(job);
      assertNotNull(job.getNews());
   }
   
   @Test
   public void testGetBatchJob_StatusIsNotOK() {
      BatchDriver batchdriver = new BatchDriver("newzini", "batchfile");
      BatchJob job = batchdriver.getBatchJob("group7");
      assertNull(job);
   }
   
   @Test
   public void testGetBatchJob_GroupHasNoIdInIniFile() {
      BatchDriver batchdriver = new BatchDriver("newzini", "batchfile");
      BatchJob job = batchdriver.getBatchJob("group8");
      assertNotNull(job);
      assertEquals(0, job.getFirstId());
   }
   
   @Test
   public void testGetBatchJob_GroupHasIdInIniFile() {
      BatchDriver batchdriver = new BatchDriver("newzini", "batchfile");
      BatchJob job = batchdriver.getBatchJob("group9");
      assertNotNull(job);
      assertEquals(123, job.getFirstId());
   }
   
   @Test
   public void testGetSubjectLines() {
      BatchDriver batchdriver = new BatchDriver("newzini", "batchfile");
      String[] subjectlines = batchdriver.getSubjectLines();
      assertNotNull(subjectlines);
      assertEquals(subjectlines, batchdriver.subjectlines);
   }
}