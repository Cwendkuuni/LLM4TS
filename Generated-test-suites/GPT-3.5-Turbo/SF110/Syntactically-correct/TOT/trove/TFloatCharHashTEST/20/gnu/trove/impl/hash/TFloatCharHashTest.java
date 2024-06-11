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
    public void testDefaultConstructor() {
        assertEquals(0.0f, floatCharHash.getNoEntryKey(), 0.0);
        assertEquals('\0', floatCharHash.getNoEntryValue());
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        floatCharHash = new TFloatCharHash(10);
        assertEquals(0.0f, floatCharHash.getNoEntryKey(), 0.0);
        assertEquals('\0', floatCharHash.getNoEntryValue());
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        floatCharHash = new TFloatCharHash(10, 0.5f);
        assertEquals(0.0f, floatCharHash.getNoEntryKey(), 0.0);
        assertEquals('\0', floatCharHash.getNoEntryValue());
    }

    @Test
    public void testConstructorWithInitialCapacityLoadFactorAndNoEntry() {
        floatCharHash = new TFloatCharHash(10, 0.5f, -1.0f, 'x');
        assertEquals(-1.0f, floatCharHash.getNoEntryKey(), 0.0);
        assertEquals('x', floatCharHash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        floatCharHash.insertKey(5.0f);
        assertTrue(floatCharHash.contains(5.0f));
        assertFalse(floatCharHash.contains(10.0f));
    }

    @Test
    public void testForEach() {
        floatCharHash.insertKey(5.0f);
        floatCharHash.insertKey(10.0f);
        floatCharHash.insertKey(15.0f);

        TFloatProcedure procedure = new TFloatProcedure() {
            float sum = 0;

            public boolean execute(float value) {
                sum += value;
                return true;
            }

            public float getSum() {
                return sum;
            }
        };

        assertTrue(floatCharHash.forEach(procedure));
        assertEquals(30.0, procedure.getSum(), 0.0);
    }

    @Test
    public void testInsertKey() {
        assertEquals(0, floatCharHash.insertKey(5.0f));
        assertEquals(1, floatCharHash.insertKey(10.0f));
        assertEquals(-2, floatCharHash.insertKey(5.0f));
        assertEquals(2, floatCharHash.insertKey(15.0f));
    }

    @Test
    public void testRemoveAt() {
        floatCharHash.insertKey(5.0f);
        floatCharHash.insertKey(10.0f);

        floatCharHash.removeAt(0);
        assertFalse(floatCharHash.contains(5.0f));
        assertTrue(floatCharHash.contains(10.0f));
    }

    @Test
    public void testWriteAndReadExternal() {
        floatCharHash = new TFloatCharHash(10, 0.5f, -1.0f, 'x');
        floatCharHash.insertKey(5.0f);
        floatCharHash.insertKey(10.0f);

        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(new FileOutputStream("hash.bin"));
            floatCharHash.writeExternal(outputStream);
            outputStream.close();

            ObjectInputStream inputStream = new ObjectInputStream(new FileInputStream("hash.bin"));
            TFloatCharHash newFloatCharHash = new TFloatCharHash();
            newFloatCharHash.readExternal(inputStream);
            inputStream.close();

            assertTrue(newFloatCharHash.contains(5.0f));
            assertTrue(newFloatCharHash.contains(10.0f));
            assertEquals(-1.0f, newFloatCharHash.getNoEntryKey(), 0.0);
            assertEquals('x', newFloatCharHash.getNoEntryValue());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            fail("Exception thrown");
        }
    }
}