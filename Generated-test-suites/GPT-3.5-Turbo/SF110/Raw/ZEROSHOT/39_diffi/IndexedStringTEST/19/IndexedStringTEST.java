import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class IndexedStringTest {

    private IndexedString indexedString;

    @Before
    public void setup() {
        indexedString = new IndexedString("test");
    }

    @Test
    public void testCount() {
        int count = indexedString.count('t');
        Assert.assertEquals(2, count);
    }

    @Test
    public void testIndexOf() {
        int index = indexedString.indexOf("es".toCharArray());
        Assert.assertEquals(1, index);
    }
}