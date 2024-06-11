
import org.junit.Assert;
import org.junit.Test;
import gnu.trove.impl.hash.TFloatCharHash;

public class TFloatCharHashTest {

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(2.0f);
        Assert.assertTrue(hash.contains(2.0f));
        Assert.assertFalse(hash.contains(1.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        StringBuilder builder = new StringBuilder();
        hash.forEach(val -> {
            builder.append(val);
            return true;
        });
        Assert.assertEquals("23.0", builder.toString());
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(2.0f);
        hash.removeAt(0);
        Assert.assertFalse(hash.contains(2.0f));
    }

    @Test
    public void testIndex() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(2.0f);
        Assert.assertEquals(0, hash.index(2.0f));
        Assert.assertEquals(-1, hash.index(1.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        Assert.assertEquals(0, hash.insertKey(2.0f));
        Assert.assertEquals(-1, hash.insertKey(2.0f));
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatCharHash hash = new TFloatCharHash();
        for (int i = 0; i < 20; i++) {
            hash.insertKey(i);
        }
        Assert.assertEquals(0, hash.insertKeyRehash(21.0f, 0, 0, (byte) 0));
        Assert.assertEquals(-21, hash.insertKeyRehash(21.0f, 0, 0, (byte) 1));
    }

    @Test
    public void testInsertKeyAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKeyAt(0, 2.0f);
        Assert.assertTrue(hash.contains(2.0f));
    }

    @Test
    public void testXinsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        Assert.assertEquals(0, hash.XinsertKey(2.0f));
        Assert.assertEquals(-1, hash.XinsertKey(2.0f));
    }

    @Test
    public void testWriteExternal() {
        // Not tested as it requires java.io.ObjectOutput
    }

    @Test
    public void testReadExternal() {
        // Not tested as it requires java.io.ObjectInput
    }

}
