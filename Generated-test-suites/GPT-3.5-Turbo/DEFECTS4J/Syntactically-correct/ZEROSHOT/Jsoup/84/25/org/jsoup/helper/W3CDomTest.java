import org.jsoup.Jsoup;
import org.jsoup.helper.W3CDom;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;

public class W3CDomTest {

    private W3CDom w3cDom;

    @Before
    public void setup() {
        w3cDom = new W3CDom();
    }

    @Test
    public void testFromJsoupNotNull() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head></head><body></body></html>");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        Assert.assertNotNull(w3cDoc);
    }

    @Test
    public void testConvertSetsDocumentURI() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head></head><body></body></html>");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        Assert.assertEquals("", w3cDoc.getDocumentURI());
    }

    @Test
    public void testConvertCreatesRootElement() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head></head><body></body></html>");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        Assert.assertEquals("html", w3cDoc.getDocumentElement().getTagName());
    }

    @Test
    public void testAsStringNotNull() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><head></head><body></body></html>");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String w3cString = w3cDom.asString(w3cDoc);
        Assert.assertNotNull(w3cString);
    }
}