import static org.junit.Assert.*;
import org.junit.Test;

public class LookupTranslatorTest {

    @Test
    public void testTranslate_basic() {
        LookupTranslator translator = new LookupTranslator(
            new CharSequence[]{"foo", "bar"}
        );
        assertEquals("bar", translator.translate("foo", 0, new StringWriter()));
    }

    @Test
    public void testTranslate_notFound() {
        LookupTranslator translator = new LookupTranslator(
            new CharSequence[]{"foo", "bar"}
        );
        assertEquals(0, translator.translate("baz", 0, new StringWriter()));
    }

    @Test
    public void testTranslate_shortInput() {
        LookupTranslator translator = new LookupTranslator(
            new CharSequence[]{"foo", "bar"}
        );
        assertEquals(0, translator.translate("fo", 0, new StringWriter()));
    }

    @Test
    public void testTranslate_longInput() {
        LookupTranslator translator = new LookupTranslator(
            new CharSequence[]{"foo", "bar"}
        );
        assertEquals("bar", translator.translate("thisisalongfooinput", 12, new StringWriter()));
    }

    @Test
    public void testTranslate_emptyLookup() {
        LookupTranslator translator = new LookupTranslator();
        assertEquals(0, translator.translate("foo", 0, new StringWriter()));
    }

    @Test(expected = IOException.class)
    public void testTranslate_ioException() throws IOException {
        LookupTranslator translator = new LookupTranslator(
            new CharSequence[]{"foo", "bar"}
        );
        translator.translate("foo", 0, new UnwritableWriter());
    }

    private static class UnwritableWriter extends Writer {
        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            throw new IOException("Unable to write");
        }

        @Override
        public void flush() throws IOException {
        }

        @Override
        public void close() throws IOException {
        }
    }
}