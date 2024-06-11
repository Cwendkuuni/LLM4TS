import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TShortHashTest {

    private TShortHash tShortHash;

    @Before
    public void setUp() {
        tShortHash = new TShortHash();
    }

    // Testing constructor TShortHash()
    @Test
    public void testConstructor1() {
        Assert.assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
    }

    // Testing constructor TShortHash(int initialCapacity)
    @Test
    public void testConstructor2() {
        int initialCapacity = 100;
        TShortHash tShortHash = new TShortHash(initialCapacity);
        Assert.assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
        Assert.assertEquals(initialCapacity, tShortHash._set.length);
    }

    // Testing constructor TShortHash(int initialCapacity, float loadFactor)
    @Test
    public void testConstructor3() {
        int initialCapacity = 100;
        float loadFactor = 0.9f;
        TShortHash tShortHash = new TShortHash(initialCapacity, loadFactor);
        Assert.assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
        Assert.assertEquals(initialCapacity, tShortHash._set.length);
        Assert.assertEquals(loadFactor, tShortHash._loadFactor, 0.0);
    }

    // Testing constructor TShortHash(int initialCapacity, float loadFactor, short no_entry_value)
    @Test
    public void testConstructor4() {
        int initialCapacity = 100;
        float loadFactor = 0.9f;
        short no_entry_value = 0;
        TShortHash tShortHash = new TShortHash(initialCapacity, loadFactor, no_entry_value);
        Assert.assertEquals(no_entry_value, tShortHash.getNoEntryValue());
        Assert.assertEquals(initialCapacity, tShortHash._set.length);
        Assert.assertEquals(loadFactor, tShortHash._loadFactor, 0.0);
    }

    // Testing setUp(int initialCapacity)
    @Test
    public void testSetUp() {
        int initialCapacity = 100;
        int capacity = tShortHash.setUp(initialCapacity);
        Assert.assertEquals(initialCapacity, capacity);
        Assert.assertEquals(initialCapacity, tShortHash._set.length);
    }

    // Testing contains(short val)
    @Test
    public void testContains() {
        short val = 10;
        tShortHash._set[0] = val;
        Assert.assertTrue(tShortHash.contains(val));
    }

    // Testing forEach(TShortProcedure procedure)
    @Test
    public void testForEach() {
        short val1 = 10;
        short val2 = 20;
        short[] expectedResult = {val1, val2};
        tShortHash._set[0] = val1;
        tShortHash._set[1] = val2;
        
        TShortProcedure procedure = new TShortProcedure() {
            int index = 0;
            @Override
            public boolean execute(short value) {
                Assert.assertEquals(expectedResult[index], value);
                index++;
                return true;
            }
        };

        Assert.assertTrue(tShortHash.forEach(procedure));
    }

    // Testing removeAt(int index)
    @Test
    public void testRemoveAt() {
        int index = 0;
        tShortHash._set[index] = 10;
        tShortHash._states[index] = 1;
        tShortHash.removeAt(index);
        Assert.assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash._set[index]);
        Assert.assertEquals(0, tShortHash._states[index]);
    }

    // Testing index(short val)
    @Test
    public void testIndex() {
        short val = 10;
        tShortHash._set[0] = val;
        tShortHash._states[0] = 1;
        Assert.assertEquals(0, tShortHash.index(val));
    }

    // Testing indexRehashed(short key, int index, int hash, byte state)
    @Test
    public void testIndexRehashed() {
        short key = 10;
        int index = 0;
        int hash = 123;
        byte state = 1;

        int result = tShortHash.indexRehashed(key, index, hash, state);
        Assert.assertEquals(-1, result);
    }

    // Testing insertKey(short val)
    @Test
    public void testInsertKey() {
        short val = 10;
        int result = tShortHash.insertKey(val);
        Assert.assertEquals(0, result);
        Assert.assertEquals(val, tShortHash._set[0]);
        Assert.assertEquals(1, tShortHash._states[0]);
        Assert.assertFalse(tShortHash.consumeFreeSlot);
    }

    // Testing insertKeyRehash(short val, int index, int hash, byte state)
    @Test
    public void testInsertKeyRehash() {
        short val = 10;
        int index = 0;
        int hash = 123;
        byte state = 1;

        int result = tShortHash.insertKeyRehash(val, index, hash, state);
        Assert.assertEquals(-1, result);
    }

    // Testing insertKeyAt(int index, short val)
    @Test
    public void testInsertKeyAt() {
        int index = 0;
        short val = 10;
        tShortHash.insertKeyAt(index, val);
        Assert.assertEquals(val, tShortHash._set[index]);
        Assert.assertEquals(1, tShortHash._states[index]);
    }

}