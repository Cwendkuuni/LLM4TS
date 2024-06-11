import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name("html");
 doctypeToken.pubSysKey("DTD HTML 5");
 doctypeToken.publicIdentifier("public \"-//W3C//DTD HTML 5//EN\"");
 doctypeToken.systemIdentifier("system \"http://www.w3.org/TR/html5/DTD/html5.dtd\"");
 doctypeToken.forceQuirks(true);

 assertEquals("Doctype", doctypeToken.tokenType());
 assertEquals("html", doctypeToken.getName());
 assertEquals("DTD HTML 5", doctypeToken.getPubSysKey());
 assertEquals("public \"-//W3C//DTD HTML 5//EN\"", doctypeToken.getPublicIdentifier());
 assertEquals("system \"http://www.w3.org/TR/html5/DTD/html5.dtd\"", doctypeToken.getSystemIdentifier());
 assertTrue(doctypeToken.isForceQuirks());

 Token resetDoctypeToken = doctypeToken.reset();
 assertNotSame(doctypeToken, resetDoctypeToken);
 assertNull(resetDoctypeToken.getName());
 assertNull(resetDoctypeToken.getPubSysKey());
 assertNull(resetDoctypeToken.getPublicIdentifier());
 assertNull(resetDoctypeToken.getSystemIdentifier());
 assertFalse(resetDoctypeToken.isForceQuirks());
 }

 @Test
 public void testStartTagToken() {
 StartTag startTagToken = new StartTag();
 startTagToken.name("div");
 startTagToken.attributes(new Attributes().put("class", "test"));

 assertEquals("StartTag", startTagToken.tokenType());
 assertEquals("div", startTagToken.name());
 assertEquals("test", startTagToken.getAttributes().getClass().getSimpleName());

 Token resetStartTagToken = startTagToken.reset();
 assertNotSame(startTagToken, resetStartTagToken);
 assertNull(resetStartTagToken.getName());
 assertNull(resetStartTagToken.getAttributes());
 }

 @Test
 public void testEndTagToken() {
 EndTag endTagToken = new EndTag();
 endTagToken.name("div");

 assertEquals("EndTag", endTagToken.tokenType());
 assertEquals("div", endTagToken.name());

 Token resetEndTagToken = endTagToken.reset();
 assertNotSame(endTagToken, resetEndTagToken);
 assertNull(resetEndTagToken.getName());
 }

 @Test
 public void testCommentToken() {
 Comment commentToken = new Comment();
 commentToken.data("This is a comment");

 assertEquals("Comment", commentToken.tokenType());
 assertEquals("This is a comment", commentToken.getData());

 Token resetCommentToken = commentToken.reset();
 assertNotSame(commentToken, resetCommentToken);
 assertNull(resetCommentToken.getData());
 }

 @Test
 public void testCharacterToken() {
 Character characterToken = new Character();
 characterToken.data("ą");

 assertEquals("Character", characterToken.tokenType());
 assertEquals("ą", characterToken.getData());

 Token resetCharacterToken = characterToken.reset();
 assertNotSame(characterToken, resetCharacterToken);
 assertNull(resetCharacterToken.getData());
 }

 @Test
 public void testEOFToken() {
 EOF eofToken = new EOF();

 assertEquals("EOF", eofToken.tokenType().toString());

 Token resetEOFToken = eofToken.reset();
 assertSame(eofToken, resetEOFToken);
 }
}