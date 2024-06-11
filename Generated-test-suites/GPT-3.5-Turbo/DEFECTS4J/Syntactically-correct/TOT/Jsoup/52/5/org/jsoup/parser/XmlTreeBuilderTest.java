import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.TreeBuilder;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInsertNode() {
        Element element = new Element("testElement");
        xmlTreeBuilder.insertNode(element);
        assertEquals(element, xmlTreeBuilder.currentElement().childNodes().get(0));
    }

    @Test
    public void testInsert_StartTag_SelfClosing() {
        Token.StartTag selfClosingStartTag = new Token.StartTag("selfClosingTag", null, true);
        Element element = xmlTreeBuilder.insert(selfClosingStartTag);
        assertEquals("selfClosingTag", element.tagName());
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertEquals(element, xmlTreeBuilder.currentElement().childNodes().get(0));
    }

    @Test
    public void testInsert_StartTag_NotSelfClosing() {
        Token.StartTag notSelfClosingStartTag = new Token.StartTag("notSelfClosingTag", null, false);
        Element element = xmlTreeBuilder.insert(notSelfClosingStartTag);
        assertEquals("notSelfClosingTag", element.tagName());
        assertEquals(0, xmlTreeBuilder.currentElement().childNodes().size());
        assertEquals(element, xmlTreeBuilder.currentElement());
    }

    @Test
    public void testInsert_Comment_Normal() {
        Token.Comment normalComment = new Token.Comment("This is a normal comment");
        xmlTreeBuilder.insert(normalComment);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertEquals("<!--This is a normal comment-->", xmlTreeBuilder.currentElement().childNodes().get(0).outerHtml());
    }

    @Test
    public void testInsert_Comment_Bogus() {
        Token.Comment bogusComment = new Token.Comment("<!DOCTYPE html>");
        xmlTreeBuilder.insert(bogusComment);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertEquals("<!DOCTYPE html>", xmlTreeBuilder.currentElement().childNodes().get(0).outerHtml());
    }

    @Test
    public void testInsert_Character() {
        Token.Character character = new Token.Character("This is a character");
        xmlTreeBuilder.insert(character);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertEquals("This is a character", xmlTreeBuilder.currentElement().childNodes().get(0).outerHtml());
    }

    @Test
    public void testInsert_Doctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "", "http://www.w3.org/TR/html4/");
        xmlTreeBuilder.insert(doctype);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
        assertEquals("!DOCTYPE html PUBLIC \"\" \"http://www.w3.org/TR/html4/\"", xmlTreeBuilder.currentElement().childNodes().get(0).outerHtml());
    }

    @Test
    public void testPopStackToClose_ElementNotInStack() {
        Token.EndTag endTag = new Token.EndTag("nonexistentElement");
        xmlTreeBuilder.popStackToClose(endTag);
        assertNull(xmlTreeBuilder.currentElement());
    }

    @Test
    public void testPopStackToClose_ElementInStack() {
        Element element1 = new Element("testElement1");
        Element element2 = new Element("testElement2");
        Element element3 = new Element("testElement3");
        xmlTreeBuilder.stack.add(element1);
        xmlTreeBuilder.stack.add(element2);
        xmlTreeBuilder.stack.add(element3);

        Token.EndTag endTag = new Token.EndTag("testElement2");
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(element3, xmlTreeBuilder.currentElement());
        assertEquals(2, xmlTreeBuilder.stack.size());
        assertEquals(element1, xmlTreeBuilder.stack.get(0));
        assertEquals(element3, xmlTreeBuilder.stack.get(1));
    }

    @Test
    public void testParseFragment_ValidFragment_NoErrors() {
        String inputFragment = "<div>This is a div</div>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, "http://example.com", new ParseErrorList());
        assertEquals(1, nodes.size());
        assertEquals("This is a div", nodes.get(0).outerHtml());
    }

    @Test
    public void testParseFragment_InvalidFragment_HasErrors() {
        String inputFragment = "<div><p>Invalid fragment";
        ParseErrorList errors = new ParseErrorList();
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, "http://example.com", errors);
        assertEquals(0, nodes.size());
        assertEquals(1, errors.size());
        assertEquals("Element div is not closed", errors.get(0).toString());
    }

}