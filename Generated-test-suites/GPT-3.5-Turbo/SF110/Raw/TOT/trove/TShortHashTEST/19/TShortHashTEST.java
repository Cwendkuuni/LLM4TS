import org.junit.*;

public class TShortHashTest {

    @Test
    public void testDefaultConstructor() {
        TShortHash tShortHash = new TShortHash();
        Assert.assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, tShortHash.getNoEntryValue());
    }

    @Test
    public void testInitialCapacityConstructor() {
        int initialCapacity = 10;
        TShortHash tShortHash = new TShortHash(initialCapacity);
        Assert.assertEquals(initialCapacity, tShortHash.capacity());
    }

    @Test
    public void testInitialCapacityAndLoadFactorConstructor() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        TShortHash tShortHash = new TShortHash(initialCapacity, loadFactor);
        Assert.assertEquals(initialCapacity, tShortHash.capacity());
        Assert.assertEquals(loadFactor, tShortHash.loadFactor(), 0.001);
    }

    @Test
    public void testInitialCapacityAndLoadFactorAndNoEntryValueConstructor() {
        int initialCapacity = 10;
        float loadFactor = 0.75f;
        short noEntryValue = -1;
        TShortHash tShortHash = new TShortHash(initialCapacity, loadFactor, noEntryValue);
        Assert.assertEquals(initialCapacity, tShortHash.capacity());
        Assert.assertEquals(loadFactor, tShortHash.loadFactor(), 0.001);
        Assert.assertEquals(noEntryValue, tShortHash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        TShortHash tShortHash = new TShortHash();
        int initialCapacity = 10;
        int capacity = tShortHash.setUp(initialCapacity);
        Assert.assertEquals(initialCapacity, capacity);
        Assert.assertEquals(initialCapacity, tShortHash.capacity());
    }

    @Test
    public void testInsertKeyAndContains() {
        TShortHash tShortHash = new TShortHash();
        short key = 10;
        int index = tShortHash.insertKey(key);
        Assert.assertTrue(tShortHash.contains(key));
    }

    @Test
    public void testForEach() {
        TShortHash tShortHash = new TShortHash();
        short key1 = 10;
        short key2 = 20;
        short key3 = 30;
        tShortHash.insertKey(key1);
        tShortHash.insertKey(key2);
        tShortHash.insertKey(key3);

        TShortProcedure procedure = new TShortProcedure() {
            int count = 0;
            @Override
            public boolean execute(short value) {
                count++;
                return true;
            }
        };

        boolean success = tShortHash.forEach(procedure);
        Assert.assertEquals(3, ((TShortProcedure) procedure).count);
        Assert.assertTrue(success);
    }

    @Test
    public void testRemoveAt() {
        TShortHash tShortHash = new TShortHash();
        short key = 10;
        tShortHash.insertKey(key);
        tShortHash.removeAt(0);
        Assert.assertFalse(tShortHash.contains(key));
    }

    @Test
    public void testIndex() {
        TShortHash tShortHash = new TShortHash();
        short key = 10;
        tShortHash.insertKey(key);
        int index = tShortHash.index(key);
        Assert.assertEquals(0, index);
    }

    @Test
    public void testIndexRehashed() {
        TShortHash tShortHash = new TShortHash();
        short key = 10;
        int index = tShortHash.indexRehashed(key, 0, 0, (byte) 1);
        Assert.assertEquals(-1, index);
    }

    @Test
    public void testInsertKeyRehash() {
        TShortHash tShortHash = new TShortHash();
        short key = 10;
        int index = tShortHash.insertKeyRehash(key, 0, 0, (byte) 1);
        Assert.assertEquals(-1, index);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyAtWithFullKeySet() {
        TShortHash tShortHash = new TShortHash();
        short key1 = 10;
        short key2 = 20;
        short key3 = 30;
        tShortHash.insertKey(key1);
        tShortHash.insertKey(key2);
        tShortHash.insertKey(key3);

        short key4 = 40;
        tShortHash.insertKeyAt(0, key4); // Try to insert when key set is full
    }

}