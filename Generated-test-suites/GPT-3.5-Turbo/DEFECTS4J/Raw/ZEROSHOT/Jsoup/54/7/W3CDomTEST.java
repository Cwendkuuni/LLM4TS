import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;

import static org.junit.Assert.*;

public class W3CDomTest {
    private W3CDom w3CDom;

    @Before
    public void setup() {
        w3CDom = new W3CDom();
    }

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);

        org.jsoup.nodes.Element jsoupRootEl = new org.jsoup.nodes.Element("root");
        jsoupDoc.appendChild(jsoupRootEl);

        Element w3cRootEl = w3cDoc.createElementNS("", "root");
        w3cDoc.appendChild(w3cRootEl);

        w3CDom.convert(jsoupDoc, w3cDoc);

        NodeList w3cDocChildren = w3cDoc.getChildNodes();
        assertEquals(1, w3cDocChildren.getLength());

        Node w3cRootElChild = w3cDocChildren.item(0);
        assertEquals("root", w3cRootElChild.getNodeName());
    }

    @Test
    public void testW3CBuilderHead_Element() {
        Document w3cDoc = new org.w3c.dom.Document();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element("test");
        org.jsoup.nodes.Attributes jsoupAttrs = new org.jsoup.nodes.Attributes();
        jsoupAttrs.put("attr", "value");
        jsoupEl.attributes().addAll(jsoupAttrs);

        builder.head(jsoupEl, 0);

        NodeList w3cDocChildren = w3cDoc.getChildNodes();
        assertEquals(1, w3cDocChildren.getLength());

        Node w3cEl = w3cDocChildren.item(0);
        assertEquals("test", w3cEl.getNodeName());

        Attr w3cAttr = ((Element) w3cEl).getAttributeNode("attr");
        assertNotNull(w3cAttr);
        assertEquals("value", w3cAttr.getValue());
    }

    @Test
    public void testW3CBuilderHead_TextNode() {
        Document w3cDoc = new org.w3c.dom.Document();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.TextNode jsoupTextNode = new org.jsoup.nodes.TextNode("text", "");
        builder.head(jsoupTextNode, 0);

        NodeList w3cDocChildren = w3cDoc.getChildNodes();
        assertEquals(1, w3cDocChildren.getLength());

        Node w3cTextNode = w3cDocChildren.item(0);
        assertEquals("text", w3cTextNode.getNodeValue());
    }

    @Test
    public void testW3CBuilderHead_Comment() {
        Document w3cDoc = new org.w3c.dom.Document();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.Comment jsoupComment = new org.jsoup.nodes.Comment("comment");
        builder.head(jsoupComment, 0);

        NodeList w3cDocChildren = w3cDoc.getChildNodes();
        assertEquals(1, w3cDocChildren.getLength());

        Node w3cComment = w3cDocChildren.item(0);
        assertEquals("comment", w3cComment.getNodeValue());
    }

    @Test
    public void testW3CBuilderHead_DataNode() {
        Document w3cDoc = new org.w3c.dom.Document();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.DataNode jsoupDataNode = new org.jsoup.nodes.DataNode("data", "");
        builder.head(jsoupDataNode, 0);

        NodeList w3cDocChildren = w3cDoc.getChildNodes();
        assertEquals(1, w3cDocChildren.getLength());

        Node w3cDataNode = w3cDocChildren.item(0);
        assertEquals("data", w3cDataNode.getNodeValue());
    }

    @Test
    public void testW3CBuilderTail_Element() {
        Document w3cDoc = new org.w3c.dom.Document();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element("test");
        org.jsoup.nodes.Attributes jsoupAttrs = new org.jsoup.nodes.Attributes();
        jsoupAttrs.put("attr", "value");
        jsoupEl.attributes().addAll(jsoupAttrs);

        builder.head(jsoupEl, 0);
        builder.tail(jsoupEl, 1);

        assertEquals(w3cDoc, builder.doc);
    }

    @Test
    public void testCopyAttributes() {
        Document w3cDoc = new org.w3c.dom.Document();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element("test");
        org.jsoup.nodes.Attributes jsoupAttrs = new org.jsoup.nodes.Attributes();
        jsoupAttrs.put("attr", "value");
        jsoupEl.attributes().addAll(jsoupAttrs);

        Element w3cEl = w3cDoc.createElement("test");

        builder.copyAttributes(jsoupEl, w3cEl);

        Attr w3cAttr = w3cEl.getAttributeNode("attr");
        assertNotNull(w3cAttr);
        assertEquals("value", w3cAttr.getValue());
    }

    @Test
    public void testUpdateNamespaces() {
        org.jsoup.nodes.Element jsoupEl = new org.jsoup.nodes.Element("test");
        jsoupEl.attr("xmlns", "urn:test");

        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(new org.w3c.dom.Document());
        String prefix = builder.updateNamespaces(jsoupEl);

        assertEquals("", prefix);
        assertEquals(1, builder.namespaces.size());
        assertEquals("urn:test", builder.namespaces.get(""));
    }

    @Test
    public void testAsString() {
        Document w3cDoc = new org.w3c.dom.Document();
        Element root = w3cDoc.createElement("root");
        Text text = w3cDoc.createTextNode("Test Content");
        root.appendChild(text);
        w3cDoc.appendChild(root);

        String result = w3CDom.asString(w3cDoc);

        assertTrue(result.contains("<root>Test Content</root>"));
    }
}