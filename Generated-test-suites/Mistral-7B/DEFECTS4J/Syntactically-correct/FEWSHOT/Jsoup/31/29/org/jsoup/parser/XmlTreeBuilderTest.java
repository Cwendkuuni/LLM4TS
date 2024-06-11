package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.Iterator;
import java.util.Stack;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder builder;
 private Stack<Element> stack;

 @Before
 public void setup() {
 builder = new XmlTreeBuilder();
 stack = new Stack<>();
 }

 @Test
 public void testInitialiseParse() {
 Document doc = mock(Document.class);
 when(doc.getBaseURI()).thenReturn("baseUri");
 builder.initialiseParse("input", "baseUri", null);
 assertSame(doc, builder.getDocument());
 stack.push(doc);
 }

 @Test
 public void testProcessStartTag() {
 Element el = new Element("tag");
 Token startTag = mock(Token.StartTag.class);
 when(startTag.name()).thenReturn("tag");
 when(startTag.isSelfClosing()).thenReturn(false);
 when(startTag.attributes()).thenReturn(Collections.emptyMap());

 builder.stack = stack;
 builder.process(startTag);

 assertEquals(el, stack.peek());
 }

 @Test
 public void testProcessSelfClosingStartTag() {
 Element el = new Element("tag");
 el.setTagName("tag");
 el.setSelfClosing(true);

 Token startTag = mock(Token.StartTag.class);
 when(startTag.name()).thenReturn("tag");
 when(startTag.isSelfClosing()).thenReturn(true);
 when(startTag.attributes()).thenReturn(Collections.emptyMap());

 builder.stack = stack;
 builder.process(startTag);

 assertNull(stack.peek());
 }

 @Test
 public void testProcessEndTag() {
 String elName = "tag";

 Token endTag = mock(Token.EndTag.class);
 when(endTag.name()).thenReturn(elName);

 builder.stack = stack;
 builder.popStackToClose(endTag);

 assertEquals(Collections.emptyList(), stack);
 }

 @Test(expected = AssertionError.class)
 public void testUnexpectedTokenType() {
 Token token = mock(Token.class);
 when(token.type).thenReturn(123);

 builder.process(token);
 }

 @Test
 public void testInsertComment() {
 Comment comment = mock(Comment.class);
 Text node = mock(Text.class);
 when(comment.getData()).thenReturn("comment");
 when(node.getData()).thenReturn("comment");
 when(node.getBaseURI()).thenReturn("baseUri");

 Token commentToken = mock(Token.Comment.class);
 when(commentToken.getData()).thenReturn("comment");

 builder.process(commentToken);

 assertSame(comment, stack.peek());
 }

 @Test
 public void testInsertCharacter() {
 Text node = mock(Text.class);
 when(node.getData()).thenReturn("char");
 when(node.getBaseURI()).thenReturn("baseUri");

 Token characterToken = mock(Token.Character.class);
 when(characterToken.getData()).thenReturn("char");

 builder.process(characterToken);

 assertSame(node, stack.peek());
 }

 @Test
 public void testInsertDoctype() {
 DocumentType doctypeNode = mock(DocumentType.class);
 when(doctypeNode.getName()).thenReturn("doctype");
 when(doctypeNode.getPublicIdentifier()).thenReturn("publicId");
 when(doctypeNode.getSystemIdentifier()).thenReturn("systemId");
 when(doctypeNode.getBaseURI()).thenReturn("baseUri");

 Token doctypeToken = mock(Token.Doctype.class);
 when(doctypeToken.getName()).thenReturn("doctype");
 when(doctypeToken.getPublicIdentifier()).thenReturn("publicId");
 when(doctypeToken.getSystemIdentifier()).thenReturn("systemId");

 builder.process(doctypeToken);

 assertSame(doctypeNode, stack.peek());
 }
}