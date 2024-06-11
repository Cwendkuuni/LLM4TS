import org.junit.*;
import static org.junit.Assert.*;

public class TShortHashTest {

    @Test
    public void testGetNoEntryValue() {
        TShortHash tShortHash = new TShortHash() {
            @Override
            protected void removeAt(int index) {
                
            }

            @Override
            protected int setUp(int initialCapacity) {
                return 0;
            }

            @Override
            protected int insertKey(short val) {
                return 0;
            }

            @Override
            int index(short val) {
                return 0;
            }

            @Override
            int indexRehashed(short key, int index, int hash, byte state) {
                return 0;
            }

            @Override
            void insertKeyAt(int index, short val) {
                
            }
        };

        short expected = Constants.DEFAULT_SHORT_NO_ENTRY_VALUE;
        short actual = tShortHash.getNoEntryValue();
        
        assertEquals(expected, actual);
    }

    @Test
    public void testContains() {
        TShortHash tShortHash = new TShortHash() {
            @Override
            protected void removeAt(int index) {
                
            }

            @Override
            protected int setUp(int initialCapacity) {
                return 0;
            }

            @Override
            protected int insertKey(short val) {
                return 0;
            }

            @Override
            int index(short val) {
                return 0;
            }

            @Override
            int indexRehashed(short key, int index, int hash, byte state) {
                return 0;
            }

            @Override
            void insertKeyAt(int index, short val) {
                
            }
        };

        tShortHash.contains((short)1);
    }

    @Test
    public void testForEach() {
        TShortHash tShortHash = new TShortHash() {
            @Override
            protected void removeAt(int index) {
                
            }

            @Override
            protected int setUp(int initialCapacity) {
                return 0;
            }

            @Override
            protected int insertKey(short val) {
                return 0;
            }

            @Override
            int index(short val) {
                return 0;
            }

            @Override
            int indexRehashed(short key, int index, int hash, byte state) {
                return 0;
            }

            @Override
            void insertKeyAt(int index, short val) {
                
            }
        };

        tShortHash.forEach(new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                return true;
            }
        });
    }

    @Test
    public void testRemoveAt() {
        TShortHash tShortHash = new TShortHash() {
            @Override
            protected void removeAt(int index) {
                
            }

            @Override
            protected int setUp(int initialCapacity) {
                return 0;
            }

            @Override
            protected int insertKey(short val) {
                return 0;
            }

            @Override
            int index(short val) {
                return 0;
            }

            @Override
            int indexRehashed(short key, int index, int hash, byte state) {
                return 0;
            }

            @Override
            void insertKeyAt(int index, short val) {
                
            }
        };

        tShortHash.removeAt(0);
    }

    @Test
    public void testIndex() {
        TShortHash tShortHash = new TShortHash() {
            @Override
            protected void removeAt(int index) {
                
            }

            @Override
            protected int setUp(int initialCapacity) {
                return 0;
            }

            @Override
            protected int insertKey(short val) {
                return 0;
            }

            @Override
            int index(short val) {
                return 0;
            }

            @Override
            int indexRehashed(short key, int index, int hash, byte state) {
                return 0;
            }

            @Override
            void insertKeyAt(int index, short val) {
                
            }
        };

        tShortHash.index((short)1);
    }

    @Test
    public void testIndexRehashed() {
        TShortHash tShortHash = new TShortHash() {
            @Override
            protected void removeAt(int index) {
                
            }

            @Override
            protected int setUp(int initialCapacity) {
                return 0;
            }

            @Override
            protected int insertKey(short val) {
                return 0;
            }

            @Override
            int index(short val) {
                return 0;
            }

            @Override
            int indexRehashed(short key, int index, int hash, byte state) {
                return 0;
            }

            @Override
            void insertKeyAt(int index, short val) {
                
            }
        };

        tShortHash.indexRehashed((short)1, 0, 0, (byte)0);
    }

    @Test
    public void testInsertKey() {
        TShortHash tShortHash = new TShortHash() {
            @Override
            protected void removeAt(int index) {
                
            }

            @Override
            protected int setUp(int initialCapacity) {
                return 0;
            }

            @Override
            protected int insertKey(short val) {
                return 0;
            }

            @Override
            int index(short val) {
                return 0;
            }

            @Override
            int indexRehashed(short key, int index, int hash, byte state) {
                return 0;
            }

            @Override
            void insertKeyAt(int index, short val) {
                
            }
        };

        tShortHash.insertKey((short)1);
    }

    @Test
    public void testInsertKeyRehash() {
        TShortHash tShortHash = new TShortHash() {
            @Override
            protected void removeAt(int index) {
                
            }

            @Override
            protected int setUp(int initialCapacity) {
                return 0;
            }

            @Override
            protected int insertKey(short val) {
                return 0;
            }

            @Override
            int index(short val) {
                return 0;
            }

            @Override
            int indexRehashed(short key, int index, int hash, byte state) {
                return 0;
            }

            @Override
            void insertKeyAt(int index, short val) {
                
            }
        };

        tShortHash.insertKeyRehash((short)1, 0, 0, (byte)0);
    }

    @Test
    public void testInsertKeyAt() {
        TShortHash tShortHash = new TShortHash() {
            @Override
            protected void removeAt(int index) {
                
            }

            @Override
            protected int setUp(int initialCapacity) {
                return 0;
            }

            @Override
            protected int insertKey(short val) {
                return 0;
            }

            @Override
            int index(short val) {
                return 0;
            }

            @Override
            int indexRehashed(short key, int index, int hash, byte state) {
                return 0;
            }

            @Override
            void insertKeyAt(int index, short val) {
                
            }
        };

        tShortHash.insertKeyAt(0, (short)1);
    }
}