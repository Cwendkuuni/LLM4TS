import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import Newzgrabber.BatchDriver;
import Newzgrabber.Ini;
import Newzgrabber.NewsFactory;
import Newzgrabber.NNTP;
import Newzgrabber.Status;
import Newzgrabber.BatchJob;
import java.io.IOException;
import java.util.Hashtable;
import java.util.Vector;

public class BatchDriverTest {
    @Test
    public void testConstructor() {
        String newzini = "testNewzini.ini";
        String batchfile = "testBatchfile.ini";
        try {
            BatchDriver bd = new BatchDriver(newzini, batchfile);
            assertNotNull(bd);
        } catch (NullPointerException | IOException e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testGetGroups() {
        BatchDriver bd = new BatchDriver("testNewzini.ini", "testBatchfile.ini");
        String[] expected = {"group1", "group2", "group3"};
        String[] actual = bd.getGroups();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetBatchJob() {
        BatchDriver bd = new BatchDriver("testNewzini.ini", "testBatchfile.ini");
        String group = "group1";
        BatchJob bj = bd.getBatchJob(group);
        assertNotNull(bj);
        assertEquals(group, bj.getGroup());
    }

    @Test
    public void testGetSubjectLines() {
        BatchDriver bd = new BatchDriver("testNewzini.ini", "testBatchfile.ini");
        String[] expected = {"subject1", "subject2", "subject3"};
        String[] actual = bd.getSubjectLines();
        assertArrayEquals(expected, actual);
    }
}