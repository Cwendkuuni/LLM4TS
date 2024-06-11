import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.Tag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.CData;

public class TokenTest {

 @Test
 public void testDoctypeReset() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("html"); // assert initial state
 doctype.reset();
 doctype.getName().equals(""); // assert reset state
 }

 @Test
 public void testDoctypeGetters() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("html");
 doctype.setName("html5");
 doctype.getPubSysKey().equals(null);
 doctype.setPubSysKey("//w3c.org/TR/html5/DTD");
 doctype.getPublicIdentifier().equals("html5");
 doctype.setPublicIdentifier("XHTML1 - Strict DTD");
 doctype.getSystemIdentifier().equals("");
 doctype.setSystemIdentifier("/path/to/myfile.html");
 doctype.isForceQuirks().equals(false);
 doctype.setForceQuirks(true);
 }

 @Test
 public void testTagReset() {
 Tag tag = new StartTag();
 tag.name().equals("tag"); // assert initial state
 tag.reset();
 tag.name().equals(null); // assert reset state
 }

 @Test
 public void testTagGetters() {
 StartTag startTag = new StartTag();
 startTag.name("tag").normalName("Tag");
 startTag.setTagName("TagName");
 startTag.isSelfClosing().equals(false);
 startTag.setSelfClosing(true);
 startTag.getAttributes().size().equals(0);
 startTag.newAttribute().getAttributes().size().equals(1);
 }

 @Test
 public void testTagToString() {
 StartTag startTag = new StartTag().name("tag");
 startTag.getAttributes().put("class", "test");
 Assert.assertEquals("<tag class=\"test\">", startTag.toString());
 }

 @Test
 public void testEndTagReset() {
 EndTag endTag = new EndTag();
 endTag.name().equals("tag"); // assert initial state
 endTag.reset();
 endTag.name().equals(null); // assert reset state
 }

 @Test
 public void testCommentReset() {
 Comment comment = new Comment();
 comment.getData().equals(""); // assert initial state
 comment.reset();
 comment.getData().equals(""); // assert reset state
 }

 @Test
 public void testCommentToString() {
 Comment comment = new Comment().getData("comment");
 Assert.assertEquals("<!--comment-->", comment.toString());
 }

 @Test
 public void testCharacterReset() {
 Character character = new Character();
 character.getData().equals(""); // assert initial state
 character.reset();
 character.getData().equals(""); // assert reset state
 }

 @Test
 public void testCharacterToString() {
 Character character = new Character().getData("a");
 Assert.assertEquals("a", character.toString());
 }

 @Test
 public void testCDATAReset() {
 CData cdata = new CData("<![CDATA[data]]>");
 cdata.getData().equals("data"); // assert initial state
 cdata.reset();
 cdata.getData().equals(""); // assert reset state
 }

 @Test(expected = AssertionError.class)
 public void testInvalidTagReset() {
 Token invalidToken = new Token();
 invalidToken.reset(); // should throw an AssertionError
 }

 @Test(expected = AssertionError.class)
 public void testInvalidTagName() {
 StartTag tag = new StartTag();
 tag.name("").equals(""); // assert initial state
 tag.setTagName(""); // should throw an AssertionError
 }

 @Test(expected = AssertionError.class)
 public void testInvalidAttributeName() {
 StartTag tag = new StartTag().name("tag").newAttribute();
 tag.setTagName("TagName");
 tag.newAttribute().setPendingAttributeName(""); // should throw an AssertionError
 }

 @Test(expected = AssertionError.class)
 public void testEmptyAttributeValue() {
 StartTag tag = new StartTag().name("tag").newAttribute();
 tag.newAttribute().setPendingAttributeValue("");
 tag.newAttribute(); // should throw an AssertionError
 }

 @Test(expected = AssertionError.class)
 public void testNullAttributeValue() {
 StartTag tag = new StartTag().name("tag").newAttribute();
 tag.newAttribute().setPendingAttributeValue(null);
 tag.newAttribute(); // should throw an AssertionError
 }

 @Test(expected = AssertionError.class)
 public void testLongAttributeValue() {
 StartTag tag = new StartTag().name("tag").newAttribute();
 tag.newAttribute().setPendingAttributeValue("x".repeat(1024)); // should throw an AssertionError
 }

 @Test(expected = AssertionError.class)
 public void testLongAttributeName() {
 StartTag tag = new StartTag();
 tag.newAttribute().setPendingAttributeName("A".repeat(1024)); // should throw an AssertionError
 tag.newAttribute();
 }

 @Test
 public void testIsTokenType() {
 Token token = new StartTag();
 Assert.assertTrue(token.isStartTag());
 Assert.assertFalse(token.isEndTag());
 Assert.assertFalse(token.isComment());
 Assert.assertFalse(token.isCharacter());
 Assert.assertFalse(token.isCData());
 Assert.assertFalse(token.isDoctype());
 Assert.assertFalse(token.isEOF());
 }

 @Test
 public void testIsTokenTypeDoctype() {
 Doctype doctype = new Doctype();
 Assert.assertFalse(doctype.isStartTag());
 Assert.assertFalse(doctype.isEndTag());
 Assert.assertFalse(doctype.isComment());
 Assert.assertFalse(doctype.isCharacter());
 Assert.assertFalse(doctype.isCData());
 Assert.assertTrue(doctype.isDoctype());
 Assert.assertFalse(doctype.isEOF());
 }

 @Test
 public void testIsTokenTypeComment() {
 Comment comment = new Comment();
 Assert.assertFalse(comment.isStartTag());
 Assert.assertFalse(comment.isEndTag());
 Assert.assertTrue(comment.isComment());
 Assert.assertFalse(comment.isCharacter());
 Assert.assertFalse(comment.isCData());
 Assert.assertFalse(comment.isDoctype());
 Assert.assertFalse(comment.isEOF());
 }

 @Test
 public void testIsTokenTypeCharacter() {
 Character character = new Character().getData("a");
 Assert.assertFalse(character.isStartTag());
 Assert.assertFalse(character.isEndTag());
 Assert.assertFalse(character.isComment());
 Assert.assertTrue(character.isCharacter());
 Assert.assertFalse(character.isCData());
 Assert.assertFalse(character.isDoctype());
 Assert.assertFalse(character.isEOF());
 }

 @Test
 public void testIsTokenTypeCData() {
 CData cdata = new CData("<![CDATA[data]]>");
 Assert.assertFalse(cdata.isStartTag());
 Assert.assertFalse(cdata.isEndTag());
 Assert.assertFalse(cdata.isComment());
 Assert.assertTrue(cdata.isCData());
 Assert.assertFalse(cdata.isDoctype());
 Assert.assertFalse(cdata.isEOF());
 }

 @Test
 public void testIsTokenTypeEOF() {
 EOF eof = new EOF();
 Assert.assertFalse(eof.isStartTag());
 Assert.assertFalse(eof.isEndTag());
 Assert.assertFalse(eof.isComment());
 Assert.assertFalse(eof.isCharacter());
 Assert.assertFalse(eof.isCData());
 Assert.assertTrue(eof.isEOF());
 }
}