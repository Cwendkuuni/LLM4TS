import org.junit.Test;
import org.jsoup.parser.*;
import org.jsoup.nodes.*;
import org.jsoup.helper.Validate;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        ParseSettings settings = treeBuilder.defaultSettings();
        assertNotNull(settings);
        assertEquals(ParseSettings.preserveCase, settings);
    }
    
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Reader input = new StringReader("<xml><tag>content</tag></xml>");
        String baseUri = "https://www.example.com";
        Parser parser = new Parser(treeBuilder);
        
        treeBuilder.initialiseParse(input, baseUri, parser);
        assertEquals(input, treeBuilder.getInput());
        assertEquals(baseUri, treeBuilder.getBaseUri());
        assertEquals(parser, treeBuilder.getParser());
        assertEquals(treeBuilder.getDocument(), treeBuilder.getStack().get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, treeBuilder.getDocument().outputSettings().syntax());
    }
    
    @Test
    public void testParse() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Reader input = new StringReader("<xml><tag>content</tag></xml>");
        String baseUri = "https://www.example.com";
        Document expected = new Document(baseUri);
        Element root = new Element(Tag.valueOf("xml"), baseUri);
        Element tag = new Element(Tag.valueOf("tag"), baseUri);
        TextNode content = new TextNode("content");
        tag.appendChild(content);
        root.appendChild(tag);
        expected.appendChild(root);
        
        Document actual = treeBuilder.parse(input, baseUri);
        
        assertEquals(expected.toString(), actual.toString());
    }
    
    @Test
    public void testParseFromString() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        String input = "<xml><tag>content</tag></xml>";
        String baseUri = "https://www.example.com";
        Document expected = new Document(baseUri);
        Element root = new Element(Tag.valueOf("xml"), baseUri);
        Element tag = new Element(Tag.valueOf("tag"), baseUri);
        TextNode content = new TextNode("content");
        tag.appendChild(content);
        root.appendChild(tag);
        expected.appendChild(root);
        
        Document actual = treeBuilder.parse(input, baseUri);
        
        assertEquals(expected.toString(), actual.toString());
    }
    
    @Test
    public void testProcess() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token token = new Token.StartTag("tag", "attr=value", false);
        
        assertTrue(treeBuilder.process(token));
        
        Element expected = new Element(Tag.valueOf("tag"), treeBuilder.getBaseUri());
        expected.attributes().put("attr", "value");
        Element actual = treeBuilder.getStack().get(treeBuilder.getStack().size() - 1);
        
        assertEquals(expected.toString(), actual.toString());
    }
    
    @Test
    public void testInsertNode() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Element root = new Element(Tag.valueOf("root"), treeBuilder.getBaseUri());
        treeBuilder.getStack().add(root);
        Element element = new Element(Tag.valueOf("tag"), treeBuilder.getBaseUri());
        
        treeBuilder.insertNode(element);
        
        assertEquals(element.toString(), root.child(0).toString());
    }
    
    @Test
    public void testInsertStartTagWithSelfClosingAttribute() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag", "attr=value", true);
        
        Element expected = new Element(Tag.valueOf("tag"), treeBuilder.getBaseUri());
        expected.attributes().put("attr", "value");
        treeBuilder.insert(startTag);
        Element actual = treeBuilder.getStack().get(treeBuilder.getStack().size() - 1);
        
        assertEquals(expected.toString(), actual.toString());
    }
    
    @Test
    public void testInsertStartTagWithoutSelfClosingAttribute() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag", "attr=value", false);
        
        Element expected = new Element(Tag.valueOf("tag"), treeBuilder.getBaseUri());
        expected.attributes().put("attr", "value");
        treeBuilder.insert(startTag);
        Element actual = treeBuilder.getStack().get(treeBuilder.getStack().size() - 1);
        
        assertEquals(expected.toString(), actual.toString());
    }
    
    @Test
    public void testInsertComment() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("content");
        
        Comment expected = new Comment("content");
        treeBuilder.insert(commentToken);
        Comment actual = (Comment)treeBuilder.getStack().get(treeBuilder.getStack().size() - 1);
        
        assertEquals(expected.toString(), actual.toString());
    }
    
    @Test
    public void testInsertCommentXmlDeclaration() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        
        XmlDeclaration expected = new XmlDeclaration("xml", "version=\"1.0\" encoding=\"UTF-8\"");
        treeBuilder.insert(commentToken);
        XmlDeclaration actual = (XmlDeclaration)treeBuilder.getStack().get(treeBuilder.getStack().size() - 1);
        
        assertEquals(expected.toString(), actual.toString());
    }
    
    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("content");
        
        TextNode expected = new TextNode("content");
        treeBuilder.insert(characterToken);
        TextNode actual = (TextNode)treeBuilder.getStack().get(treeBuilder.getStack().size() - 1);
        
        assertEquals(expected.toString(), actual.toString());
    }
    
    @Test
    public void testInsertCharacterCData() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("content", true);
        
        CDataNode expected = new CDataNode("content");
        treeBuilder.insert(characterToken);
        CDataNode actual = (CDataNode)treeBuilder.getStack().get(treeBuilder.getStack().size() - 1);
        
        assertEquals(expected.toString(), actual.toString());
    }
    
    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype("html", "", "");
        
        DocumentType expected = new DocumentType("html", "", "");
        treeBuilder.insert(doctypeToken);
        DocumentType actual = (DocumentType)treeBuilder.getStack().get(treeBuilder.getStack().size() - 1);
        
        assertEquals(expected.toString(), actual.toString());
    }
    
    @Test
    public void testPopStackToCloseNotFound() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.getStack().add(new Element(Tag.valueOf("tag1"), treeBuilder.getBaseUri()));
        
        Token.EndTag endTag = new Token.EndTag("tag2");
        treeBuilder.popStackToClose(endTag);
        
        assertEquals(1, treeBuilder.getStack().size());
    }
    
    @Test
    public void testPopStackToCloseFound() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Element tag1 = new Element(Tag.valueOf("tag1"), treeBuilder.getBaseUri());
        treeBuilder.getStack().add(tag1);
        Element tag2 = new Element(Tag.valueOf("tag2"), treeBuilder.getBaseUri());
        treeBuilder.getStack().add(tag2);
        Element tag3 = new Element(Tag.valueOf("tag3"), treeBuilder.getBaseUri());
        treeBuilder.getStack().add(tag3);
        
        Token.EndTag endTag = new Token.EndTag("tag2");
        treeBuilder.popStackToClose(endTag);
        
        assertEquals(2, treeBuilder.getStack().size());
    }
    
    @Test
    public void testParseFragment() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        String inputFragment = "<tag>content</tag>";
        String baseUri = "https://www.example.com";
        Parser parser = new Parser(treeBuilder);
        
        List<Node> expected = treeBuilder.parseFragment(inputFragment, baseUri, parser);
        List<Node> actual = treeBuilder.getDocument().childNodes();
        
        assertEquals(expected.toString(), actual.toString());
    }
    
    @Test
    public void testParseFragmentWithContext() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        String inputFragment = "<tag>content</tag>";
        String baseUri = "https://www.example.com";
        Element context = new Element(Tag.valueOf("context"), baseUri);
        Parser parser = new Parser(treeBuilder);
        
        List<Node> expected = treeBuilder.parseFragment(inputFragment, context, baseUri, parser);
        List<Node> actual = treeBuilder.getDocument().childNodes();
        
        assertEquals(expected.toString(), actual.toString());
    }
}