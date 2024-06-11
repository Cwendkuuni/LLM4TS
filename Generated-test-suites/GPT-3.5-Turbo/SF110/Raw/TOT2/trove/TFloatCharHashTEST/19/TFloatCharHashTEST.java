import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

    @Test
    public void testConstructor() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.01);
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(5.0f);
        assertTrue(hash.contains(5.0f));
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(10.0f);
        hash.removeAt(0);
        assertFalse(hash.contains(10.0f));
    }
    
    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(15.0f);
        StringBuilder sb = new StringBuilder();
        assertTrue(hash.forEach(val -> {
            sb.append(val);
            return true;
        }));
        assertEquals("15.0", sb.toString());
    }

    @Test
    public void testInsertEdgeCase() {
        TFloatCharHash hash = new TFloatCharHash(1, 0.5f, 0.0f, '\0');
        hash.insertKey(0.0f);
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgument() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.removeAt(5);
    }

    @Test
    public void testIOException() {
        TFloatCharHash hash = new TFloatCharHash();
        try {
            hash.writeExternal(null);
        } catch (Exception e) {
            fail("IOException not expected");
        }
    }
}