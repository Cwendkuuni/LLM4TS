import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class ExtendedBufferedReaderTest {

    @Test
    public void testConstructor() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        Assert.assertNotNull(reader);
    }

    @Test
    public void testRead() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc"));
        int result = reader.read();
        Assert.assertEquals('a', result);
        Assert.assertEquals(0, reader.getLineNumber());

        result = reader.read();
        Assert.assertEquals('b', result);
        Assert.assertEquals(0, reader.getLineNumber());

        result = reader.read();
        Assert.assertEquals('c', result);
        Assert.assertEquals(1, reader.getLineNumber());

        result = reader.read();
        Assert.assertEquals(-1, result);
    }

    @Test
    public void testReadAgain() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc"));
        reader.read();
        int result = reader.readAgain();
        Assert.assertEquals('a', result);

        reader.read();
        reader.read();
        result = reader.readAgain();
        Assert.assertEquals('b', result);

        reader.read();
        result = reader.readAgain();
        Assert.assertEquals('c', result);

        reader.read();
        Assert.assertEquals(-1, reader.readAgain());
    }

    @Test
    public void testReadLine() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("line1\nline2\nline3\n"));
        String result = reader.readLine();
        Assert.assertEquals("line1", result);
        Assert.assertEquals(1, reader.getLineNumber());

        result = reader.readLine();
        Assert.assertEquals("line2", result);
        Assert.assertEquals(2, reader.getLineNumber());

        result = reader.readLine();
        Assert.assertEquals("line3", result);
        Assert.assertEquals(3, reader.getLineNumber());

        result = reader.readLine();
        Assert.assertEquals(null, result);
        Assert.assertEquals(3, reader.getLineNumber());
    }

    @Test
    public void testLookAhead() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("abc"));
        int result = reader.lookAhead();
        Assert.assertEquals(-1, result);

        reader.read();
        reader.read();
        result = reader.lookAhead();
        Assert.assertEquals('c', result);
    }

    @Test(expected = IOException.class)
    public void testLookAheadException() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        reader.lookAhead();
    }

    @Test
    public void testGetLineNumber() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("line1\nline2\nline3\n"));
        Assert.assertEquals(0, reader.getLineNumber());

        reader.readLine();
        Assert.assertEquals(1, reader.getLineNumber());

        reader.readLine();
        Assert.assertEquals(2, reader.getLineNumber());

        reader.readLine();
        Assert.assertEquals(3, reader.getLineNumber());

        reader.readLine();
        Assert.assertEquals(3, reader.getLineNumber());
    }
}