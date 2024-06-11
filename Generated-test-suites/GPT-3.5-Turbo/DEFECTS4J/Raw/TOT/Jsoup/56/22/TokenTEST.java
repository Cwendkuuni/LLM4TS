import org.junit.Test;
import org.jsoup.parser.Token.*;

import static org.junit.Assert.*;

public class TokenTest {

   // Testing tokenType() method
   @Test
   public void testTokenType() {
      Token token = new Doctype();
      assertEquals("Doctype", token.tokenType());
   }

   // Testing reset() method for Doctype token
   @Test
   public void testDoctypeReset() {
      Doctype doctype = new Doctype();
      doctype.name.append("html");
      doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01//EN");
      doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
      doctype.forceQuirks = true;

      doctype.reset();

      assertEquals("", doctype.getName());
      assertEquals("", doctype.getPublicIdentifier());
      assertEquals("", doctype.getSystemIdentifier());
      assertFalse(doctype.isForceQuirks());
   }

   // Testing reset() method for StartTag token
   @Test
   public void testStartTagReset() {
      StartTag startTag = new StartTag();
      startTag.tagName = "div";
      startTag.normalName = "div";
      startTag.pendingAttributeName = "id";
      startTag.pendingAttributeValue.append("content");
      startTag.pendingAttributeValueS = null;
      startTag.hasEmptyAttributeValue = true;
      startTag.hasPendingAttributeValue = true;
      startTag.selfClosing = true;

      startTag.reset();

      assertNull(startTag.tagName);
      assertNull(startTag.normalName);
      assertNull(startTag.pendingAttributeName);
      assertEquals("", startTag.pendingAttributeValue.toString());
      assertNull(startTag.pendingAttributeValueS);
      assertFalse(startTag.hasEmptyAttributeValue);
      assertFalse(startTag.hasPendingAttributeValue);
      assertFalse(startTag.selfClosing);
      assertNull(startTag.attributes);
   }

   // Testing reset() method for EndTag token
   @Test
   public void testEndTagReset() {
      EndTag endTag = new EndTag();
      endTag.tagName = "div";
      endTag.normalName = "div";
      endTag.pendingAttributeName = "id";
      endTag.pendingAttributeValue.append("content");
      endTag.pendingAttributeValueS = null;
      endTag.hasEmptyAttributeValue = true;
      endTag.hasPendingAttributeValue = true;
      endTag.selfClosing = true;
      endTag.attributes = new Attributes();

      endTag.reset();

      assertNull(endTag.tagName);
      assertNull(endTag.normalName);
      assertNull(endTag.pendingAttributeName);
      assertEquals("", endTag.pendingAttributeValue.toString());
      assertNull(endTag.pendingAttributeValueS);
      assertFalse(endTag.hasEmptyAttributeValue);
      assertFalse(endTag.hasPendingAttributeValue);
      assertFalse(endTag.selfClosing);
      assertNull(endTag.attributes);
   }

   // Testing reset() method for Comment token
   @Test
   public void testCommentReset() {
      Comment comment = new Comment();
      comment.data.append("This is a comment");
      comment.bogus = true;

      comment.reset();

      assertEquals("", comment.getData());
      assertFalse(comment.bogus);
   }

   // Testing reset() method for Character token
   @Test
   public void testCharacterReset() {
      Character character = new Character();
      character.data("Test Character");

      character.reset();

      assertNull(character.getData());
   }

   // Testing isDoctype() method
   @Test
   public void testIsDoctype() {
      Token token = new Doctype();
      assertTrue(token.isDoctype());
      assertFalse(token.isStartTag());
      assertFalse(token.isEndTag());
      assertFalse(token.isComment());
      assertFalse(token.isCharacter());
      assertFalse(token.isEOF());
   }

   // Testing isStartTag() method
   @Test
   public void testIsStartTag() {
      Token token = new StartTag();
      assertFalse(token.isDoctype());
      assertTrue(token.isStartTag());
      assertFalse(token.isEndTag());
      assertFalse(token.isComment());
      assertFalse(token.isCharacter());
      assertFalse(token.isEOF());
   }

   // Testing isEndTag() method
   @Test
   public void testIsEndTag() {
      Token token = new EndTag();
      assertFalse(token.isDoctype());
      assertFalse(token.isStartTag());
      assertTrue(token.isEndTag());
      assertFalse(token.isComment());
      assertFalse(token.isCharacter());
      assertFalse(token.isEOF());
   }

   // Testing isComment() method
   @Test
   public void testIsComment() {
      Token token = new Comment();
      assertFalse(token.isDoctype());
      assertFalse(token.isStartTag());
      assertFalse(token.isEndTag());
      assertTrue(token.isComment());
      assertFalse(token.isCharacter());
      assertFalse(token.isEOF());
   }

   // Testing isCharacter() method
   @Test
   public void testIsCharacter() {
      Token token = new Character();
      assertFalse(token.isDoctype());
      assertFalse(token.isStartTag());
      assertFalse(token.isEndTag());
      assertFalse(token.isComment());
      assertTrue(token.isCharacter());
      assertFalse(token.isEOF());
   }

   // Testing isEOF() method
   @Test
   public void testIsEOF() {
      Token token = new EOF();
      assertFalse(token.isDoctype());
      assertFalse(token.isStartTag());
      assertFalse(token.isEndTag());
      assertFalse(token.isComment());
      assertFalse(token.isCharacter());

      assertTrue(token.isEOF());
   }

}