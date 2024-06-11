package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TFloatCharHashTest {
    private TFloatCharHash hashMap;

    @Before
    public void setUp() {
        hashMap = new TFloatCharHash();
    }
    
    @Test
    public void testInsertKey() {
        assertEquals(0, hashMap.insertKey(1.5f));
        assertEquals(1, hashMap.insertKey(2.5f));
        assertEquals(2, hashMap.insertKey(3.5f));
    }

    @Test
    public void testRemoveAt() {
        hashMap.insertKey(1.5f);
        hashMap.insertKey(2.5f);
        hashMap.insertKey(3.5f);
        
        hashMap.removeAt(1);
        assertFalse(hashMap.contains(2.5f));
        assertNull(hashMap._set[1]);
    }

    @Test
    public void testContains() {
        hashMap.insertKey(1.5f);
        assertTrue(hashMap.contains(1.5f));
        assertFalse(hashMap.contains(2.5f));
    }

    @Test
    public void testForEach() {
        hashMap.insertKey(1.5f);
        hashMap.insertKey(2.5f);
        
        TFloatProcedure procedure = new TFloatProcedure() {
            float sum = 0;
            
            @Override
            public boolean execute(float value) {
                sum += value;
                return true;
            }
            
            public float getSum() {
                return sum;
            }
        };
        
        hashMap.forEach(procedure);
        assertEquals(4.0f, procedure.getSum(), 0.0);
    }

    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, hashMap.getNoEntryKey(), 0.0);
        
        TFloatCharHash hashMapWithNoEntryKey = new TFloatCharHash(10, 0.75f, -1.0f, 'a');
        assertEquals(-1.0f, hashMapWithNoEntryKey.getNoEntryKey(), 0.0);
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals('\0', hashMap.getNoEntryValue());
        
        TFloatCharHash hashMapWithNoEntryValue = new TFloatCharHash(10, 0.75f, -1.0f, 'a');
        assertEquals('a', hashMapWithNoEntryValue.getNoEntryValue());
    }

}