package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.*;
import org.jsoup.parser.XmlTreeBuilder;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

 private XmlTreeBuilder xmlTreeBuilder;

 @Before
 public void setup() {
 xmlTreeBuilder = new XmlTreeBuilder();
 }

 @Test
 public void testInitialiseParse() {
 String input = "<root><child/></root>";
 String baseUri = "baseUrl";
 ParseErrorList errors = new ParseErrorList();
 xmlTreeBuilder.initialiseParse(input, baseUri, errors);
 assertEquals("baseUrl", xmlTreeBuilder.doc.baseUri());
 assertNotNull(xmlTreeBuilder.doc.outputSettings().syntax());
 assertEquals(Document.OutputSettings.Syntax.xml, xmlTreeBuilder.doc.outputSettings().syntax());
 }

 @Test
 public void testProcessStartTag() {
 String input = "<root><child></child></root>";
 Token startTag = new Token.StartTag("root");
 xmlTreeBuilder.process(startTag);
 Element root = (Element) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
 assertEquals("root", root.nodeName());
 assertNull(root.parentNode());
 assertNull(root.nextSibling());
 assertNull(root.previousSibling());
}

 @Test
 public void testProcessEndTag() {
 String input = "<root><child/></root>";
 Token endTag = new Token.EndTag("root");
 xmlTreeBuilder.stack.add(new Element("root", "baseUrl"));
 xmlTreeBuilder.process(endTag);
 Element root = (Element) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 2);
 assertEquals("root", root.nodeName());
 assertNotNull(root.parentNode());
 assertNull(root.nextSibling());
 assertNull(root.previousSibling());
}

 @Test
 public void testProcessComment() {
 String input = "<!--comment-->";
 Token commentToken = new Token.Comment("comment");
 xmlTreeBuilder.process(commentToken);
 Comment comment = (Comment) xmlTreeBuilder.stack.get(xmlTreebuilder.stack.size() - 1);
 assertEquals("comment", comment.getData());
 assertNull(comment.parentNode());
 assertNull(comment.nextSibling());
 assertNull(comment.previousSibling());
}

 @Test
 public void testProcessCharacter() {
 String input = "test";
 Token characterToken = new Token.Character('t');
 xmlTreeBuilder.process(characterToken);
 TextNode textNode = (TextNode) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
 assertEquals("test", textNode.text());
 assertNull(textNode.parentNode());
 assertNull(textNode.nextSibling());
 assertNull(textNode.previousSibling());
}

 @Test
 public void testProcessDoctype() {
 String input = "<!DOCTYPE html SYSTEM \"about:legacy-compat\">";
 Token doctypeToken = new Token.Doctype("html", null, null, "baseUrl");
 xmlTreeBuilder.process(doctypeToken);
 DocumentType doctypeNode = (DocumentType) xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
 assertEquals("html", doctypeNode.getNodeName());
 assertEquals("about:legacy-compat", doctypeNode.getPublicIdentifier());
 assertEquals(null, doctypeNode.getSystemIdentifier());
 assertEquals("baseUrl", doctypeNode.baseUri());
}

 @Test
 public void testPopStackToClose() {
 String input = "<root><child><grandchild/></grandchild></child></root>";
 xmlTreeBuilder.stack.add(new Element("root", "baseUrl"));
 xmlTreeBuilder.stack.add(new Element("child", "baseUrl"));
 xmlTreeBuilder.stack.add(new Element("grandchild", "baseUrl"));
 xmlTreeBuilder.popStackToClose(new Token.EndTag("child"));
 Element child = xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1);
 assertEquals("child", child.nodeName());
 assertEquals(1, xmlTreeBuilder.stack.size());
 assertEquals(null, child.nextSibling());
 assertNull(child.previousSibling());
}

 @Test
 public void testParseFragment() {
 String input = "<root><child/><grandchild/></root>";
 List<Node> nodes = xmlTreeBuilder.parseFragment(input, "baseUrl", new ParseErrorList());
 assertEquals(3, nodes.size());
 Node root = nodes.get(0);
 assertEquals("root", root.nodeName());
 assertEquals(2, root.childNodes().size());
 assertNotNull(root.childNodes().get(0));
 assertNotNull(root.childNodes().get(1));
}
}