import org.junit.*;
import java.io.*;
import java.util.*;
import com.google.common.base.*;
import javax.annotation.*;

public class JoinerTest {
    
    @Test
    public void testJoin() {
        Joiner joiner = Joiner.on(",");
        List<String> list = Arrays.asList("one", "two", "three");
        String result = joiner.join(list);
        String expected = "one,two,three";
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testJoinWithNullValues() {
        Joiner joiner = Joiner.on(",").useForNull("NULL");
        List<String> list = Arrays.asList("one", null, "three");
        String result = joiner.join(list);
        String expected = "one,NULL,three";
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testJoinEmptyList() {
        Joiner joiner = Joiner.on(",");
        List<String> list = new ArrayList<String>();
        String result = joiner.join(list);
        String expected = "";
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testJoinWithIterator() {
        Joiner joiner = Joiner.on(",");
        List<String> list = Arrays.asList("one", "two", "three");
        Iterator<String> iterator = list.iterator();
        String result = joiner.join(iterator);
        String expected = "one,two,three";
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testJoinWithObjectArray() {
        Joiner joiner = Joiner.on(",");
        String[] array = {"one", "two", "three"};
        String result = joiner.join(array);
        String expected = "one,two,three";
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testJoinWithNullValuesInArray() {
        Joiner joiner = Joiner.on(",").useForNull("NULL");
        String[] array = {"one", null, "three"};
        String result = joiner.join(array);
        String expected = "one,NULL,three";
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testJoinWithObjects() {
        Joiner joiner = Joiner.on(",");
        String result = joiner.join("one", "two", "three");
        String expected = "one,two,three";
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testJoinWithObjectsAndNullValues() {
        Joiner joiner = Joiner.on(",").useForNull("NULL");
        String result = joiner.join("one", null, "three");
        String expected = "one,NULL,three";
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testSkipNulls() {
        Joiner joiner = Joiner.on(",").skipNulls();
        List<String> list = Arrays.asList("one", null, "three");
        String result = joiner.join(list);
        String expected = "one,three";
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testAppendToWithStringBuilder() {
        Joiner joiner = Joiner.on(",");
        List<String> list = Arrays.asList("one", "two", "three");
        StringBuilder builder = new StringBuilder();
        StringBuilder resultBuilder = joiner.appendTo(builder, list);
        String result = resultBuilder.toString();
        String expected = "one,two,three";
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testAppendToWithAppendable() throws IOException {
        Joiner joiner = Joiner.on(",");
        List<String> list = Arrays.asList("one", "two", "three");
        StringWriter writer = new StringWriter();
        StringWriter resultWriter = joiner.appendTo(writer, list);
        String result = resultWriter.toString();
        String expected = "one,two,three";
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testUseForNull() {
        Joiner joiner = Joiner.on(",").useForNull("NULL");
        List<String> list = Arrays.asList("one", null, "three");
        String result = joiner.join(list);
        String expected = "one,NULL,three";
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testWithKeyValueSeparator() throws IOException {
        Joiner joiner = Joiner.on(",").withKeyValueSeparator(":");
        Map<String, String> map = new HashMap<String, String>();
        map.put("one", "1");
        map.put("two", "2");
        map.put("three", "3");
        String result = joiner.join(map);
        String expected = "one:1,two:2,three:3";
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testAppendToWithMap() throws IOException {
        Joiner joiner = Joiner.on(",");
        Map<String, String> map = new HashMap<String, String>();
        map.put("one", "1");
        map.put("two", "2");
        map.put("three", "3");
        StringWriter writer = new StringWriter();
        StringWriter resultWriter = joiner.withKeyValueSeparator(":").appendTo(writer, map);
        String result = resultWriter.toString();
        String expected = "one:1,two:2,three:3";
        Assert.assertEquals(expected, result);
    }
    
    @Test
    public void testJoinWithMap() throws IOException {
        Joiner joiner = Joiner.on(",");
        Map<String, String> map = new HashMap<String, String>();
        map.put("one", "1");
        map.put("two", "2");
        map.put("three", "3");
        String result = joiner.withKeyValueSeparator(":").join(map);
        String expected = "one:1,two:2,three:3";
        Assert.assertEquals(expected, result);
    }
}