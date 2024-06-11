import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class LexerTest {
    private static final String TEST_FILE = "test.csv";
    private CSVFormat format;
    private ExtendedBufferedReader in;
    private Lexer lexer;

    @Before
    public void setUp() throws Exception {
        format = new CSVFormatBuilder().setDelimiter(';').build();
        in = new BufferedReader(new FileReader(TEST_FILE));
        lexer = new Lexer(format, in);
    }

    @Test
    public void testNextToken_ValidInput() throws IOException {
        Token token = lexer.nextToken(null);
        assertEquals(TokenType.START_LINE, token.getType());
        assertEquals(UNDEFINED, token.getValue());
        token = lexer.nextToken(null);
        assertEquals(TokenType.FIELD, token.getType());
        assertEquals("Field1", token.getValue());
        token = lexer.nextToken(null);
        assertEquals(TokenType.FIELD, token.getType());
        assertEquals("Field2", token.getValue());
        token = lexer.nextToken(null);
        assertEquals(TokenType.END_OF_LINE, token.getType());
        token = lexer.nextToken(null);
        assertEquals(TokenType.START_LINE, token.getType());
        // ... add more test cases for all methods and edge cases
    }

    // Add more test cases for all methods: isWhitespace, isStartOfLine, isEndOfFile, isDelimiter, isEscape, isQuoteChar, isCommentStart, trimTrailingSpaces, readEndOfLine, nextToken
}