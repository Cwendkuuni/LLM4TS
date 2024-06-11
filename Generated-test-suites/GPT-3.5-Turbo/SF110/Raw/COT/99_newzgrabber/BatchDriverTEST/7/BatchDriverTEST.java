import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.util.*;

public class BatchDriverTest {
    
    @Test
    public void testConstructor() throws NullPointerException, IOException {
        BatchDriver bd = new BatchDriver("newzini", "batchfile");
        assertNotNull(bd);
    }

    @Test
    public void testGetGroups() throws ArrayIndexOutOfBoundsException {
        BatchDriver bd = new BatchDriver("newzini", "batchfile");
        String[] groups = bd.getGroups();
        assertNotNull(groups);
    }

    @Test
    public void testGetBatchJob() {
        BatchDriver bd = new BatchDriver("newzini", "batchfile");
        BatchJob bj = bd.getBatchJob("group1");
        assertNotNull(bj);
    }

    @Test
    public void testGetSubjectLines() {
        BatchDriver bd = new BatchDriver("newzini", "batchfile");
        String[] subjectLines = bd.getSubjectLines();
        assertNotNull(subjectLines);
    }

}