import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.*;
import static org.junit.Assert.*;

public class ParserTest {

    Parser parser;

    @Before
    public void setup() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Welcome</h1></body></html>";
        parser = new Parser(html, "http://test.com");
    }

    @Test
    public void testParseValidHtml() {
        Document doc = parser.parse();
        Element htmlNode = doc;
        assertEquals(htmlTag.toString(), htmlNode.tagName());
        Element headNode = htmlNode.child(0);
        assertEquals(headTag.toString(), headNode.tagName());
        Element titleNode = headNode.child(0);
        assertEquals(titleTag.toString(), titleNode.tagName());
        Element bodyNode = htmlNode.child(1);
        assertEquals(bodyTag.toString(), bodyNode.tagName());
        Element h1Node = bodyNode.child(0);
        assertEquals(h1.toString(), h1Node.tagName());
        assertEquals("Welcome", h1Node.text());
    }

    @Test(expected = IllegalStateException.class)
    public void testParseInvalidHtml() {
        String html = "<html><head><title>Test Page</title></head><body><h1<welcome</h1></body></html>";
        parser = new Parser(html, "http://test.com");
        parser.parse(); // should not reach here if the test passes
    }
}