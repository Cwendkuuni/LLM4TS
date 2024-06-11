package org.jsoup.parser;

import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;

import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testParse_Reader_String() {
        String input = "<root>Hello, World!</root>";
        String baseUri = "http://example.com";
        Reader reader = new StringReader(input);

        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(reader, baseUri);

        assertEquals("root", doc.child(0).nodeName());
        assertEquals("Hello, World!", doc.child(0).text());
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testParse_String_String() {
        String input = "<root>Hello, World!</root>";
        String baseUri = "http://example.com";

        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(input, baseUri);

        assertEquals("root", doc.child(0).nodeName());
        assertEquals("Hello, World!", doc.child(0).text());
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testInitialiseParse_Reader_String_ParseErrorList_ParseSettings() {
        String input = "<root/>";
        String baseUri = "http://example.com";
        Reader reader = new StringReader(input);

        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        builder.initialiseParse(reader, baseUri, errors, settings);

        assertEquals("http://example.com", builder.baseUri());
        assertEquals(errors, builder.getErrors());
        assertEquals(settings, builder.settings());
    }

    // More test cases for other methods...

}