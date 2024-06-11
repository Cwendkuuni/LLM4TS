import org.jsoup.parser.*;
import org.jsoup.nodes.*;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseReaderBaseUri() {
        String html = "<html><head></head><body></body></html>";
        Reader reader = new StringReader(html);
        Document doc = xmlTreeBuilder.parse(reader, "example.com");
        assertNotNull(doc);
        assertEquals("example.com", doc.baseUri());
    }

    // Additional test cases for each method can be implemented following a similar pattern

    @Test
    public void testInsertNode() {
        Element element = new Element(Tag.valueOf("div"), "example.com");
        xmlTreeBuilder.insertNode(element);
        Element insertedElement = xmlTreeBuilder.currentElement().children().get(0);
        assertEquals(element, insertedElement);
    }

    @Test
    public void testInsertStartTag() {       
        Token.StartTag startTag = Token.createStartTag("a");
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertEquals("a", element.tagName());
    }

    @Test
    public void testInsertComment() {
        String data = "Test Comment";
        Token.Comment commentToken = Token.createComment(data);
        xmlTreeBuilder.insert(commentToken);
        Comment comment = (Comment) xmlTreeBuilder.currentElement().childNode(0);
        assertEquals(data, comment.getData());
    }

    @Test
    public void testInsertCharacter() {
        String data = "Test Character";
        Token.Character characterToken = Token.createCharacter(data);
        xmlTreeBuilder.insert(characterToken);
        TextNode textNode = (TextNode) xmlTreeBuilder.currentElement().childNode(0);
        assertEquals(data, textNode.getWholeText());
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = Token.createDoctype("html", "", "");
        xmlTreeBuilder.insert(doctype);
        DocumentType documentType = (DocumentType) xmlTreeBuilder.currentElement().childNode(0);
        assertEquals("html", documentType.getName());
    }

    @Test
    public void testPopStackToClose() {
        Element element = new Element(Tag.valueOf("div"), "example.com");
        xmlTreeBuilder.currentElement().appendChild(element);
        Token.EndTag endTag = Token.createEndTag("div");
        xmlTreeBuilder.popStackToClose(endTag);
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<p>Test</p>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, "example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
    }
}