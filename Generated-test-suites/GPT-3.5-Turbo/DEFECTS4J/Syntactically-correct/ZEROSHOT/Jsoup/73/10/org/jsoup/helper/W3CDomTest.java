import org.junit.Test;
import org.junit.Assert;
import org.w3c.dom.Document;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class W3CDomTest {
    @Test
    public void testFromJsoup() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        
        Assert.assertNotNull("W3C document should not be null", w3cDoc);
        Assert.assertEquals("Document root element should be 'html'", "html", w3cDoc.getDocumentElement().getNodeName());
    }
    
    @Test
    public void testConvert() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        w3cDom.convert(jsoupDoc, w3cDoc);
        
        Assert.assertNotNull("W3C document should not be null", w3cDoc);
        Assert.assertEquals("Document root element should be 'html'", "html", w3cDoc.getDocumentElement().getNodeName());
    }
    
    @Test
    public void testAsString() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse(html);
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String w3cString = w3cDom.asString(w3cDoc);
        
        Assert.assertNotNull("W3C document string should not be null", w3cString);
        Assert.assertTrue("Document string should contain '<html>'", w3cString.contains("<html>"));
        Assert.assertTrue("Document string should contain '<title>Test</title>'", w3cString.contains("<title>Test</title>"));
    }
}