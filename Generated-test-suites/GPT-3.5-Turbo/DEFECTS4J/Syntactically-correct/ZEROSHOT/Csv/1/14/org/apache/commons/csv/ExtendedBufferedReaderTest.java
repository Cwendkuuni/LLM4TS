import org.junit.Before;
import org.junit.Test;
import java.io.StringReader;

public class ExtendedBufferedReaderTest {

    private ExtendedBufferedReader reader;

    @Before
    public void setUp() {
        StringReader stringReader = new StringReader("This is a test string");
        reader = new ExtendedBufferedReader(stringReader);
    }

    @Test
    public void testRead() throws Exception {
        int result = reader.read();
        assert result == 'T';
    }

    @Test
    public void testReadAgain() throws Exception {
        int result = reader.readAgain();
        assert result == -2;
    }

    @Test
    public void testReadCharArray() throws Exception {
        char[] buf = new char[5];
        int result = reader.read(buf, 0, 5);
        assert (result == 5 && new String(buf).equals("This "));
    }

    @Test
    public void testReadLine() throws Exception {
        String result = reader.readLine();
        assert result.equals("This is a test string");
        assert reader.getLineNumber() == 1;
    }

    @Test
    public void testLookAhead() throws Exception {
        int result = reader.lookAhead();
        assert result == 'h';
    }

    @Test
    public void testGetLineNumber() throws Exception {
        int result = reader.getLineNumber();
        assert result == 0;
    }
}