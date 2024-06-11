import org.jsoup.nodes.Document;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import java.io.StringReader;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testParse() {
        Document doc = xmlTreeBuilder.parse("<html></html>", "http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("html", doc.child(0).tagName());
    }

    @Test
    public void testProcess() {
        assertTrue(xmlTreeBuilder.process(Token.StartTag));
        // Add more test scenarios for different token types
    }

    @Test
    public void testInsertNode() {
        TextNode textNode = new TextNode("Hello, World!");
        xmlTreeBuilder.insertNode(textNode);
        assertEquals("Hello, World!", xmlTreeBuilder.currentElement().text());
    }

    @Test
    public void testInsert() {
        Token.StartTag startTag = new Token.StartTag("div", new Attributes());
        Element element = xmlTreeBuilder.insert(startTag);
        assertEquals("div", element.tagName());
        // Add edge cases for unknown tags
    }

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);
        assertEquals("<!-- This is a comment -->", xmlTreeBuilder.currentElement().outerHtml());
    }

    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character("Text node");
        xmlTreeBuilder.insert(characterToken);
        assertEquals("Text node", xmlTreeBuilder.currentElement().text());
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "");
        xmlTreeBuilder.insert(doctype);
        assertEquals("<!DOCTYPE html>", xmlTreeBuilder.currentElement().outerHtml());
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.stack.add(new Element("div"));
        xmlTreeBuilder.popStackToClose(endTag);
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    // Add more test cases for other methods in XmlTreeBuilder class

}