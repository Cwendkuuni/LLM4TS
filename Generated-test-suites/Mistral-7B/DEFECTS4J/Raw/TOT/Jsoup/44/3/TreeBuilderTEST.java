import org.jsoup.parser.TreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.*;

public class TreeBuilderTest {

 private TreeBuilder treeBuilder;
 private String input;

 @Before
 public void setup() {
 input = "<html><head><title>Test</title></head><body><h1>Hello</h1></body></html>";
 }

 @Test
 public void testParseWithValidInput() {
 TreeBuilder treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 // override process method to allow testing
 return true;
 }
 };
 treeBuilder.initialiseParse(input, "", ParseErrorList.noTracking());
 treeBuilder.parse(input, "");
 Document doc = treeBuilder.doc;
 Element html = doc.body();
 Element h1 = html.child(0);
 assertEquals("Hello", h1.text());
 }

 @Test
 public void testParseWithEmptyInput() {
 TreeBuilder treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 // override process method to allow testing
 return true;
 }
 };
 treeBuilder.initialiseParse("", "", ParseErrorList.noTracking());
 Document doc = treeBuilder.parse("", "");
 assertNull(doc);
 }

 @Test
 public void testParseWithInvalidInput() {
 TreeBuilder treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 // override process method to allow testing
 return true;
 }
 };
 treeBuilder.initialiseParse("InvalidInput", "", ParseErrorList.noTracking());
 assertNotNull(treeBuilder.errors);
 assertTrue(treeBuilder.errors.size() > 0);
 }

 @Test
 public void testProcessStartTag() {
 TreeBuilder treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 // override process method to allow testing
 return true;
 }

 @Override
 protected boolean processStartTag(String name) {
 // override processStartTag method to test specific name
 return "html".equals(name);
 }
 };
 treeBuilder.initialiseParse(input, "", ParseErrorList.noTracking());
 treeBuilder.parse(input, "");
 Element html = treeBuilder.currentElement();
 assertEquals("html", html.tagName());
 }

 @Test
 public void testProcessStartTagWithAttributes() {
 TreeBuilder treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 // override process method to allow testing
 return true;
 }

 @Override
 protected boolean processStartTag(String name, Attributes attrs) {
 // override processStartTag method to test specific name and attributes
 return "title".equals(name) && "Test".equals(attrs.get("text/plain"));
 }
 };
 treeBuilder.initialiseParse(input, "", ParseErrorList.noTracking());
 treeBuilder.parse(input, "");
 Element title = treeBuilder.currentElement();
 assertEquals("Test", title.text());
 }

 @Test
 public void testProcessEndTag() {
 TreeBuilder treeBuilder = new TreeBuilder() {
 @Override
 protected boolean process(Token token) {
 // override process method to allow testing
 return true;
 }

 @Override
 protected boolean processEndTag(String name) {
 // override processEndTag method to test specific name
 return "body".equals(name);
 }
 };
 treeBuilder.initialiseParse(input, "", ParseErrorList.noTracking());
 treeBuilder.parse(input, "");
 Element body = treeBuilder.currentElement();
 assertEquals("body", body.tagName());
 }
}