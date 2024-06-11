package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token.StartTag;
import org.visit.mockito.MockitoAnnotations;
import org.visit.mockito.Mock;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Mock
    private TreeBuilder treeBuilder;
    @Mock
    private ParseErrorList errors;
    private XmlTreeBuilder xmlTreeBuilder;
    private String inputFragment;
    private String baseUri;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        xmlTreeBuilder = new XmlTreeBuilder();
        inputFragment = "<test>content</test>";
        baseUri = "http://example.com";
    }

    @Test
    public void testInitialiseParse() {
        xmlTreeBuilder.initialiseParse(inputFragment, baseUri, errors);

        assertEquals(xmlTreeBuilder.stack.size(), 1);
        assertEquals(xmlTreeBuilder.stack.get(0), xmlTreeBuilder.doc);
        assertEquals(xmlTreeBuilder.doc.outputSettings().syntax(), Document.OutputSettings.Syntax.xml);
    }

    @Test
    public void testProcess() {
        Token token = new Token();
        token.type = Token.TokenType.StartTag;
        StartTag startTag = new StartTag();
        startTag.tagName = "test";
        token.asStartTag = startTag;

        assertTrue(xmlTreeBuilder.process(token));
        assertEquals(xmlTreeBuilder.stack.size(), 2);
    }

    @Test
    public void testProcess_EndTag() {
        Token token = new Token();
        token.type = Token.TokenType.EndTag;
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "test";
        token.asEndTag = endTag;

        xmlTreeBuilder.process(token);
        assertEquals(xmlTreeBuilder.stack.size(), 1);
    }

    @Test
    public void testProcess_Comment() {
        Token token = new Token();
        token.type = Token.TokenType.Comment;
        Token.Comment commentToken = new Token.Comment();
        commentToken.data = "comment";
        token.asComment = commentToken;

        xmlTreeBuilder.process(token);
        assertEquals(xmlTreeBuilder.stack.size(), 1);
        Comment comment = (Comment) xmlTreeBuilder.doc.childNodes().get(0);
        assertEquals(comment.getData(), commentToken.data);
        assertEquals(comment.baseUri(), xmlTreeBuilder.baseUri);
    }

    @Test
    public void testProcess_Character() {
        Token token = new Token();
        token.type = Token.TokenType.Character;
        Token.Character characterToken = new Token.Character();
        characterToken.data = "character";
        token.asCharacter = characterToken;

        xmlTreeBuilder.process(token);
        assertEquals(xmlTreeBuilder.stack.size(), 1);
        TextNode textNode = (TextNode) xmlTreeBuilder.doc.childNodes().get(0);
        assertEquals(textNode.getWholeText(), characterToken.data);
        assertEquals(textNode.baseUri(), xmlTreeBuilder.baseUri);
    }

    @Test
    public void testProcess_Doctype() {
        Token token = new Token();
        token.type = Token.TokenType.Doctype;
        Token.Doctype doctypeToken = new Token.Doctype();
        doctypeToken.name = "doctype";
        doctypeToken.publicIdentifier = "public";
        doctypeToken.systemIdentifier = "system";
        token.asDoctype = doctypeToken;

        xmlTreeBuilder.process(token);
        assertEquals(xmlTreeBuilder.stack.size(), 1);
        DocumentType doctype = (DocumentType) xmlTreeBuilder.doc.childNodes().get(0);
        assertEquals(doctype.getName(), doctypeToken.name);
        assertEquals(doctype.getPublicIdentifier(), doctypeToken.publicIdentifier);
        assertEquals(doctype.getSystemIdentifier(), doctypeToken.systemIdentifier);
        assertEquals(doctype.baseUri(), xmlTreeBuilder.baseUri);
    }

    @Test
    public void testInsertNode() {
        Node node = new Element("test", xmlTreeBuilder.baseUri);
        xmlTreeBuilder.insertNode(node);

        assertEquals(xmlTreeBuilder.currentElement().childNodes().size(), 1);
        assertEquals(xmlTreeBuilder.currentElement().childNodes().get(0), node);
    }

    @Test
    public void testInsert() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name = "test";
        Tag tag = Tag.valueOf(startTag.name);
        startTag.attributes = new Attributes();
        Element el = new Element(tag, xmlTreeBuilder.baseUri, startTag.attributes);

        Element insertedElement = xmlTreeBuilder.insert(startTag);

        assertEquals(xmlTreeBuilder.stack.size(), 2);
        assertEquals(xmlTreeBuilder.tokeniser.selfClosingFlagAcknowledged, insertedElement.isSelfClosing());
        assertEquals(xmlTreeBuilder.tokeniser.selfClosingFlagAcknowledged, insertedElement.tag().isSelfClosing());
        if (!tag.isKnownTag())
            assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testInsert_Comment() {
        Token.Comment commentToken = new Token.Comment();
        commentToken.data = "comment";
        xmlTreeBuilder.insert(commentToken);

        assertEquals(xmlTreeBuilder.stack.size(), 1);
        Comment comment = (Comment) xmlTreeBuilder.doc.childNodes().get(0);
        assertEquals(comment.getData(), commentToken.data);
        assertEquals(comment.baseUri(), xmlTreeBuilder.baseUri);
    }

    @Test
    public void testInsert_Comment_Bogus() {
        Token.Comment commentToken = new Token.Comment();
        commentToken.data = "<!DOCTYPE html>";
        xmlTreeBuilder.insert(commentToken);

        assertEquals(xmlTreeBuilder.stack.size(), 1);
        XmlDeclaration declaration = (XmlDeclaration) xmlTreeBuilder.doc.childNodes().get(0);
        assertEquals(declaration.getDeclaration(), commentToken.data.substring(1));
        assertEquals(declaration.baseUri(), xmlTreeBuilder.baseUri);
    }

    @Test
    public void testInsert_Character() {
        Token.Character characterToken = new Token.Character();
        characterToken.data = "character";
        xmlTreeBuilder.insert(characterToken);

        assertEquals(xmlTreeBuilder.stack.size(), 1);
        TextNode textNode = (TextNode) xmlTreeBuilder.doc.childNodes().get(0);
        assertEquals(textNode.getWholeText(), characterToken.data);
        assertEquals(textNode.baseUri(), xmlTreeBuilder.baseUri);
    }

    @Test
    public void testInsert_Doctype() {
        Token.Doctype doctypeToken = new Token.Doctype();
        doctypeToken.name = "doctype";
        doctypeToken.publicIdentifier = "public";
        doctypeToken.systemIdentifier = "system";
        xmlTreeBuilder.insert(doctypeToken);

        assertEquals(xmlTreeBuilder.stack.size(), 1);
        DocumentType doctype = (DocumentType) xmlTreeBuilder.doc.childNodes().get(0);
        assertEquals(doctype.getName(), doctypeToken.name);
        assertEquals(doctype.getPublicIdentifier(), doctypeToken.publicIdentifier);
        assertEquals(doctype.getSystemIdentifier(), doctypeToken.systemIdentifier);
        assertEquals(doctype.baseUri(), xmlTreeBuilder.baseUri);
    }

    @Test
    public void testPopStackToClose() {
        HTMLElement hElement = new HTMLElement("h", "h", true);
        xmlTreeBuilder.stack.add(hElement);
        xmlTreeBuilder.stack.add(hElement);

        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "h";
        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(xmlTreeBuilder.stack.size(), 1);
    }

    @Test
    public void testParseFragment() {
        List<Node> node = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);

        assertEquals(node.size(), 1);
        assertTrue(node.get(0) instanceof Element);
        assertEquals(((Element) node.get(0)).tagName(), "test");
    }

}