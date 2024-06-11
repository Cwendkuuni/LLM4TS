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
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test\n".getBytes()));
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
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, actual);

        int lastChar = extendedBufferedReader.readAgain();
        Assert.assertEquals(-1, lastChar); // UNDEFINED should be -2, but Java doesn't have a constant for it
    }

    @Test
    public void testReadLine() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test\nline\n".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        String actual = extendedBufferedReader.readLine();
        Assert.assertEquals("test", actual);
        Assert.assertEquals(1, extendedBufferedReader.getLineNumber());

        actual = extendedBufferedReader.readLine();
        Assert.assertEquals("line", actual);
        Assert.assertEquals(2, extendedbufferedReader.getLineNumber());

        actual = extendedBufferedReader.readLine();
        Assert.assertEquals(null, actual);
        Assert.assertEquals(2, extendedBufferedReader.getLineNumber());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, extendedBufferedReader.readAgain());
    }

    @Test
    public void testReadArray() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test\nline\n".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        char[] buffer = new char[3];
        int read = extendedBufferedReader.read(buffer, 0, buffer.length);
        Assert.assertEquals(2, read);

        Assert.assertEquals('t', buffer[0]);
        Assert.assertEquals('e', buffer[1]);
        Assert.assertEquals(' ', buffer[2]);

        read = extendedBufferedReader.read(buffer, 0, buffer.length);
        Assert.assertEquals(3, read);

        Assert.assertEquals('t', buffer[0]);
        Assert.assertEquals('e', buffer[1]);
        Assert.assertEquals('s', buffer[2]);

        read = extendedBufferedReader.read(buffer, 0, buffer.length);
        Assert.assertEquals(1, read);

        Assert.assertEquals('\n', buffer[0]);
        Assert.assertEquals(3, extendedBufferedReader.getLineNumber());

        read = extendedBufferedReader.read(buffer, 0, buffer.length);
        Assert.assertEquals(3, read);

        Assert.assertEquals('l', buffer[0]);
        Assert.assertEquals('i', buffer[1]);
        Assert.assertEquals('n', buffer[2]);

        Assert.assertEquals(4, extendedbufferedReader.getLineNumber());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, extendedBufferedReader.readAgain());
    }

    @Test(expected = IOException.class)
    public void testLookAheadWithIOException() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        int lookAhead = extendedBufferedReader.lookAhead();
        Assert.fail("Expected IOException");
    }

    @Test
    public void testLookAheadWithNormalCharacter() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        int lookAhead = extendedBufferedReader.lookAhead();
        Assert.assertEquals('t', lookAhead);
    }

    @Test
    public void testLookAheadWithEndOfStream() throws IOException {
        Reader reader = new InputStreamReader(new ByteArrayInputStream("test".getBytes()));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        int lookAhead = extendedBufferedReader.lookAhead();
        extendedBufferedReader.read();
        lookAhead = extendedBufferedReader.lookAhead();

        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, lookAhead);
    }
}