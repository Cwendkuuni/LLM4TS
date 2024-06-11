import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws Exception {
        // Test case 1: Read the first character of the reader and check if it is correct.
        Reader reader1 = new StringReader("Hello World");
        ExtendedBufferedReader ebr1 = new ExtendedBufferedReader(reader1);
        int char1 = ebr1.read();
        assertEquals('H', char1);

        // Test case 2: Read multiple characters from the reader and check if they are correct.
        Reader reader2 = new StringReader("12345");
        ExtendedBufferedReader ebr2 = new ExtendedBufferedReader(reader2);
        int char2_1 = ebr2.read();
        int char2_2 = ebr2.read();
        int char2_3 = ebr2.read();
        assertEquals('1', char2_1);
        assertEquals('2', char2_2);
        assertEquals('3', char2_3);

        // Test case 3: Read the last character of the reader and check if it is correct.
        Reader reader3 = new StringReader("abcdefg");
        ExtendedBufferedReader ebr3 = new ExtendedBufferedReader(reader3);
        ebr3.skip(6);
        int char3 = ebr3.read();
        assertEquals('g', char3);
    }

    @Test
    public void testReadAgain() throws Exception {
        // Test case 1: Read a character from the reader and check if it is the same as readAgain().
        Reader reader1 = new StringReader("abc");
        ExtendedBufferedReader ebr1 = new ExtendedBufferedReader(reader1);
        ebr1.read();
        int char1 = ebr1.readAgain();
        assertEquals('a', char1);

        // Test case 2: Read multiple characters from the reader and check if the last one is the same as readAgain().
        Reader reader2 = new StringReader("12345");
        ExtendedBufferedReader ebr2 = new ExtendedBufferedReader(reader2);
        ebr2.skip(3);
        int char2_1 = ebr2.read();
        int char2_2 = ebr2.readAgain();
        assertEquals('4', char2_1);
        assertEquals('4', char2_2);

        // Test case 3: Read the last character of the reader and check if it is the same as readAgain().
        Reader reader3 = new StringReader("abcd");
        ExtendedBufferedReader ebr3 = new ExtendedBufferedReader(reader3);
        ebr3.skip(3);
        int char3_1 = ebr3.read();
        int char3_2 = ebr3.readAgain();
        assertEquals('d', char3_1);
        assertEquals('d', char3_2);
    }

    @Test
    public void testReadLine() throws Exception {
        // Test case 1: Read a line from the reader and check if it is correct.
        Reader reader1 = new StringReader("Hello World\n");
        ExtendedBufferedReader ebr1 = new ExtendedBufferedReader(reader1);
        String line1 = ebr1.readLine();
        assertEquals("Hello World", line1);

        // Test case 2: Read multiple lines from the reader and check if they are correct.
        Reader reader2 = new StringReader("Line 1\nLine 2\nLine 3\n");
        ExtendedBufferedReader ebr2 = new ExtendedBufferedReader(reader2);
        String line2_1 = ebr2.readLine();
        String line2_2 = ebr2.readLine();
        String line2_3 = ebr2.readLine();
        assertEquals("Line 1", line2_1);
        assertEquals("Line 2", line2_2);
        assertEquals("Line 3", line2_3);

        // Test case 3: Read an empty line from the reader and check if it is correct.
        Reader reader3 = new StringReader("\n");
        ExtendedBufferedReader ebr3 = new ExtendedBufferedReader(reader3);
        String line3 = ebr3.readLine();
        assertEquals("", line3);
    }
    
    @Test
    public void testLookAhead() throws Exception {
        // Test case 1: Look ahead at the next character in the reader and check if it is correct.
        Reader reader1 = new StringReader("Hello World");
        ExtendedBufferedReader ebr1 = new ExtendedBufferedReader(reader1);
        int lookAhead1 = ebr1.lookAhead();
        assertEquals('e', lookAhead1);

        // Test case 2: Look ahead at multiple characters in the reader and check if they are correct.
        Reader reader2 = new StringReader("12345");
        ExtendedBufferedReader ebr2 = new ExtendedBufferedReader(reader2);
        ebr2.skip(2);
        int lookAhead2_1 = ebr2.lookAhead();
        ebr2.skip(1);
        int lookAhead2_2 = ebr2.lookAhead();
        assertEquals('3', lookAhead2_1);
        assertEquals('4', lookAhead2_2);

        // Test case 3: Look ahead at the end of the reader and check if it is correct.
        Reader reader3 = new StringReader("abcd");
        ExtendedBufferedReader ebr3 = new ExtendedBufferedReader(reader3);
        ebr3.skip(4);
        int lookAhead3 = ebr3.lookAhead();
        assertEquals(-1, lookAhead3);
    }

    @Test
    public void testGetLineNumber() throws Exception {
        // Test case 1: Get the line number before and after reading a line from the reader.
        Reader reader1 = new StringReader("Hello World\n");
        ExtendedBufferedReader ebr1 = new ExtendedBufferedReader(reader1);
        int lineNumber1_1 = ebr1.getLineNumber();
        ebr1.readLine();
        int lineNumber1_2 = ebr1.getLineNumber();
        assertEquals(0, lineNumber1_1);
        assertEquals(1, lineNumber1_2);

        // Test case 2: Get the line number after reading multiple lines from the reader.
        Reader reader2 = new StringReader("Line 1\nLine 2\nLine 3\n");
        ExtendedBufferedReader ebr2 = new ExtendedBufferedReader(reader2);
        ebr2.readLine();
        ebr2.readLine();
        int lineNumber2 = ebr2.getLineNumber();
        assertEquals(2, lineNumber2);

        // Test case 3: Get the line number after reading to the end of the reader.
        Reader reader3 = new StringReader("\n");
        ExtendedBufferedReader ebr3 = new ExtendedBufferedReader(reader3);
        ebr3.readLine();
        int lineNumber3 = ebr3.getLineNumber();
        assertEquals(1, lineNumber3);
    }
}