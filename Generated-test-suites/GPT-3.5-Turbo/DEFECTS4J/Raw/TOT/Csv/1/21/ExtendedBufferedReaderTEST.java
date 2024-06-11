package org.apache.commons.csv;

import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedBufferedReaderTest {
    
    @Test
    public void testRead() {
        // Test reading a single character
        Reader reader = new StringReader("abc");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);
        try {
            int character = bufferedReader.read(); // Read 'a'
            assertEquals('a', character);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
    
    @Test
    public void testReadAgain() {
        // Test reading a single character and checking readAgain()
        Reader reader = new StringReader("abc");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);
        try {
            int character = bufferedReader.read(); // Read 'a'
            assertEquals('a', bufferedReader.readAgain());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
    
    @Test
    public void testRead_eof() {
        // Test reading at end of file
        Reader reader = new StringReader("abc");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);
        try {
            bufferedReader.read(); // Read 'a'
            bufferedReader.read(); // Read 'b'
            bufferedReader.read(); // Read 'c'
            assertEquals(-1, bufferedReader.read());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
    
    @Test
    public void testRead_charArray() {
        // Test reading characters into char array
        Reader reader = new StringReader("abc");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);
        char[] charArray = new char[2];
        try {
            int charactersRead = bufferedReader.read(charArray, 0, 2); // Read 'a' and 'b'
            assertEquals(2, charactersRead);
            assertEquals('a', charArray[0]);
            assertEquals('b', charArray[1]);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
    
    @Test
    public void testReadLine() {
        // Test reading a line
        Reader reader = new StringReader("Hello World");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);
        try {
            String line = bufferedReader.readLine();
            assertEquals("Hello World", line);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
    
    @Test
    public void testLookAhead() {
        // Test lookAhead() method
        Reader reader = new StringReader("abc");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);
        try {
            int character = bufferedReader.lookAhead(); // Look ahead to 'a'
            assertEquals('a', character);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
    
    @Test
    public void testGetLineNumber() {
        // Test getLineNumber() method
        Reader reader = new StringReader("abc\n123\nxyz\n");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);
        try {
            bufferedReader.readLine(); // Read 'abc'
            bufferedReader.readLine(); // Read '123'
            int lineNumber = bufferedReader.getLineNumber();
            assertEquals(2, lineNumber);
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }
}