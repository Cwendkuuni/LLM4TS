package Newzgrabber;

import org.junit.Test;
import static org.junit.Assert.*;

public class BatchDriverTest {

    @Test
    public void testBatchDriverConstructor() {
        try {
            BatchDriver batchDriver = new BatchDriver("newzini_file.txt", "batchfile.txt");
            assertNotNull(batchDriver);
        } catch (NullPointerException | IOException e) {
            fail("Constructor should not throw exceptions");
        }
    }

    @Test
    public void testGetGroups() {
        BatchDriver batchDriver = new BatchDriver("newzini_file.txt", "batchfile.txt");
        String[] groups = batchDriver.getGroups();
        assertNotNull(groups);
        assertEquals(0, groups.length);
    }

    @Test
    public void testGetBatchJob() {
        BatchDriver batchDriver = new BatchDriver("newzini_file.txt", "batchfile.txt");
        String group = "testGroup";
        BatchJob batchJob = batchDriver.getBatchJob(group);
        assertNotNull(batchJob);
    }

    @Test
    public void testGetSubjectLines() {
        BatchDriver batchDriver = new BatchDriver("newzini_file.txt", "batchfile.txt");
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNull(subjectLines);
    }
}