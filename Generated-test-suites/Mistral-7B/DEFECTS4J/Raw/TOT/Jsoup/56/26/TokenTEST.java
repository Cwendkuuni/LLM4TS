import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctype = new Doctype();
 doctype.name.append("html");
 doctype.publicIdentifier.append("public");
 doctype.systemIdentifier.append("/index.html");
 doctype.forceQuirks = true;

 Token expectedDoctype = new Doctype()
 .name("html")
 .publicIdentifier("public")
 .systemIdentifier("/index.html")
 .forceQuirks(true);

 assertEquals(expectedDoctype, doctype);

 assertEquals("Doctype", doctype.tokenType());
 assertEquals(TokenType.Doctype, doctype.type);
 assertEquals("html", doctype.getName());
 assertEquals("public", doctype.getPublicIdentifier());
 assertEquals("/index.html", doctype.getSystemIdentifier());
 assertTrue(doctype.isForceQuirks());

 doctype.reset();
 assertNull(doctype.getName());
 assertNull(doctype.getPublicIdentifier());
 assertNull(doctype.getSystemIdentifier());
 assertFalse(doctype.isForceQuirks());
 }

 @Test
 public void testTagToken() {
 Tag tag = new StartTag();
 tag.name("p");
 tag.attributes = new Attributes();
 tag.attributes.put("id", "intro");
 tag.attributes.put("class", "intro");
 tag.selfClosing = false;

 Token expectedTag = new StartTag()
 .name("p")
 .attributes(new Attributes()
 .put("id", "intro")
 .put("class", "intro"));

 assertEquals(expectedTag, tag);

 assertEquals("StartTag", tag.tokenType());
 assertEquals(TokenType.StartTag, tag.type);
 assertEquals("p", tag.name());
 assertNotNull(tag.getAttributes());
 assertEquals(2, tag.getAttributes().size());
 assertEquals("id", tag.getAttributes().keyAt(0));
 assertEquals("intro", tag.getAttributes().valueAt(0));
 assertEquals("class", tag.getAttributes().keyAt(1));
 assertEquals("intro", tag.getAttributes().valueAt(1));
 assertFalse(tag.isSelfClosing());

 tag.finaliseTag();
 assertEquals("</p>", tag.toString());
 }

 @Test
 public void testCommentToken() {
 Comment comment = new Comment();
 comment.data.append("This is a comment.");

 Token expectedComment = new Comment()
 .data("This is a comment.");

 assertEquals(expectedComment, comment);

 assertEquals("Comment", comment.tokenType());
 assertEquals(TokenType.Comment, comment.type);
 assertEquals("This is a comment.", comment.getData());

 comment.reset();
 assertNull(comment.getData());
 }

 @Test(expected = AssertionError.class)
 public void testInvalidToken() {
 Token token = new Token();
 token.type = null;
 }

 @Test
 public void testEOFToken() {
 EOF eof = new EOF();

 assertEquals("EOF", eof.tokenType());
 assertEquals(TokenType.EOF, eof.type);
 }

}