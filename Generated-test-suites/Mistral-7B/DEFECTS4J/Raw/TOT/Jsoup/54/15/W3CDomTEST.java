import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.W3CDom;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);

        // Test basic structure
        Assert.assertNotNull(w3cDocument);
        Assert.assertEquals("html", w3cDocument.getDocumentElement().getTagName());

        // Test body element
        Element body = w3cDocument.getDocumentElement().getElementsByTagName("body").item(0);
        Assert.assertNotNull(body);
        Assert.assertEquals("body", body.getTagName());

        // Test h1 element
        Element h1 = (Element) body.getElementsByTagName("h1").item(0);
        Assert.assertNotNull(h1);
        Assert.assertEquals("h1", h1.getTagName());
        Assert.assertEquals("Hello World", h1.getTextContent());

        // Test text node
        Text textNode = (Text) body.getElementsByTagName("text").item(0); // this will fail as there's no text node in the input
        Assert.assertNull(textNode);

        // Test comment node
        Comment commentNode = (Comment) body.getElementsByTagName("comment").item(0); // this will fail as there's no comment node in the input
        Assert.assertNull(commentNode);

        // Test serialization
        String w3cString = new W3CDom().asString(w3cDocument);
        Assert.assertNotNull(w3cString);
        Assert.assertTrue(w3cString.contains("<html>"));
        Assert.assertTrue(w3cString.contains("<body>"));
        Assert.assertTrue(w3cString.contains("<h1>Hello World</h1>"));
    }
}