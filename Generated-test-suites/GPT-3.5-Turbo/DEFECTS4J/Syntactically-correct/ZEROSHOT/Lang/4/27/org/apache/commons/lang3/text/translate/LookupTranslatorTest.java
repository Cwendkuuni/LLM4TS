import org.junit.Test;
import org.junit.Assert;

public class LookupTranslatorTest {
    
    @Test
    public void testTranslate() {
        LookupTranslator translator = new LookupTranslator();
        CharSequence[] seq1 = {"a", "b"};
        CharSequence[] seq2 = {"c", "d"};
        CharSequence[][] lookup = {seq1, seq2};
        translator = new LookupTranslator(lookup);
        
        CharSequence input = "abc";
        Writer out = new Writer();
        int index = 0;
        int result = translator.translate(input, index, out);
        
        Assert.assertEquals(2, result);
        Assert.assertEquals("bd", out.toString());
    }
    
}