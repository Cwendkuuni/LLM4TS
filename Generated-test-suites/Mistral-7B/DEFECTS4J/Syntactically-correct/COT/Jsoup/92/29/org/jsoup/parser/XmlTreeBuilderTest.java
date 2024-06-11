import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.Parser;
import org.jsoup.parser.Token;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Test;

import java.io.StringReader;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
 private ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
 private Parser parser = new Parser(xmlTreeBuilder);

 @Test
 public void testInitialiseParse() {
 Document document = xmlTreeBuilder.parse(new StringReader(""), "", parser);
 assertNotNull(document);
 assertNotNull(document.outputSettings());
 assertEquals(Document.OutputSettings.Syntax.xml, document.outputSettings().syntax());
 }

 @Test
 public void testParse() {
 Document document = xmlTreeBuilder.parse(new StringReader("<xml><tag></tag></xml>"), "", parser);
 assertNotNull(document);
 assertEquals("xml", document.tagName());
 assertNotNull(document.childNodes());
 assertEquals(1, document.childNodes().size());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testProcessInvalidTokenType() {
 Token token = new Token(-1, "");
 xmlTreeBuilder.process(token);
 }

 @Test
 public void testProcessStartTag() {
 Token startTagToken = new Token(Token.TokenType.StartTag, "<tag>", null);
 Element element = xmlTreeBuilder.insert(startTagToken);
 assertEquals("tag", element.tagName());
 }

 @Test
 public void testProcessEndTag() {
 Token endTagToken = new Token(Token.TokenType.EndTag, "</tag>", null);
 xmlTreeBuilder.popStackToClose(endTagToken);
 }

 @Test
 public void testProcessComment() {
 Token commentToken = new Token(Token.TokenType.Comment, "-- comment --", null);
 xmlTreeBuilder.insert(commentToken);
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token(Token.TokenType.Character, "a", false);
 xmlTreeBuilder.insert(characterToken);
 }

 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token(Token.TokenType.Doctype, "<!DOCTYPE tag [ ]>", null);
 xmlTreeBuilder.insert(doctypeToken);
 }

 @Test
 public void testParseFragment() {
 List<Node> nodes = xmlTreeBuilder.parseFragment("<xml><tag></tag></xml>", "", parser);
 assertEquals(1, nodes.size());
 }

 @Test
 public void testParseFragmentWithContext() {
 List<Node> nodes = xmlTreeBuilder.parseFragment("<xml><tag></tag></xml>", new Element("parent"), "", parser);
 assertEquals(1, nodes.size());
 }

 @Test
 public void testPopStackToCloseNonExistentTag() {
 Token endTagToken = new Token(Token.TokenType.EndTag, "</nonexistenttag>", null);
 xmlTreeBuilder.popStackToClose(endTagToken);
 }
}