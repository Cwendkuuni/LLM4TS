
package gnu.trove.impl.hash;

import gnu.trove.impl.hash.TFloatCharHash;
import org.junit.Test;

import static org.junit.Assert.*;

public class TFloatCharHashTest {

    @Test
    public void testConstructor() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);
        assertEquals('\0', hash.getNoEntryValue());
        
        hash = new TFloatCharHash(10);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);
        assertEquals('\0', hash.getNoEntryValue());
        
        hash = new TFloatCharHash(10, 0.5f);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);
        assertEquals('\0', hash.getNoEntryValue());
        
        hash = new TFloatCharHash(10, 0.5f, 1.0f, 'a');
        assertEquals(1.0f, hash.getNoEntryKey(), 0.0f);
        assertEquals('a', hash.getNoEntryValue());
    }
    
    @Test
    public void testSetAndGetNoEntryValues() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.setNoEntryKey(1.0f);
        assertEquals(1.0f, hash.getNoEntryKey(), 0.0f);
        
        hash.no_entry_value = 'a';
        assertEquals('a', hash.getNoEntryValue());
        hash.setNoEntryValue('b');
        assertEquals('b', hash.getNoEntryValue());
    }
    
    @Test
    public void testSetUp() {
        TFloatCharHash hash = new TFloatCharHash();
        int capacity = hash.setUp(10);
        assertEquals(13, capacity);
        float[] expectedSet = new float[capacity];
        assertArrayEquals(expectedSet, hash._set, 0.0f);
    }
    
    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._set = new float[]{1.0f, 2.0f, 3.0f};
        assertTrue(hash.contains(1.0f));
        assertTrue(hash.contains(2.0f));
        assertTrue(hash.contains(3.0f));
        assertFalse(hash.contains(4.0f));
    }
    
    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._set = new float[]{1.0f, 2.0f, 3.0f};
        
        TFloatProcedure procedure = new TFloatProcedure() {
            float sum = 0.0f;
            
            @Override
            public boolean execute(float value) {
                sum += value;
                return true;
            }
            
            public float getSum() {
                return sum;
            }
        };
        
        assertTrue(hash.forEach(procedure));
        assertEquals(6.0f, procedure.getSum(), 0.0f);
        
    }
    
    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._set = new float[]{1.0f, 2.0f, 3.0f};
        hash._states = new byte[]{1, 1, 1};
        
        hash.removeAt(1);
        assertEquals(0.0f, hash._set[1], 0.0f);
        assertEquals(2, hash.size());
        assertEquals(2, hash._states[1]);
    }
    
    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._set = new float[]{1.0f, 2.0f, 3.0f};
        hash._states = new byte[]{1, 1, 1};
        
        assertEquals(1, hash.index(2.0f));
        assertEquals(-1, hash.index(4.0f));
    }
    
    @Test
    public void testIndexRehashed() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._set = new float[]{1.0f, 2.0f, 3.0f, 4.0f};
        hash._states = new byte[]{1, 1, 2, 1};
        
        assertEquals(1, hash.indexRehashed(2.0f, 1, 1, (byte)1));
        assertEquals(-1, hash.indexRehashed(2.0f, 2, 1, (byte)2));
    }
    
    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._set = new float[]{1.0f, 2.0f, 3.0f, 0.0f};
        hash._states = new byte[]{1, 1, 1, 0};
        
        int index = hash.insertKey(4.0f);
        assertEquals(3, index);
        assertEquals(4.0f, hash._set[3], 0.0f);
        assertEquals(1, hash._states[3]);
        
        index = hash.insertKey(2.0f);
        assertEquals(-2, index);
        
        index = hash.insertKey(5.0f);
        assertEquals(4, index);
        assertEquals(5.0f, hash._set[4], 0.0f);
        assertEquals(1, hash._states[4]);
    }
    
    @Test
    public void testInsertKeyRehash() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._set = new float[]{1.0f, 0.0f, 3.0f, 0.0f, 5.0f};
        hash._states = new byte[]{1, 0, 1, 1, 1};
        
        int index = hash.insertKeyRehash(2.0f, 1, 1, (byte)0);
        assertEquals(4, index);
        assertEquals(2.0f, hash._set[4], 0.0f);
        assertEquals(1, hash._states[4]);
        
        index = hash.insertKeyRehash(2.0f, 1, 1, (byte)1);
        assertEquals(-2, index);
    }

    @Test
    public void testInsertKeyAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._set = new float[]{1.0f, 2.0f, 3.0f, 0.0f};
        hash._states = new byte[]{1, 1, 1, 0};

        hash.insertKeyAt(3, 4.0f);
        assertEquals(4.0f, hash._set[3], 0.0f);
        assertEquals(1, hash._states[3]);

        hash.insertKeyAt(2, 5.0f);
        assertEquals(5.0f, hash._set[2], 0.0f);
        assertEquals(1, hash._states[2]);
    }

    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash._set = new float[]{1.0f, 2.0f, 3.0f, 0.0f, 5.0f};
        hash._states = new byte[]{1, 0, 1, 1, 1};

        int index = hash.XinsertKey(4.0f);
        assertEquals(3, index);
        assertEquals(4.0f, hash._set[inddex], 0.0f);
        assertEquals(1, hash._states[inddex]);

        index = hash.XinsertKey(2.0f);
        assertEquals(-2, index);

        index = hash.XinsertKey(6.0f);
        assertEquals(5, index);
        assertEquals(6.0f, hash._set[inddex], 0.0f);
        assertEquals(1, hash._states[inddex]);
    }

    @Test
    public void testWriteExternal() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(baos);

        TFloatCharHash hash = new TFloatCharHash();
        hash.writeExternal(out);

        out.flush();
        byte[] byteArray = baos.toByteArray();
        assertEquals(11, byteArray.length);
        assertEquals(0, byteArray[0]);
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        byte[] byteArray = new byte[]{0, 1, 2, 3, 4, 5, 6, 7};
        ByteArrayInputStream bais = new ByteArrayInputStream(byteArray);
        ObjectInputStream in = new ObjectInputStream(bais);

        TFloatCharHash hash = new TFloatCharHash();
        hash.readExternal(in);

        assertEquals(1, hash._set.length);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);
        assertEquals('\0', hash.getNoEntryValue());
    }
}

