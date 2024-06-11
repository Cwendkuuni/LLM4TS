import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

public class BatchDriverTest {

    @Test(expected = NullPointerException.class)
    public void testConstructorNullPointerException() throws NullPointerException, IOException {
        BatchDriver batchDriver = new BatchDriver(null, null);
    }

    @Test(expected = IOException.class)
    public void testConstructorIOException() throws NullPointerException, IOException {
        BatchDriver batchDriver = new BatchDriver("newzini.ini", "batchfile.ini");
    }

    @Test
    public void testGetGroups() throws ArrayIndexOutOfBoundsException {
        BatchDriver batchDriver = new BatchDriver("newzini.ini", "batchfile.ini");
        String[] groups = batchDriver.getGroups();
        assertNotNull(groups);
    }

    @Test
    public void testGetBatchJob() {
        BatchDriver batchDriver = new BatchDriver("newzini.ini", "batchfile.ini");
        BatchJob batchJob = batchDriver.getBatchJob("group1");
        assertNotNull(batchJob);
    }

    @Test
    public void testGetSubjectLines() {
        BatchDriver batchDriver = new BatchDriver("newzini.ini", "batchfile.ini");
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNotNull(subjectLines);
    }
}