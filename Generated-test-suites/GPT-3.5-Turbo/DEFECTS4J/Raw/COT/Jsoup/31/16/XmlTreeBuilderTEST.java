
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

public class XmlTreeBuilderTest {
    
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<xml><tag>content</tag></xml>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        
        builder.initialiseParse(input, baseUri, errors);
        
        // verify that the document is placed onto the stack
        assert(builder.stack.contains(builder.doc));
    }
    
    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag");
        startTag.attributes.put("attribute", "value");
        
        builder.insert(startTag);
        Element element = builder.stack.peek();
        
        // verify that the element is inserted with the correct tag name and attributes
        assert(element.tagName().equals("tag"));
        assert(element.attributes().get("attribute").equals("value"));
    }
    
    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("tag");
        
        // add a matching start tag to the stack
        Token.StartTag startTag = new Token.StartTag("tag");
        builder.insert(startTag);
        
        builder.popStackToClose(endTag);
        
        // verify that the start tag is removed from the stack
        assert(builder.stack.isEmpty());
    }
    
    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("This is a comment");
        
        builder.insert(commentToken);
        Node node = builder.stack.peek().childNode(0);
        
        // verify that the comment node is inserted with the correct data
        assert(node instanceof Comment);
        assert(((Comment) node).getData().equals("This is a comment"));
    }
    
    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("Text");
        
        builder.insert(characterToken);
        Node node = builder.stack.peek().childNode(0);
        
        // verify that the text node is inserted with the correct data
        assert(node instanceof TextNode);
        assert(((TextNode) node).getWholeText().equals("Text"));
    }
    
    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype("html", "-//W3C//DTD XHTML 1.0 Transitional//EN", "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
        
        builder.insert(doctypeToken);
        Node node = builder.stack.peek().childNode(0);
        
        // verify that the doctype node is inserted with the correct data
        assert(node instanceof DocumentType);
        assert(((DocumentType) node).getName().equals("html"));
        assert(((DocumentType) node).getPublicIdentifier().equals("-//W3C//DTD XHTML 1.0 Transitional//EN"));
        assert(((DocumentType) node).getSystemIdentifier().equals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd"));
    }
}
