import org.junit.Assert;
import org.junit.Test;

public class LookupTranslatorTest {

  @Test
  public void translate_withEmptyInput_returnsZero() {
    LookupTranslator translator = new LookupTranslator();
    int result = translator.translate("", 0, new StringBuilder());
    Assert.assertEquals(0, result);
  }

  @Test
  public void translate_withEmptyLookupMap_returnsZero() {
    LookupTranslator translator = new LookupTranslator(new CharSequence[][]{});
    int result = translator.translate("abc", 0, new StringBuilder());
    Assert.assertEquals(0, result);
  }

  @Test
  public void translate_withSingleCharacterLookup_returnsOne() {
    LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"a", "A"}});
    StringBuilder output = new StringBuilder();
    int result = translator.translate("a", 0, output);
    Assert.assertEquals(1, result);
    Assert.assertEquals("A", output.toString());
  }

  @Test
  public void translate_withSingleCharacterNotFound_returnsZero() {
    LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"a", "A"}});
    int result = translator.translate("b", 0, new StringBuilder());
    Assert.assertEquals(0, result);
  }

  @Test
  public void translate_withMultipleCharacterLookup_returnsCorrectTranslation() {
    LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"ab", "AB"}});
    StringBuilder output = new StringBuilder();
    int result = translator.translate("abcd", 0, output);
    Assert.assertEquals(2, result);
    Assert.assertEquals("ABcd", output.toString());
  }

  @Test
  public void translate_withMultipleCharacterNotFound_returnsZero() {
    LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"ab", "AB"}});
    int result = translator.translate("xyz", 0, new StringBuilder());
    Assert.assertEquals(0, result);
  }

  @Test
  public void translate_withEmptyLookup_returnsZero() {
    LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"", "A"}});
    int result = translator.translate("xyz", 0, new StringBuilder());
    Assert.assertEquals(0, result);
  }

  @Test
  public void translate_withEmptyInputAndNonEmptyLookup_returnsZero() {
    LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"", "A"}});
    int result = translator.translate("", 0, new StringBuilder());
    Assert.assertEquals(0, result);
  }

  @Test
  public void translate_withInputLengthLessThanShortestLookup_returnsZero() {
    LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"abc", "ABC"}});
    int result = translator.translate("ab", 0, new StringBuilder());
    Assert.assertEquals(0, result);
  }

  @Test
  public void translate_withInputLengthEqualToShortestLookup_returnsCorrectTranslation() {
    LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"abc", "ABC"}});
    StringBuilder output = new StringBuilder();
    int result = translator.translate("abc", 0, output);
    Assert.assertEquals(3, result);
    Assert.assertEquals("ABC", output.toString());
  }

  @Test
  public void translate_withInputLengthGreaterThanLongestLookup_returnsCorrectTranslation() {
    LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"abc", "ABC"}});
    StringBuilder output = new StringBuilder();
    int result = translator.translate("abcdefgh", 0, output);
    Assert.assertEquals(3, result);
    Assert.assertEquals("ABCdefgh", output.toString());
  }

  @Test
  public void translate_withInputLengthEqualToLongestLookup_returnsCorrectTranslation() {
    LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"abc", "ABC"}});
    StringBuilder output = new StringBuilder();
    int result = translator.translate("abcdef", 0, output);
    Assert.assertEquals(3, result);
    Assert.assertEquals("ABCdef", output.toString());
  }

}