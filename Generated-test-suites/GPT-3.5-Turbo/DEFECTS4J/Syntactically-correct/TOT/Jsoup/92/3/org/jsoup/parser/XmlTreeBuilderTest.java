
//Import necessary packages
import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();

        // Assert that the default settings are set to preserve case
        Assert.assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testInitialiseParse() {
        // Create XmlTreeBuilder object
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        // Create a test input and baseUri
        Reader input = new StringReader("<tag>Test</tag>");
        String baseUri = "http://example.com";
        // Create a test Parser object
        Parser parser = new Parser(xmlTreeBuilder);

        // Call the initialiseParse method
        xmlTreeBuilder.initialiseParse(input, baseUri, parser);

        // Assert that the stack contains the document
        Assert.assertTrue(xmlTreeBuilder.stack.contains(xmlTreeBuilder.doc));
        // Assert that the document's output settings syntax is set to xml
        Assert.assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testParse() {
        // Create XmlTreeBuilder object
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        // Create a test input and baseUri
        String input = "<tag>Test</tag>";
        String baseUri = "http://example.com";

        // Call the parse method
        Document document = xmlTreeBuilder.parse(input, baseUri);

        // Assert that the parsed document is not null
        Assert.assertNotNull(document);
        // Assert that the parsed document's baseUri is equal to the input baseUri
        Assert.assertEquals(baseUri, document.baseUri());
        // Assert that the document contains a single element with the tag name "tag"
        Assert.assertEquals(1, document.getElementsByTag("tag").size());
    }

    @Test
    public void testProcess() {
        // Create XmlTreeBuilder object
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        // Create a test token of type StartTag and name "tag"
        Token startTagToken = new Token.StartTag("tag");
        
        // Call the process method for the start tag token
        boolean result = xmlTreeBuilder.process(startTagToken);

        // Assert that the result is true
        Assert.assertTrue(result);
        // Assert that the stack contains the element with the tag name "tag"
        Assert.assertTrue(xmlTreeBuilder.stack.contains(xmlTreeBuilder.insert(startTagToken.asStartTag())));
    }

    @Test
    public void testInsertNode() {
        // Create XmlTreeBuilder object
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        // Create a test element with tag name "tag"
        Element element = new Element("tag");

        // Call the insertNode method for the test element
        xmlTreeBuilder.insertNode(element);

        // Assert that the current element's child nodes contain the test element
        Assert.assertTrue(xmlTreeBuilder.currentElement().childNodes().contains(element));
    }

    @Test
    public void testInsertStartTag() {
        // Create XmlTreeBuilder object
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        // Create a test StartTag token with name "tag"
        Token.StartTag startTag = new Token.StartTag("tag");

        // Call the insert method for the test StartTag token
        Element element = xmlTreeBuilder.insert(startTag);

        // Assert that the returned element is not null
        Assert.assertNotNull(element);
        // Assert that the tag name of the returned element is equal to "tag"
        Assert.assertEquals("tag", element.tagName());
        // Assert that the returned element is added to the stack
        Assert.assertTrue(xmlTreeBuilder.stack.contains(element));
    }

    @Test
    public void testInsertComment() {
        // Create XmlTreeBuilder object
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        // Create a test Comment token with data "Test comment"
        Token.Comment commentToken = new Token.Comment("Test comment");

        // Call the insert method for the test Comment token
        xmlTreeBuilder.insert(commentToken);

        // Assert that the current element's child nodes contain the test Comment node
        Assert.assertTrue(xmlTreeBuilder.currentElement().childNodes().contains(new Comment("Test comment")));
    }

    @Test
    public void testInsertCharacter() {
        // Create XmlTreeBuilder object
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        // Create a test Character token with data "Test character"
        Token.Character characterToken = new Token.Character("Test character");

        // Call the insert method for the test Character token
        xmlTreeBuilder.insert(characterToken);

        // Assert that the current element's child nodes contain the test TextNode
        Assert.assertTrue(xmlTreeBuilder.currentElement().childNodes().contains(new TextNode("Test character")));
    }

    @Test
    public void testInsertDoctype() {
        // Create XmlTreeBuilder object
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        // Create a test Doctype token with name "html"
        Token.Doctype doctypeToken = new Token.Doctype("html");

        // Call the insert method for the test Doctype token
        xmlTreeBuilder.insert(doctypeToken);

        // Assert that the current element's child nodes contain the test DocumentType node
        Assert.assertTrue(xmlTreeBuilder.currentElement().childNodes().contains(new DocumentType("html")));
    }

    @Test
    public void testPopStackToClose() {
        // Create XmlTreeBuilder object
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        // Create a test EndTag token with tag name "tag"
        Token.EndTag endTag = new Token.EndTag("tag");
        // Create a test element with tag name "tag"
        Element element = new Element("tag");
        // Add the test element to the stack
        xmlTreeBuilder.stack.add(element);

        // Call the popStackToClose method for the test EndTag token
        xmlTreeBuilder.popStackToClose(endTag);

        // Assert that the test element is no longer in the stack
        Assert.assertFalse(xmlTreeBuilder.stack.contains(element));
    }

    @Test
    public void testParseFragment() {
        // Create XmlTreeBuilder object
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        // Create a test input fragment, baseUri, and Parser object
        String inputFragment = "<tag>Test</tag>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(xmlTreeBuilder);

        // Call the parseFragment method for the test input fragment, baseUri, and parser
        List<Node> nodeList = xmlTreeBuilder.parseFragment(inputFragment, baseUri, parser);

        // Assert that the returned nodeList is not null
        Assert.assertNotNull(nodeList);
        // Assert that the nodeList contains two child nodes (Element and TextNode)
        Assert.assertEquals(2, nodeList.size());
    }
}
