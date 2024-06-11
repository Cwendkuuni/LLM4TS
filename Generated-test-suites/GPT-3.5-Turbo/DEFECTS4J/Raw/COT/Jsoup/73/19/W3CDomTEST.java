
import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.w3c.dom.Document;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><body><div id=\"test\">Hello World!</div></body></html>");
        W3CDom converter = new W3CDom();
        Document w3cDoc = converter.fromJsoup(jsoupDoc);

        //Assert that the documents are not null
        Assert.assertNotNull(jsoupDoc);
        Assert.assertNotNull(w3cDoc);

        //Assert that the output document contains the same elements as the input document
        Assert.assertEquals(jsoupDoc.toString(), converter.asString(w3cDoc));
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><body><div id=\"test\">Hello World!</div></body></html>");
        W3CDom converter = new W3CDom();
        Document w3cDoc = converter.fromJsoup(jsoupDoc);

        //Assert that the documents are not null
        Assert.assertNotNull(jsoupDoc);
        Assert.assertNotNull(w3cDoc);

        //Assert that the output document contains the same elements as the input document
        Assert.assertEquals(jsoupDoc.toString(), converter.asString(w3cDoc));
    }

    @Test
    public void testAsString() {
        org.jsoup.nodes.Document jsoupDoc = Jsoup.parse("<html><body><div id=\"test\">Hello World!</div></body></html>");
        W3CDom converter = new W3CDom();
        Document w3cDoc = converter.fromJsoup(jsoupDoc);

        //Assert that the documents are not null
        Assert.assertNotNull(jsoupDoc);
        Assert.assertNotNull(w3cDoc);

        //Assert that the output document string is not null or empty
        String w3cString = converter.asString(w3cDoc);
        Assert.assertNotNull(w3cString);
        Assert.assertNotEquals("", w3cString);
    }
}
