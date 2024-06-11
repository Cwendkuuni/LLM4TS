
package Newzgrabber;

import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class BatchDriverTest {

    @Test
    public void testConstructor() throws IOException {
        BatchDriver bd = new BatchDriver("newzini", "batchfile");
        Assert.assertNotNull(bd);
    }

    @Test
    public void testGetGroups() throws ArrayIndexOutOfBoundsException {
        BatchDriver bd = new BatchDriver("newzini", "batchfile");
        String[] groups = bd.getGroups();
        Assert.assertNotNull(groups);
    }

    @Test
    public void testGetBatchJob() {
        BatchDriver bd = new BatchDriver("newzini", "batchfile");
        BatchJob bj = bd.getBatchJob("group1");
        Assert.assertNotNull(bj);
    }

    @Test
    public void testGetSubjectLines() {
        BatchDriver bd = new BatchDriver("newzini", "batchfile");
        String[] subjectLines = bd.getSubjectLines();
        Assert.assertNotNull(subjectLines);
    }

}
