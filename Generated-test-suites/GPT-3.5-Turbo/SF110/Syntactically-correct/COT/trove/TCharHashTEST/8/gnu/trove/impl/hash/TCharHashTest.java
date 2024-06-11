
import org.junit.Test;

public class TCharHashTest {
    
    @Test
    public void testContains() {
        TCharHash tCharHash = new TCharHash();
        
        char value = 'a';
        tCharHash.insertKey(value);
        
        assert(tCharHash.contains(value));
    }
    
    @Test
    public void testForEach() {
        TCharHash tCharHash = new TCharHash();
        
        char value1 = 'a';
        tCharHash.insertKey(value1);
        
        char value2 = 'b';
        tCharHash.insertKey(value2);
        
        TCharProcedure procedure = new TCharProcedure() {
            public boolean execute(char value) {
                System.out.println(value);
                return true;
            }
        };
        
        assert(tCharHash.forEach(procedure));
    }
    
    @Test
    public void testRemoveAt() {
        TCharHash tCharHash = new TCharHash();
        
        char value = 'a';
        int index = tCharHash.insertKey(value);
        
        tCharHash.removeAt(index);
        
        assert(!tCharHash.contains(value));
    }
    
    @Test
    public void testIndex() {
        TCharHash tCharHash = new TCharHash();
        
        char value = 'a';
        int index = tCharHash.insertKey(value);
        
        assert(tCharHash.index(value) == index);
    }
    
    @Test
    public void testInsertKey() {
        TCharHash tCharHash = new TCharHash();
        
        char value = 'a';
        int index = tCharHash.insertKey(value);
        
        assert(tCharHash.contains(value));
        assert(tCharHash._set[index] == value);
        assert(tCharHash._states[index] == 1);
    }
}
