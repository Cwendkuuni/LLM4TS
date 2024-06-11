import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {

    @org.junit.Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<xml>Test Input</xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        builder.initialiseParse(input, baseUri, errors);

        assertEquals(builder.stack.peek(), builder.doc);
        assertEquals(builder.baseUri, baseUri);
    }

    @org.junit.Test
    public void testProcess_StartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag token = new Token.StartTag("testTag");

        boolean result = builder.process(token);

        assertTrue(result);
    }

    @org.junit.Test
    public void testProcess_EndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag token = new Token.EndTag("testTag");

        boolean result = builder.process(token);

        assertTrue(result);
    }

    @org.junit.Test
    public void testProcess_Comment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment token = new Token.Comment("Test Comment");

        boolean result = builder.process(token);

        assertTrue(result);
    }

    @org.junit.Test
    public void testProcess_Character() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character token = new Token.Character("Test Character");

        boolean result = builder.process(token);

        assertTrue(result);
    }

    @org.junit.Test
    public void testProcess_Doctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype token = new Token.Doctype();

        boolean result = builder.process(token);

        assertTrue(result);
    }

    @org.junit.Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element el = new Element(Tag.valueOf("testTag"), "http://example.com", new Attributes());

        builder.insertNode(el);

        assertEquals(builder.currentElement().childNodes().get(0), el);
    }

    @org.junit.Test
    public void testInsert_StartTag_SelfClosing() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("selfClosingTag");

        Element element = builder.insert(startTag);

        assertTrue(element.isSelfClosing());
        assertTrue(element.tag().isKnownTag());
        assertTrue(builder.tokeniser.selfClosingFlagAcknowledged());
        assertEquals(builder.stack.peek(), element);
    }

    @org.junit.Test
    public void testInsert_StartTag_NotSelfClosing() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("normalTag");

        Element element = builder.insert(startTag);

        assertFalse(element.isSelfClosing());
        assertTrue(builder.stack.contains(element));
    }

    @org.junit.Test
    public void testInsert_Comment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("Test Comment");

        builder.insert(commentToken);

        assertTrue(builder.currentElement().childNodes().get(0) instanceof Comment);
    }

    @org.junit.Test
    public void testInsert_Character() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("Test Character");

        builder.insert(characterToken);

        assertTrue(builder.currentElement().childNodes().get(0) instanceof TextNode);
    }

    @org.junit.Test
    public void testInsert_Doctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype();

        builder.insert(doctypeToken);

        assertTrue(builder.currentElement().childNodes().get(0) instanceof DocumentType);
    }

    @org.junit.Test
    public void testPopStackToClose_ElementFound() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("testTag");
        Element element = new Element(Tag.valueOf("testTag"), "http://example.com", new Attributes());
        builder.stack.push(element);

        builder.popStackToClose(endTag);

        assertTrue(builder.stack.isEmpty());
    }
    
    @org.junit.Test
    public void testPopStackToClose_ElementNotFound() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("testTag");
        builder.stack.push(new Element(Tag.valueOf("anotherTag"), "http://example.com", new Attributes()));
        
        builder.popStackToClose(endTag);

        assertFalse(builder.stack.isEmpty());
    }
}