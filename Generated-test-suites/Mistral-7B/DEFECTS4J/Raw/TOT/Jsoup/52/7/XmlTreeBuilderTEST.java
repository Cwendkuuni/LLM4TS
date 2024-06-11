import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testParseEmptyXml() throws IOException {
        String xml = "<root/>";
        Document doc = XmlTreeBuilderTest.parseXml(xml);
        Element root = doc.getRootElement();
        Assert.assertEquals("root", root.nodeName());
        Assert.assertTrue(root.children().isEmpty());
    }

    @Test
    public void testParseXmlWithAttributes() throws IOException {
        String xml = "<root attr1='value1' attr2='value2'/>";
        Document doc = XmlTreeBuilderTest.parseXml(xml);
        Element root = doc.getRootElement();
        List<Attribute> attributes = root.attributes();
        Assert.assertEquals(2, attributes.size());
        Assert.assertEquals("value1", attributes.get(0).getValue());
        Assert.assertEquals("value2", attributes.get(1).getValue());
    }

    @Test
    public void testParseXmlWithSelfClosingTag() throws IOException {
        String xml = "<img src='image.jpg'/>";
        Document doc = XmlTreeBuilderTest.parseXml(xml);
        Element img = doc.getElementsByTag("img").first();
        Assert.assertTrue(img.hasAttr("src"));
        Assert.assertTrue(img.children().isEmpty());
    }

    @Test
    public void testParseXmlWithNestedTags() throws IOException {
        String xml = "<root><child attr='value'><grandchild/></child></root>";
        Document doc = XmlTreeBuilderTest.parseXml(xml);
        Element root = doc.getRootElement();
        Element child = root.getElementsByTag("child").first();
        Element grandchild = child.getElementsByTagName("grandchild").first();
        Assert.assertEquals(3, doc.childNodes().size());
        Assert.assertEquals("child", child.nodeName());
        Assert.assertEquals("grandchild", grandchild.nodeName());
    }

    @Test
    public void testParseXmlWithComments() throws IOException {
        String xml = "<root><!-- comment --><child/></root>";
        Document doc = XmlTreeBuilderTest.parseXml(xml);
        List<Comment> comments = doc.body().getAllElements().stream()
                .filter(n -> n instanceof Comment)
                .map(n -> (Comment) n)
                .collect(Collectors.toList());
        Assert.assertEquals(1, comments.size());
        Assert.assertEquals("comment", comments.get(0).getData());
    }

    @Test(expected = IllegalStateException.class)
    public void testParseXmlWithMissingEndTag() throws IOException {
        String xml = "<root><child>"; // missing end tag
        Document doc = XmlTreeBuilderTest.parseXml(xml);
        doc.getElementsByTag("child").first();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseXmlWithInvalidXml() throws IOException {
        String xml = "<root><child<invalid>"; // invalid xml
        Document doc = XmlTreeBuilderTest.parseXml(xml);
    }

    private static Document parseXml(String xml) throws IOException {
        ParseErrorList errors = new ParseErrorList();
        Document doc = XmlTreeBuilder.parseFragment(xml, null, errors);
        if (errors.size() > 0) {
            throw new IOException("Errors parsing XML: " + errors.toString());
        }
        return doc;
    }
}