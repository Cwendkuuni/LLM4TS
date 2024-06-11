import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        String input = "<xml><tag></tag></xml>";
        Document document = xmlTreeBuilder.parse(new StringReader(input), "http://example.com/xml");
        assertNotNull(document);
        assertEquals("http://example.com/xml", document.baseUri());
    }

    @Test
    public void testInitialiseParse() {
        // Test for initialization of parse method
        // Add appropriate test scenario here
    }

    @Test
    public void testProcessWithStartTagToken() {
        // Test for process method with StartTag token
        // Add appropriate test scenario here
    }

    @Test
    public void testProcessWithEndTagToken() {
        // Test for process method with EndTag token
        // Add appropriate test scenario here
    }

    @Test
    public void testInsertNode() {
        // Test for insertNode method
        // Add appropriate test scenario here
    }

    // Continue adding test cases for each public method in XmlTreeBuilder class

}