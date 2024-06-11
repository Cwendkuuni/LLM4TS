import org.jsoup.parser.*;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        assertEquals(ParseSettings.preserveCase, xmlTreeBuilder.defaultSettings());
    }

    @Test
    public void testParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse("<xml></xml>", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("test"), "http://example.com");
        xmlTreeBuilder.insertNode(element);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("test", new Attributes());
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
    }

    // Additional test cases for other methods can be added here

}