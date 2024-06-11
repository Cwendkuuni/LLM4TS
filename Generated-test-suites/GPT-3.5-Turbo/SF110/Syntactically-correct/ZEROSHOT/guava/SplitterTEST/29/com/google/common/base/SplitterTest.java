import org.junit.Assert;
import org.junit.Test;

public class SplitterTest {

    @Test
    public void testOnChar() {
        Splitter splitter = Splitter.on(',');
        String input = "apple,banana,orange";
        Iterable<String> result = splitter.split(input);
        String[] expected = {"apple", "banana", "orange"};
        int index = 0;
        for(String value : result) {
            Assert.assertEquals(expected[index], value);
            index++;
        }
    }

    @Test
    public void testOnCharMatcher() {
        Splitter splitter = Splitter.on(CharMatcher.anyOf(",;"));
        String input = "apple,banana;orange";
        Iterable<String> result = splitter.split(input);
        String[] expected = {"apple", "banana", "orange"};
        int index = 0;
        for(String value : result) {
            Assert.assertEquals(expected[index], value);
            index++;
        }
    }

    @Test
    public void testOnString() {
        Splitter splitter = Splitter.on(" and ");
        String input = "apple and banana and orange";
        Iterable<String> result = splitter.split(input);
        String[] expected = {"apple", "banana", "orange"};
        int index = 0;
        for(String value : result) {
            Assert.assertEquals(expected[index], value);
            index++;
        }
    }

    @Test
    public void testOnPattern() {
        Splitter splitter = Splitter.onPattern("\\s+");
        String input = "apple banana orange";
        Iterable<String> result = splitter.split(input);
        String[] expected = {"apple", "banana", "orange"};
        int index = 0;
        for(String value : result) {
            Assert.assertEquals(expected[index], value);
            index++;
        }
    }

    @Test
    public void testFixedLength() {
        Splitter splitter = Splitter.fixedLength(2);
        String input = "applebananaorange";
        Iterable<String> result = splitter.split(input);
        String[] expected = {"ap", "pl", "eb", "an", "an", "or", "an", "ge"};
        int index = 0;
        for(String value : result) {
            Assert.assertEquals(expected[index], value);
            index++;
        }
    }

    @Test
    public void testOmitEmptyStrings() {
        Splitter splitter = Splitter.on(",").omitEmptyStrings();
        String input = "apple,,banana,,orange";
        Iterable<String> result = splitter.split(input);
        String[] expected = {"apple", "banana", "orange"};
        int index = 0;
        for(String value : result) {
            Assert.assertEquals(expected[index], value);
            index++;
        }
    }

    @Test
    public void testLimit() {
        Splitter splitter = Splitter.on(",").limit(2);
        String input = "apple,banana,orange";
        Iterable<String> result = splitter.split(input);
        String[] expected = {"apple", "banana,orange"};
        int index = 0;
        for(String value : result) {
            Assert.assertEquals(expected[index], value);
            index++;
        }
    }

    @Test
    public void testTrimResults() {
        Splitter splitter = Splitter.on(",").trimResults();
        String input = " apple , banana , orange ";
        Iterable<String> result = splitter.split(input);
        String[] expected = {"apple", "banana", "orange"};
        int index = 0;
        for(String value : result) {
            Assert.assertEquals(expected[index], value);
            index++;
        }
    }

    @Test
    public void testSplitToList() {
        Splitter splitter = Splitter.on(",");
        String input = "apple,banana,orange";
        List<String> result = splitter.splitToList(input);
        String[] expected = {"apple", "banana", "orange"};
        for(int i = 0; i < expected.length; i++) {
            Assert.assertEquals(expected[i], result.get(i));
        }
    }

    @Test
    public void testWithKeyValueSeparatorChar() {
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator(":");
        String input = "name:John,age:25,address:New York";
        Map<String, String> result = mapSplitter.split(input);
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("name", "John");
        expected.put("age", "25");
        expected.put("address", "New York");
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testWithKeyValueSeparatorString() {
        Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator("=");
        String input = "name=John,age=25,address=New York";
        Map<String, String> result = mapSplitter.split(input);
        Map<String, String> expected = new LinkedHashMap<>();
        expected.put("name", "John");
        expected.put("age", "25");
        expected.put("address", "New York");
        Assert.assertEquals(expected, result);
    }

}