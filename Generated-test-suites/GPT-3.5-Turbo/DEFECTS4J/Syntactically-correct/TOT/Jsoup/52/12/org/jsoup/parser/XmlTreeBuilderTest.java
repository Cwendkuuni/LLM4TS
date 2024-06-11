import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.*;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<root><child></child></root>";
        String baseUri = "http://www.example.com";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        assertEquals("http://www.example.com", xmlTreeBuilder.doc.baseUri());
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
        assertEquals(2, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcess_StartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("div");
        startTag.attributes.put("id", "myDiv");

        boolean result = xmlTreeBuilder.process(startTag);

        assertTrue(result);
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("div", xmlTreeBuilder.currentElement().nodeName());
        assertEquals("myDiv", xmlTreeBuilder.currentElement().attr("id"));
    }

    @Test
    public void testProcess_EndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("div");

        boolean result = xmlTreeBuilder.process(endTag);

        assertTrue(result);
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcess_Comment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("This is a comment");

        xmlTreeBuilder.process(commentToken);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof Comment);
        assertEquals("This is a comment", ((Comment) xmlTreeBuilder.currentElement().childNodes().get(0)).getData());
    }

    @Test
    public void testProcess_Character() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("Some text");

        xmlTreeBuilder.process(characterToken);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof TextNode);
        assertEquals("Some text", ((TextNode) xmlTreeBuilder.currentElement().childNodes().get(0)).getWholeText());
    }

    @Test
    public void testProcess_Doctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "publicId", "systemId");

        xmlTreeBuilder.process(doctype);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof DocumentType);
        DocumentType documentType = (DocumentType) xmlTreeBuilder.currentElement().childNodes().get(0);
        assertEquals("html", documentType.getName());
        assertEquals("publicId", documentType.getPublicIdentifier());
        assertEquals("systemId", documentType.getSystemIdentifier());
    }

    @Test
    public void testInsert_StartTag_SelfClosing() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("img");
        startTag.attributes.put("src", "image.jpg");
        startTag.selfClosing = true;

        Element element = xmlTreeBuilder.insert(startTag);

        assertEquals("img", element.nodeName());
        assertEquals("image.jpg", element.attr("src"));
        assertTrue(element.isSelfClosing());
        assertTrue(element.tag().isSelfClosing());
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testInsert_StartTag_NotSelfClosing() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("div");
        startTag.attributes.put("id", "myDiv");
        startTag.selfClosing = false;

        Element element = xmlTreeBuilder.insert(startTag);

        assertEquals("div", element.nodeName());
        assertEquals("myDiv", element.attr("id"));
        assertFalse(element.isSelfClosing());
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals(element, xmlTreeBuilder.currentElement());
    }

    @Test
    public void testInsert_Comment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("This is a comment");

        xmlTreeBuilder.insert(commentToken);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof Comment);
        assertEquals("This is a comment", ((Comment) xmlTreeBuilder.currentElement().childNodes().get(0)).getData());
    }

    @Test
    public void testInsert_Character() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("Some text");

        xmlTreeBuilder.insert(characterToken);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof TextNode);
        assertEquals("Some text", ((TextNode) xmlTreeBuilder.currentElement().childNodes().get(0)).getWholeText());
    }

    @Test
    public void testInsert_Doctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "publicId", "systemId");

        xmlTreeBuilder.insert(doctype);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof DocumentType);
        DocumentType documentType = (DocumentType) xmlTreeBuilder.currentElement().childNodes().get(0);
        assertEquals("html", documentType.getName());
        assertEquals("publicId", documentType.getPublicIdentifier());
        assertEquals("systemId", documentType.getSystemIdentifier());
    }

    @Test
    public void testPopStackToClose_ElementFoundAndRemoved() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("div"), "", new Attributes()));
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("span"), "", new Attributes()));
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("div"), "", new Attributes()));
        Token.EndTag endTag = new Token.EndTag("div");

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("span", xmlTreeBuilder.currentElement().nodeName());
    }

    @Test
    public void testPopStackToClose_ElementNotFound() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("div"), "", new Attributes()));
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("span"), "", new Attributes()));
        Token.EndTag endTag = new Token.EndTag("section");

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(2, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<p>This is a paragraph</p>";
        String baseUri = "http://www.example.com";
        ParseErrorList errors = new ParseErrorList();

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);

        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        Element element = (Element) nodes.get(0);
        assertEquals("p", element.nodeName());
        assertEquals("This is a paragraph", element.text());
    }

    // Additional Test Cases:
    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("div"), "", new Attributes());
        Element currentElement = new Element(Tag.valueOf("span"), "", new Attributes());
        xmlTreeBuilder.stack.add(currentElement);

        xmlTreeBuilder.insertNode(element);

        assertEquals(1, currentElement.childNodes().size());
        assertEquals(element, currentElement.childNodes().get(0));
    }

    @Test
    public void testInsertNode_SkipsIfElementNotFound() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("div"), "", new Attributes());

        xmlTreeBuilder.insertNode(element);

        assertEquals(0, xmlTreeBuilder.currentElement().childNodes().size());
    }
}