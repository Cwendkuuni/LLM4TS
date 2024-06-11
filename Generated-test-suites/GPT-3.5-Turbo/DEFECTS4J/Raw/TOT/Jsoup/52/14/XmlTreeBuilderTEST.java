import org.jsoup.nodes.*;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.helper.Validate;

import java.util.List;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;
    private Document document;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        document = new Document("");
        xmlTreeBuilder.setDocument(document);
    }

    @Test
    public void initialiseParse_validInput_addsDocumentToStack() {
        String input = "<root></root>";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, "", errors);

        assertEquals(document, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void initialiseParse_validInput_setsXmlSyntax() {
        String input = "<root></root>";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, "", errors);

        assertEquals(Document.OutputSettings.Syntax.xml, document.outputSettings().syntax());
    }

    @Test
    public void process_startTag_insertsStartTag() {
        Token.StartTag startTag = new Token.StartTag("p");
        startTag.attributes.put("id", "paragraph");

        xmlTreeBuilder.process(startTag);

        assertEquals("p", document.body().child(0).tagName());
        assertEquals("paragraph", document.body().child(0).attr("id"));
    }

    @Test
    public void process_endTag_popsStackToClose() {
        Token.EndTag endTag = new Token.EndTag("p");
        Element elementP = new Element("p");
        document.body().appendChild(elementP);
        xmlTreeBuilder.stack.add(elementP);

        xmlTreeBuilder.process(endTag);

        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void process_comment_insertsComment() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.process(commentToken);

        assertEquals("<!--This is a comment-->", document.body().html());
    }

    @Test
    public void process_character_insertsCharacter() {
        Token.Character characterToken = new Token.Character("This is a character");
        xmlTreeBuilder.process(characterToken);

        assertEquals("This is a character", document.body().html());
    }

    @Test
    public void process_doctype_insertsDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "", "");
        xmlTreeBuilder.process(doctype);

        assertEquals("<!DOCTYPE html >", document.html());
    }

    @Test
    public void insertNode_appendsNodeToCurrentElement() {
        Element elementP = new Element("p");
        document.body().appendChild(elementP);
        TextNode textNode = new TextNode("This is a text node");

        xmlTreeBuilder.insertNode(textNode);

        assertEquals(textNode, document.body().child(0).childNode(0));
    }

    @Test
    public void insert_startTag_insertsElementToStack() {
        Token.StartTag startTag = new Token.StartTag("div");
        Element elementDiv = new Element("div");
        xmlTreeBuilder.stack.add(elementDiv);

        Element elementInserted = xmlTreeBuilder.insert(startTag);

        assertEquals("div", elementInserted.tagName());
        assertEquals(elementDiv, xmlTreeBuilder.stack.get(0));
    }

    @Test
    public void insert_startTag_selfClosingFlagIsTrue_acknowledgesSelfClosingFlag() {
        Token.StartTag startTag = new Token.StartTag("img");
        startTag.attributes.put("src", "image.jpg");
        startTag.selfClosing = true;

        xmlTreeBuilder.insert(startTag);

        assertTrue(xmlTreeBuilder.tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void insert_startTag_unknownTag_setsSelfClosingForUnknownTag() {
        Token.StartTag startTag = new Token.StartTag("custom");
        startTag.attributes.put("attribute", "value");

        xmlTreeBuilder.insert(startTag);

        assertTrue(startTag.isSelfClosing());
    }

    @Test
    public void insert_commentToken_bogusIsTrue_insertsComment() {
        Token.Comment commentToken = new Token.Comment("<!DOCTYPE html>");
        commentToken.bogus = true;

        xmlTreeBuilder.insert(commentToken);

        assertEquals("<!--<!DOCTYPE html>-->", document.body().html());
    }

    @Test
    public void insert_commentToken_bogusIsFalse_insertsComment() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        commentToken.bogus = false;

        xmlTreeBuilder.insert(commentToken);

        assertEquals("<!--This is a comment-->", document.body().html());
    }

    @Test
    public void insert_commentToken_startsWithExclamationMark_insertsXmlDeclaration() {
        Token.Comment commentToken = new Token.Comment("<!DOCTYPE html>");
        commentToken.bogus = true;

        xmlTreeBuilder.insert(commentToken);

        assertTrue(document.childNode(0) instanceof XmlDeclaration);
    }

    @Test
    public void insert_characterToken_insertsTextNode() {
        Token.Character characterToken = new Token.Character("This is a character");

        xmlTreeBuilder.insert(characterToken);

        assertTrue(document.childNode(0) instanceof TextNode);
        assertEquals("This is a character", document.body().html());
    }

    @Test
    public void insert_doctype_insertsDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "", "");
        xmlTreeBuilder.insert(doctype);

        assertTrue(document.childNode(0) instanceof DocumentType);
    }

    @Test
    public void popStackToClose_stackContainsElementWithTagName_popsStack() {
        Token.EndTag endTag = new Token.EndTag("p");
        Element elementP = new Element("p");
        xmlTreeBuilder.stack.add(elementP);

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void popStackToClose_stackDoesNotContainElementWithTagName_doesNotPopStack() {
        Token.EndTag endTag = new Token.EndTag("div");
        Element elementP = new Element("p");
        xmlTreeBuilder.stack.add(elementP);

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(1, xmlTreeBuilder.stack.size());
    }

    @Test
    public void popStackToClose_stackContainsMultipleElementsWithTagName_popsUntilFirstOccurrence() {
        Token.EndTag endTag = new Token.EndTag("p");
        Element elementDiv = new Element("div");
        Element elementP = new Element("p");
        xmlTreeBuilder.stack.add(elementDiv);
        xmlTreeBuilder.stack.add(elementP);
        xmlTreeBuilder.stack.add(elementDiv);
        xmlTreeBuilder.stack.add(elementDiv);
        xmlTreeBuilder.stack.add(elementP);

        xmlTreeBuilder.popStackToClose(endTag);

        assertEquals(3, xmlTreeBuilder.stack.size());
    }

    @Test
    public void parseFragment_validInput_parsesFragmentAndReturnsChildNodes() {
        String inputFragment = "<p>Paragraph</p><span>Span</span>";
        ParseErrorList errors = new ParseErrorList();

        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, "", errors);

        assertEquals(2, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertTrue(nodes.get(1) instanceof Element);
    }

}