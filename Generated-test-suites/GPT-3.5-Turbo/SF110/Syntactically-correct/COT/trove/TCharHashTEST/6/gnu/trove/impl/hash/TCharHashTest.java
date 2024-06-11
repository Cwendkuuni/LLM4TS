import org.junit.Before;
import org.junit.Test;

public class TCharHashTest {
    
    private TCharHash tCharHash;

    @Before
    public void setup() {
        tCharHash = new TCharHash() {
            @Override
            protected void insertKeyAt(int index, char val) {
                // Placeholder for testing purposes
            }
        };
    }
    
    @Test
    public void testContains() {
        tCharHash.insertKey('a');
        assert(tCharHash.contains('a'));
        assert(!tCharHash.contains('b'));
    }
    
    @Test
    public void testForEach() {
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        tCharHash.insertKey('c');
        
        TCharProcedure procedure = new TCharProcedure() {
            StringBuilder stringBuilder = new StringBuilder();
            
            @Override
            public boolean execute(char value) {
                stringBuilder.append(value);
                return true;
            }
            
            public String getResult() {
                return stringBuilder.toString();
            }
        };
        
        tCharHash.forEach(procedure);
        
        assert(procedure.getResult().equals("abc"));
    }
    
    @Test
    public void testRemoveAt() {
        tCharHash.insertKey('a');
        tCharHash.insertKey('b');
        
        tCharHash.removeAt(0);
        
        assert(!tCharHash.contains('a'));
    }
    
    @Test
    public void testInsertKey() {
        assert(tCharHash.insertKey('a') == 0);
        assert(tCharHash.insertKey('b') == 1);
    }
}