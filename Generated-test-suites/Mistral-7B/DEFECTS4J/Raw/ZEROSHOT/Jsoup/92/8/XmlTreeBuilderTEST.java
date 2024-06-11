package org.jsoup.parser;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(Parameterized.class)
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Document expectedDocument;

 @Parameterized.Parameters(name = "{index}: Test case with input={0}")
 public static Iterable<Object[]> data() {
 return Arrays.asList(new Object[][]{
 // add test cases here
 });
 }

 @Parameterized.Parameter(0)
 public String inputXml;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 expectedDocument = parseExpectedXml();
 }

 @Test
 public void testParse() throws IOException {
 Document actualDocument = xmlTreeBuilder.parse(new StringReader(inputXml));
 assertEquals(expectedDocument, actualDocument);
 }

 @Test
 public void testParseFragment() throws IOException {
 List<Node> expectedFragment = expectedDocument.childNodes();
 List<Node> actualFragment = xmlTreeBuilder.parseFragment(inputXml, null, null);
 assertEquals(expectedFragment, actualFragment);
 }

 @Test
 public void testInitialiseParse() throws IOException {
 ByteArrayInputStream inputStream = new ByteArrayInputStream(inputXml.getBytes());
 ByteArrayReader inputReader = new ByteArrayReader(inputStream.readAllBytes());
 xmlTreeBuilder.initialiseParse(inputReader, null, null);
 assertNotNull(xmlTreeBuilder.doc);
 }

 @Test
 public void testProcessStartTag() throws IOException {
 // add test cases here
 }

 @Test
 public void testProcessEndTag() throws IOException {
 // add test cases here
 }

 @Test
 public void testProcessComment() throws IOException {
 // add test cases here
 }

 @Test
 public void testProcessCharacter() throws IOException {
 // add test cases here
 }

 @Test
 public void testProcessDoctype() throws IOException {
 // add test cases here
 }

 @Test
 public void testInsertNode() throws IOException {
 // add test cases here
 }

 @Test
 public void testInsertElement() throws IOException {
 // add test cases here
 }

 @Test
 public void testInsertComment() throws IOException {
 // add test cases here
 }

 @Test
 public void testInsertCharacter() throws IOException {
 // add test cases here
 }

 @Test
 public void testInsertDoctype() throws IOException {
 // add test cases here
 }

 @Test
 public void testPopStackToClose() throws IOException {
 // add test cases here
 }

 private Document parseExpectedXml() throws IOException {
 // parse the expected xml document and return it
 return null; // replace this with the expected document
 }

}