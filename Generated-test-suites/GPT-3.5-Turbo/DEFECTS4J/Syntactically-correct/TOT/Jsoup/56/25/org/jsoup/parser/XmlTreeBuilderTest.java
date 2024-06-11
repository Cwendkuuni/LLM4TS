
import org.jsoup.Jsoup;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    // Testing defaultSettings method

    @Test
    public void testDefaultSettings_shouldReturnPreserveCase() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.preserveCase, settings);
    }

    // Testing parse method

    @Test
    public void testParse_shouldReturnDocumentObject() {
        String input = "<root><child></child></root>";
        String baseUri = "";
        Document document = xmlTreeBuilder.parse(input, baseUri);

        assertNotNull(document);
        assertEquals("root", document.child(0).nodeName());
        assertEquals("child", document.child(0).child(0).nodeName());
    }

    // Testing initialiseParse method

    @Test
    public void testInitialiseParse_shouldSetOutputSyntaxToXml() {
        String input = "<root><child></child></root>";
        String baseUri = "";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);

        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.getDocument().outputSettings().syntax());
    }

    // Testing process method

    @Test
    public void testProcess_withStartTagToken_shouldInsertNewElement() {
        Token token = new Token.StartTag("div");
        xmlTreeBuilder.process(token);

        assertNotNull(xmlTreeBuilder.currentElement());
        assertEquals("div", xmlTreeBuilder.currentElement().nodeName());
    }

    @Test
    public void testProcess_withEndTagToken_shouldPopStackToClose() {
        Token startTagToken = new Token.StartTag("div");
        Token endTagToken = new Token.EndTag("div");
        xmlTreeBuilder.process(startTagToken);
        xmlTreeBuilder.process(endTagToken);

        assertNull(xmlTreeBuilder.currentElement());
    }

    // Testing insertNode method

    @Test
    public void testInsertNode_withElement_shouldAppendToCurrentElement() {
        Element element = new Element("child");
        xmlTreeBuilder.insertNode(element);

        assertEquals(element, xmlTreeBuilder.currentElement().child(0));
    }

    // Testing insert method

    @Test
    public void testInsert_withCommentToken_shouldInsertCommentNode() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);

        assertEquals("This is a comment", xmlTreeBuilder.currentElement().childNode(0).outerHtml());
    }

    // Testing insert method with Character token

    @Test
    public void testInsert_withCharacterToken_shouldInsertTextNode() {
        Token.Character characterToken = new Token.Character("Text content");
        xmlTreeBuilder.insert(characterToken);

        assertEquals("Text content", xmlTreeBuilder.currentElement().childNode(0).outerHtml());
    }

    // Testing insert method with Doctype token

    @Test
    public void testInsert_withDoctypeToken_shouldInsertDocumentTypeNode() {
        Token.Doctype doctypeToken = new Token.Doctype("html");
        xmlTreeBuilder.insert(doctypeToken);

        assertEquals("!DOCTYPE html", xmlTreeBuilder.currentElement().childNode(0).outerHtml());
    }

    // Testing insert method with StartTag token

    @Test
    public void testInsert_withStartTagToken_shouldInsertNewElement() {
        Token.StartTag startTagToken = new Token.StartTag("div");
        xmlTreeBuilder.insert(startTagToken);

        assertNotNull(xmlTreeBuilder.currentElement());
        assertEquals("div", xmlTreeBuilder.currentElement().nodeName());
    }

    // Testing insert method with Comment token

    @Test
    public void testInsert_withCommentToken_shouldInsertCommentNode() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);

        assertEquals("This is a comment", xmlTreeBuilder.currentElement().childNode(0).outerHtml());
    }

    // Testing insert method with Character token

    @Test
    public void testInsert_withCharacterToken_shouldInsertTextNode() {
        Token.Character characterToken = new Token.Character("Text content");
        xmlTreeBuilder.insert(characterToken);

        assertEquals("Text content", xmlTreeBuilder.currentElement().childNode(0).outerHtml());
    }

    // Testing insert method with Doctype token

    @Test
    public void testInsert_withDoctypeToken_shouldInsertDocumentTypeNode() {
        Token.Doctype doctypeToken = new Token.Doctype("html");
        xmlTreeBuilder.insert(doctypeToken);

        assertEquals("!DOCTYPE html", xmlTreeBuilder.currentElement().childNode(0).outerHtml());
    }

    // Testing popStackToClose method

    @Test
    public void testPopStackToClose_withMatchingEndTag_shouldRemoveElementsFromStack() {
        Token.StartTag startTagToken = new Token.StartTag("div");
        Token.EndTag endTagToken = new Token.EndTag("div");
        xmlTreeBuilder.process(startTagToken);
        xmlTreeBuilder.popStackToClose(endTagToken);

        assertNull(xmlTreeBuilder.currentElement());
    }

    @Test
    public void testPopStackToClose_withNonMatchingEndTag_shouldNotRemoveElementsFromStack() {
        Token.StartTag startTagToken = new Token.StartTag("div");
        Token.EndTag endTagToken = new Token.EndTag("span");
        xmlTreeBuilder.process(startTagToken);
        xmlTreeBuilder.popStackToClose(endTagToken);

        assertNotNull(xmlTreeBuilder.currentElement());
        assertEquals("div", xmlTreeBuilder.currentElement().nodeName());
    }

    // Testing parseFragment method

    @Test
    public void testParseFragment_shouldReturnListOfNodes() {
        String inputFragment = "<child></child>";
        String baseUri = "";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();

        xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);

        assertNotNull(xmlTreeBuilder.getDocument().childNodes());
        assertEquals("child", xmlTreeBuilder.getDocument().child(0).nodeName());
    }
}

