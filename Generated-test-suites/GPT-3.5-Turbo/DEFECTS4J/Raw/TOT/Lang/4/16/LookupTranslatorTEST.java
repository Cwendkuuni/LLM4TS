
import org.junit.Test;
import org.junit.Before;
import org.apache.commons.lang3.text.translate.LookupTranslator;
import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.*;

public class LookupTranslatorTest {

    private LookupTranslator translator;

    @Before
    public void setup() {
        translator = new LookupTranslator(new CharSequence[][]{
                {"&", "&amp;"},
                {"<", "&lt;"},
                {">", "&gt;"},
                {"\"", "&quot;"},
                {"'", "&apos;"}
        });
    }

    @Test
    public void testTranslate_typicalUseCases() throws IOException {
        assertEquals("&amp;lt;", translate("&lt;"));
        assertEquals("&apos;test&apos;", translate("'test'"));
        assertEquals("&quot;Test&quot;", translate("\"Test\""));
        assertEquals("&gt;&amp;", translate(">&"));
        assertEquals("&amp;&amp;", translate("&&"));
        assertEquals("cd", translate("cd"));
    }

    @Test
    public void testTranslate_emptyInput() throws IOException {
        assertEquals("", translate(""));
    }

    @Test
    public void testTranslate_longestInput() throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            sb.append("abc");
        }
        assertEquals("abcabcabc", translate(sb.toString()));
    }

    @Test
    public void testTranslate_nullInput() throws IOException {
        assertNull(translate(null));
    }

    private String translate(String input) throws IOException {
        StringWriter writer = new StringWriter();
        int translatedChars = translator.translate(input, 0, writer);
        if (translatedChars == 0) {
            return null;
        }
        return writer.toString();
    }
}
