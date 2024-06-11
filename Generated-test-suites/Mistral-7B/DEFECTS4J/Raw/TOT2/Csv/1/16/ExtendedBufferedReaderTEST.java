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
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld"));
        int result = reader.read();
        Assert.assertEquals('H', (char) result);
        result = reader.read();
        Assert.assertEquals('e', (char) result);
        // Test end of stream and undefined
        result = reader.read();
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, result);
        result = reader.readAgain();
        Assert.assertEquals(ExtendedBufferedReader.UNDEFINED, result);
    }

    @Test
    public void testReadAgain() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld"));
        reader.read();
        reader.read();
        int result = reader.readAgain();
        Assert.assertEquals('e', (char) result);
        reader.read();
        reader.read();
        result = reader.readAgain();
        Assert.assertEquals('l', (char) result);
    }

    @Test
    public void testReadCharacterArray() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld"));
        char[] buffer = new char[5];
        int result = reader.read(buffer, 0, buffer.length);
        Assert.assertEquals(4, result);
        Assert.assertEquals('H', buffer[0]);
        Assert.assertEquals('e', buffer[1]);
        Assert.assertEquals('l', buffer[2]);
        Assert.assertEquals('o', buffer[3]);
        Assert.assertEquals('\n', buffer[4]);
    }

    @Test
    public void testReadLine() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld\n"));
        String result = reader.readLine();
        Assert.assertEquals("Hello", result);
        result = reader.readLine();
        Assert.assertEquals("World", result);
        Assert.assertNull(reader.readLine());
    }

    @Test
    public void testLookAhead() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld"));
        int result = reader.lookAhead();
        Assert.assertEquals('H', (char) result);
        result = reader.lookAhead();
        Assert.assertEquals('e', (char) result);
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.lookAhead());

        try {
            reader.lookAhead();
            reader.read();
            reader.lookAhead();
            Assert.fail();
        } catch (IOException e) {
            // Expected
        }
    }

    @Test
    public void testGetLineNumber() throws Exception {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello\nWorld\n"));
        Assert.assertEquals(1, reader.getLineNumber());
        reader.readLine();
        Assert.assertEquals(2, reader.getLineNumber());
        reader.readLine();
        Assert.assertEquals(3, reader.getLineNumber());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.getLineNumber());
    }
}