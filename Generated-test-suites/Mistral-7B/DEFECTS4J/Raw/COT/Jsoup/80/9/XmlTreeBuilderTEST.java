import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;

import java.io.StringReader;
import java.util.List;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 private ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
 private Document document;
 private String xmlString = "<root><child1><child2>Hello</child2></child1><child3/></root>";

 @Test
 public void testDefaultSettings() {
 Validate.noException("Default settings should not throw exception", () -> xmlTreeBuilder.defaultSettings());
 }

 @Test
 public void testParseWithReaderAndBaseUri() throws Exception {
 document = xmlTreeBuilder.parse(new StringReader(xmlString), "baseUri");
 assertEquals("baseUri", document.baseUri());
 assertEquals(1, document.childNodes().size());
 }

 @Test
 public void testParseWithStringAndBaseUri() throws Exception {
 document = xmlTreeBuilder.parse(xmlString, "baseUri");
 assertEquals("baseUri", document.baseUri());
 assertEquals(1, document.childNodes().size());
 }

 @Test
 public void testInitialiseParse() throws Exception {
 Tokenizer tokenizer = new Tokenizer(new StringReader(xmlString));
 xmlTreeBuilder.initialiseParse(new StringReader(xmlString), "baseUri", new ParseErrorList(), parseSettings);
 assertEquals(xmlString, tokenizer.current().data());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessInvalidTokenType() throws Exception {
 Token token = new Token(1, "InvalidTokenType");
 xmlTreeBuilder.process(token);
 }

 @Test
 public void testInsertStartTag() throws Exception {
 Token startTag = new Token.StartTag("root");
 Element rootElement = xmlTreeBuilder.insert(startTag);
 assertEquals("root", rootElement.tagName());
 assertEquals("baseUri", rootElement.baseUri());
 }

 @Test
 public void testInsertEndTag() throws Exception {
 Token endTag = new Token.EndTag("root");
 xmlTreeBuilder.popStackToClose(endTag);
 assertEquals(0, xmlTreeBuilder.stack.size());
 }

 @Test
 public void testInsertComment() throws Exception {
 Token commentToken = new Token.Comment("This is a comment.");
 xmlTreeBuilder.insert(commentToken);
 assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
 }

 @Test
 public void testInsertCharacter() throws Exception {
 Token characterToken = new Token.Character('A');
 xmlTreeBuilder.insert(characterToken);
 assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
 }

 @Test
 public void testInsertDoctype() throws Exception {
 Token doctypeToken = new Token.Doctype("html", null, null);
 xmlTreeBuilder.insert(doctypeToken);
 assertEquals(1, xmlTreeBuilder.doc.childNodes().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessUnknownTag() throws Exception {
 Token startTag = new Token.StartTag("UnknownTag");
 xmlTreeBuilder.process(startTag);
 }

 @Test
 public void testPopStackToCloseUnknownTag() throws Exception {
 Token endTag = new Token.EndTag("UnknownTag");
 xmlTreeBuilder.popStackToClose(endTag);
 assertNull(xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1));
 }

 @Test
 public void testParseFragment() throws Exception {
 List<Node> fragmentNodes = xmlTreeBuilder.parseFragment("<root><child1><child2>Hello</child2></child1><child3/></root>", "baseUri", new ParseErrorList(), parseSettings);
 assertEquals(3, fragmentNodes.size());
 }

 @Test(expected = NullPointerException.class)
 public void testParseFragmentWithNullInput() {
 List<Node> fragmentNodes = xmlTreeBuilder.parseFragment(null, "baseUri", new ParseErrorList(), parseSettings);
 }

 @Test(expected = ParseError.class)
 public void testParseFragmentWithInvalidXml() throws Exception {
 List<Node> fragmentNodes = xmlTreeBuilder.parseFragment("<root><child1<child2>Hello</child2></child1><child3/></root>", "baseUri", new ParseErrorList(), parseSettings);
 assertNotNull(fragmentNodes.get(0).child(0).child(0).child(0).child(0).child(0).child(0).child(0));
 }

 @Test
 public void testParseFragmentWithEmptyInput() throws Exception {
 List<Node> fragmentNodes = xmlTreeBuilder.parseFragment("", "baseUri", new ParseErrorList(), parseSettings);
 assertEquals(0, fragmentNodes.size());
 }

}