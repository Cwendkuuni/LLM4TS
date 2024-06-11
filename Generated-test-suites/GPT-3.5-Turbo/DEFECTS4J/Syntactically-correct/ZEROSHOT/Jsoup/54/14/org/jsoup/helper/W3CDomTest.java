import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import java.io.StringWriter;

public class W3CDomTest {

    private W3CDom w3cDom;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
    }

    @Test
    public void testFromJsoup() {
        Document jsoupDoc = Jsoup.parse("<html><body><div>Hello World</div></body></html>");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        Element rootElement = w3cDoc.getDocumentElement();
        Assert.assertEquals("html", rootElement.getTagName());
    }

    @Test
    public void testConvert() {
        Document jsoupDoc = Jsoup.parse("<html><body><div>Hello World</div></body></html>");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder;
        try {
            dBuilder = dbFactory.newDocumentBuilder();
            org.w3c.dom.Document expectedDoc = dBuilder.newDocument();
            W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(expectedDoc);
            org.jsoup.nodes.Element rootEl = jsoupDoc.child(0);
            w3cBuilder.head(rootEl, 0);
            StringWriter stringWriter = new StringWriter();
            DOMSource domSource = new DOMSource(w3cDoc);
            StreamResult streamResult = new StreamResult(stringWriter);
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.transform(domSource, streamResult);
            String expectedString = stringWriter.toString();
            String actualString = w3cDom.asString(expectedDoc);
            Assert.assertEquals(expectedString, actualString);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAsString() {
        Document jsoupDoc = Jsoup.parse("<html><body><div>Hello World</div></body></html>");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String w3cString = w3cDom.asString(w3cDoc);
        Assert.assertTrue(w3cString.contains("<html><body><div>Hello World</div></body></html>"));
    }
}