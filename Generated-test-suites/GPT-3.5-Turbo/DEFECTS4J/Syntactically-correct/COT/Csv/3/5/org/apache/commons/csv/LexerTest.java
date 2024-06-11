import org.junit.Assert;
    import org.junit.Before;
    import org.junit.Test;
    
    import java.io.IOException;
    import org.apache.commons.csv.CSVFormat;
    import org.apache.commons.csv.Lexer;
    
    public class LexerTest {
        
        private Lexer lexer;
        
        @Before
        public void setUp() {
            CSVFormat format = CSVFormat.DEFAULT;
            ExtendedBufferedReader in = new ExtendedBufferedReader();
            lexer = new Lexer(format, in);
        }
        
        @Test
        public void testGetLineNumber() {
            long lineNumber = lexer.getLineNumber();
            Assert.assertEquals(0, lineNumber);
        }
        
        @Test
        public void testTrimTrailingSpaces() {
            StringBuilder buffer = new StringBuilder("Hello     ");
            lexer.trimTrailingSpaces(buffer);
            Assert.assertEquals("Hello", buffer.toString());
        }
        
        @Test
        public void testReadEndOfLine() throws IOException {
            int c = 13; // CR
            boolean endOfLine = lexer.readEndOfLine(c);
            Assert.assertTrue(endOfLine);
        }
        
        @Test
        public void testIsWhitespace() {
            int c = 9; // TAB
            boolean isWhitespace = lexer.isWhitespace(c);
            Assert.assertTrue(isWhitespace);
        }
        
        @Test
        public void testIsStartOfLine() {
            int c = 13; // CR
            boolean isStartOfLine = lexer.isStartOfLine(c);
            Assert.assertTrue(isStartOfLine);
        }
        
        @Test
        public void testIsEndOfFile() {
            int c = -1; // END_OF_STREAM
            boolean isEndOfFile = lexer.isEndOfFile(c);
            Assert.assertTrue(isEndOfFile);
        }
        
        @Test
        public void testIsDelimiter() {
            int c = 44; // Comma
            boolean isDelimiter = lexer.isDelimiter(c);
            Assert.assertTrue(isDelimiter);
        }
        
        @Test
        public void testIsEscape() {
            int c = 92; // Backslash
            boolean isEscape = lexer.isEscape(c);
            Assert.assertTrue(isEscape);
        }
        
        @Test
        public void testIsQuoteChar() {
            int c = 34; // Double quote
            boolean isQuoteChar = lexer.isQuoteChar(c);
            Assert.assertTrue(isQuoteChar);
        }
        
        @Test
        public void testIsCommentStart() {
            int c = 35; // Hash
            boolean isCommentStart = lexer.isCommentStart(c);
            Assert.assertTrue(isCommentStart);
        }
    }