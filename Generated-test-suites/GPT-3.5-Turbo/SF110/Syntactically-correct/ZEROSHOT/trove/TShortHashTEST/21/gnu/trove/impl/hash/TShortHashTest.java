import org.junit.*;
import static org.junit.Assert.*;

public class TShortHashTest {

    @Before
    public void setUp() {
        // initialize the object before each test method
        TShortHash tShortHash = new TShortHash() {
            @Override
            public boolean forEach(TShortProcedure procedure) {
                return false;
            }

            @Override
            protected void removeAt(int index) {
            }

            @Override
            protected int indexRehashed(short key, int index, int hash, byte state) {
                return 0;
            }

            @Override
            protected int insertKey(short val) {
                return 0;
            }

            @Override
            int insertKeyRehash(short val, int index, int hash, byte state) {
                return 0;
            }

            @Override
            void insertKeyAt(int index, short val) {
            }
        };
    }

    @After
    public void tearDown() {
        // clean up the object after each test method
    }

    @Test
    public void testGetNoEntryValue() {
        // test code
    }
    
    @Test
    public void testSetUp() {
        // test code
    }
    
    @Test
    public void testContains() {
        // test code
    }
    
    @Test
    public void testForEach() {
        // test code
    }
    
    @Test
    public void testRemoveAt() {
        // test code
    }
    
    @Test
    public void testIndex() {
        // test code
    }
    
    @Test
    public void testIndexRehashed() {
        // test code
    }
    
    @Test
    public void testInsertKey() {
        // test code
    }
    
    @Test
    public void testInsertKeyRehash() {
        // test code
    }
    
    @Test
    public void testInsertKeyAt() {
        // test code
    }

}