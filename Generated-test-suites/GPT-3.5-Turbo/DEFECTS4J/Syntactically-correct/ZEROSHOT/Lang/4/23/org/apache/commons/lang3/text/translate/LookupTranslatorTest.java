import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class LookupTranslatorTest {

  @Test
  public void testConstructor_withLookupTable() {
    CharSequence[] seq1 = {"a", "A"};
    CharSequence[] seq2 = {"b", "B"};
    CharSequence[][] lookup = {seq1, seq2};

    LookupTranslator translator = new LookupTranslator(lookup);

    HashMap<CharSequence, CharSequence> expectedLookupMap = new HashMap<CharSequence, CharSequence>();
    expectedLookupMap.put("a", "A");
    expectedLookupMap.put("b", "B");

    assertEquals(expectedLookupMap, translator.lookupMap);
    assertEquals(1, translator.shortest);
    assertEquals(1, translator.longest);
  }

  @Test
  public void testConstructor_withEmptyLookupTable() {
    CharSequence[][] lookup = {};

    LookupTranslator translator = new LookupTranslator(lookup);

    HashMap<CharSequence, CharSequence> expectedLookupMap = new HashMap<CharSequence, CharSequence>();

    assertEquals(expectedLookupMap, translator.lookupMap);
    assertEquals(Integer.MAX_VALUE, translator.shortest);
    assertEquals(0, translator.longest);
  }

 @Test
 public void testTranslate_withValidInput() throws IOException {
   CharSequence[] seq1 = {"a", "A"};
   CharSequence[] seq2 = {"b", "B"};
   CharSequence[][] lookup = {seq1, seq2};
   LookupTranslator translator = new LookupTranslator(lookup);

   StringBuilder output = new StringBuilder();
   int result = translator.translate("aba", 0, output);

   assertEquals("AB", output.toString());
   assertEquals(2, result);
 }

 @Test
 public void testTranslate_withInvalidInput() throws IOException {
   CharSequence[] seq1 = {"a", "A"};
   CharSequence[] seq2 = {"b", "B"};
   CharSequence[][] lookup = {seq1, seq2};
   LookupTranslator translator = new LookupTranslator(lookup);

   StringBuilder output = new StringBuilder();
   int result = translator.translate("xyz", 0, output);

   assertEquals(0, result);
 }

}