import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        assertEquals(builder.defaultSettings(), ParseSettings.preserveCase);
    }

    @Test
    public void testParseWithReaderBaseUri() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        assertNotNull(builder.parse(new StringReader("<xml></xml>"), "http://example.com"));
    }

    @Test
    public void testParseWithStringBaseUri() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        assertNotNull(builder.parse("<xml></xml>", "http://example.com"));
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse(new StringReader("<xml></xml>"), "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        builder.insertNode(new Element(Tag.valueOf("div"), "http://example.com", ParseSettings.preserveCase.normalizeAttributes()));
        assertNotNull(builder.currentElement().child(0));
    }

    // Add more test cases for other methods in XmlTreeBuilder class

}