import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

public class BatchDriverTest {

    private BatchDriver batchDriver;

    @Before
    public void setUp() throws IOException {
        batchDriver = new BatchDriver("newzini", "batchfile");
    }

    @Test
    public void testGetGroups() {
        String[] groups = batchDriver.getGroups();
        assertNotNull(groups);
        assertEquals(0, groups.length);
    }

    @Test
    public void testGetBatchJob() {
        BatchJob batchJob = batchDriver.getBatchJob("group");
        assertNotNull(batchJob);
        assertEquals("server", batchJob.getServer());
        assertEquals("directory", batchJob.getDirectory());
        assertNull(batchJob.getUsername());
        assertNull(batchJob.getPassword());
    }

    @Test
    public void testGetSubjectLines() {
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNotNull(subjectLines);
        assertEquals(0, subjectLines.length);
    }
}