
import org.junit.Test;

public class TFloatCharHashTest {

    @Test
    public void testContains() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        
        assert(hash.contains(1.0f));
        assert(hash.contains(2.0f));
        assert(hash.contains(3.0f));
        assert(!hash.contains(4.0f));
    }

    @Test
    public void testForEach() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);
        
        float sum = 0.0f;
        hash.forEach((val) -> { sum += val; return true; });

        assert(sum == (1.0f + 2.0f + 3.0f));
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        assert(hash.insertKey(1.0f) == 0);
        assert(hash.insertKey(2.0f) == 1);
        assert(hash.insertKey(3.0f) == 2);
        
        assert(hash.contains(1.0f));
        assert(hash.contains(2.0f));
        assert(hash.contains(3.0f));
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        assert(hash.insertKey(1.0f) == 0);
        assert(hash.insertKey(2.0f) == 1);
        assert(hash.insertKey(3.0f) == 2);
        
        hash.removeAt(1);
        
        assert(hash.contains(1.0f));
        assert(!hash.contains(2.0f));
        assert(hash.contains(3.0f));
    }
}

