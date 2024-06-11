import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.helper.StringUtil;
import org.jsoup.parser.Parser;
import java.util.List;
import java.io.Reader;
import java.io.StringReader;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings defaultSettings = xmlTreeBuilder.defaultSettings();

        assertNotNull(defaultSettings);
        assertEquals(ParseSettings.preserveCase, defaultSettings);
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Reader input = new StringReader("<example></example>");
        String baseUri = "https://www.example.com";
        Parser parser = new Parser(xmlTreeBuilder);

        xmlTreeBuilder.initialiseParse(input, baseUri, parser);

        assertEquals(xmlTreeBuilder.doc, xmlTreeBuilder.stack.get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testParseReader() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Reader input = new StringReader("<example></example>");
        String baseUri = "https://www.example.com";

        Document document = xmlTreeBuilder.parse(input, baseUri);

        assertNotNull(document);
        assertEquals("example", document.child(0).nodeName());
    }

    @Test
    public void testParseString() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<example></example>";
        String baseUri = "https://www.example.com";

        Document document = xmlTreeBuilder.parse(input, baseUri);

        assertNotNull(document);
        assertEquals("example", document.child(0).nodeName());
    }

    @Test
    public void testProcess() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token startTagToken = new Token(Token.TokenType.StartTag, "example");
        xmlTreeBuilder.process(startTagToken);

        assertNotNull(xmlTreeBuilder.stack.get(0));
        assertEquals("example", xmlTreeBuilder.stack.get(0).nodeName());

        Token endTagToken = new Token(Token.TokenType.EndTag, "example");
        xmlTreeBuilder.process(endTagToken);

        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("example"), "https://www.example.com");
        xmlTreeBuilder.insertNode(element);

        assertEquals(element, xmlTreeBuilder.currentElement().child(0));
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("example");
        Element element = xmlTreeBuilder.insert(startTag);

        assertNotNull(element);
        assertEquals(Tag.valueOf("example"), element.tag());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("example comment");
        xmlTreeBuilder.insert(commentToken);

        assertEquals("example comment", xmlTreeBuilder.currentElement().childNode(0).toString());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("example character");
        xmlTreeBuilder.insert(characterToken);

        assertEquals("example character", xmlTreeBuilder.currentElement().childNode(0).toString());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype("example-doctype-name");
        xmlTreeBuilder.insert(doctypeToken);

        assertEquals("example-doctype-name", xmlTreeBuilder.currentElement().childNode(0).nodeName());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag1 = new Token.StartTag("example");
        Token.StartTag startTag2 = new Token.StartTag("example");
        xmlTreeBuilder.insert(startTag1);
        xmlTreeBuilder.insert(startTag2);

        Token.EndTag endTag = new Token.EndTag("example");
        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(1, xmlTreeBuilder.stack.size());
        assertEquals("example", xmlTreeBuilder.stack.get(0).nodeName());
    }

    @Test
    public void testParseFragmentString() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<example></example>";
        String baseUri = "https://www.example.com";
        Parser parser = new Parser(xmlTreeBuilder);

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, parser);

        assertNotNull(nodes);
        assertEquals("example", nodes.get(0).nodeName());
    }

    @Test
    public void testParseFragmentWithContext() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<example></example>";
        Element context = new Element(Tag.valueOf("div"), "https://www.example.com");
        String baseUri = "https://www.example.com";
        Parser parser = new Parser(xmlTreeBuilder);

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, context, baseUri, parser);

        assertNotNull(nodes);
        assertEquals("example", nodes.get(0).nodeName());
    }

}