
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import static org.junit.Assert.assertEquals;

public class LookupTranslatorTest {
    @Test
    public void testTranslate() throws IOException {
        CharSequence[] seq1 = new CharSequence[]{"ab", "123"};
        CharSequence[] seq2 = new CharSequence[]{"cd", "456"};
        CharSequence[] seq3 = new CharSequence[]{"ef", "789"};
        LookupTranslator translator = new LookupTranslator(seq1, seq2, seq3);

        StringWriter writer = new StringWriter();
        int result = translator.translate("abcd", 0, writer);
        assertEquals("Checking translation result", 2, result);
        assertEquals("Checking translated output", "123456", writer.toString());
    }
}
