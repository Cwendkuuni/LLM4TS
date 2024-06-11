import org.junit.Test;
import org.junit.Assert;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_typicalCase() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"a", "1"}, 
                                                          new CharSequence[]{"b", "2"}, 
                                                          new CharSequence[]{"c", "3"});
        StringBuilder output = new StringBuilder();
        translator.translate("abc", 0, output);
        Assert.assertEquals("123", output.toString());
    }

    @Test
    public void testTranslate_emptyInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"a", "1"}, 
                                                          new CharSequence[]{"b", "2"}, 
                                                          new CharSequence[]{"c", "3"});
        StringBuilder output = new StringBuilder();
        translator.translate("", 0, output);
        Assert.assertEquals("", output.toString());
    }

    @Test
    public void testTranslate_singleCharacterInput() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"a", "1"}, 
                                                          new CharSequence[]{"b", "2"}, 
                                                          new CharSequence[]{"c", "3"});
        StringBuilder output = new StringBuilder();
        translator.translate("a", 0, output);
        Assert.assertEquals("1", output.toString());
    }

    @Test
    public void testTranslate_invalidIndex() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"a", "1"}, 
                                                          new CharSequence[]{"b", "2"}, 
                                                          new CharSequence[]{"c", "3"});
        StringBuilder output = new StringBuilder();
        translator.translate("abc", -1, output);
        Assert.assertEquals("", output.toString());
    }

    @Test
    public void testTranslate_noTranslationFound() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"a", "1"}, 
                                                          new CharSequence[]{"b", "2"}, 
                                                          new CharSequence[]{"c", "3"});
        StringBuilder output = new StringBuilder();
        translator.translate("xyz", 0, output);
        Assert.assertEquals("", output.toString());
    }
}