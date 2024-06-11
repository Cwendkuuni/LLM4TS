import org.jsoup.parser.*;
import org.jsoup.nodes.Document;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals("html", doc.child(0).tagName());
        assertEquals("body", doc.child(0).child(0).tagName());
        assertEquals("h1", doc.child(0).child(0).child(0).tagName());
        assertEquals("Hello World!", doc.child(0).child(0).child(0).text());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<div><p>Paragraph</p></div>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("div", doc.body().child(0).tagName());
        assertEquals("p", doc.body().child(0).child(0).tagName());
        assertEquals("Paragraph", doc.body().child(0).child(0).text());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        String bodyHtml = "<div><p>Paragraph</p></div>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);

        assertNotNull(doc);
        assertEquals("div", doc.body().child(0).tagName());
        assertEquals(1, doc.body().child(0).children().size());
        assertEquals("Paragraph", doc.body().child(0).child(0).tagName());
    }

    @Test
    public void testParseWithComments() {
        String html = "<html><!-- Comment --><body><h1>Hello World!</h1></body></html>";
        String baseUri = "https://www.example.com";
        Document doc = Parser.parse(html, baseUri);

        assertNotNull(doc);
        assertEquals(1, doc.childNodeSize()); // only one node, not counting the comment
        assertEquals("html", doc.child(0).tagName());
        assertEquals("body", doc.child(0).child(0).tagName());
    }

    // Add more test cases for other methods as needed

}