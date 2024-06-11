package Newzgrabber;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

import static org.junit.Assert.*;

public class BatchDriverTest {

    private BatchDriver batchDriver;

    @Before
    public void setUp() throws Exception {
        batchDriver = new BatchDriver("newzini.txt", "batchfile.txt");
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_NullNewziniFile_ThrowsNullPointerException() throws IOException {
        batchDriver = new BatchDriver(null, "batchfile.txt");
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_NullBatchFile_ThrowsNullPointerException() throws IOException {
        batchDriver = new BatchDriver("newzini.txt", null);
    }

    @Test
    public void testConstructor_ValidNewziniAndBatchFiles_DoesNotThrowExceptions() throws IOException {
        batchDriver = new BatchDriver("newzini.txt", "batchfile.txt");
    }

    @Test
    public void testGetGroups() {
        String[] groups = batchDriver.getGroups();
        assertNotNull(groups);
        assertEquals(3, groups.length);
        assertEquals("Group1", groups[0]);
        assertEquals("Group2", groups[1]);
        assertEquals("Group3", groups[2]);
    }

    @Test
    public void testGetBatchJob_ValidGroup_ReturnsNonNullBatchJob() {
        BatchJob batchJob = batchDriver.getBatchJob("Group1");
        assertNotNull(batchJob);
    }

    @Test
    public void testGetBatchJob_InvalidGroup_ReturnsNullBatchJob() {
        BatchJob batchJob = batchDriver.getBatchJob("InvalidGroup");
        assertNull(batchJob);
    }

    @Test
    public void testGetSubjectLines_ReturnsNonNullSubjectLines() {
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNotNull(subjectLines);
    }
}