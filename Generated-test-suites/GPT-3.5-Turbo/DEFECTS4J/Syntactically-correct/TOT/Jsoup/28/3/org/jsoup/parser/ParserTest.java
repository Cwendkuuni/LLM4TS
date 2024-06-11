import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
    
    @Test
    public void testParseInput_typicalCase() {
        Parser parser = new Parser(new TreeBuilder());
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";
        
        // expected output
        Document expected = new Document(baseUri);
        Element htmlElement = new Element("html");
        Element bodyElement = new Element("body");
        Element h1Element = new Element("h1");
        h1Element.appendText("Hello, World!");
        bodyElement.appendChild(h1Element);
        htmlElement.appendChild(bodyElement);
        expected.appendChild(htmlElement);
        
        Document actual = parser.parseInput(html, baseUri);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseInput_emptyHtml() {
        Parser parser = new Parser(new TreeBuilder());
        String html = "";
        String baseUri = "http://example.com";
        
        // expected output
        Document expected = new Document(baseUri);
        
        Document actual = parser.parseInput(html, baseUri);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseInput_nullBaseUri() {
        Parser parser = new Parser(new TreeBuilder());
        String html = "<p>Testing</p>";
        String baseUri = null;
        
        // expected output
        Document expected = new Document("");
        Element pElement = new Element("p");
        pElement.appendText("Testing");
        expected.appendChild(pElement);
        
        Document actual = parser.parseInput(html, baseUri);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        TreeBuilder expected = new HtmlTreeBuilder();
        TreeBuilder actual = parser.getTreeBuilder();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testSetTreeBuilder() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        TreeBuilder newTreeBuilder = new XmlTreeBuilder();
        
        Parser expected = new Parser(newTreeBuilder);
        Parser actual = parser.setTreeBuilder(newTreeBuilder);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testIsTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        
        boolean actual = parser.isTrackErrors();
        
        assertTrue(actual);
    }
    
    @Test
    public void testSetTrackErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        Parser expected = new Parser(new HtmlTreeBuilder());
        Parser actual = parser.setTrackErrors(5);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetErrors_noErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        
        List<ParseError> expected = new ArrayList<>();
        List<ParseError> actual = parser.getErrors();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetErrors_withErrors() {
        Parser parser = new Parser(new HtmlTreeBuilder());
        parser.setTrackErrors(5);
        parser.parseInput("<p>Testing</p>", "http://example.com");
        
        List<ParseError> expected = new ArrayList<>();
        expected.add(new ParseError("Some error message"));
        List<ParseError> actual = parser.getErrors();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParse_typicalCase() {
        String html = "<html><body><h1>Hello, World!</h1></body></html>";
        String baseUri = "http://example.com";
        
        // expected output
        Document expected = new Document(baseUri);
        Element htmlElement = new Element("html");
        Element bodyElement = new Element("body");
        Element h1Element = new Element("h1");
        h1Element.appendText("Hello, World!");
        bodyElement.appendChild(h1Element);
        htmlElement.appendChild(bodyElement);
        expected.appendChild(htmlElement);
        
        Document actual = Parser.parse(html, baseUri);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParse_emptyHtml() {
        String html = "";
        String baseUri = "http://example.com";
        
        // expected output
        Document expected = new Document(baseUri);
        
        Document actual = Parser.parse(html, baseUri);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseFragment_typicalCase() {
        String fragmentHtml = "<p>Testing</p>";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        
        // expected output
        Element expected = new Element("p");
        expected.appendText("Testing");
        
        List<Node> actual = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        assertEquals(expected, actual.get(0));
    }
    
    @Test
    public void testParseFragment_emptyFragment() {
        String fragmentHtml = "";
        Element context = new Element("div");
        String baseUri = "http://example.com";
        
        // expected output
        List<Node> expected = new ArrayList<>();
        
        List<Node> actual = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseFragment_nullContext() {
        String fragmentHtml = "<p>Testing</p>";
        Element context = null;
        String baseUri = "http://example.com";
        
        // expected output
        Element expected = new Element("p");
        expected.appendText("Testing");
        
        List<Node> actual = Parser.parseFragment(fragmentHtml, context, baseUri);
        
        assertEquals(expected, actual.get(0));
    }
    
    @Test
    public void testParseBodyFragment_typicalCase() {
        String bodyHtml = "<p>Testing</p>";
        String baseUri = "http://example.com";
        
        // expected output
        Document expected = new Document(baseUri);
        Element bodyElement = new Element("body");
        Element pElement = new Element("p");
        pElement.appendText("Testing");
        bodyElement.appendChild(pElement);
        expected.appendChild(bodyElement);
        
        Document actual = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseBodyFragment_emptyHtml() {
        String bodyHtml = "";
        String baseUri = "http://example.com";
        
        // expected output
        Document expected = new Document(baseUri);
        Element bodyElement = new Element("body");
        expected.appendChild(bodyElement);
        
        Document actual = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseBodyFragment_nullBaseUri() {
        String bodyHtml = "<p>Testing</p>";
        String baseUri = null;
        
        // expected output
        Document expected = new Document("");
        Element bodyElement = new Element("body");
        Element pElement = new Element("p");
        pElement.appendText("Testing");
        bodyElement.appendChild(pElement);
        expected.appendChild(bodyElement);
        
        Document actual = Parser.parseBodyFragment(bodyHtml, baseUri);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testParseBodyFragmentRelaxed_typicalCase() {
        String bodyHtml = "<p>Testing</p>";
        String baseUri = "http://example.com";
        
        // expected output
        Document expected = new Document(baseUri);
        Element bodyElement = new Element("body");
        Element pElement = new Element("p");
        pElement.appendText("Testing");
        bodyElement.appendChild(pElement);
        expected.appendChild(bodyElement);
        
        Document actual = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testHtmlParser() {
        Parser expected = new Parser(new HtmlTreeBuilder());
        Parser actual = Parser.htmlParser();
        
        assertEquals(expected, actual);
    }
    
    @Test
    public void testXmlParser() {
        Parser expected = new Parser(new XmlTreeBuilder());
        Parser actual = Parser.xmlParser();
        
        assertEquals(expected, actual);
    }
}