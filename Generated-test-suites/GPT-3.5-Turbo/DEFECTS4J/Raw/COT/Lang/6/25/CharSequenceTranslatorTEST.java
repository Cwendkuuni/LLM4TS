import org.junit.Test;
import org.apache.commons.lang3.text.translate.CharSequenceTranslator;

public class CharSequenceTranslatorTest {
  
  @Test
  public void testTranslate() {
    CharSequenceTranslator translator = new CharSequenceTranslator() {
      @Override
      public int translate(CharSequence input, int index, Writer out) throws IOException {
        out.write("Translated: " + input.charAt(index));
        return 1;
      }
    };
    
    String input = "Hello";
    String expectedOutput = "Translated: HTranslated: eTranslated: lTranslated: lTranslated: o";
    String actualOutput = translator.translate(input);
    
    assertEquals(expectedOutput, actualOutput);
  }
  
  @Test
  public void testWith() {
    CharSequenceTranslator translator1 = new CharSequenceTranslator() {
      @Override
      public int translate(CharSequence input, int index, Writer out) throws IOException {
        out.write("Translated1: " + input.charAt(index));
        return 1;
      }
    };
    
    CharSequenceTranslator translator2 = new CharSequenceTranslator() {
      @Override
      public int translate(CharSequence input, int index, Writer out) throws IOException {
        out.write("Translated2: " + input.charAt(index));
        return 1;
      }
    };
    
    CharSequenceTranslator combinedTranslator = translator1.with(translator2);
    
    String input = "Hello";
    String expectedOutput = "Translated1: HTranslated2: eTranslated1: lTranslated2: lTranslated1: o";
    String actualOutput = combinedTranslator.translate(input);
    
    assertEquals(expectedOutput, actualOutput);
  }
  
  @Test
  public void testHex() {
    int codepoint = 65;
    String expectedOutput = "41";
    String actualOutput = CharSequenceTranslator.hex(codepoint);
    
    assertEquals(expectedOutput, actualOutput);
  }
  
  private void assertEquals(String expected, String actual) {
    org.junit.Assert.assertEquals(expected, actual);
  }
}