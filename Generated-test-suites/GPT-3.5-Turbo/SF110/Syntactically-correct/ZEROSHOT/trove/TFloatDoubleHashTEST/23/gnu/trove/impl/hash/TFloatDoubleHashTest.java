import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TFloatDoubleHashTest {
    private TFloatDoubleHash tFloatDoubleHash;

    @Before
    public void setUp() {
        tFloatDoubleHash = new TFloatDoubleHashImpl();
    }

    @Test
    public void testGetNoEntryKey() {
        float expected = 0.0f;
        float result = tFloatDoubleHash.getNoEntryKey();
        assertEquals(expected, result, 0.0);
    }

    @Test
    public void testGetNoEntryValue() {
        double expected = 0.0;
        double result = tFloatDoubleHash.getNoEntryValue();
        assertEquals(expected, result, 0.0);
    }

    @Test
    public void testContains() {
        float val = 1.0f;
        boolean result = tFloatDoubleHash.contains(val);
        assertFalse(result);
    }

    @Test
    public void testForEach() {
        // TODO: Add test case
    }

    @Test
    public void testRemoveAt() {
        // TODO: Add test case
    }

    @Test
    public void testIndex() {
        // TODO: Add test case
    }

    @Test
    public void testInsertKey() {
        // TODO: Add test case
    }

    @Test
    public void testInsertKeyRehash() {
        // TODO: Add test case
    }

    @Test
    public void testInsertKeyAt() {
        // TODO: Add test case
    }

    private class TFloatDoubleHashImpl extends TFloatDoubleHash {
        @Override
        protected int setUp(int initialCapacity) {
            return 0;
        }

        @Override
        protected void removeAt(int index) {

        }

        @Override
        protected int indexRehashed(float key, int index, int hash, byte state) {
            return 0;
        }

        @Override
        public void writeExternal(ObjectOutput out) throws IOException {

        }

        @Override
        public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {

        }
    }
}