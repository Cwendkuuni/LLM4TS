import org.junit.Test;
import org.apache.commons.lang3.text.translate.*;

public class CharSequenceTranslatorTest {

    @Test
    public void testTranslate() {
        CharSequenceTranslator translator = new MockTranslator();
        StringWriter writer = new StringWriter();

        try {
            translator.translate("Hello", writer);
            assert(writer.toString().equals("Hello"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTranslateWithNullInput() {
        CharSequenceTranslator translator = new MockTranslator();
        StringWriter writer = new StringWriter();

        try {
            translator.translate(null, writer);
            assert(writer.toString() == null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTranslateWithNullWriter() {
        CharSequenceTranslator translator = new MockTranslator();

        try {
            translator.translate("Hello", null);
        } catch (Exception e) {
            assert(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testTranslateWithIOException() {
        CharSequenceTranslator translator = new MockTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new IOException();
            }
        };
        StringWriter writer = new StringWriter();

        try {
            translator.translate("Hello", writer);
        } catch (Exception e) {
            assert(e instanceof RuntimeException);
        }
    }

    @Test
    public void testTranslateWithSurrogatePair() {
        CharSequenceTranslator translator = new MockTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 2;
            }
        };
        StringWriter writer = new StringWriter();

        try {
            translator.translate("Hello", writer);
            assert(writer.toString().equals("H"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTranslateWithNonSurrogatePair() {
        CharSequenceTranslator translator = new MockTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
        StringWriter writer = new StringWriter();

        try {
            translator.translate("Hello", writer);
            assert(writer.toString().equals("Hello"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testTranslateString() {
        CharSequenceTranslator translator = new MockTranslator();
        String translatedString = translator.translate("Hello");

        assert(translatedString.equals("Hello"));
    }

    @Test
    public void testTranslateStringWithNullInput() {
        CharSequenceTranslator translator = new MockTranslator();
        String translatedString = translator.translate(null);

        assert(translatedString == null);
    }

    @Test
    public void testWith() {
        CharSequenceTranslator translator1 = new MockTranslator();
        CharSequenceTranslator translator2 = new MockTranslator();
        CharSequenceTranslator mergedTranslator = translator1.with(translator2);

        assert(mergedTranslator instanceof AggregateTranslator);
    }

    @Test
    public void testHex() {
        String hexString = CharSequenceTranslator.hex(65);

        assert(hexString.equals("41"));
    }

    private class MockTranslator extends CharSequenceTranslator {
        @Override
        public int translate(CharSequence input, int index, Writer out) throws IOException {
            out.write(input.toString());
            return 0;
        }
    }
}