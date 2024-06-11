import org.apache.commons.lang3.text.translate.*;
import static org.junit.Assert.*;
import org.junit.Test;

public class LookupTranslatorTest {
  
  @Test
  public void testTranslate() throws IOException {
    CharSequence[][] lookup = {{ "a", "x" }, { "b", "y" }};
    LookupTranslator translator = new LookupTranslator(lookup);
    
    StringBuilder input = new StringBuilder("abcde");
    StringBuilder output = new StringBuilder();
    
    int result = translator.translate(input, 0, output);
    assertEquals("x", output.toString());
    assertEquals(1, result);
    
    input = new StringBuilder("abcde");
    output = new StringBuilder();
    
    result = translator.translate(input, 1, output);
    assertEquals("y", output.toString());
    assertEquals(1, result);
    
    input = new StringBuilder("abcde");
    output = new StringBuilder();
    
    result = translator.translate(input, 0, output);
    assertEquals("", output.toString());
    assertEquals(0, result);
    
    input = new StringBuilder("abcde");
    output = new StringBuilder();
    
    result = translator.translate(input, 0, output);
    assertEquals("", output.toString());
    assertEquals(0, result);
  }
  
}