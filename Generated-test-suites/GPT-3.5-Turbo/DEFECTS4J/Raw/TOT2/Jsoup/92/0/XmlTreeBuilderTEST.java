import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        assertEquals(ParseSettings.preserveCase, xmlTreeBuilder.defaultSettings());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = new Document("");
        xmlTreeBuilder.setDocument(document);
        xmlTreeBuilder.insertNode(document);
        assertEquals(document, xmlTreeBuilder.currentElement().childNodes().get(0));
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(new StringReader("<test/>"), "http://example.com", new Parser(xmlTreeBuilder));
        Element element = xmlTreeBuilder.insert(token.asStartTag());
        assertEquals("test", element.tagName());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(new StringReader("<div><span></span></div>"), "http://example.com", new Parser(xmlTreeBuilder));
        xmlTreeBuilder.process(Token.EndTag("</span>"));
        xmlTreeBuilder.process(Token.EndTag("</div>"));
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

}