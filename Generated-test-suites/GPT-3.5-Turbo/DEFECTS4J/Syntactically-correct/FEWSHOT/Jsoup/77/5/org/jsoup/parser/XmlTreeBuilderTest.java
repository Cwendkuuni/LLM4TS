import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.parser.Token;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertTrue(settings.preserveCase());
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        String input = "<xml></xml>";
        Document document = xmlTreeBuilder.parse(new StringReader(input), "http://www.example.com");
        Element root = document.child(0);
        assertEquals("xml", root.tagName());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<xml></xml>";
        Document document = xmlTreeBuilder.parse(input, "http://www.example.com");
        Element root = document.child(0);
        assertEquals("xml", root.tagName());
    }

    @Test
    public void testInitialiseParse() {
        Document document = Jsoup.parse("");
        xmlTreeBuilder.setDoc(document);
        Reader input = new StringReader("<xml></xml>");
        String baseUri = "http://www.example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);

        assertTrue(xmlTreeBuilder.getStack().contains(document));
        assertEquals(Document.OutputSettings.Syntax.xml, document.outputSettings().syntax());
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("xml");
        startTag.attributes(new Token.Character());
        Element element = xmlTreeBuilder.insert(startTag);

        assertEquals("xml", element.tagName());
        assertEquals("http://www.example.com", element.baseUri());
        assertEquals(1, element.attributes().size());
    }

    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("xml");
        xmlTreeBuilder.getStack().add(new Element(Tag.valueOf("xml"), ""));

        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(0, xmlTreeBuilder.getStack().size());
    }

    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment();
        commentToken.data("This is a comment");
        xmlTreeBuilder.insert(commentToken);

        Comment comment = (Comment) xmlTreeBuilder.currentElement().childNode(0);
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character character = new Token.Character();
        character.data("This is a character");
        xmlTreeBuilder.insert(character);

        TextNode textNode = (TextNode) xmlTreeBuilder.currentElement().childNode(0);
        assertEquals("This is a character", textNode.getWholeText());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "public", "system");
        xmlTreeBuilder.insert(doctype);

        DocumentType documentType = (DocumentType) xmlTreeBuilder.currentElement().childNode(0);
        assertEquals("html", documentType.nodeName());
        assertEquals("public", documentType.attr("publicId"));
        assertEquals("system", documentType.attr("systemId"));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<p>This is a paragraph</p>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, "http://www.example.com", ParseErrorList.noTracking(), xmlTreeBuilder.defaultSettings());

        assertEquals(1, nodes.size());
        Element element = (Element) nodes.get(0);
        assertEquals("p", element.tagName());
        assertEquals("This is a paragraph", element.text());
    }
}