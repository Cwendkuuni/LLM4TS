
import org.junit.Assert;
import org.junit.Test;

public class TFloatDoubleHashTest {
    
    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);
        boolean result = hash.contains(1.0f);
        Assert.assertTrue(result);
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        final double[] sum = {0.0};
        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float value) {
                sum[0] += value;
                return true;
            }
        };

        boolean result = hash.forEach(procedure);
        Assert.assertTrue(result);
        Assert.assertEquals(6.0, sum[0], 0.0);
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);

        hash.removeAt(0);
        boolean result = hash.contains(1.0f);
        Assert.assertFalse(result);
    }

    @Test
    public void testGetNoEntryKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        float noEntryKey = hash.getNoEntryKey();

        Assert.assertEquals(0.0f, noEntryKey, 0.0);
    }

    @Test
    public void testGetNoEntryValue() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        double noEntryValue = hash.getNoEntryValue();

        Assert.assertEquals(0.0, noEntryValue, 0.0);
    }

    @Test
    public void testSetUp() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        int capacity = hash.setUp(20);

        Assert.assertEquals(20, capacity);
        Assert.assertNotNull(hash._set);
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);
        int index = hash.index(1.0f);

        Assert.assertEquals(0, index);
    }

    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);
        int index = hash.indexRehashed(1.0f, 0, 0, (byte)1);

        Assert.assertEquals(0, index);
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        int index = hash.insertKey(1.0f);

        Assert.assertEquals(0, index);
        boolean result = hash.contains(1.0f);
        Assert.assertTrue(result);
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKey(1.0f);
        int index = hash.insertKeyRehash(2.0f, 0, 0, (byte)2);

        Assert.assertEquals(1, index);
        boolean result = hash.contains(2.0f);
        Assert.assertTrue(result);
    }

    @Test
    public void testInsertKeyAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        hash.insertKeyAt(0, 1.0f);

        boolean result = hash.contains(1.0f);
        Assert.assertTrue(result);
    }

    @Test
    public void testXinsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        int index = hash.XinsertKey(1.0f);

        Assert.assertEquals(0, index);
        boolean result = hash.contains(1.0f);
        Assert.assertTrue(result);
    }

    @Test
    public void testWriteExternal() throws IOException {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        hash.writeExternal(objectOutputStream);
        objectOutputStream.flush();

        byte[] bytes = byteArrayOutputStream.toByteArray();
        Assert.assertNotNull(bytes);
        Assert.assertTrue(bytes.length > 0);
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        TFloatDoubleHash hash = new TFloatDoubleHash(10);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

        hash.writeExternal(objectOutputStream);
        objectOutputStream.flush();

        byte[] bytes = byteArrayOutputStream.toByteArray();
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

        hash.readExternal(objectInputStream);
        float noEntryKey = hash.getNoEntryKey();
        double noEntryValue = hash.getNoEntryValue();

        Assert.assertEquals(0.0f, noEntryKey, 0.0);
        Assert.assertEquals(0.0, noEntryValue, 0.0);
    }
}
