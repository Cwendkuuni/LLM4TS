import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;

public class LookupTranslatorTest {

    @Test
    public void testTranslate() throws IOException {
        CharSequence[] seq1 = new CharSequence[]{"abc", "xyz"};
        CharSequence[] seq2 = new CharSequence[]{"def", "uvw"};
        LookupTranslator translator = new LookupTranslator(seq1, seq2);
        
        // Test translation when input contains lookup sequence
        assertEquals(3, translator.translate("abc", 0, new StringWriter()));
        assertEquals(3, translator.translate("def", 0, new StringWriter()));
        
        // Test translation when input does not contain lookup sequence
        assertEquals(0, translator.translate("xyz", 0, new StringWriter()));
        assertEquals(0, translator.translate("uvw", 0, new StringWriter()));
    }

    @Test
    public void testTranslateWithLongInput() throws IOException {
        CharSequence[] seq1 = new CharSequence[]{"abc", "xyz"};
        CharSequence[] seq2 = new CharSequence[]{"def", "uvw"};
        LookupTranslator translator = new LookupTranslator(seq1, seq2);
        
        // Test translation of long input where lookup sequence is at different positions
        assertEquals(3, translator.translate("111abc111", 3, new StringWriter()));
        assertEquals(3, translator.translate("111def111111", 3, new StringWriter()));
    }

    @Test
    public void testTranslateWithShortInput() throws IOException {
        CharSequence[] seq1 = new CharSequence[]{"abc", "xyz"};
        CharSequence[] seq2 = new CharSequence[]{"def", "uvw"};
        LookupTranslator translator = new LookupTranslator(seq1, seq2);
        
        // Test translation of short input where lookup sequence is not present
        assertEquals(0, translator.translate("11", 0, new StringWriter()));
    }
}