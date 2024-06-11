
package org.jsoup.helper;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><body><div><p>Hello World</p></div></body></html>");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        Assert.assertEquals("HTML", w3cDoc.getDocumentElement().getTagName());
        Assert.assertEquals("BODY", w3cDoc.getDocumentElement().getChildNodes().item(0).getNodeName());
        
        NodeList nodeList = w3cDoc.getElementsByTagName("P");
        Assert.assertEquals(1, nodeList.getLength());
        
        Element element = (Element) nodeList.item(0);
        Assert.assertEquals("Hello World", element.getTextContent());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><body><div><p>Hello World</p></div></body></html>");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        Document convertedDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        w3cDom.convert(jsoupDoc, convertedDoc);
        
        Assert.assertEquals("HTML", convertedDoc.getDocumentElement().getTagName());
        Assert.assertEquals("BODY", convertedDoc.getDocumentElement().getChildNodes().item(0).getNodeName());
        
        NodeList nodeList = convertedDoc.getElementsByTagName("P");
        Assert.assertEquals(1, nodeList.getLength());
        
        Element element = (Element) nodeList.item(0);
        Assert.assertEquals("Hello World", element.getTextContent());
    }

    @Test
    public void testW3CBuilder() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><body><div><p>Hello World</p></div></body></html>");
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.Element rootEl = jsoupDoc.child(0);
        w3cBuilder.head(rootEl, 0);
        
        Assert.assertEquals("HTML", w3cDoc.getDocumentElement().getTagName());
        Assert.assertEquals("BODY", w3cDoc.getDocumentElement().getChildNodes().item(0).getNodeName());
        
        NodeList nodeList = w3cDoc.getElementsByTagName("P");
        Assert.assertEquals(1, nodeList.getLength());
        
        Element element = (Element) nodeList.item(0);
        Assert.assertEquals("Hello World", element.getTextContent());
    }

    @Test
    public void testCopyAttributes() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<div id=\"div1\" class=\"class1\"></div>");
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.Element sourceEl = (org.jsoup.nodes.Element) jsoupDoc.child(0);
        Element el = w3cDoc.createElement("DIV");
        
        w3cBuilder.copyAttributes(sourceEl, el);
        
        NamedNodeMap attributes = el.getAttributes();
        Assert.assertEquals(2, attributes.getLength());
        
        List<String> attributeList = new ArrayList<>();
        for (int i = 0; i < attributes.getLength(); i++) {
            attributeList.add(attributes.item(i).getTextContent());
        }
        
        Assert.assertTrue(attributeList.contains("id=\"div1\""));
        Assert.assertTrue(attributeList.contains("class=\"class1\""));
    }

    @Test
    public void testUpdateNamespaces() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html xmlns:ns=\"http://www.example.com\"></html>");
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);

        org.jsoup.nodes.Element sourceEl = (org.jsoup.nodes.Element) jsoupDoc.child(0);
        String prefix = w3cBuilder.updateNamespaces(sourceEl);
        
        Assert.assertEquals("html", sourceEl.tagName());
        Assert.assertEquals("ns", prefix);
        
        String xmlnsAttrValue = sourceEl.attr("xmlns:ns");
        Assert.assertEquals("http://www.example.com", xmlnsAttrValue);
    }

    @Test
    public void testAsString() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><body><div><p>Hello World</p></div></body></html>");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        String xmlString = w3cDom.asString(w3cDoc);
        
        Assert.assertTrue(xmlString.contains("<html>"));
        Assert.assertTrue(xmlString.contains("<body>"));
        Assert.assertTrue(xmlString.contains("<div>"));
        Assert.assertTrue(xmlString.contains("<p>Hello World</p>"));
        Assert.assertTrue(xmlString.contains("</div>"));
        Assert.assertTrue(xmlString.contains("</body>"));
        Assert.assertTrue(xmlString.contains("</html>"));
    }
}
