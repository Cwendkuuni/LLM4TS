import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.lang3.text.translate.LookupTranslator;

public class LookupTranslatorTest {
    
    @Test
    public void testTranslate() {
        LookupTranslator translator = new LookupTranslator(new String[]{"a", "1"}, new String[]{"b", "2"});
        StringBuilder output = new StringBuilder();
        
        try {
            int translated = translator.translate("abc", 0, output);
            
            assertEquals(1, translated);
            assertEquals("1", output.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testEmptyInput() {
        LookupTranslator translator = new LookupTranslator(new String[]{"a", "1"}, new String[]{"b", "2"});
        StringBuilder output = new StringBuilder();
        
        try {
            int translated = translator.translate("", 0, output);
            
            assertEquals(0, translated);
            assertEquals("", output.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testNoMatch() {
        LookupTranslator translator = new LookupTranslator(new String[]{"a", "1"}, new String[]{"b", "2"});
        StringBuilder output = new StringBuilder();
        
        try {
            int translated = translator.translate("xyz", 0, output);
            
            assertEquals(0, translated);
            assertEquals("", output.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testEdgeCases() {
        LookupTranslator translator = new LookupTranslator(new String[]{"ab", "12"}, new String[]{"bcd", "345"});
        StringBuilder output = new StringBuilder();
        
        try {
            int translated = translator.translate("abcdbc", 0, output);
            
            assertEquals(2, translated);
            assertEquals("12345", output.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
    
    @Test
    public void testUnexpectedInput() {
        LookupTranslator translator = new LookupTranslator();
        StringBuilder output = new StringBuilder();
        
        try {
            int translated = translator.translate("unexpected", 0, output);
            
            assertEquals(0, translated);
            assertEquals("", output.toString());
        } catch (Exception e) {
            fail("Exception thrown: " + e.getMessage());
        }
    }
}