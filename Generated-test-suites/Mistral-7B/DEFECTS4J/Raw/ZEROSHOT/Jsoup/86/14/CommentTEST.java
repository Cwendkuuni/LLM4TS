package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;

import org.junit.Test;

public class CommentTest {

 @Test
 public void testCommentCreation() {
 // Test constructor with valid data
 String data = "This is a comment";
 Comment comment = new Comment(data);
 // Test nodeName method
 assertEquals("#comment", comment.nodeName());
 // Test getData method
 assertEquals(data, comment.getData());
 }

 @Test
 public void testCommentCreationWithDeprecatedConstructor() {
 // Test constructor with valid data and baseUri
 String data = "This is a comment";
 Comment comment = new Comment(data, null);
 // Test nodeName method
 assertEquals("#comment", comment.nodeName());
 // Test getData method
 assertEquals(data, comment.getData());
 }

 @Test
 public void testCommentIsXmlDeclaration() {
 // Test comment with valid data that is not an XML declaration
 Comment comment = new Comment("This is a comment");
 assertFalse(comment.isXmlDeclaration());
 // Test comment with XML declaration data
 Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\nThis is a comment");
 assertTrue(xmlDeclarationComment.isXmlDeclaration());
 }

 @Test
 public void testCommentAsXmlDeclaration() throws IOException {
 // Test comment that can be parsed as an XML declaration
 Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element/>\n</root>");
 XmlDeclaration xmlDeclaration = xmlDeclarationComment.asXmlDeclaration();
 assertNotNull(xmlDeclaration);
 // Test comment that cannot be parsed as an XML declaration
 Comment invalidXmlDeclarationComment = new Comment("This is not an XML declaration comment");
 assertNull(invalidXmlDeclarationComment.asXmlDeclaration());
 }

 @Test
 public void testCommentOuterHtml() throws IOException {
 // Test comment with empty data
 Comment emptyComment = new Comment("");
 String emptyCommentHtml = "<!---->";
 assertEquals(emptyCommentHtml, emptyComment.outerHtml());
 // Test comment with data
 Comment dataComment = new Comment("This is a comment");
 String dataCommentHtml = "<!--This is a comment-->";
 assertEquals(dataCommentHtml, dataComment.outerHtml());
 // Test comment with XML declaration data
 Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element/>\n</root>");
 String xmlDeclarationCommentHtml = "<!--<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element/>\n</root>-->";
 assertEquals(xmlDeclarationCommentHtml, xmlDeclarationComment.outerHtml());
 }

 @Test
 public void testCommentToString() throws IOException {
 // Test comment with empty data
 Comment emptyComment = new Comment("");
 String emptyCommentString = "<!---->";
 assertEquals(emptyCommentString, emptyComment.toString());
 // Test comment with data
 Comment dataComment = new Comment("This is a comment");
 String dataCommentString = "<!--This is a comment-->";
 assertEquals(dataCommentString, dataComment.toString());
 // Test comment with XML declaration data
 Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element/>\n</root>");
 String xmlDeclarationCommentString = "<!--<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element/>\n</root>-->";
 assertEquals(xmlDeclarationCommentString, xmlDeclarationComment.toString());
 }

 @Test
 public void testCommentIsInstanceOfXmlDeclaration() throws IOException {
 // Test comment that can be parsed as an XML declaration
 Comment xmlDeclarationComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<root>\n<element/>\n</root>");
 XmlDeclaration xmlDeclaration = xmlDeclarationComment.asXmlDeclaration();
 assertTrue(xmlDeclarationComment instanceof Comment);
 assertTrue(xmlDeclaration instanceof XmlDeclaration);
 // Test comment that cannot be parsed as an XML declaration
 Comment invalidXmlDeclarationComment = new Comment("This is not an XML declaration comment");
 assertFalse(invalidXmlDeclarationComment instanceof XmlDeclaration);
 }
}