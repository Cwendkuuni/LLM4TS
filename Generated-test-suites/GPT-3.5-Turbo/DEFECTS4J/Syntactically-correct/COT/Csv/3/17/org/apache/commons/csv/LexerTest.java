import org.junit.Test;
import static org.junit.Assert.*;

public class LexerTest {

    @Test
    public void testLexerConstructor() {
        CSVFormat format = new CSVFormat();
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        assertNotNull(lexer);
        // Add more assertions if necessary
    }

    @Test
    public void testGetLineNumber() {
        CSVFormat format = new CSVFormat();
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        long lineNumber = lexer.getLineNumber();
        assertEquals(0, lineNumber);
        // Add more assertions if necessary
    }

    @Test
    public void testLexerConstructorWithCustomFormat() {
        CSVFormat format = CSVFormat.newBuilder()
            .setDelimiter(';')
            .setEscape('\\')
            .setQuoteChar('"')
            .build();
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(format, in);
        assertNotNull(lexer);
        // Add more assertions if necessary
    }

    @Test
    public void testLexerConstructorWithFileInputStream() throws FileNotFoundException {
        CSVFormat format = new CSVFormat();
        File file = new File("test.csv");
        FileInputStream fileInputStream = new FileInputStream(file);
        ExtendedBufferedReader in = new ExtendedBufferedReader(new InputStreamReader(fileInputStream));
        Lexer lexer = new Lexer(format, in);
        assertNotNull(lexer);
        // Add more assertions if necessary
    }

    @Test
    public void testLexerConstructorWithNullFormatAndInputStream() {
        ExtendedBufferedReader in = new ExtendedBufferedReader();
        Lexer lexer = new Lexer(null, in);
        assertNotNull(lexer);
        // Add more assertions if necessary
    }

    @Test
    public void testGetLineNumberAfterReadingMultipleLines() throws IOException {
        CSVFormat format = new CSVFormat();
        StringReader reader = new StringReader("Line 1\nLine 2\nLine 3");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        Lexer lexer = new Lexer(format, in);
        in.readLine();
        long lineNumber = lexer.getLineNumber();
        assertEquals(2, lineNumber);
        // Add more assertions if necessary
    }

    @Test
    public void testGetLineNumberAfterResettingInputStream() throws IOException {
        CSVFormat format = new CSVFormat();
        StringReader reader = new StringReader("Line 1\nLine 2");
        ExtendedBufferedReader in = new ExtendedBufferedReader(reader);
        Lexer lexer = new Lexer(format, in);
        in.readLine(); // Read Line 1
        in.reset();
        long lineNumber = lexer.getLineNumber();
        assertEquals(1, lineNumber);
        // Add more assertions if necessary
    }

}