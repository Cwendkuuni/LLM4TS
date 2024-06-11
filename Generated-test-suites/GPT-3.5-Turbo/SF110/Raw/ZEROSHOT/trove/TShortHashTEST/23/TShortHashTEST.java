import org.junit.*;
import static org.junit.Assert.*;

public class TShortHashTest {

    @Test
    public void testGetNoEntryValue() {
        TShortHash tShortHash = new TShortHash() {
            @Override
            protected int setUp(int initialCapacity) {
                return 0;
            }

            @Override
            protected int index(short val) {
                return 0;
            }

            @Override
            protected void removeAt(int index) {
                
            }

            @Override
            public boolean contains(short val) {
                return false;
            }

            @Override
            protected int insertKey(short val) {
                return 0;
            }
        };

        short no_entry_value = tShortHash.getNoEntryValue();
        assertEquals(Constants.DEFAULT_SHORT_NO_ENTRY_VALUE, no_entry_value);
    }

    @Test
    public void testContains() {
        TShortHash tShortHash = new TShortHash() {
            @Override
            protected int setUp(int initialCapacity) {
                return 0;
            }

            @Override
            protected int index(short val) {
                return 0;
            }

            @Override
            protected void removeAt(int index) {
                
            }

            @Override
            protected int insertKey(short val) {
                return 0;
            }
        };

        boolean contains = tShortHash.contains((short) 10);
        assertFalse(contains);
    }

    @Test
    public void testForEach() {
        TShortHash tShortHash = new TShortHash() {
            @Override
            protected int setUp(int initialCapacity) {
                return 0;
            }

            @Override
            public boolean forEach(TShortProcedure procedure) {
                return false;
            }

            @Override
            protected void removeAt(int index) {
                
            }

            @Override
            protected int insertKey(short val) {
                return 0;
            }
        };

        boolean forEach = tShortHash.forEach(new TShortProcedure() {
            @Override
            public boolean execute(short value) {
                return false;
            }
        });
        assertFalse(forEach);
    }

    @Test
    public void testIndex() {
        TShortHash tShortHash = new TShortHash() {
            @Override
            protected int setUp(int initialCapacity) {
                return 0;
            }

            @Override
            protected void removeAt(int index) {
                
            }

            @Override
            protected int insertKey(short val) {
                return 0;
            }
        };

        int index = tShortHash.index((short) 10);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKey() {
        TShortHash tShortHash = new TShortHash() {
            @Override
            protected int setUp(int initialCapacity) {
                return 0;
            }

            @Override
            protected void insertKeyAt(int index, short val) {

            }

            @Override
            protected void removeAt(int index) {
                
            }

            @Override
            protected int indexRehashed(short key, int index, int hash, byte state) {
                return 0;
            }
        };

        int insertKey = tShortHash.insertKey((short) 10);
        assertEquals(0, insertKey);
    }

}