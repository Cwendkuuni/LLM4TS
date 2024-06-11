import org.junit.Assert;
import org.junit.Test;

public class LookupTranslatorTest {
    
    @Test
    public void testTranslate() throws IOException {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {
            {"a", "123"},
            {"b", "456"},
            {"c", "789"}
        });
        
        StringBuilder input = new StringBuilder();
        input.append("abc");
        StringBuilder output = new StringBuilder();
        translator.translate(input, 0, new java.io.StringWriter(output));
        
        Assert.assertEquals("123456789", output.toString());
    }
    
    @Test
    public void testTranslate_emptyInput() throws IOException {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {
            {"a", "123"},
            {"b", "456"},
            {"c", "789"}
        });
        
        StringBuilder input = new StringBuilder();
        StringBuilder output = new StringBuilder();
        translator.translate(input, 0, new java.io.StringWriter(output));
        
        Assert.assertEquals("", output.toString());
    }
    
    @Test
    public void testTranslate_noMatches() throws IOException {
        LookupTranslator translator = new LookupTranslator(new CharSequence[][] {
            {"a", "123"},
            {"b", "456"},
            {"c", "789"}
        });
        
        StringBuilder input = new StringBuilder();
        input.append("def");
        StringBuilder output = new StringBuilder();
        translator.translate(input, 0, new java.io.StringWriter(output));
        
        Assert.assertEquals("", output.toString());
    }
    
}