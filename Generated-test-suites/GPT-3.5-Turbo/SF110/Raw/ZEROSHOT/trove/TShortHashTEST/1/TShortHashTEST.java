package gnu.trove.impl.hash;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class TShortHashTest {

    private TShortHash tShortHash;

    @Before
    public void setUp() {
        tShortHash = new TShortHash() {
            @Override
            protected int setUp(int initialCapacity) {
                return 0;
            }

            @Override
            protected void removeAt(int index) {

            }
        };
    }

    @Test
    public void testContains() {
        Assert.assertFalse(tShortHash.contains((short) 0));
    }

    @Test
    public void testForEach() {
        TShortProcedure tShortProcedure = new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                return false;
            }
        };

        Assert.assertTrue(tShortHash.forEach(tShortProcedure));
    }

    @Test
    public void testRemoveAt() {
        tShortHash.removeAt(0);
    }

    @Test
    public void testIndex() {
        Assert.assertEquals(-1, tShortHash.index((short) 0));
    }

    @Test
    public void testIndexRehashed() {
        Assert.assertEquals(-1, tShortHash.indexRehashed((short) 0, 0, 0, (byte) 0));
    }

    @Test
    public void testInsertKey() {
        Assert.assertEquals(0, tShortHash.insertKey((short) 0));
    }

    @Test
    public void testInsertKeyRehash() {
        Assert.assertEquals(0, tShortHash.insertKeyRehash((short) 0, 0, 0, (byte) 0));
    }

    @Test
    public void testInsertKeyAt() {
        tShortHash.insertKeyAt(0, (short) 0);
    }
}