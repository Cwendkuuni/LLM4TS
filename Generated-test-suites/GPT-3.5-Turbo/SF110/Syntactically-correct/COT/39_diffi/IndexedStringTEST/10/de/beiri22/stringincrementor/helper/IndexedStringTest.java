import org.junit.Assert;
import org.junit.Test;

public class IndexedStringTest {

    @Test
    public void testCount() {
        IndexedString indexedString = new IndexedString("");
        int actualResult = indexedString.count('a');
        Assert.assertEquals(0, actualResult);
    }
    
    @Test
    public void testIndexedString() {
        IndexedString indexedString = new IndexedString("abcd");
        Assert.assertNotNull(indexedString);
    }
    
    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("abcabc");
        int[] target = {'a', 'b', 'c'};
        int actualResult = indexedString.indexOf(target);
        Assert.assertEquals(0, actualResult);
    }
    
    @Test
    public void testIndexOfNotFound() {
        IndexedString indexedString = new IndexedString("abcabc");
        int[] target = {'d', 'e', 'f'};
        int actualResult = indexedString.indexOf(target);
        Assert.assertEquals(-1, actualResult);
    }
}