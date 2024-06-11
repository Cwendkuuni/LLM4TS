import static org.junit.Assert.*;
import org.junit.Test;

public class QuotedPrintableCodecTest {

    @Test
    public void testEncode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Hello World!";
        String expected = "Hello=20World=21";
        assertEquals(expected, codec.encode(input));
    }

    @Test
    public void testDecode() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "Hello=20World=21";
        String expected = "Hello World!";
        assertEquals(expected, codec.decode(input));
    }

    @Test
    public void testEncodeNull() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertNull(codec.encode(null));
    }

    @Test
    public void testDecodeNull() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        assertNull(codec.decode(null));
    }

    @Test
    public void testEncodeWithCustomCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-8");
        String input = "Hello World!";
        String expected = "Hello=20World=21";
        assertEquals(expected, codec.encode(input));
    }

    @Test
    public void testDecodeWithCustomCharset() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec("UTF-8");
        String input = "Hello=20World=21";
        String expected = "Hello World!";
        assertEquals(expected, codec.decode(input));
    }

    @Test
    public void testEncodeNonAsciiCharacters() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "こんにちは";
        String expected = "=E3=81=93=E3=82=93=E3=81=AB=E3=81=A1=E3=81=AF";
        assertEquals(expected, codec.encode(input));
    }

    @Test
    public void testDecodeNonAsciiCharacters() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "=E3=81=93=E3=82=93=E3=81=AB=E3=81=A1=E3=81=AF";
        String expected = "こんにちは";
        assertEquals(expected, codec.decode(input));
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidInput() {
        QuotedPrintableCodec codec = new QuotedPrintableCodec();
        String input = "=E3=81=93=E3=82=93=E3=81=AB=E3=81=A1";
        codec.decode(input);
    }
}