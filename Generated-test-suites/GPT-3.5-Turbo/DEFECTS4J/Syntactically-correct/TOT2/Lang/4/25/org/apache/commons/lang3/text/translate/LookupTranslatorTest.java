import org.junit.Test;
import org.junit.Assert;
import java.io.StringWriter;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_SingleCharMapping() {
        CharSequence[] mapping = {{"a", "1"}};
        LookupTranslator translator = new LookupTranslator(mapping);
        
        StringWriter writer = new StringWriter();
        String input = "apple";
        int result = translator.translate(input, 0, writer);
        
        Assert.assertEquals(1, result);
        Assert.assertEquals("1", writer.toString());
    }
    
    @Test
    public void testTranslate_MultipleCharMapping() {
        CharSequence[] mapping = {{"apple", "1"}};
        LookupTranslator translator = new LookupTranslator(mapping);
        
        StringWriter writer = new StringWriter();
        String input = "apple";
        int result = translator.translate(input, 0, writer);
        
        Assert.assertEquals(5, result);
        Assert.assertEquals("1", writer.toString());
    }
    
    @Test
    public void testTranslate_NoMatch() {
        CharSequence[] mapping = {{"a", "1"}};
        LookupTranslator translator = new LookupTranslator(mapping);
        
        StringWriter writer = new StringWriter();
        String input = "banana";
        int result = translator.translate(input, 0, writer);
        
        Assert.assertEquals(0, result);
        Assert.assertEquals("", writer.toString());
    }
    
    @Test
    public void testTranslate_EdgeCase() {
        CharSequence[] mapping = {{"apple", "1"}, {"banana", "2"}, {"orange", "3"}};
        LookupTranslator translator = new LookupTranslator(mapping);
        
        StringWriter writer = new StringWriter();
        String input = "banana";
        int result = translator.translate(input, 0, writer);
        
        Assert.assertEquals(6, result);
        Assert.assertEquals("2", writer.toString());
    }
    
    @Test
    public void testTranslate_ExceptionHandling() {
        CharSequence[] mapping = {{"apple", "1"}};
        LookupTranslator translator = new LookupTranslator(mapping);
        
        StringWriter writer = new StringWriter();
        String input = "apple";
        
        try {
            int result = translator.translate(input, 10, writer);
            Assert.fail("Exception not thrown");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IndexOutOfBoundsException);
        }
    }
}