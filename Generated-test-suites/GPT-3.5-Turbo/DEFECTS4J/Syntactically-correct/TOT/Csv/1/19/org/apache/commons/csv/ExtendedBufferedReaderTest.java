import org.junit.jupiter.api.Test;
import org.apache.commons.csv.ExtendedBufferedReader;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.jupiter.api.Assertions.*;

class ExtendedBufferedReaderTest {

    @Test
    void testRead() {
        StringReader reader = new StringReader("This is a test string.");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);

        try {
            int ch;
            StringBuilder result = new StringBuilder();
            while ((ch = bufferedReader.read()) != -1) {
                result.append((char) ch);
            }

            assertEquals("This is a test string.", result.toString());

            bufferedReader.close();
        } catch (IOException e) {
            fail("IOException occurred.");
        }
    }

    @Test
    void testReadAgain() {
        StringReader reader = new StringReader("This is a test string.");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);

        try {
            while (bufferedReader.read() != -1) {
                // Reading till the end of stream
            }

            assertEquals(-1, bufferedReader.readAgain());

            bufferedReader.close();
        } catch (IOException e) {
            fail("IOException occurred.");
        }
    }

    @Test
    void testReadWithBuffer() {
        StringReader reader = new StringReader("Line 1\nLine 2\rLine 3\r\nLine 4");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);
        char[] buffer = new char[10];

        try {
            bufferedReader.read(buffer, 0, 10);
            assertEquals('L', buffer[0]);
            assertEquals('i', buffer[1]);
            assertEquals('n', buffer[2]);
            assertEquals('e', buffer[3]);
            assertEquals(' ', buffer[4]);
            assertEquals('1', buffer[5]);
            assertEquals('\n', buffer[6]);
            assertEquals('L', buffer[7]);
            assertEquals('i', buffer[8]);
            assertEquals('n', buffer[9]);

            bufferedReader.close();
        } catch (IOException e) {
            fail("IOException occurred.");
        }
    }

    @Test
    void testReadLine() {
        StringReader reader = new StringReader("Line 1\nLine 2\rLine 3\r\nLine 4");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);

        try {
            String line1 = bufferedReader.readLine();
            String line2 = bufferedReader.readLine();
            String line3 = bufferedReader.readLine();
            String line4 = bufferedReader.readLine();

            assertEquals("Line 1", line1);
            assertEquals("Line 2", line2);
            assertEquals("Line 3", line3);
            assertEquals("Line 4", line4);

            bufferedReader.close();
        } catch (IOException e) {
            fail("IOException occurred.");
        }
    }

    @Test
    void testLookAhead() {
        StringReader reader = new StringReader("This is a test string.");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);

        try {
            int ch = bufferedReader.lookAhead();
            assertEquals('T', ch);

            bufferedReader.close();
        } catch (IOException e) {
            fail("IOException occurred.");
        }
    }

    @Test
    void testGetLineNumber() {
        StringReader reader = new StringReader("Line 1\nLine 2\rLine 3\r\nLine 4");
        ExtendedBufferedReader bufferedReader = new ExtendedBufferedReader(reader);

        try {
            bufferedReader.readLine();
            bufferedReader.readLine();
            bufferedReader.readLine();
            bufferedReader.readLine();

            assertEquals(4, bufferedReader.getLineNumber());

            bufferedReader.close();
        } catch (IOException e) {
            fail("IOException occurred.");
        }
    }
}