package Newzgrabber;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import java.io.IOException;

public class BatchDriverTest {

    BatchDriver batchDriver;

    @Before
    public void setUp() throws IOException {
        batchDriver = new BatchDriver("newzini.txt", "batchfile.txt");
    }

    @Test
    public void testGetGroups() {
        String[] groups = batchDriver.getGroups();
        assertNotNull(groups);
        assertEquals(0, groups.length);
    }

    @Test
    public void testGetBatchJob() {
        BatchJob batchJob = batchDriver.getBatchJob("testgroup");
        assertNull(batchJob);
    }

    @Test
    public void testGetSubjectLines() {
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNull(subjectLines);
    }
}