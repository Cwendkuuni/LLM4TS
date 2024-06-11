import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

public class BatchDriverTest {

    @Test
    public void testConstructor() {
        try {
            BatchDriver batchDriver = new BatchDriver("newzini.txt", "batchfile.ini");
            assertNotNull(batchDriver);
        } catch (NullPointerException | IOException e) {
            fail("Constructor should not throw exceptions");
        }
    }

    @Test
    public void testGetGroups() {
        BatchDriver batchDriver = new BatchDriver("newzini.txt", "batchfile.ini");
        String[] groups = batchDriver.getGroups();
        assertNotNull(groups);
        assertEquals(0, groups.length);
    }

    @Test
    public void testGetBatchJob() {
        BatchDriver batchDriver = new BatchDriver("newzini.txt", "batchfile.ini");
        assertNull(batchDriver.getBatchJob("NonExistingGroup"));
    }

    @Test
    public void testGetSubjectLines() {
        BatchDriver batchDriver = new BatchDriver("newzini.txt", "batchfile.ini");
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNull(subjectLines);
    }
}