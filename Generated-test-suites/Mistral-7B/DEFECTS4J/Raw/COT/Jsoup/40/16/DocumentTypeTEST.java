package org.jsoup.nodes.test;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.jsoup.nodes.DocumentType;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class DocumentTypeTest {

 private DocumentType documentType;

 @Test
 public void testDocumentTypeName() {
 documentType = new DocumentType("html", "", "", "");
 assertEquals("#doctype", documentType.nodeName());
 }

 @Test
 public void testDocumentTypeWithPublicId() {
 documentType = new DocumentType("html", "publicId", "", "");
 assertEquals("#doctype name=\"html\" publicid=\"publicId\"", documentType.outerHtml());
 }

 @Test
 public void testDocumentTypeWithPublicIdAndSystemId() {
 documentType = new DocumentType("html", "publicId", "systemId", "");
 assertEquals("#doctype name=\"html\" publicid=\"publicId\" systemid=\"systemId\"", documentType.outerHtml());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyName() {
 Validate.testIllegalArg(() -> new DocumentType("", "", "", ""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullName() {
 Validate.testIllegalArg(() -> new DocumentType(null, "", "", ""));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyPublicId() {
 documentType = new DocumentType("html", "", "", "");
 documentType.attr("publicId", "");
 assertEquals("#doctype name=\"html\"", documentType.outerHtml());
 Validate.testIllegalArg(() -> documentType.attr("publicId", ""));
 }

 @Test
 public void testDocumentTypeWithLongPublicId() {
 String longPublicId = StringUtil.repeat("a", 1000);
 documentType = new DocumentType("html", longPublicId, "", "");
 assertEquals("#doctype name=\"html\" publicid=\"" + longPublicId + "\"", documentType.outerHtml());
 }

 @Test
 public void testDocumentTypeWithLongSystemId() {
 String longSystemId = StringUtil.repeat("a", 1000);
 documentType = new DocumentType("html", "", longSystemId, "");
 assertEquals("#doctype name=\"html\" systemid=\"" + longSystemId + "\"", documentType.outerHtml());
 }

 @Test
 public void testDocumentTypeWithLongBaseUri() {
 String longBaseUri = StringUtil.repeat("a", 1000);
 documentType = new DocumentType("html", "", "", longBaseUri);
 assertEquals("#doctype name=\"html\"", documentType.outerHtml());
 }
}