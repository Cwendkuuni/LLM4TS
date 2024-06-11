package org.jsoup.parser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;

import java.util.Stack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class XmlTreeBuilderTest {

 private Tokenizer tokeniser;
 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;

 @Before
 public void setUp() {
 tokeniser = new Tokenizer();
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.setInput("", "", new ParseErrorList());
 doc = xmlTreeBuilder.parse();
 }

 @Test
 public void testInsertStartTag() {
 Token startTagToken = new Token.StartTag("tag", new Attribute("attr", "value"));
 Element actual = xmlTreeBuilder.insert(startTagToken);
 Element expected = new Element("tag", "", new Attribute("attr", "value"));
 assertEquals(expected, actual);
 }

 @Test
 public void testInsertSelfClosingStartTag() {
 Token startTagToken = new Token.StartTag("tag", "", new Attribute[0], true);
 Element actual = xmlTreeBuilder.insert(startTagToken);
 Element expected = new Element("tag");
 assertEquals(expected, actual);
 }

 @Test
 public void testInsertComment() {
 Token commentToken = new Token.Comment("comment text");
 xmlTreeBuilder.insert(commentToken);
 Comment actual = (Comment) doc.child(0);
 Comment expected = new Comment("comment text");
 assertEquals(expected.text(), actual.text());
 }

 @Test
 public void testInsertCharacter() {
 Token characterToken = new Token.Character('a');
 xmlTreeBuilder.insert(characterToken);
 TextNode actual = (TextNode) doc.child(1);
 TextNode expected = new TextNode("a");
 assertEquals(expected.text(), actual.text());
 }

 @Test
 public void testInsertDocumentType() {
 Token documentTypeToken = new Token.Doctype("doctype", "", "", "");
 DocumentType actual = (DocumentType) doc.child(0);
 DocumentType expected = new DocumentType("doctype", "", "", "");
 assertEquals(expected.name(), actual.name());
 }

 @Test
 public void testPopStackToClose() {
 // Arrange
 Token startTagToken = new Token.StartTag("outer", new Attribute[0]);
 xmlTreeBuilder.insert(startTagToken);
 Element outer = (Element) doc.child(0);
 stack.push(outer);

 Token startTagToken2 = new Token.StartTag("inner", new Attribute[0]);
 xmlTreeBuilder.insert(startTagToken2);
 Element inner = (Element) doc.child(1);
 stack.push(inner);

 Token endTagToken = new Token.EndTag("outer");
 // Act
 xmlTreeBuilder.popStackToClose(endTagToken);

 // Assert
 assertEquals(1, stack.size());
 assertEquals(outer, stack.peek());
 }

 @After
 public void tearDown() {
 xmlTreeBuilder = null;
 doc = null;
 tokeniser = null;
 }

}