import org.junit.Assert;
import org.junit.Test;
import de.beiri22.stringincrementor.helper.IndexedString;

public class IndexedStringTest {
    
    @Test
    public void testCount_ValidChar_ReturnsCorrectCount() {
        IndexedString indexedString = new IndexedString("Hello");
        int expected = 2;
        int actual = indexedString.count('l');
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testCount_InvalidChar_ReturnsZero() {
        IndexedString indexedString = new IndexedString("Hello");
        int expected = 0;
        int actual = indexedString.count('z');
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testConstructor_ValidString_CreatesIndexCorrectly() {
        IndexedString indexedString = new IndexedString("Hello");
        int[][] expected = new int[256][];
        expected['H'] = new int[]{0};
        expected['e'] = new int[]{1};
        expected['l'] = new int[]{2, 3};
        expected['o'] = new int[]{4};
        
        Assert.assertArrayEquals(expected, indexedString.getIndex());
    }
    
    @Test
    public void testConstructor_EmptyString_CreatesEmptyIndex() {
        IndexedString indexedString = new IndexedString("");
        int[][] expected = new int[256][];
        Assert.assertArrayEquals(expected, indexedString.getIndex());
    }
    
    @Test(expected = NullPointerException.class)
    public void testConstructor_NullString_ThrowsNullPointerException() {
        IndexedString indexedString = new IndexedString(null);
    }
    
    @Test
    public void testIndexOf_ValidTarget_ReturnsCorrectIndex() {
        IndexedString indexedString = new IndexedString("Hello");
        char[] target = "el".toCharArray();
        int expected = 1;
        int actual = indexedString.indexOf(target);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testIndexOf_TargetNotFound_ReturnsMinusOne() {
        IndexedString indexedString = new IndexedString("Hello");
        char[] target = "xyz".toCharArray();
        int expected = -1;
        int actual = indexedString.indexOf(target);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testIndexOf_EmptyTarget_ReturnsMinusOne() {
        IndexedString indexedString = new IndexedString("Hello");
        char[] target = "".toCharArray();
        int expected = -1;
        int actual = indexedString.indexOf(target);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testIndexOf_TargetLargerThanValues_ReturnsMinusOne() {
        IndexedString indexedString = new IndexedString("Hello");
        char[] target = "HelloWorld".toCharArray();
        int expected = -1;
        int actual = indexedString.indexOf(target);
        Assert.assertEquals(expected, actual);
    }
    
}