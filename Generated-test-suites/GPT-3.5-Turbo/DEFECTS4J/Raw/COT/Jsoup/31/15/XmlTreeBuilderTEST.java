import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.nodes.*;
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
        String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        assertEquals("http://example.com", xmlTreeBuilder.baseUri);
        assertNotNull(xmlTreeBuilder.stack);
        assertNotNull(xmlTreeBuilder.doc);
        assertEquals(1, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("tag");
        Element element = xmlTreeBuilder.insert(startTag);

        assertNotNull(element);
        assertEquals("tag", element.nodeName());
        assertNotNull(element.attributes());
        assertEquals(0, element.childNodeSize());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character characterToken = new Token.Character();
        characterToken.data("data");
        xmlTreeBuilder.insert(characterToken);

        NodeList nodes = xmlTreeBuilder.doc.childNodes();
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof TextNode);
        assertEquals("data", ((TextNode) nodes.get(0)).getWholeText());
    }

    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment();
        commentToken.data("comment");
        xmlTreeBuilder.insert(commentToken);

        NodeList nodes = xmlTreeBuilder.doc.childNodes();
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Comment);
        assertEquals("comment", ((Comment) nodes.get(0)).getData());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name("doctype");
        doctype.publicIdentifier("public");
        doctype.systemIdentifier("system");
        xmlTreeBuilder.insert(doctype);

        NodeList nodes = xmlTreeBuilder.doc.childNodes();
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof DocumentType);
        DocumentType documentType = (DocumentType) nodes.get(0);
        assertEquals("doctype", documentType.getName());
        assertEquals("public", documentType.getPublicIdentifier());
        assertEquals("system", documentType.getSystemIdentifier());
    }

    @Test
    public void testPopStackToClose() {
        Element root = new Element("root");
        Element child1 = new Element("child1");
        Element child2 = new Element("child2");
        Element child3 = new Element("child3");
        root.appendChild(child1);
        root.appendChild(child2);
        root.appendChild(child3);
        xmlTreeBuilder.stack.add(root);
        xmlTreeBuilder.stack.add(child1);
        xmlTreeBuilder.stack.add(child2);
        xmlTreeBuilder.stack.add(child3);

        Token.EndTag endTag = new Token.EndTag();
        endTag.name("child2");
        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("child3", xmlTreeBuilder.stack.getFirst().nodeName());
    }

    // Add additional test cases for edge cases and exception handling scenarios

}