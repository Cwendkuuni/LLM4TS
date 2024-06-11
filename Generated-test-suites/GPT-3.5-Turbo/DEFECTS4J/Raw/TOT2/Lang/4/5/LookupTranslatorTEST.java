import static org.junit.Assert.*;
import org.junit.Test;
import java.io.StringWriter;
import java.io.Writer;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_basicCase() throws Exception {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"foo", "bar"}});
        StringWriter writer = new StringWriter();
        
        int result = translator.translate("food", 0, writer);
        
        assertEquals(3, result);
        assertEquals("bar", writer.toString());
    }
    
    @Test
    public void testTranslate_edgeCase() throws Exception {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"foo", "bar"}});
        StringWriter writer = new StringWriter();
        
        int result = translator.translate("foodbar", 0, writer);
        
        assertEquals(3, result);
        assertEquals("bar", writer.toString());
    }
    
    @Test
    public void testTranslate_notFound() throws Exception {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{{"foo", "bar"}});
        StringWriter writer = new StringWriter();
        
        int result = translator.translate("test", 0, writer);
        
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }
    
    @Test
    public void testTranslate_emptyLookup() throws Exception {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][]{});
        StringWriter writer = new StringWriter();
        
        int result = translator.translate("foo", 0, writer);
        
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }
}