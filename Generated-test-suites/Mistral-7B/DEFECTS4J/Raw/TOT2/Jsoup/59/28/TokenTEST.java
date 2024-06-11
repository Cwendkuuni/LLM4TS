import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TokenTest {

 // Test for Doctype method
 @Test
 public void testDoctype() {
 Doctype doctype = new Doctype();
 doctype.name("html");
 doctype.pubSysKey("PUBLIC");
 doctype.publicIdentifier("-//W3C//DTD HTML 4.01//EN");
 doctype.systemIdentifier("/path/to/html.dtd");
 doctype.forceQuirks(true);

 assertEquals("Doctype", doctype.tokenType());
 assertEquals("html", doctype.getName());
 assertEquals("PUBLIC", doctype.getPubSysKey());
 assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.getPublicIdentifier());
 assertEquals("/path/to/html.dtd", doctype.getSystemIdentifier());
 assertTrue(doctype.isForceQuirks());

 // Test reset method
 Doctype resetDoctype = doctype.reset();
 assertSame(doctype, resetDoctype);
 assertNull(resetDoctype.getName());
 assertNull(resetDoctype.getPubSysKey());
 assertNull(resetDoctype.getPublicIdentifier());
 assertNull(resetDoctype.getSystemIdentifier());
 assertFalse(resetDoctype.isForceQuirks());
 }

 // Test for StartTag method
 @Test
 public void testStartTag() {
 StartTag startTag = new StartTag().name("div");

 assertEquals("StartTag", startTag.tokenType().toString());
 assertEquals("div", startTag.name());
 assertNull(startTag.getAttributes());

 StartTag attributesStartTag = startTag.name("div").attributes(new Attributes().put("class", "testClass"));
 assertEquals("div", attributesStartTag.name());
 assertEquals("testClass", attributesStartTag.getAttributes().getClass().getSimpleName());
 }

 // Test for EndTag method
 @Test
 public void testEndTag() {
 EndTag endTag = new EndTag().name("div");

 assertEquals("EndTag", endTag.tokenType().toString());
 assertEquals("div", endTag.name());
 }

 // Test for Comment method
 @Test
 public void testComment() {
 Comment comment = new Comment().data("This is a comment.");

 assertEquals("Comment", comment.tokenType().toString());
 assertEquals("This is a comment.", comment.getData());
 }

 // Test for Character method
 @Test
 public void testCharacter() {
 Character character = new Character().data("A");

 assertEquals("Character", character.tokenType().toString());
 assertEquals("A", character.getData());
 }

 // Test for EOF method
 @Test
 public void testEOF() {
 EOF eof = new EOF();

 assertEquals("EOF", eof.tokenType().toString());
 }
}