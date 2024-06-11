
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import java.io.IOException;
import java.io.Writer;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class NumericEntityUnescaperTest {

    private NumericEntityUnescaper numericEntityUnescaper;
    
    @Before
    public void setUp() {
        numericEntityUnescaper = new NumericEntityUnescaper();
    }

    @Test
    public void testTranslate_basicInput() throws IOException {
        Writer writerMock = mock(Writer.class);
        CharSequence input = "&#65;";
        int index = 0;

        int result = numericEntityUnescaper.translate(input, index, writerMock);

        assertEquals(5, result);
        verify(writerMock).write(65);
    }

    // Edge case: Empty input
    @Test
    public void testTranslate_emptyInput() throws IOException {
        Writer writerMock = mock(Writer.class);
        CharSequence input = "";
        int index = 0;

        int result = numericEntityUnescaper.translate(input, index, writerMock);

        assertEquals(0, result);
    }

    // Edge case: Input less than two characters
    @Test
    public void testTranslate_inputLessThanTwoCharacters() throws IOException {
        Writer writerMock = mock(Writer.class);
        CharSequence input = "&";
        int index = 0;

        int result = numericEntityUnescaper.translate(input, index, writerMock);

        assertEquals(0, result);
    }

    // Edge case: Input with only "&#" but no following characters
    @Test
    public void testTranslate_inputOnlyContainsHexPrefix() throws IOException {
        Writer writerMock = mock(Writer.class);
        CharSequence input = "&#";
        int index = 0;

        int result = numericEntityUnescaper.translate(input, index, writerMock);

        assertEquals(0, result);
    }

    // Edge case: Input with "&#" and following "x" but no digit
    @Test
    public void testTranslate_inputWithHexPrefixButNoDigit() throws IOException {
        Writer writerMock = mock(Writer.class);
        CharSequence input = "&#x;";
        int index = 0;

        int result = numericEntityUnescaper.translate(input, index, writerMock);

        assertEquals(0, result);
    }

    // Edge case: Input with "&#" and following "X" but no digit
    @Test
    public void testTranslate_inputWithHexPrefixUppercaseButNoDigit() throws IOException {
        Writer writerMock = mock(Writer.class);
        CharSequence input = "&#X;";
        int index = 0;

        int result = numericEntityUnescaper.translate(input, index, writerMock);

        assertEquals(0, result);
    }

    // Edge case: Input with "&#x" and following digit but no ";"
    @Test
    public void testTranslate_inputWithHexPrefixAndDigitButNoSemicolon() throws IOException {
        Writer writerMock = mock(Writer.class);
        CharSequence input = "&#x41";
        int index = 0;

        int result = numericEntityUnescaper.translate(input, index, writerMock);

        assertEquals(0, result);
    }

    // Edge case: Input with "&#x" and following digit and ";"
    @Test
    public void testTranslate_inputWithHexPrefixAndDigitAndSemicolon() throws IOException {
        Writer writerMock = mock(Writer.class);
        CharSequence input = "&#x41;";
        int index = 0;

        int result = numericEntityUnescaper.translate(input, index, writerMock);

        assertEquals(7, result);
        verify(writerMock).write(65);
    }

    // Edge case: Input with "&#X" and following digit and ";"
    @Test
    public void testTranslate_inputWithHexPrefixUppercaseAndDigitAndSemicolon() throws IOException {
        Writer writerMock = mock(Writer.class);
        CharSequence input = "&#X41;";
        int index = 0;

        int result = numericEntityUnescaper.translate(input, index, writerMock);

        assertEquals(7, result);
        verify(writerMock).write(65);
    }

    // Edge case: Input with "&#1234567890" and no ";" but within integer range
    @Test
    public void testTranslate_inputInIntegerRangeButNoSemicolon() throws IOException {
        Writer writerMock = mock(Writer.class);
        CharSequence input = "&#1234567890";
        int index = 0;

        int result = numericEntityUnescaper.translate(input, index, writerMock);

        assertEquals(10, result);
        verify(writerMock).write(1234567890);
    }

    // Edge case: Input with "&#1234567890" and no ";" but beyond integer range
    @Test
    public void testTranslate_inputBeyondIntegerRangeButNoSemicolon() throws IOException {
        Writer writerMock = mock(Writer.class);
        CharSequence input = "&#12345678901234567890";
        int index = 0;

        int result = numericEntityUnescaper.translate(input, index, writerMock);

        assertEquals(0, result);
    }

    // Edge case: Input with "&#123;456" and ";" in unexpected position
    @Test
    public void testTranslate_inputWithUnexpectedSemicolonPosition() throws IOException {
        Writer writerMock = mock(Writer.class);
        CharSequence input = "&#123;456";
        int index = 0;

        int result = numericEntityUnescaper.translate(input, index, writerMock);

        assertEquals(9, result);
        verify(writerMock).write(123);
    }
  
    // Edge case: Input with "ABC" and no numeric entity
    @Test
    public void testTranslate_inputWithoutNumericEntity() throws IOException {
        Writer writerMock = mock(Writer.class);
        CharSequence input = "ABC";
        int index = 0;

        int result = numericEntityUnescaper.translate(input, index, writerMock);

        assertEquals(0, result);
    }

    // Exception handling: Input with invalid hex value
    @Test
    public void testTranslate_inputWithInvalidHexValue() throws IOException {
        Writer writerMock = mock(Writer.class);
        CharSequence input = "&#xZZ;";
        int index = 0;

        int result = numericEntityUnescaper.translate(input, index, writerMock);

        assertEquals(0, result);
    }

    // Exception handling: Input with invalid decimal value
    @Test
    public void testTranslate_inputWithInvalidDecimalValue() throws IOException {
        Writer writerMock = mock(Writer.class);
        CharSequence input = "&#ZZ;";
        int index = 0;

        int result = numericEntityUnescaper.translate(input, index, writerMock);

        assertEquals(0, result);
    }
}
