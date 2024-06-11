import org.jsoup.Jsoup;
import org.jsoup.helper.DataUtil;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Entities;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Tag;
import org.jsoup.parser.TokenQueue;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class ParserTest {

    private Parser parser;
    private Document doc;

    @Before
    public void setUp() {
        parser = new Parser("<html></html>", "http://example.com", false);
        doc = parser.parse();
    }

    @Test
    public void testParse() {
        Assert.assertEquals("<html>\n <head></head>\n <body></body>\n</html>", doc.outerHtml());
    }

    @Test
    public void testParseBodyFragment() {
        Document bodyFragment = Parser.parseBodyFragment("<div>Hello World</div>", "http://example.com");
        Assert.assertEquals("<body>\n <div>Hello World</div>\n</body>", bodyFragment.outerHtml());
    }

    @Test
    public void testParseBodyFragmentRelaxed() {
        Document bodyFragment = Parser.parseBodyFragmentRelaxed("<div>Hello World</div>", "http://example.com");
        Assert.assertEquals("<body>\n <div>Hello World</div>\n</body>", bodyFragment.outerHtml());
    }

    @Test
    public void testParseComment() {
        Parser parser = new Parser("<!-- Test Comment -->", "http://example.com", false);
        Document commentDoc = parser.parse();

        Assert.assertEquals("<html>\n <head></head>\n <body><!-- Test Comment --></body>\n</html>", commentDoc.outerHtml());
    }

    @Test
    public void testParseXmlDecl() {
        Parser parser = new Parser("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "http://example.com", false);
        Document xmlDoc = parser.parse();

        Assert.assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<html>\n <head></head>\n <body></body>\n</html>", xmlDoc.outerHtml());
    }

    @Test
    public void testParseEndTag() {
        Parser parser = new Parser("<html></html>", "http://example.com", false);
        Document endTagDoc = parser.parse();

        Assert.assertEquals("<html>\n <head></head>\n <body></body>\n</html>", endTagDoc.outerHtml());
    }

    @Test
    public void testParseStartTag() {
        Parser parser = new Parser("<html><head></head><body></body></html>", "http://example.com", false);
        Document startTagDoc = parser.parse();

        Assert.assertEquals("<html>\n <head></head>\n <body></body>\n</html>", startTagDoc.outerHtml());
    }

    @Test
    public void testParseTextNode() {
        Parser parser = new Parser("Hello World", "http://example.com", false);
        Document textNodeDoc = parser.parse();

        Assert.assertEquals("<html>\n <head></head>\n <body>Hello World</body>\n</html>", textNodeDoc.outerHtml());
    }

    @Test
    public void testParseCdata() {
        Parser parser = new Parser("<![CDATA[Hello World]]>", "http://example.com", false);
        Document cdataDoc = parser.parse();

        Assert.assertEquals("<html>\n <head></head>\n <body>Hello World</body>\n</html>", cdataDoc.outerHtml());
    }

    @Test
    public void testParseAttribute() {
        Parser parser = new Parser("<div id=\"test\"></div>", "http://example.com", false);
        Document attributeDoc = parser.parse();

        Assert.assertEquals("<html>\n <head></head>\n <body><div id=\"test\"></div></body>\n</html>", attributeDoc.outerHtml());
    }

    @Test
    public void testAddChildToParent() {
        Parser parser = new Parser("<div><p>Hello World</p></div>", "http://example.com", false);
        Document childDoc = parser.parse();

        Assert.assertEquals("<html>\n <head></head>\n <body><div><p>Hello World</p></div></body>\n</html>", childDoc.outerHtml());
    }

    @Test
    public void testStackHasValidParent() {
        Parser parser = new Parser("<div><p>Hello World</p></div>", "http://example.com", false);
        boolean validParent = parser.stackHasValidParent(Tag.valueOf("div"));

        Assert.assertTrue(validParent);
    }

    @Test
    public void testPopStackToSuitableContainer() {
        Parser parser = new Parser("<div><p>Hello World</p></div>", "http://example.com", false);
        Element container = parser.popStackToSuitableContainer(Tag.valueOf("p"));

        Assert.assertNotNull(container);
        Assert.assertEquals("div", container.tag().getName());
    }

    @Test
    public void testPopStackToClose() {
        Parser parser = new Parser("<div><p>Hello World</p></div>", "http://example.com", false);
        Element closedElement = parser.popStackToClose(Tag.valueOf("div"));

        Assert.assertEquals("div", closedElement.tag().getName());
    }

    @Test
    public void testLast() {
        Parser parser = new Parser("<div><p>Hello World</p></div>", "http://example.com", false);
        Element lastElement = parser.last();

        Assert.assertEquals("p", lastElement.tag().getName());
    }
}