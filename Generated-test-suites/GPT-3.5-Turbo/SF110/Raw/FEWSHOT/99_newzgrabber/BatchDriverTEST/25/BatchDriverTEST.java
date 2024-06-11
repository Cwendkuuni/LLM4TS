
package Newzgrabber;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class BatchDriverTest {

    private BatchDriver batchDriver;

    @Before
    public void setUp() {
        try {
            batchDriver = new BatchDriver("newzini", "batchfile");
        } catch (NullPointerException | IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetGroups() {
        String[] expected = {"group1", "group2", "group3"};
        assertArrayEquals(expected, batchDriver.getGroups());
    }

    @Test
    public void testGetBatchJob() {
        BatchJob batchJob = batchDriver.getBatchJob("group1");
        assertNotNull(batchJob);
        assertEquals("server1", batchJob.getServer());
        assertEquals("directory1", batchJob.getDirectory());
        assertEquals("username1", batchJob.getUsername());
        assertEquals("password1", batchJob.getPassword());
        assertEquals(3, batchJob.getNThreads());
        assertEquals(10, batchJob.getTIMEOUTMAX());
        assertEquals(1, batchJob.getFirstId());
        assertEquals(100, batchJob.getLastId());
    }

    @Test
    public void testGetSubjectLines() {
        String[] expected = {"subject1", "subject2", "subject3"};
        assertArrayEquals(expected, batchDriver.getSubjectLines());
    }
}
