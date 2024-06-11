import org.junit.Before;
import org.junit.Test;

public class TCharHashTest {

    private TCharHash tCharHash;

    @Before
    public void setUp() {
        tCharHash = new TCharHash() {
            @Override
            protected int setUp(int initialCapacity) {
                return 0;
            }

            @Override
            protected void removeAt(int index) {

            }

            @Override
            protected int indexRehashed(char key, int index, int hash, byte state) {
                return 0;
            }

            @Override
            protected int insertKeyRehash(char val, int index, int hash, byte state) {
                return 0;
            }

            @Override
            void insertKeyAt(int index, char val) {

            }
        };
    }

    @Test
    public void testContains() {
        boolean result = tCharHash.contains('a');
        assert(result == false);
    }

    @Test
    public void testForEach() {
        boolean result = tCharHash.forEach(new TCharProcedure() {
            @Override
            public boolean execute(char value) {
                return true;
            }
        });
        assert(result == true);
    }

    // edge cases and exceptions testing
    @Test
    public void testInsertKey() {
        int index = tCharHash.insertKey('a');
        assert(index == 0);
    }

    @Test
    public void testInsertKeyRehash() {
        int index = tCharHash.insertKeyRehash('a', 1, 10, (byte) 0);
        assert(index == 0);
    }

    @Test(expected = IllegalStateException.class)
    public void testInsertKeyRehashFullKeySet() {
        tCharHash.insertKeyRehash('a', 1, 10, (byte) 0);
        tCharHash.insertKeyRehash('b', 1, 10, (byte) 1);
    }
}