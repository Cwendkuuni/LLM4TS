package gnu.trove.impl.hash;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

    private TFloatCharHash testObj;

    @Before
    public void setUp() {
        testObj = new TFloatCharHash();
    }

    @Test
    public void testConstructor1() {
        TFloatCharHash test = new TFloatCharHash();
        assertNotNull(test);
    }

    @Test
    public void testConstructor2() {
        TFloatCharHash test = new TFloatCharHash(10);
        assertNotNull(test);
    }

    @Test
    public void testConstructor3() {
        TFloatCharHash test = new TFloatCharHash(10, 0.5f);
        assertNotNull(test);
    }

    @Test
    public void testConstructor4() {
        TFloatCharHash test = new TFloatCharHash(10, 0.5f, 1.0f, 'a');
        assertNotNull(test);
    }

    @Test
    public void testGetNoEntryKey() {
        float result = testObj.getNoEntryKey();
        assertEquals(0.0f, result, 0.0001);
    }

    @Test
    public void testGetNoEntryValue() {
        char result = testObj.getNoEntryValue();
        assertEquals('\0', result);
    }

    @Test
    public void testContains() {
        boolean result = testObj.contains(1.0f);
        assertFalse(result);
    }

    @Test
    public void testForEach() {
        boolean result = testObj.forEach(new TFloatProcedure() {
            public boolean execute(float val) {
                return true;
            }
        });
        assertTrue(result);
    }

    @Test
    public void testRemoveAt() {
        testObj.removeAt(0);
        assertFalse(testObj.contains(0.0f));
    }

    @Test
    public void testIndex() {
        int result = testObj.index(1.0f);
        assertEquals(-1, result);
    }

    @Test
    public void testIndexRehashed() {
        int result = testObj.indexRehashed(1.0f, 1, 1, (byte) 1);
        assertEquals(-1, result);
    }

    @Test
    public void testInsertKey() {
        int result = testObj.insertKey(1.0f);
        assertEquals(0, result);
        assertTrue(testObj.contains(1.0f));
    }

    @Test
    public void testInsertKeyRehash() {
        int result = testObj.insertKeyRehash(1.0f, 1, 1, (byte) 1);
        assertEquals(-1, result);
    }

    @Test
    public void testInsertKeyAt() {
        testObj.insertKeyAt(0, 1.0f);
        assertTrue(testObj.contains(1.0f));
    }

    @Test
    public void testXinsertKey() {
        int result = testObj.XinsertKey(1.0f);
        assertEquals(0, result);
        assertTrue(testObj.contains(1.0f));
    }

    @Test
    public void testWriteExternal() {
        TFloatCharHash newObj = new TFloatCharHash();
        
        try {
            newObj.writeExternal(null);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals(e.getMessage(), null);
        }
    }

    @Test
    public void testReadExternal() {
        TFloatCharHash newObj = new TFloatCharHash();
        
        try {
            newObj.readExternal(null);
            fail("Expected IOException to be thrown");
        } catch (IOException e) {
            assertEquals(e.getMessage(), null);
        } catch (ClassNotFoundException e) {
            assertEquals(e.getMessage(), null);
        }
    }
}