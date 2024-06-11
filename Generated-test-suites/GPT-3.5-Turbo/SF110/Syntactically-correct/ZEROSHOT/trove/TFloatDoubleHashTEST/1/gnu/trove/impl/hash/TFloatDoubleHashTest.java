package gnu.trove.impl.hash;

import gnu.trove.procedure.*;
import gnu.trove.impl.*;
import java.io.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TFloatDoubleHashTest {
    private TFloatDoubleHash tFloatDoubleHash;

    @Before
    public void setUp() {
        tFloatDoubleHash = new TFloatDoubleHash();
    }

    @Test
    public void testContains() {
        float val = 5.0f;
        boolean result = tFloatDoubleHash.contains(val);
        Assert.assertFalse(result);
    }

    @Test
    public void testForEach() {
        TFloatProcedure procedure = new TFloatProcedure() {
            @Override
            public boolean execute(float val) {
                return true;
            }
        };
        boolean result = tFloatDoubleHash.forEach(procedure);
        Assert.assertTrue(result);
    }

    @Test
    public void testRemoveAt() {
        int index = 5;
        tFloatDoubleHash.removeAt(index);
        Assert.assertEquals(tFloatDoubleHash.getNoEntryKey(), tFloatDoubleHash._set[index], 0);
    }

    @Test
    public void testIndex() {
        float key = 5.0f;
        int index = tFloatDoubleHash.index(key);
        Assert.assertEquals(-1, index);
    }

    @Test
    public void testIndexRehashed() {
        float key = 5.0f;
        int index = 3;
        int hash = 6;
        byte state = 1;
        int result = tFloatDoubleHash.indexRehashed(key, index, hash, state);
        Assert.assertEquals(-1, result);
    }

    @Test
    public void testInsertKey() {
        float val = 5.0f;
        int result = tFloatDoubleHash.insertKey(val);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testInsertKeyRehash() {
        float val = 5.0f;
        int index = 3;
        int hash = 6;
        byte state = 1;
        int result = tFloatDoubleHash.insertKeyRehash(val, index, hash, state);
        Assert.assertEquals(-1, result);
    }

    @Test
    public void testInsertKeyAt() {
        float val = 5.0f;
        int index = 3;
        tFloatDoubleHash.insertKeyAt(index, val);
        Assert.assertEquals(val, tFloatDoubleHash._set[index], 0);
        Assert.assertEquals(1, tFloatDoubleHash._states[index]);
    }

    @Test
    public void testXinsertKey() {
        float key = 5.0f;
        int result = tFloatDoubleHash.XinsertKey(key);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testWriteExternal() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        tFloatDoubleHash.writeExternal(objectOutputStream);
        objectOutputStream.flush();
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        Assert.assertNotNull(byteArray);
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        tFloatDoubleHash.writeExternal(objectOutputStream);
        objectOutputStream.flush();
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        tFloatDoubleHash.readExternal(objectInputStream);
        Assert.assertEquals(tFloatDoubleHash.getNoEntryKey(), in.readFloat(), 0);
        Assert.assertEquals(tFloatDoubleHash.getNoEntryValue(), in.readDouble(), 0);
    }

}