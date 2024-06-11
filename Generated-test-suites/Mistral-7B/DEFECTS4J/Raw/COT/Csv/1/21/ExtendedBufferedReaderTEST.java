import org.apache.commons.csv.ExtendedBufferedReader;
import org.apache.commons.csv.BufferiedReaderForTest;
import org.junit.Assert;
import org.junit.Test;

public class ExtendedBufferedReaderTest {

    @Test
    public void testConstructor() {
        Reader reader = new BufferiedReaderForTest();
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);
        Assert.assertNotNull(extendedBufferedReader);
    }

    @Test
    public void testRead() throws IOException {
        Reader reader = new BufferiedReaderForTest();
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        char[] buffer = new char[1];
        int result;

        result = extendedBufferedReader.read();
        Assert.assertEquals(UNDEFINED, result);

        reader.read('a');
        result = extendedBufferedReader.read();
        Assert.assertEquals('a', result);

        result = extendedBufferedReader.read();
        Assert.assertEquals(UNDEFINED, result);

        reader.read('b');
        result = extendedBufferedReader.read();
        Assert.assertEquals('b', result);

        result = extendedBufferedReader.read();
        Assert.assertEquals(UNDEFINED, result);

        reader.read('c');
        reader.read('d');
        result = extendedBufferedReader.read(buffer, 0, buffer.length);
        Assert.assertEquals(2, result);
        Assert.assertEquals('c', buffer[0]);
        Assert.assertEquals('d', buffer[1]);

        result = extendedBufferedReader.read();
        Assert.assertEquals(UNDEFINED, result);

        reader.close();
        result = extendedBufferedReader.read();
        Assert.assertEquals(END_OF_STREAM, result);
    }

    @Test
    public void testReadAgain() throws IOException {
        Reader reader = new BufferiedReaderForTest();
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        char[] buffer = new char[1];

        reader.read('a');
        extendedBufferedReader.read();
        int result = extendedBufferedReader.readAgain();
        Assert.assertEquals('a', result);

        reader.read('b');
        extendedBufferedReader.read();
        reader.read('c');
        result = extendedBufferedReader.readAgain();
        Assert.assertEquals('b', result);

        reader.close();
        result = extendedBufferedReader.readAgain();
        Assert.assertEquals(END_OF_STREAM, result);
    }

    @Test
    public void testReadLine() throws IOException {
        Reader reader = new BufferiedReaderForTest();
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        String result;

        result = extendedBufferedReader.readLine();
        Assert.assertNull(result);

        reader.read('a');
        reader.read('b');
        reader.read('c');
        reader.read('d');
        reader.readLine(); // consume a blank line

        result = extendedBufferedReader.readLine();
        Assert.assertEquals("abc", result);

        result = extendedBufferedReader.readLine();
        Assert.assertEquals("d", result);

        result = extendedBufferedReader.readLine();
        Assert.assertEquals(null, result);
    }

    @Test
    public void testLookAhead() throws IOException {
        Reader reader = new BufferiedReaderForTest();
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        char c;

        reader.read('a');
        c = extendedBufferedReader.lookAhead();
        Assert.assertEquals('a', c);

        reader.read('b');
        reader.read(); // consume 'b'
        c = extendedBufferedReader.lookAhead();
        Assert.assertEquals(UNDEFINED, c);

        reader.read('c');
        reader.read(); // consume 'c'
        reader.read('d');
        c = extendedBufferedReader.lookAhead();
        Assert.assertEquals('d', c);

        reader.close();
        c = extendedBufferedReader.lookAhead();
        Assert.assertEquals(END_OF_STREAM, c);
    }

    @Test
    public void testGetLineNumber() throws IOException {
        Reader reader = new BufferiedReaderForTest();
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);

        int result;

        result = extendedBufferedReader.getLineNumber();
        Assert.assertEquals(0, result);

        reader.read('a');
        result = extendedBufferedReader.getLineNumber();
        Assert.assertEquals(1, result);

        reader.read('b');
        reader.read(); // consume 'b'
        reader.read('c');
        reader.read(); // consume 'c'
        reader.readLine(); // consume a blank line
        reader.readLine(); // consume a second line
        result = extendedBufferedReader.getLineNumber();
        Assert.assertEquals(4, result);

        reader.close();
        result = extendedBufferedReader.getLineNumber();
        Assert.assertEquals(-1, result);
    }

    @Test(expected = IOException.class)
    public void testIOException() throws IOException {
        Reader reader = new BufferiedReaderForTest(new IOException("Error"));
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(reader);
        extendedBufferedReader.read();
    }

    @Test
    public void testNullReader() {
        ExtendedBufferedReader extendedBufferedReader = new ExtendedBufferedReader(null);
        Assert.assertNull(extendedBufferedReader);
    }
}