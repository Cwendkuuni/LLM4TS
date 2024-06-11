import org.junit.Before;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.LookupTranslator;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import static org.junit.Assert.*;

public class LookupTranslatorTest {

    private LookupTranslator lookupTranslator;

    @Before
    public void setUp() {
        lookupTranslator = new LookupTranslator(
                new CharSequence[][]{
                        {"abc", "123"},
                        {"def", "456"},
                        {"ghi", "789"},
                        {"123", "xyz"}
                });
    }

    @Test
    public void testTranslate_WithValidInput_ShouldReturnCorrectResult() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(outputStream);

        int result = lookupTranslator.translate("abc", 0, writer);

        writer.flush();
        assertEquals(3, result);
        assertEquals("123", outputStream.toString());
    }

    @Test
    public void testTranslate_WithValidInputAndOffset_ShouldReturnCorrectResult() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(outputStream);

        int result = lookupTranslator.translate("abcdefghi", 3, writer);

        writer.flush();
        assertEquals(3, result);
        assertEquals("123", outputStream.toString());
    }

    @Test
    public void testTranslate_WithValidInputAndOffsetAndWriter_ShouldReturnCorrectResult() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(byteArrayOutputStream);

        int result = lookupTranslator.translate("abcdefghi", 3, writer);

        writer.flush();
        assertEquals(3, result);
        assertEquals("123", byteArrayOutputStream.toString());
    }

    @Test
    public void testTranslate_WithValidInputAndOffsetAndWriterAndMultipleMatches_ShouldReturnCorrectResult() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(byteArrayOutputStream);

        int result = lookupTranslator.translate("abcdefghi", 0, writer);

        writer.flush();
        assertEquals(3, result);
        assertEquals("123", byteArrayOutputStream.toString());
    }

    @Test
    public void testTranslate_WithValidInputAndOffsetAndWriterAndNoMatch_ShouldReturnCorrectResult() throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(byteArrayOutputStream);

        int result = lookupTranslator.translate("xyz", 0, writer);

        writer.flush();
        assertEquals(0, result);
        assertEquals("", byteArrayOutputStream.toString());
    }

    @Test
    public void testTranslate_WithEmptyInput_ShouldReturnZero() throws IOException {
        OutputStream outputStream = new ByteArrayOutputStream();
        Writer writer = new OutputStreamWriter(outputStream);

        int result = lookupTranslator.translate("", 0, writer);

        writer.flush();
        assertEquals(0, result);
        assertEquals("", outputStream.toString());
    }

}