import org.junit.Assert;
import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testTranslateWithMatchingInput() throws Exception {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {
            {"a", "alpha"},
            {"b", "beta"},
            {"c", "gamma"}
        });

        StringBuilder sb = new StringBuilder();
        translator.translate("a", 0, sb);

        Assert.assertEquals("alpha", sb.toString());
    }

    @Test
    public void testTranslateWithNonMatchingInput() throws Exception {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {
            {"a", "alpha"},
            {"b", "beta"},
            {"c", "gamma"}
        });

        StringBuilder sb = new StringBuilder();
        translator.translate("z", 0, sb);

        Assert.assertEquals("", sb.toString());
    }
    
    @Test
    public void testTranslateWithMultipleMatches() throws Exception {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {
            {"ab", "alpha"},
            {"bc", "beta"},
            {"cd", "gamma"}
        });

        StringBuilder sb = new StringBuilder();
        translator.translate("abcd", 0, sb);

        Assert.assertEquals("alphabeta", sb.toString());
    }
}