import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        // Typical use case
        String input = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.initialiseParse(input, baseUri, errors);

        // Verify that stack contains document
        Assert.assertEquals(1, treeBuilder.stack.size());
        Assert.assertEquals(treeBuilder.doc, treeBuilder.stack.get(0));

        // Verify outputSettings syntax is XML
        Assert.assertEquals(Document.OutputSettings.Syntax.xml, treeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcess() {
        // Typical use case: Start tag
        Token startTagToken = new Token();
        startTagToken.type = Token.TokenType.StartTag;
        startTagToken.name = "div";
        Token.StartTag startTag = startTagToken.asStartTag();

        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.stack.add(new Element("html"));
        treeBuilder.process(startTag);

        // Verify that the start tag is inserted and added to the stack
        Assert.assertTrue(treeBuilder.stack.size() > 1);
        Assert.assertEquals("div", treeBuilder.stack.get(treeBuilder.stack.size() - 1).tagName());

        // Edge case: End tag
        Token endTagToken = new Token();
        endTagToken.type = Token.TokenType.EndTag;
        endTagToken.name = "div";
        Token.EndTag endTag = endTagToken.asEndTag();

        treeBuilder.process(endTag);

        // Verify that the stack is popped and the end tag is removed
        Assert.assertEquals(1, treeBuilder.stack.size());

        // Error scenario: Unexpected token type
        Token unexpectedToken = new Token();
        unexpectedToken.type = Token.TokenType.Comment;

        try {
            treeBuilder.process(unexpectedToken);
        } catch (Exception e) {
            Assert.assertTrue(e instanceof IllegalArgumentException);
        }
    }

    @Test
    public void testInsertNode() {
        // Typical use case
        Element element = new Element("p");
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.stack.add(new Element("div"));
        treeBuilder.insertNode(element);

        // Verify that the element is appended as a child to the current element
        Assert.assertEquals(element, treeBuilder.currentElement().child(0));

        // Edge case: Current element is null
        try {
            treeBuilder.stack.clear();
            treeBuilder.insertNode(element);
            Assert.fail("Exception not thrown");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NullPointerException);
        }
    }

    @Test
    public void testInsertStartTag() {
        // Typical use case
        Token startTagToken = new Token();
        startTagToken.type = Token.TokenType.StartTag;
        startTagToken.name = "div";
        startTagToken.attributes = new Attributes();
        Token.StartTag startTag = startTagToken.asStartTag();

        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.baseUri = "http://example.com";
        treeBuilder.tokeniser = new Tokeniser();

        Element element = treeBuilder.insert(startTag);

        // Verify that the element is inserted and added to the stack
        Assert.assertTrue(treeBuilder.stack.size() > 1);
        Assert.assertEquals("div", element.tagName());

        // Verify that the self-closing flag is acknowledged and tag is set to self-closing if unknown
        startTagToken.selfClosing = true;
        startTagToken.name = "unknown";
        startTag.attributes.put("attr", "value");
        treeBuilder.insert(startTag);

        Assert.assertTrue(treeBuilder.tokeniser.selfClosingFlagAcknowledged());

        element = treeBuilder.stack.get(treeBuilder.stack.size() - 1);
        Assert.assertTrue(element.tag().isSelfClosing());

        // Edge case: Unknown tag
        element = treeBuilder.insert(startTag);

        Assert.assertTrue(element.tag().isUnknown());
        Assert.assertEquals("div", element.tagName());

        // Edge case: Tag is not self-closing
        treeBuilder.stack.clear();
        startTagToken.selfClosing = false;
        treeBuilder.insert(startTag);

        Assert.assertEquals(1, treeBuilder.stack.size());

        // Error scenario: Start tag is null
        try {
            treeBuilder.insert(null);
            Assert.fail("Exception not thrown");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NullPointerException);
        }
    }

    @Test
    public void testInsertComment() {
        // Typical use case
        Token commentToken = new Token();
        commentToken.type = Token.TokenType.Comment;
        commentToken.data = "Test Comment";
        commentToken.baseUri = "http://example.com";
        commentToken.bogus = false;
        Token.Comment comment = commentToken.asComment();

        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.baseUri = "http://example.com";

        treeBuilder.insert(comment);

        // Verify that the comment is inserted as a Comment node
        Assert.assertTrue(treeBuilder.currentElement().childNode(0) instanceof Comment);
        Assert.assertEquals("Test Comment", treeBuilder.currentElement().childNode(0).outerHtml());

        // Edge case: Bogus comment with XML declaration
        commentToken.data = "<?xml version='1.0' encoding='UTF-8'?>";
        commentToken.bogus = true;
        treeBuilder.insert(comment);

        // Verify that the comment is parsed as an XmlDeclaration
        Assert.assertTrue(treeBuilder.currentElement().childNode(1) instanceof XmlDeclaration);
        Assert.assertEquals("<?xml version='1.0' encoding='UTF-8'?>", treeBuilder.currentElement().childNode(1).outerHtml());

        // Edge case: Bogus comment without XML declaration
        commentToken.data = "This is a bogus comment";
        treeBuilder.insert(comment);

        // Verify that the comment is inserted as a Comment node
        Assert.assertTrue(treeBuilder.currentElement().childNode(2) instanceof Comment);
        Assert.assertEquals("This is a bogus comment", treeBuilder.currentElement().childNode(2).outerHtml());

        // Error scenario: Comment is null
        try {
            treeBuilder.insert((Token.Comment) null);
            Assert.fail("Exception not thrown");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NullPointerException);
        }
    }

    @Test
    public void testInsertCharacter() {
        // Typical use case
        Token characterToken = new Token();
        characterToken.type = Token.TokenType.Character;
        characterToken.data = "Test Text";
        characterToken.baseUri = "http://example.com";
        Token.Character character = characterToken.asCharacter();

        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.baseUri = "http://example.com";

        treeBuilder.insert(character);

        // Verify that the character is inserted as a TextNode
        Assert.assertTrue(treeBuilder.currentElement().childNode(0) instanceof TextNode);
        Assert.assertEquals("Test Text", treeBuilder.currentElement().childNode(0).outerHtml());

        // Error scenario: Character is null
        try {
            treeBuilder.insert((Token.Character) null);
            Assert.fail("Exception not thrown");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NullPointerException);
        }
    }

    @Test
    public void testInsertDoctype() {
        // Typical use case
        Token doctypeToken = new Token();
        doctypeToken.type = Token.TokenType.Doctype;
        doctypeToken.name = "html";
        doctypeToken.publicIdentifier = "-//W3C//DTD XHTML 1.0 Strict//EN";
        doctypeToken.systemIdentifier = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd";
        doctypeToken.baseUri = "http://example.com";
        Token.Doctype doctype = doctypeToken.asDoctype();

        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.baseUri = "http://example.com";

        treeBuilder.insert(doctype);

        // Verify that the doctype is inserted as a DocumentType node
        Assert.assertTrue(treeBuilder.currentElement().childNode(0) instanceof DocumentType);
        Assert.assertEquals("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">",
                treeBuilder.currentElement().childNode(0).outerHtml());

        // Error scenario: Doctype is null
        try {
            treeBuilder.insert((Token.Doctype) null);
            Assert.fail("Exception not thrown");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NullPointerException);
        }
    }

    @Test
    public void testPopStackToClose() {
        // Typical use case
        Token endTagToken = new Token();
        endTagToken.type = Token.TokenType.EndTag;
        endTagToken.name = "div";
        Token.EndTag endTag = endTagToken.asEndTag();

        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        stack.add(new Element("html"));
        stack.add(new Element("body"));
        stack.add(new Element("div"));

        treeBuilder.popStackToClose(endTag);

        // Verify that the stack is popped up to the first occurrence of the end tag
        Assert.assertEquals(2, treeBuilder.stack.size());
        Assert.assertEquals("div", treeBuilder.stack.get(treeBuilder.stack.size() - 1).tagName());

        // Error scenario: End tag not found
        endTagToken.name = "p";

        treeBuilder.popStackToClose(endTag);

        // Verify that no elements are popped from the stack
        Assert.assertEquals(2, treeBuilder.stack.size());
    }

    @Test
    public void testParseFragment() {
        // Typical use case
        String inputFragment = "<p>This is a paragraph</p>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        List<Node> nodes = treeBuilder.parseFragment(inputFragment, baseUri, errors);

        // Verify that the input fragment is parsed and returns a list of nodes
        Assert.assertEquals(1, nodes.size());
        Assert.assertTrue(nodes.get(0) instanceof Element);

        // Error scenario: Input fragment is null
        try {
            treeBuilder.parseFragment(null, baseUri, errors);
            Assert.fail("Exception not thrown");
        } catch (Exception e) {
            Assert.assertTrue(e instanceof NullPointerException);
        }
    }
}