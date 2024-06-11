import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.io.IOException;

public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;

    @Before
    public void setUp() {
        reader = new ExtendedBufferedReader(new StringReader("test string"));
    }

    @Test
    public void testRead() throws IOException {
        int result = reader.read();
        Assert.assertEquals('t', result);
    }

    @Test
    public void testReadAgain() throws IOException {
        reader.read();
        int result = reader.readAgain();
        Assert.assertEquals('t', result);
    }

    @Test
    public void testReadCharArrayWithOffsetAndLength() throws IOException {
        char[] buf = new char[5];
        int result = reader.read(buf, 0, 5);
        Assert.assertEquals(5, result);
    }

    @Test
    public void testReadLine() throws IOException {
        String result = reader.readLine();
        Assert.assertEquals("test string", result);
    }

    @Test
    public void testLookAhead() throws IOException {
        int result = reader.lookAhead();
        Assert.assertEquals('t', result);
    }

    @Test
    public void testGetLineNumber() {
        int result = reader.getLineNumber();
        Assert.assertEquals(0, result);
    }

    @Test
    public void testReadWhenEndOfStream() throws IOException {
        reader.read();
        reader.read();
        int result = reader.read();
        Assert.assertEquals(-1, result);
    }

    @Test
    public void testReadCharArrayWithOffsetAndLengthWhenLengthZero() throws IOException {
        char[] buf = new char[5];
        int result = reader.read(buf, 0, 0);
        Assert.assertEquals(0, result);
    }

    @Test
    public void testReadLineWhenNull() throws IOException {
        String result = reader.readLine();
        Assert.assertNull(result);
    }

}