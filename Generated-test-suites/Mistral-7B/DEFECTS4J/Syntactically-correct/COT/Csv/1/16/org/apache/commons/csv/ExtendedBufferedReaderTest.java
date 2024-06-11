import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExtendedBufferedReaderTest {

    @Test
    public void testConstructor() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);
        Assert.assertNotNull(extendedBufferedReader);
    }

    @Test
    public void testRead() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test\n".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        int actual = extendedBufferedReader.read();
        int expected = 't';
        Assert.assertEquals(expected, actual);

        actual = extendedBufferedReader.read();
        expected = 'e';
        Assert.assertEquals(expected, actual);

        actual = extendedBufferedReader.read();
        expected = 'st';
        Assert.assertEquals(expected, String.valueOf(actual));

        actual = extendedBufferedReader.read();
        expected = '\n';
        Assert.assertEquals(expected, actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, actual);
    }

    @Test
    public void testReadAgain() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test\n".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        int actual = extendedBufferedReader.read();
        int expected = 't';
        Assert.assertEquals(expected, actual);

        actual = extendedBufferedReader.readAgain();
        Assert.assertEquals(expected, actual);

        actual = extendedBufferedReader.read();
        expected = 'e';
        Assert.assertEquals(expected, actual);

        actual = extendedBufferedReader.readAgain();
        Assert.assertEquals(expected, actual);

        actual = extendedBufferedReader.read();
        expected = 's';
        Assert.assertEquals(expected, actual);

        actual = extendedBufferedReader.readAgain();
        Assert.assertEquals(expected, actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, actual);

        actual = extendedBufferedReader.readAgain();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, actual);
    }

    @Test
    public void testReadArray() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test\n".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        char[] buffer = new char[3];
        int actual = extendedBufferedReader.read(buffer, 0, 3);
        String expected = "tes";
        Assert.assertEquals(expected.length(), actual);
        Assert.assertArrayEquals(expected.toCharArray(), buffer, 0, actual);

        actual = extendedBufferedReader.read();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, actual);
    }

    @Test
    public void testReadLine() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test\n".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        String actual = extendedBufferedReader.readLine();
        String expected = "test";
        Assert.assertEquals(expected, actual);

        actual = extendedBufferedReader.readLine();
        Assert.assertNull(actual);
    }

    @Test(expected = IOException.class)
    public void testLookAheadThrowsIOException() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test\n".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        int actual = extendedBufferedReader.lookAhead();
    }

    @Test
    public void testGetLineNumber() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test\n".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        int actual = extendedBufferedReader.getLineNumber();
        int expected = 1;
        Assert.assertEquals(expected, actual);
    }
}