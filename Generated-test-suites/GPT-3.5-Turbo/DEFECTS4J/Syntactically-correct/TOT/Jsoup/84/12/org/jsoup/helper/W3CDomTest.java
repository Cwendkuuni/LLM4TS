import org.junit.Test;
import org.junit.Assert;
import org.jsoup.helper.W3CDom;
import org.w3c.dom.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class W3CDomTest {
    
    // Test case for fromJsoup() method
    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><body><div id=\"test\">Hello World</div></body></html>");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        Assert.assertNotNull(w3cDoc);
        Element body = jsoupDoc.body();
        Element testDiv = body.getElementById("test");
        Element w3cBody = w3cDoc.getDocumentElement().getElementsByTagName("body").item(0);
        Element w3cTestDiv = w3cBody.getElementById("test");
        
        Assert.assertEquals(testDiv.html(), w3cTestDiv.getTextContent());
    }
    
    // Test case for convert() method
    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><body><div id=\"test\">Hello World</div></body></html>");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        Document out = w3cDom.convert(jsoupDoc, w3cDoc);
        
        // Assert that the converted document has the same content as the original document
        Assert.assertNotNull(out);
        Assert.assertEquals(w3cDoc, out);
    }
    
    // Test case for W3CBuilder.head() method
    @Test
    public void testW3CBuilderHead() {
        org.jsoup.nodes.Element jsoupElement = Jsoup.parse("<div id=\"test\">Hello World</div>").body();
        Document w3cDoc = Jsoup.parse("<div></div>");
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        builder.head(jsoupElement, 0);

        Element root = w3cDoc.getDocumentElement();
        
        // Assert that the root element has the same id as the jsoupElement
        Assert.assertNotNull(root);
        Assert.assertEquals(jsoupElement.id(), root.getAttribute("id"));
    }
    
    // Test case for W3CBuilder.tail() method
    @Test
    public void testW3CBuilderTail() {
        org.jsoup.nodes.Element jsoupElement = Jsoup.parse("<div></div>").body();
        Document w3cDoc = Jsoup.parse("<div></div>");
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        builder.head(jsoupElement, 0);
        builder.tail(jsoupElement, 0);

        Element root = w3cDoc.getDocumentElement();

        // Assert that the dest element is the root element after calling tail()
        Assert.assertNotNull(root);
        Assert.assertEquals(root, builder.dest);
    }
    
    // Test case for asString() method
    @Test
    public void testAsString() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><body><div id=\"test\">Hello World</div></body></html>");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        String w3cString = w3cDom.asString(w3cDoc);
        
        // Assert that the generated string is not null
        Assert.assertNotNull(w3cString);
        // Assert that the generated string contains the content of the jsoup document
        Assert.assertTrue(w3cString.contains(jsoupDoc.toString()));
    }
}