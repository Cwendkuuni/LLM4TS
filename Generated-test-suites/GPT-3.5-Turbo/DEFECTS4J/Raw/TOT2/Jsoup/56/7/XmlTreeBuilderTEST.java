import org.jsoup.parser.*;
import org.jsoup.nodes.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        assertEquals(ParseSettings.preserveCase, treeBuilder.defaultSettings());
    }

    @Test
    public void testParse() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Document doc = treeBuilder.parse("<html></html>", "http://example.com");
        assertNotNull(doc);
        assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Element el = new Element(Tag.valueOf("div"), "http://example.com");
        treeBuilder.insertNode(el);
        assertEquals(el, treeBuilder.currentElement().childNode(0));
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("div");
        Element el = treeBuilder.insert(startTag);
        assertFalse(startTag.isSelfClosing());
        assertFalse(el.tag().isKnownTag());
    }

    // More test cases for other methods can be added here

}