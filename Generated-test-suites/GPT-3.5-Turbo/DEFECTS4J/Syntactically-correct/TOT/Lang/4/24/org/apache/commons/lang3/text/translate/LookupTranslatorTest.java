import org.junit.Test;
import org.junit.Assert;

public class LookupTranslatorTest {

  // Test case for the constructor
  @Test
  public void testConstructor() {
    // Typical use case
    CharSequence[] lookup1 = {{"a", "A"}, {"b", "B"}, {"c", "C"}};
    LookupTranslator translator1 = new LookupTranslator(lookup1);
    Assert.assertEquals("A", translator1.lookupMap.get("a"));
    Assert.assertEquals("B", translator1.lookupMap.get("b"));
    Assert.assertEquals("C", translator1.lookupMap.get("c"));
    Assert.assertEquals(1, translator1.shortest);
    Assert.assertEquals(1, translator1.longest);
    
    // Edge cases
    CharSequence[] lookup2 = {{"A", "B"}};
    LookupTranslator translator2 = new LookupTranslator(lookup2);
    Assert.assertEquals("B", translator2.lookupMap.get("A"));
    Assert.assertEquals(1, translator2.shortest);
    Assert.assertEquals(1, translator2.longest);
    
    CharSequence[] lookup3 = {};
    LookupTranslator translator3 = new LookupTranslator(lookup3);
    Assert.assertEquals(0, translator3.lookupMap.size());
    Assert.assertEquals(Integer.MAX_VALUE, translator3.shortest);
    Assert.assertEquals(0, translator3.longest);
    
    // Error scenarios
    LookupTranslator translator4 = new LookupTranslator(null);
    Assert.assertNull(translator4.lookupMap);
    Assert.assertEquals(Integer.MAX_VALUE, translator4.shortest);
    Assert.assertEquals(0, translator4.longest);
  }

  // Test case for the translate() method
  @Test
  public void testTranslate() {
    CharSequence[] lookup = {{"ab", "A"}, {"cd", "B"}, {"ef", "C"}};
    LookupTranslator translator = new LookupTranslator(lookup);

    // Typical use cases
    StringBuilder input1 = new StringBuilder("abcdef");
    StringBuilder output1 = new StringBuilder();
    int index1 = 0;
    int result1 = translator.translate(input1, index1, output1);
    Assert.assertEquals("A", output1.toString());
    Assert.assertEquals(2, result1);
    
    StringBuilder input2 = new StringBuilder("cdefgh");
    StringBuilder output2 = new StringBuilder();
    int index2 = 0;
    int result2 = translator.translate(input2, index2, output2);
    Assert.assertEquals("B", output2.toString());
    Assert.assertEquals(2, result2);
    
    StringBuilder input3 = new StringBuilder("efghij");
    StringBuilder output3 = new StringBuilder();
    int index3 = 0;
    int result3 = translator.translate(input3, index3, output3);
    Assert.assertEquals("C", output3.toString());
    Assert.assertEquals(2, result3);
    
    // Edge cases
    StringBuilder input4 = new StringBuilder("ab");
    StringBuilder output4 = new StringBuilder();
    int index4 = 0;
    int result4 = translator.translate(input4, index4, output4);
    Assert.assertEquals("A", output4.toString());
    Assert.assertEquals(2, result4);
    
    StringBuilder input5 = new StringBuilder("cd");
    StringBuilder output5 = new StringBuilder();
    int index5 = 0;
    int result5 = translator.translate(input5, index5, output5);
    Assert.assertEquals("B", output5.toString());
    Assert.assertEquals(2, result5);
    
    StringBuilder input6 = new StringBuilder("ef");
    StringBuilder output6 = new StringBuilder();
    int index6 = 0;
    int result6 = translator.translate(input6, index6, output6);
    Assert.assertEquals("C", output6.toString());
    Assert.assertEquals(2, result6);
    
    // Error scenarios
    StringBuilder input7 = new StringBuilder("xyz");
    StringBuilder output7 = new StringBuilder();
    int index7 = 0;
    int result7 = translator.translate(input7, index7, output7);
    Assert.assertEquals(0, result7);
  }
}