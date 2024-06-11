import org.jsoup.nodes.Document;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        String input = "<root></root>";
        String baseUri = "http://example.com";
        Document doc = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testProcess() {
        // Test for StartTag processing
        assertNotNull(xmlTreeBuilder.process(Token.StartTag));
        
        // Test for EndTag processing
        assertNotNull(xmlTreeBuilder.process(Token.EndTag));
        
        // Test for Comment processing
        assertNotNull(xmlTreeBuilder.process(Token.Comment));
        
        // Test for Character processing
        assertNotNull(xmlTreeBuilder.process(Token.Character));
        
        // Test for Doctype processing
        assertNotNull(xmlTreeBuilder.process(Token.Doctype));
        
        // Test for  EOF processing
        assertNotNull(xmlTreeBuilder.process(Token.EOF));
    }

    @Test
    public void testInsertNode() {
        Element element = new Element("div");
        xmlTreeBuilder.insertNode(element);
        Elements elements = xmlTreeBuilder.currentElement().children();
        assertTrue(elements.contains(element));
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("<div>");
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertEquals("div", element.tagName());
    }

    @Test
    public void testInsertComment() {
        Token.Comment commentToken = new Token.Comment("Test comment");
        xmlTreeBuilder.insert(commentToken);
        Elements elements = xmlTreeBuilder.currentElement().children();
        assertEquals("Test comment", elements.get(0).data());
    }

    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = new Token.Character("Test character");
        xmlTreeBuilder.insert(characterToken);
        Elements elements = xmlTreeBuilder.currentElement().children();
        assertEquals("Test character", elements.get(0).ownText());
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", null, null);
        xmlTreeBuilder.insert(doctype);
        Elements elements = xmlTreeBuilder.currentElement().children();
        assertEquals("<!DOCTYPE html>", elements.get(0).outerHtml());
    }

    @Test
    public void testPopStackToClose() {
        // Test for popping element when found
        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.stack.add(new Element("p"));
        xmlTreeBuilder.stack.add(new Element("div"));
        xmlTreeBuilder.popStackToClose(endTag);
        assertTrue(xmlTreeBuilder.stack.size() == 1);
        
        // Test for not popping when element not found
        Token.EndTag endTagNotFound = new Token.EndTag("span");
        xmlTreeBuilder.stack.add(new Element("p"));
        xmlTreeBuilder.stack.add(new Element("div"));
        xmlTreeBuilder.popStackToClose(endTagNotFound);
        assertTrue(xmlTreeBuilder.stack.size() == 2);
    }
}