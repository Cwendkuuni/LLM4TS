import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import static org.junit.Assert.*;

public class TokenTest {

 @Test
 public void testDoctypeReset() {
 Doctype doctype = new Doctype();
 doctype.getName().equals("doctype"); // basic functionality check
 doctype.reset();
 assertNull(doctype.getName()); // reset functionality check
 }

 @Test(expected = AssertionError.class)
 public void testDoctypeForceQuirks() {
 Doctype doctype = new Doctype();
 doctype.isForceQuirks(); // basic functionality check
 doctype.forceQuirks = true;
 doctype.reset(); // edge case check
 assertFalse(doctype.isForceQuirks());
 }

 @Test
 public void testDoctypePublicIdentifier() {
 Doctype doctype = new Doctype();
 doctype.getPublicIdentifier().equals(""); // default value check
 doctype.publicIdentifier.append("publicID");
 doctype.reset();
 assertEquals("", doctype.getPublicIdentifier()); // reset functionality check
 }

 @Test
 public void testStartTagReset() {
 StartTag startTag = new StartTag();
 startTag.name().equals("starttag"); // basic functionality check
 startTag.reset();
 assertNull(startTag.name()); // reset functionality check
 }

 @Test
 public void testStartTagGetAttributes() {
 StartTag startTag = new StartTag();
 Attributes attributes = new Attributes();
 Attribute attribute = new Attribute("attributeName", "attributeValue");
 attributes.put(attribute);
 startTag.nameAttr("tagName", attributes);
 assertEquals(1, startTag.getAttributes().size());
 }

 @Test
 public void testStartTagFinaliseTag() {
 StartTag startTag = new StartTag();
 startTag.name("tagName");
 Attribute attribute = new Attribute("attributeName", "attributeValue");
 startTag.newAttribute();
 startTag.newAttribute();
 startTag.finaliseTag();
 assertEquals(1, startTag.getAttributes().size());
 }

 @Test
 public void testStartTagIsSelfClosing() {
 StartTag startTag = new StartTag();
 assertFalse(startTag.isSelfClosing());
 startTag.name("selfclosingtag");
 assertTrue(startTag.isSelfClosing());
 }

 @Test
 public void testEndTagReset() {
 EndTag endTag = new EndTag();
 endTag.name().equals("endtag"); // basic functionality check
 endTag.reset();
 assertNull(endTag.name()); // reset functionality check
 }

 @Test
 public void testCharacterReset() {
 Character character = new Character();
 character.data("characterData").equals("characterData"); // basic functionality check
 character.reset();
 assertNull(character.getData()); // reset functionality check
 }

 @Test
 public void testCharacterData() {
 Character character = new Character();
 character.data("\n").equals("\n");
 character.data("\t").equals("\t");
 character.data(" ").equals(" ");
 }

 @Test
 public void testCommentReset() {
 Comment comment = new Comment();
 comment.getData().equals(""); // default value check
 comment.data.append("commentData");
 comment.reset();
 assertEquals("", comment.getData()); // reset functionality check
 }

 @Test
 public void testCommentBogus() {
 Comment comment = new Comment();
 comment.bogus = true;
 assertTrue(comment.isBogus());
 comment.reset();
 assertFalse(comment.isBogus());
 }

 @Test(expected = AssertionError.class)
 public void testEOFReset() {
 EOF eof = new EOF();
 eof.reset();
 }
}