import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        assertEquals(ParseSettings.preserveCase, builder.defaultSettings());
    }

    @Test
    public void testParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse("<xml></xml>", "http://example.com");
        assertNotNull(doc);
        assertTrue(doc.childNodes().size() > 0);
    }

    @Test
    public void testInitialiseParse() {
        // Test initialisation of parse method
    }

    @Test
    public void testProcess() {
        // Test processing of different token types
    }

    @Test
    public void testInsertNode() {
        // Test insertion of a node in the document
    }

    @Test
    public void testInsertStartTag() {
        // Test insertion of a start tag element
    }

    @Test
    public void testInsertComment() {
        // Test insertion of a comment
    }

    @Test
    public void testInsertCharacter() {
        // Test insertion of a character node
    }

    @Test
    public void testInsertDoctype() {
        // Test insertion of a doctype declaration
    }

    @Test
    public void testPopStackToClose() {
        // Test popping elements from the stack to close tags
    }

    @Test
    public void testParseFragment() {
        // Test parsing a fragment of XML
    }
}