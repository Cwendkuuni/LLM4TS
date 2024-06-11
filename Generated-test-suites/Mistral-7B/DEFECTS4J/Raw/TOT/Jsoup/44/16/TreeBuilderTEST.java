package org.jsoup.parser.test;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.helper.CharSequenceInput;

import java.util.ArrayList;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class TreeBuilderTest {

private TreeBuilder builder;
private Document doc;

@Before
public void setup() {
// Initialize the test environment
builder = new TreeBuilder() {
@Override
protected boolean process(Token token) {
return true;
}
};
doc = builder.parse("", "", ParseErrorList.noTracking());
}

@Test
public void testParseWithEmptyInput() {
// Test case for parsing an empty input
assertEquals("", doc.body().text());
}

@Test
public void testParseWithBaseUri() {
// Test case for parsing an input with baseUri
builder = new TreeBuilder() {
@Override
protected boolean process(Token token) {
return true;
}
};
doc = builder.parse("<html></html>", "http://example.com/", ParseErrorList.noTracking());
assertNotNull(doc);
assertEquals("http://example.com/", doc.baseUri().toString());
}

@Test
public void testParseWithErrors() {
// Test case for parsing an input with errors
ParseErrorList errors = new ParseErrorList();
builder = new TreeBuilder() {
@Override
protected boolean process(Token token) {
return true;
}
};
doc = builder.parse("<html><foo></html>", "", errors);
assertEquals(0, errors.size());
// Test case for parsing an input with errors
errors = new ParseErrorList();
builder = new TreeBuilder() {
@Override
protected boolean process(Token token) {
return true;
}
};
doc = builder.parse("<html><foo bar=\"baz\">", "", errors);
assertEquals(1, errors.size());
assertTrue(errors.getMessage(0).contains("Unbalanced start tag foo."));
}

@Test
public void testProcessStartTag() {
// Test case for processing a start tag
builder = new TreeBuilder() {
@Override
protected boolean process(Token token) {
return processStartTag(token);
}
};
doc = builder.parse("<html><body><div id=\"test\"></div></body></html>", "", ParseErrorList.noTracking());
Element div = currentElement();
assertNotNull(div);
assertEquals("div", div.tagName());
assertEquals("test", div.id());
}

@Test
public void testProcessStartTagWithAttributes() {
// Test case for processing a start tag with attributes
builder = new TreeBuilder() {
@Override
protected boolean process(Token token) {
return processStartTag(token);
}
};
doc = builder.parse("<html><body><p id=\"test\" class=\"testClass\" title=\"Test Title\"></p></body></html>", "", ParseErrorList.noTracking());
Element p = currentElement();
assertNotNull(p);
assertEquals("p", p.tagName());
assertEquals("test", p.id());
assertEquals("testClass", p.classes().get(0).toString());
assertEquals("Test Title", p.attr("title"));
}

@Test
public void testProcessEndTag() {
// Test case for processing an end tag
builder = new TreeBuilder() {
@Override
protected boolean process(Token token) {
return processEndTag(token.name());
}
};
doc = builder.parse("<html><body><div id=\"test\"><p></p></div></body></html>", "", ParseErrorList.noTracking());
assertEquals(1, stack.size());
Element div = stack.get(stack.size()-1);
assertEquals("div", div.tagName());
assertEquals("test", div.id());
// Test case for processing an invalid end tag
builder = new TreeBuilder() {
@Override
protected boolean process(Token token) {
return processEndTag(token.name());
}
};
doc = builder.parse("<html><body><div id=\"test\"><p></p><foo></foo></div></body></html>", "", ParseErrorList.noTracking());
assertEquals(1, stack.size());
fail("Unexpected number of elements on stack");
}
}