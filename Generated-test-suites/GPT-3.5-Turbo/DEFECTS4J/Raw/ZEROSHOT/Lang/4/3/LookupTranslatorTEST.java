import org.junit.Before;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import static org.junit.Assert.*;

public class LookupTranslatorTest {
  
  private LookupTranslator lookupTranslator;
  
  @Before
  public void setUp() {
    lookupTranslator = new LookupTranslator(
      new CharSequence[]{"a", "1"},
      new CharSequence[]{"b", "2"},
      new CharSequence[]{"c", "3"}
    );
  }
  
  @Test
  public void testTranslate() {
    StringBuilder sb = new StringBuilder();
    try {
      lookupTranslator.translate("abc", 0, sb);
    } catch (Exception e) {
      fail("Unexpected exception");
    }
    assertEquals("123", sb.toString());
    
    sb.setLength(0);
    try {
      lookupTranslator.translate("def", 0, sb);
    } catch (Exception e) {
      fail("Unexpected exception");
    }
    assertEquals("", sb.toString());
  }
  
  @Test
  public void testTranslate_withIndex() {
    StringBuilder sb = new StringBuilder();
    try {
      lookupTranslator.translate("xyzabc", 3, sb);
    } catch (Exception e) {
      fail("Unexpected exception");
    }
    assertEquals("123", sb.toString());
    
    sb.setLength(0);
    try {
      lookupTranslator.translate("xyzdef", 3, sb);
    } catch (Exception e) {
      fail("Unexpected exception");
    }
    assertEquals("", sb.toString());
  }
  
  @Test
  public void testTranslate_withWriterError() {
    MockWriter writer = new MockWriter(true);
    try {
      lookupTranslator.translate("abc", 0, writer);
    } catch (Exception e) {
      fail("Unexpected exception");
    }
    assertEquals("123", writer.getContent());
  }
  
  @Test
  public void testTranslate_withIndexAndWriterError() {
    MockWriter writer = new MockWriter(true);
    try {
      lookupTranslator.translate("xyzabc", 3, writer);
    } catch (Exception e) {
      fail("Unexpected exception");
    }
    assertEquals("123", writer.getContent());
  }
  
  @Test
  public void testTranslate_withNullInput() {
    StringBuilder sb = new StringBuilder();
    try {
      lookupTranslator.translate(null, 0, sb);
    } catch (Exception e) {
      fail("Unexpected exception");
    }
    assertEquals("", sb.toString());
  }
  
  @Test
  public void testTranslate_withNullOutput() {
    try {
      lookupTranslator.translate("abc", 0, null);
    } catch (Exception e) {
      fail("Unexpected exception");
    }
  }
  
  @Test
  public void testTranslate_withNullInputAndOutput() {
    try {
      lookupTranslator.translate(null, 0, null);
    } catch (Exception e) {
      fail("Unexpected exception");
    }
  }
  
  @Test
  public void testTranslate_withEmptyInput() {
    StringBuilder sb = new StringBuilder();
    try {
      lookupTranslator.translate("", 0, sb);
    } catch (Exception e) {
      fail("Unexpected exception");
    }
    assertEquals("", sb.toString());
  }
  
  @Test
  public void testConstructor() {
    lookupTranslator = new LookupTranslator(
      new CharSequence[]{"a", "1"},
      new CharSequence[]{"b", "2"},
      new CharSequence[]{"c", "3"}
    );
    assertNotNull(lookupTranslator);
  }
  
  @Test
  public void testConstructor_withNullLookup() {
    lookupTranslator = new LookupTranslator(null);
    assertNotNull(lookupTranslator);
  }
  
  @Test
  public void testConstructor_withEmptyLookup() {
    lookupTranslator = new LookupTranslator(new CharSequence[0][]);
    assertNotNull(lookupTranslator);
  }
  
  @Test
  public void testConstructor_withEmptyLookupElements() {
    lookupTranslator = new LookupTranslator(new CharSequence[]{});
    assertNotNull(lookupTranslator);
  }
  
  @Test
  public void testConstructor_withNullLookupElement() {
    lookupTranslator = new LookupTranslator(new CharSequence[]{null, null});
    assertNotNull(lookupTranslator);
  }
  
  @Test
  public void testConstructor_withInvalidLookup() {
    lookupTranslator = new LookupTranslator(new CharSequence[]{"a"});
    assertNotNull(lookupTranslator);
  }
}