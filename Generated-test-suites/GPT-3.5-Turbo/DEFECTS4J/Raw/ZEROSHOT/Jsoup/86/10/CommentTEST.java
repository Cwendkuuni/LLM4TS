
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class CommentTest {
  private Comment comment;
  
  @Before
  public void setUp() {
    comment = new Comment("Test comment");
  }

  @Test
  public void testNodeName() {
    assertEquals("#comment", comment.nodeName());
  }

  @Test
  public void testGetData() {
    assertEquals("Test comment", comment.getData());
  }

  @Test
  public void testIsXmlDeclaration() {
    assertFalse(comment.isXmlDeclaration());
  
    Comment xmlDeclComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    assertTrue(xmlDeclComment.isXmlDeclaration());
  }

  @Test
  public void testAsXmlDeclaration() {
    Comment xmlDeclComment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    XmlDeclaration xmlDecl = xmlDeclComment.asXmlDeclaration();
  
    assertNotNull(xmlDecl);
  
    Document parsedDoc = Jsoup.parse("<" + xmlDeclComment.getData().substring(1, xmlDeclComment.getData().length() - 1) + ">", "", Parser.xmlParser());
    Element parsedEl = parsedDoc.child(0);

    assertEquals(parsedEl.tagName(), xmlDecl.nodeName());
    assertEquals(parsedEl.attributes(), xmlDecl.attributes());
    assertEquals(xmlDeclComment.isXmlDeclaration(), xmlDecl.isXmlDeclaration());
  }

  @Test
  public void testToString() {
    assertEquals("<!--Test comment-->", comment.toString());
  }
}
