package org.jsoup.parser;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

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

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document doc;
 private Tokenizer tokeniser;

 @Before
 public void setUp() {
 xmlTreeBuilder = new XmlTreeBuilder();
 doc = new Document();
 tokeniser = new Tokenizer("<?xml version=\"1.0\"?>\n<root>\n<element attr=\"value\"/>\n<comment>this is a comment</comment>\n<element2/>\n</root>");
 xmlTreeBuilder.initialiseParse(tokeniser.input(), null, null);
 }

 @Test
 public void testInitialiseParse() {
 assertNotNull(xmlTreeBuilder.doc);
 }

 @Test
 public void testProcessStartTag() {
 Token startTag = tokeniser.nextToken();
 Element element = xmlTreeBuilder.insert(startTag.asStartTag());
 assertEquals("<element attr=\"value\"/>", element.outerHtml());
 }

 @Test
 public void testProcessSelfClosingStartTag() {
 Token selfClosingStartTag = tokeniser.nextToken();
 xmlTreeBuilder.insert(selfClosingStartTag.asStartTag());
 assertEquals("<element/>", doc.select("element").first().outerHtml());
 }

 @Test
 public void testProcessEndTag() {
 Token endTag = tokeniser.nextToken();
 xmlTreeBuilder.popStackToClose(endTag.asEndTag());
 assertEquals(0, doc.body().childNodes().size());
 }

 @Test
 public void testProcessComment() {
 Token comment = tokeniser.nextToken();
 xmlTreeBuilder.insert(comment.asComment());
 assertEquals("this is a comment", doc.select("comment").text());
 }

 @Test
 public void testProcessCharacter() {
 Token character = tokeniser.nextToken();
 xmlTreeBuilder.insert(character.asCharacter());
 assertEquals(" ", doc.select("text( )").text());
 }

 @Test
 public void testProcessDoctype() {
 Token doctype = tokeniser.nextToken();
 xmlTreeBuilder.insert(doctype.asDoctype());
 AssertDoctype("<?xml version=\"1.0\"?>", doc.doctype());
 }

 @Test
 public void testProcessEOF() {
 Token eof = tokeniser.nextToken();
 assertNull(xmlTreeBuilder.insert(eof));
 }

 @Test
 public void testPopStackToClose() {
 // Given a stack: root -> element -> comment -> element2
 Token startTagElement2 = tokeniser.nextToken();
 xmlTreeBuilder.insert(startTagElement2.asStartTag());
 Token endTagElement = tokeniser.nextToken();
 xmlTreeBuilder.popStackToClose(endTagElement.asEndTag());
 assertNotNull(doc.getElementById("element"));
 // Now the stack should be: root -> element
 assertNull(doc.getElementById("comment"));
 assertNull(doc.getElementById("element2"));
 }

 private void AssertDoctype(String expected, DocumentType documentType) {
 String actual = documentType.toString();
 assertTrue(actual.contains(expected));
 }
}