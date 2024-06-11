import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testInitialiseParse() {
        String input = "<root><child><grandchild></grandchild></child></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        assertEquals(input, xmlTreeBuilder.getDoc().toString());
        assertEquals(baseUri, xmlTreeBuilder.getBaseUri());
        assertTrue(xmlTreeBuilder.getStack().size() == 1);
        assertEquals("root", xmlTreeBuilder.getStack().peek().nodeName());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag", "attr=value");
        Element element = xmlTreeBuilder.insert(startTag);

        assertEquals("tag", element.nodeName());
        assertEquals("attr=value", element.attributes().toString());
        assertTrue(element.parent() instanceof Document);
    }

    @Test
    public void testProcessStartTagSelfClosing() {
        Token.StartTag selfClosingStartTag = new Token.StartTag("selfclosingtag", "attr=value", true);
        Element selfClosingElement = xmlTreeBuilder.insert(selfClosingStartTag);

        assertEquals("selfclosingtag", selfClosingElement.nodeName());
        assertEquals("attr=value", selfClosingElement.attributes().toString());
        assertTrue(selfClosingElement.parent() instanceof Document);
        assertTrue(selfClosingElement.tag().isSelfClosing());
    }

    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("tag");
        Document doc = Jsoup.parse("<root><tag></tag></root>");
        xmlTreeBuilder.setDoc(doc);
        xmlTreeBuilder.setStack(doc.select("root"));
        xmlTreeBuilder.setBaseUri("http://example.com");

        xmlTreeBuilder.popStackToClose(endTag);

        assertTrue(xmlTreeBuilder.getStack().isEmpty());
    }

    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("This is a comment");
        Document doc = Jsoup.parse("<root></root>");
        xmlTreeBuilder.setDoc(doc);
        xmlTreeBuilder.setStack(doc.select("root"));
        xmlTreeBuilder.setBaseUri("http://example.com");

        xmlTreeBuilder.insert(commentToken);

        assertEquals("This is a comment", doc.select("root").first().childNode(0).toString());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character characterToken = new Token.Character("This is a character");
        Document doc = Jsoup.parse("<root></root>");
        xmlTreeBuilder.setDoc(doc);
        xmlTreeBuilder.setStack(doc.select("root"));
        xmlTreeBuilder.setBaseUri("http://example.com");

        xmlTreeBuilder.insert(characterToken);

        assertEquals("This is a character", doc.select("root").first().childNode(0).toString());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "", null);
        Document doc = Jsoup.parse("<root></root>");
        xmlTreeBuilder.setDoc(doc);
        xmlTreeBuilder.setStack(doc.select("root"));
        xmlTreeBuilder.setBaseUri("http://example.com");

        xmlTreeBuilder.insert(doctype);

        assertEquals("<!DOCTYPE html>", doc.childNode(0).toString());
    }
}