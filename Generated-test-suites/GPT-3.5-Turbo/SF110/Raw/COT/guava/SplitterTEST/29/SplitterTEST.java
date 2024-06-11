import static org.junit.Assert.*;
import org.junit.Test;

public class SplitterTest {

  @Test
  public void testOnChar() {
    Splitter splitter = Splitter.on(';');
    Iterable<String> result = splitter.split("abc;def;ghi");
    assertEquals("abc", result.iterator().next());
  }

  @Test
  public void testOnCharMatcher() {
    Splitter splitter = Splitter.on(CharMatcher.is(':'));
    Iterable<String> result = splitter.split("abc:def:ghi");
    assertEquals("abc", result.iterator().next());
  }

  @Test
  public void testOnString() {
    Splitter splitter = Splitter.on(":");
    Iterable<String> result = splitter.split("abc:def:ghi");
    assertEquals("abc", result.iterator().next());
  }

  @Test
  public void testOnPattern() {
    Splitter splitter = Splitter.onPattern("\\s+");
    Iterable<String> result = splitter.split("abc def ghi");
    assertEquals("abc", result.iterator().next());
  }

  @Test
  public void testFixedLength() {
    Splitter splitter = Splitter.fixedLength(3);
    Iterable<String> result = splitter.split("abcdefghi");
    assertEquals("abc", result.iterator().next());
  }

  @Test
  public void testOmitEmptyStrings() {
    Splitter splitter = Splitter.on(":").omitEmptyStrings();
    Iterable<String> result = splitter.split("abc::def:ghi");
    assertEquals("abc", result.iterator().next());
  }

  @Test
  public void testLimit() {
    Splitter splitter = Splitter.on(":").limit(2);
    Iterable<String> result = splitter.split("abc:def:ghi");
    assertEquals("abc", result.iterator().next());
  }

  @Test
  public void testTrimResults() {
    Splitter splitter = Splitter.on(":").trimResults();
    Iterable<String> result = splitter.split("   abc   :   def   :   ghi   ");
    assertEquals("abc", result.iterator().next());
  }

  @Test
  public void testTrimResultsCharMatcher() {
    Splitter splitter = Splitter.on(":").trimResults(CharMatcher.is(' '));
    Iterable<String> result = splitter.split("   abc   :   def   :   ghi   ");
    assertEquals("abc", result.iterator().next());
  }

  @Test
  public void testSplit() {
    Splitter splitter = Splitter.on(":");
    Iterable<String> result = splitter.split("abc:def:ghi");
    assertEquals("abc", result.iterator().next());
  }

  @Test
  public void testSplitToList() {
    Splitter splitter = Splitter.on(":");
    List<String> result = splitter.splitToList("abc:def:ghi");
    assertEquals("abc", result.get(0));
  }

  @Test
  public void testWithKeyValueSeparator() {
    Splitter splitter = Splitter.on(":").withKeyValueSeparator("=");
    Splitter.MapSplitter mapSplitter = splitter.withKeyValueSeparator("=");
    Map<String, String> result = mapSplitter.split("key1=value1:key2=value2");
    assertEquals("value1", result.get("key1"));
  }
}