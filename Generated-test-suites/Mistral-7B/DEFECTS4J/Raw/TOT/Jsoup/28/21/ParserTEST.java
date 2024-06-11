package org.jsoup.parser;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.ParseError;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(MockitoJUnitRunner.class)
public class ParserTest {

 private Parser parser;
 private TreeBuilder treeBuilder;
 private Document document;

 @Before
 public void setup() {
 treeBuilder = Mockito.mock(TreeBuilder.class);
 parser = new Parser(treeBuilder);
 }

 @Test
 public void testParseInput_ValidHtml_ReturnsDocument() {
 String html = "<html><body><h1>Hello World</h1></body></html>";
 document = parser.parseInput(html, "http://example.com");

 assert document != null;
 assert document.body().child(0).tagName().equals("h1");
 assert document.body().child(0).text().equals("Hello World");
 }

 @Test
 public void testParseInput_InvalidHtml_ThrowsException() {
 String html = "<html><body><h1<script>alert('XSS')</script>";

 Assertions.assertThrows(IllegalArgumentException.class, () -> parser.parseInput(html, "http://example.com"));
 }

 @Test
 public void testParseInput_WithErrorTracking_ReturnsDocumentAndErrors() {
 maxErrors = 2;
 document = parser.parseInput("<html><body><script>alert('XSS')</script></body></html>", "http://example.com");
 List<ParseError> errors = parser.getErrors();

 assert document != null;
 assert document.body().child(0).tagName().equals("script");
 assert errors.size() > 0;
 assert errors.get(0).getLine() > 0;
 assert errors.get(0).getMessage().contains("script tag");
 }

 @Test
 public void testParseInput_WithMaxErrorsReached_StopsParsing() {
 maxErrors = 1;
 String html = "<html><body><script>alert('XSS')</script></body><script>alert('XSS')</script></html>";

 document = parser.parseInput(html, "http://example.com");
 List<ParseError> errors = parser.getErrors();

 assert document == null;
 assert errors.size() == 1;
 }

 @Test
 public void testSetTreeBuilder() {
 TreeBuilder newTreeBuilder = Mockito.mock(TreeBuilder.class);
 parser.setTreeBuilder(newTreeBuilder);

 ArgumentCaptor<TreeBuilder> treeBuilderArgumentCaptor = ArgumentCaptor.forClass(TreeBuilder.class);
 Mockito.verify(treeBuilder).setParent(parser);
 Mockito.verify(treeBuilder).setParent(treeBuilderArgumentCaptor.capture());
 TreeBuilder newTreeBuilderCaptor = treeBuilderArgumentCaptor.getValue();
 assert newTreeBuilderCaptor == newTreeBuilder;
 }

 @Test
 public void testIsTrackErrors() {
 assert !parser.isTrackErrors();
 parser.setTrackErrors(1);
 assert parser.isTrackErrors();
 }

 @Test
 public void testSetTrackErrors_WithZero_DisablesTracking() {
 parser.setTrackErrors(0);
 assert !parser.isTrackErrors();
 }
}