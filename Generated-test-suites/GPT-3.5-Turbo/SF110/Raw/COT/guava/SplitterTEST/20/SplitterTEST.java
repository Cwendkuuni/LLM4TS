
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SplitterTest {
  
  // Test on(char separator)
  @Test
  public void testOnCharSeparator() {
    Splitter splitter = Splitter.on(',');
    assertEquals("Splitter{trimmer=CharMatcher.NONE, omitEmptyStrings=false, strategy=...}", splitter.toString());
  }
  
  // Test on(CharMatcher separatorMatcher)
  @Test
  public void testOnCharMatcherSeparator() {
    Splitter splitter = Splitter.on(CharMatcher.anyOf(",."));
    assertEquals("Splitter{trimmer=CharMatcher.NONE, omitEmptyStrings=false, strategy=...}", splitter.toString());
  }
  
  // Test on(String separator)
  @Test
  public void testOnStringSeparator() {
    Splitter splitter = Splitter.on(",");
    assertEquals("Splitter{trimmer=CharMatcher.NONE, omitEmptyStrings=false, strategy=...}", splitter.toString());
  }
  
  // Test on(Pattern separatorPattern)
  @Test
  public void testOnPatternSeparator() {
    Splitter splitter = Splitter.on(Pattern.compile(","));
    assertEquals("Splitter{trimmer=CharMatcher.NONE, omitEmptyStrings=false, strategy=...}", splitter.toString());
  }
  
  // Test onPattern(String separatorPattern)
  @Test
  public void testOnPatternStringSeparator() {
    Splitter splitter = Splitter.onPattern(",");
    assertEquals("Splitter{trimmer=CharMatcher.NONE, omitEmptyStrings=false, strategy=...}", splitter.toString());
  }
  
  // Test fixedLength(int length)
  @Test
  public void testFixedLength() {
    Splitter splitter = Splitter.fixedLength(2);
    assertEquals("Splitter{trimmer=CharMatcher.NONE, omitEmptyStrings=false, strategy=...}", splitter.toString());
  }
  
  // Test omitEmptyStrings()
  @Test
  public void testOmitEmptyStrings() {
    Splitter splitter = Splitter.on(",").omitEmptyStrings();
    assertEquals("Splitter{trimmer=CharMatcher.NONE, omitEmptyStrings=true, strategy=...}", splitter.toString());
  }
  
  // Test limit(int limit)
  @Test
  public void testLimit() {
    Splitter splitter = Splitter.on(",").limit(2);
    assertEquals("Splitter{trimmer=CharMatcher.NONE, omitEmptyStrings=false, strategy=...}", splitter.toString());
  }
  
  // Test trimResults()
  @Test
  public void testTrimResults() {
    Splitter splitter = Splitter.on(",").trimResults();
    assertEquals("Splitter{trimmer=CharMatcher.WHITESPACE, omitEmptyStrings=false, strategy=...}", splitter.toString());
  }
  
  // Test trimResults(CharMatcher trimmer)
  @Test
  public void testTrimResultsWithCharMatcher() {
    Splitter splitter = Splitter.on(",").trimResults(CharMatcher.anyOf(" "));
    assertEquals("Splitter{trimmer=CharMatcher.anyOf(\" \"), omitEmptyStrings=false, strategy=...}", splitter.toString());
  }
  
  // Test split(CharSequence sequence)
  @Test
  public void testSplit() {
    Splitter splitter = Splitter.on(",");
    Iterable<String> result = splitter.split("A,B,C");
    List<String> expected = Arrays.asList("A", "B", "C");
    assertEquals(expected, result);
  }
  
  // Test splitToList(CharSequence sequence)
  @Test
  public void testSplitToList() {
    Splitter splitter = Splitter.on(",");
    List<String> result = splitter.splitToList("A,B,C");
    List<String> expected = Arrays.asList("A", "B", "C");
    assertEquals(expected, result);
  }
  
  // Test withKeyValueSeparator(String separator)
  @Test
  public void testWithKeyValueSeparatorString() {
    Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator("=");
    assertEquals("Splitter$MapSplitter@...", mapSplitter.toString());
  }
  
  // Test withKeyValueSeparator(char separator)
  @Test
  public void testWithKeyValueSeparatorChar() {
    Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator('=');
    assertEquals("Splitter$MapSplitter@...", mapSplitter.toString());
  }
  
  // Test withKeyValueSeparator(Splitter keyValueSplitter)
  @Test
  public void testWithKeyValueSeparatorSplitter() {
    Splitter.MapSplitter mapSplitter = Splitter.on(",").withKeyValueSeparator(Splitter.on("="));
    assertEquals("Splitter$MapSplitter@...", mapSplitter.toString());
  }
}

