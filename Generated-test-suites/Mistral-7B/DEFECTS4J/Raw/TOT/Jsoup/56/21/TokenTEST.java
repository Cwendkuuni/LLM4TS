import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenTest {

 private Token doctype;
 private Token startTag;
 private Token endTag;
 private Token comment;
 private Token character;
 private Token eof;

 @Before
 public void setUp() {
 doctype = new Token.Doctype();
 startTag = new Token.StartTag();
 endTag = new Token.EndTag();
 comment = new Token.Comment();
 character = new Token.Character();
 eof = new Token.EOF();
 }

 @Test
 public void testDoctypeReset() {
 Doctype doctypeReset = (Doctype) doctype.reset();
 assertEquals(TokenType.Doctype, doctypeReset.type);
 assertNull(doctypeReset.name);
 assertNull(doctypeReset.publicIdentifier);
 assertNull(doctypeReset.systemIdentifier);
 assertFalse(doctypeReset.isForceQuirks());
 }

 @Test
 public void testStartTagReset() {
 StartTag startTagReset = (StartTag) startTag.reset();
 assertEquals(TokenType.StartTag, startTagReset.type);
 assertNull(startTagReset.tagName);
 assertNull(startTagReset.normalName);
 assertNull(startTagReset.pendingAttributeName);
 assertNull(startTagReset.pendingAttributeValue);
 assertNull(startTagReset.pendingAttributeValueS);
 assertFalse(startTagReset.hasEmptyAttributeValue);
 assertFalse(startTagReset.hasPendingAttributeValue);
 assertFalse(startTagReset.isSelfClosing());
 assertNull(startTagReset.attributes);
 }

 @Test
 public void testEndTagReset() {
 EndTag endTagReset = (EndTag) endTag.reset();
 assertEquals(TokenType.EndTag, endTagReset.type);
 assertNull(endTagReset.tagName);
 assertNull(endTagReset.normalName);
 assertNull(endTagReset.attributes);
 }

 @Test
 public void testCommentReset() {
 Comment commentReset = (Comment) comment.reset();
 assertEquals(TokenType.Comment, commentReset.type);
 assertNull(commentReset.data);
 assertFalse(commentReset.isBogus());
 }

 @Test
 public void testCharacterReset() {
 Character characterReset = (Character) character.reset();
 assertEquals(TokenType.Character, characterReset.type);
 assertNull(characterReset.data);
 }

 @Test
 public void testEOFReset() {
 EOF eofReset = (EOF) eof.reset();
 assertEquals(TokenType.EOF, eofReset.type);
 }

 @Test
 public void testDoctypeGetMethods() {
 Doctype doctype = new Token.Doctype();
 doctype.name = "html";
 doctype.publicIdentifier = "W3C";
 doctype.systemIdentifier = "/foo/bar.html";
 doctype.forceQuirks = true;

 Doctype doctypeGet = (Doctype) doctype.reset();
 assertEquals("Doctype", doctypeGet.tokenType());
 assertEquals("html", doctypeGet.getName());
 assertEquals("W3C", doctypeGet.getPublicIdentifier());
 assertEquals("/foo/bar.html", doctypeGet.getSystemIdentifier());
 assertTrue(doctypeGet.isForceQuirks());
 }

 @Test
 public void testStartTagNameSetting() {
 StartTag startTag = new StartTag();
 startTag.name("html");
 StartTag startTagName = (StartTag) startTag.reset();
 assertEquals("html", startTagName.name());
 assertEquals("html", startTagName.normalName());
 }

 @Test
 public void testStartTagAttributes() {
 StartTag startTag = new StartTag();
 startTag.newAttribute().name("id").value("test");
 startTag.newAttribute().name("class").value("testClass");
 startTag.finaliseTag();

 Attributes attributes = startTag.getAttributes();
 assertEquals(2, attributes.size());
 assertEquals("id", attributes.getKey(0));
 assertEquals("test", attributes.getValue(0));
 assertEquals("class", attributes.getKey(1));
 assertEquals("testClass", attributes.getValue(1));
 }

 @Test
 public void testCommentDataSetting() {
 Comment comment = new Comment();
 comment.data("This is a comment.");

 Comment commentData = (Comment) comment.reset();
 assertEquals("Comment", commentData.tokenType());
 assertEquals("This is a comment.", commentData.getData());
 }

 @Test
 public void testCharacterDataSetting() {
 Character character = new Character();
 character.data("A");

 Character characterData = (Character) character.reset();
 assertEquals("Character", characterData.tokenType());
 assertEquals("A", characterData.getData());
 }
}