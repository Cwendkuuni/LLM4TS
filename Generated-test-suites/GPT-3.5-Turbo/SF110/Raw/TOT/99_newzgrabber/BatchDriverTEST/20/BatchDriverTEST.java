import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class BatchDriverTest {
    private BatchDriver batchDriver;

    @Before
    public void setUp() throws Exception {
        batchDriver = new BatchDriver("newzini.txt", "batchfile.txt");
    }

    @Test
    public void testGetGroups() {
        String[] expectedGroups = {"group1", "group2", "group3"};

        String[] actualGroups = batchDriver.getGroups();

        assertArrayEquals(expectedGroups, actualGroups);
    }

    @Test
    public void testGetBatchJob_validGroup() {
        String validGroup = "group1";
        BatchJob expectedBatchJob = new BatchJob("server", validGroup, "directory", 0, 10);

        BatchJob actualBatchJob = batchDriver.getBatchJob(validGroup);

        assertEquals(expectedBatchJob, actualBatchJob);
    }

    @Test
    public void testGetBatchJob_invalidGroup() {
        String invalidGroup = "invalidGroup";

        BatchJob actualBatchJob = batchDriver.getBatchJob(invalidGroup);

        assertNull(actualBatchJob);
    }

    @Test
    public void testGetSubjectLines() {
        String[] expectedSubjectLines = {"subject1", "subject2", "subject3"};

        String[] actualSubjectLines = batchDriver.getSubjectLines();

        assertArrayEquals(expectedSubjectLines, actualSubjectLines);
    }
}