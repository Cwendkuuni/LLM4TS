package gnu.trove.impl.hash;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

    private TFloatCharHash floatCharHash;

    @Before
    public void setUp() {
        floatCharHash = new TFloatCharHash();
    }

    @Test
    public void testConstructor() {
        assertEquals(0.0f, floatCharHash.getNoEntryKey(), 0.0);
        assertEquals('\0', floatCharHash.getNoEntryValue());
    }

    @Test
    public void testInsertKey() {
        int index = floatCharHash.insertKey(1.0f);
        assertTrue(floatCharHash.contains(1.0f));
        assertEquals(0, index);

        index = floatCharHash.insertKey(2.0f);
        assertTrue(floatCharHash.contains(2.0f));
        assertEquals(1, index);
    }

    @Test
    public void testIndex() {
        floatCharHash.insertKey(1.0f);
        floatCharHash.insertKey(2.0f);

        assertEquals(0, floatCharHash.index(1.0f));
        assertEquals(1, floatCharHash.index(2.0f));
    }

    @Test
    public void testRemoveAt() {
        floatCharHash.insertKey(1.0f);
        floatCharHash.insertKey(2.0f);

        floatCharHash.removeAt(0);
        assertFalse(floatCharHash.contains(1.0f));
        assertEquals(-1, floatCharHash.index(1.0f));
        assertTrue(floatCharHash.contains(2.0f));
        assertEquals(0, floatCharHash.index(2.0f));
    }

    @Test
    public void testForEach() {
        floatCharHash.insertKey(1.0f);
        floatCharHash.insertKey(2.0f);
        floatCharHash.insertKey(3.0f);

        TFloatProcedure procedure = new TFloatProcedure() {
            int sum = 0;

            public boolean execute(float value) {
                sum += value;
                return true;
            }

            public int getSum() {
                return sum;
            }
        };

        assertTrue(floatCharHash.forEach(procedure));
        assertEquals(6, procedure.getSum());
    }

    @Test
    public void testWriteExternal() {
        floatCharHash.insertKey(1.0f);
        floatCharHash.insertKey(2.0f);
        floatCharHash.insertKey(3.0f);

        TFloatCharHash externalHash = new TFloatCharHash();
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOut = new ObjectOutputStream(outStream);
            floatCharHash.writeExternal(objectOut);
            objectOut.flush();
            
            ByteArrayInputStream inStream = new ByteArrayInputStream(outStream.toByteArray());
            ObjectInputStream objectIn = new ObjectInputStream(inStream);
            externalHash.readExternal(objectIn);
        } catch (Exception e) {
            fail("Exception: " + e.getMessage());
        }

        assertTrue(externalHash.contains(1.0f));
        assertTrue(externalHash.contains(2.0f));
        assertTrue(externalHash.contains(3.0f));
    }

    @Test
    public void testReadExternal() {
        floatCharHash.insertKey(1.0f);
        floatCharHash.insertKey(2.0f);
        floatCharHash.insertKey(3.0f);

        TFloatCharHash externalHash = new TFloatCharHash();
        try {
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOut = new ObjectOutputStream(outStream);
            floatCharHash.writeExternal(objectOut);
            objectOut.flush();
            
            ByteArrayInputStream inStream = new ByteArrayInputStream(outStream.toByteArray());
            ObjectInputStream objectIn = new ObjectInputStream(inStream);
            externalHash.readExternal(objectIn);
        } catch (Exception e) {
            fail("Exception: " + e.getMessage());
        }

        assertTrue(externalHash.contains(1.0f));
        assertTrue(externalHash.contains(2.0f));
        assertTrue(externalHash.contains(3.0f));
    }

}