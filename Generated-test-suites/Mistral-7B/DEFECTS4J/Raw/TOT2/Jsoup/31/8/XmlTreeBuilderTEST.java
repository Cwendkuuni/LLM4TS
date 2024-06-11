
import org.junit.Test;
import org.jsoup.Helper;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        String input = "<xml></xml>";
        String baseUri = "http://example.com/";
        ParseErrorList errors = new ParseErrorList();
         XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
         xmlTreeBuilder.initialiseParse(input, baseUri, errors);
         Document doc = xmlTreeBuilder.getDocument();
         assertNotNull(doc);
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<test><tag attr='value'/></test>";
        Document doc = Jsoup.parse(xmlString);
        Element root = doc.getRootElement();
        Token startTag = root.child(0).child(0);
        xmlTreeBuilder.process(startTag);
        Element el = xmlTreeBuilder.getCurrentElement();
        assertEquals(Tag.TAG_TEST, el.tagName());
        assertEquals("value", el.attribute("attr"));
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<test><tag/></test>";
        Document doc = Jsoup.parse(xmlString);
        Element root = doc.getRootElement();
        Token endTag = root.child(0).child(1);
        xmlTreeBuilder.process(endTag);
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test(expected = AssertionError.class)
    public void testProcessUnexpectedTokenType() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token unexpectedToken = new Token(-1, Helper.decodeBase64("VG8gZXhpc3RfdGltZTogQ29udGVudDogQ29udGVudDogUmVzdWx0")); // base64 for <x:foo xmlns:x="http://example.com/">
        xmlTreeBuilder.process(unexpectedToken);
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element root = new Element("root");
        TextNode textNode = new TextNode("Hello World", null);
        xmlTreeBuilder.insertNode(textNode);
        assertEquals(textNode, xmlTreeBuilder.getCurrentElement().child(0));
    }

    @Test
    public void testInsertNodeForComplexCase() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element root = new Element("root");
        Element element1 = new Element("element1");
        Element element2 = new Element("element2");
        TextNode textNode1 = new TextNode("Hello World", null);
        TextNode textNode2 = new TextNode("Goodbye World", null);

        xmlTreeBuilder.insertNode(root);
        xmlTreeBuilder.insertNode(element1);
        xmlTreeBuilder.insertNode(textNode1);
        xmlTreeBuilder.insertNode(element2);
        xmlTreeBuilder.insertNode(textNode2);

        Element currentElement = xmlTreeBuilder.getCurrentElement();
        assertEquals(root, currentElement);
        currentElement = currentElement.child(0);
        assertEquals("element1", currentElement.nodeName());
        currentElement = currentElement.child(0);
        assertEquals("Hello World", currentElement.text());

        currentElement = currentElement.parent();
        assertEquals("element2", currentElement.child(0).nodeName());
        assertEquals("Goodbye World", currentElement.child(0).child(0).text());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Element root = new Element("root");
        root.appendChild(new Element("tag1"));
        root.appendChild(new Element("tag2"));
        xmlTreeBuilder.getCurrentElement().appendChild(root);

        Token endTag1 = new Token(Token.EndTag, "tag1");
        xmlTreeBuilder.process(endTag1);
        assertEquals(1, xmlTreeBuilder.stack.size());
        Element tag1 = (Element) xmlTreeBuilder.stack.peek();
        assertEquals("tag1", tag1.nodeName());

        Token endTag2 = new Token(Token.EndTag, "tag2");
        xmlTreeBuilder.process(endTag2);
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<test><tag><![CDATA[Hello]]></tag></test>";
        Document doc = Jsoup.parse(xmlString);
        Element root = doc.getRootElement();
        TextNode characterToken = root.child(0).child(0).child(0);
        xmlTreeBuilder.process(characterToken);
        assertEquals("Hello", xmlTreeBuilder.getCurrentElement().child(0).child(0).child(0).text());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<test><!-- This is a comment --></test>";
        Document doc = Jsoup.parse(xmlString);
        Element root = doc.getRootElement();
        Comment commentToken = root.child(0).child(0);
        xmlTreeBuilder.process(commentToken);
        assertEquals("This is a comment", xmlTreeBuilder.getCurrentElement().child(0).child(0).child(0).text());
    }

    @Test
    public void testProcessDocumentType() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String xmlString = "<!DOCTYPE test [ <!ENTITY test SYSTEM \"test.dtd\"> ]>";
        Document doc = Jsoup.parse(xmlString);
        Element root = doc.getRootElement();
        DocumentType documentTypeToken = root.child(0);
        xmlTreeBuilder.process(documentTypeToken);
        assertEquals("test", xmlTreeBuilder.getCurrentElement().nodeName());
        assertEquals("test.dtd", xmlTreeBuilder.getCurrentElement().getAttributes().get("SYSTEM"));
    }
}