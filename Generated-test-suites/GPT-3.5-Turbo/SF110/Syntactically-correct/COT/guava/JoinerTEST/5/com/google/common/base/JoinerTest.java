import static org.junit.Assert.*;
import java.io.*;
import java.util.*;
import org.junit.*;

public class JoinerTest {

  //public static Joiner on(final String separator)
  @Test
  public void testOnString() {
    Joiner joiner = Joiner.on(", ");
    assertNotNull(joiner);

    joiner = Joiner.on("");
    assertNotNull(joiner);
  }

  //public static Joiner on(final char separator)
  @Test
  public void testOnChar() {
    Joiner joiner = Joiner.on(',');
    assertNotNull(joiner);
  }

  //<A extends Appendable> A appendTo(final A appendable, final Iterable<?> parts) throws IOException
  @Test
  public void testAppendToIterable() throws IOException {
    Joiner joiner = Joiner.on(", ");
    StringBuilder stringBuilder = new StringBuilder();
    Iterable<String> parts = Arrays.asList("one", "two", "three");
    StringBuilder result = joiner.appendTo(stringBuilder, parts);
    assertEquals("one, two, three", result.toString());

    parts = Collections.emptyList();
    result = joiner.appendTo(stringBuilder, parts);
    assertEquals("", result.toString());
  }

  //<A extends Appendable> A appendTo(final A appendable, final Iterator<?> parts) throws IOException
  @Test
  public void testAppendToIterator() throws IOException {
    Joiner joiner = Joiner.on(", ");
    StringBuilder stringBuilder = new StringBuilder();
    List<String> parts = Arrays.asList("one", "two", "three");
    StringBuilder result = joiner.appendTo(stringBuilder, parts.iterator());
    assertEquals("one, two, three", result.toString());

    parts = Collections.emptyList();
    result = joiner.appendTo(stringBuilder, parts.iterator());
    assertEquals("", result.toString());
  }

  //public final <A extends Appendable> A appendTo(final A appendable, final Object[] parts) throws IOException
  @Test
  public void testAppendToObjectArray() throws IOException {
    Joiner joiner = Joiner.on(", ");
    StringBuilder stringBuilder = new StringBuilder();
    Object[] parts = new Object[]{"one", "two", "three"};
    StringBuilder result = joiner.appendTo(stringBuilder, parts);
    assertEquals("one, two, three", result.toString());

    parts = new Object[]{};
    result = joiner.appendTo(stringBuilder, parts);
    assertEquals("", result.toString());
  }

  //public final <A extends Appendable> A appendTo(final A appendable, @Nullable final Object first, @Nullable final Object second, final Object... rest) throws IOException
  @Test
  public void testAppendToObjects() throws IOException {
    Joiner joiner = Joiner.on(", ");
    StringBuilder stringBuilder = new StringBuilder();
    StringBuilder result = joiner.appendTo(stringBuilder, "one", "two", "three");
    assertEquals("one, two, three", result.toString());

    result = joiner.appendTo(stringBuilder);
    assertEquals("", result.toString());
  }

  //public final StringBuilder appendTo(final StringBuilder builder, final Iterable<?> parts)
  @Test
  public void testAppendToBuilderIterable() {
    Joiner joiner = Joiner.on(", ");
    StringBuilder stringBuilder = new StringBuilder();
    Iterable<String> parts = Arrays.asList("one", "two", "three");
    StringBuilder result = joiner.appendTo(stringBuilder, parts);
    assertEquals("one, two, three", result.toString());

    parts = Collections.emptyList();
    result = joiner.appendTo(stringBuilder, parts);
    assertEquals("", result.toString());
  }

  //public final StringBuilder appendTo(final StringBuilder builder, final Iterator<?> parts)
  @Test
  public void testAppendToBuilderIterator() {
    Joiner joiner = Joiner.on(", ");
    StringBuilder stringBuilder = new StringBuilder();
    List<String> parts = Arrays.asList("one", "two", "three");
    StringBuilder result = joiner.appendTo(stringBuilder, parts.iterator());
    assertEquals("one, two, three", result.toString());

    parts = Collections.emptyList();
    result = joiner.appendTo(stringBuilder, parts.iterator());
    assertEquals("", result.toString());
  }

  //public final StringBuilder appendTo(final StringBuilder builder, final Object[] parts)
  @Test
  public void testAppendToBuilderObjectArray() {
    Joiner joiner = Joiner.on(", ");
    StringBuilder stringBuilder = new StringBuilder();
    Object[] parts = new Object[]{"one", "two", "three"};
    StringBuilder result = joiner.appendTo(stringBuilder, parts);
    assertEquals("one, two, three", result.toString());

    parts = new Object[]{};
    result = joiner.appendTo(stringBuilder, parts);
    assertEquals("", result.toString());
  }

  //public final StringBuilder appendTo(final StringBuilder builder, @Nullable final Object first, @Nullable final Object second, final Object... rest)
  @Test
  public void testAppendToBuilderObjects() {
    Joiner joiner = Joiner.on(", ");
    StringBuilder stringBuilder = new StringBuilder();
    StringBuilder result = joiner.appendTo(stringBuilder, "one", "two", "three");
    assertEquals("one, two, three", result.toString());

    result = joiner.appendTo(stringBuilder);
    assertEquals("", result.toString());
  }

  //public final String join(final Iterable<?> parts)
  @Test
  public void testJoinIterable() {
    Joiner joiner = Joiner.on(", ");
    Iterable<String> parts = Arrays.asList("one", "two", "three");
    String result = joiner.join(parts);
    assertEquals("one, two, three", result);

    parts = Collections.emptyList();
    result = joiner.join(parts);
    assertEquals("", result);
  }

  //public final String join(final Iterator<?> parts)
  @Test
  public void testJoinIterator() {
    Joiner joiner = Joiner.on(", ");
    List<String> parts = Arrays.asList("one", "two", "three");
    String result = joiner.join(parts.iterator());
    assertEquals("one, two, three", result);

    parts = Collections.emptyList();
    result = joiner.join(parts.iterator());
    assertEquals("", result);
  }

  //public final String join(final Object[] parts)
  @Test
  public void testJoinArray() {
    Joiner joiner = Joiner.on(", ");
    Object[] parts = new Object[]{"one", "two", "three"};
    String result = joiner.join(parts);
    assertEquals("one, two, three", result);

    parts = new Object[]{};
    result = joiner.join(parts);
    assertEquals("", result);
  }

  //public final String join(@Nullable final Object first, @Nullable final Object second, final Object... rest)
  @Test
  public void testJoinObjects() {
    Joiner joiner = Joiner.on(", ");
    String result = joiner.join("one", "two", "three");
    assertEquals("one, two, three", result);

    result = joiner.join();
    assertEquals("", result);
  }

  //@CheckReturnValue
  //public Joiner useForNull(final String nullText)
  @Test
  public void testUseForNull() {
    Joiner joiner = Joiner.on(", ").useForNull("NULL");
    StringBuilder stringBuilder = new StringBuilder();
    List<String> parts = Arrays.asList("one", null, "three");
    StringBuilder result = joiner.appendTo(stringBuilder, parts.iterator());
    assertEquals("one, NULL, three", result.toString());
  }

  //@CheckReturnValue
  //public Joiner skipNulls()
  @Test
  public void testSkipNulls() throws IOException {
    Joiner joiner = Joiner.on(", ").skipNulls();
    StringBuilder stringBuilder = new StringBuilder();
    List<String> parts = Arrays.asList("one", null, "three");
    StringBuilder result = joiner.appendTo(stringBuilder, parts.iterator());
    assertEquals("one, three", result.toString());
  }

  //@CheckReturnValue
  //public MapJoiner withKeyValueSeparator(final String keyValueSeparator)
  @Test
  public void testWithKeyValueSeparator() throws IOException {
    Joiner.MapJoiner mapJoiner = Joiner.on(", ").withKeyValueSeparator(":");
    StringBuilder stringBuilder = new StringBuilder();
    Map<String, String> map = new HashMap<>();
    map.put("key1", "value1");
    map.put("key2", "value2");
    StringBuilder result = mapJoiner.appendTo(stringBuilder, map);
    assertEquals("key1:value1, key2:value2", result.toString());
  }

  //private static Iterable<Object> iterable(final Object first, final Object second, final Object[] rest)
  @Test
  public void testIterable() {
    Object[] rest = new Object[]{"one", "two", "three"};
    Iterable<Object> iterable = Joiner.iterable("first", "second", rest);
    assertNotNull(iterable);
  }

  //CharSequence toString(final Object part)
  @Test
  public void testToString() {
    Joiner joiner = Joiner.on(", ");
    String str = joiner.toString("test");
    assertEquals("test", str);

    str = joiner.toString(null);
    assertNull(str);
  }

  //@GwtCompatible
  public void testUseForNull_Exception() {
    Joiner joiner = Joiner.on(", ").useForNull("NULL");

    try {
      joiner.useForNull("NULL");
      fail("Expected UnsupportedOperationException not thrown");
    } catch (UnsupportedOperationException e) {
      assertTrue(e.getMessage().contains("already specified useForNull"));
    }
  }

  //@GwtCompatible
  public void testSkipNulls_Exception() {
    Joiner joiner = Joiner.on(", ").skipNulls();

    try {
      joiner.skipNulls();
      fail("Expected UnsupportedOperationException not thrown");
    } catch (UnsupportedOperationException e) {
      assertTrue(e.getMessage().contains("already specified skipNulls"));
    }
  }

  //@GwtCompatible
  public void testWithKeyValueSeparator_Exception() {
    Joiner.MapJoiner mapJoiner = Joiner.on(", ").withKeyValueSeparator(":");

    try {
      mapJoiner.withKeyValueSeparator(":");
      fail("Expected UnsupportedOperationException not thrown");
    } catch (UnsupportedOperationException e) {
      assertTrue(e.getMessage().contains("can't use .skipNulls() with maps"));
    }
  }
}