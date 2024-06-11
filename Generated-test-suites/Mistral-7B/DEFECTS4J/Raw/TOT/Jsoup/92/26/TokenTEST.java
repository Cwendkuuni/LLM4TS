import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctype = new Doctype();
 doctype.name("html");
 doctype.pubSysKey("DTD XHTML 1.0");
 doctype.systemIdentifier("/xhtml1-strict.dtd");
 doctype.forceQuirks(true);

 assertEquals("Doctype", doctype.tokenType());
 assertEquals("html", doctype.getName());
 assertEquals("DTD XHTML 1.0", doctype.getPubSysKey());
 assertEquals("/xhtml1-strict.dtd", doctype.getSystemIdentifier());
 assertTrue(doctype.isForceQuirks());

 Doctype resetDoctype = doctype.reset();
 assertNotSame(doctype, resetDoctype);
 assertEquals("Doctype", resetDoctype.tokenType());
 assertNull(resetDoctype.getName());
 assertNull(resetDoctype.getPubSysKey());
 assertNull(resetDoctype.getSystemIdentifier());
 assertFalse(resetDoctype.isForceQuirks());
 }

 @Test
 public void testStartTagToken() {
 StartTag startTag = new StartTag();
 startTag.name("div");
 startTag.attributes().put("id", "container");
 startTag.attributes().put("class", "someClass");

 assertEquals("StartTag", startTag.tokenType().toString());
 assertEquals("div", startTag.name());
 assertNotNull(startTag.getAttributes());
 assertTrue(startTag.getAttributes().containsKey("id"));
 assertEquals("container", startTag.getAttributes().get("id"));
 assertTrue(startTag.getAttributes().containsKey("class"));
 assertEquals("someClass", startTag.getAttributes().get("class"));

 StartTag resetStartTag = startTag.reset();
 assertNotSame(startTag, resetStartTag);
 assertEquals("StartTag", resetStartTag.tokenType().toString());
 assertNull(resetStartTag.name());
 assertNull(resetStartTag.getAttributes());
 }

 @Test
 public void testEndTagToken() {
 EndTag endTag = new EndTag();
 endTag.name("div");

 assertEquals("EndTag", endTag.tokenType().toString());
 assertEquals("div", endTag.name());

 EndTag resetEndTag = endTag.reset();
 assertNotSame(endTag, resetEndTag);
 assertEquals("EndTag", resetEndTag.tokenType().toString());
 assertNull(resetEndTag.name());
 }

 @Test
 public void testCommentToken() {
 Comment comment = new Comment();
 comment.data("This is a comment.");

 assertEquals("Comment", comment.tokenType().toString());
 assertEquals("This is a comment.", comment.getData());

 Comment resetComment = comment.reset();
 assertNotSame(comment, resetComment);
 assertEquals("Comment", resetComment.tokenType().toString());
 assertNull(resetComment.getData());
 }

 @Test(expected = AssertionError.class)
 public void testUnknownTokenType() {
 Token unknownToken = new Object();
 assertEquals("Unknown", unknownToken.tokenType().toString());
 }
}