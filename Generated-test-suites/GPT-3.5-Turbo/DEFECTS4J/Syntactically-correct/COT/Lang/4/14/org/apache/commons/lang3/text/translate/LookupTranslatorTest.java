
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

public class LookupTranslatorTest {
  private LookupTranslator lookupTranslator;

  @Before
  public void setUp() {
    CharSequence[] lookup1 = { "foo", "bar" };
    CharSequence[] lookup2 = { "baz", "qux" };

    lookupTranslator = new LookupTranslator(lookup1, lookup2);
  }

  @Test
  public void testTranslate() throws IOException {
    String input = "foo baz";
    StringWriter out = new StringWriter();
    int result = lookupTranslator.translate(input, 0, out);

    Assert.assertEquals("bar qux", out.toString());
    Assert.assertEquals(7, result);

    input = "hello world";
    out = new StringWriter();
    result = lookupTranslator.translate(input, 0, out);

    Assert.assertEquals("", out.toString());
    Assert.assertEquals(0, result);
  }

  @Test
  public void testTranslate_withPartialMatch() throws IOException {
    String input = "fo";
    StringWriter out = new StringWriter();
    int result = lookupTranslator.translate(input, 0, out);

    Assert.assertEquals("", out.toString());
    Assert.assertEquals(0, result);
  }

  @Test
  public void testTranslate_withLongestMatch() throws IOException {
    String input = "bazfoo";
    StringWriter out = new StringWriter();
    int result = lookupTranslator.translate(input, 0, out);

    Assert.assertEquals("quxbar", out.toString());
    Assert.assertEquals(7, result);
  }

  @Test
  public void testTranslate_withNoMatch() throws IOException {
    String input = "abc";
    StringWriter out = new StringWriter();
    int result = lookupTranslator.translate(input, 0, out);

    Assert.assertEquals("", out.toString());
    Assert.assertEquals(0, result);
  }
}