import org.jsoup.nodes.*;
import org.junit.Test;
import org.junit.Before;

import javax.xml.parsers.*;
import org.w3c.dom.*;

import static org.junit.Assert.*;

public class W3CDomTest {
    
    private W3CDom w3cDom;
    
    @Before
    public void setUp() {
        w3cDom = new W3CDom();
    }
    
    @Test
    public void testConvert() {
        String html = "<html><body><h1>Title</h1><div><p>Paragraph</p></div></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document w3cDoc = builder.newDocument();
            w3cDom.convert(jsoupDoc, w3cDoc);
            assertEquals("html", w3cDoc.getDocumentElement().getTagName());
            Element bodyElement = (Element) w3cDoc.getDocumentElement().getElementsByTagName("body").item(0);
            assertNotNull(bodyElement);
            Element divElement = (Element) bodyElement.getElementsByTagName("div").item(0);
            assertNotNull(divElement);
            Element pElement = (Element) divElement.getElementsByTagName("p").item(0);
            assertNotNull(pElement);
        } catch (ParserConfigurationException e) {
            fail("ParserConfigurationException occurred");
        }
    }
    
    @Test
    public void testFromJsoup() {
        String html = "<html><body><h1>Title</h1><div><p>Paragraph</p></div></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertEquals("html", w3cDoc.getDocumentElement().getTagName());
        Element bodyElement = (Element) w3cDoc.getDocumentElement().getElementsByTagName("body").item(0);
        assertNotNull(bodyElement);
        Element divElement = (Element) bodyElement.getElementsByTagName("div").item(0);
        assertNotNull(divElement);
        Element pElement = (Element) divElement.getElementsByTagName("p").item(0);
        assertNotNull(pElement);
    }
    
    @Test
    public void testCopyAttributes() {
        Element jsoupElement = new Element(Tag.valueOf("div"), "");
        jsoupElement.attr("id", "testid");
        jsoupElement.attr("class", "testclass");
        jsoupElement.attr("custom", "testcustom");
        Document w3cDoc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            w3cDoc = builder.newDocument();
        } catch (ParserConfigurationException e) {
            fail("ParserConfigurationException occurred");
        }
        Element w3cElement = w3cDoc.createElementNS("", "div");
        w3cDom.copyAttributes(jsoupElement, w3cElement);
        assertEquals("testid", w3cElement.getAttribute("id"));
        assertEquals("testclass", w3cElement.getAttribute("class"));
        assertEquals("testcustom", w3cElement.getAttribute("custom"));
    }
    
    @Test
    public void testUpdateNamespaces() {
        Element element = new Element(Tag.valueOf("div"), "");
        element.attr("xmlns", "uri");
        element.attr("xmlns:prefix", "uri2");
        W3CDom.W3CBuilder w3cBuilder = w3cDom.new W3CBuilder(null);
        String prefix = w3cBuilder.updateNamespaces(element);
        assertEquals("prefix", prefix);
        assertEquals("uri", w3cBuilder.namespacesStack.peek().get(""));
        assertEquals("uri2", w3cBuilder.namespacesStack.peek().get("prefix"));
    }
    
    @Test
    public void testAsString() {
        String html = "<html><body><h1>Title</h1><div><p>Paragraph</p></div></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String w3cString = w3cDom.asString(w3cDoc);
        assertTrue(w3cString.contains("<html>") && w3cString.contains("Title") && w3cString.contains("Paragraph"));
    }
}