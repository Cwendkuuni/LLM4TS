
import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class W3CDomTest {
    W3CDom w3CDom = new W3CDom();
    
    @Test
    public void testFromJsoup() {
        Document jsoupDoc = Jsoup.parse("<html><body><h1>Title</h1></body></html>");
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        Element rootElement = w3cDoc.getDocumentElement();
        Assert.assertEquals("html", rootElement.getTagName());
    }
    
    @Test
    public void testConvert() {
        Document jsoupDoc = Jsoup.parse("<html><body><h1>Title</h1></body></html>");
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        org.w3c.dom.Document outputDoc = w3CDom.fromJsoup(jsoupDoc);
        w3CDom.convert(jsoupDoc, outputDoc);
        String w3cDocString = w3CDom.asString(w3cDoc);
        String outputDocString = w3CDom.asString(outputDoc);
        Assert.assertEquals(w3cDocString, outputDocString);
    }
    
    @Test
    public void testAsString() {
        Document jsoupDoc = Jsoup.parse("<html><body><h1>Title</h1></body></html>");
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        String w3cDocString = w3CDom.asString(w3cDoc);
        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><html><body><h1>Title</h1></body></html>", w3cDocString);
    }
}
