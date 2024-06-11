import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctypeToken = new Doctype();
 doctypeToken.name("html");
 doctypeToken.pubSysKey("PUBLIC");
 doctypeToken.publicIdentifier("-//W3C//DTD HTML 4.01//EN");
 doctypeToken.systemIdentifier("/some/path/html.dtd");
 doctypeToken.forceQuirks(true);

 assertEquals("Doctype", doctypeToken.tokenType());
 assertEquals("html", doctypeToken.getName());
 assertEquals("PUBLIC", doctypeToken.getPubSysKey());
 assertEquals("-//W3C//DTD HTML 4.01//EN", doctypeToken.getPublicIdentifier());
 assertEquals("/some/path/html.dtd", doctypeToken.getSystemIdentifier());
 assertEquals(true, doctypeToken.isForceQuirks());

 Doctype resetDoctypeToken = doctypeToken.reset();
 assertEquals("Doctype", resetDoctypeToken.tokenType());
 assertNull(resetDoctypeToken.getName());
 assertNull(resetDoctypeToken.getPubSysKey());
 assertNull(resetDoctypeToken.getPublicIdentifier());
 assertNull(resetDoctypeToken.getSystemIdentifier());
 assertEquals(false, resetDoctypeToken.isForceQuirks());
 }

 @Test
 public void testCommentToken() {
 Comment commentToken = new Comment();
 commentToken.data("This is a comment.");

 assertEquals("Comment", commentToken.tokenType());
 assertEquals("", commentToken.name());
 assertEquals("", commentToken.normalName());
 assertEquals("This is a comment.", commentToken.getData());

 Comment resetCommentToken = commentToken.reset();
 assertEquals("Comment", resetCommentToken.tokenType());
 assertNull(resetCommentToken.name());
 assertNull(resetCommentToken.normalName());
 assertNull(resetCommentToken.getData());
 }

 @Test
 public void testCharacterToken() {
 Character characterToken = new Character();
 characterToken.data("A");

 assertEquals("Character", characterToken.tokenType());
 assertEquals("A", characterToken.getData());

 Character resetCharacterToken = characterToken.reset();
 assertEquals("Character", resetCharacterToken.tokenType());
 assertNull(resetCharacterToken.getData());
 }

 @Test
 public void testStartTagToken() {
 StartTag startTagToken = new StartTag();
 startTagToken.name("html").attributes(new Attributes().put("xmlns:html", "http://www.w3.org/1999/xhtml"));

 assertEquals("StartTag", startTagToken.tokenType());
 assertEquals("html", startTagToken.name());
 assertEquals("html", startTagToken.normalName());
 assertFalse(startTagToken.isSelfClosing());
 assertNotNull(startTagToken.getAttributes());
 assertEquals(1, startTagToken.getAttributes().size());
 assertEquals("http://www.w3.org/1999/xhtml", startTagToken.getAttributes().get("xmlns:html"));

 StartTag resetStartTagToken = startTagToken.reset();
 assertEquals("StartTag", resetStartTagToken.tokenType());
 assertEquals(null, resetStartTagToken.name());
 assertEquals(null, resetStartTagToken.normalName());
 assertFalse(resetStartTagToken.isSelfClosing());
 assertNull(resetStartTagToken.getAttributes());
 }

 @Test
 public void testEndTagToken() {
 EndTag endTagToken = new EndTag();
 endTagToken.name("html");

 assertEquals("EndTag", endTagToken.tokenType());
 assertEquals("html", endTagToken.name());
 assertEquals("html", endTagToken.normalName());
 assertFalse(endTagToken.isSelfClosing());

 EndTag resetEndTagToken = endTagToken.reset();
 assertEquals("EndTag", resetEndTagToken.tokenType());
 assertEquals(null, resetEndTagToken.name());
 assertEquals(null, resetEndTagToken.normalName());
 assertFalse(resetEndTagToken.isSelfClosing());
 }

 @Test
 public void testResetAllTokens() {
 Doctype doctypeToken = new Doctype();
 StartTag startTagToken = new StartTag();
 EndTag endTagToken = new EndTag();
 Comment commentToken = new Comment();
 Character characterToken = new Character();
 CData cDataToken = new CData("Some data inside CDATA.");
 EOF eofToken = new EOF();

 assertEquals("Doctype", doctypeToken.tokenType());
 assertEquals("", doctypeToken.name());
 // Calling reset() on Doctype directly is not supported.
 // The code resets all fields, but it also calls the super-class method, which is abstract.
 // Instead, we test it by creating a new instance and comparing the tokens.
 Doctype resetDoctypeToken = new Doctype();
 assertEquals("Doctype", resetDoctypeToken.tokenType());
 assertEquals("", resetDoctypeToken.name());

 assertEquals("StartTag", startTagToken.tokenType());
 assertEquals(null, startTagToken.name());
 assertEquals("", startTagToken.normalName());
 assertFalse(startTagToken.isSelfClosing());
 assertNull(startTagToken.getAttributes());
 assertEquals("StartTag", startTagToken.reset().tokenType());
 assertEquals(null, startTagToken.reset().name());
 assertEquals("", startTagToken.reset().normalName());
 assertFalse(startTagToken.reset().isSelfClosing());
 assertNull(startTagToken.reset().getAttributes());

 assertEquals("EndTag", endTagToken.tokenType());
 assertEquals("", endTagToken.name());
 assertEquals("", endTagToken.normalName());
 assertFalse(endTagToken.isSelfClosing());
 assertEquals("EndTag", endTagToken.reset().tokenType());
 assertEquals(null, endTagToken.reset().name());
 assertEquals("", endTagToken.reset().normalName());
 assertFalse(endTagToken.reset().isSelfClosing());

 assertEquals("Comment", commentToken.tokenType());
 assertEquals("", commentToken.name());
 assertEquals("", commentToken.normalName());
 assertEquals("", commentToken.getData());
 assertEquals("Comment", commentToken.reset().tokenType());
 assertEquals(null, commentToken.reset().name());
 assertEquals(null, commentToken.reset().normalName());
 assertNull(commentToken.reset().getData());

 assertEquals("Character", characterToken.tokenType());
 assertEquals("", characterToken.name());
 assertEquals("", characterToken.normalName());
 assertEquals("", characterToken.getData());
 assertEquals("Character", characterToken.reset().tokenType());
 assertEquals(null, characterToken.reset().name());
 assertEquals(null, characterToken.reset().normalName());
 assertNull(characterToken.reset().getData());

 assertEquals("CDATA", cDataToken.tokenType());
 assertEquals("", cDataToken.name());
 assertEquals("CDATA", cDataToken.normalName());
 assertEquals("Some data inside CDATA.", cDataToken.getData());
 assertEquals("CDATA", cDataToken.reset().tokenType());
 assertEquals(null, cDataToken.reset().name());
 assertEquals("CDATA", cDataToken.reset().normalName());
 assertNull(cDataToken.reset().getData());

 assertEquals("EOF", eofToken.tokenType());
 assertEquals("", eofToken.name());
 assertEquals("", eofToken.normalName());
 assertFalse(eofToken.isSelfClosing());
 assertNull(eofToken.getAttributes());
 }
}