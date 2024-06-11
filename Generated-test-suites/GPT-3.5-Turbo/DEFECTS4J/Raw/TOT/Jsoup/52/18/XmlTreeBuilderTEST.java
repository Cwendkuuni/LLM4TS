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

        assertEquals(input, xmlTreeBuilder.getReader().getInput());
        assertEquals(baseUri, xmlTreeBuilder.getBaseUri());
        assertEquals(errors, xmlTreeBuilder.getErrors());

        assertTrue(xmlTreeBuilder.getStack().contains(xmlTreeBuilder.getDoc()));
        assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.getDoc().outputSettings().syntax());
    }

    @Test
    public void testProcess_WithStartTagToken_AddsElementToStack() {
        Token token = new Token.StartTag("div");

        xmlTreeBuilder.process(token);

        assertEquals(1, xmlTreeBuilder.getStack().size());
        assertTrue(xmlTreeBuilder.getStack().contains(xmlTreeBuilder.getDoc()));
        assertTrue(xmlTreeBuilder.getCurrentElement().nodeName().equals("div"));
    }

    @Test
    public void testProcess_WithEndTagToken_ClosesMatchingElementAndRemovesFromStack() {
        Element divElemInStack = new Element(Tag.valueOf("div"), "http://example.com");
        xmlTreeBuilder.getStack().add(divElemInStack);
        Token token = new Token.EndTag("div");

        xmlTreeBuilder.process(token);

        assertFalse(xmlTreeBuilder.getStack().contains(divElemInStack));
    }

    @Test
    public void testProcess_WithCharacterToken_InsertsTextNode() {
        String textData = "This is a text node.";
        Token token = new Token.Character(textData);

        xmlTreeBuilder.process(token);

        Node insertedNode = xmlTreeBuilder.getCurrentElement().childNode(0);
        assertTrue(insertedNode instanceof TextNode);
        assertEquals(textData, ((TextNode) insertedNode).getWholeText());
    }

    @Test
    public void testProcess_WithCommentToken_InsertsCommentNode() {
        String commentData = "This is a comment.";
        Token token = new Token.Comment(commentData);

        xmlTreeBuilder.process(token);

        Node insertedNode = xmlTreeBuilder.getCurrentElement().childNode(0);
        assertTrue(insertedNode instanceof Comment);
        assertEquals(commentData, ((Comment) insertedNode).getComment());
    }

    @Test
    public void testProcess_WithDoctypeToken_InsertsDoctypeNode() {
        String doctypeName = "html";
        String publicIdentifier = "-//W3C//DTD HTML 4.01 Transitional//EN";
        String systemIdentifier = "http://www.w3.org/TR/html4/loose.dtd";
        Token token = new Token.Doctype(doctypeName, publicIdentifier, systemIdentifier);

        xmlTreeBuilder.process(token);

        Node insertedNode = xmlTreeBuilder.getCurrentElement().childNode(0);
        assertTrue(insertedNode instanceof DocumentType);
        assertEquals(doctypeName, ((DocumentType) insertedNode).getName());
        assertEquals(publicIdentifier, ((DocumentType) insertedNode).getPublicIdentifier());
        assertEquals(systemIdentifier, ((DocumentType) insertedNode).getSystemIdentifier());
    }

    @Test
    public void testInsert_WithStartTagToken_InsertsElementAndPushesToStack() {
        Token.StartTag startTag = new Token.StartTag("div");

        Element insertedElement = xmlTreeBuilder.insert(startTag);

        assertTrue(xmlTreeBuilder.getStack().contains(insertedElement));
        assertEquals(insertedElement, xmlTreeBuilder.getCurrentElement());
    }

    @Test
    public void testInsert_WithStartTagToken_SetsSelfClosingFlagForUnknownTags() {
        Token.StartTag startTag = new Token.StartTag("unknownTag");

        Element insertedElement = xmlTreeBuilder.insert(startTag);

        assertTrue(insertedElement.tag().isSelfClosing());
    }

    @Test
    public void testInsert_WithStartTagToken_DoesNotSetSelfClosingFlagForKnownTags() {
        Token.StartTag startTag = new Token.StartTag("div");

        Element insertedElement = xmlTreeBuilder.insert(startTag);

        assertFalse(insertedElement.tag().isSelfClosing());
    }

    @Test
    public void testInsert_WithStartTagTokenAndSelfClosingFlag_AcknowledgesSelfClosingFlag() {
        Token.StartTag selfClosingStartTag = new Token.StartTag("div", true);

        xmlTreeBuilder.insert(selfClosingStartTag);

        assertTrue(xmlTreeBuilder.getTokeniser().selfClosingFlagAcknowledged());
    }

    @Test
    public void testInsert_WithCommentTokenAndBogusFlag_InsertsXmlDeclarationNodeInsteadOfComment() {
        String commentData = "<!DOCTYPE html>";
        Token.Comment commentToken = new Token.Comment(commentData);

        xmlTreeBuilder.insert(commentToken);

        Node insertedNode = xmlTreeBuilder.getCurrentElement().childNode(0);
        assertTrue(insertedNode instanceof XmlDeclaration);
        assertEquals(commentData.substring(1), ((XmlDeclaration) insertedNode).getDeclaration());
    }

    @Test
    public void testInsert_WithCharacterToken_InsertsTextNode() {
        String characterData = "This is a character.";
        Token.Character characterToken = new Token.Character(characterData);

        xmlTreeBuilder.insert(characterToken);

        Node insertedNode = xmlTreeBuilder.getCurrentElement().childNode(0);
        assertTrue(insertedNode instanceof TextNode);
        assertEquals(characterData, ((TextNode) insertedNode).getWholeText());
    }

    @Test
    public void testInsert_WithDoctypeToken_InsertsDoctypeNode() {
        String doctypeName = "html";
        String publicIdentifier = "-//W3C//DTD HTML 4.01 Transitional//EN";
        String systemIdentifier = "http://www.w3.org/TR/html4/loose.dtd";
        Token.Doctype doctypeToken = new Token.Doctype(doctypeName, publicIdentifier, systemIdentifier);

        xmlTreeBuilder.insert(doctypeToken);

        Node insertedNode = xmlTreeBuilder.getCurrentElement().childNode(0);
        assertTrue(insertedNode instanceof DocumentType);
        assertEquals(doctypeName, ((DocumentType) insertedNode).getName());
        assertEquals(publicIdentifier, ((DocumentType) insertedNode).getPublicIdentifier());
        assertEquals(systemIdentifier, ((DocumentType) insertedNode).getSystemIdentifier());
    }

    @Test
    public void testPopStackToClose_WithEndTagToken_RemovesMatchingElementAndElementsAboveFromStack() {
        Element divElem1 = new Element(Tag.valueOf("div"), "http://example.com");
        Element divElem2 = new Element(Tag.valueOf("div"), "http://example.com");

        xmlTreeBuilder.getStack().add(divElem1);
        xmlTreeBuilder.getStack().add(divElem2);

        Token.EndTag endTag = new Token.EndTag("div");

        xmlTreeBuilder.popStackToClose(endTag);

        assertFalse(xmlTreeBuilder.getStack().contains(divElem1));
        assertFalse(xmlTreeBuilder.getStack().contains(divElem2));
    }

    @Test
    public void testPopStackToClose_WithEndTagToken_RemovesOnlyMatchingElementFromStack() {
        Element divElem1 = new Element(Tag.valueOf("div"), "http://example.com");
        Element divElem2 = new Element(Tag.valueOf("span"), "http://example.com");

        xmlTreeBuilder.getStack().add(divElem1);
        xmlTreeBuilder.getStack().add(divElem2);

        Token.EndTag endTag = new Token.EndTag("div");

        xmlTreeBuilder.popStackToClose(endTag);

        assertTrue(xmlTreeBuilder.getStack().contains(divElem2));
    }

    @Test
    public void testParseFragment_WithValidInputAndNoErrors_ReturnsParsedNodes() {
        String inputFragment = "<div>This is a div.</div>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        List<Node> parsedNodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);

        assertEquals(1, parsedNodes.size());
        assertTrue(parsedNodes.get(0) instanceof Element);
        assertEquals("div", ((Element) parsedNodes.get(0)).nodeName());
    }

    @Test
    public void testParseFragment_WithInvalidInput_AddsParseErrorToErrorList() {
        String inputFragment = "<div>This is a div.";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        List<Node> parsedNodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);

        assertEquals(0, parsedNodes.size());
        assertFalse(errors.isEmpty());
    }
}