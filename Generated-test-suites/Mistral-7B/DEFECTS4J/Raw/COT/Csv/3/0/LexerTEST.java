import org.apache.commons.csv.Constants;
import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Test;
import org.junit.Assert;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Test class for {@link Lexer}
 */
public class LexerTest {

    @Test
    public void testGetLineNumber() throws IOException {
        CSVFormat format = new CSVFormat(',', '\u001E', '\u001C', '\u0022');
        ExtendedBufferedReader in = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("line1\nline2\n".getBytes())));
        Lexer lexer = new Lexer(format, in);

        Assert.assertEquals(1, lexer.getLineNumber());

        lexer.nextToken(null); // consume first token

        Assert.assertEquals(2, lexer.getLineNumber());
    }

    @Test
    public void testReadEscape_CR() throws IOException {
        CSVFormat format = new CSVFormat(',', '\u001E', '\u001C', '\u0022');
        ExtendedBufferedReader in = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("\\r".getBytes())));
        Lexer lexer = new Lexer(format, in);

        Assert.assertEquals(CR, lexer.readEscape());
    }

    @Test
    public void testReadEscape_LF() throws IOException {
        CSVFormat format = new CSVFormat(',', '\u001E', '\u001C', '\u0022');
        ExtendedBufferedReader in = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("\\n".getBytes())));
        Lexer lexer = new Lexer(format, in);

        Assert.assertEquals(LF, lexer.readEscape());
    }

    @Test
    public void testReadEscape_TAB() throws IOException {
        CSVFormat format = new CSVFormat(',', '\u001E', '\u001C', '\u0022');
        ExtendedBufferedReader in = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("\\t".getBytes())));
        Lexer lexer = new Lexer(format, in);

        Assert.assertEquals(TAB, lexer.readEscape());
    }

    @Test(expected = IOException.class)
    public void testReadEscape_EOF() throws IOException {
        CSVFormat format = new CSVFormat(',', '\u001E', '\u001C', '\u0022');
        ExtendedBufferedReader in = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("\\r".getBytes())));
        Lexer lexer = new Lexer(format, in);

        // consume the CR character
        lexer.readEscape();

        // read EOF
        lexer.readEscape();
    }

    @Test
    public void testReadEscape_Other() throws IOException {
        CSVFormat format = new CSVFormat(',', '\u001E', '\u001C', '\u0022');
        ExtendedBufferedReader in = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("x".getBytes())));
        Lexer lexer = new Lexer(format, in);

        Assert.assertEquals('x', lexer.readEscape());
    }

    @Test
    public void testTrimTrailingSpaces() throws IOException {
        CSVFormat format = new CSVFormat(',', '\u001E', '\u001C', '\u0022');
        ExtendedBufferedReader in = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("line1   ".getBytes())));
        Lexer lexer = new Lexer(format, in);

        StringBuilder buffer = new StringBuilder();
        lexer.nextToken(buffer);

        trimTrailingSpaces(buffer);

        Assert.assertEquals("line1", buffer.toString());
    }

    @Test
    public void testIsStartOfLine() throws IOException {
        CSVFormat format = new CSVFormat(',', '\u001E', '\u001C', '\u0022');
        ExtendedBufferedReader in = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("line1\nline2\n".getBytes())));
        Lexer lexer = new Lexer(format, in);

        Assert.assertTrue(lexer.isStartOfLine(Constants.CR));
        Assert.assertTrue(lexer.isStartOfLine(Constants.LF));
        Assert.assertTrue(lexer.isStartOfLine(Constants.UNDEFINED));

        lexer.nextToken(null); // consume first token

        Assert.assertFalse(lexer.isStartOfLine(Constants.CR));
        Assert.assertFalse(lexer.isStartOfLine(Constants.LF));
        Assert.assertFalse(lexer.isStartOfLine(Constants.UNDEFINED));
    }

    @Test
    public void testIsEndOfFile() throws IOException {
        CSVFormat format = new CSVFormat(',', '\u001E', '\u001C', '\u0022');
        ExtendedBufferedReader in = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("line1\nline2\n".getBytes())));
        Lexer lexer = new Lexer(format, in);

        Assert.assertFalse(lexer.isEndOfFile(Constants.CR));
        Assert.assertFalse(lexer.isEndOfFile(Constants.LF));
        Assert.assertFalse(lexer.isEndOfFile(Constants.UNDEFINED));

        lexer.nextToken(null); // consume first token

        // read EOF
        Assert.assertTrue(lexer.isEndOfFile(Constants.UNDEFINED));
    }

    @Test
    public void testIsDelimiter() throws IOException {
        CSVFormat format = new CSVFormat(',', '\u001E', '\u001C', '\u0022');
        ExtendedBufferedReader in = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("line1,line2\nline3,line4\n".getBytes())));
        Lexer lexer = new Lexer(format, in);

        Assert.assertFalse(lexer.isDelimiter(Constants.CR));
        Assert.assertFalse(lexer.isDelimiter(Constants.LF));
        Assert.assertFalse(lexer.isDelimiter(Constants.UNDEFINED));

        Assert.assertTrue(lexer.isDelimiter(','));
    }

    @Test
    public void testIsEscape() throws IOException {
        CSVFormat format = new CSVFormat(',', '\u001E', '\u001C', '\u0022');
        ExtendedBufferedReader in = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("line1\\e line2\nline3\\e line4\n".getBytes())));
        Lexer lexer = new Lexer(format, in);

        Assert.assertFalse(lexer.isEscape(Constants.CR));
        Assert.assertFalse(lexer.isEscape(Constants.LF));
        Assert.assertFalse(lexer.isEscape(Constants.UNDEFINED));

        Assert.assertTrue(lexer.isEscape('\u001E')); // escape character
        Assert.assertFalse(lexer.isEscape(',')); // delimiter
        Assert.assertFalse(lexer.isEscape('\u0022')); // quote char
    }

    @Test
    public void testIsQuoteChar() throws IOException {
        CSVFormat format = new CSVFormat(',', '\u001E', '\u001C', '\u0022');
        ExtendedBufferedReader in = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("line1\"line2\nline3\"line4\n".getBytes())));
        Lexer lexer = new Lexer(format, in);

        Assert.assertFalse(lexer.isQuoteChar(Constants.CR));
        Assert.assertFalse(lexer.isQuoteChar(Constants.LF));
        Assert.assertFalse(lexer.isQuoteChar(Constants.UNDEFINED));

        Assert.assertFalse(lexer.isQuoteChar(',')); // delimiter
        Assert.assertTrue(lexer.isQuoteChar('"')); // quote char
    }

    @Test
    public void testIsCommentStart() throws IOException {
        CSVFormat format = new CSVFormat(',', '\u001E', '\u001C', '\u0022');
        ExtendedBufferedReader in = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("line1% line2\nline3% line4\n".getBytes())));
        Lexer lexer = new Lexer(format, in);

        Assert.assertFalse(lexer.isCommentStart(Constants.CR));
        Assert.assertFalse(lexer.isCommentStart(Constants.LF));
        Assert.assertFalse(lexer.isCommentStart(Constants.UNDEFINED));

        Assert.assertFalse(lexer.isCommentStart(',')); // delimiter
        Assert.assertFalse(lexer.isCommentStart('\u0022')); // quote char
        Assert.assertTrue(lexer.isCommentStart('\u0025')); // comment start
    }
}