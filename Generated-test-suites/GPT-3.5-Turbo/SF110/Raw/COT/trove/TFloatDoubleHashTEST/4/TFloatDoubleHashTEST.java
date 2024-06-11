import org.junit.Test;

public class TFloatDoubleHashTest {

    @Test
    public void testContains() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        assert(hash.contains(1.0f));
        assert(!hash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        final StringBuilder builder = new StringBuilder();
        hash.forEach(new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                builder.append(value);
                return true;
            }
        });

        assert(builder.toString().equals("123"));
    }

    @Test
    public void testRemoveAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        hash.removeAt(0);
        assert(!hash.contains(1.0f));
    }

    @Test
    public void testIndex() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        assert(hash.index(1.0f) == 0);
        assert(hash.index(2.0f) == -1);
    }

    @Test
    public void testInsertKey() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assert(hash.insertKey(1.0f) == 0);
        assert(hash.insertKey(2.0f) == 1);
        assert(hash.insertKey(2.0f) == -2);
    }

    @Test
    public void testIndexRehashed() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKey(1.0f);
        assert(hash.indexRehashed(1.0f, 0, 1, (byte)1) == 0);
        assert(hash.indexRehashed(2.0f, 0, 1, (byte)1) == -1);
    }

    @Test
    public void testInsertKeyRehash() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        assert(hash.insertKeyRehash(1.0f, 0, 1, (byte)1) == -1);
        assert(hash.insertKeyRehash(2.0f, 0, 1, (byte)1) == 0);
    }

    @Test
    public void testInsertKeyAt() {
        TFloatDoubleHash hash = new TFloatDoubleHash();
        hash.insertKeyAt(0, 1.0f);
        assert(hash.contains(1.0f));
    }

}