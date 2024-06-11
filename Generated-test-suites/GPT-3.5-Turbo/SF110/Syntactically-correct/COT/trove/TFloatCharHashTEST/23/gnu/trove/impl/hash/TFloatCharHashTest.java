import org.junit.Before;
import org.junit.Test;

public class TFloatCharHashTest {

    private TFloatCharHash tFloatCharHash;

    @Before
    public void setUp() {
        tFloatCharHash = new TFloatCharHash();
    }

    @Test
    public void testContains() {
        float value = 1.5f;
        tFloatCharHash.insertKey(value);

        assert(tFloatCharHash.contains(value));
    }

    @Test
    public void testContainsWhenNotPresent() {
        float value = 1.5f;
        float anotherValue = 2.5f;
        tFloatCharHash.insertKey(value);

        assert(!tFloatCharHash.contains(anotherValue));
    }

    @Test
    public void testForEach() {
        float value1 = 1.5f;
        float value2 = 2.5f;
        int[] count = new int[1];

        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float val) {
                count[0]++;
                return true;
            }
        };

        tFloatCharHash.insertKey(value1);
        tFloatCharHash.insertKey(value2);
        tFloatCharHash.forEach(procedure);

        assert(count[0] == 2);
    }

    @Test
    public void testRemoveAt() {
        float value = 1.5f;
        tFloatCharHash.insertKey(value);
        int initialSize = tFloatCharHash.size();

        tFloatCharHash.removeAt(0);

        assert(tFloatCharHash.size() == initialSize - 1);
    }

    @Test
    public void testGetNoEntryKey() {
        float noEntryKey = tFloatCharHash.getNoEntryKey();

        assert(noEntryKey == 0.0f);
    }

    @Test
    public void testGetNoEntryValue() {
        char noEntryValue = tFloatCharHash.getNoEntryValue();

        assert(noEntryValue == '\0');
    }

    @Test
    public void testSetUp() {
        int initialCapacity = 10;

        int capacity = tFloatCharHash.setUp(initialCapacity);

        assert(capacity >= initialCapacity);
    }

    @Test
    public void testIndex() {
        float value = 1.5f;
        tFloatCharHash.insertKey(value);

        int index = tFloatCharHash.index(value);

        assert(index >= 0);
    }

    @Test
    public void testIndexWhenNotPresent() {
        float value = 1.5f;
        float anotherValue = 2.5f;
        tFloatCharHash.insertKey(value);

        int index = tFloatCharHash.index(anotherValue);

        assert(index < 0);
    }

    @Test
    public void testInsertKey() {
        float value = 1.5f;

        int index = tFloatCharHash.insertKey(value);

        assert(index >= 0);
    }

    @Test
    public void testInsertKeyWhenAlreadyPresent() {
        float value = 1.5f;
        tFloatCharHash.insertKey(value);

        int index = tFloatCharHash.insertKey(value);

        assert(index < 0);
    }

    @Test
    public void testInsertKeyRehash() {
        float value = 1.5f;

        int index = tFloatCharHash.insertKeyRehash(value, 0, 0, (byte) 0);

        assert(index >= 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehashWhenNoFreeSlotsAvailable() {
        float value = 1.5f;
        for(int i = 0; i < tFloatCharHash._states.length; i++) {
            tFloatCharHash._states[i] = (byte) 1;
        }

        tFloatCharHash.insertKeyRehash(value, 0, 0, (byte) 0);

        //Should throw IllegalStateException
    }

    @Test
    public void testInsertKeyAt() {
        float value = 1.5f;

        tFloatCharHash.insertKeyAt(0, value);

        assert(tFloatCharHash._set[0] == value);
    }

    @Test
    public void testXinsertKey() {
        float value = 1.5f;

        int index = tFloatCharHash.XinsertKey(value);

        assert(index >= 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testXinsertKeyWhenNoFreeSlotsAvailable() {
        float value = 1.5f;
        for(int i = 0; i < tFloatCharHash._states.length; i++) {
            tFloatCharHash._states[i] = (byte) 1;
        }

        tFloatCharHash.XinsertKey(value);

        //Should throw IllegalStateException
    }
}