import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * ExtendedBufferedReaderTest
 */
public class ExtendedBufferedReaderTest {

    @Test
    public void testExtendedBufferedReaderConstructor() {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);
        Assert.assertNotNull(extendedBufferedReader);
    }

    @Test
    public void testReadMethod() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test\nhello\nworld".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        int actual = extendedBufferedReader.read();
        Assert.assertEquals('t', actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('e', actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('s', actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('\n', actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('h', actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('e', actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('l', actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('l', actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('o', actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals(' ', actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('w', actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('o', actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('r', actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('d', actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('\n', actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, actual);

    }

    @Test
    public void testReadAgainMethod() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test\nhello\nworld".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        int actual = extendedBufferedReader.read();
        Assert.assertEquals('t', actual);

        int lastChar = extendedBufferedReader.readAgain();
        Assert.assertEquals('t', lastChar);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('e', actual);

        lastChar = extendedBufferedReader.readAgain();
        Assert.assertEquals('e', lastChar);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('s', actual);

        lastChar = extendedBufferedReader.readAgain();
        Assert.assertEquals('s', lastChar);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('\n', actual);

        lastChar = extendedBufferedReader.readAgain();
        Assert.assertEquals('\n', lastChar);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('h', actual);

        lastChar = extendedBufferedReader.readAgain();
        Assert.assertEquals('h', lastChar);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('e', actual);

        lastChar = extendedBufferedReader.readAgain();
        Assert.assertEquals('e', lastChar);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('l', actual);

        lastChar = extendedBufferedReader.readAgain();
        Assert.assertEquals('l', lastChar);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('l', actual);

        lastChar = extendedBufferedReader.readAgain();
        Assert.assertEquals('l', lastChar);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('o', actual);

        lastChar = extendedBufferedReader.readAgain();
        Assert.assertEquals('o', lastChar);

        actual = extendedBufferedReader.read();
        Assert.assertEquals(' ', actual);

        lastChar = extendedBufferedReader.readAgain();
        Assert.assertEquals(' ', lastChar);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('w', actual);

        lastChar = extendedBufferedReader.readAgain();
        Assert.assertEquals('w', lastChar);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('o', actual);

        lastChar = extendedBufferedReader.readAgain();
        Assert.assertEquals('o', lastChar);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('r', actual);

        lastChar = extendedBufferedReader.readAgain();
        Assert.assertEquals('r', lastChar);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('d', actual);

        lastChar = extendedBufferedReader.readAgain();
        Assert.assertEquals('d', lastChar);

        actual = extendedBufferedReader.read();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, actual);

        lastChar = extendedBufferedReader.readAgain();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, lastChar);
    }

    @Test
    public void testReadMethodWithLength() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test\r\nhello\nworld".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        char[] buffer = new char[6];
        int length = extendedBufferedReader.read(buffer, 0, 6);
        Assert.assertEquals(5, length);

        Assert.assertEquals('t', buffer[0]);
        Assert.assertEquals('e', buffer[1]);
        Assert.assertEquals('s', buffer[2]);
        Assert.assertEquals('t', buffer[3]);
        Assert.assertEquals(' ', buffer[4]);

        length = extendedBufferedReader.read(buffer, 4, 3);
        Assert.assertEquals(3, length);

        Assert.assertEquals('\r', buffer[4]);
        Assert.assertEquals('h', buffer[5]);
        Assert.assertEquals('e', buffer[6]);

        length = extendedBufferedReader.read(buffer, 6, 1);
        Assert.assertEquals(1, length);

        Assert.assertEquals('\n', buffer[7]);

        length = extendedBufferedReader.read(buffer, 7, 2);
        Assert.assertEquals(2, length);

        Assert.assertEquals('h', buffer[7]);
        Assert.assertEquals('e', buffer[8]);

        length = extendedBufferedReader.read(buffer, 8, 3);
        Assert.assertEquals(3, length);

        Assert.assertEquals('l', buffer[8]);
        Assert.assertEquals('l', buffer[9]);
        Assert.assertEquals('o', buffer[10]);

        length = extendedBufferedReader.read(buffer, 10, 1);
        Assert.assertEquals(1, length);

        Assert.assertEquals('\n', buffer[11]);

        length = extendedBufferedReader.read(buffer, 11, 1);
        Assert.assertEquals(1, length);

        Assert.assertEquals('w', buffer[12]);

        length = extendedBufferedReader.read(buffer, 12, 2);
        Assert.assertEquals(2, length);

        Assert.assertEquals('o', buffer[12]);
        Assert.assertEquals('r', buffer[13]);

        length = extendedBufferedReader.read(buffer, 13, 2);
        Assert.assertEquals(2, length);

        Assert.assertEquals('d', buffer[13]);
        Assert.assertEquals('\n', buffer[15]);

        length = extendedBufferedReader.read(buffer, 15, 1);
        Assert.assertEquals(1, length);

        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, buffer[16]);
    }

    @Test(expected = IOException.class)
    public void testReadMethodWithNegativeLength() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test\r\nhello\nworld".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        char[] buffer = new char[6];
        int length = extendedBufferedReader.read(buffer, 2, -3);
        Assert.fail(); // Test should throw an IOException
    }

    @Test
    public void testLookAheadMethod() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test\r\nhello\nworld".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        int actual = extendedBufferedReader.lookAhead();
        Assert.assertEquals('t', actual);

        actual = extendedBufferedReader.lookAhead();
        Assert.assertEquals('e', actual);

        actual = extendedBufferedReader.lookAhead();
        Assert.assertEquals('s', actual);

        actual = extendedBufferedReader.lookAhead();
        Assert.assertEquals('\n', actual);

        actual = extendedBufferedReader.lookAhead();
        Assert.assertEquals('h', actual);

        actual = extendedBufferedReader.lookAhead();
        Assert.assertEquals('e', actual);

        actual = extendedBufferedReader.lookAhead();
        Assert.assertEquals('l', actual);

        actual = extendedBufferedReader.lookAhead();
        Assert.assertEquals('l', actual);

        actual = extendedBufferedReader.lookAhead();
        Assert.assertEquals('o', actual);

        actual = extendedBufferedReader.lookAhead();
        Assert.assertEquals(' ', actual);

        actual = extendedBufferedReader.lookAhead();
        Assert.assertEquals('w', actual);

        actual = extendedBufferedReader.lookAhead();
        Assert.assertEquals('o', actual);

        actual = extendedBufferedReader.lookAhead();
        Assert.assertEquals('r', actual);

        actual = extendedBufferedReader.lookAhead();
        Assert.assertEquals('d', actual);

        actual = extendedBufferedReader.lookAhead();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, actual);

    }

    @Test
    public void testGetLineNumberMethod() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test\r\nhello\nworld".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        int actual = extendedBufferedReader.getLineNumber();
        Assert.assertEquals(0, actual);

        String line = extendedBufferedReader.readLine();
        Assert.assertEquals("test", line);
        actual = extendedBufferedReader.getLineNumber();
        Assert.assertEquals(1, actual);

        line = extendedBufferedReader.readLine();
        Assert.assertEquals("hello", line);
        actual = extendedBufferedReader.getLineNumber();
        Assert.assertEquals(2, actual);

        line = extendedBufferedReader.readLine();
        Assert.assertEquals("world", line);
        actual = extendedBufferedReader.getLineNumber();
        Assert.assertEquals(-1, actual);
    }

}