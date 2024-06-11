
import org.junit.*;
import java.io.*;
import java.util.*;

public class BatchDriverTest {

    @Test
    public void testConstructor() throws NullPointerException, IOException {
        BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
        Assert.assertNotNull(batchDriver);
    }

    @Test
    public void testGetGroups() throws ArrayIndexOutOfBoundsException {
        BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
        String[] groups = batchDriver.getGroups();
        Assert.assertNotNull(groups);
    }

    @Test
    public void testGetBatchJob() {
        BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
        BatchJob batchJob = batchDriver.getBatchJob("group");
        Assert.assertNotNull(batchJob);
    }

    @Test
    public void testGetSubjectLines() throws ArrayIndexOutOfBoundsException {
        BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
        String[] subjectLines = batchDriver.getSubjectLines();
        Assert.assertNotNull(subjectLines);
    }

}
