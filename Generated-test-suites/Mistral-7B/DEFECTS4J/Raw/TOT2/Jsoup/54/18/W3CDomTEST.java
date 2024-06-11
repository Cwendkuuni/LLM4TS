import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.w3c.dom.Document;

public class W3CDomTest {
    @Test
    public void testFromJsoup() {
        // Given a jsoup document
        Document jsoupDoc = org.jsoup.Jsoup.parse("<html><body><p>Hello World</p></body></html>");

        // When converting the jsoup document to a w3c document
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        // Then the w3c document should not be null
        Assert.assertNotNull(w3cDoc);

        // Check functionality
        Assert.assertEquals("html", w3cDoc.getDocumentElement().getTagName());
        Assert.assertEquals("body", w3cDoc.getDocumentElement().getElementsByTagName("body").item(0).getTagName());
        Assert.assertEquals("p", w3cDoc.getDocumentElement().getElementsByTagName("p").item(0).getTagName());
        Assert.assertEquals("Hello World", w3cDoc.getDocumentElement().getElementsByTagName("p").item(0).getTextContent());

        // Edge cases and exception handling
        Document emptyJsoupDoc = org.jsoup.Jsoup.parse("");
        Assert.assertThrows(IllegalStateException.class, () -> new W3CDom().fromJsoup(emptyJsoupDoc));
        Document malformedJsoupDoc = org.jsoup.Jsoup.parse("<html><body><p><script>alert('Hello World')</script>");
        Assert.assertThrows(ParserConfigurationException.class, () -> new W3CDom().fromJsoup(malformedJsoupDoc));
    }
}