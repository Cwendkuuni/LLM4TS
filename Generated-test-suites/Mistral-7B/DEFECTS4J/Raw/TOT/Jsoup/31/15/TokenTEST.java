import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenTest {

 @Test
 public void testDoctype() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("doctype");
 assertEquals("", doctype.getPublicIdentifier().toString());
 assertEquals("", doctype.getSystemIdentifier().toString());
 assertFalse(doctype.isForceQuirks());
 }

 @Test
 public void testDoctypeWithName() {
 Doctype doctype = new Doctype().name("html");
 doctype.getName().equals("doctype");
 doctype.getName().equals("html");
 assertEquals("", doctype.getPublicIdentifier().toString());
 assertEquals("", doctype.getSystemIdentifier().toString());
 assertFalse(doctype.isForceQuirks());
 }

 @Test
 public void testDoctypeWithNameAndPublicId() {
 Doctype doctype = new Doctype().name("html").publicIdentifier("_public");
 doctype.getName().equals("doctype");
 doctype.getName().equals("html");
 doctype.getPublicIdentifier().equals("_public");
 assertEquals("", doctype.getSystemIdentifier().toString());
 assertFalse(doctype.isForceQuirks());
 }

 @Test
 public void testDoctypeWithNamePublicIdAndSystemId() {
 Doctype doctype = new Doctype().name("html").publicIdentifier("_public").systemIdentifier("_system");
 doctype.getName().equals("doctype");
 doctype.getName().equals("html");
 doctype.getPublicIdentifier().equals("_public");
 doctype.getSystemIdentifier().equals("_system");
 assertFalse(doctype.isForceQuirks());
 }

 @Test
 public void testDoctypeForceQuirks() {
 Doctype doctype = new Doctype().name("html").forceQuirks(true);
 doctype.getName().equals("doctype");
 doctype.getName().equals("html");
 assertEquals("", doctype.getPublicIdentifier().toString());
 assertEquals("", doctype.getSystemIdentifier().toString());
 assertTrue(doctype.isForceQuirks());
 }

 // Test other Token types here...
}