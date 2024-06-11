import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

 @Test
 public void testDoctypeToken() {
 Doctype doctype = new Doctype();
 doctype.name.append("html");
 doctype.publicIdentifier.append("PublicID:");
 doctype.systemIdentifier.append("SystemID:");
 doctype.forceQuirks = true;

 Token expectedDoctype = new Doctype();
 expectedDoctype.name.append("html");
 expectedDoctype.publicIdentifier.append("PublicID:");
 expectedDoctype.systemIdentifier.append("SystemID:");
 expectedDoctype.forceQuirks = true;

 assertEquals(TokenType.Doctype, doctype.type);
 assertEquals(expectedDoctype.tokenType(), doctype.tokenType());
 assertEquals(expectedDoctype.getName(), doctype.getName());
 assertEquals(expectedDoctype.getPublicIdentifier(), doctype.getPublicIdentifier());
 assertEquals(expectedDoctype.getSystemIdentifier(), doctype.getSystemIdentifier());
 assertEquals(expectedDoctype.isForceQuirks(), doctype.isForceQuirks());

 doctype.reset();
 assertNotNull(doctype);
 assertEquals(TokenType.Doctype, doctype.type);
 }

 @Test
 public void testStartTagToken() {
 StartTag startTag = new StartTag();
 startTag.nameAttr("html", new Attributes());

 Token expectedStartTag = new StartTag();
 expectedStartTag.name("html");
 expectedStartTag.getAttributes().put("html", new Attribute("html"));

 assertEquals(TokenType.StartTag, startTag.type);
 assertEquals(expectedStartTag.tokenType(), startTag.tokenType());
 assertEquals(expectedStartTag.name(), startTag.name());
 assertEquals(expectedStartTag.getAttributes(), startTag.getAttributes());

 startTag.reset();
 assertNotNull(startTag);
 assertEquals(TokenType.StartTag, startTag.type);
 }

 @Test
 public void testEndTagToken() {
 EndTag endTag = new EndTag();
 endTag.nameAttr("html", null);

 Token expectedEndTag = new EndTag();
 expectedEndTag.name("html");

 assertEquals(TokenType.EndTag, endTag.type);
 assertEquals(expectedEndTag.tokenType(), endTag.tokenType());
 assertEquals(expectedEndTag.name(), endTag.name());

 endTag.reset();
 assertNotNull(endTag);
 assertEquals(TokenType.EndTag, endTag.type);
 }

 @Test
 public void testCommentToken() {
 Comment comment = new Comment();
 comment.data.append("This is a comment.");

 Token expectedComment = new Comment();
 expectedComment.getData().append("This is a comment.");

 assertEquals(TokenType.Comment, comment.type);
 assertEquals(expectedComment.tokenType(), comment.tokenType());
 assertEquals(expectedComment.getData(), comment.getData());

 comment.reset();
 assertNotNull(comment);
 assertEquals(TokenType.Comment, comment.type);
 }

 @Test
 public void testCharacterToken() {
 Character character = new Character();
 character.data("&amp;");

 Token expectedCharacter = new Character();
 expectedCharacter.data("&amp;");

 assertEquals(TokenType.Character, character.type);
 assertEquals(expectedCharacter.tokenType(), character.tokenType());
 assertEquals(expectedCharacter.getData(), character.getData());

 character.reset();
 assertNotNull(character);
 assertEquals(TokenType.Character, character.type);
 }

 @Test
 public void testEOFToken() {
 EOF eof = new EOF();

 Token expectedEOF = new EOF();
 assertEquals(TokenType.EOF, eof.type);
 assertEquals(expectedEOF.tokenType(), eof.tokenType());

 eof.reset();
 assertNotNull(eof);
 assertEquals(TokenType.EOF, eof.type);
 }

 @Test(expected = AssertionError.class)
 public void testInvalidToken() {
 Token invalidToken = new Token() {
 @Override
 Token reset() {
 return super.reset();
 }
 };

 invalidToken.type = null;
 assertEquals(TokenType.Doctype, invalidToken.type); // should throw an NPE or AssertionError here
 }
}