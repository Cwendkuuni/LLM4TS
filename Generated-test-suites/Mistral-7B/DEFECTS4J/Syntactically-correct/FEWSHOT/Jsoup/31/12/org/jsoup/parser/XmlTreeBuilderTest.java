package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Tokenizer tokeniser;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 tokeniser = new Tokenizer();
 }

 @Test
 public void testInsertNode() {
 Document doc = new Document();
 xmlTreeBuilder.initialiseParse("", "", null);
 xmlTreeBuilder.stack.add(doc);
 Node node = new Element("test");
 xmlTreeBuilder.insertNode(node);
 assertNotNull(doc.child(0));
 assertEquals("test", doc.child(0).nodeName());
 }

 @Test
 public void testInsertStartTag() {
 Document doc = new Document();
 xmlTreeBuilder.initialiseParse("", "", null);
 xmlTreeBuilder.stack.add(doc);
 Token startTag = tokeniser.next();
 Element el = xmlTreeBuilder.insert(startTag);
 assertNotNull(doc.child(0));
 assertEquals("test", doc.child(0).nodeName());
 }

 @Test
 public void testInsertEndTag() {
 Document doc = new Document();
 Element root = new Element("test");
 doc.appendChild(root);
 xmlTreeBuilder.stack.add(root);
 Token endTag = tokeniser.next();
 xmlTreeBuilder.popStackToClose(endTag);
 assertNull(root.lastChild());
 }

 @Test
 public void testInsertComment() {
 Document doc = new Document();
 xmlTreeBuilder.initialiseParse("", "", null);
 xmlTreeBuilder.stack.add(doc);
 Token comment = tokeniser.next();
 xmlTreeBuilder.insert(comment);
 assertNotNull(doc.child(0));
 assertTrue(doc.child(0) instanceof Comment);
 }

 @Test
 public void testInsertCharacter() {
 Document doc = new Document();
 xmlTreeBuilder.initialiseParse("", "", null);
 xmlTreeBuilder.stack.add(doc);
 Token characterToken = tokeniser.next();
 xmlTreeBuilder.insert(characterToken);
 assertNotNull(doc.child(0));
 assertTrue(doc.child(0) instanceof TextNode);
 }

 @Test
 public void testInsertDoctype() {
 Document doc = new Document();
 xmlTreeBuilder.initialiseParse("", "", null);
 xmlTreeBuilder.stack.add(doc);
 Token doctype = tokeniser.next();
 xmlTreeBuilder.insert(doctype);
 assertNotNull(doc.child(0));
 assertTrue(doc.child(0) instanceof DocumentType);
 }

 @Test
 public void testPopStackToClose() {
 Document doc = new Document();
 Element root = new Element("test");
 doc.appendChild(root);
 xmlTreeBuilder.stack.add(root);
 xmlTreeBuilder.stack.add(doc);
 Token endTag = tokeniser.next();
 xmlTreeBuilder.popStackToClose(endTag);
 assertEquals(1, doc.childCount());
 assertNotNull(doc.child(0));
 assertEquals("test", doc.child(0).nodeName());
 }

 @Test
 public void testParseEmptyFile() {
 Document doc = new Document();
 xmlTreeBuilder.initialiseParse("", "", null);
 assertTrue(doc.childCount() == 0);
 }

 @Test(expected = AssertionError.class)
 public void testValidateUnexpectedTokenType() {
 Document doc = new Document();
 xmlTreeBuilder.initialiseParse("", "", null);
 Token unexpectedToken = tokeniser.next();
 xmlTreeBuilder.process(unexpectedToken);
 }
}