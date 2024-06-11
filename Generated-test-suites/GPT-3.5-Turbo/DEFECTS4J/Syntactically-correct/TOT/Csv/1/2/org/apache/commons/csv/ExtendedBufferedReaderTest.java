import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.io.StringReader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ExtendedBufferedReaderTest {
    
    private ExtendedBufferedReader reader;

    @Before
    public void setUp() {
        // Create a new ExtendedBufferedReader with a StringReader
        StringReader stringReader = new StringReader("test\n123\n456\r\n789");
        reader = new ExtendedBufferedReader(stringReader);
    }

    @Test
    public void testRead() throws IOException {
        // Test reading characters
        assertEquals('t', (char) reader.read());
        assertEquals('e', (char) reader.read());
        assertEquals('s', (char) reader.read());
        assertEquals('t', (char) reader.read());
    }

    @Test
    public void testReadAgain() throws IOException {
        // Test reading last character returned
        reader.read();
        reader.read();
        reader.read();
        reader.read();
        
        assertEquals('t', (char) reader.readAgain());
    }

    @Test
    public void testReadWithBuffer() throws IOException {
        // Test reading characters with buffer
        char[] buffer = new char[3];
        int bytesRead = reader.read(buffer, 0, 3);
        
        assertEquals(3, bytesRead);
        assertEquals('t', buffer[0]);
        assertEquals('e', buffer[1]);
        assertEquals('s', buffer[2]);
    }

    @Test
    public void testReadWithBufferAndLineCounter() throws IOException {
        // Test reading characters with buffer and line counter
        char[] buffer = new char[5];
        int bytesRead = reader.read(buffer, 0, 5);
        
        assertEquals(5, bytesRead);
        assertEquals('t', buffer[0]);
        assertEquals('e', buffer[1]);
        assertEquals('s', buffer[2]);
        assertEquals('t', buffer[3]);
        assertEquals('\n', buffer[4]);
        assertEquals(1, reader.getLineNumber());
    }

    @Test
    public void testReadWithBufferAndCRLineEnding() throws IOException {
        // Test reading characters with buffer and CR line ending
        char[] buffer = new char[5];
        int bytesRead = reader.read(buffer, 0, 5);
        
        assertEquals(5, bytesRead);
        assertEquals('1', buffer[0]);
        assertEquals('2', buffer[1]);
        assertEquals('3', buffer[2]);
        assertEquals('\n', buffer[3]);
        assertEquals('4', buffer[4]);
        assertEquals(2, reader.getLineNumber());
    }

    @Test
    public void testReadLine() throws IOException {
        // Test reading lines
        assertEquals("test", reader.readLine());
        assertEquals("123", reader.readLine());
        assertEquals("456", reader.readLine());
        assertNotEquals("789", reader.readLine());
        assertNull(reader.readLine());
    }

    @Test
    public void testLookAhead() throws IOException {
        // Test looking ahead
        assertEquals('t', (char) reader.lookAhead());
        reader.read();
        assertEquals('e', (char) reader.lookAhead());
    }

    @After
    public void tearDown() throws IOException {
        reader.close();
    }
}