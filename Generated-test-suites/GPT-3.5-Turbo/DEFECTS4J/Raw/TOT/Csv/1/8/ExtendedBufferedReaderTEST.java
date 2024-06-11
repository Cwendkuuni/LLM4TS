
import org.junit.Before;
import org.junit.Test;

import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.*;

public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;

    @Before
    public void setUp() throws IOException {
        reader = new ExtendedBufferedReader(new FileReader("testFile.txt"));
    }

    @Test
    public void testRead() throws IOException {
        // Typical use case: reading a character
        int character = reader.read();
        assertEquals('T', character);

        // Edge case: reading the last character
        while (reader.read() != ExtendedBufferedReader.END_OF_STREAM) {
            // Keep reading characters until the end
        }
        character = reader.read();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, character);

        // Error scenario: reading from an empty reader
        // Create an empty reader
        ExtendedBufferedReader emptyReader = new ExtendedBufferedReader(new FileReader("empty.txt"));
        character = emptyReader.read();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, character);
    }

    @Test
    public void testReadAgain() throws IOException {
        // Typical use case: reading a character
        int character = reader.read();
        int lastCharacter = reader.readAgain();
        assertEquals('T', lastCharacter);

        // Edge case: reading the last character
        while (reader.read() != ExtendedBufferedReader.END_OF_STREAM) {
            // Keep reading characters until the end
        }
        lastCharacter = reader.readAgain();
        assertEquals(reader.read(), lastCharacter);

        // Error scenario: reading from an empty reader
        // Create an empty reader
        ExtendedBufferedReader emptyReader = new ExtendedBufferedReader(new FileReader("empty.txt"));
        lastCharacter = emptyReader.readAgain();
        assertEquals(ExtendedBufferedReader.UNDEFINED, lastCharacter);
    }

    @Test
    public void testReadCharArray() throws IOException {
        // Typical use case: reading characters into a char array
        char[] buffer = new char[10];
        reader.read(buffer, 0, 10);
        assertEquals("Hello, Wor", new String(buffer));

        // Edge case: reading into an empty char array
        buffer = new char[0];
        int length = reader.read(buffer, 0, 0);
        assertEquals(0, length);

        // Error scenario: reading into null char array
        buffer = null;
        try {
            reader.read(buffer, 0, 10);
            fail("Should throw NullPointerException");
        } catch (NullPointerException e) {
            // Exception should be thrown
        }
    }

    @Test
    public void testReadLine() throws IOException {
        // Typical use case: reading a line with line terminator
        String line = reader.readLine();
        assertEquals("Hello, World!", line);

        // Edge case: reading an empty line
        line = reader.readLine();
        assertEquals("", line);

        // Error scenario: reading from an empty reader
        // Create an empty reader
        ExtendedBufferedReader emptyReader = new ExtendedBufferedReader(new FileReader("empty.txt"));
        line = emptyReader.readLine();
        assertNull(line);
    }

    @Test
    public void testLookAhead() throws IOException {
        // Typical use case: looking ahead at the next character
        int character = reader.lookAhead();
        assertEquals('e', character);

        // Edge case: looking ahead at end of stream
        while (reader.read() != ExtendedBufferedReader.END_OF_STREAM) {
            // Keep reading characters until the end
        }
        character = reader.lookAhead();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, character);

        // Error scenario: looking ahead from an empty reader
        // Create an empty reader
        ExtendedBufferedReader emptyReader = new ExtendedBufferedReader(new FileReader("empty.txt"));
        character = emptyReader.lookAhead();
        assertEquals(ExtendedBufferedReader.END_OF_STREAM, character);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        // Typical use case: get line number after reading a line
        reader.readLine();
        int lineNumber = reader.getLineNumber();
        assertEquals(1, lineNumber);

        // Edge case: get line number at end of stream
        while (reader.read() != ExtendedBufferedReader.END_OF_STREAM) {
            // Keep reading characters until the end
        }
        lineNumber = reader.getLineNumber();
        assertTrue(lineNumber > 1);

        // Error scenario: get line number from an empty reader
        // Create an empty reader
        ExtendedBufferedReader emptyReader = new ExtendedBufferedReader(new FileReader("empty.txt"));
        lineNumber = emptyReader.getLineNumber();
        assertEquals(0, lineNumber);
    }

}
