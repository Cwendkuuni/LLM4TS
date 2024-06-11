import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        W3CDom domConverter = new W3CDom();
        Document w3cDoc = domConverter.fromJsoup(jsoupDoc);
        Element rootElement = w3cDoc.getDocumentElement();
        Assert.assertEquals("html",rootElement.getTagName());
    }

    @Test
    public void testConvert() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document w3cDoc = builder.newDocument();

            W3CDom domConverter = new W3CDom();
            domConverter.convert(jsoupDoc, w3cDoc);
            
            Element rootElement = w3cDoc.getDocumentElement();
            Assert.assertEquals("html",rootElement.getTagName());
            NodeList nodeList = w3cDoc.getElementsByTagName("h1");
            Assert.assertEquals(1, nodeList.getLength());
            Element h1Element = (Element) nodeList.item(0);
            Assert.assertEquals("Hello, World!", h1Element.getTextContent());
        } catch (Exception e) {
            Assert.fail("Exception: " + e.getMessage());
        }
    }

    @Test
    public void testAsString() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        Document jsoupDoc = Jsoup.parse(html);
        W3CDom domConverter = new W3CDom();
        Document w3cDoc = domConverter.fromJsoup(jsoupDoc);
        String xmlString = domConverter.asString(w3cDoc);
        Assert.assertTrue(xmlString.contains("<h1>Hello, World!</h1>"));
    }
}