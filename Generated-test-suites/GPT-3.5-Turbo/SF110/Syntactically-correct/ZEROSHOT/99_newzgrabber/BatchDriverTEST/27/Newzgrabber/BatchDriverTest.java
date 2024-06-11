package Newzgrabber;

import org.junit.Test;
import static org.junit.Assert.*;

public class BatchDriverTest {

    @Test
    public void testGetGroups() {
        BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
        String[] expectedGroups = {"group1", "group2", "group3"};
        assertArrayEquals(expectedGroups, batchDriver.getGroups());
    }

    @Test
    public void testGetBatchJob() {
        BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
        BatchJob batchJob = batchDriver.getBatchJob("group1");
        assertNotNull(batchJob);
    }

    @Test
    public void testGetSubjectLines() {
        BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
        String[] expectedSubjectLines = {"subject1", "subject2", "subject3"};
        assertArrayEquals(expectedSubjectLines, batchDriver.getSubjectLines());
    }
}