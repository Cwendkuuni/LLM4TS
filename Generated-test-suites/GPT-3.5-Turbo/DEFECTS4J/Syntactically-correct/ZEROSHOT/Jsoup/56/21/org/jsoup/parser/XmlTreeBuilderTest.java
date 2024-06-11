import static org.junit.Assert.*;

import java.util.List;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();

        assertNotNull(parseSettings);
    }

    @Test
    public void testParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<xml><tag1>data1</tag1><tag2>data2</tag2></xml>";
        String baseUri = "https://www.example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);

        assertNotNull(document);
        assertEquals("xml", document.nodeName());
        assertEquals("https://www.example.com", document.baseUri());
        assertEquals("data1", document.select("tag1").text());
        assertEquals("data2", document.select("tag2").text());
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<tag>data</tag>";
        String baseUri = "https://www.example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();

        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);

        Element docElement = xmlTreeBuilder.getDocument().child(0);
        assertEquals("tag", docElement.nodeName());
        assertEquals("https://www.example.com", docElement.baseUri());
        assertEquals("data", docElement.text());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("tag"), "https://www.example.com");
        Token.StartTag startTag = new Token.StartTag("tag");
        startTag.attributes().put("attr1", "value1");
        startTag.attributes().put("attr2", "value2");

        xmlTreeBuilder.process(startTag);

        Element insertedElement = xmlTreeBuilder.getDocument().child(0);
        assertEquals(element.nodeName(), insertedElement.nodeName());
        assertEquals(element.baseUri(), insertedElement.baseUri());
        assertEquals(element.attributes().size(), insertedElement.attributes().size());
        assertTrue(insertedElement.attributes().hasKey("attr1"));
        assertTrue(insertedElement.attributes().hasKey("attr2"));
        assertEquals("value1", insertedElement.attributes().get("attr1"));
        assertEquals("value2", insertedElement.attributes().get("attr2"));
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Document document = new Document("");
        Element element1 = new Element(Tag.valueOf("tag1"), "https://www.example.com");
        Element element2 = new Element(Tag.valueOf("tag2"), "https://www.example.com");
        document.appendChild(element1);
        document.appendChild(element2);
        Token.EndTag endTag = new Token.EndTag("tag1");

        xmlTreeBuilder.setDocument(document);
        xmlTreeBuilder.process(endTag);

        assertEquals(1, document.childNodeSize());
        assertEquals("tag2", document.child(0).nodeName());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Comment comment = new Comment("Comment", "https://www.example.com");
        Token.Comment commentToken = new Token.Comment("Comment");

        xmlTreeBuilder.process(commentToken);

        Comment insertedComment = (Comment) xmlTreeBuilder.getDocument().child(0);
        assertEquals(comment.getData(), insertedComment.getData());
        assertEquals(comment.baseUri(), insertedComment.baseUri());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        TextNode textNode = new TextNode("Text", "https://www.example.com");
        Token.Character characterToken = new Token.Character("Text");

        xmlTreeBuilder.process(characterToken);

        TextNode insertedTextNode = (TextNode) xmlTreeBuilder.getDocument().child(0);
        assertEquals(textNode.getWholeText(), insertedTextNode.getWholeText());
        assertEquals(textNode.baseUri(), insertedTextNode.baseUri());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        DocumentType documentType = new DocumentType("doctype", "", "", "https://www.example.com");
        Token.Doctype doctypeToken = new Token.Doctype("doctype", "", "", null);

        xmlTreeBuilder.process(doctypeToken);

        DocumentType insertedDoctype = (DocumentType) xmlTreeBuilder.getDocument().child(0);
        assertEquals(documentType.nodeName(), insertedDoctype.nodeName());
        assertEquals(documentType.publicId(), insertedDoctype.publicId());
        assertEquals(documentType.systemId(), insertedDoctype.systemId());
        assertEquals(documentType.baseUri(), insertedDoctype.baseUri());
    }

}