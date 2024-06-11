import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringWriter;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_LookupExists_Success() throws IOException {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"abc", "123"});
        StringWriter writer = new StringWriter();
        int result = translator.translate("abc", 0, writer);
        assertEquals(3, result);
        assertEquals("123", writer.toString());
    }

    @Test
    public void testTranslate_LookupDoesNotExist_NoTranslation() throws IOException {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"abc", "123"});
        StringWriter writer = new StringWriter();
        int result = translator.translate("def", 0, writer);
        assertEquals(0, result);
        assertEquals("", writer.toString());
    }

    @Test
    public void testTranslate_LongestSequence_Success() throws IOException {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"aa", "11"}, new CharSequence[]{"aaa", "111"});
        StringWriter writer = new StringWriter();
        int result = translator.translate("aaa", 0, writer);
        assertEquals(3, result);
        assertEquals("111", writer.toString());
    }

    @Test
    public void testTranslate_ShortestSequence_Success() throws IOException {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"a", "1"}, new CharSequence[]{"aa", "11"});
        StringWriter writer = new StringWriter();
        int result = translator.translate("a", 0, writer);
        assertEquals(1, result);
        assertEquals("1", writer.toString());
    }

    @Test(expected = IOException.class)
    public void testTranslate_ExceptionThrown_ErrorHandling() throws IOException {
        LookupTranslator translator = new LookupTranslator(new CharSequence[]{"abc", "123"});
        StringWriter writer = new StringWriter();
        translator.translate("abc", 0, null);
    }

}