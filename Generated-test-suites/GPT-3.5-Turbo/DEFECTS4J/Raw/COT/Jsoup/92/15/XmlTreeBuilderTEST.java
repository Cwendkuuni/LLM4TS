import org.jsoup.nodes.*;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Token;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        // Test that the document is placed onto the stack and syntax is set to XML
        Reader input = new StringReader("<xml></xml>");
        xmlTreeBuilder.initialiseParse(input, "baseUri", Parser.xmlParser());

        List<Node> stack = xmlTreeBuilder.stack;
        Document doc = xmlTreeBuilder.doc;

        assertEquals(1, stack.size());
        assertSame(doc, stack.get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, doc.outputSettings().syntax());
    }

    @Test
    public void testParseReaderBaseUri() {
        // Test parsing XML from a Reader with baseUri
        Reader input = new StringReader("<xml></xml>");
        Document result = xmlTreeBuilder.parse(input, "baseUri");

        assertNotNull(result);
        assertEquals("<xml></xml>", result.toString());
    }

    @Test
    public void testParseStringBaseUri() {
        // Test parsing XML from a String with baseUri
        String input = "<xml></xml>";
        Document result = xmlTreeBuilder.parse(input, "baseUri");

        assertNotNull(result);
        assertEquals("<xml></xml>", result.toString());
    }

    @Test
    public void testProcessStartTag() {
        // Test processing a StartTag token
        Token.StartTag startTag = new Token.StartTag("tag", null);
        Element element = xmlTreeBuilder.insert(startTag);

        assertNotNull(element);
        assertEquals("tag", element.tagName());
    }

    @Test
    public void testProcessEndTag() {
        // Test processing an EndTag token
        Token.EndTag endTag = new Token.EndTag("tag");
        xmlTreeBuilder.stack.add(new Element("tag"));
        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcessComment() {
        // Test processing a Comment token
        Token.Comment commentToken = new Token.Comment("comment");
        xmlTreeBuilder.insert(commentToken);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        Comment comment = (Comment) childNodes.get(0);

        assertNotNull(comment);
        assertEquals("comment", comment.getData());
    }

    @Test
    public void testProcessCharacter() {
        // Test processing a Character token
        Token.Character charToken = new Token.Character("data");
        xmlTreeBuilder.insert(charToken);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        TextNode textNode = (TextNode) childNodes.get(0);

        assertNotNull(textNode);
        assertEquals("data", textNode.getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        // Test processing a Doctype token
        Token.Doctype doctype = new Token.Doctype("name", "public", "system");
        xmlTreeBuilder.insert(doctype);

        List<Node> childNodes = xmlTreeBuilder.currentElement().childNodes();
        DocumentType documentType = (DocumentType) childNodes.get(0);

        assertNotNull(documentType);
        assertEquals("name", documentType.name());
        assertEquals("public", documentType.publicIdentifier());
        assertEquals("system", documentType.systemIdentifier());
    }

    @Test
    public void testPopStackToClose() {
        // Test popping stack elements to close a tag
        xmlTreeBuilder.stack.add(new Element("tag"));
        xmlTreeBuilder.popStackToClose(new Token.EndTag("tag"));

        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testParseFragment() {
        // Test parsing a fragment of XML
        String inputFragment = "<fragment>content</fragment>";
        List<Node> result = xmlTreeBuilder.parseFragment(inputFragment, "baseUri", Parser.xmlParser());

        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    public void testParseFragmentWithContext() {
        // Test parsing a fragment of XML with a context element
        Element context = new Element("context");
        String inputFragment = "<fragment>content</fragment>";
        List<Node> result = xmlTreeBuilder.parseFragment(inputFragment, context, "baseUri", Parser.xmlParser());

        assertNotNull(result);
        assertEquals(1, result.size());
    }
}