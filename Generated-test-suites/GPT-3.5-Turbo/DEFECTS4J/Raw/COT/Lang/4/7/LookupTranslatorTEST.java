
import org.junit.Assert;
import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testTranslate() throws IOException {
        LookupTranslator translator = new LookupTranslator(
                new CharSequence[][]{{"abc", "123"}, {"def", "456"}}
        );
        
        StringBuilder output = new StringBuilder();
        translator.translate("abc def", 0, new StringWriter(output));
        
        Assert.assertEquals("123 456", output.toString());
    }
    
    @Test
    public void testTranslate_NoMatch() throws IOException {
        LookupTranslator translator = new LookupTranslator(
                new CharSequence[][]{{"abc", "123"}, {"def", "456"}}
        );
        
        StringBuilder output = new StringBuilder();
        translator.translate("xyz", 0, new StringWriter(output));
        
        Assert.assertEquals("", output.toString());
    }
    
    @Test
    public void testTranslate_EmptyInput() throws IOException {
        LookupTranslator translator = new LookupTranslator(
                new CharSequence[][]{{"abc", "123"}, {"def", "456"}}
        );
        
        StringBuilder output = new StringBuilder();
        translator.translate("", 0, new StringWriter(output));
        
        Assert.assertEquals("", output.toString());
    }
    
    @Test
    public void testTranslate_IndexOutOfBounds() throws IOException {
        LookupTranslator translator = new LookupTranslator(
                new CharSequence[][]{{"abc", "123"}, {"def", "456"}}
        );
        
        StringBuilder output = new StringBuilder();
        translator.translate("abc def", 10, new StringWriter(output));
        
        Assert.assertEquals(0, output.length());
    }
    
    @Test
    public void testTranslate_NullOutputWriter() throws IOException {
        LookupTranslator translator = new LookupTranslator(
                new CharSequence[][]{{"abc", "123"}, {"def", "456"}}
        );
        
        translator.translate("abc def", 0, null);
       
        // If no exception is thrown, the test passes
    }

}
