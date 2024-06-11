import org.junit.Test;
import org.junit.Assert;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() {
        // Typical use case - Read a character
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello"));
        try {
            int result = reader.read();
            Assert.assertEquals('H', result);
        } catch (IOException e) {
            Assert.fail("An IOException occurred");
        }

        // Edge case - Empty reader
        reader = new ExtendedBufferedReader(new StringReader(""));
        try {
            int result = reader.read();
            Assert.assertEquals(ExtendedBufferedReader.UNDEFINED, result);
        } catch (IOException e) {
            Assert.fail("An IOException occurred");
        }

        // Error scenario - Reader with special characters
        reader = new ExtendedBufferedReader(new StringReader("!@#$%^&*()"));
        try {
            int result = reader.read();
            Assert.assertEquals('!', result);
        } catch (IOException e) {
            Assert.fail("An IOException occurred");
        }
    }

    @Test
    public void testReadAgain() {
        // Typical use case - Read a character again
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello"));
        try {
            int result = reader.read();
            int resultAgain = reader.readAgain();
            Assert.assertEquals('H', resultAgain);
        } catch (IOException e) {
            Assert.fail("An IOException occurred");
        }

        // Edge case - No characters read yet
        reader = new ExtendedBufferedReader(new StringReader(""));
        try {
            int result = reader.readAgain();
            Assert.assertEquals(ExtendedBufferedReader.UNDEFINED, result);
        } catch (IOException e) {
            Assert.fail("An IOException occurred");
        }
    }

    @Test
    public void testReadCharArray() {
        // Typical use case - Read a char array
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello"));
        char[] buffer = new char[5];
        try {
            int result = reader.read(buffer, 0, 5);
            Assert.assertEquals(5, result);
            Assert.assertArrayEquals(new char[]{'H', 'e', 'l', 'l', 'o'}, buffer);
        } catch (IOException e) {
            Assert.fail("An IOException occurred");
        }

        // Edge case - Empty reader
        reader = new ExtendedBufferedReader(new StringReader(""));
        buffer = new char[5];
        try {
            int result = reader.read(buffer, 0, 5);
            Assert.assertEquals(-1, result);
        } catch (IOException e) {
            Assert.fail("An IOException occurred");
        }

        // Error scenario - Read only a part of the char array
        reader = new ExtendedBufferedReader(new StringReader("Hello"));
        buffer = new char[5];
        try {
            int result = reader.read(buffer, 0, 3);
            Assert.assertEquals(3, result);
            Assert.assertArrayEquals(new char[]{'H', 'e', 'l', '\u0000', '\u0000'}, buffer);
        } catch (IOException e) {
            Assert.fail("An IOException occurred");
        }
    }

    @Test
    public void testReadLine() {
        // Typical use case - Read a line
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld"));
        try {
            String line = reader.readLine();
            Assert.assertEquals("Hello", line);
        } catch (IOException e) {
            Assert.fail("An IOException occurred");
        }

        // Edge case - Empty reader
        reader = new ExtendedBufferedReader(new StringReader(""));
        try {
            String line = reader.readLine();
            Assert.assertNull(line);
        } catch (IOException e) {
            Assert.fail("An IOException occurred");
        }

        // Error scenario - Reader with multiple lines
        reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld"));
        try {
            String line = reader.readLine();
            Assert.assertEquals("Hello", line);
            line = reader.readLine();
            Assert.assertEquals("World", line);
        } catch (IOException e) {
            Assert.fail("An IOException occurred");
        }
    }

    @Test
    public void testLookAhead() {
        // Typical use case - Look ahead
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello"));
        try {
            int result = reader.lookAhead();
            Assert.assertEquals('H', result);
        } catch (IOException e) {
            Assert.fail("An IOException occurred");
        }

        // Edge case - Empty reader
        reader = new ExtendedBufferedReader(new StringReader(""));
        try {
            int result = reader.lookAhead();
            Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, result);
        } catch (IOException e) {
            Assert.fail("An IOException occurred");
        }

        // Error scenario - Reader with special characters
        reader = new ExtendedBufferedReader(new StringReader("!@#$%^&*()"));
        try {
            int result = reader.lookAhead();
            Assert.assertEquals('!', result);
        } catch (IOException e) {
            Assert.fail("An IOException occurred");
        }
    }

    @Test
    public void testGetLineNumber() {
        // Typical use case - Get line number
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello\n\nWorld"));
        try {
            int lineNumber = reader.getLineNumber();
            Assert.assertEquals(0, lineNumber);

            reader.readLine();
            lineNumber = reader.getLineNumber();
            Assert.assertEquals(1, lineNumber);

            reader.readLine();
            lineNumber = reader.getLineNumber();
            Assert.assertEquals(2, lineNumber);

            reader.readLine();
            lineNumber = reader.getLineNumber();
            Assert.assertEquals(2, lineNumber);
        } catch (IOException e) {
            Assert.fail("An IOException occurred");
        }

        // Edge case - Empty reader
        reader = new ExtendedBufferedReader(new StringReader(""));
        try {
            int lineNumber = reader.getLineNumber();
            Assert.assertEquals(0, lineNumber);
        } catch (IOException e) {
            Assert.fail("An IOException occurred");
        }
    }
}