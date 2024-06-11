
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IndexedStringTest {
    
    private IndexedString indexedString;
    
    @Before
    public void setup() {
        indexedString = new IndexedString("Hello World");
    }
    
    @Test
    public void testCount() {
        int count = indexedString.count('o');
        Assert.assertEquals(2, count);
    }
    
    @Test
    public void testIndexOf() {
        int index = indexedString.indexOf("llo".toCharArray());
        Assert.assertEquals(2, index);
        
        index = indexedString.indexOf("Wo".toCharArray());
        Assert.assertEquals(6, index);
        
        index = indexedString.indexOf("X".toCharArray());
        Assert.assertEquals(-1, index);
    }
    
    @Test
    public void testIndexOf_emptyArray() {
        int index = indexedString.indexOf("".toCharArray());
        Assert.assertEquals(-1, index);
    }
}
