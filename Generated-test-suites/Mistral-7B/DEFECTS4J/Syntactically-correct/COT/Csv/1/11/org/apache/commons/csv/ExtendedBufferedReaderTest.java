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
        Reader reader = new InputStreamReader(new ByteArrayInputStream("abcd\n".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        Assert.assertEquals('a', extendedBufferedReader.read());
        Assert.assertEquals('b', extendedBufferedReader.read());
        Assert.assertEquals('c', extendedBufferedReader.read());
        Assert.assertEquals('\n', extendedBufferedReader.read());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, extendedBufferedReader.read());

        // Test readAgain
        Assert.assertEquals('c', extendedBufferedReader.readAgain());
    }

    @Test
    public void testReadArray() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("abcd\n".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        char[] buffer = new char[4];
        Assert.assertEquals(3, extendedBufferedReader.read(buffer, 0, buffer.length));
        Assert.assertArrayEquals(new char[]{'a', 'b', 'c'}, buffer);
    }

    @Test
    public void testReadLine() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("abcd\n".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        Assert.assertEquals("abcd", extendedBufferedReader.readLine());
        Assert.assertEquals(4, extendedBufferedReader.getLineNumber());
    }

    @Test(expected = IOException.class)
    public void testLookAheadThrowsIOException() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        extendedBufferedReader.lookAhead();
    }

    @Test
    public void testLookAhead() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("abcd\n".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        Assert.assertEquals('a', extendedBufferedReader.lookAhead());
    }

    @Test
    public void testGetLineNumber() {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("abcd\n".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        Assert.assertEquals(0, extendedBufferedReader.getLineNumber());

        String line = extendedBufferedReader.readLine();
        Assert.assertEquals("abcd", line);
        Assert.assertEquals(1, extendedBufferedReader.getLineNumber());

        line = extendedBufferedReader.readLine();
        Assert.assertNull(line);
        Assert.assertEquals(1, extendedBufferedReader.getLineNumber());
    }
}