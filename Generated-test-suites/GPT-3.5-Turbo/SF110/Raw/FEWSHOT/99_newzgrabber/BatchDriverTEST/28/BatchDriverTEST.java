
package Newzgrabber;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

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
        String[] expected = { "group1", "group2", "group3" };
        assertArrayEquals(expected, batchDriver.getGroups());
    }

    @Test
    public void testGetBatchJob() {
        BatchJob expected = new BatchJob("server", "group1", "directory", 0, 10);
        assertEquals(expected, batchDriver.getBatchJob("group1"));
        assertNull(batchDriver.getBatchJob("invalidGroup"));
    }

    @Test
    public void testGetSubjectLines() {
        String[] expected = { "subject1", "subject2", "subject3" };
        assertArrayEquals(expected, batchDriver.getSubjectLines());
    }

}
