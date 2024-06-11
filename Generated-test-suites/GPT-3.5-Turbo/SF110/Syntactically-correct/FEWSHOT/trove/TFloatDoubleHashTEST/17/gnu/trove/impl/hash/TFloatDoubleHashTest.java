
package gnu.trove.impl.hash;

import org.junit.Test;

import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    @Test
    public void testGetNoEntryKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertEquals(0.0, hash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertFalse(hash.contains(1.0f));
        
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        
        hash.remove(1.0f);
        assertFalse(hash.contains(1.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        TFloatProcedure procedure = new TFloatProcedure() {
            double sum = 0.0;
            
            @Override
            public boolean execute(float value) {
                sum += value;
                return true;
            }
        };
        
        assertTrue(hash.forEach(procedure));
        assertEquals(3.0, procedure.sum, 0.0);
    }


    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        assertEquals(0, hash.insertKey(1.0f));
        assertTrue(hash.contains(1.0f));
        
        assertEquals(1, hash.insertKey(2.0f));
        assertTrue(hash.contains(2.0f));
        
        assertEquals(2, hash.insertKey(3.0f));
        assertTrue(hash.contains(3.0f));
    }

    @Test
    public void testRemove() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        assertFalse(hash.remove(1.0f));
        
        hash.insertKey(1.0f);
        assertTrue(hash.remove(1.0f));
        assertFalse(hash.contains(1.0f));
    }

    @Test
    public void testWriteExternal() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
            hash.writeExternal(objectOutputStream);
            
            ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
            TFloatDoubleHash hashRead = new TFloatDoubleHash();
            hashRead.readExternal(objectInputStream);
            
            assertEquals(hash.getNoEntryKey(), hashRead.getNoEntryKey(), 0.0);
            assertEquals(hash.getNoEntryValue(), hashRead.getNoEntryValue(), 0.0);
            assertEquals(hash.contains(1.0f), hashRead.contains(1.0f));
            assertEquals(hash.contains(2.0f), hashRead.contains(2.0f));
            
        } catch (IOException | ClassNotFoundException e) {
            fail("Exception caught: " + e.getMessage());
        }
    }
}

