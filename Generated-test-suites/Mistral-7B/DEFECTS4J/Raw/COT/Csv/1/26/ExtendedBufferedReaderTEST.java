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
    public void testConstructor() {
        Reader reader = new InputStreamReader(new ByteArrayInputStream(new byte[0]));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);
        Assert.assertNotNull(extendedBufferedReader);
    }

    @Test
    public void testRead() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("abcd\r\n".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);
        int actual = extendedBufferedReader.read();
        Assert.assertEquals('a', actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('b', actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('c', actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('\r', actual); // line terminator

        actual = extendedBufferedReader.read();
        Assert.assertEquals('\n', actual); // line terminator

        actual = extendedBufferedReader.read();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, actual);
    }

    @Test
    public void testReadAgain() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("abcd\r\n".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        int actual = extendedBufferedReader.read();
        int expected = 'a';
        Assert.assertEquals(expected, actual);

        actual = extendedBufferedReader.readAgain();
        Assert.assertEquals(expected, actual);

        actual = extendedBufferedReader.read();
        expected = 'b';
        Assert.assertEquals(expected, actual);

        actual = extendedBufferedReader.readAgain();
        Assert.assertEquals(expected, actual);

        actual = extendedBufferedReader.read();
        expected = 'c';
        Assert.assertEquals(expected, actual);

        actual = extendedBufferedReader.readAgain();
        Assert.assertEquals(expected, actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('\r', actual); // line terminator

        actual = extendedBufferedReader.read();
        Assert.assertEquals('\n', actual); // line terminator

        actual = extendedBufferedReader.readAgain();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, actual);
    }

    @Test
    public void testReadLine() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("abcd\r\n".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);
        String actual = extendedBufferedReader.readLine();
        String expected = "abcd";
        Assert.assertEquals(expected, actual);

        int lineNumber = extendedBufferedReader.getLineNumber();
        Assert.assertEquals(1, lineNumber);

        actual = extendedBufferedReader.readLine();
        Assert.assertNull(actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testLookAheadWithNoData() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream(new byte[0]));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);
        extendedBufferedReader.lookAhead();
    }

    @Test
    public void testLookAhead() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("abcd\r\n".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);
        int actual = extendedBufferedReader.lookAhead();
        Assert.assertEquals('a', actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('a', actual);

        actual = extendedBufferedReader.lookAhead();
        Assert.assertEquals('b', actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('b', actual);

        actual = extendedBufferedReader.lookAhead();
        Assert.assertEquals('c', actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals('c', actual);

        actual = extendedBufferedReader.lookAhead();
        Assert.assertEquals('\r', actual); // line terminator

        actual = extendedBufferedReader.read();
        Assert.assertEquals('\r', actual);

        actual = extendedBufferedReader.lookAhead();
        Assert.assertEquals('\n', actual); // line terminator

        actual = extendedBufferedReader.read();
        Assert.assertEquals('\n', actual);

        actual = extendedBufferedReader.lookAhead();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, actual);
    }

    @Test
    public void testGetLineNumber() {
        Reader reader = new InputStreamReader(new ByteArrayInputStream(new byte[0]));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);
        Assert.assertEquals(-1, extendedBufferedReader.getLineNumber());

        reader = new InputStreamReader(new ByteArrayInputStream("abcd\r\n".getBytes()));
        extendedBufferedReader = new ExtendedBufferedReader(reader);
        Assert.assertEquals(1, extendedBufferedReader.getLineNumber());
    }
}