import org.junit.Test;
import org.w3c.dom.Document;
import org.jsoup.helper.W3CDom;
import static org.junit.Assert.*;

public class W3CDomTest {
    @Test
    public void testFromJsoupEmptyDocument() {
        org.jsoup.nodes.Document input = org.jsoup.Jsoup.parse("<html></html>");
        Document output = W3CDom.fromJsoup(input);
        assertEquals("<html></html>", W3CDom.asString(output));
    }

    @Test
    public void testFromJsoupDocumentWithTextNode() {
        org.jsoup.nodes.Document input = org.jsoup.Jsoup.parse("<html><body><p>Hello World</p></body></html>");
        Document output = W3CDom.fromJsoup(input);
        assertEquals("<html><body><p>Hello World</p></body></html>", W3CDom.asString(output));
    }

    @Test
    public void testFromJsoupDocumentWithElement() {
        org.jsoup.nodes.Document input = org.jsoup.Jsoup.parse("<html><body><p class=\"foo\"><span>Hello World</span></p></body></html>");
        Document output = W3CDom.fromJsoup(input);
        assertEquals("<html><body><p class=\"foo\"><span>Hello World</span></p></body></html>", W3CDom.asString(output));
    }

    @Test
    public void testFromJsoupDocumentWithComment() {
        org.jsoup.nodes.Document input = org.jsoup.Jsoup.parse("<html><!-- This is a comment --></html>");
        Document output = W3CDom.fromJsoup(input);
        assertEquals("<html><!-- This is a comment --></html>", W3CDom.asString(output));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoupNullDocument() {
        W3CDom.fromJsoup(null);
    }

    @Test
    public void testConvertDocument() {
        org.jsoup.nodes.Document input = org.jsoup.Jsoup.parse("<html><body><p class=\"foo\"><span>Hello World</span></p></body></html>");
        Document output = W3CDom.newDocument();
        W3CDom.convert(input, output);
        assertEquals("<html><body><p class=\"foo\"><span>Hello World</span></p></body></html>", W3CDom.asString(output));
    }

    @Test
    public void testAsStringDocument() {
        Document doc = W3CDom.newDocument();
        doc.appendChild(doc.createTextNode("Hello World"));
        assertEquals("Hello World", W3CDom.asString(doc));
    }
}