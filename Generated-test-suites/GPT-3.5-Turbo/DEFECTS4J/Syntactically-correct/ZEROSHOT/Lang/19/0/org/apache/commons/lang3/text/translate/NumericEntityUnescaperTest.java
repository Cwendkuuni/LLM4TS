import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

  @Test
  public void testTranslate() throws IOException {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    
    // Test 1
    StringBuilder input1 = new StringBuilder("&#65;");
    StringBuilder output1 = new StringBuilder("A");
    StringBuilder result1 = new StringBuilder();
    unescaper.translate(input1, 0, new java.io.StringWriter(result1));
    assertEquals(output1.toString(), result1.toString());
    
    // Test 2
    StringBuilder input2 = new StringBuilder("&#x41;");
    StringBuilder output2 = new StringBuilder("A");
    StringBuilder result2 = new StringBuilder();
    unescaper.translate(input2, 0, new java.io.StringWriter(result2));
    assertEquals(output2.toString(), result2.toString());
    
    // Test 3
    StringBuilder input3 = new StringBuilder("&#x20AC;");
    StringBuilder output3 = new StringBuilder("€");
    StringBuilder result3 = new StringBuilder();
    unescaper.translate(input3, 0, new java.io.StringWriter(result3));
    assertEquals(output3.toString(), result3.toString());
    
    // Test 4
    StringBuilder input4 = new StringBuilder("&#8364;");
    StringBuilder output4 = new StringBuilder("€");
    StringBuilder result4 = new StringBuilder();
    unescaper.translate(input4, 0, new java.io.StringWriter(result4));
    assertEquals(output4.toString(), result4.toString());
  }
}