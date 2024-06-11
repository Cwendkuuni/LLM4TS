package org.jsoup.nodes;
import org.jsoup.nodes.XmlDeclaration;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class XmlDeclarationTest {

 @Test
 public void testXmlDeclarationInstance() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 XmlDeclaration processingInstruction = new XmlDeclaration("processing-instruction", null, true);

 assertEquals("#declaration", xmlDeclaration.nodeName());
 assertEquals("xml", xmlDeclaration.name());
 assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"?", xmlDeclaration.getWholeDeclaration());
 assertEquals("processing-instruction", processingInstruction.name());
 assertNull(processingInstruction.getWholeDeclaration()); // processing instruction does not have a version or encoding
 }

 @Test
 public void testXmlDeclarationWithAttributes() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration("xml", null, false);
 xmlDeclaration.attributes().put("version", "1.0");
 xmlDeclaration.attributes().put("encoding", "UTF-8");

 assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"?", xmlDeclaration.getWholeDeclaration());
 }

 @Test(expected = NullPointerException.class)
 public void testXmlDeclarationWithNullName() {
 XmlDeclaration xmlDeclaration = new XmlDeclaration(null, null, false);
 }
}