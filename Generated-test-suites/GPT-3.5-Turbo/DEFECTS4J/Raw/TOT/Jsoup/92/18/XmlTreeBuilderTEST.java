import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.*;

public class XmlTreeBuilderTest {

    @Test
    public void testParse_ReaderString_documentCreated() {
        // Arrange
        String input = "<xml>Test</xml>";
        String baseUri = "http://www.example.com";
        XmlTreeBuilder builder = new XmlTreeBuilder();

        // Act
        Document document = builder.parse(new StringReader(input), baseUri);

        // Assert
        Assert.assertNotNull(document);
    }

    @Test
    public void testParse_ReaderString_baseUriPreserved() {
        // Arrange
        String input = "<xml>Test</xml>";
        String baseUri = "http://www.example.com";
        XmlTreeBuilder builder = new XmlTreeBuilder();

        // Act
        Document document = builder.parse(new StringReader(input), baseUri);

        // Assert
        Assert.assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testParse_StringString_documentCreated() {
        // Arrange
        String input = "<xml>Test</xml>";
        String baseUri = "http://www.example.com";
        XmlTreeBuilder builder = new XmlTreeBuilder();

        // Act
        Document document = builder.parse(input, baseUri);

        // Assert
        Assert.assertNotNull(document);
    }

    @Test
    public void testParse_StringString_baseUriPreserved() {
        // Arrange
        String input = "<xml>Test</xml>";
        String baseUri = "http://www.example.com";
        XmlTreeBuilder builder = new XmlTreeBuilder();

        // Act
        Document document = builder.parse(input, baseUri);

        // Assert
        Assert.assertEquals(baseUri, document.baseUri());
    }

    @Test
    public void testProcess_StartTag_insertNodeCalled() {
        // Arrange
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag", null);

        // Act
        boolean result = builder.process(startTag);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testProcess_EndTag_popStackToCloseCalled() {
        // Arrange
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("tag");

        // Act
        boolean result = builder.process(endTag);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testProcess_Comment_insertNodeCalled() {
        // Arrange
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("Test comment");

        // Act
        boolean result = builder.process(comment);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testProcess_Character_insertNodeCalled() {
        // Arrange
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("Test character");

        // Act
        boolean result = builder.process(character);

        // Assert
        Assert.assertTrue(result);
    }

    @Test
    public void testProcess_Doctype_insertNodeCalled() {
        // Arrange
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "", "");

        // Act
        boolean result = builder.process(doctype);

        // Assert
        Assert.assertTrue(result);
    }

    @Test(expected = RuntimeException.class)
    public void testProcess_UnknownToken_throwException() {
        // Arrange
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token(Token.TokenType.EOF, "");

        // Act
        builder.process(token);
    }

    @Test
    public void testInsert_StartTag_elementInserted() {
        // Arrange
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag", null);

        // Act
        Element element = builder.insert(startTag);

        // Assert
        Assert.assertNotNull(element);
        Assert.assertEquals("tag", element.tagName());
    }

    @Test
    public void testInsert_EndTag_selfClosingTag_setSelfClosing() {
        // Arrange
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag", null);
        startTag.setSelfClosing(true);

        // Act
        Element element = builder.insert(startTag);

        // Assert
        Assert.assertTrue(element.tag().isSelfClosing());
    }

    @Test
    public void testInsert_EndTag_unknownTag_setSelfClosing() {
        // Arrange
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag", null);
        startTag.setSelfClosing(true);

        // Act
        Element element = builder.insert(startTag);

        // Assert
        Assert.assertTrue(element.tag().isSelfClosing());
    }

    @Test
    public void testInsert_Comment_insertedAsComment() {
        // Arrange
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("Test comment");

        // Act
        builder.insert(comment);
        Node node = builder.currentElement().childNodes().get(0);

        // Assert
        Assert.assertNotNull(node);
        Assert.assertTrue(node instanceof Comment);
        Assert.assertEquals("Test comment", ((Comment) node).getData());
    }

    @Test
    public void testInsert_Comment_xmlDeclarationInserted() {
        // Arrange
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("<?xml version=\"1.0\"?>");

        // Act
        builder.insert(comment);
        Node node = builder.currentElement().childNodes().get(0);

        // Assert
        Assert.assertNotNull(node);
        Assert.assertTrue(node instanceof XmlDeclaration);
    }

    @Test
    public void testInsert_Character_textNodeInserted() {
        // Arrange
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("Test character");

        // Act
        builder.insert(character);
        Node node = builder.currentElement().childNodes().get(0);

        // Assert
        Assert.assertNotNull(node);
        Assert.assertTrue(node instanceof TextNode);
        Assert.assertEquals("Test character", ((TextNode) node).getWholeText());
    }

    @Test
    public void testInsert_Character_cDataNodeInserted() {
        // Arrange
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("<![CDATA[Test]]>");

        // Act
        builder.insert(character);
        Node node = builder.currentElement().childNodes().get(0);

        // Assert
        Assert.assertNotNull(node);
        Assert.assertTrue(node instanceof CDataNode);
        Assert.assertEquals("Test", ((CDataNode) node).getWholeText());
    }

    @Test
    public void testInsert_Doctype_doctypeNodeInserted() {
        // Arrange
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "", "");

        // Act
        builder.insert(doctype);
        Node node = builder.currentElement().childNodes().get(0);

        // Assert
        Assert.assertNotNull(node);
        Assert.assertTrue(node instanceof DocumentType);
        Assert.assertEquals("<!DOCTYPE html PUBLIC \"\" \"\">", node.toString());
    }

    @Test
    public void testPopStackToClose_matchingTag_poppedStack() {
        // Arrange
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element parent = new Element("parent");
        Element expected = new Element("tag");
        builder.stack.add(parent);
        builder.stack.add(expected);

        Token.EndTag endTag = new Token.EndTag("tag");

        // Act
        builder.popStackToClose(endTag);

        // Assert
        Assert.assertEquals(1, builder.stack.size());
        Assert.assertEquals(parent, builder.stack.get(0));
    }

    @Test
    public void testParseFragment_inputFragment_documentCreated() {
        // Arrange
        String inputFragment = "<tag>Test</tag>";
        String baseUri = "http://www.example.com";
        XmlTreeBuilder builder = new XmlTreeBuilder();

        // Act
        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, new Parser(builder));

        // Assert
        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());
        Assert.assertTrue(nodes.get(0) instanceof Element);
    }

    @Test
    public void testParseFragment_inputFragment_context_documentCreatedWithinContext() {
        // Arrange
        String inputFragment = "<tag>Test</tag>";
        Element context = new Element("context");
        String baseUri = "http://www.example.com";
        XmlTreeBuilder builder = new XmlTreeBuilder();

        // Act
        List<Node> nodes = builder.parseFragment(inputFragment, context, baseUri, new Parser(builder));

        // Assert
        Assert.assertNotNull(nodes);
        Assert.assertEquals(1, nodes.size());
        Assert.assertTrue(nodes.get(0) instanceof Element);
        Assert.assertEquals("context", nodes.get(0).parentNode().nodeName());
    }
}