import org.junit.Test;
import org.w3c.dom.Document;
import static org.junit.Assert.*;

public class W3CDomTest {

    @Test
    public void testFromJsoupEmptyDocument() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.factory.NewDocument();
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        String actual = new W3CDom().asString(w3cDocument);
        String expected = "<document></document>";
        assertEquals(expected, actual);
    }

    @Test
    public void testFromJsoupTextDocument() {
        org.jsoup.nodes.Document jsoupDocument = org.jsoup.factory.NewDocumentBuilder().openParser().parseBodyFragment("Hello World!");
        Document w3cDocument = new W3CDom().fromJsoup(jsoupDocument);
        String actual = new W3CDom().asString(w3cDocument);
        String expected = "<document><text>Hello World!</text></document>";
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testFromJsoupNullDocument() {
        org.jsoup.nodes.Document jsoupDocument = null;
        new W3CDom().fromJsoup(jsoupDocument);
    }

    // Add more test cases for other methods
}