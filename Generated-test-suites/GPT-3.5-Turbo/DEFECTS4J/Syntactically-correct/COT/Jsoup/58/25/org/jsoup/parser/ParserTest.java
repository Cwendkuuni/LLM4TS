import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

	@Test
	public void testParseInput() {
		String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
		String baseUri = "http://example.com";
		
		Parser parser = new Parser(new HtmlTreeBuilder());
		Document document = parser.parseInput(html, baseUri);
		
		assertNotNull(document);
		assertEquals("Test", document.title());
		assertEquals(1, document.select("h1").size());
		assertEquals("Hello, World!", document.select("h1").first().text());
	}
	
	@Test
	public void testGetTreeBuilder() {
		Parser parser = new Parser(new HtmlTreeBuilder());
		
		TreeBuilder treeBuilder = parser.getTreeBuilder();
		
		assertNotNull(treeBuilder);
		assertTrue(treeBuilder instanceof HtmlTreeBuilder);
	}
	
	@Test
	public void testSetTreeBuilder() {
		Parser parser = new Parser(new HtmlTreeBuilder());
		TreeBuilder treeBuilder = new XmlTreeBuilder();
		
		parser.setTreeBuilder(treeBuilder);
		
		assertSame(treeBuilder, parser.getTreeBuilder());
	}
	
	@Test
	public void testIsTrackErrors() {
		Parser parser = new Parser(new HtmlTreeBuilder());
		parser.setTrackErrors(5);
		
		boolean trackErrors = parser.isTrackErrors();
		
		assertTrue(trackErrors);
	}
	
	@Test
	public void testSetTrackErrors() {
		Parser parser = new Parser(new HtmlTreeBuilder());
		
		parser.setTrackErrors(10);
		
		assertTrue(parser.isTrackErrors());
	}
	
	@Test
	public void testGetErrors() {
		String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
		String baseUri = "http://example.com";
		
		Parser parser = new Parser(new HtmlTreeBuilder());
		parser.setTrackErrors(5);
		
		Document document = parser.parseInput(html, baseUri);
		List<ParseError> errors = parser.getErrors();
		
		assertNotNull(errors);
		assertEquals(0, errors.size());
	}
	
	@Test
	public void testSettings() {
		Parser parser = new Parser(new HtmlTreeBuilder());
		ParseSettings settings = new ParseSettings(false);
		
		parser.settings(settings);
		
		assertSame(settings, parser.settings());
	}
	
	@Test
	public void testParse() {
		String html = "<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>";
		String baseUri = "http://example.com";
		
		Document document = Parser.parse(html, baseUri);
		
		assertNotNull(document);
		assertEquals("Test", document.title());
		assertEquals(1, document.select("h1").size());
		assertEquals("Hello, World!", document.select("h1").first().text());
	}
	
	@Test
	public void testParseFragment() {
		String fragmentHtml = "<h1>Hello, World!</h1>";
		Element context = null;
		String baseUri = "http://example.com";
		
		List<Node> nodes = Parser.parseFragment(fragmentHtml, context, baseUri);
		
		assertNotNull(nodes);
		assertEquals(1, nodes.size());
		assertTrue(nodes.get(0) instanceof Element);
		assertEquals("h1", ((Element)nodes.get(0)).tagName());
		assertEquals("Hello, World!", ((Element)nodes.get(0)).text());
	}
	
	@Test
	public void testParseXmlFragment() {
		String fragmentXml = "<h1>Hello, World!</h1>";
		String baseUri = "http://example.com";
		
		List<Node> nodes = Parser.parseXmlFragment(fragmentXml, baseUri);
		
		assertNotNull(nodes);
		assertEquals(1, nodes.size());
		assertTrue(nodes.get(0) instanceof Element);
		assertEquals("h1", ((Element)nodes.get(0)).tagName());
		assertEquals("Hello, World!", ((Element)nodes.get(0)).text());
	}
	
	@Test
	public void testParseBodyFragment() {
		String bodyHtml = "<h1>Hello, World!</h1>";
		String baseUri = "http://example.com";
		
		Document document = Parser.parseBodyFragment(bodyHtml, baseUri);
		
		assertNotNull(document);
		assertEquals(1, document.select("h1").size());
		assertEquals("Hello, World!", document.select("h1").first().text());
	}
	
	@Test
	public void testUnescapeEntities() {
		String escapedString = "&lt;h1&gt;Hello, World!&lt;/h1&gt;";
		boolean inAttribute = false;
		
		String unescapedString = Parser.unescapeEntities(escapedString, inAttribute);
		
		assertEquals("<h1>Hello, World!</h1>", unescapedString);
	}
	
	@Test
	public void testParseBodyFragmentRelaxed() {
		String bodyHtml = "<h1>Hello, World!</h1>";
		String baseUri = "http://example.com";
		
		Document document = Parser.parseBodyFragmentRelaxed(bodyHtml, baseUri);
		
		assertNotNull(document);
		assertEquals(1, document.select("h1").size());
		assertEquals("Hello, World!", document.select("h1").first().text());
	}
	
	@Test
	public void testHtmlParser() {
		Parser parser = Parser.htmlParser();
		
		assertNotNull(parser);
		assertTrue(parser.getTreeBuilder() instanceof HtmlTreeBuilder);
	}
	
	@Test
	public void testXmlParser() {
		Parser parser = Parser.xmlParser();
		
		assertNotNull(parser);
		assertTrue(parser.getTreeBuilder() instanceof XmlTreeBuilder);
	}
}