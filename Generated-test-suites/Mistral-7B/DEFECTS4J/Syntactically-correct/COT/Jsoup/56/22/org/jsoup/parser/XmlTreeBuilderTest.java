import org.junit.Assert;
import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.Validate;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.XmlDeclaration;

import java.util.List;
import java.util.ArrayList;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();

 @Test
 public void testDefaultSettings() {
 Document doc = xmlTreeBuilder.parse("<xml/>", "baseUrl");
 Assert.assertEquals("baseUrl", doc.baseUri());
 }

 @Test
 public void testParseMethod() {
 Document doc = xmlTreeBuilder.parse("<xml><tag attr='val'/></xml>", "baseUrl");
 Element tag = doc.select("tag").first();
 Assert.assertEquals("tag", tag.tagName());
 Assert.assertEquals("val", tag.attr("attr"));
 }

 @Test
 public void testInitialiseParseMethod() {
 Document doc = new Document();
 ParseErrorList errors = new ParseErrorList();
 ParseSettings settings = ParseSettings.preserveCase;
 XmlTreeBuilder xmlTreeBuilderSpy = new XmlTreeBuilder() {
 @Override
 protected void initialiseParse(String input, String baseUri, ParseErrorList errors, ParseSettings settings) {
 super.initialiseParse(input, baseUri, errors, settings);
 Assert.assertEquals("input", doc.body().text());
 Assert.assertEquals("baseUri", baseUri);
 }
 };
 xmlTreeBuilderSpy.parse("<xml/>", "input", errors, settings);
 }

 @Test
 public void testProcessStartTag() {
 Token startTagToken = new Token.StartTag("tag", null, null);
 Element actual = xmlTreeBuilder.insert(startTagToken);
 Element expected = new Element("tag", "baseUrl", null);
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testProcessEndTag() {
 Token endTagToken = new Token.EndTag("tag");
 xmlTreeBuilder.popStackToClose(endTagToken);
 Element topElement = xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
 Assert.assertFalse(topElement.nodeName().equals("tag"));
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 Token unexpectedToken = new Token(-1, "unkown", null);
 xmlTreeBuilder.process(unexpectedToken);
 }

 @Test
 public void testInsertComment() {
 Token commentToken = new Token.Comment("comment");
 Comment actual = (Comment) xmlTreeBuilder.insert(commentToken);
 Comment expected = new Comment("comment", "baseUrl");
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testInsertCharacter() {
 Token characterToken = new Token.Character('a');
 TextNode actual = (TextNode) xmlTreeBuilder.insert(characterToken);
 TextNode expected = new TextNode("a", "baseUrl");
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testInsertDoctype() {
 Token doctypeToken = new Token.Doctype("html", null, null);
 DocumentType actual = (DocumentType) xmlTreeBuilder.insert(doctypeToken);
 DocumentType expected = new DocumentType("html", null, null, "baseUrl");
 Assert.assertEquals(expected, actual);
 }

 @Test
 public void testParseFragmentMethod() {
 String xmlFragment = "<xml><tag attr='val'/><tag2/></xml>";
 List<Node> actualNodes = xmlTreeBuilder.parseFragment(xmlFragment, "baseUrl", new ParseErrorList(), ParseSettings.preserveCase);
 Element tag = (Element) actualNodes.get(0);
 Element tag2 = (Element) actualNodes.get(1);
 Assert.assertEquals("tag", tag.tagName());
 Assert.assertEquals("val", tag.attr("attr"));
 Assert.assertEquals("tag2", tag2.tagName());
 }

 @Test(expected = AssertionError.class)
 public void testInvalidComment() {
 String xmlWithInvalidComment = "<xml><!--!invalid-->";
 xmlTreeBuilder.parse(xmlWithInvalidComment, "baseUrl");
 Validate.errorMessage("Unexpected comment type: "); // Validate.fail is being stubbed with Assert.assertEquals
 }

 @Test
 public void testParseWithErrors() {
 String xmlWithErrors = "<xml><tag attr='val'/><tag2/><tag3/><tag4/><tag5/>"; // tag5 is unknown
 ParseErrorList errors = new ParseErrorList();
 List<Node> nodes = xmlTreeBuilder.parseFragment(xmlWithErrors, "baseUrl", errors, ParseSettings.preserveCase);
 Assert.assertEquals(5, nodes.size());
 Element tag1 = (Element) nodes.get(0);
 Element tag2 = (Element) nodes.get(1);
 Element tag3 = (Element) nodes.get(2);
 Element tag4 = (Element) nodes.get(3);
 // tag5 should not be in the list due to ParseError
 }
}