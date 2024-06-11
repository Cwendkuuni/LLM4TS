import org.junit.Test;
import static org.junit.Assert.*;

public class DocumentTypeTest {

  @Test
  public void testNodeName() {
    DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.w3.org/1999/xhtml");

    String expected = "#doctype";
    String actual = doctype.nodeName();
    assertEquals(expected, actual);
  }

  @Test
  public void testOuterHtmlHead() {
    DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.w3.org/1999/xhtml");

    StringBuilder accum = new StringBuilder();
    int depth = 0;
    Document.OutputSettings out = new Document.OutputSettings();
    
    doctype.outerHtmlHead(accum, depth, out);

    String expected = "<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" http://www.w3.org/TR/html4/strict.dtd\">";
    String actual = accum.toString();
    assertEquals(expected, actual);
  }

  @Test
  public void testOuterHtmlTail() {
    DocumentType doctype = new DocumentType("html", "-//W3C//DTD HTML 4.01//EN", "http://www.w3.org/TR/html4/strict.dtd", "http://www.w3.org/1999/xhtml");

    StringBuilder accum = new StringBuilder();
    int depth = 0;
    Document.OutputSettings out = new Document.OutputSettings();
    
    doctype.outerHtmlTail(accum, depth, out);

    String expected = "";
    String actual = accum.toString();
    assertEquals(expected, actual);
  }

}