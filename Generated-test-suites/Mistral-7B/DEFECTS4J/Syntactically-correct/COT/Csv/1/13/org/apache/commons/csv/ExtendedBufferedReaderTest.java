import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.BuffereredReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExtendedBufferedReaderTest {

    @Test
    public void testConstructor() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("test".getBytes())));
        Assert.assertNotNull(reader);
    }

    @Test
    public void testRead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("test\n".getBytes())));
        int actual = reader.read();
        Assert.assertEquals('t', actual);
    }

    @Test
    public void testReadAgain() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("test\n".getBytes())));
        int actual = reader.read();
        int expected = 't';
        Assert.assertEquals(expected, reader.readAgain());
    }

    @Test
    public void testReadWithCharArray() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("test\n".getBytes())));
        char[] buffer = new char[3];
        int length = reader.read(buffer, 0, buffer.length);
        String actual = new String(buffer, 0, length);
        String expected = "tes";
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testReadLine() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("test\n".getBytes())));
        String actual = reader.readLine();
        String expected = "test";
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IOException.class)
    public void testLookAheadWithEmptyBuffer() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("".getBytes())));
        reader.lookAhead();
    }

    @Test(expected = IOException.class)
    public void testLookAheadWithEof() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("test\n".getBytes())));
        reader.readLine();
        reader.lookAhead();
    }

    @Test
    public void testLookAhead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("test\n".getBytes())));
        int actual = reader.lookAhead();
        int expected = 't';
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetLineNumber() {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("test\ntest\n".getBytes())));
        int actual = reader.getLineNumber();
        int expected = 2;
        Assert.assertEquals(expected, actual);
    }
}