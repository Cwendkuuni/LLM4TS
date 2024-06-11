import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.helper.W3CDom;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.w3c.dom.*;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        // create a jsoup document for testing
        Document jsoupDoc = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");

        // create a W3CDom object
        W3CDom w3cDom = new W3CDom();

        // convert jsoup doc to W3C doc
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // verify if the W3C doc is not null
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        // create a jsoup document for testing
        Document jsoupDoc = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");

        // create a W3CDom object
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // convert from jsoup to W3C doc
        Document out = w3cDom.convert(w3cDoc, jsoupDoc);

        // verify if the converted doc is not null
        assertNotNull(out);
    }

    @Test
    public void testAsString() {
        // create a jsoup document for testing
        Document jsoupDoc = Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");

        // create a W3CDom object
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // convert W3C doc to string
        String output = w3cDom.asString(w3cDoc);

        // verify if the output is not null
        assertNotNull(output);
    }
}