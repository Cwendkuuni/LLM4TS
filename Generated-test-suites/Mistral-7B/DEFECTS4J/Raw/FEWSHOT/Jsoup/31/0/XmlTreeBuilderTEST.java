package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.*;
import org.jsoup.parser.Token.*;
import org.junit.Test;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder parser;

 @Test
 public void testInitialiseParse() {
 parser = new XmlTreeBuilder();
parser.initialiseParse("", "", null);
 assertEquals(Document.class, parser.getRootNode().getClass());
 }

 @Test
 public void testProcessStartTag() {
 parser = new XmlTreeBuilder();
 parser.initialiseParse("", "", null);
 Token startTagToken = new Token(StartTag.class, "root", null);
 parser.process(startTagToken);
 Equals("root", parser.getRootNode().nodeName());
 }

 @Test
 public void testProcessEndTag() {
 parser = new XmlTreeBuilder();
 parser.initialiseParse("", "", null);
 Element root = new Element("root");
 parser.insert(root);
 parser.stack.add(root);
 Token endTagToken = new Token(EndTag.class, "root");
 parser.process(endTagToken);
 assertEquals(0, parser.stack.size());
 }

 @Test
 public void testProcessComment() {
 parser = new XmlTreeBuilder();
 parser.initialiseParse("", "", null);
 Token commentToken = new Token(Comment.class, "this is a comment");
 parser.process(commentToken);
 assertEquals(1, parser.getDocument().childNodes().size());
 assertEquals("this is a comment", parser.getDocument().childNodes().get(0).nodeValue());
 }

 @Test
 public void testProcessCharacter() {
 parser = new XmlTreeBuilder();
 parser.initialiseParse("", "", null);
 Token characterToken = new Token(Character.class, "a");
 parser.process(characterToken);
 Element root = new Element("root");
 root.appendChild(parser.getDocument().createTextNode("a"));
 assertEquals(root.childNodes().size(), 1);
 }

 @Test
 public void testProcessDoctype() {
 parser = new XmlTreeBuilder();
 parser.initialiseParse("", "", null);
 Token doctypeToken = new Token(Doctype.class, "html", null, null, null);
 parser.process(doctypeToken);
 Document doc = parser.getDocument();
 DocumentType doctype = doc.getDoctype();
 assertEquals("html", doctype.getName());
 assertEquals(null, doctype.getPublicIdentifier());
 assertEquals(null, doctype.getSystemIdentifier());
 }

 @Test
 public void testProcessEof() {
 parser = new XmlTreeBuilder();
 parser.initialiseParse("", "", null);
 parser.process(new Token(EOF.class));
 assertTrue(parser.getDocument().getDocumentElement().getTagName().equals(""));
 }

 @Test
 public void testPopStackToClose() {
 parser = new XmlTreeBuilder();
 parser.initialiseParse("", "", null);
 Element root = new Element("root");
 parser.insert(root);
 parser.stack.add(root);
 Element child1 = new Element("child1");
 parser.insert(child1);
 parser.stack.add(child1);
 Element child2 = new Element("child2");
 parser.insert(child2);
 parser.stack.add(child2);
 Element endTag = new Element("root");
 endTag.setTagName("root");
 endTag.setName("root");
 endTag.setEndTag(true);
 parser.process(endTag);
 assertEquals(0, parser.stack.size());
 }
}