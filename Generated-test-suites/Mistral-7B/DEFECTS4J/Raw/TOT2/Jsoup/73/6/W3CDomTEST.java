import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.w3c.dom.Document;

public class W3CDomTest {
    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = org.jsoup.Jsoup.parse("example.html");
        Document w3cDoc = new W3CDom().fromJsoup(jsoupDoc);

        // Check if the created W3C Document is not null
        Assert.assertNotNull(w3cDoc);
    }
}