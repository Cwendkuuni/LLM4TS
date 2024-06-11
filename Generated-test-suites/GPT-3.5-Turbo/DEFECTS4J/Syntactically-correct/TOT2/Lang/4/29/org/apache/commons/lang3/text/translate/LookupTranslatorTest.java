import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringWriter;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_basic() throws Exception {
        LookupTranslator translator = new LookupTranslator(new String[]{"aa", "bb"}, new String[]{"cc", "dd"});
        StringWriter writer = new StringWriter();
        translator.translate("aaccddee", 0, writer);
        assertEquals("bbdd", writer.toString());
    }

    @Test
    public void testTranslate_edgeCases() throws Exception {
        // Test with empty input
        LookupTranslator translator = new LookupTranslator(new String[]{"aa", "bb"});
        StringWriter writer = new StringWriter();
        translator.translate("", 0, writer);
        assertEquals("", writer.toString());
        
        // Test with input at index 0
        translator.translate("aabb", 0, writer);
        assertEquals("bbbb", writer.toString());
        
        // Test with input length equal to shortest lookup key
        translator.translate("a", 0, writer);
        assertEquals("b", writer.toString());
        
        // Test with input length equal to longest lookup key
        translator.translate("aa", 0, writer);
        assertEquals("bb", writer.toString());
    }

    @Test(expected = IOException.class)
    public void testTranslate_exception() throws Exception {
        LookupTranslator translator = new LookupTranslator(new String[]{"aa", "bb"});
        StringWriter writer = new StringWriter();
        translator.translate("aaccddee", 0, writer);
    }
    
}