import org.junit.Test; import static org.junit.Assert.*; import static org.jsoup.nodes.DocumentType.PUBLIC_KEY; import static org.jsoup.nodes.DocumentType.SYSTEM_KEY; import static org.jsoup.nodes.DocumentType.NAME; import static org.jsoup.nodes.DocumentType.PUBLIC_ID; import static org.jsoup.nodes.DocumentType.SYSTEM_ID; import org.jsoup.nodes.DocumentType;

public class DocumentTypeTest {

 @Test public void testDocumentTypeCreation() {
 DocumentType docType = new DocumentType("HTML", "publicID", "systemID", null); assertEquals("#doctype", docType.nodeName()); assertEquals("HTML", docType.attr(NAME)); assertEquals("publicID", docType.attr(PUBLIC_ID)); assertEquals("systemID", docType.attr(SYSTEM_ID)); }

 @Test(expected = IllegalArgumentException.class) public void testDocumentTypeCreationWithNullName() {
 DocumentType docType = new DocumentType(null, "publicID", "systemID", null); }

 @Test(expected = IllegalArgumentException.class) public void testDocumentTypeCreationWithNullPublicId() {
 DocumentType docType = new DocumentType("HTML", null, "systemID", null); }

 @Test(expected = IllegalArgumentException.class) public void testDocumentTypeCreationWithNullSystemId() {
 DocumentType docType = new DocumentType("HTML", "publicID", null, null); }

 @Test(expected = IllegalArgumentException.class) public void testDocumentTypeCreationWithNullBaseUri() {
 DocumentType docType = new DocumentType("HTML", "publicID", "systemID", null); }

 @Test public void testDocumentTypeCreationWithEmptyName() {
 DocumentType docType = new DocumentType("", "publicID", "systemID", null); assertEquals("#doctype", docType.nodeName()); }

 @Test public void testDocumentTypeCreationWithEmptyPublicId() {
 DocumentType docType = new DocumentType("HTML", "", "systemID", null); assertEquals("#doctype", docType.nodeName()); }

 @Test public void testDocumentTypeCreationWithEmptySystemId() {
 DocumentType docType = new DocumentType("HTML", "publicID", "", null); assertEquals("#doctype", docType.nodeName()); }

 @Test public void testDocumentTypeCreationWithWhitespaceName() {
 DocumentType docType = new DocumentType(" HTML ", "publicID", "systemID", null); assertEquals("#doctype", docType.nodeName()); }

 @Test public void testDocumentTypeCreationWithWhitespacePublicId() {
 DocumentType docType = new DocumentType("HTML", " publicID ", "systemID", null); assertEquals("#doctype", docType.nodeName()); }

 @Test public void testDocumentTypeCreationWithWhitespaceSystemId() {
 DocumentType docType = new DocumentType("HTML", "publicID", " systemID ", null); assertEquals("#doctype", docType.nodeName()); }

 @Test(expected = IllegalArgumentException.class) public void testDocumentTypeCreationWithInvalidBaseUri() {
 DocumentType docType = new DocumentType("HTML", "publicID", "systemID", new String("invalidUri")); }
}