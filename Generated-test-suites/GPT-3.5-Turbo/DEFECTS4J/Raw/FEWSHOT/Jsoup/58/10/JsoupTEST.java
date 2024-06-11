
package org.jsoup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.helper.DataUtil;
import org.jsoup.helper.HttpConnection;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.jsoup.safety.Cleaner;
import org.jsoup.safety.Whitelist;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class JsoupTest {
    @Test
    public void testParse() {
        Document document = Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>");
        assertEquals("<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>", document.html());
    }
    
    @Test
    public void testParseWithBaseUri() {
        Document document = Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>", "http://example.org");
        assertEquals("<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>", document.html());
    }
    
    @Test
    public void testParseWithParser() {
        Document.CustomParser customParser = new Document.CustomParser();
        Document document = Jsoup.parse("<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>", "http://example.org", customParser);
        assertEquals("<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>", document.html());
    }
    
    @Test
    public void testParseEmptyString() {
        Document document = Jsoup.parse("");
        assertEquals("", document.html());
    }
    
    @Test
    public void testConnect() {
        Connection connection = Jsoup.connect("http://example.com");
        assertNotNull(connection);
    }
    
    @Test
    public void testParseFileWithCharsetNameAndBaseUri() throws IOException {
        File file = new File("example.html");
        Document document = Jsoup.parse(file, "UTF-8", "http://example.org");
        assertEquals("<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>", document.html());
    }
    
    @Test
    public void testParseFileWithCharsetName() throws IOException {
        File file = new File("example.html");
        Document document = Jsoup.parse(file, "UTF-8");
        assertEquals("<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>", document.html());
    }
    
    @Test
    public void testParseInputStreamWithCharsetNameAndBaseUri() throws IOException {
        InputStream inputStream = DataUtil.loadStream("example.html");
        Document document = Jsoup.parse(inputStream, "UTF-8", "http://example.org");
        assertEquals("<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>", document.html());
    }
    
    @Test
    public void testParseInputStreamWithCharsetName() throws IOException {
        InputStream inputStream = DataUtil.loadStream("example.html");
        Document document = Jsoup.parse(inputStream, "UTF-8");
        assertEquals("<html><head><title>Test</title></head><body><h1>Hello, World!</h1></body></html>", document.html());
    }
    
    @Test
    public void testParseBodyFragmentWithBaseUri() {
        Document document = Jsoup.parseBodyFragment("<h1>Hello, World!</h1>", "http://example.org");
        assertEquals("<h1>Hello, World!</h1>", document.html());
    }
    
    @Test
    public void testParseBodyFragment() {
        Document document = Jsoup.parseBodyFragment("<h1>Hello, World!</h1>");
        assertEquals("<h1>Hello, World!</h1>", document.html());
    }
    
    @Test
    public void testCleanWithBaseUriAndWhitelist() {
        String clean = Jsoup.clean("<h1>Hello, World!</h1>", "http://example.org", Whitelist.basic());
        assertEquals("<h1>Hello, World!</h1>", clean);
    }
    
    @Test
    public void testCleanWithWhitelist() {
        String clean = Jsoup.clean("<h1>Hello, World!</h1>", Whitelist.basic());
        assertEquals("<h1>Hello, World!</h1>", clean);
    }
    
    @Test
    public void testCleanWithBaseUriWhitelistAndOutputSettings() {
        String clean = Jsoup.clean("<h1>Hello, World!</h1>", "http://example.org", Whitelist.basic(), new Document.OutputSettings());
        assertEquals("<h1>Hello, World!</h1>", clean);
    }
    
    @Test
    public void testIsValid() {
        boolean valid = Jsoup.isValid("<h1>Hello, World!</h1>", Whitelist.basic());
        assertTrue(valid);
    }
    
    @Test
    public void testIsValidWithInvalidHtml() {
        boolean valid = Jsoup.isValid("<h1>Hello, World!</h2>", Whitelist.basic());
        assertFalse(valid);
    }
    
    @Test
    public void testParseURLWithTimeoutMillis() throws IOException {
        URL url = new URL("http://example.com");
        Document document = Jsoup.parse(url, 1000);
        assertNotNull(document);
    }
}
