import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assertFalse(hash.contains(0.0f));

        hash.insertKey(0.0f);
        assertTrue(hash.contains(0.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(0.0f);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float value) {
                System.out.println(value);
                return true;
            }
        };

        assertTrue(hash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(0.0f);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        hash.removeAt(1);
        assertFalse(hash.contains(1.0f));
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(0.0f);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        assertEquals(1, hash.index(1.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(0.0f);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        assertTrue(hash.contains(1.0f));
    }

    @Test
    public void testXinsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.XinsertKey(0.0f);
        hash.XinsertKey(1.0f);
        hash.XinsertKey(2.0f);

        assertTrue(hash.contains(1.0f));
    }

    @Test
    public void testWriteExternal() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(0.0f);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

            objectOutputStream.writeObject(hash);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

            TFloatDoubleHash deserializedHash = (TFloatDoubleHash) objectInputStream.readObject();

            assertTrue(deserializedHash.contains(1.0f));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testReadExternal() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(0.0f);
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);

        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);

            objectOutputStream.writeObject(hash);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
            ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

            TFloatDoubleHash deserializedHash = (TFloatDoubleHash) objectInputStream.readObject();

            assertTrue(deserializedHash.contains(1.0f));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}