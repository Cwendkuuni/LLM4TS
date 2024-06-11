package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.lang.Validate;
import org.jsoup.nodes.*;
import org.junit.Test;

import java.util.*;

public class ParserTest {
    @Test
    public void testParse() {
        Document doc = Parser.parse("<html>\n<head>\n<title>Test</title>\n</head>\n<body>\n<h1>Hello World</h1>\n</body>\n</html>", "");
        
        assertEquals("html", doc.tagName());
        assertEquals("head", doc.head().tagName());
        assertEquals("title", doc.title().tagName());
        assertEquals("Test", doc.title().text());
        assertEquals("body", doc.body().tagName());
        assertEquals("h1", doc.body().child(0).tagName());
        assertEquals("Hello World", doc.body().child(0).text());
    }
    
    @Test
    public void testParseBodyFragment() {
        Document doc = Parser.parseBodyFragment("<h1>Hello World</h1>", "");
        
        assertNull(doc.head());
        assertEquals("body", doc.body().tagName());
        assertEquals("h1", doc.body().child(0).tagName());
        assertEquals("Hello World", doc.body().child(0).text());
    }
    
    @Test
    public void testEmptyStringArray() {
        assertEquals(0, Util.EMPTY_STRING_ARRAY.length);
    }
    
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo \"bar\"", Util.stripLeadingAndTrailingQuotes("foo \"bar\""));
        assertEquals("\"foo\" bar", Util.stripLeadingAndTrailingQuotes("\"foo\" bar"));
        assertEquals("\"foo\" and \"bar\"", Util.stripLeadingAndTrailingQuotes("\"foo\" and \"bar\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\""));
    }
    
    @Test
    public void testStripLeadingHyphens() {
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));
        assertNull(Util.stripLeadingHyphens(null));
    }
}