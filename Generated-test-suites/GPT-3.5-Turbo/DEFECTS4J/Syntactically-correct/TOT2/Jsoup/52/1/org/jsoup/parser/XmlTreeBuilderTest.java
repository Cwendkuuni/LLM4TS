package org.jsoup.parser;

import static org.junit.Assert.*;
import org.junit.Test;

public class XmlTreeBuilderTest {
  
  @Test
  public void testInsertNode() {
      XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
      Element element = new Element(Tag.valueOf("test"), "http://example.com", new Attributes());
      xmlTreeBuilder.insertNode(element);
      assertEquals(element, xmlTreeBuilder.currentElement().childNode(0));
  }

  @Test
  public void testInsertStartTag() {
      XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
      Token.StartTag startTag = new Token.StartTag("test", new Attributes(), false);
      Element element = xmlTreeBuilder.insert(startTag);
      assertEquals("test", element.tagName());
      assertFalse(element.tag().isKnownTag());
  }

  @Test
  public void testInsertComment() {
      XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
      Token.Comment comment = new Token.Comment("Test comment", "http://example.com");
      xmlTreeBuilder.insert(comment);
      assertEquals(Comment.class, xmlTreeBuilder.currentElement().childNode(0).getClass());
  }

  @Test
  public void testInsertCharacter() {
      XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
      Token.Character character = new Token.Character("Test character data");
      xmlTreeBuilder.insert(character);
      assertEquals(TextNode.class, xmlTreeBuilder.currentElement().childNode(0).getClass());
  }

  @Test
  public void testInsertDoctype() {
      XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
      Token.Doctype doctype = new Token.Doctype("html", "", "", "http://example.com");
      xmlTreeBuilder.insert(doctype);
      assertEquals(DocumentType.class, xmlTreeBuilder.currentElement().childNode(0).getClass());
  }

  @Test
  public void testPopStackToClose() {
      XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
      Token.EndTag endTag = new Token.EndTag("test");
      xmlTreeBuilder.stack.add(new Element(Tag.valueOf("test"), "http://example.com", new Attributes()));
      xmlTreeBuilder.popStackToClose(endTag);
      assertTrue(xmlTreeBuilder.stack.isEmpty());
  }

  @Test
  public void testParseFragment() {
      XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
      String inputFragment = "<test></test>";
      List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, "http://example.com", new ParseErrorList());
      assertEquals(1, nodes.size());
  }

}