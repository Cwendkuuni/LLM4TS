import org.junit.Before;
import org.junit.After;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    private TFloatDoubleHash tFloatDoubleHash;

    @Before
    public void setUp() {
        tFloatDoubleHash = new TFloatDoubleHash();
    }

    @After
    public void tearDown() {
        tFloatDoubleHash = null;
    }

    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, tFloatDoubleHash.getNoEntryKey(), 0.0);
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals(0.0, tFloatDoubleHash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testSetUp() {
        tFloatDoubleHash.setUp(10);
        assertNotNull(tFloatDoubleHash._set);
    }

    @Test
    public void testContains() {
        assertFalse(tFloatDoubleHash.contains(5.0f));
    }

    @Test
    public void testInsertKey() {
        tFloatDoubleHash.setUp(5);
        assertEquals(0, tFloatDoubleHash.insertKey(5.0f));
    }

    @Test
    public void testRemoveAt() {
        tFloatDoubleHash.removeAt(2);
        assertEquals(0.0f, tFloatDoubleHash._set[2], 0.0);
    }

    @Test
    public void testIndex() {
        tFloatDoubleHash.setUp(5);
        tFloatDoubleHash._set[1] = 3.0f;
        assertEquals(1, tFloatDoubleHash.index(3.0f));
    }

    // Add more test cases as needed

}