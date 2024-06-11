import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.NodeUtils;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

import static org.junit.Assert.*;

public class CommentTest {

  @Test
  public void testConstructorWithData() {
    Comment comment = new Comment("Test Comment");
    assertEquals("Test Comment", comment.getData());
  }

  @Test
  public void testConstructorWithDataAndBaseUri() {
    Comment comment = new Comment("Test Comment", "http://example.com");
    assertEquals("Test Comment", comment.getData());
  }

  @Test
  public void testNodeName() {
    Comment comment = new Comment("Test Comment");
    assertEquals("#comment", comment.nodeName());
  }

  @Test
  public void testOuterHtmlHead() {
    Comment comment = new Comment("Test Comment");
    StringBuilder accum = new StringBuilder();
    Document.OutputSettings out = new Document.OutputSettings();
    try {
      comment.outerHtmlHead(accum, 0, out);
    } catch (Exception e) {
      e.printStackTrace();
    }
    assertEquals("<!--Test Comment-->", accum.toString());
  }

  @Test
  public void testOuterHtmlTail() {
    Comment comment = new Comment("Test Comment");
    StringBuilder accum = new StringBuilder();
    Document.OutputSettings out = new Document.OutputSettings();
    try {
      comment.outerHtmlTail(accum, 0, out);
    } catch (Exception e) {
      e.printStackTrace();
    }
    assertEquals("", accum.toString());
  }

  @Test
  public void testToString() {
    Comment comment = new Comment("Test Comment");
    assertEquals("<!--Test Comment-->", comment.toString());
  }

  @Test
  public void testIsXmlDeclarationWithXmlDeclarationData() {
    Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    assertTrue(comment.isXmlDeclaration());
  }

  @Test
  public void testIsXmlDeclarationWithNonXmlDeclarationData() {
    Comment comment = new Comment("Test Comment");
    assertFalse(comment.isXmlDeclaration());
  }

  @Test
  public void testAsXmlDeclarationWithValidXmlDeclarationData() {
    Comment comment = new Comment("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
    XmlDeclaration decl = comment.asXmlDeclaration();
    assertNotNull(decl);
    assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", decl.outerHtml());
  }

  @Test
  public void testAsXmlDeclarationWithInvalidXmlDeclarationData() {
    Comment comment = new Comment("Test Comment");
    XmlDeclaration decl = comment.asXmlDeclaration();
    assertNull(decl);
  }
}