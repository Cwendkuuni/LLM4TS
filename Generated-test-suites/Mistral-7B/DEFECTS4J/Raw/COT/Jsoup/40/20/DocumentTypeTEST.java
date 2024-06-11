package org.jsoup.nodes.test;

import static org.junit.Assert.*;

import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.helper.StringUtil;
import org.junit.Test;

public class DocumentTypeTest {

 // List of all public methods in DocumentType class with their signatures

 /**
 * Create a new doctype element.
 * @param name the doctype's name
 * @param publicId the doctype's public ID
 * @param systemId the doctype's system ID
 * @param baseUri the doctype's base URI
 */
 @Test
 public void testDocumentTypeConstructor() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
 assertEquals("#doctype", documentType.nodeName());
 assertEquals("html", documentType.attr("name"));
 assertEquals("publicId", documentType.attr("publicId"));
 assertEquals("systemId", documentType.attr("systemId"));
 assertEquals("baseUri", documentType.baseUri());
 }

 /**
 * Test the nodeName method
 */
 @Test
 public void testNodeName() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
 assertEquals("#doctype", documentType.nodeName());
 }

 /**
 * Test the outerHtmlHead method
 */
 @Test
 public void testOuterHtmlHead() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 String expected = "<!DOCTYPE html PUBLIC \"publicId\" \"systemId\">";
 assertEquals(expected, accum.toString());
 }

 /**
 * Test the outerHtmlHead method when publicId and systemId are empty
 */
 @Test
 public void testOuterHtmlHeadWithEmptyPublicIdAndSystemId() {
 DocumentType documentType = new DocumentType("html", "", "", "baseUri");
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 String expected = "<!DOCTYPE html>";
 assertEquals(expected, accum.toString());
 }

 /**
 * Test the outerHtmlHead method when name is empty
 */
 @Test(expected = IllegalArgumentException.class)
 public void testOuterHtmlHeadWithEmptyName() {
 DocumentType documentType = new DocumentType("", "publicId", "systemId", "baseUri");
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlHead(accum, 0, null);
 fail("Expected an IllegalArgumentException");
 }

 /**
 * Test the outerHtmlTail method
 */
 @Test
 public void testOuterHtmlTail() {
 DocumentType documentType = new DocumentType("html", "publicId", "systemId", "baseUri");
 StringBuilder accum = new StringBuilder();
 documentType.outerHtmlTail(accum, 0, null);
 assertEquals("", accum.toString());
 }

 /**
 * Test the equals method
 */
 @Test
 public void testEquals() {
 DocumentType documentType1 = new DocumentType("html", "publicId1", "systemId1", "baseUri1");
 DocumentType documentType2 = new DocumentType("html", "publicId2", "systemId2", "baseUri2");
 assertNotEquals(documentType1, documentType2);
 DocumentType documentType3 = new DocumentType("html", "publicId1", "systemId1", "baseUri1");
 assertEquals(documentType1, documentType3);
 }

 /**
 * Test the hashCode method
 */
 @Test
 public void testHashCode() {
 DocumentType documentType1 = new DocumentType("html", "publicId1", "systemId1", "baseUri1");
 DocumentType documentType2 = new DocumentType("html", "publicId2", "systemId2", "baseUri2");
 DocumentType documentType3 = new DocumentType("html", "publicId1", "systemId1", "baseUri1");
 assertEquals(documentType1.hashCode(), documentType3.hashCode());
 assertNotEquals(documentType1.hashCode(), documentType2.hashCode());
 }

}