package org.jsoup.nodes.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Document.OutputSettings;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Before;
import org.junit.Test;

/**
 * Test class for {@link XmlDeclaration} class.
 */
public class XmlDeclarationTest {

 private XmlDeclaration xmlDeclaration;
 private Document document;

 @Before
 public void setUp() {
 document = mock(Document.class);
 xmlDeclaration = new XmlDeclaration("xml", "", false);
 }

 // Test for constructor
 @Test
 public void testXmlDeclarationConstructor() {
 assertNotNull(xmlDeclaration);
 assertEquals("#declaration", xmlDeclaration.nodeName());
 }

 // Test for name() method
 @Test
 public void testNameMethod() {
 assertEquals("xml", xmlDeclaration.name());
 }

 // Test for getWholeDeclaration() method
 @Test
 public void testGetWholeDeclarationMethod() {
 String expectedDeclaration = "xml version=\"1.0\" encoding=\"UTF-8\"";
 xmlDeclaration.attributes.put("version", "1.0");
 xmlDeclaration.attributes.put("encoding", "UTF-8");
 String actualDeclaration = xmlDeclaration.getWholeDeclaration();
 assertEquals(expectedDeclaration, actualDeclaration);
 }

 // Test for getWholeDeclaration() method when there is no version and encoding specified
 @Test
 public void testGetWholeDeclarationMethodNoVersionOrEncoding() {
 String actualDeclaration = xmlDeclaration.getWholeDeclaration();
 assertEquals("xml", actualDeclaration);
 }

 // Test for outerHtmlHead() method
 @Test
 public void testOuterHtmlHead() throws IOException {
 Appendable accum = mock(Appendable.class);
 OutputSettings out = mock(OutputSettings.class);
 xmlDeclaration.outerHtmlHead(accum, 0, out);
 String expectedDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 assertEquals(expectedDeclaration, accum.toString());
 }

 // Test for outerHtmlHead() method when there is no version and encoding specified
 @Test
 public void testOuterHtmlHeadNoVersionOrEncoding() throws IOException {
 Appendable accum = mock(Appendable.class);
 OutputSettings out = mock(OutputSettings.class);
 xmlDeclaration.outerHtmlHead(accum, 0, out);
 String expectedDeclaration = "<?xml?>";
 assertEquals(expectedDeclaration, accum.toString());
 }

 // Test for outerHtmlTail() method
 @Test
 public void testOuterHtmlTail() throws IOException {
 Appendable accum = mock(Appendable.class);
 OutputSettings out = mock(OutputSettings.class);
 xmlDeclaration.outerHtmlTail(accum, 0, out);
 assertNull(accum.toString());
 }

 // Test for toString() method
 @Test
 public void testToStringMethod() throws IOException {
 String expectedDeclaration = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
 xmlDeclaration.attributes.put("version", "1.0");
 xmlDeclaration.attributes.put("encoding", "UTF-8");
 String actualDeclaration = xmlDeclaration.toString();
 assertEquals(expectedDeclaration, actualDeclaration);
 }

 // Test for toString() method when there is no version and encoding specified
 @Test
 public void testToStringMethodNoVersionOrEncoding() {
 String actualDeclaration = xmlDeclaration.toString();
 assertEquals("<?xml?>", actualDeclaration);
 }

 // Test for toString() method when isProcessingInstruction is true
 @Test
 public void testToStringMethodIsProcessingInstructionTrue() {
 xmlDeclaration = new XmlDeclaration("xml-pi", "", true);
 String actualDeclaration = xmlDeclaration.toString();
 assertEquals("<?xml-pi?>", actualDeclaration);
 }

 // Test for toString() method when name is null
 @Test(expected = NullPointerException.class)
 public void testToStringMethodNameNull() {
 xmlDeclaration = new XmlDeclaration(null, "", false);
 xmlDeclaration.toString();
 }

 // Test for toString() method when name is empty
 @Test
 public void testToStringMethodNameEmpty() {
 xmlDeclaration = new XmlDeclaration("", "", false);
 String actualDeclaration = xmlDeclaration.toString();
 assertEquals("", actualDeclaration);
 }

 // Test for toString() method when name is whitespace
 @Test
 public void testToStringMethodNameWhitespace() {
 xmlDeclaration = new XmlDeclaration(" \txml \r\n ", "", false);
 String actualDeclaration = xmlDeclaration.toString();
 assertEquals("", actualDeclaration);
 }

 // Test for name() method when name is null
 @Test(expected = NullPointerException.class)
 public void testNameMethodNameNull() {
 xmlDeclaration = new XmlDeclaration(null, "", false);
 xmlDeclaration.name();
 }

 // Test for name() method when name is empty
 @Test
 public void testNameMethodNameEmpty() {
 xmlDeclaration = new XmlDeclaration("", "", false);
 String actualName = xmlDeclaration.name();
 assertEquals("#declaration", actualName);
 }

 // Test for name() method when name is whitespace
 @Test
 public void testNameMethodNameWhitespace() {
 xmlDeclaration = new XmlDeclaration(" \txml \r\n ", "", false);
 String actualName = xmlDeclaration.name();
 assertEquals("#declaration", actualName);
 }

 // Test for getWholeDeclaration() method when attributes contain invalid keys
 @Test(expected = IllegalArgumentException.class)
 public void testGetWholeDeclarationMethodInvalidAttributes() {
 xmlDeclaration.attributes.put("invalidKey", "value");
 xmlDeclaration.getWholeDeclaration();
 }
}