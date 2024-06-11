import static org.junit.Assert.*;
import org.junit.Test;

public class NumericEntityUnescaperTest {
  
  @Test
  public void testTranslate() {
    NumericEntityUnescaper translator = new NumericEntityUnescaper();
    
    // Typical use case
    StringBuilder input1 = new StringBuilder("&#65;"); // should translate to A
    StringBuilder output1 = new StringBuilder();
    try {
      int translated = translator.translate(input1, 0, output1);
      assertEquals("Translated value is incorrect", 4, translated);
      assertEquals("Output is incorrect", "A", output1.toString());
    } catch (Exception e) {
      fail("Exception thrown during translation");
    }
    
    // Edge case: minimum input
    StringBuilder input2 = new StringBuilder("&#0;"); // should translate to null character
    StringBuilder output2 = new StringBuilder();
    try {
      int translated = translator.translate(input2, 0, output2);
      assertEquals("Translated value is incorrect", 4, translated);
      assertEquals("Output is incorrect", "\u0000", output2.toString());
    } catch (Exception e) {
      fail("Exception thrown during translation");
    }
    
    // Edge case: maximum input
    StringBuilder input3 = new StringBuilder("&#65535;"); // should translate to U+FFFF
    StringBuilder output3 = new StringBuilder();
    try {
      int translated = translator.translate(input3, 0, output3);
      assertEquals("Translated value is incorrect", 8, translated);
      assertEquals("Output is incorrect", "\uFFFF", output3.toString());
    } catch (Exception e) {
      fail("Exception thrown during translation");
    }
    
    // Error scenario: invalid character after &#
    StringBuilder input4 = new StringBuilder("&#x;"); // invalid character after x
    StringBuilder output4 = new StringBuilder();
    try {
      int translated = translator.translate(input4, 0, output4);
      fail("Expected NumberFormatException not thrown");
    } catch (NumberFormatException e) {
      // success
    } catch (Exception e) {
      fail("Unexpected exception thrown");
    }
    
    // Error scenario: invalid character before ;
    StringBuilder input5 = new StringBuilder("&#25z;"); // invalid character before ;
    StringBuilder output5 = new StringBuilder();
    try {
      int translated = translator.translate(input5, 0, output5);
      fail("Expected NumberFormatException not thrown");
    } catch (NumberFormatException e) {
      // success
    } catch (Exception e) {
      fail("Unexpected exception thrown");
    }
  }

}