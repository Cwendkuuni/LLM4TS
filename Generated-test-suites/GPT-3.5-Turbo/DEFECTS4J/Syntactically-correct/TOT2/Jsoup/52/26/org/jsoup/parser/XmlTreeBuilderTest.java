import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("input", "baseUri", new ParseErrorList());
        assertEquals("baseUri", builder.baseUri);
        assertTrue(builder.stack.contains(builder.doc));
        assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcess() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        assertTrue(builder.process(new Token(Token.TokenType.EOF)));
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element = new Element("test", "http://example.com", new Attributes());
        builder.stack.add(element);
        builder.insertNode(element);
        assertEquals(element, builder.currentElement());
    }

    // Add tests for other public methods here

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment("<div>test</div>", "http://example.com", new ParseErrorList());
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("div", nodes.get(0).nodeName());
    }
}