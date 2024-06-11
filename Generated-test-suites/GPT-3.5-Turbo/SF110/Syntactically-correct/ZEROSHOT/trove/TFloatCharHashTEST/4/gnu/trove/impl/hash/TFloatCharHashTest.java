package gnu.trove.impl.hash;

import gnu.trove.procedure.*;
import gnu.trove.impl.*;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TFloatCharHashTest {
    
    private TFloatCharHash tFloatCharHash;
    
    @Before
    public void setUp() {
        tFloatCharHash = new TFloatCharHash();
    }
    
    @Test
    public void testConstructor1() {
        assertNotNull(tFloatCharHash);
    }
    
    @Test
    public void testConstructor2() {
        tFloatCharHash = new TFloatCharHash(10);
        assertNotNull(tFloatCharHash);
    }
    
    @Test
    public void testConstructor3() {
        tFloatCharHash = new TFloatCharHash(10, 0.8);
        assertNotNull(tFloatCharHash);
    }
    
    @Test
    public void testConstructor4() {
        tFloatCharHash = new TFloatCharHash(10, 0.8, 1.0f, 'a');
        assertNotNull(tFloatCharHash);
    }
    
    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, tFloatCharHash.getNoEntryKey(), 0.0);
    }
    
    @Test
    public void testGetNoEntryValue() {
        assertEquals('\0', tFloatCharHash.getNoEntryValue());
    }
    
    @Test
    public void testSetUp() {
        assertEquals(10, tFloatCharHash.setUp(10));
    }
    
    @Test
    public void testContains() {
        assertFalse(tFloatCharHash.contains(1.0f));
    }
    
    @Test
    public void testForEach() {
        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float value) {
                return true;
            }
        };
        assertTrue(tFloatCharHash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        tFloatCharHash.removeAt(0);
        assertEquals(0.0f, tFloatCharHash._set[0], 0.0);
        assertEquals(0, tFloatCharHash._states[0]);
    }
    
    @Test
    public void testIndex() {
        assertEquals(-1, tFloatCharHash.index(1.0f));
    }
    
    @Test
    public void testIndexRehashed() {
        assertEquals(-1, tFloatCharHash.indexRehashed(1.0f, 0, 1, (byte)0));
    }
    
    @Test
    public void testInsertKey() {
        assertEquals(0, tFloatCharHash.insertKey(1.0f));
    }
    
    @Test
    public void testInsertKeyRehash() {
        assertEquals(-1, tFloatCharHash.insertKeyRehash(1.0f, 0, 1, (byte)0));
    }
    
    @Test
    public void testInsertKeyAt() {
        tFloatCharHash.insertKeyAt(0, 1.0f);
        assertEquals(1.0f, tFloatCharHash._set[0], 0.0);
        assertEquals(1, tFloatCharHash._states[0]);
    }
    
    @Test
    public void testXinsertKey() {
        assertEquals(0, tFloatCharHash.XinsertKey(1.0f));
    }
    
    @Test
    public void testWriteExternal() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("test.txt"));
            tFloatCharHash.writeExternal(out);
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    @Test
    public void testReadExternal() {
        try {
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("test.txt"));
            tFloatCharHash.readExternal(in);
            in.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    
}