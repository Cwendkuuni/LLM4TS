import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_withValidNumericEntity_returnsTranslatedCodepoint() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder sb = new StringBuilder();
        Writer writer = new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                sb.append(cbuf, off, len);
            }

            @Override
            public void flush() throws IOException {}

            @Override
            public void close() throws IOException {}
        };

        int result = unescaper.translate("&#65;", 0, writer);

        assertEquals(6, result); // 2 + (end - start) + (isHex ? 1 : 0) + 1
        assertEquals("A", sb.toString());
    }
    
    @Test
    public void testTranslate_withInvalidNumericEntity_returnsZero() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder sb = new StringBuilder();
        Writer writer = new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                sb.append(cbuf, off, len);
            }

            @Override
            public void flush() throws IOException {}

            @Override
            public void close() throws IOException {}
        };

        int result = unescaper.translate("&#INVALID;", 0, writer);

        assertEquals(0, result);
        assertEquals("", sb.toString());
    }

    @Test
    public void testTranslate_withHexadecimalNumericEntity_returnsTranslatedCodepoint() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder sb = new StringBuilder();
        Writer writer = new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                sb.append(cbuf, off, len);
            }

            @Override
            public void flush() throws IOException {}

            @Override
            public void close() throws IOException {}
        };

        int result = unescaper.translate("&#x41;", 0, writer);

        assertEquals(6, result); // 2 + (end - start) + (isHex ? 1 : 0) + 1
        assertEquals("A", sb.toString());
    }

    @Test
    public void testTranslate_withUnicodeCodepointGreaterThan0xFFFF_returnsTwoCodepoints() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder sb = new StringBuilder();
        Writer writer = new Writer() {
            @Override
            public void write(char[] cbuf, int off, int len) throws IOException {
                sb.append(cbuf, off, len);
            }

            @Override
            public void flush() throws IOException {}

            @Override
            public void close() throws IOException {}
        };

        int result = unescaper.translate("&#65536;", 0, writer);

        assertEquals(8, result); // 2 + (end - start) + (isHex ? 1 : 0) + 1
        assertEquals("\uD800\uDC00", sb.toString());
    }
}