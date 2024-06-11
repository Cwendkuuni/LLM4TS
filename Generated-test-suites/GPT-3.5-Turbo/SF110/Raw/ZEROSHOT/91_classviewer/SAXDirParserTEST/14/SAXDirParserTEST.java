import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class SAXDirParserTest {

    private SAXDirParser parser;

    @Before
    public void setUp() throws Exception {
        parser = new SAXDirParser();
    }

    @Test
    public void testGetWebData() {
        assertEquals("", parser.getWebData());
    }

    @Test
    public void testStartElement() {
        // Add test cases here
    }

    @Test
    public void testCharacters() {
        // Add test cases here
    }

    @Test
    public void testEndElement() {
        // Add test cases here
    }

    @Test
    public void testGetDirectory() {
        // Add test cases here
    }

    @Test
    public void testGetLocalPackages() {
        // Add test cases here
    }

    @Test
    public void testGetLocalBrowser() {
        // Add test cases here
    }

    @Test
    public void testGetEditor() {
        // Add test cases here
    }

    @Test
    public void testAcceptsLineNumber() {
        // Add test cases here
    }

    @Test
    public void testLineNumberParameter() {
        // Add test cases here
    }
}