import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.*;
import java.util.*;

public class BatchDriverTest {

    private BatchDriver batchDriver;

    @Before
    public void setUp() {
        batchDriver = new BatchDriver("newzini", "batchfile");
    }

    @Test
    public void testGetGroups() {
        String[] expectedGroups = {"Group1", "Group2", "Group3"};
        assertArrayEquals(expectedGroups, batchDriver.getGroups());
    }

    @Test
    public void testGetBatchJob() {
        BatchJob batchJob = batchDriver.getBatchJob("Group1");
        assertNotNull(batchJob);
        assertEquals("server1", batchJob.getServer());
        assertEquals("dir1", batchJob.getDirectory());
        assertEquals(1, batchJob.getnThreads());
        assertEquals(-1, batchJob.getTIMEOUT());
    }

    @Test
    public void testGetBatchJob_ExceptionHandling() {
        BatchJob batchJob = batchDriver.getBatchJob("NonExistingGroup");
        assertNull(batchJob);
    }

    @Test
    public void testGetSubjectLines() {
        String[] expectedSubjectLines = {"Subject1", "Subject2", "Subject3"};
        assertArrayEquals(expectedSubjectLines, batchDriver.getSubjectLines());
    }

}