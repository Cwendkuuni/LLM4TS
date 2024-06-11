import org.jsoup.Jsoup;
import org.jsoup.helper.StringUtil;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Attributes;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DocumentTypeTest {

 @Test
 public void testDocumentTypeCreationWithValidArguments() {
 DocumentType documentType = new DocumentType("html", "HTML TDNG", null, null);

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr(NAME));
 assertNotNull(documentType.attr(PUBLIC_ID));
 assertNotNull(documentType.attr(SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithEmptyPublicId() {
 DocumentType documentType = new DocumentType("html", "", null, null);

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr(NAME));
 assertNull(documentType.attr(PUBLIC_ID));
 assertNotNull(documentType.attr(SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithEmptySystemId() {
 DocumentType documentType = new DocumentType("html", null, "", null);

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr(NAME));
 assertNotNull(documentType.attr(PUBLIC_ID));
 assertNull(documentType.attr(SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeCreationWithBothPublicIdAndSystemId() {
 DocumentType documentType = new DocumentType("html", "HTML TDNG", "http://example.com/DTD.dtd", null);

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr(NAME));
 assertNotNull(documentType.attr(PUBLIC_ID));
 assertNotNull(documentType.attr(SYSTEM_ID));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeCreationWithNullName() {
 DocumentType documentType = new DocumentType(null, "HTML TDNG", null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDocumentTypeCreationWithNullPublicId() {
 DocumentType documentType = new DocumentType("html", null, null, null);
 }

 @Test
 public void testDocumentTypeCreationWithEmptyBaseUri() {
 DocumentType documentType = new DocumentType("html", "HTML TDNG", null, "");

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr(NAME));
 assertNull(documentType.attr(PUBLIC_ID));
 assertNull(documentType.attr(SYSTEM_ID));
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithPublicId() {
 Document document = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
 DocumentType documentType = document.getDocumentType();

 StringBuilder expected = new StringBuilder();
 expected.append("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");

 StringBuilder actual = new StringBuilder();
 try {
 documentType.outerHtmlHead(actual, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.xml));
 } catch (IOException e) {
 e.printStackTrace();
 }

 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithSystemId() {
 Document document = Jsoup.parse("<!DOCTYPE html SYSTEM \"about:legacy-compat\">");
 DocumentType documentType = document.getDocumentType();

 StringBuilder expected = new StringBuilder();
 expected.append("<!DOCTYPE html SYSTEM \"about:legacy-compat\">");

 StringBuilder actual = new StringBuilder();
 try {
 documentType.outerHtmlHead(actual, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.xml));
 } catch (IOException e) {
 e.printStackTrace();
 }

 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithNoPublicIdNorSystemId() {
 Document document = Jsoup.parse("<!doctype html>");
 DocumentType documentType = document.getDocumentType();

 StringBuilder expected = new StringBuilder();
 expected.append("<!doctype html>");

 StringBuilder actual = new StringBuilder();
 try {
 documentType.outerHtmlHead(actual, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 } catch (IOException e) {
 e.printStackTrace();
 }

 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlHeadWithHtmlSyntax() {
 Document document = Jsoup.parse("<!doctype html>");
 DocumentType documentType = document.getDocumentType();

 StringBuilder expected = new StringBuilder();
 expected.append("<!DOCTYPE html>");

 StringBuilder actual = new StringBuilder();
 try {
 documentType.outerHtmlHead(actual, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 } catch (IOException e) {
 e.printStackTrace();
 }

 assertEquals(expected.toString(), actual.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlTail() {
 Document document = Jsoup.parse("<!DOCTYPE html>");
 DocumentType documentType = document.getDocumentType();

 StringBuilder actual = new StringBuilder();
 try {
 documentType.outerHtmlTail(actual, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.html));
 } catch (IOException e) {
 e.printStackTrace();
 }

 assertEquals("", actual.toString());
 }

 @Test
 public void testDocumentTypeOuterHtmlTailWithXmlSyntax() {
 Document document = Jsoup.parse("<!DOCTYPE html>");
 DocumentType documentType = document.getDocumentType();

 StringBuilder actual = new StringBuilder();
 try {
 documentType.outerHtmlTail(actual, 0, Document.OutputSettings.syntax(Document.OutputSettings.Syntax.xml));
 } catch (IOException e) {
 e.printStackTrace();
 }

 assertEquals("", actual.toString());
 }

 @Test
 public void testHasAttributeWithValidPublicId() {
 Document document = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">");
 DocumentType documentType = document.getDocumentType();

 assertTrue(documentType.has(PUBLIC_ID));
 }

 @Test
 public void testHasAttributeWithValidSystemId() {
 Document document = Jsoup.parse("<!DOCTYPE html SYSTEM \"about:legacy-compat\">");
 DocumentType documentType = document.getDocumentType();

 assertTrue(documentType.has(SYSTEM_ID));
 }

 @Test
 public void testHasAttributeWithEmptyPublicId() {
 Document document = Jsoup.parse("<!DOCTYPE html>");
 DocumentType documentType = document.getDocumentType();

 assertFalse(documentType.has(PUBLIC_ID));
 }

 @Test
 public void testHasAttributeWithEmptySystemId() {
 Document document = Jsoup.parse("<!DOCTYPE html>");
 DocumentType documentType = document.getDocumentType();

 assertFalse(documentType.has(SYSTEM_ID));
 }

 @Test
 public void testHasAttributeWithNoPublicIdNorSystemId() {
 Document document = Jsoup.parse("<!DOCTYPE html>");
 DocumentType documentType = document.getDocumentType();

 assertFalse(documentType.has(PUBLIC_ID));
 assertFalse(documentType.has(SYSTEM_ID));
 }

 @Test
 public void testHasAttributeWithNullDocumentType() {
 Document document = Jsoup.parse("<html><head></head><body></body></html>");
 assertFalse(document.getDocumentType().has(PUBLIC_ID));
 }
}