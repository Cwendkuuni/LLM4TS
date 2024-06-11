package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.junit.jupiter.api.*;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class ParserTest {

    @Test
    public void parse_validHtml_shouldReturnDocument() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello World</h1></body></html>";
        Document doc = Parser.parse(html, "https://www.example.com");

        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
    }

    @Test
    public void parseBodyFragment_validFragment_shouldReturnDocument() {
        String fragment = "<p>Hello World</p>";
        Document doc = Parser.parseBodyFragment(fragment, "https://www.example.com");

        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("<body>\n" +
                " <p>\n" +
                "  Hello World\n" +
                " </p>\n" +
                "</body>", doc.body().html());
    }

    @Test
    public void parseBodyFragmentRelaxed_validFragmentWithImplicitTags_shouldReturnDocument() {
        String fragment = "<div><p>Hello World</p></div>";
        Document doc = Parser.parseBodyFragmentRelaxed(fragment, "https://www.example.com");

        assertNotNull(doc);
        assertEquals("https://www.example.com", doc.baseUri());
        assertEquals("<body>\n" +
                " <div>\n" +
                "  <p>\n" +
                "   Hello World\n" +
                "  </p>\n" +
                " </div>\n" +
                "</body>", doc.body().html());
    }

    @Test
    public void parse_emptyHtml_shouldReturnEmptyDocument() {
        String html = "";
        Document doc = Parser.parse(html, "");

        assertNotNull(doc);
        assertEquals("", doc.baseUri());
        assertTrue(doc.children().isEmpty());
    }

    @Test
    public void parseBodyFragment_emptyFragment_shouldReturnEmptyDocument() {
        String fragment = "";
        Document doc = Parser.parseBodyFragment(fragment, "");

        assertNotNull(doc);
        assertEquals("", doc.baseUri());
        assertTrue(doc.children().isEmpty());
    }

    @Test
    public void parseBodyFragmentRelaxed_emptyFragment_shouldReturnEmptyDocument() {
        String fragment = "";
        Document doc = Parser.parseBodyFragmentRelaxed(fragment, "");

        assertNotNull(doc);
        assertEquals("", doc.baseUri());
        assertTrue(doc.children().isEmpty());
    }

    @Test
    public void parse_invalidHtml_shouldThrowException() {
        String html = "<html><head><title>Test</title></head><body><h1>Hello</h1></html>";

        assertThrows(Exception.class, () -> {
            Parser.parse(html, "");
        });
    }

    @Test
    public void parseBodyFragment_invalidFragment_shouldThrowException() {
        String fragment = "<p>Hello World";

        assertThrows(Exception.class, () -> {
            Parser.parseBodyFragment(fragment, "");
        });
    }

    @Test
    public void parseBodyFragmentRelaxed_invalidFragment_shouldThrowException() {
        String fragment = "<div><p>Hello World";

        assertThrows(Exception.class, () -> {
            Parser.parseBodyFragmentRelaxed(fragment, "");
        });
    }
}