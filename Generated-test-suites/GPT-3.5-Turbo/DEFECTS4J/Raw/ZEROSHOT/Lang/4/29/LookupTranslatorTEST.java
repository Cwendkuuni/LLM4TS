import org.junit.Test;
import static org.junit.Assert.*;

public class LookupTranslatorTest {

    @Test
    public void testTranslate() throws Exception {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"a", "1"}, {"b", "2"}});
        
        assertEquals("1", translator.translate("a", 0, new StringWriter()).toString());
        assertEquals("2", translator.translate("b", 0, new StringWriter()).toString());
        assertEquals(0, translator.translate("c", 0, new StringWriter()).toString());
    }
}