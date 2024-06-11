import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    private TFloatDoubleHash floatDoubleHash;

    @Before
    public void setUp() {
        floatDoubleHash = new TFloatDoubleHash();
    }

    @Test
    public void testGetNoEntryKey() {
        float noEntryKey = floatDoubleHash.getNoEntryKey();
        assertEquals(0.0f, noEntryKey, 0.0001f);
    }

    @Test
    public void testGetNoEntryValue() {
        double noEntryValue = floatDoubleHash.getNoEntryValue();
        assertEquals(0.0, noEntryValue, 0.0001);
    }

    @Test
    public void testContains() {
        floatDoubleHash.insertKey(1.0f);
        assertTrue(floatDoubleHash.contains(1.0f));
    }

    @Test
    public void testForEach() {
        floatDoubleHash.insertKey(1.0f);
        floatDoubleHash.insertKey(2.0f);
        floatDoubleHash.insertKey(3.0f);
        TIntProcedure procedure = new TIntProcedure() {
            double sum = 0.0;

            public boolean execute(double value) {
                sum += value;
                return true;
            }
        };
        assertTrue(floatDoubleHash.forEach(procedure));
        assertEquals(6.0, procedure.getSum(), 0.0001);
    }

    @Test
    public void testRemoveAt() {
        floatDoubleHash.insertKey(1.0f);
        floatDoubleHash.insertKey(2.0f);
        floatDoubleHash.insertKey(3.0f);
        floatDoubleHash.removeAt(1);
        assertFalse(floatDoubleHash.contains(2.0f));
    }

    @Test
    public void testIndex() {
        floatDoubleHash.insertKey(1.0f);
        floatDoubleHash.insertKey(2.0f);
        floatDoubleHash.insertKey(3.0f);

        int index = floatDoubleHash.index(2.0f);
        assertEquals(1, index);

        index = floatDoubleHash.index(4.0f);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKey() {
        int index = floatDoubleHash.insertKey(1.0f);
        assertTrue(index >= 0);

        index = floatDoubleHash.insertKey(1.0f);
        assertTrue(index < 0);
    }

    @Test
    public void testInsertKeyRehash() {
        floatDoubleHash.insertKey(1.0f);
        floatDoubleHash.insertKey(2.0f);
        floatDoubleHash.insertKey(3.0f);

        int index = floatDoubleHash.insertKeyRehash(4.0f, 0, 0, (byte)1);
        assertTrue(index >= 0);

        index = floatDoubleHash.insertKeyRehash(1.0f, 0, 0, (byte)2);
        assertTrue(index < 0);
    }

    @Test
    public void testXinsertKey() {
        int index = floatDoubleHash.XinsertKey(1.0f);
        assertTrue(index >= 0);

        index = floatDoubleHash.XinsertKey(1.0f);
        assertTrue(index < 0);
    }

    @Test
    public void testWriteExternal() {
        floatDoubleHash.insertKey(1.0f);
        floatDoubleHash.insertKey(2.0f);

        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("test.dat"));
            floatDoubleHash.writeExternal(out);
            out.close();
            ObjectInputStream in = new ObjectInputStream(new FileInputStream("test.dat"));
            TFloatDoubleHash newFloatDoubleHash = new TFloatDoubleHash();
            newFloatDoubleHash.readExternal(in);
            in.close();
            assertTrue(newFloatDoubleHash.contains(1.0f));
            assertTrue(newFloatDoubleHash.contains(2.0f));
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}