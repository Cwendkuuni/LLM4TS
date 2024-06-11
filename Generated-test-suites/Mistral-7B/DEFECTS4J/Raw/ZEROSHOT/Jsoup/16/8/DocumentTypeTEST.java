import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import static org.junit.Assert.*;

public class DocumentTypeTest {

 private Document document;

 @Before
 public void setUp() {
 document = new Document();
 }

 @Test
 public void testDocumentTypeCreation() {
 DocumentType documentType = new DocumentType("html", "publicID", "systemID", "baseUri");
 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertEquals("publicID", documentType.attr("publicId"));
 assertEquals("systemID", documentType.attr("systemId"));
 assertEquals("baseUri", documentType.baseUri());
 }

 @Test
 public void testEmptyDocumentTypeCreation() {
 DocumentType documentType = new DocumentType("html");
 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertNull(documentType.attr("publicId"));
 assertNull(documentType.attr("systemId"));
 assertNull(documentType.baseUri());
 }

 @Test
 public void testOuterHtmlHead() {
 StringBuilder expectedOutput = new StringBuilder();
 DocumentType documentType = new DocumentType("html", "publicID", "systemID", "baseUri");
 expectedOutput.append("<!DOCTYPE html");
 expectedOutput.append(" PUBLIC \"publicID\"");
 expectedOutput.append(" \"systemID\"");
 expectedOutput.append(">");
 documentType.outerHtmlHead(expectedOutput, 0, null);
 assertEquals(expectedOutput.toString(), documentType.outerHtml());
 }

 @Test
 public void testOuterHtmlTail() {
 DocumentType documentType = new DocumentType("html");
 StringBuilder expectedOutput = new StringBuilder();
 documentType.outerHtmlTail(expectedOutput, 0, null);
 assertEquals("", expectedOutput.toString());
 }

 @Test
 public void testOuterHtmlWithPublicIdAndSystemId() {
 DocumentType documentType = new DocumentType("html", "publicID", "systemID", "baseUri");
 StringBuilder expectedOutput = new StringBuilder();
 documentType.outerHtmlHead(expectedOutput, 0, null);
 documentType.outerHtmlTail(expectedOutput, 0, null);
 expectedOutput.append(System.lineSeparator());
 documentType.outerHtml(expectedOutput, 0, null);
 String output = expectedOutput.toString();
 assertTrue(output.contains("<!DOCTYPE htmlPUBLIC \"publicID\" \"systemID\""));
 }

 @Test
 public void testOuterHtmlWithOnlyPublicId() {
 DocumentType documentType = new DocumentType("html", "publicID", "", "baseUri");
 StringBuilder expectedOutput = new StringBuilder();
 documentType.outerHtmlHead(expectedOutput, 0, null);
 documentType.outerHtmlTail(expectedOutput, 0, null);
 expectedOutput.append(System.lineSeparator());
 documentType.outerHtml(expectedOutput, 0, null);
 String output = expectedOutput.toString();
 assertTrue(output.contains("<!DOCTYPE htmlPUBLIC \"publicID\""));
 }

 @Test
 public void testOuterHtmlWithOnlySystemId() {
 DocumentType documentType = new DocumentType("html", "", "systemID", "baseUri");
 StringBuilder expectedOutput = new StringBuilder();
 documentType.outerHtmlHead(expectedOutput, 0, null);
 documentType.outerHtmlTail(expectedOutput, 0, null);
 expectedOutput.append(System.lineSeparator());
 documentType.outerHtml(expectedOutput, 0, null);
 String output = expectedOutput.toString();
 assertTrue(output.contains("<!DOCTYPE html SYSTEM \"systemID\""));
 }

 @Test
 public void testOuterHtmlWithEmptyPublicIdAndSystemId() {
 DocumentType documentType = new DocumentType("html", "", "", "baseUri");
 StringBuilder expectedOutput = new StringBuilder();
 documentType.outerHtmlHead(expectedOutput, 0, null);
 documentType.outerHtmlTail(expectedOutput, 0, null);
 expectedOutput.append(System.lineSeparator());
 documentType.outerHtml(expectedOutput, 0, null);
 String output = expectedOutput.toString();
 assertEquals("<!DOCTYPE html>\n", output);
 }

 @Test
 public void testOuterHtmlWithNullPublicIdAndSystemId() {
 DocumentType documentType = new DocumentType("html", null, null, "baseUri");
 StringBuilder expectedOutput = new StringBuilder();
 documentType.outerHtmlHead(expectedOutput, 0, null);
 documentType.outerHtmlTail(expectedOutput, 0, null);
 expectedOutput.append(System.lineSeparator());
 documentType.outerHtml(expectedOutput, 0, null);
 String output = expectedOutput.toString();
 assertEquals("<!DOCTYPE html>\n", output);
 }
}