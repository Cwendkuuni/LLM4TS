import org.junit.Test;
import org.jsoup.nodes.*;

public class ParserTest {

    @Test
    public void testParse() {
        String html = "<html><head><title>Test Page</title></head><body><h1>Hello World!</h1></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parse(html, baseUri);

        assert doc != null;
        assert doc.title().equals("Test Page");
        assert doc.body().getElementsByTag("h1").text().equals("Hello World!");
    }

    @Test
    public void testParseBodyFragment() {
        String bodyHtml = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

        assert doc != null;
        assert doc.body().getElementsByTag("h1").text().equals("Hello World!");
    }

    @Test
    public void testParseComment() {
        String html = "<!-- This is a comment -->";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parse(html, baseUri);

        assert doc != null;
        assert doc.childNode(0) instanceof Comment;
        Comment comment = (Comment) doc.childNode(0);
        assert comment.getData().equals(" This is a comment ");
    }

    @Test
    public void testParseXmlDecl() {
        String html = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parse(html, baseUri);

        assert doc != null;
        assert doc.childNode(0) instanceof XmlDeclaration;
        XmlDeclaration declaration = (XmlDeclaration) doc.childNode(0);
        assert declaration.getData().equals("xml version=\"1.0\" encoding=\"UTF-8\"");
    }

    @Test
    public void testParseEndTag() {
        String html = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parse(html, baseUri);

        assert doc != null;
        assert doc.body().getElementsByTag("h1").isEmpty();
    }

    @Test
    public void testParseStartTag() {
        String html = "<h1>Hello World!</h1>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parse(html, baseUri);

        assert doc != null;
        assert !doc.body().getElementsByTag("h1").isEmpty();
        assert doc.body().getElementsByTag("h1").text().equals("Hello World!");
    }

    @Test
    public void testParseTextNode() {
        String html = "Hello World!";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parse(html, baseUri);

        assert doc != null;
        assert doc.text().equals("Hello World!");
    }

    @Test
    public void testParseCdata() {
        String html = "<![CDATA[This is a CDATA section]]>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parse(html, baseUri);

        assert doc != null;
        assert doc.childNode(0) instanceof TextNode;
        TextNode textNode = (TextNode) doc.childNode(0);
        assert textNode.getWholeText().equals("This is a CDATA section");
    }

    @Test
    public void testAddChildToParent() {
        String html = "<body></body>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parse(html, baseUri);

        assert doc != null;
        assert doc.body() != null;
    }

    @Test
    public void testStackHasValidParent() {
        String html = "<body></body>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parse(html, baseUri);

        assert doc != null;
        assert doc.body() != null;
    }
    
    @Test
    public void testPopStackToSuitableContainer() {
        String html = "<p><div><span></span></div></p>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parse(html, baseUri);

        assert doc != null;
        assert doc.select("p > div > span").size() == 1;
    }

    @Test
    public void testPopStackToClose() {
        String html = "<p><div><span></span></div></p>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parse(html, baseUri);

        assert doc != null;
        assert doc.select("p > div").isEmpty();
    }

    @Test
    public void testLast() {
        String html = "<html><head></head><body></body></html>";
        String baseUri = "http://example.com";
        
        Document doc = Parser.parse(html, baseUri);

        assert doc != null;
        assert doc.head() != null;
        assert doc.body() != null;
        assert doc.head().equals(doc.last());
    }
}