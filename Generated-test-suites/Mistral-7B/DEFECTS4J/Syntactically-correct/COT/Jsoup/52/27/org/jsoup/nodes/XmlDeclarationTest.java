package org.jsoup.nodes.test;

import org.jsoup.nodes.XmlDeclaration;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class XmlDeclarationTest {

 private XmlDeclaration xmlDeclaration;

 @Test
 public void testXmlDeclarationConstructor() {
 XMLDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 Assert.assertEquals("#declaration", xmlDeclaration.nodeName());
 Assert.assertEquals("xml", xmlDeclaration.name());
 Assert.assertFalse(xmlDeclaration.isProcessingInstruction());
 }

 @Test
 public void testXmlDeclarationConstructorWithProcessingInstruction() {
 XMLDeclaration xmlDeclaration = new XmlDeclaration("<?xml", null, true);
 Assert.assertEquals("#declaration", xmlDeclaration.nodeName());
 Assert.assertNull(xmlDeclaration.name());
 Assert.assertTrue(xmlDeclaration.isProcessingInstruction());
 }

 @Test
 public void testGetWholeDeclaration() {
 XMLDeclaration xmlDeclaration = new XMLDeclaration("xml", null, false);
 xmlDeclaration.attributes.put("version", "1.0");
 xmlDeclaration.attributes.put("encoding", "UTF-8");

 String expected = "<declaration version=\"1.0\" encoding=\"UTF-8\">";
 String actual = xmlDeclaration.getWholeDeclaration();
 assertEquals(expected, actual);
 }

 @Test(expected = NullPointerException.class)
 public void testGetWholeDeclarationWithNullName() {
 XMLDeclaration xmlDeclaration = new XMLDeclaration(null, null, false);
 xmlDeclaration.getWholeDeclaration();
 }

 @Test(expected = NullPointerException.class)
 public void testGetWholeDeclarationWithEmptyName() {
 XMLDeclaration xmlDeclaration = new XMLDeclaration("", null, false);
 xmlDeclaration.getWholeDeclaration();
 }

 @Test(expected = NullPointerException.class)
 public void testGetWholeDeclarationWithNoAttributes() {
 XMLDeclaration xmlDeclaration = new XMLDeclaration("xml", null, false);
 xmlDeclaration.getWholeDeclaration();
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetWholeDeclarationWithMultipleRootDeclarations() {
 XMLDeclaration xmlDeclaration = new XMLDeclaration("xml", null, false);
 xmlDeclaration.addAttribute("version", "1.0");
 xmlDeclaration.addAttribute("version", "2.0");
 }

 @Test
 public void testToString() {
 XMLDeclaration xmlDeclaration = new XMLDeclaration("xml", null, false);
 xmlDeclaration.attributes.put("version", "1.0");
 xmlDeclaration.attributes.put("encoding", "UTF-8");

 String expected = "<declaration version=\"1.0\" encoding=\"UTF-8\">";
 String actual = xmlDeclaration.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testOuterHtmlHead() throws IOException {
 Appendable accumulator = new StringBuilder();
 XMLDeclaration xmlDeclaration = new XMLDeclaration("xml", null, false);
 xmlDeclaration.attributes.put("version", "1.0");
 xmlDeclaration.attributes.put("encoding", "UTF-8");

 Document.OutputSettings outputSettings = new Document.OutputSettings();
 xmlDeclaration.outerHtmlHead(accumulator, 0, outputSettings);

 String expected = "<declaration version=\"1.0\" encoding=\"UTF-8\">";
 String actual = accumulator.toString();
 assertEquals(expected, actual);
 }

 @Test
 public void testOuterHtmlTail() throws IOException {
 Appendable accumulator = new StringBuilder();
 XMLDeclaration xmlDeclaration = new XMLDeclaration("xml", null, false);

 Document.OutputSettings outputSettings = new Document.OutputSettings();
 xmlDeclaration.outerHtmlTail(accumulator, 0, outputSettings);

 String actual = accumulator.toString();
 assertEquals("", actual);
 }

}