import static org.junit.Assert.*;
import org.junit.Test;

public class SAXDirParserTest {

    @Test
    public void testGetWebData() {
        SAXDirParser parser = new SAXDirParser();
        String expected = "";
        assertEquals(expected, parser.getWebData());
    }
}