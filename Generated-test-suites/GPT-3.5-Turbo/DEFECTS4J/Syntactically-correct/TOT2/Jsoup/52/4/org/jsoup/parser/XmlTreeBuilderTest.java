import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.parser.ParseErrorList;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        xmlTreeBuilder.initialiseParse("input", "baseUri", new ParseErrorList());
        assertNotNull(xmlTreeBuilder.currentElement());
    }

    @Test
    public void testProcess() {
        assertTrue(xmlTreeBuilder.process(new Token(Token.TokenType.StartTag)));
        assertTrue(xmlTreeBuilder.process(new Token(Token.TokenType.EndTag)));
        assertTrue(xmlTreeBuilder.process(new Token(Token.TokenType.Comment)));
        assertTrue(xmlTreeBuilder.process(new Token(Token.TokenType.Character)));
        assertTrue(xmlTreeBuilder.process(new Token(Token.TokenType.Doctype)));
        assertFalse(xmlTreeBuilder.process(new Token(Token.TokenType.EOF)));
    }

    @Test
    public void testInsertNode() {
        xmlTreeBuilder.insertNode(new Element("sample"));
        assertEquals("sample", xmlTreeBuilder.currentElement().childNode(0).nodeName());
    }

    @Test
    public void testInsert() {
        Element element = xmlTreeBuilder.insert(new Token.StartTag("sample"));
        assertNotNull(element);
    }

    @Test
    public void testInsertComment() {
        xmlTreeBuilder.insert(new Token.Comment("sample"));
        assertEquals("sample", xmlTreeBuilder.currentElement().childNode(0).nodeName());
    }

    @Test
    public void testInsertCharacter() {
        xmlTreeBuilder.insert(new Token.Character("sample"));
        assertEquals("sample", xmlTreeBuilder.currentElement().childNode(0).nodeName());
    }

    @Test
    public void testInsertDoctype() {
        xmlTreeBuilder.insert(new Token.Doctype("sample", "public", "system"));
        assertEquals("sample", xmlTreeBuilder.currentElement().childNode(0).nodeName());
    }

    @Test
    public void testPopStackToClose() {
        // test case for element not found in stack
        xmlTreeBuilder.popStackToClose(new Token.EndTag("unknown"));
        // add element to stack for testing
        xmlTreeBuilder.stack.add(new Element("sample"));
        xmlTreeBuilder.popStackToClose(new Token.EndTag("sample"));
        assertTrue(xmlTreeBuilder.stack.isEmpty());
    }

    @Test
    public void testParseFragment() {
        List<Node> nodes = xmlTreeBuilder.parseFragment("input", "baseUri", new ParseErrorList());
        assertNotNull(nodes);
    }

}