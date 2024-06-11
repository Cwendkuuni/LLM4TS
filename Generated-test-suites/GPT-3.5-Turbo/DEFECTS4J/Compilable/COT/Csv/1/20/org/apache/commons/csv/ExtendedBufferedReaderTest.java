package org.apache.commons.csv;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.StringReader;
import java.io.IOException;

public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;

    @Before
    public void setUp() {
        StringReader stringReader = new StringReader("Hello, World!\nThis is a test.\n");
        reader = new ExtendedBufferedReader(stringReader);
    }

    @Test
    public void testRead() {
        try {
            int char1 = reader.read();
            assertEquals('H', char1);
            
            int char2 = reader.read();
            assertEquals('e', char2);
            
            int char3 = reader.read();
            assertEquals('l', char3);
            
            int char4 = reader.read();
            assertEquals('l', char4);
            
            int char5 = reader.read();
            assertEquals('o', char5);
        } catch (IOException e) {
            fail("IOException was thrown");
        }
    }

    @Test
    public void testReadAgain() {
        try {
            reader.read();  // 'H'
            reader.read();  // 'e'
            int char1 = reader.readAgain();
            assertEquals('e', char1);
            
            reader.read();  // 'l'
            int char2 = reader.readAgain();
            assertEquals('l', char2);
        } catch (IOException e) {
            fail("IOException was thrown");
        }
    }

    @Test
    public void testReadLine() {
        try {
            String line1 = reader.readLine();
            assertEquals("Hello, World!", line1);
            
            String line2 = reader.readLine();
            assertEquals("This is a test.", line2);
            
            String line3 = reader.readLine();
            assertNull(line3);
        } catch (IOException e) {
            fail("IOException was thrown");
        }
    }

    @Test
    public void testLookAhead() {
        try {
            int char1 = reader.lookAhead();
            assertEquals('H', char1);
            
            int char2 = reader.read();
            assertEquals('H', char2);
            
            int char3 = reader.read();
            assertEquals('e', char3);
        } catch (IOException e) {
            fail("IOException was thrown");
        }
    }

    @Test
    public void testGetLineNumber() {
        try {
            reader.readLine();  // "Hello, World!"
            int line1Number = reader.getLineNumber();
            assertEquals(1, line1Number);
            
            reader.readLine();  // "This is a test."
            int line2Number = reader.getLineNumber();
            assertEquals(2, line2Number);
            
            reader.readLine();  // null
            int line3Number = reader.getLineNumber();
            assertEquals(2, line3Number); // Line number should not increase after reaching the end of the stream.
        } catch (IOException e) {
            fail("IOException was thrown");
        }
    }

}