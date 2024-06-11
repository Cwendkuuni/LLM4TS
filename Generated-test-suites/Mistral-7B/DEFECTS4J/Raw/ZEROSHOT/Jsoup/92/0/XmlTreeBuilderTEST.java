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
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayReader;
import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(Parameterized.class)
public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;
 private Parser parser;
 private Document document;
 private Reader reader;

 @Parameterized.Parameters(name = "{index}: testing parse method with input={0}")
 public static List<Object[]> data() {
 return Arrays.asList(new Object[][]{
 // Test cases for parse method
 {"<root><tag>Hello World</tag></root>", "<root><tag>Hello World</tag></root>"},
 {"<root><tag attr='value'/><tag2/></root>", "<root><tag attr='value'/><tag2/></root>"},
 {"<root><tag attr='value'/><tag attr='value'/></root>", "<root><tag attr='value'/><tag attr='value'/></root>"},
 {"<root></root>", "<root></root>"},
 {"<root><!DOCTYPE root [ <!ELEMENT tag EMPTY> ]>", "<root></root>"},
 {"<root><!DOCTYPE root SYSTEM 'test.dtd' [ <!ELEMENT tag EMPTY> ]>", "<root></root>"},
 {"<root><!-- comment --></root>", "<root><!-- comment --></root>"},
 {"<root><![CDATA[ <tag>Hello World</tag> ]]>", "<root><tag>Hello World</tag></root>"},
 {"<root><![CDATA[ <tag attr='value'/><tag2/> ]]>", "<root><tag attr='value'/><tag2/></root>"},
 {"<root><![CDATA[ <tag attr='value'/><tag attr='value'/> ]]>", "<root><tag attr='value'/><tag attr='value'/></root>"},
 {"<root><tag attr='value'/></root>", "<root><tag attr='value'/></root>"},
 {"<root><tag attr='value'/><tag attr='value'/><tag attr='value'/></root>", "<root><tag attr='value'/><tag attr='value'/><tag attr='value'/></root>"},
 {"<root><tag attr='value'/><tag attr='value'/><tag attr='value'/></root><tag attr='value'/>", "<root><tag attr='value'/><tag attr='value'/><tag attr='value'/></root><tag attr='value'/>"},
 {"<root><tag attr='value'/><tag attr='value'/><tag attr='value'/></root><tag attr='value'/><tag attr='value'/>", "<root><tag attr='value'/><tag attr='value'/><tag attr='value'/></root><tag attr='value'/><tag attr='value'/>"},
 {"<root><tag attr='value'/><tag attr='value'/><tag attr='value'/></root><tag attr='value'/><tag attr='value'/><tag attr='value'/>", "<root><tag attr='value'/><tag attr='value'/><tag attr='value'/></root><tag attr='value'/><tag attr='value'/><tag attr='value'/>"},
 {"<root><tag attr='value'/><tag attr='value'/><tag attr='value'/></root><tag attr='value'/><tag attr='value'/><tag attr='value'/><tag attr='value'/>", "<root><tag attr='value'/><tag attr='value'/><tag attr='value'/></root><tag attr='value'/><tag attr='value'/><tag attr='value'/><tag attr='value'/>"}
 });
 }

 @Before
 public void setup() {
 parser = mock(Parser.class);
 xmlTreeBuilder = new XmlTreeBuilder();
 document = mock(Document.class);
 reader = new StringReader("");
 }

 @Test
 public void testParse() throws IOException {
 for (int i = 0; i < data().size(); i++) {
 String input = (String) data().get(i)[0];
 String expected = (String) data().get(i)[1];

 when(parser.parse(ArgumentMatchers.eq(reader), ArgumentMatchers.eq(document.baseUri()))).thenReturn(document);
 when(document.outputSettings().syntax(Document.OutputSettings.Syntax.xml)).thenReturn(document);
 when(document.childNodes()).thenReturn(new ArrayList<>());

 reader = new StringReader(input);
 xmlTreeBuilder.parse(reader, document.baseUri(), parser);
 assertEquals(expected, document.html());
 }
 }

 @Test
 public void testParseFragment() throws IOException {
 for (int i = 0; i < data().size(); i++) {
 String input = (String) data().get(i)[0];
 String baseUri = "testBaseUri";

 when(parser.parseFragment(ArgumentMatchers.eq(input), ArgumentMatchers.eq(document), ArgumentMatchers.eq(document.baseUri()), ArgumentMatchers.eq(parser))).thenReturn(Arrays.asList(document.createElement("root")));

 List<Node> result = xmlTreeBuilder.parseFragment(input, document, baseUri, parser);
 assertEquals(1, result.size());
 assertNotNull(result.get(0));
 assertSame(document.createElement("root"), result.get(0));
 }
 }

 @Test
 public void testProcessStartTag() throws IOException {
 for (int i = 0; i < data().size(); i++) {
 String input = (String) data().get(i)[0];
 String expected = (String) data().get(i)[1];

 when(parser.parse(ArgumentMatchers.eq(reader), ArgumentMatchers.eq(document.baseUri()))).thenReturn(document);
 when(document.outputSettings().syntax(Document.OutputSettings.Syntax.xml)).thenReturn(document);
 when(document.childNodes()).thenReturn(new ArrayList<>());

reader = new StringReader(input);
xmlTreeBuilder = new XmlTreeBuilder();
xmlTreeBuilder.parse(reader, document.baseUri(), parser);

List<Node> result = new ArrayList<>();
Token token;
while ((token = xmlTreeBuilder.nextToken()) != null) {
switch (token.type()) {
case StartTag:
result.add(token.asStartTag());
break;
default:
break;
}
}
assertEquals(expected.split("<")[0], result.get(0).tagName());
assertEquals(expected.substring(expected.lastIndexOf(">") + 1), result.get(result.size() - 1).tagName());
}
}

 @Test
 public void testProcessEndTag() throws IOException {
 for (int i = 0; i < data().size(); i++) {
 String input = (String) data().get(i)[0];
 String expected = (String) data().get(i)[1];

 when(parser.parse(ArgumentMatchers.eq(reader), ArgumentMatchers.eq(document.baseUri()))).thenReturn(document);
 when(document.outputSettings().syntax(Document.OutputSettings.Syntax.xml)).thenReturn(document);
 when(document.childNodes()).thenReturn(new ArrayList<>());

 reader = new StringReader(input);
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.parse(reader, document.baseUri(), parser);

 List<Node> result = new ArrayList<>();
 Token token;
 while ((token = xmlTreeBuilder.nextToken()) != null) {
 switch (token.type()) {
 case EndTag:
 result.add(token.asEndTag());
 break;
 default:
 break;
 }
 }
 assertEquals(expected.split("</")[0], result.get(0).tagName());
 }
 }

 @Test
 public void testProcessComment() throws IOException {
 for (int i = 0; i < data().size(); i++) {
 String input = (String) data().get(i)[0];
 String expected = (String) data().get(i)[1];

 when(parser.parse(ArgumentMatchers.eq(reader), ArgumentMatchers.eq(document.baseUri()))).thenReturn(document);
 when(document.outputSettings().syntax(Document.OutputSettings.Syntax.xml)).thenReturn(document);
 when(document.childNodes()).thenReturn(new ArrayList<>());

 reader = new StringReader(input);
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.parse(reader, document.baseUri(), parser);

 List<Node> result = new ArrayList<>();
 Token token;
 while ((token = xmlTreeBuilder.nextToken()) != null) {
 switch (token.type()) {
 case Comment:
 result.add(token.asComment());
 break;
 default:
 break;
 }
 }
 assertEquals(expected, result.get(0).data());
 }
 }

 @Test
 public void testProcessCharacter() throws IOException {
 for (int i = 0; i < data().size(); i++) {
 String input = (String) data().get(i)[0];
 String expected = (String) data().get(i)[1];

 when(parser.parse(ArgumentMatchers.eq(reader), ArgumentMatchers.eq(document.baseUri()))).thenReturn(document);
 when(document.outputSettings().syntax(Document.OutputSettings.Syntax.xml)).thenReturn(document);
 when(document.childNodes()).thenReturn(new ArrayList<>());

 reader = new StringReader(input);
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.parse(reader, document.baseUri(), parser);

 List<Node> result = new ArrayList<>();
 Token token;
 while ((token = xmlTreeBuilder.nextToken()) != null) {
 switch (token.type()) {
 case Character:
 result.add(token.asCharacter());
 break;
 default:
 break;
 }
 }
 assertEquals(expected, result.get(0).data());
 }
 }

 @Test
 public void testProcessDoctype() throws IOException {
 for (int i = 0; i < data().size(); i++) {
 String input = (String) data().get(i)[0];
 String expectedName = (String) data().get(i)[1].split(" ")[0];
 String expectedPublicId = (String) data().get(i)[1].split(" ")[1];
 String expectedSystemId = (String) data().get(i)[1].split(" ")[2];

 when(parser.parse(ArgumentMatchers.eq(reader), ArgumentMatchers.eq(document.baseUri()))).thenReturn(document);
 when(document.outputSettings().syntax(Document.OutputSettings.Syntax.xml)).thenReturn(document);
 when(document.childNodes()).thenReturn(new ArrayList<>());

 reader = new StringReader(input);
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.parse(reader, document.baseUri(), parser);

 Token token = xmlTreeBuilder.nextToken();
 assertSame(Token.Type.Doctype, token.type());
 assertEquals(expectedName, token.asDoctype().name());
 assertEquals(expectedPublicId, token.asDoctype().publicId());
 assertEquals(expectedSystemId, token.asDoctype().systemId());
 }
 }

 @Test
 public void testInsertNode() throws IOException {
 Element root = document.createElement("root");
 Element child = document.createElement("tag");
 xmlTreeBuilder.insertNode(child);
 assertEquals(root, child.parentNode());
 }

 @Test
 public void testInsert() throws IOException {
 String input = "<root><tag>Hello World</tag></root>";
 when(parser.parse(ArgumentMatchers.eq(reader), ArgumentMatchers.eq(document.baseUri()))).thenReturn(document);
 when(document.outputSettings().syntax(Document.OutputSettings.Syntax.xml)).thenReturn(document);
 when(document.childNodes()).thenReturn(new ArrayList<>());

 reader = new StringReader(input);
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.parse(reader, document.baseUri(), parser);

 Token startTagToken = xmlTreeBuilder.nextToken();
 Token endTagToken = xmlTreeBuilder.nextToken();

 xmlTreeBuilder.insert(startTagToken);
 xmlTreeBuilder.insert(endTagToken);

 Element root = document.getElementsByTagName("root").get(0);
 Element tag = root.getElementsByTagName("tag").get(0);
 assertEquals("Hello World", tag.textContent());
 }

 @Test
 public void testPopStackToClose() throws IOException {
 String input = "<root><tag><tag2/></tag></root>";
 when(parser.parse(ArgumentMatchers.eq(reader), ArgumentMatchers.eq(document.baseUri()))).thenReturn(document);
 when(document.outputSettings().syntax(Document.OutputSettings.Syntax.xml)).thenReturn(document);
 when(document.childNodes()).thenReturn(new ArrayList<>());

 reader = new StringReader(input);
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.parse(reader, document.baseUri(), parser);

 Element tag2 = xmlTreeBuilder.nextToken().asStartTag();
 Element tag1 = xmlTreeBuilder.nextToken().asStartTag();
 xmlTreeBuilder.nextToken(); // consume text node
 xmlTreeBuilder.nextToken(); // consume tag2 end tag
 xmlTreeBuilder.popStackToClose(tag1);

 assertEquals(0, xmlTreeBuilder.stack.size());
 }

 @Test(expected = AssertionError.class)
 public void testInvalidTokenType() throws IOException {
 String input = "<root><tag>";
 when(parser.parse(ArgumentMatchers.eq(reader), ArgumentMatchers.eq(document.baseUri()))).thenReturn(document);
 when(document.outputSettings().syntax(Document.OutputSettings.Syntax.xml)).thenReturn(document);
 when(document.childNodes()).thenReturn(new ArrayList<>());

 reader = new StringReader(input);
 xmlTreeBuilder = new XmlTreeBuilder();
 xmlTreeBuilder.parse(reader, document.baseUri(), parser);

 xmlTreeBuilder.nextToken();
 xmlTreeBuilder.nextToken(); // consume tag start tag
 xmlTreeBuilder.nextToken(); // consume text node
 xmlTreeBuilder.popStackToClose(mock(Token.EndTag.class)); // should throw AssertionError because the stack is not big enough
 }
}