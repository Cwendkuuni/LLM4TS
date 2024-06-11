package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
  
  private XmlTreeBuilder xmlTreeBuilder;
  
  @Before
  public void setUp() {
    xmlTreeBuilder = new XmlTreeBuilder();
  }
  
  @Test
  public void testInitialiseParse() {
    String input = "<xml><tag></tag></xml>";
    String baseUri = "http://example.com";
    ParseErrorList errors = new ParseErrorList();
    
    xmlTreeBuilder.initialiseParse(input, baseUri, errors);
    
    assertEquals(xmlTreeBuilder.stack.size(), 1);
    assertEquals(xmlTreeBuilder.stack.get(0), xmlTreeBuilder.doc);
    assertEquals(xmlTreeBuilder.doc.outputSettings().syntax(), Document.OutputSettings.Syntax.xml);
  }
  
  @Test
  public void testProcess() {
    Token startTagToken = new Token.StartTag("tag");
    Token commentToken = new Token.Comment("comment");
    Token characterToken = new Token.Character("character");
    Token doctypeToken = new Token.Doctype("html");
    
    xmlTreeBuilder.process(startTagToken);
    xmlTreeBuilder.process(commentToken);
    xmlTreeBuilder.process(characterToken);
    xmlTreeBuilder.process(doctypeToken);
    
    Element element = xmlTreeBuilder.doc.createElement("tag");
    assertEquals(xmlTreeBuilder.doc.body().child(0), element);
    
    Comment comment = xmlTreeBuilder.doc.createComment("comment");
    assertEquals(xmlTreeBuilder.doc.body().child(1), comment);
    
    TextNode textNode = xmlTreeBuilder.doc.createTextNode("character");
    assertEquals(xmlTreeBuilder.doc.body().child(2), textNode);
    
    DocumentType doctype = xmlTreeBuilder.doc.createDoctype("html");
    assertEquals(xmlTreeBuilder.doc.childNode(0), doctype);
  }
  
  @Test
  public void testInsertNode() {
    Element element = xmlTreeBuilder.doc.createElement("tag");
    
    xmlTreeBuilder.insertNode(element);
    
    assertEquals(xmlTreeBuilder.currentElement().child(0), element);
  }
  
  @Test
  public void testInsert() {
    Token.StartTag startTagToken = new Token.StartTag("tag");
    startTagToken.attributes = new Attributes();
    
    Element element = xmlTreeBuilder.insert(startTagToken);
    
    assertEquals(xmlTreeBuilder.tokeniser.selfClosingFlagAcknowledged, true);
    assertEquals(element, xmlTreeBuilder.currentElement());
    
    startTagToken = new Token.StartTag("unknownTag");
    startTagToken.attributes = new Attributes();
    
    element = xmlTreeBuilder.insert(startTagToken);
    
    assertEquals(element.tag().isSelfClosing(), true);
  }
  
  @Test
  public void testInsertComment() {
    Token.Comment commentToken = new Token.Comment("comment");
    
    xmlTreeBuilder.insert(commentToken);
    
    Comment comment = xmlTreeBuilder.doc.createComment("comment");
    assertEquals(xmlTreeBuilder.doc.body().child(0), comment);
    
    commentToken = new Token.Comment("<!DOCTYPE html>");
    
    xmlTreeBuilder.insert(commentToken);
    
    Comment doctypeComment = xmlTreeBuilder.doc.createComment("<!DOCTYPE html>");
    assertEquals(xmlTreeBuilder.doc.body().child(1), doctypeComment);
    assertNotEquals(doctypeComment.nodeName(), "#docType");
  }
  
  @Test
  public void testInsertCharacter() {
    Token.Character characterToken = new Token.Character("character");
    
    xmlTreeBuilder.insert(characterToken);
    
    TextNode textNode = xmlTreeBuilder.doc.createTextNode("character");
    assertEquals(xmlTreeBuilder.doc.body().child(0), textNode);
  }
  
  @Test
  public void testInsertDoctype() {
    Token.Doctype doctypeToken = new Token.Doctype("html");
    
    xmlTreeBuilder.insert(doctypeToken);
    
    DocumentType doctype = xmlTreeBuilder.doc.createDoctype("html");
    assertEquals(xmlTreeBuilder.doc.childNode(0), doctype);
  }
  
  @Test
  public void testPopStackToClose() {
    Element element1 = xmlTreeBuilder.doc.createElement("tag");
    Element element2 = xmlTreeBuilder.doc.createElement("tag");
    
    xmlTreeBuilder.stack.add(element1);
    xmlTreeBuilder.stack.add(element2);
    
    Token.EndTag endTag = new Token.EndTag("tag");
    
    xmlTreeBuilder.popStackToClose(endTag);
    
    assertEquals(xmlTreeBuilder.stack.size(), 1);
    assertEquals(xmlTreeBuilder.stack.get(0), element1);
    
    endTag = new Token.EndTag("unknownTag");
    
    xmlTreeBuilder.popStackToClose(endTag);
    
    assertEquals(xmlTreeBuilder.stack.size(), 1);
    assertEquals(xmlTreeBuilder.stack.get(0), element1);
  }
  
  @Test
  public void testParseFragment() {
    String inputFragment = "<tag></tag>";
    String baseUri = "http://example.com";
    ParseErrorList errors = new ParseErrorList();
    
    xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors);
    
    assertEquals(xmlTreeBuilder.stack.size(), 1);
    assertEquals(xmlTreeBuilder.stack.get(0), xmlTreeBuilder.doc);
    assertEquals(xmlTreeBuilder.doc.outputSettings().syntax(), Document.OutputSettings.Syntax.xml);
    assertEquals(xmlTreeBuilder.doc.childNodes().size(), 1);
  }
}