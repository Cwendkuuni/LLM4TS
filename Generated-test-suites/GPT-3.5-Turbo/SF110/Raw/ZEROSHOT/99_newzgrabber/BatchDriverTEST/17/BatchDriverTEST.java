package Newzgrabber;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BatchDriverTest {
   private BatchDriver batchDriver;

   @Before
   public void setUp() throws NullPointerException, IOException {
      batchDriver = new BatchDriver("newzini", "batchfile");
   }

   @Test
   public void testGetGroups() {
      String[] expected = { "group1", "group2", "group3" };
      String[] actual = batchDriver.getGroups();
      Assert.assertArrayEquals(expected, actual);
   }

   @Test
   public void testGetBatchJob() {
      String group = "group1";
      BatchJob batchJob = batchDriver.getBatchJob(group);
      Assert.assertNotNull(batchJob);
   }

   @Test
   public void testGetSubjectLines() {
      String[] expected = { "subject1", "subject2", "subject3" };
      String[] actual = batchDriver.getSubjectLines();
      Assert.assertArrayEquals(expected, actual);
   }
}