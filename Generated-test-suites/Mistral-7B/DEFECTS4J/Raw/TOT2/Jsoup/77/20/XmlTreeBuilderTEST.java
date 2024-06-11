package org.jsoup.parser;

import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.exceptions.ParseError;
import static org.junit.Assert.*;;

public class XmlTreeBuilderTest {

@Test
public void testParse() {
String xmlString = "<root><child>Hello</child></root>";
Document xmlDocument = XmlTreeBuilder.parse(new StringReader(xmlString), "/test");
assertEquals("<root><child>Hello</child></root>", xmlDocument.html());
}

@Test(expected = ParseError.class)
public void testParseInvalidXml() {
String xmlString = "<root><child>Hello</child></root><script>"; // missing end tag
Document xmlDocument = XmlTreeBuilder.parse(new StringReader(xmlString), "/test");
}

@Test
public void testParseFragment() {
String xmlFragment = "<root><child>Hello</child></root>";
List<Node> nodes = XmlTreeBuilder.parseFragment(xmlFragment, "/test", ParseErrorList.noTracking(), ParseSettings.preserveCase);
Document xmlDocument = Jsoup.parseBodyFragment(nodes);
assertEquals("<root><child>Hello</child></root>", xmlDocument.html());
}
}