import org.junit.*;
import static org.junit.Assert.*;

public class TShortHashTest {
    
    @Test
    public void testGetNoEntryValue() {
        TShortHash hash = new TShortHashImpl();
        short noEntryValue = hash.getNoEntryValue();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, noEntryValue);
    }
    
    @Test
    public void testSetUp() {
        TShortHash hash = new TShortHashImpl();
        int capacity = hash.setUp(10);
        assertEquals(10, capacity);
        short[] set = ((TShortHashImpl)hash)._set;
        assertEquals(10, set.length);
    }
    
    @Test
    public void testContains() {
        TShortHash hash = new TShortHashImpl();
        
        boolean contains = hash.contains((short)1);
        assertFalse(contains);
    }
    
    @Test
    public void testForEach() {
        TShortHash hash = new TShortHashImpl();
        
        TShortProcedure procedure = new TShortProcedureImpl();
        boolean result = hash.forEach(procedure);
        assertTrue(result);
    }
    
    @Test
    public void testRemoveAt() {
        TShortHash hash = new TShortHashImpl();
        
        ((TShortHashImpl)hash)._set[0] = (short)1;
        ((TShortHashImpl)hash)._states[0] = 1;
        
        hash.removeAt(0);
        
        short[] set = ((TShortHashImpl)hash)._set;
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, set[0]);
    }
    
    @Test
    public void testIndex() {
        TShortHash hash = new TShortHashImpl();
        
        int index = hash.index((short)1);
        assertEquals(-1, index);
    }
    
    @Test
    public void testIndexRehashed() {
        TShortHash hash = new TShortHashImpl();
        
        int result = hash.indexRehashed((short)1, 0, 0, (byte)0);
        assertEquals(-1, result);
    }
    
    @Test
    public void testInsertKey() {
        TShortHash hash = new TShortHashImpl();
        
        int index = hash.insertKey((short)1);
        assertEquals(0, index);
    }
    
    @Test
    public void testInsertKeyRehash() {
        TShortHash hash = new TShortHashImpl();
        
        int result = hash.insertKeyRehash((short)1, 0, 0, (byte)0);
        assertEquals(-1, result);
    }
    
    @Test
    public void testInsertKeyAt() {
        TShortHash hash = new TShortHashImpl();
        
        hash.insertKeyAt(0, (short)1);
        
        short[] set = ((TShortHashImpl)hash)._set;
        assertEquals(1, set[0]);
        
        byte[] states = ((TShortHashImpl)hash)._states;
        assertEquals(1, states[0]);
    }
}

class TShortHashImpl extends TShortHash {
    public TShortHashImpl() {
        super();
    }
    
    public TShortHashImpl(int initialCapacity) {
        super(initialCapacity);
    }
    
    public TShortHashImpl(int initialCapacity, float loadFactor) {
        super(initialCapacity, loadFactor);
    }
    
    public TShortHashImpl(int initialCapacity, float loadFactor, short no_entry_value) {
        super(initialCapacity, loadFactor, no_entry_value);
    }
}

class TShortProcedureImpl implements TShortProcedure {
    public boolean execute(short value) {
        return true;
    }
}