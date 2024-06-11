import org.junit.Before;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Comment;

import java.util.List;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testParseEmptyXml() {
        String xml = "<>";
        Document doc = xmlTreeBuilder.parse(xml, "baseUrl");
        List<Node> nodes = doc.childNodes();
        assertEquals(0, nodes.size());
    }

    @Test
    public void testParseXmlWithRootElement() {
        String xml = "<root></root>";
        Document doc = xmlTreeBuilder.parse(xml, "baseUrl");
        List<Node> nodes = doc.childNodes();
        Element root = (Element) nodes.get(0);
        assertEquals("root", root.nodeName());
        assertEquals(0, root.childNodes().size());
    }

    @Test
    public void testParseXmlWithElementsAndAttributes() {
        String xml = "<element attribute1='value1'><subElement attribute2='value2'/></element>";
        Document doc = xmlTreeBuilder.parse(xml, "baseUrl");
        List<Node> nodes = doc.childNodes();
        Element element = (Element) nodes.get(0);
        assertEquals("element", element.nodeName());
        assertNotNull(element.attribute("attribute1"));
        assertEquals("value1", element.attribute("attribute1").getValue());
        List<Node> subElementNodes = element.childNodes();
        Element subElement = (Element) subElementNodes.get(0);
        assertEquals("subElement", subElement.nodeName());
        assertNotNull(subElement.attribute("attribute2"));
        assertEquals("value2", subElement.attribute("attribute2").getValue());
    }

    @Test
    public void testParseXmlWithSelfClosingTag() {
        String xml = "<img src='image.jpg'/>";
        Document doc = xmlTreeBuilder.parse(xml, "baseUrl");
        List<Node> nodes = doc.childNodes();
        Element img = (Element) nodes.get(0);
        assertEquals("img", img.nodeName());
        assertSame(Element.Tag.IMG, img.tag());
        assertTrue(img.isSelfClosing());
        assertEquals(0, img.childNodes().size());
    }

    @Test
    public void testParseXmlWithComment() {
        String xml = "<!-- This is a comment -->";
        Document doc = xmlTreeBuilder.parse(xml, "baseUrl");
        List<Node> nodes = doc.childNodes();
        Comment comment = (Comment) nodes.get(0);
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testParseXmlWithDoctype() {
        String xml = "<!DOCTYPE html SYSTEM 'about:legacy-compat'>";
        Document doc = xmlTreeBuilder.parse(xml, "baseUrl");
        List<Node> nodes = doc.childNodes();
        DocumentType doctype = (DocumentType) nodes.get(0);
        assertEquals("html", doctype.nodeName());
        assertEquals("about:legacy-compat", doctype.getSystemIdentifier());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseXmlWithInvalidInput() {
        String xml = "<invalid>";
        xmlTreeBuilder.parse(xml, "baseUrl");
        fail("Expected IllegalArgumentException");
    }

    @Test
    public void testParseXmlWithErrorHandling() {
        Set<String> errors = new HashSet<>();
        ParseErrorList parseErrorList = new ParseErrorList() {
            @Override
            public void add(ParseError error) {
                errors.add(error.getMessage());
            }
        };
        String xml = "<tag error='error'>Content</tag>";
        Document doc = xmlTreeBuilder.parse(xml, "baseUrl", parseErrorList, xmlTreeBuilder.defaultSettings());
        List<Node> nodes = doc.childNodes();
        Element tag = (Element) nodes.get(0);
        assertEquals("tag", tag.nodeName());
        assertNotNull(tag.attribute("error"));
        assertEquals("error", tag.attribute("error").getValue());
        assertEquals(1, errors.size());
        assertEquals("Error parsing tag 'tag': Error at [row,col]:[1,1] : Error:<tag error='error'>Content</tag>: Line 1, column 1: parser error : Error: Tag 'tag' not known", errors.iterator().next());
    }
}