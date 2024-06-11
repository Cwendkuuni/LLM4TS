
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.NodeList;

import static org.hamcrest.core.IsNull.notNullValue;

public class W3CDomTest {

    @Test
    public void testFromJsoupValidDocument() {
        // Create a valid jsoup Document
        Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");
        W3CDom w3cDom = new W3CDom();

        // Convert the jsoup Document to a W3C Document
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Assert that the W3C Document is not null
        Assert.assertThat(w3cDoc, notNullValue());
    }

    @Test
    public void testFromJsoupNullDocument() {
        W3CDom w3cDom = new W3CDom();

        // Attempt to convert a null jsoup Document to a W3C Document
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(null);

        // Assert that the W3C Document is null
        Assert.assertNull(w3cDoc);
    }

    @Test
    public void testConvertValidDocument() {
        // Create a valid jsoup Document
        Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Create a valid W3C Document as the output
        org.w3c.dom.Document outDoc = w3cDom.factory.newDocumentBuilder().newDocument();

        // Convert the jsoup Document to the W3C Document
        w3cDom.convert(jsoupDoc, outDoc);

        // Assert that the output W3C Document is not null
        Assert.assertThat(outDoc, notNullValue());
    }

    @Test
    public void testConvertBlankLocation() {
        // Create a jsoup Document with blank location
        Document jsoupDoc = Jsoup.parse("<html><head><title>Test</title></head><body></body></html>");
        jsoupDoc.setBaseUri("");
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Create a valid W3C Document as the output
        org.w3c.dom.Document outDoc = w3cDom.factory.newDocumentBuilder().newDocument();

        // Convert the jsoup Document to the W3C Document
        w3cDom.convert(jsoupDoc, outDoc);

        // Assert that the output W3C Document's documentURI is blank
        Assert.assertEquals("", outDoc.getDocumentURI());
    }

    @Test
    public void testConvertNullDocument() {
        W3CDom w3cDom = new W3CDom();

        // Attempt to convert a null jsoup Document to a W3C Document
        org.w3c.dom.Document w3cDoc = w3cDom.factory.newDocumentBuilder().newDocument();

        // Assert that the output W3C Document is null
        Assert.assertNull(w3cDoc);
    }

    @Test
    public void testConvertEmptyDocument() {
        // Create an empty jsoup Document
        Document jsoupDoc = Jsoup.parse("");
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Create a valid W3C Document as the output
        org.w3c.dom.Document outDoc = w3cDom.factory.newDocumentBuilder().newDocument();

        // Convert the jsoup Document to the W3C Document
        w3cDom.convert(jsoupDoc, outDoc);
        
        // Assert that the output W3C Document is not null
        Assert.assertThat(outDoc, notNullValue());
    }

    @Test
    public void testAsStringValidDocument() {
        // Create a valid W3C Document
        org.w3c.dom.Document w3cDoc = createValidW3CDocument();
        W3CDom w3cDom = new W3CDom();

        // Convert the W3C Document to a String
        String w3cString = w3cDom.asString(w3cDoc);

        // Assert that the W3C Document String is not null
        Assert.assertNotNull(w3cString);
    }

    @Test
    public void testAsStringNullDocument() {
        W3CDom w3cDom = new W3CDom();

        // Attempt to convert a null W3C Document to a String
        String w3cString = w3cDom.asString(null);

        // Assert that the W3C Document String is null
        Assert.assertNull(w3cString);
    }

    private org.w3c.dom.Document createValidW3CDocument() {
        org.w3c.dom.Document doc = null;
        try {
            doc = w3cDom.factory.newDocumentBuilder().newDocument();
            org.w3c.dom.Element rootElement = doc.createElement("root");
            doc.appendChild(rootElement);
            org.w3c.dom.Element childElement = doc.createElement("child");
            rootElement.appendChild(childElement);
            return doc;
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }
}
