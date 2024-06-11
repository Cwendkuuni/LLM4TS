import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.parser.XmlTreeBuilder;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<root><child>data</child></root>";
        Document doc = builder.parse(input, "http://example.com");
        assertNotNull(doc);
        assertEquals("root", doc.child(0).nodeName());
        assertEquals("data", doc.select("child").text());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        // Test inserting a new element node
        builder.insertNode(new Element("test"));
        assertNotNull(builder.currentElement().child(0));
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("test");
        Element el = builder.insert(startTag);
        assertNotNull(el);
        assertEquals("test", el.tagName());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("test");
        builder.stack.add(new Element("test"));
        builder.popStackToClose(endTag);
        assertEquals(0, builder.stack.size());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<child>fragment</child>";
        List<Node> nodes = builder.parseFragment(inputFragment, "http://example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertEquals(1, nodes.size());
        assertEquals("fragment", nodes.get(0).outerHtml());
    }

}