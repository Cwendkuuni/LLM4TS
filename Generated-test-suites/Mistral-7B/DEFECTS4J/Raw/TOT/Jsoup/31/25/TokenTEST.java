import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Doctype;
import org.jsoup.parser.StartTag;
import org.jsoup.parser.EndTag;
import org.jsoup.parser.Comment;
import org.jsoup.parser.Character;
import org.jsoup.parser.EOF;

public class TokenTest {

 @Test
 public void testDoctype() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("doctype");
 doctype.getPublicIdentifier().equals("");
 doctype.getSystemIdentifier().equals("");
 doctype.isForceQuirks() == false;
 }

 @Test
 public void testDoctypeWithForceQuirks() {
 Doctype doctype = new Doctype();
 doctype.setForceQuirks(true);
 doctype.isForceQuirks() == true;
 }

 @Test(expected = IllegalArgumentException.class)
 public void testDoctypeWithEmptyName() {
 Doctype doctype = new Doctype();
 doctype.name("").getName().equals("");
 }

 @Test
 public void testStartTag() {
 StartTag startTag = new StartTag("html");
 startTag.name().equals("html");
 startTag.isSelfClosing() == false;
 }

 @Test
 public void testStartTagWithSelfClosing() {
 StartTag startTag = new StartTag("img").name("img").selfClosing(true);
 startTag.name().equals("img");
 startTag.isSelfClosing() == true;
 }

 @Test(expected = IllegalArgumentException.class)
 public void testStartTagWithNullName() {
 StartTag startTag = new StartTag(null);
 }

 @Test
 public void testEndTag() {
 EndTag endTag = new EndTag("body");
 endTag.name().equals("body");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEndTagWithNullName() {
 EndTag endTag = new EndTag(null);
 }

 @Test
 public void testComment() {
 Comment comment = new Comment();
 comment.getData().equals("");
 }

 @Test
 public void testCommentWithData() {
 Comment comment = new Comment("This is a comment");
 comment.getData().equals("This is a comment");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCommentWithEmptyData() {
 Comment comment = new Comment();
 comment.getData().equals("");
 }

 @Test
 public void testCharacter() {
 Character character = new Character("A");
 character.getData().equals("A");
 }

 @Test
 public void testCharacterWithMultipleCharacters() {
 Character character = new Character("ABC");
 character.getData().equals("ABC");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testCharacterWithEmptyData() {
 Character character = new Character("");
 character.getData().equals("");
 }

 @Test
 public void testEOF() {
 EOF eof = new EOF();
 }
}