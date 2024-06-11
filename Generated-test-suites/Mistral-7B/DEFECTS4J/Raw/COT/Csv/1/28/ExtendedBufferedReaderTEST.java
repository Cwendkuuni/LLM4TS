import org.apache.commons.csv.ExtendedBufferedReader;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class ExtendedBufferedReaderTest {

    @Test
    public void testRead() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("line1\nline2\n".getBytes())));

        Assert.assertEquals('l', reader.read());
        Assert.assertEquals('i', reader.read());
        Assert.assertEquals('n', reader.read());
        Assert.assertEquals('e', reader.read());
        Assert.assertEquals('\n', reader.read()); // line counter should be incremented
        Assert.assertEquals('1', reader.readAgain()); // lastChar should be the last read character
        Assert.assertEquals('l', reader.read());
        Assert.assertEquals('i', reader.read());
        Assert.assertEquals('n', reader.read());
        Assert.assertEquals('g', reader.read());
        Assert.assertEquals('\n', reader.read());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());

        Assert.assertEquals(-1, reader.read()); // EOF reached
        Assert.assertEquals(-1, reader.readAgain()); // EOF reached
    }

    @Test
    public void testReadArray() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("line1\nline2\n".getBytes())));

        char[] buffer = new char[5];
        int length = reader.read(buffer, 0, buffer.length);

        Assert.assertEquals(4, length);
        Assert.assertEquals('l', buffer[0]);
        Assert.assertEquals('i', buffer[1]);
        Assert.assertEquals('n', buffer[2]);
        Assert.assertEquals('e', buffer[3]);

        Assert.assertEquals('\n', reader.read()); // line counter should be incremented
        Assert.assertEquals('1', reader.readAgain()); // lastChar should be the last read character

        length = reader.read(buffer, 0, buffer.length);
        Assert.assertEquals(4, length);
        Assert.assertEquals('l', buffer[0]);
        Assert.assertEquals('i', buffer[1]);
        Assert.assertEquals('n', buffer[2]);
        Assert.assertEquals('g', buffer[3]);

        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readAgain());
    }

    @Test
    public void testReadLine() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("line1\nline2\n".getBytes())));

        String line1 = reader.readLine();
        Assert.assertEquals("line1", line1);
        Assert.assertEquals('\n', reader.read()); // line counter should be incremented

        String line2 = reader.readLine();
        Assert.assertEquals("line2", line2);
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.read());

        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readLine());
        Assert.assertEquals(ExtendedBufferedReader.END_OF_STREAM, reader.readLine());
    }

    @Test(expected = IOException.class)
    public void testLookAheadException() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("line1\nline2\n".getBytes())));

        reader.lookAhead();
    }

    @Test(expected = IOException.class)
    public void testLookAheadEndOfStream() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new InputStreamReader(new ByteArrayInputStream("line1\nline2\n".getBytes())));

        reader.readLine();
        reader.readLine();
        reader.lookAhead();
    }
}