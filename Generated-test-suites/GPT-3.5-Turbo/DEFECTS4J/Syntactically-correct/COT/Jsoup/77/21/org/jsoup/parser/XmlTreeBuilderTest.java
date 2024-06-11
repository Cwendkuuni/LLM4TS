import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
    }
    
    @Test
    public void testParseWithReaderAndBaseUri() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse(new StringReader("<xml></xml>"), "http://www.example.com");
        assertEquals("http://www.example.com", doc.baseUri());
        assertTrue(doc.html().contains("<xml></xml>"));
    }
    
    @Test
    public void testParseWithStringAndBaseUri() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = xmlTreeBuilder.parse("<xml></xml>", "http://www.example.com");
        assertEquals("http://www.example.com", doc.baseUri());
        assertTrue(doc.html().contains("<xml></xml>"));
    }
    
    @Test
    public void testInitialiseParseWithReaderAndBaseUriAndErrorsAndSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        xmlTreeBuilder.initialiseParse(new StringReader("<xml></xml>"), "http://www.example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        // Check if the document is added to the stack
        assertEquals(1, xmlTreeBuilder.stack.size());
        // Check if the document's syntax is set to xml
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }
    
    @Test
    public void testProcessWithStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token.StartTag("xml");
        assertTrue(xmlTreeBuilder.process(token));
    }
    
    @Test
    public void testProcessWithEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token.EndTag("xml");
        assertTrue(xmlTreeBuilder.process(token));
    }
    
    @Test
    public void testProcessWithComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token.Comment("This is a comment");
        assertTrue(xmlTreeBuilder.process(token));
    }
    
    @Test
    public void testProcessWithCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token.Character("This is a character");
        assertTrue(xmlTreeBuilder.process(token));
    }
    
    @Test
    public void testProcessWithDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token.Doctype("html");
        assertTrue(xmlTreeBuilder.process(token));
    }
    
    @Test(expected = IllegalStateException.class)
    public void testProcessWithUnexpectedToken() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token.Text("This is an unexpected token");
        xmlTreeBuilder.process(token);
    }
    
    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document doc = new Document("");
        xmlTreeBuilder.stack.add(doc);
        Element parent = xmlTreeBuilder.currentElement();
        Element child = new Element("div");
        xmlTreeBuilder.insertNode(child);
        assertEquals(1, parent.children().size());
        assertTrue(parent.children().contains(child));
    }
    
    @Test
    public void testInsertWithStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("div");
        Element element = xmlTreeBuilder.insert(startTag);
        assertEquals("div", element.tagName());
        assertEquals(xmlTreeBuilder.baseUri, element.baseUri());
        assertEquals(ParseSettings.preserveCase.normalizeAttributes(startTag.attributes), element.attributes());
    }
    
    @Test
    public void testInsertWithCommentToken() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof Comment);
    }
    
    @Test
    public void testInsertWithCharacterToken() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("This is a character");
        xmlTreeBuilder.insert(characterToken);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof TextNode);
    }
    
    @Test
    public void testInsertWithDoctypeToken() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype("html");
        xmlTreeBuilder.insert(doctypeToken);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodeSize());
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof DocumentType);
    }
    
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element div1 = new Element("div");
        Element div2 = new Element("div");
        Element div3 = new Element("div");
        // Push elements onto the stack
        xmlTreeBuilder.stack.add(div1);
        xmlTreeBuilder.stack.add(div2);
        xmlTreeBuilder.stack.add(div3);
        // Pop the stack to close the first occurrence of "div"
        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.popStackToClose(endTag);
        // Check if the first occurrence of "div" is removed from the stack
        assertEquals(2, xmlTreeBuilder.stack.size());
        assertFalse(xmlTreeBuilder.stack.contains(div1));
    }
    
    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<div>This is a div</div>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, "http://www.example.com", ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
    }
}