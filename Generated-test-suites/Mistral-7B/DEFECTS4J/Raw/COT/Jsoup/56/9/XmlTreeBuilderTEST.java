import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(ParseSettings.Class.PRESERVE_CASE, settings.classification());
    }

    @Test
    public void testParse() {
        String input = "<xml><tag>Test</tag></xml>";
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = xmlTreeBuilder.parse(input, "");
        assertNotNull(document);
        Element root = document.rootElement();
        assertEquals("xml", root.tagName());
        Element tag = root.child(0);
        assertEquals("tag", tag.tagName());
        assertEquals("Test", tag.text());
    }

    @Test(expected = AssertionError.class)
    public void testInvalidTokenType() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token invalidToken = new Token(-1, "Invalid Token");
        xmlTreeBuilder.process(invalidToken);
    }

    @Test
    public void testStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<tag attr1='value1'>Hello World</tag>";
        Token startTag = new Token.StartTag("tag", null, null, null, null, null, null, null, null, null, null);
        Element element = xmlTreeBuilder.insert(startTag);
        assertEquals("tag", element.tagName());
        assertEquals("value1", element.attribute(0).getValue());
        assertEquals("Hello World", element.text());
    }

    @Test
    public void testEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<tag><subtag/></tag>";
        Token endTag = new Token.EndTag("tag");
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(1, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<!-- This is a comment -->";
        Token commentToken = new Token.Comment("This is a comment");
        xmlTreeBuilder.insert(commentToken);
        Element comment = xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
        assertEquals(Comment.class.getName(), comment.getClass().getName());
        assertEquals("This is a comment", comment.text());
    }

    @Test
    public void testCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<tag>Hello World</tag>";
        Token characterToken = new Token.Character('H');
        xmlTreeBuilder.insert(characterToken);
        Element tag = xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
        TextNode textNode = (TextNode) tag.child(0);
        assertEquals("Hello World", textNode.text());
    }

    @Test
    public void testDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<!DOCTYPE xml SYSTEM \"xml.dtd\">";
        Token doctypeToken = new Token.Doctype("xml", null, null, "");
        DocumentType documentType = (DocumentType) xmlTreeBuilder.insert(doctypeToken);
        assertEquals("xml", documentType.nodeName());
    }

    @Test(expected = AssertionError.class)
    public void testBogusComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<?xml version='1.0' encoding='UTF-8'?>";
        Token commentToken = new Token.Comment(input);
        xmlTreeBuilder.insert(commentToken);
        Validate.fail("Unexpected error");
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<tag1><tag2 attr='value'><tag3/></tag2></tag1>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(input, "", new ParseErrorList(), ParseSettings.preserveCase);
        assertEquals(4, nodes.size());
        Element tag1 = (Element) nodes.get(0);
        Element tag2 = (Element) nodes.get(1);
        Element tag3 = (Element) nodes.get(2);
        TextNode textNode = (TextNode) nodes.get(3);
        assertEquals("tag1", tag1.tagName());
        assertEquals("tag2", tag2.tagName());
        assertEquals("tag3", tag3.tagName());
        assertEquals("value", tag2.attribute(0).getValue());
        assertEquals("", textNode.text());
    }
}