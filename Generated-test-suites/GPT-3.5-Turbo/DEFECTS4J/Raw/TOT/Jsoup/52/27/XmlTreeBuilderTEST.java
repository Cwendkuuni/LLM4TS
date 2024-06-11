import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        String input = "<root></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        assertEquals(baseUri, xmlTreeBuilder.doc.baseUri());
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("root", xmlTreeBuilder.currentElement().nodeName());
        assertNotNull(xmlTreeBuilder.doc.outputSettings());
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcess_StartTag() {
        Token.StartTag startTag = new Token.StartTag("tag", "attr1=value1");
        
        xmlTreeBuilder.insert(startTag);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertEquals("tag", xmlTreeBuilder.currentElement().childNodes().get(0).nodeName());
        assertEquals("value1", xmlTreeBuilder.currentElement().attributes().get("attr1"));
    }

    @Test
    public void testProcess_EndTag() {
        Token.EndTag endTag = new Token.EndTag("tag");

        xmlTreeBuilder.stack.add(new Element("tag"));

        xmlTreeBuilder.popStackToClose(endTag);

        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testProcess_Comment() {
        Token.Comment commentToken = new Token.Comment("Test comment");

        xmlTreeBuilder.insert(commentToken);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertEquals("Test comment", xmlTreeBuilder.currentElement().childNodes().get(0).toString());
        assertFalse(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof Comment);
    }

    @Test
    public void testProcess_Character() {
        Token.Character characterToken = new Token.Character("Test character");

        xmlTreeBuilder.insert(characterToken);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertEquals("Test character", xmlTreeBuilder.currentElement().childNodes().get(0).toString());
        assertFalse(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof TextNode);
    }

    @Test
    public void testProcess_Doctype() {
        Token.Doctype doctype = new Token.Doctype("html");

        xmlTreeBuilder.insert(doctype);

        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertEquals("html", xmlTreeBuilder.currentElement().childNodes().get(0).nodeName());
        assertFalse(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof DocumentType);
    }

    @Test
    public void testInsertNode() {
        xmlTreeBuilder.currentElement().appendChild(new Element("child"));

        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertEquals("child", xmlTreeBuilder.currentElement().childNodes().get(0).nodeName());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<root><child></child></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(inputFragment, baseUri, errors);
        xmlTreeBuilder.runParser();

        assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
        assertEquals("root", xmlTreeBuilder.doc.childNodes().get(0).nodeName());
        assertEquals(1, xmlTreeBuilder.doc.childNodes().get(0).childNodes().size());
        assertEquals("child", xmlTreeBuilder.doc.childNodes().get(0).childNodes().get(0).nodeName());
    }

}