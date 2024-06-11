
import org.jsoup.parser.ParseErrorList;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import java.util.Iterator;
import org.junit.Assert;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<example></example>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        builder.initialiseParse(input, baseUri, errors);
        // Check if the document is added to the stack
        Assert.assertEquals(builder.stack.get(0), builder.doc);
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("example"), "http://example.com");
        builder.insertNode(element);
        // Check if the element is added as a child to the current element
        Assert.assertEquals(builder.currentElement().childNode(0), element);
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag(Tag.valueOf("example"), null);
        Element element = builder.insert(startTag);
        // Check if the element is inserted and added to the stack
        Assert.assertEquals(builder.stack.get(0), element);
        // Check if the start tag is self-closing
        Assert.assertTrue(startTag.isSelfClosing());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("This is a comment");
        builder.insert(commentToken);
        // Check if the comment is inserted as a child to the current element
        Assert.assertEquals(builder.currentElement().childNode(0).nodeName(), "#comment");
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("This is a character");
        builder.insert(characterToken);
        // Check if the character is inserted as a child to the current element
        Assert.assertEquals(builder.currentElement().childNode(0).nodeName(), "#text");
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "", "", "http://www.w3.org/TR/html4/strict.dtd");
        builder.insert(doctype);
        // Check if the doctype is inserted as a child to the current element
        Assert.assertEquals(builder.currentElement().childNode(0).nodeName(), "#doctype");
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element(Tag.valueOf("example"), "http://example.com"));
        builder.stack.add(new Element(Tag.valueOf("example"), "http://example.com"));
        Token.EndTag endTag = new Token.EndTag("example");
        builder.popStackToClose(endTag);
        // Check if the first occurrence of the element is removed from the stack
        Assert.assertEquals(builder.stack.size(), 1);
    }
}
