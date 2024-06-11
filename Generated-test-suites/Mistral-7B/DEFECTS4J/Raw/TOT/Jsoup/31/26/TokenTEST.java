import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenTest {

 @Test
 public void testDoctypeConstruction() {
 Doctype doctype = new Doctype();
 assertEquals("Doctype", doctype.tokenType());
 assertNull(doctype.getName());
 assertNull(doctype.getPublicIdentifier());
 assertNull(doctype.getSystemIdentifier());
 assertFalse(doctype.isForceQuirks());
 }

 @Test
 public void testDoctypeNameConstruction() {
 Doctype doctype = new Doctype().name("html");
 assertEquals("Doctype", doctype.tokenType());
 assertEquals("html", doctype.getName());
 assertNull(doctype.getPublicIdentifier());
 assertNull(doctype.getSystemIdentifier());
 assertFalse(doctype.isForceQuirks());
 }

 @Test
 public void testDoctypeNamePublicIdentifierConstruction() {
 Doctype doctype = new Doctype().name("html").publicIdentifier("_xml");
 assertEquals("Doctype", doctype.tokenType());
 assertEquals("html", doctype.getName());
 assertEquals("_xml", doctype.getPublicIdentifier());
 assertNull(doctype.getSystemIdentifier());
 assertFalse(doctype.isForceQuirks());
 }

 @Test
 public void testDoctypeSystemIdentifierConstruction() {
 Doctype doctype = new Doctype().name("html").systemIdentifier("/path/to/file.html");
 assertEquals("Doctype", doctype.tokenType());
 assertEquals("html", doctype.getName());
 assertNull(doctype.getPublicIdentifier());
 assertEquals("/path/to/file.html", doctype.getSystemIdentifier());
 assertFalse(doctype.isForceQuirks());
 }

 @Test
 public void testDoctypeForceQuirksConstruction() {
 Doctype doctype = new Doctype().name("html").forceQuirks(true);
 assertEquals("Doctype", doctype.tokenType());
 assertEquals("html", doctype.getName());
 assertNull(doctype.getPublicIdentifier());
 assertNull(doctype.getSystemIdentifier());
 assertTrue(doctype.isForceQuirks());
 }

 // ...Similar test cases for StartTag, EndTag, Comment, Character, and EOF classes...

}