import static org.junit.Assert.*;
import org.junit.Test;

public class NumericEntityUnescaperTest {

  @Test
  public void testTranslate() throws Exception {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    
    // Test 1: Numeric entity with decimal value
    String input1 = "&#65;";
    StringBuilder output1 = new StringBuilder();
    unescaper.translate(input1, 0, output1);
    assertEquals("A", output1.toString());
    
    // Test 2: Numeric entity with hexadecimal value
    String input2 = "&#x48;";
    StringBuilder output2 = new StringBuilder();
    unescaper.translate(input2, 0, output2);
    assertEquals("H", output2.toString());
    
    // Test 3: Numeric entity without semicolon
    String input3 = "&#105";
    StringBuilder output3 = new StringBuilder();
    unescaper.translate(input3, 0, output3);
    assertEquals("", output3.toString());
    
    // Test 4: Numeric entity with non-numeric value
    String input4 = "&#x123G;";
    StringBuilder output4 = new StringBuilder();
    unescaper.translate(input4, 0, output4);
    assertEquals("", output4.toString());
    
    // Test 5: Entity with prefix but no numeric value
    String input5 = "&#";
    StringBuilder output5 = new StringBuilder();
    unescaper.translate(input5, 0, output5);
    assertEquals("", output5.toString());
  }

}