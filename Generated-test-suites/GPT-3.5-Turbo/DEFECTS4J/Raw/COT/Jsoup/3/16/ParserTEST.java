
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {
    @Test
    public void testParse() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "");

        assertEquals("<html>\n" +
                " <head>\n" +
                "  <title>\n" +
                "   Test\n" +
                "  </title>\n" +
                " </head>\n" +
                " <body>\n" +
                "  <h1>\n" +
                "   Hello World\n" +
                "  </h1>\n" +
                " </body>\n" +
                "</html>", doc.outerHtml());
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World</h1>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "");

        assertEquals("<body>\n" +
                " <h1>\n" +
                "  Hello World\n" +
                " </h1>\n" +
                "</body>", doc.body().outerHtml());
    }

    @Test
    public void testParseBodyFragmentWithRelaxedParsing() {
        String bodyHtml = "<table><tr><td>Test</td></tr></table>";
        Document doc = Parser.parseBodyFragment(bodyHtml, "");

        assertEquals("<body>\n" +
                " <table>\n" +
                "  <tbody>\n" +
                "   <tr>\n" +
                "    <td>\n" +
                "     Test\n" +
                "    </td>\n" +
                "   </tr>\n" +
                "  </tbody>\n" +
                " </table>\n" +
                "</body>", doc.body().outerHtml());
    }

    @Test
    public void testParseComment() {
        String html = "<!-- Comment -->";
        Document doc = Parser.parse(html, "");

        assertEquals("<html>\n" +
                " <!-- Comment -->\n" +
                "</html>", doc.outerHtml());
    }

    @Test
    public void testParseCdata() {
        String html = "<![CDATA[This is a CDATA section]]>";
        Document doc = Parser.parse(html, "");

        assertEquals("<html>\n" +
                " <![CDATA[This is a CDATA section]]>\n" +
                "</html>", doc.outerHtml());
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        Document doc = Parser.parse(html, "");

        assertEquals("<html>\n" +
                " <?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "</html>", doc.outerHtml());
    }

    @Test
    public void testParseEndTag() {
        String html = "<p>This is a paragraph</p>";
        Document doc = Parser.parse(html, "");

        assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <p>\n" +
                "   This is a paragraph\n" +
                "  </p>\n" +
                " </body>\n" +
                "</html>", doc.outerHtml());
    }

    @Test
    public void testParseStartTag() {
        String html = "<div class=\"container\">Content</div>";
        Document doc = Parser.parse(html, "");

        assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <div class=\"container\">\n" +
                "   Content\n" +
                "  </div>\n" +
                " </body>\n" +
                "</html>", doc.outerHtml());
    }

    @Test
    public void testParseTextNode() {
        String html = "<p>This is a paragraph</p>";
        Document doc = Parser.parse(html, "");

        assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <p>\n" +
                "   This is a paragraph\n" +
                "  </p>\n" +
                " </body>\n" +
                "</html>", doc.outerHtml());
    }

    @Test
    public void testParseAttribute() {
        String html = "<a href=\"https://www.example.com\">Example</a>";
        Document doc = Parser.parse(html, "");

        assertEquals("<html>\n" +
                " <head></head>\n" +
                " <body>\n" +
                "  <a href=\"https://www.example.com\">\n" +
                "   Example\n" +
                "  </a>\n" +
                " </body>\n" +
                "</html>", doc.outerHtml());
    }
}
