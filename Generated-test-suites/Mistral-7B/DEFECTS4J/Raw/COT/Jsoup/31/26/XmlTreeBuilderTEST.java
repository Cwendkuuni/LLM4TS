import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.ElementTree;
import org.jsoup.nodes.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document document;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 document = new Document();
 xmlTreeBuilder.initialiseParse("", "", new ParseErrorList());
 xmlTreeBuilder.setInput(document);
 }

 @Test
 public void testInitialiseParse() {
 xmlTreeBuilder.initialiseParse("", "", new ParseErrorList());
 assertTrue(document.content().size() == 1);
 assertEquals(document.rootElement(), xmlTreeBuilder.getRootNode());
 }

 @Test
 public void testProcess_StartTag() {
 Token startTagToken = new Token(Token.StartTag.class, "<tag name='test' attr='test' />");
 xmlTreeBuilder.process(startTagToken);
 Element tagElement = xmlTreeBuilder.getCurrentElement();
 assertEquals("tag", tagElement.nodeName());
 assertEquals("test", tagElement.tagName());
 assertEquals("test", tagElement.attr("attr"));
 assertEquals(1, tagElement.childNodes().size());
 }

 @Test
 public void testProcess_EndTag() {
 Token endTagToken = new Token(Token.EndTag.class, "</tag>");
 xmlTreeBuilder.process(endTagToken);
 assertFalse(xmlTreeBuilder.getCurrentElement() == null);
 }

 @Test
 public void testProcess_Comment() {
 Token commentToken = new Token(Token.Comment.class, "-- comment -->");
 xmlTreeBuilder.process(commentToken);
 Comment commentNode = (Comment) xmlTreeBuilder.getCurrentChild();
 assertEquals("-- comment -->", commentNode.data());
 }

 @Test
 public void testProcess_Character() {
 Token characterToken = new Token(Token.Character.class, "ABC");
 xmlTreebuilder.process(characterToken);
 TextNode characterNode = (TextNode) xmlTreebuilder.getCurrentChild();
 assertEquals("ABC", characterNode.text());
 }

 @Test
 public void testProcess_Doctype() {
 Token doctypeToken = new Token(Token.Doctype.class, "<!DOCTYPE tag [ ]>");
 xmlTreeBuilder.process(doctypeToken);
 DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.getCurrentChild();
 assertEquals("tag", doctypeNode.tagName());
 }

 @Test(expected = AssertionError.class)
 public void testProcess_UnexpectedTokenType() {
 Token unexpectedToken = new Token(0, "");
 xmlTreeBuilder.process(unexpectedToken);
 }

 @Test
 public void testPopStackToClose() {
 Element tag1 = new Element("tag1");
 Element tag2 = new Element("tag2");
 stack.push(tag1);
 stack.push(tag2);
 xmlTreeBuilder.popStackToClose(new Token(Token.EndTag.class, "</tag1>"));
 assertEquals(1, stack.size());
 assertEquals(tag2, stack.peek());
 }

 @Test
 public void testPopStackToClose_NotFound() {
 xmlTreeBuilder.popStackToClose(new Token(Token.EndTag.class, "</tagX>"));
 assertEquals(1, stack.size());
 }
}