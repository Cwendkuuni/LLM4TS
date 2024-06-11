import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class ExtendedBufferedReaderTest {

    @Test
    public void testConstruct() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        Assert.assertNotNull(reader);
    }

    @Test
    public void testRead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc"));
        int actual = reader.read();
        Assert.assertEquals('a', actual);

        actual = reader.read();
        Assert.assertEquals('b', actual);

        actual = reader.read();
        Assert.assertEquals('c', actual);

        actual = reader.read();
        Assert.assertEquals(-1, actual);
    }

    @Test
    public void testReadAgain() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("ab"));
        int actual = reader.read();
        Assert.assertEquals('a', actual);

        actual = reader.read();
        Assert.assertEquals('b', actual);

        actual = reader.readAgain();
        Assert.assertEquals('a', actual);
    }

    @Test
    public void testReadLine() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Line1\nLine2"));

        String actual = reader.readLine();
        Assert.assertEquals("Line1", actual);

        actual = reader.readLine();
        Assert.assertEquals("Line2", actual);

        actual = reader.readLine();
        Assert.assertEquals(null, actual);
    }

    @Test
    public void testReadWithOffsetAndLength() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abcd"));

        char[] buffer = new char[3];
        int length = reader.read(buffer, 0, buffer.length);
        Assert.assertEquals(3, length);

        Assert.assertEquals('a', buffer[0]);
        Assert.assertEquals('b', buffer[1]);
        Assert.assertEquals('c', buffer[2]);
    }

    @Test
    public void testLookAhead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("ab"));

        int actual = reader.lookAhead();
        Assert.assertEquals(-1, actual);

        reader.read(); // read 'a'

        actual = reader.lookAhead();
        Assert.assertEquals('b', actual);
    }

    @Test(expected = IOException.class)
    public void testReadPastEOF() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        reader.read();
        reader.read();
        reader.read();
    }

    @Test(expected = IOException.class)
    public void testReadNegativeOffset() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("ab"));
        reader.read(); // read 'a'
        reader.read(new char[2], -1, 2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadNegativeLength() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("ab"));
        reader.read(); // read 'a'
        reader.read(new char[1], 0, -1);
    }
}