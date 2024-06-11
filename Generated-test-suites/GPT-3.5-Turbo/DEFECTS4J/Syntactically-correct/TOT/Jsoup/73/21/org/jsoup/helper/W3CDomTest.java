import org.jsoup.Jsoup;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class W3CDomTest {

    private W3CDom w3cDom;
    private org.jsoup.nodes.Document jsoupDoc;

    @Test
    public void testFromJsoup_ValidInput_ReturnsValidW3CDocument() {
        // Test with valid jsoup document
        jsoupDoc = Jsoup.parse("<html><head></head><body></body></html>");
        w3cDom = new W3CDom();

        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        Element rootElement = w3cDoc.getDocumentElement();
        Assert.assertNotNull(rootElement);
        Assert.assertEquals("html", rootElement.getTagName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoup_NullInput_ThrowsIllegalArgumentException() {
        // Test with null jsoup document
        jsoupDoc = null;
        w3cDom = new W3CDom();

        w3cDom.fromJsoup(jsoupDoc);
    }

    @Test(expected = IllegalStateException.class)
    public void testFromJsoup_ParserConfigurationException_ThrowsIllegalStateException() {
        // Test with ParserConfigurationException
        jsoupDoc = Jsoup.parse("<html><head></head><body></body></html>");
        w3cDom = new W3CDom();
        w3cDom.factory.setNamespaceAware(false);

        w3cDom.fromJsoup(jsoupDoc);
    }

    @Test
    public void testConvert_ValidInput_ReturnsConvertedW3CDocument() {
        // Test with valid jsoup document and w3c document
        jsoupDoc = Jsoup.parse("<html><head></head><body></body></html>");
        w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        Document out = w3cDom.factory.newDocumentBuilder().newDocument();
        w3cDom.convert(jsoupDoc, out);

        Element rootElement = out.getDocumentElement();
        Assert.assertNotNull(rootElement);
        Assert.assertEquals("html", rootElement.getTagName());
    }

    @Test
    public void testAsString_ValidDocument_ReturnsString() {
        // Test with valid w3c document
        jsoupDoc = Jsoup.parse("<html><head></head><body></body></html>");
        w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String expected = "<html><head></head><body></body></html>";

        String result = w3cDom.asString(w3cDoc);

        Assert.assertEquals(expected, result);
    }

    @Test(expected = IllegalStateException.class)
    public void testAsString_TransformerException_ThrowsIllegalStateException() {
        // Test with TransformerException
        jsoupDoc = Jsoup.parse("<html><head></head><body></body></html>");
        w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        w3cDom.factory.setNamespaceAware(true);
        w3cDom.asString(w3cDoc);
    }
}