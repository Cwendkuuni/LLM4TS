import org.junit.Assert;
import org.junit.Test;
import java.io.StringReader;

public class ExtendedBufferedReaderTest {

    @Test
    public void testReadNormalCharacter() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader("Hello"));
        int actual = reader.read();
        Assert.assertEquals('H', actual);
    }

    @Test
    public void testReadEmptyLine() throws IOException {
        ExtendedBufferedReader reader = new ExtendedBufferedReader(new StringReader(""));
        String actual = reader.readLine();
        Assert.assertNull(actual);
    }

    // Add similar test cases for other methods
}