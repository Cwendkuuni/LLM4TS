import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {
    
    @Test
    public void testConstructor() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        assertNotNull(tFloatDoubleHash);
    }
    
    @Test
    public void testGetNoEntryKey() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        assertEquals(0.0f, tFloatDoubleHash.getNoEntryKey(), 0.0001);
    }
    
    @Test
    public void testGetNoEntryValue() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        assertEquals(0.0, tFloatDoubleHash.getNoEntryValue(), 0.0001);
    }
    
    @Test
    public void testContains() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        assertFalse(tFloatDoubleHash.contains(5.0f));
    }
    
    @Test
    public void testRemoveAt() {
        TFloatDoubleHash tFloatDoubleHash = new TFloatDoubleHash();
        tFloatDoubleHash.setUp(10);
        tFloatDoubleHash.removeAt(5);
        assertTrue(tFloatDoubleHash._set[5] == 0.0f);
    }
    
    // Add more test cases for other methods
    
}