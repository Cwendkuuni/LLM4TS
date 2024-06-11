import org.junit.Assert;
import org.junit.Test;
import java.io.*;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        Reader reader = new StringReader("Hello World");
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        int result = extReader.read();

        Assert.assertEquals('H', result);
        Assert.assertEquals(1, extReader.getLineNumber());
    }

    @Test
    public void testReadAgain() throws IOException {
        Reader reader = new StringReader("Hello World");
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        int result = extReader.read();
        int readAgainResult = extReader.readAgain();

        Assert.assertEquals('H', result);
        Assert.assertEquals('H', readAgainResult);
    }

    @Test
    public void testReadCharArray() throws IOException {
        Reader reader = new StringReader("Hello World");
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        char[] buffer = new char[5];
        int result = extReader.read(buffer, 0, 5);

        Assert.assertEquals(5, result);
        Assert.assertEquals(1, extReader.getLineNumber());
    }

    @Test
    public void testReadLine() throws IOException {
        Reader reader = new StringReader("Line 1\nLine 2\nLine 3");
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        String line1 = extReader.readLine();
        String line2 = extReader.readLine();

        Assert.assertEquals("Line 1", line1);
        Assert.assertEquals("Line 2", line2);
        Assert.assertEquals(2, extReader.getLineNumber());
    }

    @Test
    public void testLookAhead() throws IOException {
        Reader reader = new StringReader("Hello World");
        ExtendedBufferedReader extReader = new ExtendedBufferedReader(reader);

        int lookAheadResult = extReader.lookAhead();

        Assert.assertEquals('H', lookAheadResult);
        Assert.assertEquals(1, extReader.getLineNumber());
    }
}