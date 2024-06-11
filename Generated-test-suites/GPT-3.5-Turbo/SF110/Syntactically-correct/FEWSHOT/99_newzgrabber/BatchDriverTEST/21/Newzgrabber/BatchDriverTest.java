package Newzgrabber;

import static org.junit.Assert.*;

import org.junit.Test;

public class BatchDriverTest {

    @Test
    public void testConstructor() {
        try {
            BatchDriver batchDriver = new BatchDriver(null, null);
            assertNotNull(batchDriver);
        } catch (NullPointerException e) {
            fail("Null pointer exception occurred");
        } catch (IOException e) {
            fail("IO exception occurred");
        }
    }

    @Test
    public void testGetGroups() {
        BatchDriver batchDriver = new BatchDriver(null, null);
        try {
            String[] groups = batchDriver.getGroups();
            assertNotNull(groups);
        } catch (ArrayIndexOutOfBoundsException e) {
            fail("Array index out of bounds exception occurred");
        }
    }

    @Test
    public void testGetBatchJob() {
        BatchDriver batchDriver = new BatchDriver(null, null);
        BatchJob batchJob = batchDriver.getBatchJob("group");
        assertNotNull(batchJob);
    }

    @Test
    public void testGetSubjectLines() {
        BatchDriver batchDriver = new BatchDriver(null, null);
        String[] subjectLines = batchDriver.getSubjectLines();
        assertNotNull(subjectLines);
    }
}