package org.jsoup.parser;

import org.apache.commons.lang3.Validate;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.jsoup.parser.Parser.parse;

public class ParserTest {

 private Parser parser;

 @Before
 public void setUp() {
 String html = "<html>\n" +
 " <head>\n" +
 " <title>Test Page</title>\n" +
 " </head>\n" +
 " <body>\n" +
 " <h1 id=\"title\">Hello World</h1>\n" +
 " <p id=\"p1\">First Paragraph</p>\n" +
 " <ul>\n" +
 " <li>Item 1</li>\n" +
 " <li>Item 2\n" +
 " <ul>\n" +
 " <li>Sub-item 1</li>\n" +
 " <li>Sub-item 2</li>\n" +
 " </ul>\n" +
 " </li>\n" +
 " </ul>\n" +
 " </body>\n" +
 " </html>";
 String baseUri = "http://test.com/";
 parser = new Parser(html, baseUri, false);
 }

 @Test
 public void testParse() {
 Document doc = parser.parse();

 assertThat(doc.title(), instanceOf(Element.class));
 Element title = (Element) doc.title();
 assertThat(title.text(), is("Test Page"));

 Element body = doc.body();
 Element h1 = body.selectFirst("h1");
 assertThat(h1.text(), is("Hello World"));

 Element p1 = body.selectFirst("#p1");
 assertThat(p1.text(), is("First Paragraph"));

 Element ul = body.selectFirst("ul");
 List<Element> liList = ul.children();
 assertThat(liList.size(), is(2));

 Element subUl = liList.get(1).selectFirst("ul");
 List<Element> subLiList = subUl.children();
 assertThat(subLiList.size(), is(2));
 }

 @Test
 public void testParseBodyFragment() {
 String bodyHtml = "<ul>\n" +
 " <li>Item 1</li>\n" +
 " <li>Item 2\n" +
 " <ul>\n" +
 " <li>Sub-item 1</li>\n" +
 " <li>Sub-item 2</li>\n" +
 " </ul>\n" +
 " </li>\n" +
 " </ul>";
 Document doc = Parser.parseBodyFragment(bodyHtml, baseUri);

 Element ul = doc.body();
 List<Element> liList = ul.children();
 assertThat(liList.size(), is(2));

 Element subUl = liList.get(1).selectFirst("ul");
 List<Element> subLiList = subUl.children();
 assertThat(subLiList.size(), is(2));
 }

 @Test
 public void testParseXmlDecl() {
 String xmlDeclHtml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 Document doc = Parser.parse(xmlDeclHtml, baseUri);
 XmlDeclaration xmlDecl = (XmlDeclaration) doc.head().children().get(0);
 assertThat(xmlDecl.version(), is("1.0"));
 }
}