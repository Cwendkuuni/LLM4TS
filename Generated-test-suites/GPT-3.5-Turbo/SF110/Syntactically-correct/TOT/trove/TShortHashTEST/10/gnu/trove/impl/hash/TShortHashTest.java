import gnu.trove.impl.hash.TShortHash;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TShortHashTest {
    
    private TShortHash tShortHash;

    @Before
    public void setUp() {
        tShortHash = new TShortHash();
    }

    @Test
    public void testConstructor() {
        Assert.assertEquals(0, tShortHash.size());
    }

    @Test
    public void testInitialCapacityConstructor() {
        tShortHash = new TShortHash(10);
        Assert.assertEquals(0, tShortHash.size());
        Assert.assertTrue(tShortHash.isEmpty());
    }

    @Test
    public void testLoadFactorConstructor() {
        tShortHash = new TShortHash(10, 0.5f);
        Assert.assertEquals(0, tShortHash.size());
        Assert.assertTrue(tShortHash.isEmpty());
    }

    @Test
    public void testShortValueConstructor() {
        short no_entry_value = -1;
        tShortHash = new TShortHash(10, 0.5f, no_entry_value);
        Assert.assertEquals(0, tShortHash.size());
        Assert.assertEquals(no_entry_value, tShortHash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        int initialCapacity = 10;
        int capacity = tShortHash.setUp(initialCapacity);
        Assert.assertEquals(initialCapacity, capacity);
    }

    @Test
    public void testContains() {
        int value = 5;
        tShortHash.insertKey((short) value);
        Assert.assertTrue(tShortHash.contains((short) value));
        Assert.assertFalse(tShortHash.contains((short) (value + 1)));
    }

    @Test
    public void testForEach() {
        int value1 = 10;
        int value2 = 20;
        tShortHash.insertKey((short) value1);
        tShortHash.insertKey((short) value2);
        
        TShortProcedure procedure = new TShortProcedure() {
            int count = 0;
            
            public boolean execute(short value) {
                if (value == value1 || value == value2) {
                    count++;
                }
                return true;
            }
        };
        
        Assert.assertTrue(tShortHash.forEach(procedure));
        Assert.assertEquals(2, procedure.count);
    }

    @Test
    public void testRemoveAt() {
        int value = 5;
        tShortHash.insertKey((short) value);
        int index = tShortHash.index((short) value);
        tShortHash.removeAt(index);
        
        Assert.assertFalse(tShortHash.contains((short) value));
    }

    @Test
    public void testIndex() {
        int value = 5;
        tShortHash.insertKey((short) value);
        int index = tShortHash.index((short) value);
        
        Assert.assertEquals(0, index);
        Assert.assertEquals(-1, tShortHash.index((short) (value + 1)));
    }

    @Test
    public void testInsertKey() {
        int value = 5;
        int index = tShortHash.insertKey((short) value);
        
        Assert.assertEquals(0, index);
        Assert.assertEquals(value, tShortHash._set[index]);
        Assert.assertEquals(1, tShortHash._states[index]);
    }

    @Test
    public void testInsertKeyRehash() {
        int value1 = 10;
        int value2 = 20;
        tShortHash.insertKey((short) value1);
        int index = tShortHash.insertKey((short) value2);
        
        Assert.assertEquals(1, index);
        Assert.assertEquals(value2, tShortHash._set[index]);
        Assert.assertEquals(1, tShortHash._states[index]);
    }

    @Test(expected = IllegalStateException.class)
    public void testFullKeySet() {
        for (int i = 0; i < TPrimitiveHash.DEFAULT_CAPACITY; i++) {
            tShortHash.insertKey((short) i);
        }
        tShortHash.insertKey((short) (TPrimitiveHash.DEFAULT_CAPACITY + 1));
    }
}