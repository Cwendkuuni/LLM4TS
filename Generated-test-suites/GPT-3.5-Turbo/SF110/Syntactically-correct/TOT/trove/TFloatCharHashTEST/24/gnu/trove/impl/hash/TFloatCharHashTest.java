import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

    private TFloatCharHash hash;

    @Before
    public void setUp() {
        hash = new TFloatCharHash();
    }

    @Test
    public void testDefaultConstructor() {
        assertEquals(0.0f, hash.getNoEntryKey(), 0.000001);
        assertEquals('\0', hash.getNoEntryValue());
        assertFalse(hash.contains(0.0f));
    }

    @Test
    public void testConstructorWithInitialCapacity() {
        hash = new TFloatCharHash(15);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.000001);
        assertEquals('\0', hash.getNoEntryValue());
        assertFalse(hash.contains(0.0f));
    }

    @Test
    public void testConstructorWithInitialCapacityAndLoadFactor() {
        hash = new TFloatCharHash(15, 0.75f);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.000001);
        assertEquals('\0', hash.getNoEntryValue());
        assertFalse(hash.contains(0.0f));
    }

    @Test
    public void testConstructorWithInitialCapacityLoadFactorNoEntryKeyAndNoEntryValue() {
        hash = new TFloatCharHash(15, 0.75f, 1.5f, 'A');
        assertEquals(1.5f, hash.getNoEntryKey(), 0.000001);
        assertEquals('A', hash.getNoEntryValue());
        assertFalse(hash.contains(1.5f));
    }

    @Test
    public void testContains() {
        hash.insertKey(1.0f);
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        StringBuilder stringBuilder = new StringBuilder();
        TFloatProcedure floatProcedure = new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                stringBuilder.append(value);
                return true;
            }
        };
        assertTrue(hash.forEach(floatProcedure));
        assertEquals("1.02.03.0", stringBuilder.toString());
    }

    @Test
    public void testRemoveAt() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        hash.removeAt(1);
        assertFalse(hash.contains(2.0f));
        assertEquals(1.0f, hash.getNoEntryKey(), 0.000001);
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testIndex() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        assertEquals(1, hash.index(2.0f));
        assertEquals(-1, hash.index(4.0f));
    }

    @Test
    public void testInsertKey() {
        assertEquals(0, hash.insertKey(1.0f));
        assertEquals(0.0f, hash.getNoEntryKey(), 0.000001);
        assertEquals('\0', hash.getNoEntryValue());
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
        assertFalse(hash.consumeFreeSlot);
    }
    
    @Test
    public void testXinsertKey() {
        assertEquals(0, hash.XinsertKey(1.0f));
        assertEquals(0.0f, hash.getNoEntryKey(), 0.000001);
        assertEquals('\0', hash.getNoEntryValue());
        assertTrue(hash.contains(1.0f));
        assertFalse(hash.contains(2.0f));
        assertFalse(hash.consumeFreeSlot);
    }

    @Test
    public void testWriteExternal() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        // Write to object output stream
    }

    @Test
    public void testReadExternal() {
        // Read object from input stream and verify the values
    }

}