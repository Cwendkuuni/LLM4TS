import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        Assert.assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document w3cDoc = builder.newDocument();
            W3CDom w3cDom = new W3CDom();
            w3cDom.convert(jsoupDoc, w3cDoc);
            Assert.assertNotNull(w3cDoc);
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }

    @Test
    public void testW3CBuilderHead() {
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("tag");
        sourceEl.attr("attr", "value");
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        Element dest = w3cBuilder.doc.createElement("tag");
        w3cBuilder.dest = dest;

        w3cBuilder.head(sourceEl, 0);
        Assert.assertEquals("tag", dest.getTagName());
        Attr attr = (Attr) dest.getAttributes().item(0);
        Assert.assertEquals("attr", attr.getName());
        Assert.assertEquals("value", attr.getValue());
    }

    @Test
    public void testW3CBuilderTail() {
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("tag");
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element dest = doc.createElement("tag");
        doc.appendChild(dest);
        w3cBuilder.dest = dest;

        w3cBuilder.tail(sourceEl, 0);
        Node parentNode = dest.getParentNode();
        Assert.assertNull(parentNode);
    }

    @Test
    public void testW3CBuilderHeadTextNode() {
        org.jsoup.nodes.TextNode sourceText = new org.jsoup.nodes.TextNode("text");
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element dest = doc.createElement("tag");
        doc.appendChild(dest);
        w3cBuilder.dest = dest;

        w3cBuilder.head(sourceText, 0);
        Node childNode = dest.getFirstChild();
        Assert.assertEquals("text", childNode.getNodeValue());
    }

    @Test
    public void testW3CBuilderHeadComment() {
        org.jsoup.nodes.Comment sourceComment = new org.jsoup.nodes.Comment("comment");
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element dest = doc.createElement("tag");
        doc.appendChild(dest);
        w3cBuilder.dest = dest;

        w3cBuilder.head(sourceComment, 0);
        Node childNode = dest.getFirstChild();
        Assert.assertEquals("comment", childNode.getNodeValue());
    }

    @Test
    public void testW3CBuilderHeadDataNode() {
        org.jsoup.nodes.DataNode sourceData = new org.jsoup.nodes.DataNode("data", "value");
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element dest = doc.createElement("tag");
        doc.appendChild(dest);
        w3cBuilder.dest = dest;

        w3cBuilder.head(sourceData, 0);
        Node childNode = dest.getFirstChild();
        Assert.assertEquals("value", childNode.getNodeValue());
    }

    @Test
    public void testCopyAttributes() {
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("tag");
        sourceEl.attr("attr1", "value1");
        sourceEl.attr("attr2", "value2");
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);
        Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element dest = doc.createElement("tag");
        doc.appendChild(dest);

        w3cBuilder.copyAttributes(sourceEl, dest);
        Attr attr1 = (Attr) dest.getAttributes().getNamedItem("attr1");
        Attr attr2 = (Attr) dest.getAttributes().getNamedItem("attr2");
        Assert.assertEquals("value1", attr1.getValue());
        Assert.assertEquals("value2", attr2.getValue());
    }

    @Test
    public void testUpdateNamespaces() {
        org.jsoup.nodes.Element el = new org.jsoup.nodes.Element("tag");
        el.attr("xmlns", "http://www.w3.org/1999/xhtml");
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);

        String prefix = w3cBuilder.updateNamespaces(el);
        Assert.assertEquals("", prefix);
        Assert.assertEquals("http://www.w3.org/1999/xhtml", w3cBuilder.namespaces.get(prefix));
    }

    @Test
    public void testAsString() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document w3cDoc = builder.newDocument();
            Element element = w3cDoc.createElement("tag");
            w3cDoc.appendChild(element);

            W3CDom w3cDom = new W3CDom();
            String xmlString = w3cDom.asString(w3cDoc);
            Assert.assertNotNull(xmlString);
            Assert.assertTrue(xmlString.contains("<tag></tag>"));
        } catch (Exception e) {
            Assert.fail("Exception occurred: " + e.getMessage());
        }
    }

}