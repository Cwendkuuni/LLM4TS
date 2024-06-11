package org.jsoup.helper;

import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextDocument;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.junit.Assert;
import org.junit.Test;

import javax.xml.parsers.ParserConfigurationException;

public class W3CDomTest {

    @Test
    public void testFromJsoup() throws ParserConfigurationException {
        Document jsoupDocument = new TextDocument("Test Document");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        // Basic functionality test
        Assert.assertNotNull(w3cDocument);
    }

    @Test
    public void testFromJsoupNullInput() {
        Assert.assertNull(new W3CDom().fromJsoup(null));
    }

    @Test
    public void testConvert() {
        Document jsoupDocument = new TextDocument("Test Document");
        Document w3cDocument = new Document();
        new W3CDom().convert(jsoupDocument, w3cDocument);
        // Basic functionality test
        Assert.assertNotNull(w3cDocument.getDocumentElement());
    }

    @Test
    public void testConvertNullInputs() {
        Assert.assertNull(new W3CDom().convert(null, null));
    }

    @Test
    public void testAsString() {
        Document w3cDocument = new W3CDom().fromJsoup(new TextDocument("Test Document"));
        String output = new W3CDom().asString(w3cDocument);
        // Basic functionality test
        Assert.assertNotNull(output);
        Assert.assertEquals("<TestDocument xmlns='http://www.w3.org/1999/xhtml'><TestDocument/></TestDocument>", output);
    }

    @Test
    public void testAsStringNullInput() {
        Assert.assertNull(new W3CDom().asString(null));
    }

    @Test
    public void testW3CBuilder() {
        Document w3cDocument = new Document();
        org.jsoup.nodes.Document jsoupDocument = new org.jsoup.nodes.Document();
        jsoupDocument.appendElement(new org.jsoup.nodes.Element("div"));
        jsoupDocument.getElementsByTag("div").first().appendText("Test");

        new W3CDom().convert(jsoupDocument, w3cDocument);
        Element divElement = (Element) w3cDocument.getDocumentElement().getElementsByTagName("div").item(0);
        Text textNode = (Text) divElement.getChildNodes().item(0);

        // Basic functionality test
        Assert.assertEquals("Test", textNode.getData());
    }

    @Test
    public void testW3CBuilderNullInputs() {
        Document w3cDocument = new Document();
        org.jsoup.nodes.Document jsoupDocument = null;

        new W3CDom().convert(jsoupDocument, w3cDocument);
        Assert.assertFalse(w3cDocument.getDocumentElement() != null);
    }

    @Test
    public void testUpdateNamespacesEmptyElement() {
        String prefix = new W3CDom().new W3CBuilder(new Document()).updateNamespaces(new org.jsoup.nodes.Element(""));
        Assert.assertEquals("", prefix);
    }

    @Test
    public void testUpdateNamespacesElementWithNamespace() {
        String prefix = new W3CDom().new W3CBuilder(new Document()).updateNamespaces(new org.jsoup.nodes.Element("xmlns:prefix=\"namespace\""));
        Assert.assertEquals("prefix", prefix);
    }

    @Test
    public void testUpdateNamespacesElementWithNamespaceAndTagName() {
        String prefix = new W3CDom().new W3CBuilder(new Document()).updateNamespaces(new org.jsoup.nodes.Element("prefix:tagName"));
        Assert.assertEquals("prefix", prefix);
    }

    @Test(expected = IllegalStateException.class)
    public void testUpdateNamespacesElementWithInvalidNamespace() {
        new W3CDom().new W3CBuilder(new Document()).updateNamespaces(new org.jsoup.nodes.Element("xmlnsInvalid:tagName"));
    }

    @Test
    public void testCopyAttributesSimpleElement() {
        org.jsoup.nodes.Element source = new org.jsoup.nodes.Element("div");
        source.attr("id", "test");

        Element dest = new Element("div");
        new W3CDom().new W3CBuilder(new Document()).copyAttributes(source, dest);

        // Basic functionality test
        Assert.assertEquals("test", dest.getAttribute("id"));
    }

    @Test
    public void testCopyAttributesElementWithNamespace() {
        org.jsoup.nodes.Element source = new org.jsoup.nodes.Element("xmlns:prefix:div");
        source.attr("id", "test");

        Element dest = new Element("div");
        new W3CDom().new W3CBuilder(new Document()).copyAttributes(source, dest);

        // Basic functionality test
        Assert.assertEquals("test", dest.getAttributeNS("xmlns:prefix", "id"));
    }
}