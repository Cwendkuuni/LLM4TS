import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;

    @Before
    public void setUp() {
        reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld\r\nTest"));
    }

    @Test
    public void testRead() throws IOException {
        assertEquals('H', (char) reader.read());
        assertEquals('e', (char) reader.read());
        assertEquals('l', (char) reader.read());
        assertEquals('l', (char) reader.read());
        assertEquals('o', (char) reader.read());
        assertEquals('\n', (char) reader.read());
    }

    @Test
    public void testReadAgain() throws IOException {
        reader.read();
        assertEquals('H', (char) reader.readAgain());
        reader.read();
        assertEquals('e', (char) reader.readAgain());
    }

    @Test
    public void testReadCharBuffer() throws IOException {
        char[] buf = new char[5];
        reader.read(buf, 0, 5);
        assertEquals("Hello", new String(buf).trim());

        reader.read(buf, 0, 3);
        assertNotEquals("Hello", new String(buf).trim());
    }

    @Test
    public void testReadLine() throws IOException {
        assertEquals("Hello", reader.readLine());
        assertEquals("World", reader.readLine());
        assertEquals("Test", reader.readLine());
        assertNull(reader.readLine());
    }

    @Test
    public void testLookAhead() throws IOException {
        assertEquals((int) 'e', reader.lookAhead());
        reader.read();
        assertEquals((int) 'l', reader.lookAhead());
    }

    @Test
    public void testGetLineNumber() throws IOException {
        assertEquals(0, reader.getLineNumber());
        reader.readLine();
        assertEquals(1, reader.getLineNumber());
    }
}