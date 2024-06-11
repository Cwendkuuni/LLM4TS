import org.junit.Test;
import org.junit.Assert;
import java.io.StringReader;
import java.io.IOException;

public class ExtendedBufferedReaderTest {

    // Test read method
    @Test
    public void testRead() throws IOException {
        Reader reader = new StringReader("Hello World");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        int result = br.read();
        Assert.assertEquals('H', result);
    }

    // Test readAgain method
    @Test
    public void testReadAgain() throws IOException {
        Reader reader = new StringReader("Hello World");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        br.read();
        int result = br.readAgain();
        Assert.assertEquals('H', result);
    }

    // Test read method with char array
    @Test
    public void testReadWithCharArray() throws IOException {
        Reader reader = new StringReader("Hello World");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        char[] buf = new char[5];
        int result = br.read(buf, 0, 5);
        Assert.assertEquals(5, result);
        Assert.assertArrayEquals(new char[]{'H', 'e', 'l', 'l', 'o'}, buf);
    }

    // Test readLine method
    @Test
    public void testReadLine() throws IOException {
        Reader reader = new StringReader("Hello\nWorld");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        String line = br.readLine();
        Assert.assertEquals("Hello", line);
    }

    // Test lookAhead method
    @Test
    public void testLookAhead() throws IOException {
        Reader reader = new StringReader("Hello World");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        int result = br.lookAhead();
        Assert.assertEquals('H', result);
    }

    // Test getLineNumber method
    @Test
    public void testGetLineNumber() throws IOException {
        Reader reader = new StringReader("Hello\nWorld\nTest");
        ExtendedBufferedReader br = new ExtendedBufferedReader(reader);

        br.readLine();
        int result = br.getLineNumber();
        Assert.assertEquals(1, result);
    }

}