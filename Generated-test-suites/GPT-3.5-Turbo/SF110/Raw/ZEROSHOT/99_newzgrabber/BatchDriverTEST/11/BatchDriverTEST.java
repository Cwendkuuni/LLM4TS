package Newzgrabber;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

public class BatchDriverTest {

    @Test
    public void testConstructor() {
        try {
            BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
            assertNotNull(batchDriver);
        } catch (NullPointerException | IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testGetGroups() {
        try {
            BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
            String[] groups = batchDriver.getGroups();
            assertNotNull(groups);
        } catch (ArrayIndexOutOfBoundsException e) {
            fail("Exception thrown");
        } catch (NullPointerException | IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testGetBatchJob() {
        try {
            BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
            BatchJob batchJob = batchDriver.getBatchJob("group");
            assertNotNull(batchJob);
        } catch (NullPointerException | IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testGetSubjectLines() {
        try {
            BatchDriver batchDriver = new BatchDriver("newzini", "batchfile");
            String[] subjectLines = batchDriver.getSubjectLines();
            assertNotNull(subjectLines);
        } catch (NullPointerException | IOException e) {
            fail("Exception thrown");
        }
    }
}