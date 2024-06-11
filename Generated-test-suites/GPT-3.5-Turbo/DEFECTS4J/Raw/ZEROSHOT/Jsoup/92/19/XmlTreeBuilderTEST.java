import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;

public class XmlTreeBuilderTest {

    @Test
    public void testParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?><root><element>Example</element><element>Example2</element></root>";
        Document doc = builder.parse(input, "");
        assertNotNull(doc);
        assertEquals("root", doc.tagName());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<element>Example</element><element>Example2</element>";
        String baseUri = "";
        Parser parser = new Parser(builder);
        List<Node> nodes = builder.parseFragment(input, baseUri, parser);
        assertNotNull(nodes);
        assertEquals(2, nodes.size());
    }

    @Test
    public void testInsert() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag();
        startTag.name = "element";
        startTag.attributes = new HashMap<String, String>();
        startTag.attributes.put("attr", "value");
        Element el = builder.insert(startTag);
        assertNotNull(el);
        assertEquals("element", el.tagName());
        assertTrue(el.hasAttr("attr"));
        assertEquals("value", el.attr("attr"));
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element el = new Element("element");
        builder.currentElement().appendChild(el);
        Node node = new TextNode("Example");
        builder.insertNode(node);
        assertEquals(1, el.childNodes().size());
        assertEquals(node, el.childNode(0));
    }

    @Test
    public void testProcess() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag();
        startTag.name = "element";
        startTag.attributes = new HashMap<String, String>();
        builder.process(startTag);
        assertEquals(1, builder.stack.size());
        assertEquals("element", builder.currentElement().tagName());

        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "element";
        builder.process(endTag);
        assertEquals(0, builder.stack.size());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element el1 = new Element("element");
        Element el2 = new Element("element");
        builder.stack.add(el1);
        builder.stack.add(el2);
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "element";
        builder.popStackToClose(endTag);
        assertEquals(0, builder.stack.size());
    }
}