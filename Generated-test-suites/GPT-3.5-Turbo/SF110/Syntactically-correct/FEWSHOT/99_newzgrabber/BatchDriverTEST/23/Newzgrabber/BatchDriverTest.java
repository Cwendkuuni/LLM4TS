
package Newzgrabber;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class BatchDriverTest {
    private BatchDriver batchDriver;
    
    @Before
    public void setUp() throws NullPointerException, IOException {
        batchDriver = new BatchDriver("newz.ini", "batchfile.ini");
    }
    
    @Test
    public void testGetGroups() {
        String[] expectedGroups = {"group1", "group2", "group3"};
        String[] actualGroups = batchDriver.getGroups();
        assertNotNull(actualGroups);
        assertEquals(expectedGroups.length, actualGroups.length);
        for (int i = 0; i < expectedGroups.length; i++) {
            assertEquals(expectedGroups[i], actualGroups[i]);
        }
    }
    
    @Test
    public void testGetBatchJob() {
        BatchJob batchJob = batchDriver.getBatchJob("group1");
        assertNotNull(batchJob);
        assertEquals("server1", batchJob.getServer());
        assertEquals("directory1", batchJob.getDirectory());
        assertEquals(1, batchJob.getThreads());
        assertEquals(-1, batchJob.getTimeout());
    }
    
    @Test
    public void testGetSubjectLines() {
        String[] expectedSubjectLines = {"subject1", "subject2", "subject3"};
        String[] actualSubjectLines = batchDriver.getSubjectLines();
        assertNotNull(actualSubjectLines);
        assertEquals(expectedSubjectLines.length, actualSubjectLines.length);
        for (int i = 0; i < expectedSubjectLines.length; i++) {
            assertEquals(expectedSubjectLines[i], actualSubjectLines[i]);
        }
    }
}
