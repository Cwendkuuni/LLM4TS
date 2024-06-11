package org.jsoup.helper;

import org.jsoup.nodes.Element;
import org.jsoup.select.NodeTraversor;
import org.jsoup.select.NodeVisitor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

public class W3CDomTest {
    private W3CDom w3CDom;
    private org.jsoup.nodes.Document jsoupDoc;
    private Document w3cDoc;

    @Before
    public void setUp() {
        w3CDom = new W3CDom();
        jsoupDoc = new org.jsoup.nodes.Document("");
        w3cDoc = createW3CDocument();
    }

    @Test
    public void testFromJsoup() {
        Document result = w3CDom.fromJsoup(jsoupDoc);

        Assert.assertNotNull(result);
        Assert.assertEquals(w3cDoc, result);
    }

    @Test
    public void testConvert() {
        Document result = createW3CDocument();
        w3CDom.convert(jsoupDoc, result);

        Assert.assertEquals(w3cDoc, result);
    }

    @Test
    public void testConvertWithLocation() {
        String location = "https://www.example.com";
        jsoupDoc.setBaseUri(location);
        Document result = createW3CDocument();
        w3CDom.convert(jsoupDoc, result);

        Assert.assertEquals(location, result.getDocumentURI());
    }

    @Test
    public void testW3CBuilderHeadElement() {
        Document doc = createW3CDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(doc);

        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");
        sourceEl.attributes().put("attr1", "value1");
        sourceEl.attributes().put("attr2", "value2");

        builder.head(sourceEl, 0);

        Element dest = (Element) doc.getFirstChild();
        Assert.assertEquals(sourceEl.tagName(), dest.getTagName());
        Assert.assertEquals(sourceEl.attributes().size(), dest.getAttributes().getLength());
        Assert.assertEquals(sourceEl.attributes().get("attr1"), dest.getAttribute("attr1"));
        Assert.assertEquals(sourceEl.attributes().get("attr2"), dest.getAttribute("attr2"));
    }

    @Test
    public void testW3CBuilderHeadTextNode() {
        Document doc = createW3CDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(doc);

        org.jsoup.nodes.TextNode sourceText = new org.jsoup.nodes.TextNode("textNode");

        builder.head(sourceText, 0);

        Text dest = (Text) doc.getFirstChild();
        Assert.assertEquals(sourceText.getWholeText(), dest.getWholeText());
    }

    @Test
    public void testW3CBuilderHeadComment() {
        Document doc = createW3CDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(doc);

        org.jsoup.nodes.Comment sourceComment = new org.jsoup.nodes.Comment("comment");

        builder.head(sourceComment, 0);

        org.w3c.dom.Comment dest = (org.w3c.dom.Comment) doc.getFirstChild();
        Assert.assertEquals(sourceComment.getData(), dest.getData());
    }

    @Test
    public void testW3CBuilderHeadDataNode() {
        Document doc = createW3CDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(doc);

        org.jsoup.nodes.DataNode sourceData = new org.jsoup.nodes.DataNode("dataNode");

        builder.head(sourceData, 0);

        Text dest = (Text) doc.getFirstChild();
        Assert.assertEquals(sourceData.getWholeData(), dest.getWholeData());
    }

    @Test
    public void testW3CBuilderTailElement() {
        Document doc = createW3CDocument();
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(doc);

        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("div");
        sourceEl.attributes().put("attr1", "value1");
        sourceEl.attributes().put("attr2", "value2");

        builder.head(sourceEl, 0);
        builder.tail(sourceEl, 0);

        Assert.assertEquals(doc, builder.dest);
    }

    private Document createW3CDocument() {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder;
        try {
            documentBuilder = documentBuilderFactory.newDocumentBuilder();
            return documentBuilder.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
            return null;
        }
    }
}