package org.jsoup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Test;

public class JsoupTest {
	
	@Test
	public void testParse() {
		Document doc = Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>");
		String title = doc.title();
		assertEquals("Test", title);
	}

	@Test
	public void testParseWithBaseUri() {
		Document doc = Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>", "http://example.com");
		String baseUri = doc.baseUri();
		assertEquals("http://example.com", baseUri);
	}

	@Test
	public void testParseWithParser() {
		Parser parser = Parser.xmlParser();
		Document doc = Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>", "", parser);
		String title = doc.title();
		assertEquals("Test", title);
	}

	@Test
	public void testParseFile() {
		Document doc = Jsoup.parse(new File("test.html"), "UTF-8", "http://example.com");
		String baseUri = doc.baseUri();
		assertEquals("http://example.com", baseUri);
	}

	@Test
	public void testParseInputStream() {
		InputStream in = new FileInputStream(new File("test.html"));
		Document doc = Jsoup.parse(in, "UTF-8", "http://example.com");
		String baseUri = doc.baseUri();
		assertEquals("http://example.com", baseUri);
	}

	@Test
	public void testParseInputStreamWithParser() {
		Parser parser = Parser.xmlParser();
		InputStream in = new FileInputStream(new File("test.html"));
		Document doc = Jsoup.parse(in, "UTF-8", "http://example.com", parser);
		String baseUri = doc.baseUri();
		assertEquals("http://example.com", baseUri);
	}

	@Test
	public void testParseBodyFragment() {
		Document doc = Jsoup.parseBodyFragment("<div><p>Hello, World!</p></div>", "http://example.com");
		String baseUri = doc.baseUri();
		assertEquals("http://example.com", baseUri);
	}

	@Test
	public void testParseBodyFragmentWithoutBaseUri() {
		Document doc = Jsoup.parseBodyFragment("<div><p>Hello, World!</p></div>");
		String baseUri = doc.baseUri();
		assertEquals("", baseUri);
	}

	@Test
	public void testConnect() {
		Connection connection = Jsoup.connect("http://example.com");
		assertEquals("http://example.com", connection.url());
	}

	@Test
	public void testClean() {
		String cleanHtml = Jsoup.clean("<div><script>alert('Hello, World!');</script></div>", "http://example.com", Whitelist.basic());
		assertEquals("<div></div>", cleanHtml);
	}

	@Test
	public void testCleanWithoutBaseUri() {
		String cleanHtml = Jsoup.clean("<div><script>alert('Hello, World!');</script></div>", Whitelist.basic());
		assertEquals("<div></div>", cleanHtml);
	}

	@Test
	public void testCleanWithOutputSettings() {
		Document.OutputSettings outputSettings = new Document.OutputSettings().prettyPrint(false);
		String cleanHtml = Jsoup.clean("<div><p>Hello, World!</p></div>", "http://example.com", Whitelist.basic(), outputSettings);
		assertEquals("<div><p>Hello, World!</p></div>", cleanHtml);
	}

	@Test
	public void testIsValid() {
		boolean isValid = Jsoup.isValid("<div><p>Hello, World!</p></div>", Whitelist.basic());
		assertTrue(isValid);
	}

	@Test
	public void testIsValidWithInvalidTags() {
		boolean isValid = Jsoup.isValid("<script>alert('Hello, World!');</script>", Whitelist.basic());
		assertFalse(isValid);
	}
}