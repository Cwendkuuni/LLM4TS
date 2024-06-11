import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, parseSettings);
    }

    @Test
    public void testParseWithReader() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<xml><tag></tag></xml>";
        String baseUri = "http://www.example.com";
        Reader reader = new StringReader(input);
        Document document = xmlTreeBuilder.parse(reader, baseUri);
        assertNotNull(document);
        assertEquals(input, document.toString());
    }

    @Test
    public void testParseWithString() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<xml><tag></tag></xml>";
        String baseUri = "http://www.example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(document);
        assertEquals(input, document.toString());
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<xml><tag></tag></xml>";
        String baseUri = "http://www.example.com";
        ParseErrorList parseErrorList = ParseErrorList.noTracking();
        ParseSettings parseSettings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(new StringReader(input), baseUri, parseErrorList, parseSettings);
        assertNotNull(xmlTreeBuilder.stack);
        assertNotNull(xmlTreeBuilder.doc);
    }

    @Test
    public void testProcess() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token.StartTag();
        boolean result = xmlTreeBuilder.process(token);
        assertTrue(result);
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("tag"), "http://www.example.com");
        xmlTreeBuilder.insertNode(element);
        assertEquals(element, xmlTreeBuilder.currentElement().childNodes().get(0));
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag();
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
        assertEquals(startTag.name(), element.tagName());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment();
        xmlTreeBuilder.insert(commentToken);
        Comment comment = (Comment) xmlTreeBuilder.currentElement().childNodes().get(0);
        assertNotNull(comment);
        assertEquals(commentToken.getData(), comment.getData());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character();
        xmlTreeBuilder.insert(characterToken);
        assertEquals(characterToken.getData(), xmlTreeBuilder.currentElement().childNodes().get(0).toString());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype();
        xmlTreeBuilder.insert(doctype);
        DocumentType documentType = (DocumentType) xmlTreeBuilder.currentElement().childNodes().get(0);
        assertNotNull(documentType);
        assertEquals(doctype.getName(), documentType.tagName());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag();
        xmlTreeBuilder.stack.add(xmlTreeBuilder.doc);
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("tag"), "http://www.example.com"));
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(1, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<tag></tag>";
        String baseUri = "http://www.example.com";
        ParseErrorList parseErrorList = ParseErrorList.noTracking();
        ParseSettings parseSettings = ParseSettings.preserveCase;
        List<Node> nodeList = xmlTreeBuilder.parseFragment(input, baseUri, parseErrorList, parseSettings);
        assertEquals(1, nodeList.size());
        assertEquals(Tag.valueOf("tag"), nodeList.get(0).nodeName());
    }
}