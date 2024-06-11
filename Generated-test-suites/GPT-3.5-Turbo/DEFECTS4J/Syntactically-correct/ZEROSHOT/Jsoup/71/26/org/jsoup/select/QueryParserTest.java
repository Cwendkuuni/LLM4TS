package org.jsoup.select;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class QueryParserTest {

    private Document document;

    @Before
    public void setUp() {
        String html = "<html><head></head><body>" +
                "<div id=\"div1\" class=\"class1\"></div>" +
                "<div id=\"div2\" class=\"class2\"></div>" +
                "<div id=\"div3\" class=\"class3\"></div>" +
                "<span id=\"span1\" class=\"class1\"></span>" +
                "<span id=\"span2\" class=\"class2\"></span>" +
                "</body></html>";
        document = Jsoup.parse(html);
    }

    @After
    public void tearDown() {
        document = null;
    }

    @Test
    public void testParse_WithIdSelector_ShouldReturnCorrectElement() {
        Element element = QueryParser.parse("#div1").findElement(document);
        assertEquals("div", element.tagName());
        assertEquals("div1", element.id());
    }

    @Test
    public void testParse_WithClassSelector_ShouldReturnCorrectElements() {
        Elements elements = QueryParser.parse(".class1").findElements(document);
        assertEquals(2, elements.size());
        assertTrue(elements.hasClass("class1"));
    }

    @Test
    public void testParse_WithTagSelector_ShouldReturnCorrectElements() {
        Elements elements = QueryParser.parse("span").findElements(document);
        assertEquals(2, elements.size());
        assertEquals("span", elements.first().tagName());
    }

    @Test
    public void testParse_WithAttributeSelector_ShouldReturnCorrectElements() {
        Elements elements = QueryParser.parse("[class=class2]").findElements(document);
        assertEquals(2, elements.size());
        assertTrue(elements.hasClass("class2"));
    }

    @Test
    public void testParse_WithCombinationSelectors_ShouldReturnCorrectElements() {
        Elements elements = QueryParser.parse("#div1.class1").findElements(document);
        assertEquals(1, elements.size());
        assertEquals("div", elements.first().tagName());
        assertEquals("div1", elements.first().id());
        assertTrue(elements.hasClass("class1"));
    }

    @Test
    public void testParse_WithPseudoSelector_ShouldReturnCorrectElements() {
        Elements elements = QueryParser.parse(":last-child").findElements(document);
        assertEquals(2, elements.size());
        assertTrue(elements.first().is("span"));
        assertTrue(elements.last().is("div"));
    }

    @Test
    public void testParse_WithAttributeContainsSelector_ShouldReturnCorrectElements() {
        Elements elements = QueryParser.parse("[class~=class]").findElements(document);
        assertEquals(5, elements.size());
    }

    @Test
    public void testParse_WithNthChildSelector_ShouldReturnCorrectElements() {
        Elements elements = QueryParser.parse(":nth-child(2)").findElements(document);
        assertEquals(2, elements.size());
    }

    @Test
    public void testParse_WithContainsTextSelector_ShouldReturnCorrectElements() {
        Elements elements = QueryParser.parse(":contains(\"class\")").findElements(document);
        assertEquals(5, elements.size());
    }

    @Test
    public void testParse_WithCustomSelector_ShouldReturnCorrectElements() {
        Elements elements = QueryParser.parse("div[class$=3]").findElements(document);
        assertEquals(1, elements.size());
        assertEquals("div", elements.first().tagName());
        assertTrue(elements.hasClass("class3"));
    }
}