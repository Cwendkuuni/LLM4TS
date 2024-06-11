import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.jsoup.helper.W3CDom;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        // Given a jsoup Document
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><h1>Hello</h1></body></html>");

        // When converting it to a W3C Document
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

        // Then the resulting W3C Document should not be null
        Assert.assertNotNull(w3cDocument);

        // And it should contain an h1 element
        Element h1Element = w3cDocument.getElementsByTagName("h1").item(0);
        Assert.assertNotNull(h1Element);
        Assert.assertEquals("Hello", h1Element.getTextContent());
    }

    @Test
    public void testConvert() {
        // Given a jsoup Document
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><h1>Hello</h1></body></html>");

        // And a W3C Document
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

        // When converting the jsoup Document to the existing W3C Document
        new W3CDom().convert(jsoupDocument, w3cDocument);

        // Then the W3C Document should not have changed
        Assert.assertEquals(w3cDocument.getDocumentElement().getTextContent(), jsoupDocument.text());
    }

    @Test
    public void testAsString() {
        // Given a W3C Document
        Document w3cDocument = new W3CDom().fromJsoup(org.jsoup.Jsoup.parse("<html><body><h1>Hello</h1></body></html>"));

        // When serializing it to a String
        String w3cString = new W3CDom().asString(w3cDocument);

        // Then the resulting String should match the original jsoup Document
        Assert.assertEquals(org.jsoup.Jsoup.parse(w3cString).text(), org.jsoup.Jsoup.parse("<html><body><h1>Hello</h1></body></html>").text());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullJsoupDocument() {
        // Given a null jsoup Document
        new W3CDom().fromJsoup(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullW3CDocument() {
        // Given a W3CDom instance
        W3CDom w3CDom = new W3CDom();

        // And a null W3C Document
        Document nullW3CDocument = null;

        // When trying to convert a jsoup Document to the null W3C Document
        w3CDom.convert(new org.jsoup.nodes.Document(), nullW3CDocument);

        // Then an IllegalArgumentException should be thrown
    }
}