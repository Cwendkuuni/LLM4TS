import static org.junit.Assert.*;
import org.junit.Test;

public class CharSequenceTranslatorTest {

  @Test
  public void testTranslate_withNullInput_expectNullOutput() {
    CharSequenceTranslator translator = new CharSequenceTranslatorTestImpl();
    String input = null;
    String expectedOutput = null;
    
    String actualOutput = translator.translate(input);
    
    assertEquals(expectedOutput, actualOutput);
  }
  
  @Test
  public void testTranslate_withEmptyInput_expectEmptyOutput() {
    CharSequenceTranslator translator = new CharSequenceTranslatorTestImpl();
    String input = "";
    String expectedOutput = "";
    
    String actualOutput = translator.translate(input);
    
    assertEquals(expectedOutput, actualOutput);
  }
  
  @Test
  public void testTranslate_withValidInput_expectTranslatedOutput() {
    CharSequenceTranslator translator = new CharSequenceTranslatorTestImpl();
    String input = "Hello, world!";
    String expectedOutput = "Hola, mundo!";
    
    String actualOutput = translator.translate(input);
    
    assertEquals(expectedOutput, actualOutput);
  }

  private class CharSequenceTranslatorTestImpl extends CharSequenceTranslator {
    public int translate(CharSequence input, int index, Writer out) throws IOException {
      return 0;
    }
  }

}