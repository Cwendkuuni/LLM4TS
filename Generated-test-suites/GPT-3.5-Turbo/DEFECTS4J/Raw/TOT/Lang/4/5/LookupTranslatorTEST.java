import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringWriter;
import org.apache.commons.lang3.text.translate.*;

public class LookupTranslatorTest {

  @Test
  public void testTranslate_NormalInput() throws IOException {
    CharSequence[] lookup = {{ "key1", "value1" }, { "key2", "value2" }};
    LookupTranslator translator = new LookupTranslator(lookup);
    CharSequence input = "This is a test input";
    StringWriter writer = new StringWriter();
    
    int result = translator.translate(input, 0, writer);
    
    assertEquals(0, result);
    assertEquals(input.length(), writer.toString().length());
    assertEquals(input.toString(), writer.toString());
  }
  
  @Test
  public void testTranslate_LookupFound() throws IOException {
    CharSequence[] lookup = {{ "key1", "value1" }, { "key2", "value2" }};
    LookupTranslator translator = new LookupTranslator(lookup);
    CharSequence input = "key1key2";
    StringWriter writer = new StringWriter();
    
    int result = translator.translate(input, 0, writer);
    
    assertEquals(4, result);
    assertEquals("value1value2", writer.toString());
  }
  
  @Test
  public void testTranslate_LongestLookupFound() throws IOException {
    CharSequence[] lookup = {{ "aaa", "value1" }, { "bbbbbbb", "value2" }};
    LookupTranslator translator = new LookupTranslator(lookup);
    CharSequence input = "bbbbbbbaaa";
    StringWriter writer = new StringWriter();
    
    int result = translator.translate(input, 0, writer);
    
    assertEquals(7, result);
    assertEquals("value2value1", writer.toString());
  }
  
  @Test
  public void testTranslate_LookupNotFound() throws IOException {  
    CharSequence[] lookup = {{ "key1", "value1" }, { "key2", "value2" }};
    LookupTranslator translator = new LookupTranslator(lookup);
    CharSequence input = "noMatch";
    StringWriter writer = new StringWriter();
    
    int result = translator.translate(input, 0, writer);
    
    assertEquals(0, result);
    assertEquals("", writer.toString());
  }
  
  @Test(expected = IOException.class)
  public void testTranslate_IOError() throws IOException {
    CharSequence[] lookup = {{ "key1", "value1" }, { "key2", "value2" }};
    LookupTranslator translator = new LookupTranslator(lookup);
    CharSequence input = "key1";
    
    translator.translate(input, 0, null);
  }
  
}