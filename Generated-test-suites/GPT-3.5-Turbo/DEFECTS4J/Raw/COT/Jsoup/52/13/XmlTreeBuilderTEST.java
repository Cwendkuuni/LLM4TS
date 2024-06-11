import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import java.util.List;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        String input = "<xml>Test</xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        Assert.assertEquals(xmlTreeBuilder.stack.size(), 1);
        Assert.assertEquals(xmlTreeBuilder.doc.outputSettings().syntax(), Document.OutputSettings.Syntax.xml);
    }
    
    @Test
    public void testProcess() {
        Token token = createToken();
        boolean result = xmlTreeBuilder.process(token);
        Assert.assertTrue(result);
    }
    
    @Test
    public void testInsertNode() {
        Node node = createNode();
        xmlTreeBuilder.insertNode(node);
        Assert.assertEquals(xmlTreeBuilder.currentElement().childNodes().size(), 1);
    }
    
    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = createStartTag();
        Element element = xmlTreeBuilder.insert(startTag);
        Assert.assertNotNull(element);
    }
    
    @Test
    public void testInsertComment() {
        Token.Comment commentToken = createCommentToken();
        xmlTreeBuilder.insert(commentToken);
        Assert.assertEquals(xmlTreeBuilder.currentElement().childNodes().size(), 1);
    }
    
    @Test
    public void testInsertCharacter() {
        Token.Character characterToken = createCharacterToken();
        xmlTreeBuilder.insert(characterToken);
        Assert.assertEquals(xmlTreeBuilder.currentElement().childNodes().size(), 1);
    }
    
    @Test
    public void testInsertDoctype() {
        Token.Doctype doctypeToken = createDoctypeToken();
        xmlTreeBuilder.insert(doctypeToken);
        Assert.assertEquals(xmlTreeBuilder.currentElement().childNodes().size(), 1);
    }
    
    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = createEndTag();
        xmlTreeBuilder.stack.add(createElement("div"));
        xmlTreeBuilder.stack.add(createElement("span"));
        xmlTreeBuilder.stack.add(createElement("div"));
        
        xmlTreeBuilder.popStackToClose(endTag);
        
        Assert.assertEquals(xmlTreeBuilder.stack.size(), 1);
        Assert.assertEquals(xmlTreeBuilder.stack.get(0).nodeName(), "div");
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<div>Test</div>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);
        Assert.assertEquals(nodes.size(), 1);
        Assert.assertEquals(nodes.get(0).nodeName(), "div");
    }
    
    private Token createToken() {
        Token token = new Token(Token.TokenType.StartTag);
        token.value = "<div>";
        return token;
    }
    
    private Node createNode() {
        return new Element(Tag.valueOf("div"), "http://example.com");
    }
    
    private Token.StartTag createStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.attributes.put("class", "test");
        return startTag;
    }
    
    private Token.Comment createCommentToken() {
        Token.Comment commentToken = new Token.Comment();
        commentToken.data = "Test Comment";
        commentToken.baseUri = "http://example.com";
        commentToken.bogus = false;
        return commentToken;
    }
    
    private Token.Character createCharacterToken() {
        Token.Character characterToken = new Token.Character();
        characterToken.data = "Test";
        return characterToken;
    }
    
    private Token.Doctype createDoctypeToken() {
        Token.Doctype doctypeToken = new Token.Doctype();
        doctypeToken.name = "html";
        doctypeToken.publicIdentifier = "";
        doctypeToken.systemIdentifier = "";
        return doctypeToken;
    }
    
    private Token.EndTag createEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "span";
        return endTag;
    }
    
    private Element createElement(String tagName) {
        return new Element(Tag.valueOf(tagName), "http://example.com");
    }
}