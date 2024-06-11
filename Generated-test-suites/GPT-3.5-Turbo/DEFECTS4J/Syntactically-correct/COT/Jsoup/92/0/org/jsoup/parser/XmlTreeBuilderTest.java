
import org.junit.Test;
import org.junit.Assert;
import java.util.Arrays;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        Assert.assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Reader reader = new StringReader("test");
        String baseUri = "http://example.com";
        Parser parser = new Parser(builder);
        builder.initialiseParse(reader, baseUri, parser);
        // Verify the stack contains the document
        Assert.assertEquals(builder.stack.get(0), builder.doc);
        // Verify the document's output settings syntax is XML
        Assert.assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
    }

    @Test
    public void testParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Reader reader = new StringReader("test");
        String baseUri = "http://example.com";
        Document doc = builder.parse(reader, baseUri);
        // Verify the parsed document is not null
        Assert.assertNotNull(doc);
    }

    @Test
    public void testParseString() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<xml></xml>";
        String baseUri = "http://example.com";
        Document doc = builder.parse(input, baseUri);
        // Verify the parsed document is not null
        Assert.assertNotNull(doc);
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.StartTag("tag", Arrays.asList(new Attribute("attr", "value")));
        boolean result = builder.process(token);
        // Verify the start tag is inserted correctly
        Assert.assertTrue(result);
        Assert.assertEquals(1, builder.stack.size());
    }
    
    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element(Tag.valueOf("tag"), "http://example.com"));
        Token token = new Token.EndTag("tag");
        boolean result = builder.process(token);
        // Verify the end tag is inserted correctly
        Assert.assertTrue(result);
        Assert.assertEquals(0, builder.stack.size());
    }
    
    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.Comment("comment");
        boolean result = builder.process(token);
        // Verify the comment is inserted correctly
        Assert.assertTrue(result);
        Assert.assertEquals(1, builder.stack.size());
        Assert.assertTrue(builder.stack.get(0).nodeName().equals("#comment"));
    }
    
    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.Character("data");
        boolean result = builder.process(token);
        // Verify the character data is inserted correctly
        Assert.assertTrue(result);
        Assert.assertEquals(1, builder.stack.size());
        Assert.assertTrue(builder.stack.get(0).nodeName().equals("#text"));
    }
    
    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token token = new Token.Doctype("html");
        boolean result = builder.process(token);
        // Verify the doctype is inserted correctly
        Assert.assertTrue(result);
        Assert.assertEquals(1, builder.stack.size());
        Assert.assertTrue(builder.stack.get(0).nodeName().equals("#doctype"));
    }
    
    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("tag"), "http://example.com");
        builder.insertNode(element);
        // Verify the node is inserted correctly into the current element
        Assert.assertEquals(1, builder.currentElement().childNodeSize());
        Assert.assertEquals(element, builder.currentElement().childNode(0));
    }
    
    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag", Arrays.asList(new Attribute("attr", "value")));
        Element element = builder.insert(startTag);
        // Verify the start tag is inserted correctly
        Assert.assertEquals(1, builder.currentElement().childNodeSize());
        Assert.assertEquals(element, builder.currentElement().childNode(0));
    }
    
    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("comment");
        builder.insert(commentToken);
        // Verify the comment is inserted correctly
        Assert.assertEquals(1, builder.currentElement().childNodeSize());
        Assert.assertEquals("#comment", builder.currentElement().childNode(0).nodeName());
    }
    
    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("data");
        builder.insert(characterToken);
        // Verify the character data is inserted correctly
        Assert.assertEquals(1, builder.currentElement().childNodeSize());
        Assert.assertEquals("#text", builder.currentElement().childNode(0).nodeName());
    }
    
    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html");
        builder.insert(doctype);
        // Verify the doctype is inserted correctly
        Assert.assertEquals(1, builder.currentElement().childNodeSize());
        Assert.assertEquals("#doctype", builder.currentElement().childNode(0).nodeName());
    }
    
    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element(Tag.valueOf("tag"), "http://example.com"));
        Token.EndTag endTag = new Token.EndTag("tag");
        builder.popStackToClose(endTag);
        // Verify the first occurrence of the tag is removed from the stack
        Assert.assertEquals(0, builder.stack.size());
    }
    
    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<tag>data</tag>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(builder);
        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, parser);
        // Verify the parsed fragment contains the correct nodes
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("tag", nodes.get(0).nodeName());
        Assert.assertEquals("data", nodes.get(0).childNode(0).outerHtml());
    }
    
    @Test
    public void testParseFragmentWithContext() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<tag>data</tag>";
        Element context = new Element(Tag.valueOf("context"), "http://example.com");
        String baseUri = "http://example.com";
        Parser parser = new Parser(builder);
        List<Node> nodes = builder.parseFragment(inputFragment, context, baseUri, parser);
        // Verify the parsed fragment contains the correct nodes
        Assert.assertEquals(1, nodes.size());
        Assert.assertEquals("tag", nodes.get(0).nodeName());
        Assert.assertEquals("data", nodes.get(0).childNode(0).outerHtml());
    }
}

