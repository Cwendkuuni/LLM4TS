package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;

/**
 * Test class for XmlDeclaration.
 */
public class XmlDeclarationTest {

 private static final String XML_DECLARATION = "xml version=\"1.0\" encoding=\"UTF-8\"";

 @Test
 public void testXmlDeclarationWithNameAndAttributes() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", true);
 Attributes attributes = new Attributes();
 attributes.put("version", "1.0");
 attributes.put("encoding", "UTF-8");
 xmlDeclaration.attributes = attributes;

 String expectedDeclaration = XML_DECLARATION;
 String actualDeclaration = xmlDeclaration.getWholeDeclaration();
 assertEquals(expectedDeclaration, actualDeclaration);
 }

 @Test
 public void testXmlDeclarationWithoutAttributes() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", true);
 String expectedDeclaration = "xml";
 String actualDeclaration = xmlDeclaration.getWholeDeclaration();
 assertEquals(expectedDeclaration, actualDeclaration);
 }

 @Test(expected = NullPointerException.class)
 public void testNullName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(null, "baseUri", true);
 }

 @Test(expected = NullPointerException.class)
 public void testNullBaseUri() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, true);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEmptyName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("", "baseUri", true);
 }

 @Test
 public void testXmlDeclarationToString() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", true);
 String expectedDeclaration = "<!xml version=\"1.0\" encoding=\"\">";
 String actualDeclaration = xmlDeclaration.toString();
 assertEquals(expectedDeclaration, actualDeclaration);
 }

 @Test
 public void testXmlDeclarationOuterHtmlHead() throws IOException {
 Document document = new Document();
 Appendable accum = document.createAppender("UTF-8");
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", true);
 xmlDeclaration.outerHtmlHead(accum, 0, null);
 String expectedDeclaration = "<!xml version=\"1.0\" encoding=\"UTF-8\">";
 String actualDeclaration = accum.toString();
 assertEquals(expectedDeclaration, actualDeclaration);
 }

 @Test
 public void testXmlDeclarationOuterHtmlTail() throws IOException {
 Document document = new Document();
 Appendable accum = document.createAppender("UTF-8");
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", "baseUri", true);
 xmlDeclaration.outerHtmlTail(accum, 0, null);
 String expectedDeclaration = "";
 String actualDeclaration = accum.toString();
 assertEquals(expectedDeclaration, actualDeclaration);
 }

}