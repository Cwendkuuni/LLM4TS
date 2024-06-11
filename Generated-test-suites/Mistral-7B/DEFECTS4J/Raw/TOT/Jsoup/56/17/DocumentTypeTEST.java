import org.junit.Test;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreation() {
 Document document = Jsoup.parse("<!doctype html public \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
 DocumentType htmlDoctype = (DocumentType) document.child(0);

 assertEquals("#doctype", htmlDoctype.nodeName());
 assertEquals("html", htmlDoctype.attr(NAME));
 assertEquals("-//W3C//DTD HTML 4.01//EN", htmlDoctype.attr(PUBLIC_ID));
 assertEquals("http://www.w3.org/TR/html4/strict.dtd", htmlDoctype.attr(SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithPublicIdAndSystemIdEmpty() {
 Document document = Jsoup.parse("<!doctype html>");
 DocumentType htmlDoctype = (DocumentType) document.child(0);

 assertEquals("#doctype", htmlDoctype.nodeName());
 assertNull(htmlDoctype.attr(PUBLIC_ID));
 assertNull(htmlDoctype.attr(SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithName() {
 Document document = Jsoup.parse("<!doctype html [<!ENTITY html SYMBOL \"html\">]>");
 DocumentType htmlDoctype = (DocumentType) document.child(0);

 assertEquals("#doctype", htmlDoctype.nodeName());
 assertEquals("html", htmlDoctype.attr(NAME));
 assertNull(htmlDoctype.attr(PUBLIC_ID));
 assertNull(htmlDoctype.attr(SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithPublicIdAndSystemId() {
 Document document = Jsoup.parse("<!doctype html PUBLIC \"-//W3C//DTD HTML 4.01//EN\" \"http://www.w3.org/TR/html4/strict.dtd\">");
 DocumentType htmlDoctype = (DocumentType) document.child(0);

 assertEquals("#doctype", htmlDoctype.nodeName());
 assertEquals("html", htmlDoctype.attr(NAME));
 assertEquals("-//W3C//DTD HTML 4.01//EN", htmlDoctype.attr(PUBLIC_ID));
 assertEquals("http://www.w3.org/TR/html4/strict.dtd", htmlDoctype.attr(SYSTEM_ID));
 }

 @Test(expected = AssertionError.class)
 public void testDocumentTypeCreationWithEmptyName() {
 Document document = Jsoup.parse("<!doctype >");
 DocumentType htmlDoctype = (DocumentType) document.child(0);

 assertEquals("#doctype", htmlDoctype.nodeName());
 assertNotNull(htmlDoctype.attr(NAME)); //should throw an AssertionError as the name attribute is empty
 }

 @Test
 public void testDocumentTypeCreationWithInvalidSyntax() {
 Document document = Jsoup.parse("<!badSyntax>");
 DocumentType htmlDoctype = (DocumentType) document.child(0);

 assertEquals("#doctype", htmlDoctype.nodeName());
 assertNull(htmlDoctype.attr(NAME));
 assertNull(htmlDoctype.attr(PUBLIC_ID));
 assertNull(htmlDoctype.attr(SYSTEM_ID));
 }
}