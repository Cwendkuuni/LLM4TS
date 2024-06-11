import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class W3CDomTest {
    private Document jsoupDocument;

    @Before
    public void setup() {
        File input = new File("test.html");
        try {
            jsoupDocument = Jsoup.parse(input, "UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFromJsoupNotNull() {
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);
        Assert.assertNotNull(w3cDocument);
    }

    @Test
    public void testConvertLocation() {
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);
        Assert.assertEquals(jsoupDocument.location(), w3cDocument.getDocumentURI());
    }

    @Test
    public void testConvertTags() {
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);
        String jsoupHtml = jsoupDocument.html();
        String w3cHtml = w3cDom.asString(w3cDocument);
        Assert.assertEquals(jsoupHtml, w3cHtml);
    }

    @Test
    public void testConvertAttributes() {
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);
        String jsoupHtml = jsoupDocument.html();
        String w3cHtml = w3cDom.asString(w3cDocument);
        Assert.assertEquals(jsoupHtml, w3cHtml);
    }

    @Test
    public void testAsStringNotNull() {
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);
        String w3cHtml = w3cDom.asString(w3cDocument);
        Assert.assertNotNull(w3cHtml);
    }

    @Test
    public void testAsStringCorrect() {
        W3CDom w3cDom = new W3CDom();
        org.w3c.dom.Document w3cDocument = w3cDom.fromJsoup(jsoupDocument);
        String w3cHtml = w3cDom.asString(w3cDocument);
        Assert.assertEquals(jsoupDocument.html(), w3cHtml);
    }
}