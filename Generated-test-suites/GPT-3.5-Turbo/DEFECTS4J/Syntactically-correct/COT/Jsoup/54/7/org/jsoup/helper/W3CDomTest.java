import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import java.io.StringWriter;
import java.util.HashMap;

public class W3CDomTest {
    private W3CDom w3cDom;
    private org.jsoup.nodes.Document jsoupDoc;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
        jsoupDoc = new org.jsoup.nodes.Document("");
    }

    @Test
    public void testFromJsoup() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        Assert.assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        Document out = w3cDom.convert(jsoupDoc, w3cDoc);
        Assert.assertNotNull(out);
    }

    @Test
    public void testAsString() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String output = w3cDom.asString(w3cDoc);
        Assert.assertNotNull(output);
    }

    @Test
    public void testW3CBuilderHead() {
        Element element = new Element("test");
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(jsoupDoc);
        builder.head(element, 0);
        Assert.assertEquals(element.tagName(), jsoupDoc.child(0).tagName());
    }

    @Test
    public void testW3CBuilderTail() {
        Element element = new Element("test");
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(jsoupDoc);
        builder.head(element, 0);
        builder.tail(element, 0);
        Assert.assertNull(jsoupDoc.child(0));
    }

    @Test
    public void testW3CBuilderHeadTextNode() {
        Text textNode = new Text("test");
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(jsoupDoc);
        builder.head(textNode, 0);
        Assert.assertEquals(textNode.getWholeText(), jsoupDoc.child(0).text());
    }

    @Test
    public void testW3CBuilderHeadComment() {
        Comment comment = new Comment("test");
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(jsoupDoc);
        builder.head(comment, 0);
        Assert.assertEquals(comment.getData(), jsoupDoc.child(0).data());
    }

    @Test
    public void testW3CBuilderHeadDataNode() {
        org.jsoup.nodes.DataNode dataNode = new org.jsoup.nodes.DataNode("test", "");
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(jsoupDoc);
        builder.head(dataNode, 0);
        Assert.assertEquals(dataNode.getWholeData(), jsoupDoc.child(0).data());
    }

    @Test
    public void testW3CBuilderCopyAttributes() {
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("test");
        org.jsoup.nodes.Attribute attribute = new org.jsoup.nodes.Attribute("attr", "value");
        jsoupElement.attributes().put(attribute);
        Element w3cElement = jsoupDoc.createElementNS("", "test");
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(jsoupDoc);
        builder.copyAttributes(jsoupElement, w3cElement);
        Assert.assertEquals(attribute.getValue(), w3cElement.getAttribute(attribute.getKey()));
    }

    @Test
    public void testW3CBuilderUpdateNamespaces() {
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("test");
        org.jsoup.nodes.Attribute attribute1 = new org.jsoup.nodes.Attribute("xmlns", "value");
        org.jsoup.nodes.Attribute attribute2 = new org.jsoup.nodes.Attribute("xmlns:prefix", "value");
        jsoupElement.attributes().put(attribute1);
        jsoupElement.attributes().put(attribute2);
        Element w3cElement = jsoupDoc.createElementNS("", "test");
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(jsoupDoc);
        String prefix = builder.updateNamespaces(jsoupElement);
        Assert.assertEquals(attribute2.getKey().substring(W3CDom.W3CBuilder.xmlnsPrefix.length()), prefix);
        Assert.assertEquals(attribute1.getValue(), builder.namespaces.get(""));
        Assert.assertEquals(attribute2.getValue(), builder.namespaces.get(prefix));
    }

}