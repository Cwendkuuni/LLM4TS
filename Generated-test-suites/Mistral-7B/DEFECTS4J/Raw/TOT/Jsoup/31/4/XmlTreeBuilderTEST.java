import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.ElementFilter;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Token;

import java.util.Stack;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 doc = new Document();
 xmlTreeBuilder.initialiseParse("", "", new ParseErrorList());
 xmlTreeBuilder.setInput(new StringReader("<xml><root><child/><child tag='test'/><child tag='test'/></root></xml>"));
 }

 @Test
 public void testProcessStartTag() {
 Token startTagToken = new Token.StartTag("root", null, null, null, true);
 Element rootElement = xmlTreeBuilder.process(startTagToken);
 assertEquals("root", rootElement.nodeName());
 assertNotNull(rootElement.attributes());
 assertEquals(1, doc.childNodes().size());
 assertTrue(rootElement.hasChildNodes());
 }

 @Test
 public void testProcessEndTag() {
 Token endTagToken = new Token.EndTag("root");
 xmlTreeBuilder.process(endTagToken);
 assertEquals(0, doc.childNodes().size());
 }

 @Test
 public void testProcessComment() {
 Token commentToken = new Token.Comment("This is a comment");
 xmlTreeBuilder.process(commentToken);
 Element commentElement = doc.selectSingle("//comment()");
 assertEquals("This is a comment", commentElement.text());
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token.Character(' ');
 xmlTreeBuilder.process(characterToken);
 Text characterElement = (Text) doc.selectSingle("//text()");
 assertEquals(" ", characterElement.text());
 }

 @Test
 public void testProcessDoctype() {
 Token doctypeToken = new Token.Doctype("xml", null, null);
 xmlTreeBuilder.process(doctypeToken);
 DocumentType doctypeNode = (DocumentType) doc.selectSingle("//doctype()");
 assertEquals("xml", doctypeNode.name());
 assertNull(doctypeNode.publicIdentifier());
 assertNull(doctypeNode.systemIdentifier());
 }

 @Test
 public void testProcessUnknownTag() {
 Token startTagToken = new Token.StartTag("unknown", null, null, null, true);
 Element unknownElement = xmlTreeBuilder.process(startTagToken);
 assertEquals("unknown", unknownElement.nodeName());
 assertFalse(unknownElement.hasAttributes());
 }

 @Test
 public void testProcessSelfClosingTag() {
 Token startTagToken = new Token.StartTag("selfClosing", null, null, null, true);
 Element selfClosingElement = xmlTreeBuilder.process(startTagToken);
 assertEquals("selfClosing", selfClosingElement.nodeName());
 assertFalse(selfClosingElement.hasChildNodes());
 assertTrue(selfClosingElement.hasAttributes());
 }

 @Test(expected = AssertionError.class)
 public void testProcessUnexpectedTokenType() {
 Token unexpectedToken = new Token(123);
 xmlTreeBuilder.process(unexpectedToken);
 }

 @Test
 public void testPopStackToClose() {
 Token startTagToken = new Token.StartTag("root", null, null, null, true);
 Token endTagToken = new Token.EndTag("root");
 xmlTreeBuilder.process(startTagToken);
 xmlTreeBuilder.process(startTagToken);
 xmlTreeBuilder.process(endTagToken);
 Stack<Element> stack = xmlTreeBuilder.stack;
 assertEquals(1, stack.size());
 assertEquals("root", stack.peek().nodeName());
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertEquals(0, stack.size());
 }
}