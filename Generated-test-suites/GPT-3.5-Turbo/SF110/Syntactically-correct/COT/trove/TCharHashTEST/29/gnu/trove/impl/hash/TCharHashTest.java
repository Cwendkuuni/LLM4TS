import org.junit.Test;

public class TCharHashTest {

    @Test
    public void testContains() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('a'); 
        tCharHash.insertKey('b'); 
        tCharHash.insertKey('c'); 
        
        assert tCharHash.contains('a') == true;
        assert tCharHash.contains('b') == true;
        assert tCharHash.contains('c') == true;
        assert tCharHash.contains('d') == false;
    }
    
    @Test
    public void testForEach() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('a'); 
        tCharHash.insertKey('b'); 
        tCharHash.insertKey('c'); 
        
        TCharProcedure procedure = new TCharProcedure() {
            public boolean execute(char value) {
                System.out.println(value);
                return true;
            }
        };
        
        assert tCharHash.forEach(procedure) == true;
    }
    
    @Test
    public void testRemoveAt() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('a'); 
        tCharHash.insertKey('b'); 
        tCharHash.insertKey('c'); 
        
        tCharHash.removeAt(1);
        
        assert tCharHash.contains('a') == true;
        assert tCharHash.contains('b') == false;
        assert tCharHash.contains('c') == true;
    }
    
    @Test
    public void testIndex() {
        TCharHash tCharHash = new TCharHash();
        tCharHash.insertKey('a'); 
        tCharHash.insertKey('b'); 
        tCharHash.insertKey('c');
        
        assert tCharHash.index('a') == 0;
        assert tCharHash.index('b') == 1;
        assert tCharHash.index('c') == 2;
        assert tCharHash.index('d') == -1;
    }
    
    @Test
    public void testInsertKey() {
        TCharHash tCharHash = new TCharHash();
        int indexA = tCharHash.insertKey('a'); 
        int indexB = tCharHash.insertKey('b'); 
        int indexC = tCharHash.insertKey('c');
        int indexD = tCharHash.insertKey('d');
        
        assert indexA == 0;
        assert indexB == 1;
        assert indexC == 2;
        assert indexD == 3;
    }
}