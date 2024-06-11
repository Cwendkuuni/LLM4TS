import org.junit.Test;
import org.apache.commons.lang3.text.translate.LookupTranslator;

public class LookupTranslatorTest {

    @Test
    public void testTranslate() {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"abc", "123"}, {"def", "456"}});
        
        CharSequence input = "abcdef";
        StringBuilder output = new StringBuilder();
        
        try {
            translator.translate(input, 0, output);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        assert(output.toString().equals("123456"));
    }
}