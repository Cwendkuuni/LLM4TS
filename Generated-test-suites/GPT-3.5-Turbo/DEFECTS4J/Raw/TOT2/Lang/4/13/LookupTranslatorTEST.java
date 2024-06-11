import org.junit.Test;
import static org.junit.Assert.*;
import java.io.StringWriter;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_DefaultCase() {
        CharSequence[] lookup1 = { "foo", "bar" };
        LookupTranslator translator = new LookupTranslator(lookup1);
        StringWriter writer = new StringWriter();
        try {
            int result = translator.translate("foo", 0, writer);
            assertEquals(3, result);
            assertEquals("bar", writer.toString());
        } catch (Exception e) {
            fail("An exception should not have been thrown");
        }
    }

    @Test
    public void testTranslate_EmptyInput() {
        CharSequence[] lookup2 = { "foo", "bar" };
        LookupTranslator translator = new LookupTranslator(lookup2);
        StringWriter writer = new StringWriter();
        try {
            int result = translator.translate("", 0, writer);
            assertEquals(0, result);
            assertEquals("", writer.toString());
        } catch (Exception e) {
            fail("An exception should not have been thrown");
        }
    }

    @Test
    public void testTranslate_NoMatch() {
        CharSequence[] lookup3 = { "foo", "bar" };
        LookupTranslator translator = new LookupTranslator(lookup3);
        StringWriter writer = new StringWriter();
        try {
            int result = translator.translate("baz", 0, writer);
            assertEquals(0, result);
            assertEquals("", writer.toString());
        } catch (Exception e) {
            fail("An exception should not have been thrown");
        }
    }

    @Test
    public void testTranslate_ExceptionHandling() {
        CharSequence[] lookup4 = { "foo", "bar" };
        LookupTranslator translator = new LookupTranslator(lookup4);
        StringWriter writer = new StringWriter();
        try {
            translator.translate(null, 0, writer);
            fail("An IOException should have been thrown for null input");
        } catch (IOException e) {
            // Pass
        }
    }
}