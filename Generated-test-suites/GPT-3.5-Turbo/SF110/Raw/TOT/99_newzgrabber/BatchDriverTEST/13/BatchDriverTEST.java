import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class BatchDriverTest {

    private BatchDriver batchDriver;

    @Before
    public void setUp() throws NullPointerException, IOException {
        batchDriver = new BatchDriver("newzini", "batchfile");
    }

    @Test
    public void testConstructor() {
        assertNotNull(batchDriver.getGroups());
        assertNotNull(batchDriver.getSubjectLines());
    }

    @Test(expected = NullPointerException.class)
    public void testConstructor_NullNewzini() throws NullPointerException, IOException {
        batchDriver = new BatchDriver(null, "batchfile");
    }

    @Test(expected = IOException.class)
    public void testConstructor_NullBatchfile() throws NullPointerException, IOException {
        batchDriver = new BatchDriver("newzini", null);
    }

    @Test
    public void testGetGroups() {
        // Typical use case
        String[] actual = batchDriver.getGroups();
        String[] expected = {"group1", "group2", "group3"};
        assertArrayEquals(expected, actual);

        // Edge case: Empty batch groups vector
        batchDriver = new BatchDriver("newzini", "emptybatchfile");
        actual = batchDriver.getGroups();
        expected = new String[0];
        assertArrayEquals(expected, actual);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testGetGroups_EmptyBatchgroupsVector() {
        batchDriver.getGroups()[0];
    }

    @Test
    public void testGetBatchJob() {
        // Typical use case
        BatchJob actual = batchDriver.getBatchJob("group1");
        assertNotNull(actual);
        assertEquals("server1", actual.getServer());
        assertEquals("directory1", actual.getDirectory());
        assertEquals(1, actual.getFirstId());
        assertEquals(10, actual.getLastId());

        // Edge case: Non-existing group
        actual = batchDriver.getBatchJob("group4");
        assertNull(actual);

        // Error scenario: Exception caught when getting server and directory
        actual = batchDriver.getBatchJob("group5");
        assertNull(actual);
    }

    @Test
    public void testGetSubjectLines() {
        // Typical use case
        String[] actual = batchDriver.getSubjectLines();
        String[] expected = {"Subject1", "Subject2", "Subject3"};
        assertArrayEquals(expected, actual);

        // Edge case: Empty subject lines array
        batchDriver = new BatchDriver("newzini", "emptybatchfile");
        actual = batchDriver.getSubjectLines();
        assertEquals(0, actual.length);
    }

}