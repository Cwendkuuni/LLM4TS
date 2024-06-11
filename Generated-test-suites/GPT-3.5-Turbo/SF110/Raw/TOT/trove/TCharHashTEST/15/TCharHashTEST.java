import org.junit.Assert;
import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testConstructor1() {
        TCharHash tCharHash = new TCharHash();
        Assert.assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash.getNoEntryValue());
    }

    @Test
    public void testConstructor2() {
        int initialCapacity = 10;
        TCharHash tCharHash = new TCharHash(initialCapacity);
        Assert.assertEquals(initialCapacity, tCharHash._set.length);
    }

    @Test
    public void testConstructor3() {
        int initialCapacity = 10;
        float loadFactor = 0.8f;
        TCharHash tCharHash = new TCharHash(initialCapacity, loadFactor);
        Assert.assertEquals(initialCapacity, tCharHash._set.length);
        Assert.assertEquals(loadFactor, tCharHash._loadFactor, 0.0001);
    }

    @Test
    public void testConstructor4() {
        int initialCapacity = 10;
        float loadFactor = 0.8f;
        char noEntryValue = 'N';
        TCharHash tCharHash = new TCharHash(initialCapacity, loadFactor, noEntryValue);
        Assert.assertEquals(initialCapacity, tCharHash._set.length);
        Assert.assertEquals(loadFactor, tCharHash._loadFactor, 0.0001);
        Assert.assertEquals(noEntryValue, tCharHash.getNoEntryValue());
    }

    @Test
    public void testSetUp() {
        int initialCapacity = 10;
        TCharHash tCharHash = new TCharHash();
        int actualCapacity = tCharHash.setUp(initialCapacity);
        Assert.assertEquals(initialCapacity, actualCapacity);
        Assert.assertEquals(initialCapacity, tCharHash._set.length);
    }

    @Test
    public void testContains() {
        TCharHash tCharHash = new TCharHash();
        tCharHash._set = new char[] { 'A', Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, 'B' };
        Assert.assertTrue(tCharHash.contains('A'));
        Assert.assertFalse(tCharHash.contains('C'));
    }

    @Test
    public void testForEach() {
        TCharHash tCharHash = new TCharHash();
        tCharHash._set = new char[] { 'A', Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, 'B' };

        TCharProcedure mockProcedure = new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                System.out.println(value);
                return true;
            }
        };

        Assert.assertTrue(tCharHash.forEach(mockProcedure));
    }

    @Test
    public void testRemoveAt() {
        TCharHash tCharHash = new TCharHash();
        tCharHash._set = new char[] { 'A', Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, 'B' };
        tCharHash._states = new byte[] { 1, 0, 1 };

        tCharHash.removeAt(0);
        Assert.assertEquals(Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, tCharHash._set[0]);
        Assert.assertEquals(0, tCharHash._states[0]);
    }

    @Test
    public void testIndex() {
        TCharHash tCharHash = new TCharHash();
        tCharHash._set = new char[] { 'A', Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, 'B' };
        tCharHash._states = new byte[] { 1, 0, 1 };

        Assert.assertEquals(0, tCharHash.index('A'));
        Assert.assertEquals(-1, tCharHash.index('C'));
    }

    @Test
    public void testIndexRehashed() {
        TCharHash tCharHash = new TCharHash();
        tCharHash._set = new char[] { 'A', Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, 'B' };
        tCharHash._states = new byte[] { 1, 0, 1 };

        Assert.assertEquals(2, tCharHash.indexRehashed('B', 0, 1, (byte) 1));
        Assert.assertEquals(-1, tCharHash.indexRehashed('C', 0, 1, (byte) 1));
    }

    @Test
    public void testInsertKey() {
        TCharHash tCharHash = new TCharHash();
        tCharHash._set = new char[] { 'A', Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, 'B' };
        tCharHash._states = new byte[] { 1, 0, 1 };

        Assert.assertEquals(-2, tCharHash.insertKey('A'));
        Assert.assertEquals(3, tCharHash.insertKey('C'));
    }

    @Test
    public void testInsertKeyRehash() {
        TCharHash tCharHash = new TCharHash();
        tCharHash._set = new char[] { 'A', Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, 'B' };
        tCharHash._states = new byte[] { 1, 0, 1 };

        Assert.assertEquals(-1, tCharHash.insertKeyRehash('A', 0, 1, (byte) 1));
        Assert.assertEquals(0, tCharHash.insertKeyRehash('C', 0, 1, (byte) 2));
    }

    @Test
    public void testInsertKeyAt() {
        TCharHash tCharHash = new TCharHash();
        tCharHash._set = new char[] { 'A', Constants.DEFAULT_CHAR_NO_ENTRY_VALUE, 'B' };
        tCharHash._states = new byte[] { 1, 0, 1 };

        tCharHash.insertKeyAt(1, 'C');
        Assert.assertEquals('C', tCharHash._set[1]);
        Assert.assertEquals(1, tCharHash._states[1]);
    }

}