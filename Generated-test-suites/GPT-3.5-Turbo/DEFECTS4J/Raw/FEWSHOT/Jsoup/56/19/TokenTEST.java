import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.TokenType;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("name");
        doctype.publicIdentifier.append("public");
        doctype.systemIdentifier.append("system");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.name.toString());
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tag";
        startTag.normalName = "tag";
        startTag.pendingAttributeName = "attribute";
        startTag.pendingAttributeValue.append("value");
        startTag.pendingAttributeValueS = "attr";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();

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

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");
        comment.bogus = true;

        comment.reset();

        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "data";

        character.reset();

        assertNull(character.data);
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        eof.reset();

        assertNotNull(eof);
    }

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("name");

        assertEquals("name", doctype.getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("public");

        assertEquals("public", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("system");

        assertEquals("system", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;

        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tag";

        assertEquals("tag", startTag.name());
    }

    @Test
    public void testStartTagNormalName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.normalName = "tag";

        assertEquals("tag", startTag.normalName());
    }

    @Test
    public void testStartTagIsSelfClosing() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.selfClosing = true;

        assertTrue(startTag.isSelfClosing());
    }

    @Test
    public void testStartTagGetAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.attributes = new Attributes();

        assertNotNull(startTag.getAttributes());
    }

    @Test
    public void testStartTagAppendTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tag";
        startTag.normalName = "tag";

        startTag.appendTagName("name");

        assertEquals("tagname", startTag.tagName);
        assertEquals("tagname", startTag.normalName);
    }

    @Test
    public void testStartTagAppendAttributeValue() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.pendingAttributeValue.append("value");

        assertEquals("value", startTag.pendingAttributeValue.toString());

        startTag.appendAttributeValue(" appended");

        assertEquals("value appended", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testStartTagAppendAttributeValueS() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.pendingAttributeValueS = "value";

        startTag.appendAttributeValue(" appended");

        assertEquals("value appended", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
    }

    @Test
    public void testEndTagName() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "tag";

        assertEquals("tag", endTag.name());
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");

        assertEquals("data", comment.getData());
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character();
        character.data = "data";

        assertEquals("data", character.getData());
    }

    @Test
    public void testDoctypeIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();

        assertTrue(doctype.isDoctype());
    }

    @Test
    public void testDoctypeAsDoctype() {
        Token.Doctype doctype = new Token.Doctype();

        assertSame(doctype, doctype.asDoctype());
    }

    @Test
    public void testStartTagIsStartTag() {
        Token.StartTag startTag = new Token.StartTag();

        assertTrue(startTag.isStartTag());
    }

    @Test
    public void testStartTagAsStartTag() {
        Token.StartTag startTag = new Token.StartTag();

        assertSame(startTag, startTag.asStartTag());
    }

    @Test
    public void testEndTagIsEndTag() {
        Token.EndTag endTag = new Token.EndTag();

        assertTrue(endTag.isEndTag());
    }

    @Test
    public void testEndTagAsEndTag() {
        Token.EndTag endTag = new Token.EndTag();

        assertSame(endTag, endTag.asEndTag());
    }

    @Test
    public void testCommentIsComment() {
        Token.Comment comment = new Token.Comment();

        assertTrue(comment.isComment());
    }

    @Test
    public void testCommentAsComment() {
        Token.Comment comment = new Token.Comment();

        assertSame(comment, comment.asComment());
    }

    @Test
    public void testCharacterIsCharacter() {
        Token.Character character = new Token.Character();

        assertTrue(character.isCharacter());
    }

    @Test
    public void testCharacterAsCharacter() {
        Token.Character character = new Token.Character();

        assertSame(character, character.asCharacter());
    }

    @Test
    public void testEOFIsEOF() {
        Token.EOF eof = new Token.EOF();

        assertTrue(eof.isEOF());
    }

    @Test
    public void testEOFAsEOF() {
        Token.EOF eof = new Token.EOF();

        assertSame(eof, eof.asEOF());
    }
}