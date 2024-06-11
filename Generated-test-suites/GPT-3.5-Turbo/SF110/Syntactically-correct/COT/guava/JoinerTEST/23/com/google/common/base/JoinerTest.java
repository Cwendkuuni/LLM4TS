import org.junit.Test;
import static org.junit.Assert.*;

public class JoinerTest {

  @Test
  public void testOn_String() {
    Joiner joiner = Joiner.on(",");
    assertNotNull(joiner);
    assertEquals(",", joiner.separator);
  }

  @Test
  public void testOn_Char() {
    Joiner joiner = Joiner.on(' ');
    assertNotNull(joiner);
    assertEquals(" ", joiner.separator);
  }

  @Test
  public void testAppendTo_Iterable() {
    Joiner joiner = Joiner.on(",");
    StringBuilder builder = new StringBuilder();
    Iterable<?> parts = Arrays.asList("abc", "def", "ghi");
    try {
      joiner.appendTo(builder, parts);
    } catch (IOException e) {
      fail("IOException thrown");
    }
    assertEquals("abc,def,ghi", builder.toString());
  }

  @Test
  public void testAppendTo_Iterator() {
    Joiner joiner = Joiner.on(",");
    StringBuilder builder = new StringBuilder();
    Iterator<?> parts = Arrays.asList("abc", "def", "ghi").iterator();
    try {
      joiner.appendTo(builder, parts);
    } catch (IOException e) {
      fail("IOException thrown");
    }
    assertEquals("abc,def,ghi", builder.toString());
  }

  @Test
  public void testAppendTo_ObjectArray() {
    Joiner joiner = Joiner.on(",");
    StringBuilder builder = new StringBuilder();
    Object[] parts = {"abc", "def", "ghi"};
    try {
      joiner.appendTo(builder, parts);
    } catch (IOException e) {
      fail("IOException thrown");
    }
    assertEquals("abc,def,ghi", builder.toString());
  }

  @Test
  public void testAppendTo_ObjectParams() {
    Joiner joiner = Joiner.on(",");
    StringBuilder builder = new StringBuilder();
    Object first = "abc";
    Object second = "def";
    Object[] rest = {"ghi", "jkl"};
    try {
      joiner.appendTo(builder, first, second, rest);
    } catch (IOException e) {
      fail("IOException thrown");
    }
    assertEquals("abc,def,ghi,jkl", builder.toString());
  }

  @Test
  public void testAppendTo_StringBuilder_Iterable() {
    Joiner joiner = Joiner.on(",");
    StringBuilder builder = new StringBuilder();
    Iterable<?> parts = Arrays.asList("abc", "def", "ghi");
    joiner.appendTo(builder, parts);
    assertEquals("abc,def,ghi", builder.toString());
  }

  @Test
  public void testAppendTo_StringBuilder_Iterator() {
    Joiner joiner = Joiner.on(",");
    StringBuilder builder = new StringBuilder();
    Iterator<?> parts = Arrays.asList("abc", "def", "ghi").iterator();
    joiner.appendTo(builder, parts);
    assertEquals("abc,def,ghi", builder.toString());
  }

  @Test
  public void testAppendTo_StringBuilder_ObjectArray() {
    Joiner joiner = Joiner.on(",");
    StringBuilder builder = new StringBuilder();
    Object[] parts = {"abc", "def", "ghi"};
    joiner.appendTo(builder, parts);
    assertEquals("abc,def,ghi", builder.toString());
  }

  @Test
  public void testAppendTo_StringBuilder_ObjectParams() {
    Joiner joiner = Joiner.on(",");
    StringBuilder builder = new StringBuilder();
    Object first = "abc";
    Object second = "def";
    Object[] rest = {"ghi", "jkl"};
    joiner.appendTo(builder, first, second, rest);
    assertEquals("abc,def,ghi,jkl", builder.toString());
  }

  @Test
  public void testJoin_Iterable() {
    Joiner joiner = Joiner.on(",");
    Iterable<?> parts = Arrays.asList("abc", "def", "ghi");
    String result = joiner.join(parts);
    assertEquals("abc,def,ghi", result);
  }

  @Test
  public void testJoin_Iterator() {
    Joiner joiner = Joiner.on(",");
    Iterator<?> parts = Arrays.asList("abc", "def", "ghi").iterator();
    String result = joiner.join(parts);
    assertEquals("abc,def,ghi", result);
  }

  @Test
  public void testJoin_ObjectArray() {
    Joiner joiner = Joiner.on(",");
    Object[] parts = {"abc", "def", "ghi"};
    String result = joiner.join(parts);
    assertEquals("abc,def,ghi", result);
  }

  @Test
  public void testJoin_ObjectParams() {
    Joiner joiner = Joiner.on(",");
    Object first = "abc";
    Object second = "def";
    Object[] rest = {"ghi", "jkl"};
    String result = joiner.join(first, second, rest);
    assertEquals("abc,def,ghi,jkl", result);
  }

  @Test
  public void testUseForNull() {
    Joiner joiner = Joiner.on(",").useForNull("null");
    assertNotNull(joiner);
    assertEquals("null", joiner.toString(null));
    assertEquals("abc,null,def", joiner.join("abc", null, "def"));
  }

  @Test
  public void testSkipNulls() {
    Joiner joiner = Joiner.on(",").skipNulls();
    assertNotNull(joiner);
    assertEquals("abc,def", joiner.join("abc", null, "def"));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testSkipNulls_Exception() {
    Joiner joiner = Joiner.on(",").skipNulls();
    joiner.useForNull("null");
  }

  @Test
  public void testWithKeyValueSeparator() {
    Joiner.MapJoiner mapJoiner = Joiner.on(",").withKeyValueSeparator("=");
    assertNotNull(mapJoiner);
    assertEquals("abc=123,def=456", mapJoiner.join(Map.of("abc", 123, "def", 456)));
  }

  @Test(expected = UnsupportedOperationException.class)
  public void testWithKeyValueSeparator_Exception() {
    Joiner.MapJoiner mapJoiner = Joiner.on(",").skipNulls().withKeyValueSeparator("=");
    mapJoiner.skipNulls();
  }
}