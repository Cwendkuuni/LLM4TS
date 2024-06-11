package Newzgrabber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class BatchDriverTest {
    
    private BatchDriver batchDriver;
    
    @Before
    public void setup() {
        try {
            batchDriver = new BatchDriver("newzini", "batchfile");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorThrowsNullPointerException() throws NullPointerException, IOException {
        BatchDriver bd = new BatchDriver(null, null);
        assertNull(bd);
    }

    @Test
    public void testGetGroups() {
        String[] expectedGroups = {"group1", "group2", "group3"};
        String[] actualGroups = batchDriver.getGroups();
        assertEquals(expectedGroups.length, actualGroups.length);
        for(int i = 0; i < expectedGroups.length; i++) {
            assertEquals(expectedGroups[i], actualGroups[i]);
        }
    }

    @Test
    public void testGetBatchJob() {
        String group = "group1";
        BatchJob batchJob = batchDriver.getBatchJob(group);
        assertNotNull(batchJob);
        assertEquals(batchJob.getServer(), "server");
        assertEquals(batchJob.getDirectory(), "directory");
        assertEquals(batchJob.getUsername(), "username");
        assertEquals(batchJob.getPassword(), "password");
        assertEquals(batchJob.getThreads(), 1);
        assertEquals(batchJob.getTimeout(), -1);
        assertTrue(batchJob.getNews() != null);
        assertEquals(batchJob.getGroup(), group);
        assertEquals(batchJob.getFirstId(), 0);
        assertEquals(batchJob.getLastId(), 100);
    }

    @Test
    public void testGetSubjectLines() {
        String[] expectedSubjectLines = {"subject1", "subject2", "subject3"};
        String[] actualSubjectLines = batchDriver.getSubjectLines();
        assertEquals(expectedSubjectLines.length, actualSubjectLines.length);
        for(int i = 0; i < expectedSubjectLines.length; i++) {
            assertEquals(expectedSubjectLines[i], actualSubjectLines[i]);
        }
    }

}