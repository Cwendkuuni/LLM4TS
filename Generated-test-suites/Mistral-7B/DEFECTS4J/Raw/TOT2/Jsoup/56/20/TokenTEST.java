import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Doctype;
import org.jsoup.parser.Tag;
import org.jsoup.parser.StartTag;
import org.jsoup.parser.EndTag;
import org.jsoup.parser.Comment;
import org.jsoup.parser.Character;
import org.jsoup.parser.EOF;

public class TokenTest {

 @Test
 public void testDoctype() {
 Doctype doctype = new Doctype();
 Doctype resetDoctype = doctype.reset();
 assertEquals("Doctype", resetDoctype.tokenType());
 }

 @Test
 public void testDoctype_getName() {
 Doctype doctype = new Doctype();
 doctype.getName("html");
 Doctype resetDoctype = doctype.reset();
 assertEquals("Doctype", resetDoctype.tokenType());
 assertEquals("html", resetDoctype.getName());
 }

 @Test
 public void testDoctype_getPublicIdentifier() {
 Doctype doctype = new Doctype();
 doctype.getPublicIdentifier("_publicID");
 Doctype resetDoctype = doctype.reset();
 assertEquals("Doctype", resetDoctype.tokenType());
 assertEquals("_publicID", resetDoctype.getPublicIdentifier());
 }

 @Test
 public void testDoctype_getSystemIdentifier() {
 Doctype doctype = new Doctype();
 doctype.getSystemIdentifier("/systemID");
 Doctype resetDoctype = doctype.reset();
 assertEquals("Doctype", resetDoctype.tokenType());
 assertEquals("/systemID", resetDoctype.getSystemIdentifier());
 }

 @Test
 public void testDoctype_isForceQuirks() {
 Doctype doctype = new Doctype();
 doctype.isForceQuirks(true);
 Doctype resetDoctype = doctype.reset();
 assertEquals("Doctype", resetDoctype.tokenType());
 assertTrue(resetDoctype.isForceQuirks());
 }

 @Test
 public void testTag() {
 Tag tag = new Tag();
 Tag resetTag = tag.reset();
 assertEquals("Token", resetTag.tokenType());
 }

 @Test
 public void testTag_name() {
 Tag tag = new Tag();
 tag.name("div");
 Tag resetTag = tag.reset();
 assertEquals("Token", resetTag.tokenType());
 assertEquals("div", resetTag.name());
 }

 @Test
 public void testTag_selfClosing() {
 Tag tag = new Tag();
 tag.isSelfClosing(true);
 Tag resetTag = tag.reset();
 assertEquals("Token", resetTag.tokenType());
 assertTrue(resetTag.isSelfClosing());
 }

 @Test
 public void testStartTag() {
 StartTag startTag = new StartTag();
 StartTag resetStartTag = startTag.reset();
 assertEquals("StartTag", resetStartTag.tokenType());
 }

 @Test
 public void testStartTag_name() {
 StartTag startTag = new StartTag();
 startTag.name("p");
 StartTag resetStartTag = startTag.reset();
 assertEquals("StartTag", resetStartTag.tokenType());
 assertEquals("p", resetStartTag.name());
 }

 @Test
 public void testStartTag_attributes() {
 StartTag startTag = new StartTag();
 startTag.newAttribute().name("class").value("test");
 StartTag resetStartTag = startTag.reset();
 assertEquals("StartTag", resetStartTag.tokenType());
 Assert.assertNotNull(resetStartTag.getAttributes());
 }

 @Test
 public void testEndTag() {
 EndTag endTag = new EndTag();
 EndTag resetEndTag = endTag.reset();
 assertEquals("EndTag", resetEndTag.tokenType());
 }

 @Test
 public void testEndTag_name() {
 EndTag endTag = new EndTag();
 endTag.name("p");
 EndTag resetEndTag = endTag.reset();
 assertEquals("EndTag", resetEndTag.tokenType());
 assertEquals("p", resetEndTag.name());
 }

 @Test
 public void testComment() {
 Comment comment = new Comment();
 Comment resetComment = comment.reset();
 assertEquals("Comment", resetComment.tokenType());
 }

 @Test
 public void testComment_getData() {
 Comment comment = new Comment();
 comment.getData("<!-- This is a comment -->");
 Comment resetComment = comment.reset();
 assertEquals("Comment", resetComment.tokenType());
 assertEquals("This is a comment", resetComment.getData());
 }

 @Test
 public void testCharacter() {
 Character character = new Character();
 Character resetCharacter = character.reset();
 assertEquals("Character", resetCharacter.tokenType());
 }

 @Test
 public void testCharacter_data() {
 Character character = new Character();
 character.data("&");
 Character resetCharacter = character.reset();
 assertEquals("Character", resetCharacter.tokenType());
 assertEquals("&", resetCharacter.getData());
 }

 @Test
 public void testEOF() {
 EOF eof = new EOF();
 EOF resetEOF = eof.reset();
 assertEquals("EOF", resetEOF.tokenType());
 }

 @Test (expected = AssertionError.class)
 public void testNullTagName() {
 Tag tag = new Tag();
 tag.name(null);
 }

 @Test (expected = AssertionError.class)
 public void testEmptyTagName() {
 Tag tag = new Tag();
 tag.name("");
 }

 @Test (expected = AssertionError.class)
 public void testNullAttributeName() {
 StartTag startTag = new StartTag();
 startTag.newAttribute().name(null);
 }

 @Test (expected = AssertionError.class)
 public void testEmptyAttributeName() {
 StartTag startTag = new StartTag();
 startTag.newAttribute().name("");
 }

 @Test
 public void testResetTokenType() {
 Token token = new Doctype();
 token = token.reset();
 assertEquals(TokenType.EOF, token.tokenType());
 }

 @Test
 public void testResetTokens() {
 Doctype doctype = new Doctype();
 StartTag startTag = new StartTag();
 EndTag endTag = new EndTag();
 Character character = new Character();
 EOF eof = new EOF();
 Doctype resetDoctype = doctype.reset();
 StartTag resetStartTag = startTag.reset();
 EndTag resetEndTag = endTag.reset();
 Character resetCharacter = character.reset();
 EOF resetEof = eof.reset();
 assertEquals(TokenType.Doctype, resetDoctype.tokenType());
 assertEquals(TokenType.StartTag, resetStartTag.tokenType());
 assertEquals(TokenType.EndTag, resetEndTag.tokenType());
 assertEquals(TokenType.Character, resetCharacter.tokenType());
 assertEquals(TokenType.EOF, resetEof.tokenType());
 }
}