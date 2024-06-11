import org.junit.*;
import java.util.regex.Pattern;
import java.util.*;

public class SplitterTest {

  @Test
  public void testOnCharSeparator() {
    Splitter splitter = Splitter.on(',');
    String input = "hello,world";
    List<String> expected = Arrays.asList("hello", "world");
    List<String> actual = splitter.splitToList(input);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testOnCharMatcherSeparator() {
    Splitter splitter = Splitter.on(CharMatcher.is(','));
    String input = "hello,world";
    List<String> expected = Arrays.asList("hello", "world");
    List<String> actual = splitter.splitToList(input);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testOnStringSeparator() {
    Splitter splitter = Splitter.on(";");
    String input = "hello;world";
    List<String> expected = Arrays.asList("hello", "world");
    List<String> actual = splitter.splitToList(input);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testOnPatternSeparator() {
    Splitter splitter = Splitter.on(Pattern.compile(";"));
    String input = "hello;world";
    List<String> expected = Arrays.asList("hello", "world");
    List<String> actual = splitter.splitToList(input);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testFixedLength() {
    Splitter splitter = Splitter.fixedLength(2);
    String input = "helloworld";
    List<String> expected = Arrays.asList("he", "ll", "ow", "or", "ld");
    List<String> actual = splitter.splitToList(input);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testOmitEmptyStrings() {
    Splitter splitter = Splitter.on(',').omitEmptyStrings();
    String input = "hello,,world";
    List<String> expected = Arrays.asList("hello", "world");
    List<String> actual = splitter.splitToList(input);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testLimit() {
    Splitter splitter = Splitter.on(',').limit(2);
    String input = "hello,world,again";
    List<String> expected = Arrays.asList("hello", "world,again");
    List<String> actual = splitter.splitToList(input);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testTrimResults() {
    Splitter splitter = Splitter.on(',').trimResults();
    String input = " hello , world ";
    List<String> expected = Arrays.asList("hello", "world");
    List<String> actual = splitter.splitToList(input);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testTrimResultsWithCharMatcher() {
    Splitter splitter = Splitter.on(',').trimResults(CharMatcher.is(' '));
    String input = " hello , world ";
    List<String> expected = Arrays.asList("hello", "world");
    List<String> actual = splitter.splitToList(input);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testSplitToIterable() {
    Splitter splitter = Splitter.on(',');
    String input = "hello,world";
    List<String> expected = Arrays.asList("hello", "world");
    List<String> actual = new ArrayList<>(splitter.split(input));
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testSplitToList() {
    Splitter splitter = Splitter.on(',');
    String input = "hello,world";
    List<String> expected = Arrays.asList("hello", "world");
    List<String> actual = splitter.splitToList(input);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testSplitToListWithEmptyString() {
    Splitter splitter = Splitter.on(',');
    String input = "";
    List<String> expected = Collections.emptyList();
    List<String> actual = splitter.splitToList(input);
    Assert.assertEquals(expected, actual);
  }

  @Test
  public void testWithKeyValueSeparator() {
    Splitter splitter = Splitter.on(':').withKeyValueSeparator('=');
    String input = "key1=value1:key2=value2";
    Map<String, String> expected = new LinkedHashMap<>();
    expected.put("key1", "value1");
    expected.put("key2", "value2");
    Map<String, String> actual = splitter.split(input);
    Assert.assertEquals(expected, actual);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithKeyValueSeparatorInvalidEntry() {
    Splitter splitter = Splitter.on(':').withKeyValueSeparator('=');
    String input = "key1=value1:key2";
    splitter.split(input);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testWithKeyValueSeparatorDuplicateKey() {
    Splitter splitter = Splitter.on(':').withKeyValueSeparator('=');
    String input = "key1=value1:key1=value2";
    splitter.split(input);
  }

}