package Newzgrabber;

import org.junit.Test;
import org.junit.Assert;
import java.io.IOException;

public class BatchDriverTest {

    @Test
    public void testConstructor() {
        try {
            BatchDriver batchDriver = new BatchDriver("test.ini", "testBatchFile.ini");
            Assert.assertNotNull(batchDriver);
        } catch (IOException e) {
            Assert.fail("IOException thrown during constructor test");
        }
    }

    @Test
    public void testGetGroups() {
        BatchDriver batchDriver = new BatchDriver("test.ini", "testBatchFile.ini");
        String[] groups = batchDriver.getGroups();
        Assert.assertNotNull(groups);
    }

    @Test
    public void testGetBatchJob() {
        BatchDriver batchDriver = new BatchDriver("test.ini", "testBatchFile.ini");
        BatchJob batchJob = batchDriver.getBatchJob("testGroup");
        Assert.assertNotNull(batchJob);
    }

    @Test
    public void testGetSubjectLines() {
        BatchDriver batchDriver = new BatchDriver("test.ini", "testBatchFile.ini");
        String[] subjectLines = batchDriver.getSubjectLines();
        Assert.assertNotNull(subjectLines);
    }
}