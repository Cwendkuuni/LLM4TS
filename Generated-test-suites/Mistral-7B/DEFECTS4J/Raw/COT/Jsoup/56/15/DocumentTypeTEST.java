package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;
import org.jsoup.helper.StringUtil;
import org.jsoup.helper.Urls;

public class DocumentTypeTest {

 private Document doc;

 @Before
 public void setup() {
 doc = new Document();
 }

 // Test constructor method
 @Test
 public void testDocumentTypeConstructor() {
 DocumentType documentType = new DocumentType("html", "publicID", "systemID", Urls.toAbsolute(Urls.protocolsFromString("http://example.com")));

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr(DocumentType.NAME));
 assertEquals("publicID", documentType.attr(DocumentType.PUBLIC_ID));
 assertEquals("systemID", documentType.attr(DocumentType.SYSTEM_ID));
 assertEquals("http://example.com", documentType.baseUri());
 }

 // Test nodeName method
 @Test
 public void testNodeName() {
 DocumentType documentType = new DocumentType("html", null, null, null);
 assertEquals("#doctype", documentType.nodeName());
 }

 // Test outerHtmlHead method
 @Test
 public void testOuterHtmlHeadHtmlSyntaxNoPublicIdAndSystemId() {
 Appendable accum = new StringBuilder();
 DocumentType documentType = new DocumentType("html", null, null, Urls.toAbsolute(Urls.protocolsFromString("http://example.com")));
 documentType.outerHtmlHead(accum, 0, Document.OutputSettings.syntax(Syntax.html));

 assertEquals("<!doctype html>", accum.toString());
 }

 @Test
 public void testOuterHtmlHeadHtmlSyntaxWithPublicId() {
 Appendable accum = new StringBuilder();
 DocumentType documentType = new DocumentType("html", "publicID", null, Urls.toAbsolute(Urls.protocolsFromString("http://example.com")));
 documentType.outerHtmlHead(accum, 0, Document.OutputSettings.syntax(Syntax.html));

 assertEquals("<!DOCTYPE html PUBLIC \"publicID\" >", accum.toString());
 }

 @Test
 public void testOuterHtmlHeadHtmSyntaxWithPublicIdAndSystemId() {
 Appendable accum = new StringBuilder();
 DocumentType documentType = new DocumentType("html", "publicID", "systemID", Urls.toAbsolute(Urls.protocolsFromString("http://example.com")));
 documentType.outerHtmlHead(accum, 0, Document.OutputSettings.syntax(Syntax.html));

 assertEquals("<!DOCTYPE html PUBLIC \"publicID\" \"systemID\" >", accum.toString());
 }

 // Test outerHtmlHead method with other syntaxes
 @Test
 public void testOuterHtmlHeadOtherSyntaxes() {
 Appendable accum = new StringBuilder();
 DocumentType documentType = new DocumentType("html", null, null, Urls.toAbsolute(Urls.protocolsFromString("http://example.com")));

 documentType.outerHtmlHead(accum, 0, Document.OutputSettings.syntax(Syntax.xml));
 assertEquals("<!DOCTYPE html>", accum.toString());

 documentType.outerHtmlHead(accum, 0, Document.OutputSettings.syntax(Syntax.xhtml11));
 assertEquals("<!DOCTYPE html SYSTEM>", accum.toString());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOuterHtmlHeadWithNullAccumulator() {
 DocumentType documentType = new DocumentType("html", null, null, null);
 documentType.outerHtmlHead(null, 0, Document.OutputSettings.syntax(Syntax.html));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOuterHtmlHeadWithNegativeDepth() {
 DocumentType documentType = new DocumentType("html", null, null, null);
 documentType.outerHtmlHead(accum, -1, Document.OutputSettings.syntax(Syntax.html));
 }

 // Test outerHtmlTail method
 @Test
 public void testOuterHtmlTail() {
 Appendable accum = new StringBuilder();
 DocumentType documentType = new DocumentType("html", null, null, null);
 documentType.outerHtmlTail(accum, 0, Document.OutputSettings.syntax(Syntax.html));

 assertNull(accum.toString());
 }

 // Test has method
 @Test
 public void testHasAttribute() {
 DocumentType documentType = new DocumentType("html", "publicID", "systemID", Urls.toAbsolute(Urls.protocolsFromString("http://example.com")));

 assertTrue(documentType.has(DocumentType.NAME));
 assertTrue(documentType.has(DocumentType.PUBLIC_ID));
 assertTrue(documentType.has(DocumentType.SYSTEM_ID));

 assertFalse(documentType.has("nonExistentAttribute"));
 }

 @Test
 public void testHasAttributeWithNullAttribute() {
 DocumentType documentType = new DocumentType("html", "publicID", "systemID", Urls.toAbsolute(Urls.protocolsFromString("http://example.com")));

 assertFalse(documentType.has(null));
 }

 @Test(expected = NullPointerException.class)
 public void testHasAttributeWithNullDocumentType() {
 DocumentType documentType = null;

 assertFalse(documentType.has(DocumentType.NAME));
 }

 // Test attr method with valid attributes
 @Test
 public void testAttrWithValidAttributes() {
 DocumentType documentType = new DocumentType("html", "publicID", "systemID", Urls.toAbsolute(Urls.protocolsFromString("http://example.com")));

 assertEquals("publicID", documentType.attr(DocumentType.PUBLIC_ID));
 assertEquals("systemID", documentType.attr(DocumentType.SYSTEM_ID));
 }

 // Test attr method with null documentType
 @Test(expected = NullPointerException.class)
 public void testAttrWithNullDocumentType() {
 DocumentType documentType = null;

 assertNull(documentType.attr(DocumentType.PUBLIC_ID));
 }

 // Test attr method with non-existent attribute
 @Test
 public void testAttrWithNonExistentAttribute() {
 DocumentType documentType = new DocumentType("html", "publicID", "systemID", Urls.toAbsolute(Urls.protocolsFromString("http://example.com")));

 assertNull(documentType.attr("nonExistentAttribute"));
 }

 // Test constructor with blank arguments
 @Test
 public void testConstructorWithBlankArguments() {
 DocumentType documentType = new DocumentType(null, null, null, null);

 assertEquals("#doctype", documentType.nodeName());
 assertNull(documentType.attr(DocumentType.NAME));
 assertNull(documentType.attr(DocumentType.PUBLIC_ID));
 assertNull(documentType.attr(DocumentType.SYSTEM_ID));
 assertNull(documentType.baseUri());
 }

 // Test constructor with empty string arguments
 @Test
 public void testConstructorWithEmptyStringArguments() {
 DocumentType documentType = new DocumentType("", "", "", Urls.toAbsolute(Urls.protocolsFromString("http://example.com")));

 assertEquals("#doctype", documentType.nodeName());
 assertEquals("", documentType.attr(DocumentType.NAME));
 assertNull(documentType.attr(DocumentType.PUBLIC_ID));
 assertNull(documentType.attr(DocumentType.SYSTEM_ID));
 assertEquals("http://example.com", documentType.baseUri());
 }

 // Test constructor with null baseUri
 @Test(expected = NullPointerException.class)
 public void testConstructorWithNullBaseUri() {
 DocumentType documentType = new DocumentType("html", "publicID", "systemID", null);

 assertNotNull(documentType);
 assertEquals("#doctype", documentType.nodeName());
 assertNull(documentType.attr(DocumentType.PUBLIC_ID));
 assertNull(documentType.attr(DocumentType.SYSTEM_ID));
 assertNull(documentType.baseUri());
 }

 @Test
 public void testToString() {
 DocumentType documentType = new DocumentType("html", "publicID", "systemID", Urls.toAbsolute(Urls.protocolsFromString("http://example.com")));

 assertEquals("<html>", documentType.toString());
 }

 // Test toString with no attributes
 @Test
 public void testToStringWithNoAttributes() {
 DocumentType documentType = new DocumentType("html", null, null, null);

 assertEquals("<!DOCTYPE html>", documentType.toString());
 }

 // Test toString with public id and system id
 @Test
 public void testToStringWithPublicIdAndSystemId() {
 DocumentType documentType = new DocumentType("html", "publicID", "systemID", null);

 assertEquals("<!DOCTYPE html PUBLIC \"publicID\" \"systemID\" >", documentType.toString());
 }

 // Test toString with only name
 @Test
 public void testToStringWithOnlyName() {
 DocumentType documentType = new DocumentType("html", null, null, null);

 assertEquals("<!DOCTYPE html>", documentType.toString());
 }

 // Test toString with only public id
 @Test
 public void testToStringWithOnlyPublicId() {
 DocumentType documentType = new DocumentType("html", "publicID", null, null);

 assertEquals("<!DOCTYPE html PUBLIC \"publicID\" >", documentType.toString());
 }

 // Test toString with only system id
 @Test
 public void testToStringWithOnlySystemId() {
 DocumentType documentType = new DocumentType("html", null, "systemID", null);

 assertEquals("<!DOCTYPE html SYSTEM \"systemID\">", documentType.toString());
 }
}