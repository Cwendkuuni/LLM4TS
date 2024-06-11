import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Comment;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.w3c.dom.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.StringWriter;
import java.util.HashMap;

public class W3CDomTest {

    private static W3CDom w3cDom;

    @BeforeClass
    public static void setUp() {
        w3cDom = new W3CDom();
    }

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        Assert.assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        jsoupDoc.appendElement("html");
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(w3cDoc);
        w3cBuilder.head(jsoupDoc.child(0), 0);

        Assert.assertNotNull(w3cDoc);
    }

    @Test
    public void testCopyAttributes() {
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("tag", "");
        Element w3cElement = w3cDom.fromJsoup(jsoupElement);

        w3cDom.copyAttributes(jsoupElement, w3cElement);

        Assert.assertEquals(0, jsoupElement.attributes().size());
        Assert.assertEquals(0, w3cElement.getAttributes().getLength());
    }

    @Test
    public void testUpdateNamespaces() {
        org.jsoup.nodes.Element jsoupElement = new org.jsoup.nodes.Element("tag", "");
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(null);

        String prefix = w3cBuilder.updateNamespaces(jsoupElement);

        Assert.assertEquals("", prefix);
    }

    @Test
    public void testAsString() throws ParserConfigurationException, TransformerException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document w3cDoc = builder.newDocument();
        Element root = w3cDoc.createElement("root");
        w3cDoc.appendChild(root);
        String expectedString = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><root></root>";

        String resultString = w3cDom.asString(w3cDoc);

        Assert.assertEquals(expectedString, resultString);
    }
}