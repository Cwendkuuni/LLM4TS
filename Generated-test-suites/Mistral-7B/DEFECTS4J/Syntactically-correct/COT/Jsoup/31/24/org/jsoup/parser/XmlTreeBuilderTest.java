package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokenizer;
import org.junit.Test;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;
 private Tokenizer tokeniser;

 @SuppressWarnings("unchecked")
 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 doc = new Document();
 tokeniser = new Tokenizer();
 }

 @Test
 public void testInitialiseParse() {
 xmlTreeBuilder.initialiseParse("", "", new ParseErrorList());
 assertNotNull(xmlTreeBuilder.stack);
 assertNotNull(xmlTreeBuilder.doc);
 }

 @Test
 public void testProcessStartTag() {
 Token startTagToken = new Token.StartTag("tag", null);
 Element expectedElement = new Element("tag");
 XMLTreeBuilder xmlTreeBuilder = new XMLTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", new ParseErrorList());
 xmlTreeBuilder.tokeniser = tokeniser;
 xmlTreeBuilder.process(startTagToken);
 Element actualElement = xmlTreeBuilder.stack.peek();
 assertEquals(expectedElement.nodeName(), actualElement.nodeName());
 }

 @Test
 public void testProcessEndTag() {
 Token endTagToken = new Token.EndTag("tag");
 XMLTreeBuilder xmlTreeBuilder = new XMLTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", new ParseErrorList());
 xmlTreeBuilder.tokeniser = tokeniser;
 xmlTreeBuilder.popStackToClose(endTagToken);
 assertNull(xmlTreeBuilder.stack.peek());
 }

 @Test
 public void testProcessComment() {
 Token commentToken = new Token.Comment("comment");
 XMLTreeBuilder xmlTreeBuilder = new XMLTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", new ParseErrorList());
 xmlTreeBuilder.tokeniser = tokeniser;
 xmlTreeBuilder.process(commentToken);
 assertNotNull(xmlTreeBuilder.stack.peek());
 }

 @Test
 public void testProcessCharacter() {
 Token characterToken = new Token.Character('c');
 XMLTreeBuilder xmlTreeBuilder = new XMLTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", new ParseErrorList());
 xmlTreeBuilder.tokeniser = tokeniser;
 xmlTreeBuilder.process(characterToken);
 Element root = doc.getRootElement();
 assertNotNull(root.text());
 assertEquals("c", root.text());
 }

 @Test
 public void testProcessDoctype() {
 Token docTypeToken = new Token.Doctype("doctype");
 XMLTreeBuilder xmlTreeBuilder = new XMLTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", new ParseErrorList());
 xmlTreeBuilder.tokeniser = tokeniser;
 xmlTreeBuilder.process(docTypeToken);
 DocumentType expectedDocumentType = new DocumentType("doctype", null, null, "");
 DocumentType actualDocumentType = (DocumentType) xmlTreeBuilder.stack.peek();
 assertEquals(expectedDocumentType.nodeName(), actualDocumentType.nodeName());
 }

 @Test(expected = IllegalStateException.class)
 public void testProcessUnexpectedTokenType() {
 Token unexpectedToken = new Token(123);
 XMLTreeBuilder xmlTreeBuilder = new XMLTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", new ParseErrorList());
 xmlTreeBuilder.tokeniser = tokeniser;
 xmlTreeBuilder.process(unexpectedToken);
 }

 @Test
 public void testPopStackToCloseUnknownTag() {
 XMLTreeBuilder xmlTreeBuilder = new XMLTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", new ParseErrorList());
 xmlTreeBuilder.tokeniser = tokeniser;
 xmlTreeBuilder.stack.add(new Element("tag"));
 xmlTreeBuilder.popStackToClose(new Token.EndTag("tag"));
 assertNull(xmlTreeBuilder.stack.peek());
 }

 @Test
 public void testPopStackToCloseUnknownTagWithRememberSelfClosingFlag() {
 XMLTreeBuilder xmlTreeBuilder = new XMLTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", new ParseErrorList());
 xmlTreeBuilder.tokeniser = tokeniser;
 xmlTreeBuilder.stack.add(new Element("tag"));
 xmlTreeBuilder.tokeniser.setSelfClosingFlag(true);
 xmlTreeBuilder.popStackToClose(new Token.EndTag("tag"));
 assertNull(xmlTreeBuilder.stack.peek());
 }

 @Test
 public void testPopStackToCloseKnownTag() {
 XMLTreeBuilder xmlTreeBuilder = new XMLTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", new ParseErrorList());
 xmlTreeBuilder.tokeniser = tokeniser;
 xmlTreeBuilder.stack.add(new Element("tag"));
 xmlTreeBuilder.stack.add(new Element("tag"));
 xmlTreeBuilder.popStackToClose(new Token.EndTag("tag"));
 Element expectedElement = new Element("tag");
 assertEquals(expectedElement, xmlTreeBuilder.stack.peek());
 }

 @Test
 public void testPopStackToCloseKnownTagWithSelfClosingFlag() {
 XMLTreeBuilder xmlTreeBuilder = new XMLTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", new ParseErrorList());
 xmlTreeBuilder.tokeniser = tokeniser;
 xmlTreeBuilder.stack.add(new Element("tag"));
 xmlTreeBuilder.stack.add(new Element("tag"));
 xmlTreeBuilder.tokeniser.setSelfClosingFlag(true);
 xmlTreeBuilder.popStackToClose(new Token.EndTag("tag"));
 assertNull(xmlTreeBuilder.stack.peek());
 }

 @Test
 public void testPopStackToCloseUnknownTagWithSelfClosingFlag() {
 XMLTreeBuilder xmlTreeBuilder = new XMLTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", new ParseErrorList());
 xmlTreeBuilder.tokeniser = tokeniser;
 xmlTreeBuilder.stack.add(new Element("tag"));
 xmlTreeBuilder.tokeniser.setSelfClosingFlag(true);
 xmlTreeBuilder.popStackToClose(new Token.EndTag("tag"));
 Element expectedElement = new Element("tag");
 expectedElement.setSelfClosing(true);
 assertEquals(expectedElement, xmlTreeBuilder.stack.peek());
 }

 @Test(expected = IllegalStateException.class)
 public void testProcessEndTagWithEmptyStack() {
 XMLTreeBuilder xmlTreeBuilder = new XMLTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", new ParseErrorList());
 xmlTreeBuilder.tokeniser = tokeniser;
 xmlTreeBuilder.popStackToClose(new Token.EndTag("tag"));
 }

 @Test
 public void testProcessEndTagWithNoMatchingTag() {
 XMLTreeBuilder xmlTreeBuilder = new XMLTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", new ParseErrorList());
 xmlTreeBuilder.tokeniser = tokeniser;
 xmlTreeBuilder.stack.add(new Element("tag1"));
 xmlTreeBuilder.stack.add(new Element("tag2"));
 xmlTreeBuilder.popStackToClose(new Token.EndTag("tag1"));
 assertEquals(1, xmlTreeBuilder.stack.size());
 }

 @Test
 public void testProcessEndTagWithMatchingTag() {
 XMLTreeBuilder xmlTreeBuilder = new XMLTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", new ParseErrorList());
 xmlTreeBuilder.tokeniser = tokeniser;
 xmlTreeBuilder.stack.add(new Element("tag1"));
 xmlTreeBuilder.stack.add(new Element("tag2"));
 xmlTreeBuilder.stack.add(new Element("tag1"));
 xmlTreeBuilder.popStackToClose(new Token.EndTag("tag1"));
 assertEquals(1, xmlTreeBuilder.stack.size());
 }

 @Test
 public void testInsertNode() {
 Element parentElement = new Element("parent");
 Node childNode = new TextNode("child", "");
 xmlTreeBuilder.insertNode(childNode);
 parentElement.appendChild(childNode);
 assertEquals(childNode, parentElement.child(0));
 }

 @Test
 public void testConstructElement() {
 Token startTag = new Token.StartTag("tag", null);
 Element expectedElement = new Element("tag");
 Element actualElement = xmlTreeBuilder.insert(startTag);
 assertEquals(expectedElement.nodeName(), actualElement.nodeName());
 }

 @Test
 public void testConstructComment() {
 Token commentToken = new Token.Comment("comment");
 Comment expectedComment = new Comment("comment", "");
 Comment actualComment = xmlTreeBuilder.insert(commentToken);
 assertEquals(expectedComment.getData(), actualComment.getData());
 }

 @Test
 public void testConstructTextNode() {
 Token characterToken = new Token.Character('c');
 TextNode expectedTextNode = new TextNode("c", "");
 TextNode actualTextNode = xmlTreeBuilder.insert(characterToken);
 assertEquals(expectedTextNode.getData(), actualTextNode.getData());
 }

 @Test
 public void testConstructDocumentType() {
 Token docTypeToken = new Token.Doctype("doctype");
 DocumentType expectedDocumentType = new DocumentType("doctype", null, null, "");
 DocumentType actualDocumentType = xmlTreeBuilder.insert(docTypeToken);
 assertEquals(expectedDocumentType.nodeName(), actualDocumentType.nodeName());
 }

 @Test
 public void testPopStackToCloseMultipleTags() {
 XMLTreeBuilder xmlTreeBuilder = new XMLTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", new ParseErrorList());
 xmlTreeBuilder.tokeniser = tokeniser;
 xmlTreeBuilder.stack.add(new Element("tag1"));
 xmlTreeBuilder.stack.add(new Element("tag2"));
 xmlTreeBuilder.stack.add(new Element("tag1"));
 List<Token.EndTag> endTags = new ArrayList<>();
 endTags.add(new Token.EndTag("tag1"));
 endTags.add(new Token.EndTag("tag2"));
 for (Token.EndTag endTag : endTags) {
 xmlTreeBuilder.popStackToClose(endTag);
 }
 assertNull(xmlTreeBuilder.stack.peek());
 }

 @Test
 public void testProcessEmptyFile() {
 XMLTreeBuilder xmlTreeBuilder = new XMLTreeBuilder();
 xmlTreeBuilder.initialiseParse("", "", new ParseErrorList());
 xmlTreeBuilder.tokeniser = tokeniser;
 xmlTreeBuilder.process(new Token.EOF());
 assertEquals(0, xmlTreeBuilder.stack.size());
 }
}