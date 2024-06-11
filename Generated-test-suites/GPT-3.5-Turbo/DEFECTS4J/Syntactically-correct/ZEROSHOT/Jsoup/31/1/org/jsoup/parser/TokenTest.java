import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token.Doctype doctype = new Token.Doctype();
        assertEquals("Doctype", doctype.tokenType());

        Token.StartTag startTag = new Token.StartTag();
        assertEquals("StartTag", startTag.tokenType());

        Token.EndTag endTag = new Token.EndTag();
        assertEquals("EndTag", endTag.tokenType());

        Token.Comment comment = new Token.Comment();
        assertEquals("Comment", comment.tokenType());

        Token.Character character = new Token.Character("data");
        assertEquals("Character", character.tokenType());

        Token.EOF eof = new Token.EOF();
        assertEquals("EOF", eof.tokenType());
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
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tag";
        assertEquals("tag", startTag.name());
    }

    @Test
    public void testStartTagIsSelfClosing() {
        Token.StartTag startTag = new Token.StartTag();
        assertFalse(startTag.isSelfClosing());
    }

    @Test
    public void testStartTagGetAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        assertNotNull(startTag.getAttributes());
    }

    @Test
    public void testStartTagAppendTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendTagName("append");
        assertEquals("append", startTag.tagName);
    }

    @Test
    public void testStartTagAppendTagNameChar() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendTagName('a');
        assertEquals("a", startTag.tagName);
    }

    @Test
    public void testStartTagAppendAttributeName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendAttributeName("append");
        assertEquals("append", startTag.pendingAttributeName);
    }

    @Test
    public void testStartTagAppendAttributeNameChar() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendAttributeName('a');
        assertEquals("a", startTag.pendingAttributeName);
    }

    @Test
    public void testStartTagAppendAttributeValue() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendAttributeValue("append");
        assertNotNull(startTag.pendingAttributeValue);
        assertEquals("append", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testStartTagAppendAttributeValueChar() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendAttributeValue('a');
        assertNotNull(startTag.pendingAttributeValue);
        assertEquals("a", startTag.pendingAttributeValue.toString());
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
        Token.Character character = new Token.Character("data");
        assertEquals("data", character.getData());
    }

    @Test
    public void testIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        assertTrue(doctype.isDoctype());

        Token.StartTag startTag = new Token.StartTag();
        assertFalse(startTag.isDoctype());

        Token.EndTag endTag = new Token.EndTag();
        assertFalse(endTag.isDoctype());

        Token.Comment comment = new Token.Comment();
        assertFalse(comment.isDoctype());

        Token.Character character = new Token.Character("data");
        assertFalse(character.isDoctype());

        Token.EOF eof = new Token.EOF();
        assertFalse(eof.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        assertSame(doctype, doctype.asDoctype());

        Token.StartTag startTag = new Token.StartTag();
        assertNull(startTag.asDoctype());

        Token.EndTag endTag = new Token.EndTag();
        assertNull(endTag.asDoctype());

        Token.Comment comment = new Token.Comment();
        assertNull(comment.asDoctype());

        Token.Character character = new Token.Character("data");
        assertNull(character.asDoctype());

        Token.EOF eof = new Token.EOF();
        assertNull(eof.asDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        assertTrue(startTag.isStartTag());

        Token.Doctype doctype = new Token.Doctype();
        assertFalse(doctype.isStartTag());

        Token.EndTag endTag = new Token.EndTag();
        assertFalse(endTag.isStartTag());

        Token.Comment comment = new Token.Comment();
        assertFalse(comment.isStartTag());

        Token.Character character = new Token.Character("data");
        assertFalse(character.isStartTag());

        Token.EOF eof = new Token.EOF();
        assertFalse(eof.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        assertSame(startTag, startTag.asStartTag());

        Token.Doctype doctype = new Token.Doctype();
        assertNull(doctype.asStartTag());

        Token.EndTag endTag = new Token.EndTag();
        assertNull(endTag.asStartTag());

        Token.Comment comment = new Token.Comment();
        assertNull(comment.asStartTag());

        Token.Character character = new Token.Character("data");
        assertNull(character.asStartTag());

        Token.EOF eof = new Token.EOF();
        assertNull(eof.asStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        assertTrue(endTag.isEndTag());

        Token.Doctype doctype = new Token.Doctype();
        assertFalse(doctype.isEndTag());

        Token.StartTag startTag = new Token.StartTag();
        assertFalse(startTag.isEndTag());

        Token.Comment comment = new Token.Comment();
        assertFalse(comment.isEndTag());

        Token.Character character = new Token.Character("data");
        assertFalse(character.isEndTag());

        Token.EOF eof = new Token.EOF();
        assertFalse(eof.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        assertSame(endTag, endTag.asEndTag());

        Token.Doctype doctype = new Token.Doctype();
        assertNull(doctype.asEndTag());

        Token.StartTag startTag = new Token.StartTag();
        assertNull(startTag.asEndTag());

        Token.Comment comment = new Token.Comment();
        assertNull(comment.asEndTag());

        Token.Character character = new Token.Character("data");
        assertNull(character.asEndTag());

        Token.EOF eof = new Token.EOF();
        assertNull(eof.asEndTag());
    }

    @Test
    public void testIsComment() {
        Token.Comment comment = new Token.Comment();
        assertTrue(comment.isComment());

        Token.Doctype doctype = new Token.Doctype();
        assertFalse(doctype.isComment());

        Token.StartTag startTag = new Token.StartTag();
        assertFalse(startTag.isComment());

        Token.EndTag endTag = new Token.EndTag();
        assertFalse(endTag.isComment());

        Token.Character character = new Token.Character("data");
        assertFalse(character.isComment());

        Token.EOF eof = new Token.EOF();
        assertFalse(eof.isComment());
    }

    @Test
    public void testAsComment() {
        Token.Comment comment = new Token.Comment();
        assertSame(comment, comment.asComment());

        Token.Doctype doctype = new Token.Doctype();
        assertNull(doctype.asComment());

        Token.StartTag startTag = new Token.StartTag();
        assertNull(startTag.asComment());

        Token.EndTag endTag = new Token.EndTag();
        assertNull(endTag.asComment());

        Token.Character character = new Token.Character("data");
        assertNull(character.asComment());

        Token.EOF eof = new Token.EOF();
        assertNull(eof.asComment());
    }

    @Test
    public void testIsCharacter() {
        Token.Character character = new Token.Character("data");
        assertTrue(character.isCharacter());

        Token.Doctype doctype = new Token.Doctype();
        assertFalse(doctype.isCharacter());

        Token.StartTag startTag = new Token.StartTag();
        assertFalse(startTag.isCharacter());

        Token.EndTag endTag = new Token.EndTag();
        assertFalse(endTag.isCharacter());

        Token.Comment comment = new Token.Comment();
        assertFalse(comment.isCharacter());

        Token.EOF eof = new Token.EOF();
        assertFalse(eof.isCharacter());
    }

    @Test
    public void testAsCharacter() {
        Token.Character character = new Token.Character("data");
        assertSame(character, character.asCharacter());

        Token.Doctype doctype = new Token.Doctype();
        assertNull(doctype.asCharacter());

        Token.StartTag startTag = new Token.StartTag();
        assertNull(startTag.asCharacter());

        Token.EndTag endTag = new Token.EndTag();
        assertNull(endTag.asCharacter());

        Token.Comment comment = new Token.Comment();
        assertNull(comment.asCharacter());

        Token.EOF eof = new Token.EOF();
        assertNull(eof.asCharacter());
    }

    @Test
    public void testIsEOF() {
        Token.EOF eof = new Token.EOF();
        assertTrue(eof.isEOF());

        Token.Doctype doctype = new Token.Doctype();
        assertFalse(doctype.isEOF());

        Token.StartTag startTag = new Token.StartTag();
        assertFalse(startTag.isEOF());

        Token.EndTag endTag = new Token.EndTag();
        assertFalse(endTag.isEOF());

        Token.Comment comment = new Token.Comment();
        assertFalse(comment.isEOF());

        Token.Character character = new Token.Character("data");
        assertFalse(character.isEOF());
    }

    @Test
    public void testToString() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("name");
        doctype.publicIdentifier.append("public");
        doctype.systemIdentifier.append("system");
        assertEquals("<!DOCTYPE name public \"public\" \"system\">", doctype.toString());

        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tag";
        assertEquals("<tag>", startTag.toString());

        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "tag";
        assertEquals("</tag>", endTag.toString());

        Token.Comment comment = new Token.Comment();
        comment.data.append("data");
        assertEquals("<!--data-->", comment.toString());

        Token.Character character = new Token.Character("data");
        assertEquals("data", character.toString());
    }

    @Test
    public void testNewAttribute() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.pendingAttributeName = "name";
        startTag.newAttribute();
        assertNull(startTag.pendingAttributeName);
        assertNull(startTag.pendingAttributeValue);
    }

    @Test
    public void testFinaliseTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.pendingAttributeName = "name";
        startTag.pendingAttributeValue = new StringBuilder("value");
        startTag.finaliseTag();
        assertNull(startTag.pendingAttributeName);
        assertEquals("name", startTag.attributes.get(0).getKey());
        assertEquals("value", startTag.attributes.get(0).getValue());
    }
}