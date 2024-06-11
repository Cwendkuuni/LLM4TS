import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Assert;
import org.junit.Test;

public class LookupTranslatorTest {

   @Test
   public void translate_validInput_returnsCorrectTranslation() throws IOException {
      CharSequence[] lookup1 = {"cat", "dog"};
      CharSequence[] lookup2 = {"e", "f"};
      LookupTranslator lookupTranslator = new LookupTranslator(lookup1, lookup2);
      
      StringBuilder input = new StringBuilder("The cat is eating.");
      StringBuilder expectedOutput = new StringBuilder("The dog is eating.");
      
      StringBuilder output = new StringBuilder();
      lookupTranslator.translate(input, 4, output);
      
      Assert.assertEquals(expectedOutput.toString(), output.toString());
   }
   
   @Test
   public void translate_inputWithNoMatch_returnsZero() throws IOException {
      CharSequence[] lookup = {"cat", "dog"};
      LookupTranslator lookupTranslator = new LookupTranslator(lookup);
      
      StringBuilder input = new StringBuilder("The bird is singing.");
      StringBuilder output = new StringBuilder();
      
      int translatedCount = lookupTranslator.translate(input, 4, output);
      
      Assert.assertEquals(0, translatedCount);
   }
   
   @Test
   public void translate_inputShorterThanShortestLookup_returnsZero() throws IOException {
      CharSequence[] lookup = {"cat", "dog"};
      LookupTranslator lookupTranslator = new LookupTranslator(lookup);
      
      StringBuilder input = new StringBuilder("The b");
      StringBuilder output = new StringBuilder();
      
      int translatedCount = lookupTranslator.translate(input, 4, output);
      
      Assert.assertEquals(0, translatedCount);
   }
   
   @Test
   public void translate_inputLongerThanMaxLength_returnsCorrectTranslation() throws IOException {
      CharSequence[] lookup = {"cat", "dog"};
      LookupTranslator lookupTranslator = new LookupTranslator(lookup);
      
      StringBuilder input = new StringBuilder("The cat is eating");
      StringBuilder expectedOutput = new StringBuilder("The dog");
      
      StringBuilder output = new StringBuilder();
      lookupTranslator.translate(input, 4, output);
      
      Assert.assertEquals(expectedOutput.toString(), output.toString());
   }
}