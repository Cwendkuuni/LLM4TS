package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

/**
 * XmlTreeBuilderTest.java
 */
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 // Test for all methods in XmlTreeBuilder class

 // 1. Test for initialiseParse method
 @Test
 public void testInitialiseParse() {
 String input = "<xml></xml>";
 String baseUri = "http://www.example.com";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);
 assertNotNull(xmlTreeBuilder.doc);
 }

 // 2. Test for process method with valid token
 @Test
 public void testProcessValidToken() {
 Token token = new Token(Token.TokenType.StartTag, "<test>", 1, 1, 1, 1);
 assertTrue(xmlTreeBuilder.process(token));
 }

 // 3. Test for process method with invalid token
 @Test(expected = AssertionError.class)
 public void testProcessInvalidToken() {
 Token token = new Token(-1, "", 1, 1, 1, 1);
 xmlTreeBuilder.process(token);
 }

 // 4. Test for insertNode method
 @Test
 public void testInsertNode() {
 Element element = new Element("test", baseUri, null);
 xmlTreeBuilder.insertNode(element);
 assertNotNull(xmlTreeBuilder.currentElement().getChildNodes().get(0));
 }

 // 5. Test for insert method with StartTag token
 @Test
 public void testInsertStartTag() {
 Token startTag = new Token.StartTag("test", null, null, null);
 Element element = xmlTreeBuilder.insert(startTag);
 assertNotNull(element);
 }

 // 6. Test for insert method with Comment token
 @Test
 public void testInsertComment() {
 Token commentToken = new Token.Comment("<!-- comment -->", null);
 xmlTreeBuilder.insert(commentToken);
 assertNotNull(xmlTreeBuilder.currentElement().childNodes().get(0));
 }

 // 7. Test for insert method with Character token
 @Test
 public void testInsertCharacter() {
 Token characterToken = new Token.Character('a');
 xmlTreeBuilder.insert(characterToken);
 assertNotNull(xmlTreeBuilder.currentElement().childNodes().get(0));
 }

 // 8. Test for insert method with Doctype token
 @Test
 public void testInsertDoctype() {
 Token doctypeToken = new Token.Doctype("xml", "", "", null);
 xmlTreeBuilder.insert(doctypeToken);
 assertNotNull(xmlTreeBuilder.doc.getDoctype());
 }

 // 9. Test for popStackToClose method with valid tag name
 @Test
 public void testPopStackToCloseValidTagName() {
 Token startTag = new Token.StartTag("test", null, null, null);
 xmlTreeBuilder.insert(startTag);
 Token endTag = new Token.EndTag("test", null);
 xmlTreeBuilder.popStackToClose(endTag);
 assertNull(xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1));
 }

 // 10. Test for popStackToClose method with invalid tag name
 @Test
 public void testPopStackToCloseInvalidTagName() {
 Token startTag = new Token.StartTag("test", null, null, null);
 xmlTreeBuilder.insert(startTag);
 Token endTag = new Token.EndTag("invalidTag", null);
 xmlTreeBuilder.popStackToClose(endTag);
 assertNotNull(xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1));
 }

 // 11. Test for parseFragment method with valid input
 @Test
 public void testParseFragmentValidInput() {
 String input = "<xml><test/><test><comment>comment</comment></test></xml>";
 String baseUri = "http://www.example.com";
 List<Node> nodes = xmlTreeBuilder.parseFragment(input, baseUri, new ParseErrorList());
 assertNotNull(nodes.get(0));
 assertNotNull(nodes.get(1));
 assertNotNull(nodes.get(1).childNodes().get(0));
 }

 // 12. Test for parseFragment method with invalid input
 @Test(expected = AssertionError.class)
 public void testParseFragmentInvalidInput() {
 String input = "invalidInput";
 String baseUri = "http://www.example.com";
 xmlTreeBuilder.parseFragment(input, baseUri, new ParseErrorList());
 }

}