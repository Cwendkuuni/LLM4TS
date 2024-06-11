package org.apache.commons.lang3.text.translate;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Locale;

public class CharSequenceTranslatorTest {

    private CharSequenceTranslator translator;

    @Before
    public void setUp() {
        translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                return 0;
            }
        };
    }

    @Test
    public void testTranslate_InputIsNull_ReturnNull() {
        String result = translator.translate(null);
        Assert.assertNull(result);
    }

    @Test
    public void testTranslate_InputIsEmpty_ReturnEmptyString() {
        String result = translator.translate("");
        Assert.assertEquals("", result);
    }

    @Test
    public void testTranslate_InputIsLowercase_ReturnUppercase() {
        String result = translator.translate("abcdefghijklmnopqrstuvwxyz");
        Assert.assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", result);
    }

    @Test
    public void testTranslate_InputIsUppercase_ReturnUppercase() {
        String result = translator.translate("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
        Assert.assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ", result);
    }

    @Test
    public void testTranslate_InputHasNumbers_ReturnNumbers() {
        String result = translator.translate("1234567890");
        Assert.assertEquals("1234567890", result);
    }

    @Test
    public void testTranslate_InputHasSpecialCharacters_ReturnSpecialCharacters() {
        String result = translator.translate("!@#$%^&*()_-+=[]{};':,.<>/?");
        Assert.assertEquals("!@#$%^&*()_-+=[]{};':,.<>/?", result);
    }

    @Test
    public void testTranslate_InputHasMixedCharacters_ReturnMixedCharacters() {
        String result = translator.translate("aBcDeFg123!@#");
        Assert.assertEquals("ABCDEFG123!@#", result);
    }

    @Test
    public void testTranslate_InputHasUnicodeCharacters_ReturnUnicodeCharacters() {
        String result = translator.translate("こんにちは");
        Assert.assertEquals("こんにちは", result);
    }

    @Test
    public void testTranslate_InputHasLongString_ReturnTranslatedString() {
        String result = translator.translate("This is a long string to be translated.");
        Assert.assertEquals("THIS IS A LONG STRING TO BE TRANSLATED.", result);
    }

    @Test
    public void testTranslate_InputHasNullValueCharacters_ReturnNullValueCharacters() {
        String result = translator.translate("\u0000\u0001\u0002\u0003\u0004");
        Assert.assertEquals("\u0000\u0001\u0002\u0003\u0004", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_OutputWriterIsNull_ThrowsIllegalArgumentException() throws IOException {
        translator.translate("example", null);
    }

    @Test
    public void testTranslate_IOExceptionOccurred_ThrowsRuntimeException() {
        CharSequenceTranslator translator = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                throw new IOException("Example IOException");
            }
        };
        try {
            translator.translate("example");
            Assert.fail("RuntimeException not thrown");
        } catch (RuntimeException e) {
            Assert.assertEquals("Example IOException", e.getMessage());
        }
    }

    @Test
    public void testTranslate_WithTranslators_ReturnTranslatedString() {
        final CharSequenceTranslator translator1 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write("A");
                return 1;
            }
        };
        final CharSequenceTranslator translator2 = new CharSequenceTranslator() {
            @Override
            public int translate(CharSequence input, int index, Writer out) throws IOException {
                out.write("B");
                return 1;
            }
        };
        CharSequenceTranslator result = translator.with(translator1, translator2);
        Assert.assertNotNull(result);

        String translated = result.translate("example");
        Assert.assertEquals("AB", translated);
    }

    @Test
    public void testHex_InputIsCharacter_ReturnHexadecimalString() {
        String hex = CharSequenceTranslator.hex('A');
        Assert.assertEquals("41", hex);
    }

    @Test
    public void testHex_InputIsEmoji_ReturnHexadecimalString() {
        String hex = CharSequenceTranslator.hex(0x1F601);
        Assert.assertEquals("1F601", hex);
    }
}