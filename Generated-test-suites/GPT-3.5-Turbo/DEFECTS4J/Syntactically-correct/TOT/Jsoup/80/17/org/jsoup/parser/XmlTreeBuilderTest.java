import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        assertEquals(ParseSettings.preserveCase, xmlTreeBuilder.defaultSettings());
    }

    @Test
    public void testParse_ReaderBaseUri() {
        String input = "<root><element attribute=\"value\">Text</element></root>";
        String baseUri = "http://example.com";
        Reader reader = new StringReader(input);
        Document document = xmlTreeBuilder.parse(reader, baseUri);
        
        assertEquals("http://example.com", document.baseUri());
        assertEquals("<root>\n" +
                " <element attribute=\"value\">\n" +
                "  Text\n" +
                " </element>\n" +
                "</root>", document.outerHtml());
    }

    @Test
    public void testParse_StringBaseUri() {
        String input = "<root><element attribute=\"value\">Text</element></root>";
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        
        assertEquals("http://example.com", document.baseUri());
        assertEquals("<root>\n" +
                " <element attribute=\"value\">\n" +
                "  Text\n" +
                " </element>\n" +
                "</root>", document.outerHtml());
    }

    @Test
    public void testInitialiseParse_ReaderBaseUriErrorsSettings() {
        // Test initialization with valid reader, baseUri, errors, and settings
        Reader reader = new StringReader("<root><element attribute=\"value\">Text</element></root>");
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        
        xmlTreeBuilder.initialiseParse(reader, baseUri, errors, settings);
        
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("<root>\n" +
                " <element attribute=\"value\">\n" +
                "  Text\n" +
                " </element>\n" +
                "</root>", xmlTreeBuilder.stack.get(0).outerHtml());
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcess_StartTag() {
        Token token = new Token.StartTag("element");
        xmlTreeBuilder.stack.push(new Element("root"));
        xmlTreeBuilder.process(token);
        
        assertEquals("<root>\n" +
                " <element></element>\n" +
                "</root>", xmlTreeBuilder.stack.peek().parent().outerHtml());
    }

    @Test
    public void testProcess_EndTag_NotFound() {
        Token token = new Token.EndTag("element");
        xmlTreeBuilder.stack.push(new Element("root"));
        xmlTreeBuilder.process(token);
        
        assertEquals(1, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcess_EndTag_Found() {
        Token token = new Token.EndTag("element");
        Element element = new Element("element");
        xmlTreeBuilder.stack.push(new Element("root"));
        xmlTreeBuilder.stack.push(element);
        xmlTreeBuilder.process(token);
        
        assertEquals(1, xmlTreeBuilder.stack.size());
        assertNotEquals(element, xmlTreeBuilder.stack.peek());
    }

    @Test
    public void testProcess_Comment() {
        Token token = new Token.Comment("This is a comment");
        xmlTreeBuilder.stack.push(new Element("root"));
        xmlTreeBuilder.process(token);
        
        assertEquals("<root>\n" +
                " <!--This is a comment-->\n" +
                "</root>", xmlTreeBuilder.stack.peek().parent().outerHtml());
    }

    @Test
    public void testProcess_Character() {
        Token token = new Token.Character("Text");
        xmlTreeBuilder.stack.push(new Element("root"));
        xmlTreeBuilder.process(token);
        
        assertEquals("<root>\nText\n</root>", xmlTreeBuilder.stack.peek().parent().outerHtml());
    }

    @Test
    public void testProcess_Doctype() {
        Token token = new Token.Doctype("html", "publicId", "systemId");
        xmlTreeBuilder.stack.push(new Element("root"));
        xmlTreeBuilder.process(token);
        
        assertEquals("<root>\n<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">\n</root>", xmlTreeBuilder.stack.peek().parent().outerHtml());
    }

    @Test
    public void testInsertNode() {
        Element element = new Element("element");
        xmlTreeBuilder.stack.push(new Element("root"));
        xmlTreeBuilder.insertNode(element);
        
        assertEquals(element, xmlTreeBuilder.stack.peek().child(0));
    }

    @Test
    public void testInsert_StartTag_SelfClosing_KnownTag() {
        Token.StartTag startTag = new Token.StartTag("input", true);
        xmlTreeBuilder.stack.push(new Element("root"));
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertTrue(element.tag().isKnownTag());
        assertTrue(element.tag().isSelfClosing());
    }

    @Test
    public void testInsert_StartTag_SelfClosing_UnknownTag() {
        Token.StartTag startTag = new Token.StartTag("customtag", true);
        xmlTreeBuilder.stack.push(new Element("root"));
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertFalse(element.tag().isKnownTag());
        assertTrue(element.tag().isSelfClosing());
    }

    @Test
    public void testInsert_StartTag_NotSelfClosing() {
        Token.StartTag startTag = new Token.StartTag("element", false);
        xmlTreeBuilder.stack.push(new Element("root"));
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertFalse(element.tag().isSelfClosing());
        assertEquals(element, xmlTreeBuilder.stack.peek());
    }

    @Test
    public void testInsert_Comment_Bogus() {
        Token.Comment commentToken = new Token.Comment("<!-- Comment -->");
        xmlTreeBuilder.stack.push(new Element("root"));
        xmlTreeBuilder.insert(commentToken);
        
        assertEquals("<!-- Comment -->", xmlTreeBuilder.stack.peek().childNode(0).outerHtml());
    }

    @Test
    public void testInsert_Comment_Bogus_UnknownTag() {
        Token.Comment commentToken = new Token.Comment("<!--?xml version=\"1.0\"?-->");
        xmlTreeBuilder.stack.push(new Element("root"));
        xmlTreeBuilder.insert(commentToken);
        
        assertEquals("<!--?xml version=\"1.0\"?-->", xmlTreeBuilder.stack.peek().childNode(0).outerHtml());
    }

    @Test
    public void testInsert_Comment_Bogus_XmlDeclaration() {
        Token.Comment commentToken = new Token.Comment("<!--?xml version=\"1.0\"?-->");
        xmlTreeBuilder.stack.push(new Element("root"));
        xmlTreeBuilder.insert(commentToken);
        
        assertEquals(1, xmlTreeBuilder.stack.peek().childNodes().size());
        assertTrue(xmlTreeBuilder.stack.peek().childNode(0) instanceof XmlDeclaration);
    }

    @Test
    public void testInsert_Character_TextNode() {
        Token.Character characterToken = new Token.Character("Text");
        xmlTreeBuilder.stack.push(new Element("root"));
        xmlTreeBuilder.insert(characterToken);
        
        assertTrue(xmlTreeBuilder.stack.peek().childNode(0) instanceof TextNode);
        assertEquals("Text", xmlTreeBuilder.stack.peek().childNode(0).outerHtml());
    }

    @Test
    public void testInsert_Character_CDataNode() {
        Token.Character characterToken = new Token.Character("<![CDATA[Text]]>");
        xmlTreeBuilder.stack.push(new Element("root"));
        xmlTreeBuilder.insert(characterToken);
        
        assertTrue(xmlTreeBuilder.stack.peek().childNode(0) instanceof CDataNode);
        assertEquals("<![CDATA[Text]]>", xmlTreeBuilder.stack.peek().childNode(0).outerHtml());
    }

    @Test
    public void testInsert_Doctype() {
        Token.Doctype doctype = new Token.Doctype("html", "publicId", "systemId");
        xmlTreeBuilder.stack.push(new Element("root"));
        xmlTreeBuilder.insert(doctype);
        
        assertTrue(xmlTreeBuilder.stack.peek().childNode(0) instanceof DocumentType);
        assertEquals("<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">", xmlTreeBuilder.stack.peek().childNode(0).outerHtml());
    }

    @Test
    public void testPopStackToClose_NotFound() {
        Token.EndTag endTag = new Token.EndTag("element");
        xmlTreeBuilder.stack.push(new Element("root"));
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertEquals(1, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testPopStackToClose_Found() {
        Token.EndTag endTag = new Token.EndTag("element");
        Element element = new Element("element");
        xmlTreeBuilder.stack.push(new Element("root"));
        xmlTreeBuilder.stack.push(element);
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<element attribute=\"value\">Text</element>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = ParseSettings.preserveCase;
        
        xmlTreeBuilder.stack.push(new Element("root"));
        assertEquals(1, xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings).size());
        assertTrue(xmlTreeBuilder.stack.peek().childNode(0) instanceof Element);
        assertEquals(inputFragment, xmlTreeBuilder.stack.peek().childNode(0).outerHtml());
    }
}