import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class W3CDomTest {
    private W3CDom w3CDom;
    private DocumentBuilder builder;

    @Before
    public void setUp() throws Exception {
        w3CDom = new W3CDom();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();
    }

    @Test
    public void testFromJsoupNotNull() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        Assert.assertNotNull(w3cDoc);
    }

    @Test(expected = IllegalStateException.class)
    public void testFromJsoupParserConfigurationException() throws Exception {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        builder.setEntityResolver((publicId, systemId) -> builder.newDocument());
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        org.w3c.dom.Document w3cDoc = builder.newDocument();
        w3CDom.convert(jsoupDoc, w3cDoc);
        Assert.assertNotNull(w3cDoc);
    }

    @Test
    public void testW3CBuilderHead() {
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(builder.newDocument());
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("test"), "");
        w3CBuilder.head(sourceEl, 0);
        Element element = w3CBuilder.dest;
        Assert.assertNotNull(element);
        Assert.assertEquals("test", element.getTagName());
    }

    @Test
    public void testW3CBuilderHeadTextNode() {
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(builder.newDocument());
        org.jsoup.nodes.TextNode sourceText = new org.jsoup.nodes.TextNode("test");
        w3CBuilder.dest = w3CBuilder.doc.createElement("test");
        w3CBuilder.head(sourceText, 0);
        Element element = w3CBuilder.dest;
        Assert.assertNotNull(element);
        Assert.assertEquals("test", element.getTextContent());
    }

    @Test
    public void testW3CBuilderHeadComment() {
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(builder.newDocument());
        org.jsoup.nodes.Comment sourceComment = new org.jsoup.nodes.Comment("test");
        w3CBuilder.dest = w3CBuilder.doc.createElement("test");
        w3CBuilder.head(sourceComment, 0);
        Element element = w3CBuilder.dest;
        Assert.assertNotNull(element);
        Assert.assertEquals("<!--test-->", element.getTextContent());
    }

    @Test
    public void testW3CBuilderHeadDataNode() {
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(builder.newDocument());
        org.jsoup.nodes.DataNode sourceData = new org.jsoup.nodes.DataNode("test", "");
        w3CBuilder.dest = w3CBuilder.doc.createElement("test");
        w3CBuilder.head(sourceData, 0);
        Element element = w3CBuilder.dest;
        Assert.assertNotNull(element);
        Assert.assertEquals("test", element.getTextContent());
    }

    @Test
    public void testW3CBuilderTail() {
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(builder.newDocument());
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("test"), "");
        w3CBuilder.head(sourceEl, 0);
        w3CBuilder.tail(sourceEl, 0);
        Element element = w3CBuilder.dest;
        Assert.assertNull(element.getParentNode());
    }

    @Test
    public void testCopyAttributes() {
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(builder.newDocument());
        org.jsoup.nodes.Element source = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("test1"), "");
        org.jsoup.nodes.Element dest = w3CBuilder.doc.createElement("test2");

        source.attr("attr1", "value1");
        source.attr("attr2", "value2");
        w3CBuilder.copyAttributes(source, dest);

        Assert.assertEquals("value1", dest.getAttribute("attr1"));
        Assert.assertEquals("value2", dest.getAttribute("attr2"));
    }

    @Test
    public void testUpdateNamespaces() {
        W3CDom.W3CBuilder w3CBuilder = new W3CDom.W3CBuilder(builder.newDocument());
        org.jsoup.nodes.Element el = new org.jsoup.nodes.Element(org.jsoup.parser.Tag.valueOf("test"), "");
        el.attr("xmlns", "http://www.example.com");
        el.attr("xmlns:prefix", "http://www.example2.com");

        String prefix = w3CBuilder.updateNamespaces(el);

        Assert.assertEquals("prefix", prefix);
        Assert.assertEquals("http://www.example.com", w3CBuilder.namespaces.get(""));
        Assert.assertEquals("http://www.example2.com", w3CBuilder.namespaces.get("prefix"));
    }

    @Test
    public void testAsString() {
        Document doc = builder.newDocument();
        String result = w3CDom.asString(doc);

        Assert.assertNotNull(result);
    }
}