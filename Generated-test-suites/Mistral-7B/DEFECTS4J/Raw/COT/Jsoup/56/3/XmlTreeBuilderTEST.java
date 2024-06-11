import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 doc = Jsoup.parse("<root><tag attr1='val1' attr2='val2'><subtag/><tag2 attr3='val3'/></tag></root>", "http://example.com", Parser.xmlParser());
 }

 @Test
 public void testDefaultSettings() {
 ParseSettings settings = xmlTreeBuilder.defaultSettings();
 Assert.assertEquals(ParseSettings.PreserveCase.class, settings.getClass());
 }

 @Test
 public void testParse() {
 Document parsedDoc = xmlTreeBuilder.parse("<root><tag attr1='val1' attr2='val2'><subtag/><tag2 attr3='val3'/></tag></root>", "http://example.com");
 Assert.assertEquals(doc.body(), parsedDoc.body());
 }

 @Test
 public void testInitialiseParse() {
 String input = "<root><tag attr1='val1' attr2='val2'><subtag/><tag2 attr3='val3'/></tag></root>";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, "http://example.com", errors, ParseSettings.preserveCase);
 // This test does not have an assert statement as it's more of a setup for other tests
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessNullToken() {
 xmlTreeBuilder.process(null);
 }

 @Test
 public void testProcessStartTag() {
 Token.StartTag startTag = new Token.StartTag("tag", 0, 0, 0, "tag", null);
 Element el = xmlTreeBuilder.insert(startTag);
 Assert.assertEquals("tag", el.nodeName());
 Assert.assertEquals("http://example.com", el.baseUri().toString());
 }

 @Test
 public void testProcessEndTag() {
 String elName = "tag";
 xmlTreeBuilder.popStackToClose(new Token.EndTag(elName.toCharArray(), 0, 0));
 Element firstFound = null;
 for (int pos = xmlTreeBuilder.stack.size() - 1; pos >= 0; pos--) {
 Element next = xmlTreeBuilder.stack.get(pos);
 if (next.nodeName().equals(elName)) {
 firstFound = next;
 break;
 }
 }
 Assert.assertNotNull(firstFound);
 }

 @Test
 public void testParseFragment() {
 String inputFragment = "<subtag/><tag2 attr3='val3'/>";
 List<Node> parsedNodes = xmlTreeBuilder.parseFragment(inputFragment, "http://example.com", new ParseErrorList(), ParseSettings.preserveCase);
 List<Node> docChildNodes = doc.childNodes();
 for (int i = 0; i < parsedNodes.size(); i++) {
 Node parsedNode = parsedNodes.get(i);
 Node docNode = docChildNodes.get(i);
 Assert.assertEquals(parsedNode.nodeName(), docNode.nodeName());
 Assert.assertEquals(parsedNode.text(), docNode.text());
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullNode() {
 xmlTreeBuilder.insertNode(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInsertNullToken() {
 xmlTreeBuilder.insert(null);
 }

 @Test
 public void testInsertComment() {
 Token.Comment commentToken = new Token.Comment("<!-- comment -->", 0, 0);
 xmlTreeBuilder.insert(commentToken);
 Comment comment = (Comment) doc.childNodes().get(0);
 Assert.assertEquals("comment", comment.data());
 }

 @Test
 public void testParseFragmentEmptyInput() {
 List<Node> parsedNodes = xmlTreeBuilder.parseFragment("", "http://example.com", new ParseErrorList(), ParseSettings.preserveCase);
 Assert.assertEquals(0, parsedNodes.size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseFragmentNullInput() {
 List<Node> parsedNodes = xmlTreeBuilder.parseFragment(null, "http://example.com", new ParseErrorList(), ParseSettings.preserveCase);
 }

 @Test
 public void testParseFragmentWithError() {
 ParseErrorList errors = new ParseErrorList();
 String input = "<root><tag attr1='val1' attr2='val2'><subtag/><tag2 attr3='val3'/></tag></root><invalid>";
 List<Node> parsedNodes = xmlTreeBuilder.parseFragment(input, "http://example.com", errors, ParseSettings.preserveCase);
 Assert.assertFalse(parsedNodes.isEmpty());
 }
}