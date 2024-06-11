import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.ParseError;
import org.jsoup.parser.Parser;
import org.junit.Test;

public class ParserTest {
    @Test
    public void testParseInput() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><body><h1>Hello World</h1></body></html>";
        String baseUri = "http://www.example.com";
        Document document = parser.parseInput(html, baseUri);
        Element element = document.getElementsByTag("h1").first();
        assert element.text().equals("Hello World");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputWithNullBaseUri() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><body><h1>Hello World</h1></body></html>";
        parser.parseInput(html, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputWithEmptyString() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "";
        String baseUri = "http://www.example.com";
        parser.parseInput(html, baseUri);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseInputWithInvalidHtml() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<";
        String baseUri = "http://www.example.com";
        parser.setTrackErrors(1);
        parser.parseInput(html, baseUri);
        List<ParseError> errors = parser.getErrors();
        assert errors.size() > 0;
    }

    @Test
    public void testParseInputWithMaxErrorsReached() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        String html = "<html><body><h1>Hello World</h1><h1>Hello World</h1></body></html>";
        String baseUri = "http://www.example.com";
        parser.setTrackErrors(1);
        Document document = parser.parseInput(html, baseUri);
        List<ParseError> errors = parser.getErrors();
        assert errors.size() == 1;
    }

    @Test
    public void testParseStaticHtml() {
        Document document = Parser.parse("«html><body><h1>Hello World</h1></body></html»", "http://www.example.com");
        Element element = document.getElementsByTag("h1").first();
        assert element.text().equals("Hello World");
    }

    @Test
    public void testParseStaticHtmlWithMaxErrorsReached() {
        Document document = Parser.parse("<html><body><h1>Hello World</h1><h1>Hello World</h1></body></html>", "http://www.example.com");
        List<ParseError> errors = Parser.getErrors();
        assert errors.size() > 0;
    }

    @Test
    public void testParseStaticBodyFragment() {
        Document document = Parser.parseBodyFragment("<html><body><h1>Hello World</h1></body></html>", "http://www.example.com");
        Element element = document.getElementsByTag("h1").first();
        assert element.text().equals("Hello World");
    }

    @Test
    public void testParseStaticBodyFragmentWithMaxErrorsReached() {
        Document document = Parser.parseBodyFragment("<html><body><h1>Hello World</h1><h1>Hello World</h1></body></html>", "http://www.example.com");
        List<ParseError> errors = Parser.getErrors();
        assert errors.size() > 0;
    }
}