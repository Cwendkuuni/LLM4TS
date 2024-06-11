import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.xml.XmlDeclaration;
import org.jsoup.parser.tokenizer.CharSequenceToken;
import org.jsoup.parser.tokenizer.Comment;
import org.jsoup.parser.tokenizer.Doctype;
import org.jsoup.parser.tokenizer.EndTag;
import org.jsoup.parser.tokenizer.StartTag;
import org.jsoup.parser.tokenizer.Token;
import org.jsoup.parser.tokenizer.Tokenizer;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testParseXMLWithDefaultSettings() {
        String xml = "<root><element>Hello World</element></root>";
        Document doc = xmlTreeBuilder.parse(new StringReader(xml), null);

        assertNotNull(doc);
        Element root = doc.getRootElement();
        Element element = root.getChild(0);

        assertEquals("Hello World", element.text());
    }

    @Test
    public void testParseXMLWithEmptyInput() {
        Document doc = xmlTreeBuilder.parse(new StringReader(""), null);
        assertNotNull(doc);
        assertEquals(0, doc.childNodes().size());
    }

    @Test
    public void testParseXMLWithEmptyInputAndNameSpace() {
        Document doc = xmlTreeBuilder.parse(new StringReader(""), "http://example.com");
        assertNotNull(doc);
        assertEquals(0, doc.childNodes().size());
    }

    @Test
    public void testParseXMLWithComment() {
        String xml = "<root><!-- This is a comment --></root>";
        Document doc = xmlTreeBuilder.parse(new StringReader(xml), null);

        Element root = doc.getRootElement();
        Comment comment = root.getFirstMetaInformation();

        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testParseXMLWithSelfClosingTag() {
        String xml = "<img src=\"image.png\" />";
        Document doc = xmlTreeBuilder.parse(new StringReader(xml), null);

        Element img = doc.getElementsByTag("img").get(0);
        assertNotNull(img);
        assertEquals(0, img.childNodes().size());
    }

    @Test
    public void testParseXMLWithError() {
        String xml = "<root><element><script>alert('Hello World')</script></element></root>";
        ParseErrorList errors = new ParseErrorList();
        Document doc = xmlTreeBuilder.parse(new StringReader(xml), null, errors, null);

        assertNotNull(doc);
        assertEquals(1, errors.size());
        assertEquals("Error parsing XML: Unexpected character '<' at position 32: <root><element><script>alert('Hello World')</script></element></root>", errors.get(0).getMessage());
    }

    @Test
    public void testParseXMLWithCDataSection() {
        String xml = "<root><![CDATA[<script>alert('Hello World')</script>]]></root>";
        Document doc = xmlTreeBuilder.parse(new StringReader(xml), null);

        assertEquals("alert('Hello World')", doc.getElementsByTag("script").get(0).text());
    }

    @Test
    public void testParseXMLFragment() {
        String xmlFragment = "<element><subElement>Hello</subElement></element>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, null, null);

        Element element = (Element) nodes.get(0);
        Element subElement = (Element) element.getChild(0);

        assertEquals("Hello", subElement.text());
    }

    @Test
    public void testParseXMLFragmentWithBogusXMLDeclaration() {
        String xmlFragment = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><element><subElement>Hello</subElement></element>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, null, null);

        XmlDeclaration xmlDeclaration = (XmlDeclaration) nodes.get(0);
        Element element = (Element) nodes.get(1);
        Element subElement = (Element) element.getChild(0);

        assertEquals("Hello", subElement.text());
        assertEquals("1.0", xmlDeclaration.getVersion());
        assertEquals("UTF-8", xmlDeclaration.getEncoding());
    }

    @Test
    public void testParseXMLFragmentWithBogusComment() {
        String xmlFragment = "<!-- This is a comment --><element><subElement>Hello</subElement></element>";
        List<Node> nodes = xmlTreeBuilder.parseFragment(xmlFragment, null, null, null);

        Element element = (Element) nodes.get(1);
        Element subElement = (Element) element.getChild(0);

        assertEquals("Hello", subElement.text());
        assertNull(nodes.get(0));
    }
}