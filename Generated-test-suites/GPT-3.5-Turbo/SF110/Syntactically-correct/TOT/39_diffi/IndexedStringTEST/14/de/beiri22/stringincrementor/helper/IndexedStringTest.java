import static org.junit.Assert.*;
import org.junit.Test;

public class IndexedStringTest {
    
    @Test
    public void testCount_allCharsInString() {
        String s = "abcabc";
        IndexedString indexedString = new IndexedString(s);
        
        int count = indexedString.count('a');
        
        assertEquals(2, count);
    }
    
    @Test
    public void testCount_charNotInString() {
        String s = "abcabc";
        IndexedString indexedString = new IndexedString(s);
        
        int count = indexedString.count('d');
        
        assertEquals(0, count);
    }
    
    @Test
    public void testCount_emptyString() {
        String s = "";
        IndexedString indexedString = new IndexedString(s);
        
        int count = indexedString.count('a');
        
        assertEquals(0, count);
    }
    
    @Test
    public void testIndexOf_exactMatch() {
        String s = "abcabc";
        IndexedString indexedString = new IndexedString(s);
        
        int index = indexedString.indexOf(new char[]{'a','b','c'});
        
        assertEquals(0, index);
    }
    
    @Test
    public void testIndexOf_noMatch() {
        String s = "abcabc";
        IndexedString indexedString = new IndexedString(s);
        
        int index = indexedString.indexOf(new char[]{'d','e','f'});
        
        assertEquals(-1, index);
    }
    
    @Test
    public void testIndexOf_partialMatch() {
        String s = "abcabc";
        IndexedString indexedString = new IndexedString(s);
        
        int index = indexedString.indexOf(new char[]{'b','c','d'});
        
        assertEquals(-1, index);
    }
    
    @Test
    public void testIndexOf_emptyTarget() {
        String s = "abcabc";
        IndexedString indexedString = new IndexedString(s);
        
        int index = indexedString.indexOf(new char[]{});
        
        assertEquals(-1, index);
    }
    
    @Test
    public void testIndexOf_emptyString() {
        String s = "";
        IndexedString indexedString = new IndexedString(s);
        
        int index = indexedString.indexOf(new char[]{'a','b','c'});
        
        assertEquals(-1, index);
    }
    
    @Test
    public void testIndexOf_targetLongerThanString() {
        String s = "abc";
        IndexedString indexedString = new IndexedString(s);
        
        int index = indexedString.indexOf(new char[]{'a','b','c','d'});
        
        assertEquals(-1, index);
    }
    
}