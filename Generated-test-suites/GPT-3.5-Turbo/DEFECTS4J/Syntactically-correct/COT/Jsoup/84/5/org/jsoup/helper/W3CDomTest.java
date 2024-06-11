import org.junit.Assert;
import org.junit.Test;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;

import javax.xml.transform.TransformerException;
import java.io.IOException;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        W3CDom w3CDom = new W3CDom();
        Document jsoupDoc = new Document("");
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        Assert.assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        W3CDom w3CDom = new W3CDom();
        Document jsoupDoc = new Document("");
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        w3CDom.convert(jsoupDoc, w3cDoc);
        Assert.assertNotNull(w3cDoc.getFirstChild());
    }

    @Test
    public void testAsString() throws TransformerException {
        W3CDom w3CDom = new W3CDom();
        Document jsoupDoc = new Document("");
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        String result = w3CDom.asString(w3cDoc);
        Assert.assertNotNull(result);
    }
}