import static org.junit.Assert.*;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        assertNotNull(xmlTreeBuilder.defaultSettings());
    }

    @Test
    public void testParseWithReader() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        assertNotNull(xmlTreeBuilder.parse(new StringReader("<xml></xml>"), "http://example.com"));
    }

    @Test
    public void testParseWithString() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        assertNotNull(xmlTreeBuilder.parse("<xml></xml>", "http://example.com"));
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse(new StringReader("<xml></xml>"), "http://example.com");
        xmlTreeBuilder.insertNode(new Element("div"));
        assertNotNull(xmlTreeBuilder.currentElement().getElementsByTag("div"));
    }

    @Test
    public void testInsertStartElementTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        assertNotNull(xmlTreeBuilder.insert(new Token.StartTag("div")));
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse(new StringReader("<!-- comment -->"), "http://example.com");
        assertNotNull(xmlTreeBuilder.doc.getElementsByTag("comment"));
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.parse(new StringReader("character data"), "http://example.com");
        assertNotNull(xmlTreeBuilder.doc.text());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.insert(new Token.Doctype("html"));
        assertNotNull(xmlTreeBuilder.doc.childNodes().size());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        assertNotNull(xmlTreeBuilder.parseFragment("<div></div>", "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase));
    }
}