import org.apache.commons.csv.Lexer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class LexerTest1 {
    private Lexer lexer;
    private StringBuilder input = new StringBuilder();
    private String testCsv = "field1,field2\nfield3,\"field4,field5\"\n";

    @Before
    public void setUp() throws IOException {
        String csv = "test.csv";
        CSVFormat format = CSVFormat.DEFAULT;
        ExtendedBufferedReader in = new ExtendedBufferedReader(new FileReader(csv));
        lexer = new Lexer(format, in);
        input.append(testCsv);
        lexer.in.input(input.toString().getBytes());
    }

    @Test
    public void testNextToken() throws IOException {
        Token token = new Token();
        Token nextToken = lexer.nextToken(token);
        assertEquals("field1", nextToken.getValue());
        assertEquals(TokenType.STRING, nextToken.getType());
        nextToken = lexer.nextToken(token);
        assertEquals("field2", nextToken.getValue());
        assertEquals(TokenType.STRING, nextToken.getType());
        nextToken = lexer.nextToken(token);
        assertEquals("field3", nextToken.getValue());
        assertEquals(TokenType.STRING, nextToken.getType());
        nextToken = lexer.nextToken(token);
        assertEquals("field4,field5", nextToken.getValue());
        assertEquals(TokenType.STRING, nextToken.getType());
    }

    @Test
    public void testIsStartOfLine() throws IOException {
        int c = lexer.read();
        assertTrue(lexer.isStartOfLine(c));
    }

    @Test
    public void testIsEndOfFile() throws IOException {
        int c = lexer.read();
        assertFalse(lexer.isEndOfFile(c));
        c = lexer.read();
        // assuming next character is not EOF
        assertFalse(lexer.isEndOfFile(c));
        lexer.in.close();
        c = lexer.read();
        assertTrue(lexer.isEndOfFile(c));
    }

    @Test(expected = IOException.class)
    public void testReadEscapeWithInvalidChar() throws IOException {
        lexer.readEscape();
    }
}