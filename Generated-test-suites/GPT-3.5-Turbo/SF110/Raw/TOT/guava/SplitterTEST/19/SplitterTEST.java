import org.junit.Assert;
import org.junit.Test;

public class SplitterTest {

    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("hello,world");
        Assert.assertArrayEquals(new String[] {"hello", "world"}, result.toArray());
    }

    @Test
    public void testOnChar_NoSeparator() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("helloworld");
        Assert.assertArrayEquals(new String[] {"helloworld"}, result.toArray());
    }

    @Test
    public void testOnChar_EmptyString() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split("");
        Assert.assertArrayEquals(new String[] {""}, result.toArray());
    }

    @Test
    public void testOnChar_EmptyStringsOmitted() {
        Splitter splitter = Splitter.on(',').omitEmptyStrings();
        Iterable<String> result = splitter.split("hello,,world");
        Assert.assertArrayEquals(new String[] {"hello", "world"}, result.toArray());
    }

    @Test(expected = NullPointerException.class)
    public void testOnChar_NullString() {
        Splitter splitter = Splitter.on(',');
        Iterable<String> result = splitter.split(null);
        Assert.fail("NullPointerException should have been thrown.");
    }
  
    // ... continue with test cases for other methods
  
}