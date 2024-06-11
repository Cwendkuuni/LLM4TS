
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;
    
    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }
    
    @Test
    public void testInitialiseParse() {
        String input = "<xml></xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        assertEquals(errors.hasErrors(), false);
        assertEquals(xmlTreeBuilder.getStack().size(), 1);
        assertEquals(xmlTreeBuilder.getDoc().outputSettings().syntax(), Document.OutputSettings.Syntax.xml);
    }
    
    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag", "name", null, false);
        
        Element element = xmlTreeBuilder.insert(startTag);
        
        assertEquals(element.tagName(), "tag");
        assertEquals(element.baseUri(), xmlTreeBuilder.getBaseUri());
        assertEquals(element.children().size(), 0);
    }
    
    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("tag");
        
        xmlTreeBuilder.getStack().add(new Element("tag"));
        xmlTreeBuilder.popStackToClose(endTag);
        
        assertEquals(xmlTreeBuilder.getStack().size(), 0);
    }
    
    @Test
    public void testProcessComment() {
        Token.Comment comment = new Token.Comment("comment");
        
        xmlTreeBuilder.insert(comment);
        
        assertEquals(xmlTreeBuilder.getCurrentElement().nodeName(), "#comment");
        assertEquals(xmlTreeBuilder.getCurrentElement().outerHtml(), "<!--comment-->");
    }
    
    @Test
    public void testProcessCharacter() {
        Token.Character character = new Token.Character("text");
        
        xmlTreeBuilder.insert(character);
        
        assertEquals(xmlTreeBuilder.getCurrentElement().nodeName(), "#text");
        assertEquals(xmlTreeBuilder.getCurrentElement().outerHtml(), "text");
    }
    
    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "", "");
        
        xmlTreeBuilder.insert(doctype);
        
        assertEquals(xmlTreeBuilder.getCurrentElement().nodeName(), "!doctype");
        assertEquals(xmlTreeBuilder.getCurrentElement().outerHtml(), "<!doctype html>");
    }
    
    @Test
    public void testParseFragment() {
        String inputFragment = "<xml></xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);
        
        assertEquals(errors.hasErrors(), false);
        assertEquals(nodes.size(), 1);
        assertEquals(nodes.get(0).nodeName(), "xml");
    }
}
