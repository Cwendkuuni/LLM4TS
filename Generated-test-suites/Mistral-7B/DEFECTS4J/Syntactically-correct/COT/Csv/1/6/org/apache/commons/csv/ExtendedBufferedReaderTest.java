import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExtendedBufferedReaderTest {

    @Test
    public void testConstructor() {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);
        Assert.assertNotNull(extendedBufferedReader);
    }

    @Test
    public void testRead() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test\ntest".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        int actual = extendedBufferedReader.read();
        Assert.assertEquals('t', actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('e', actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('\n', actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('t', actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('e', actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, actual);
    }

    @Test
    public void testReadAgain() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test\ntest".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        int actual = extendedBufferedReader.read();
        int lastChar = extendedBufferedReader.readAgain();
        Assert.assertEquals('t', actual);
        Assert.assertEquals('t', lastChar);

        actual = extendedBufferedReader.read();
        lastChar = extendedBufferedReader.readAgain();
        Assert.assertEquals('e', actual);
        Assert.assertEquals('e', lastChar);

        actual = extendedBufferedReader.read();
        lastChar = extendedBufferedReader.readAgain();
        Assert.assertEquals('\n', actual);
        Assert.assertEquals('\n', lastChar);

        actual = extendedBufferedReader.read();
        lastChar = extendedBufferedReader.readAgain();
        Assert.assertEquals('t', actual);
        Assert.assertEquals('t', lastChar);

        actual = extendedBufferedReader.read();
        lastChar = extendedBufferedReader.readAgain();
        Assert.assertEquals('e', actual);
        Assert.assertEquals('e', lastChar);

        actual = extendedBufferedReader.read();
        lastChar = extendedBufferedReader.readAgain();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, actual);
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, lastChar);
    }

    @Test
    public void testReadWithArray() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test\ntest".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        char[] buffer = new char[2];
        int actual = extendedBufferedReader.read(buffer, 0, 2);
        Assert.assertEquals(2, actual);
        Assert.assertEquals('t', buffer[0]);
        Assert.assertEquals('e', buffer[1]);

        int lastChar = extendedBufferedReader.readAgain();
        Assert.assertEquals('t', lastChar);

        actual = extendedBufferedReader.read(buffer, 1, 1);
        Assert.assertEquals(1, actual);
        Assert.assertEquals('\n', buffer[1]);

        lastChar = extendedBufferedReader.readAgain();
        Assert.assertEquals('\n', lastChar);

        actual = extendedBufferedReader.read(buffer, 0, 1);
        Assert.assertEquals(1, actual);
        Assert.assertEquals('t', buffer[0]);

        lastChar = extendedBufferedReader.readAgain();
        Assert.assertEquals('t', lastChar);

        actual = extendedBufferedReader.read(buffer, 1, 1);
        Assert.assertEquals(1, actual);
        Assert.assertEquals('e', buffer[1]);

        lastChar = extendedBufferedReader.readAgain();
        Assert.assertEquals('e', lastChar);

        actual = extendedBufferedReader.read();
        lastChar = extendedBufferedReader.readAgain();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, actual);
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, lastChar);
    }

    @Test(expected = IOException.class)
    public void testReadLineWithEmptyBuffer() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream(new byte[0]));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);
        String line = extendedBufferedReader.readLine();
        Assert.assertEquals(null, line);
    }

    @Test
    public void testReadLine() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test\ntest".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        String actual = extendedBufferedReader.readLine();
        Assert.assertEquals("test", actual);

        actual = extendedBufferedReader.readLine();
        Assert.assertEquals("test", actual);

        actual = extendedBufferedReader.readLine();
        Assert.assertEquals(null, actual);
    }

    @Test
    public void testLookAhead() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test\ntest".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        int actual = extendedBufferedReader.lookAhead();
        Assert.assertEquals('t', actual);

        actual = extendedBufferedReader.lookAhead();
        Assert.assertEquals('e', actual);

        actual = extendedBufferedReader.lookAhead();
        Assert.assertEquals('\n', actual);

        actual = extendedBufferedReader.lookAhead();
        Assert.assertEquals('t', actual);

        actual = extendedBufferedReader.lookAhead();
        Assert.assertEquals('e', actual);

        actual = extendedBufferedReader.lookAhead();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, actual);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test\ntest\ntest".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        int actual = extendedBufferedReader.getLineNumber();
        Assert.assertEquals(0, actual);

        String line = extendedBufferedReader.readLine();
        actual = extendedBufferedReader.getLineNumber();
        Assert.assertEquals(1, actual);

        line = extendedBufferedReader.readLine();
        actual = extendedBufferedReader.getLineNumber();
        Assert.assertEquals(2, actual);

        line = extendedBufferedReader.readLine();
        actual = extendedBufferedReader.getLineNumber();
        Assert.assertEquals(-1, actual);
    }

    @Test(expected = IOException.class)
    public void testReadBeyondLastChar() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        int expectedLastChar = 't';
        int actual = extendedBufferedReader.read();
        Assert.assertEquals(expectedLastChar, actual);
        Assert.assertEquals(expectedLastChar, extendedBufferedReader.readAgain());

        int nextChar = extendedBufferedReader.lookAhead();
        Assert.assertEquals('e', nextChar);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('e', actual);

        nextChar = extendedBufferedReader.lookAhead();
        Assert.assertEquals('\n', nextChar);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('\n', actual);

        nextChar = extendedBufferedReader.lookAhead();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, nextChar);

        actual = extendedBufferedReader.read();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, actual);
    }

    @Test(expected = IOException.class)
    public void testReadBeyondLastLine() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test\ntest".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        int expectedLastChar = '\n';
        int actual = extendedBufferedReader.read();
        Assert.assertEquals(expectedLastChar, actual);
        Assert.assertEquals(expectedLastChar, extendedBufferedReader.readAgain());

        int nextChar = extendedBufferedReader.lookAhead();
        Assert.assertEquals('t', nextChar);

        String line = extendedBufferedReader.readLine();
        Assert.assertEquals("test", line);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('t', actual);

        nextChar = extendedBufferedReader.lookAhead();
        Assert.assertEquals('e', nextChar);

        line = extendedBufferedReader.readLine();
        Assert.assertEquals("test", line);

        nextChar = extendedBufferedReader.lookAhead();
        Assert.assertEquals('\n', nextChar);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('\n', actual);

        nextChar = extendedBufferedReader.lookAhead();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, nextChar);

        actual = extendedBufferedReader.read();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, actual);
    }
}