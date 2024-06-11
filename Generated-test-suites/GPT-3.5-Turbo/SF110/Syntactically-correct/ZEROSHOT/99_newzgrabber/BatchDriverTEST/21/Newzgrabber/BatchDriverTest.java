package Newzgrabber;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

public class BatchDriverTest {
    private BatchDriver batchDriver;
    private final String newzini = "path/to/newzini";
    private final String batchfile = "path/to/batchfile";

    @Before
    public void setUp() throws NullPointerException, IOException {
        batchDriver = new BatchDriver(newzini, batchfile);
    }

    @After
    public void tearDown() {
        batchDriver = null;
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_NullNewzini_ThrowsNullPointerException() throws IOException {
        new BatchDriver(null, batchfile);
    }

    @Test(expected = IOException.class)
    public void testConstructor_NullBatchFile_ThrowsIOException() throws NullPointerException, IOException {
        new BatchDriver(newzini, null);
    }

    @Test
    public void testGetGroups_ReturnsCorrectGroups() {
        String[] expectedGroups = {"group1", "group2", "group3"};
        String[] actualGroups = batchDriver.getGroups();
        Assert.assertArrayEquals(expectedGroups, actualGroups);
    }

    @Test
    public void testGetBatchJob_ReturnsValidBatchJob() {
        String groupName = "group1";
        BatchJob batchJob = batchDriver.getBatchJob(groupName);
        Assert.assertNotNull(batchJob);
        Assert.assertEquals(groupName, batchJob.getGroup());
    }

    @Test
    public void testGetBatchJob_InvalidGroup_ReturnsNull() {
        String invalidGroupName = "invalidGroup";
        BatchJob batchJob = batchDriver.getBatchJob(invalidGroupName);
        Assert.assertNull(batchJob);
    }

    @Test
    public void testGetSubjectLines_ReturnsValidSubjectLines() {
        String[] expectedSubjectLines = {"subject1", "subject2", "subject3"};
        String[] actualSubjectLines = batchDriver.getSubjectLines();
        Assert.assertArrayEquals(expectedSubjectLines, actualSubjectLines);
    }
}