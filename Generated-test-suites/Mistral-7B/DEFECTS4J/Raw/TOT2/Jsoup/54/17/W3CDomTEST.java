import org.junit.Test;
import org.w3c.dom.Document;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document in = org.jsoup.Jsoup.parse("<html><body><h1>Hello World</h1></body></html>");
        Document out = W3CDom.getInstance().fromJsoup(in);

        assertNotNull(out);
        assertEquals("<html><body><h1>Hello World</h1></body></html>", W3CDom.getInstance().asString(out));
    }
}