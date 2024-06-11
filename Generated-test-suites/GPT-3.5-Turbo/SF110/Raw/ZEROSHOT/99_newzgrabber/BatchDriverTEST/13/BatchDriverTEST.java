import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BatchDriverTest {

    private BatchDriver batchDriver;

    @Before
    public void setUp() throws Exception {
        batchDriver = new BatchDriver("newzini", "batchfile");
    }

    @After
    public void tearDown() throws Exception {
        batchDriver = null;
    }

    @Test
    public void testGetGroups() throws Exception {
        String[] expected = {"group1", "group2"};
        String[] actual = batchDriver.getGroups();
        assertArrayEquals(expected, actual);
    }

    @Test
    public void testGetBatchJob() throws Exception {
        String group = "group1";
        BatchJob expected = new BatchJob("server", group, "directory", 0, 100);
        BatchJob actual = batchDriver.getBatchJob(group);
        assertEquals(expected.getServer(), actual.getServer());
        assertEquals(expected.getGroup(), actual.getGroup());
        assertEquals(expected.getDirectory(), actual.getDirectory());
        assertEquals(expected.getFirstId(), actual.getFirstId());
        assertEquals(expected.getLastId(), actual.getLastId());
    }

    @Test
    public void testGetSubjectLines() {
        String[] expected = {"subject1", "subject2"};
        batchDriver.setSubjectLines(expected);
        String[] actual = batchDriver.getSubjectLines();
        assertArrayEquals(expected, actual);
    }
}