import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.*;
import org.jsoup.parser.Token.*;
import java.util.Iterator;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        String input = "<html><body><div></div></body></html>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);
        
        // Check if the document is placed on stack
        Assert.assertEquals(xmlTreeBuilder.stack.size(), 1);
        Assert.assertEquals(xmlTreeBuilder.stack.get(0), xmlTreeBuilder.doc);
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("div");
        XmlTreeBuilder.Element el = xmlTreeBuilder.insert(startTag);
        
        // Check if the element is inserted correctly
        Assert.assertEquals(el.tagName(), "div");
        Assert.assertEquals(el.baseUri(), xmlTreeBuilder.baseUri);
        Assert.assertEquals(el.attributes(), startTag.attributes);

        // Check if self-closing flag is acknowledged
        Assert.assertEquals(xmlTreeBuilder.tokeniser.selfClosingFlagAcknowledged, true);
    }

    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);
        
        // Check if the comment is inserted correctly
        Node node = xmlTreeBuilder.currentElement().childNode(0);
        Assert.assertTrue(node instanceof Comment);
        Comment comment = (Comment) node;
        Assert.assertEquals(comment.getData(), commentToken.getData());
        Assert.assertEquals(comment.baseUri(), xmlTreeBuilder.baseUri);
    }

    @Test
    public void testProcessCharacter() {
        Token.Character characterToken = new Token.Character("Some text");
        xmlTreeBuilder.insert(characterToken);
        
        // Check if the character node is inserted correctly
        Node node = xmlTreeBuilder.currentElement().childNode(0);
        Assert.assertTrue(node instanceof TextNode);
        TextNode textNode = (TextNode) node;
        Assert.assertEquals(textNode.getWholeText(), characterToken.getData());
        Assert.assertEquals(textNode.baseUri(), xmlTreeBuilder.baseUri);
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctypeToken = new Token.Doctype("html", "publicId", "systemId");
        xmlTreeBuilder.insert(doctypeToken);
        
        // Check if the doctype node is inserted correctly
        Node node = xmlTreeBuilder.currentElement().childNode(0);
        Assert.assertTrue(node instanceof DocumentType);
        DocumentType doctype = (DocumentType) node;
        Assert.assertEquals(doctype.getName(), doctypeToken.getName());
        Assert.assertEquals(doctype.getPublicIdentifier(), doctypeToken.getPublicIdentifier());
        Assert.assertEquals(doctype.getSystemIdentifier(), doctypeToken.getSystemIdentifier());
        Assert.assertEquals(doctype.baseUri(), xmlTreeBuilder.baseUri);
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("div");
        
        // Add some elements to the stack
        Element el1 = new Element(Tag.valueOf("h1"), xmlTreeBuilder.baseUri);
        Element el2 = new Element(Tag.valueOf("div"), xmlTreeBuilder.baseUri);
        Element el3 = new Element(Tag.valueOf("p"), xmlTreeBuilder.baseUri);
        xmlTreeBuilder.stack.add(el1);
        xmlTreeBuilder.stack.add(el2);
        xmlTreeBuilder.stack.add(el3);

        // Call the method to pop the stack and remove the first occurrence of "div"
        xmlTreeBuilder.popStackToClose(endTag);
        
        // Check if "div" is removed from the stack
        Iterator<Element> it = xmlTreeBuilder.stack.descendingIterator();
        while (it.hasNext()) {
            Element next = it.next();
            Assert.assertNotEquals(next.tagName(), "div");
        }
    }

}