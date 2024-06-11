
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = mock(Token.class);
        token.type = Token.TokenType.Doctype;
        assertEquals("Doctype", token.tokenType());

        token.type = Token.TokenType.StartTag;
        assertEquals("StartTag", token.tokenType());

        token.type = Token.TokenType.EndTag;
        assertEquals("EndTag", token.tokenType());

        token.type = Token.TokenType.Comment;
        assertEquals("Comment", token.tokenType());

        token.type = Token.TokenType.Character;
        assertEquals("Character", token.tokenType());

        token.type = Token.TokenType.EOF;
        assertEquals("EOF", token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("name");
        doctype.pubSysKey = "pubSysKey";
        doctype.publicIdentifier.append("publicIdentifier");
        doctype.systemIdentifier.append("systemIdentifier");
        doctype.forceQuirks = true;

        Token.Doctype resetDoctype = doctype.reset();

        assertEquals("", resetDoctype.name.toString());
        assertEquals(null, resetDoctype.pubSysKey);
        assertEquals("", resetDoctype.publicIdentifier.toString());
        assertEquals("", resetDoctype.systemIdentifier.toString());
        assertFalse(resetDoctype.forceQuirks);
    }

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("name");

        assertEquals("name", doctype.getName());
    }

    @Test
    public void testDoctypeGetPubSysKey() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.pubSysKey = "pubSysKey";

        assertEquals("pubSysKey", doctype.getPubSysKey());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("publicIdentifier");

        assertEquals("publicIdentifier", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("systemIdentifier");

        assertEquals("systemIdentifier", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;

        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "tagName";
        tag.normalName = "normalName";
        tag.pendingAttributeName = "pendingAttributeName";
        tag.pendingAttributeValue.append("pendingAttributeValue");
        tag.pendingAttributeValueS = "pendingAttributeValueS";
        tag.hasEmptyAttributeValue = true;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;
        tag.attributes = mock(Attributes.class);

        Token.Tag resetTag = tag.reset();

        assertEquals(null, resetTag.tagName);
        assertEquals(null, resetTag.normalName);
        assertEquals(null, resetTag.pendingAttributeName);
        assertEquals("", resetTag.pendingAttributeValue.toString());
        assertEquals(null, resetTag.pendingAttributeValueS);
        assertFalse(resetTag.hasEmptyAttributeValue);
        assertFalse(resetTag.hasPendingAttributeValue);
        assertFalse(resetTag.selfClosing);
        assertEquals(null, resetTag.attributes);
    }

    @Test
    public void testTagName() {
        Token.Tag tag = new Token.Tag();
        tag.tagName = "tagName";
        assertEquals("tagName", tag.name());

        tag.tagName = null;
        assertEquals(null, tag.name());
    }

    @Test
    public void testTagNormalName() {
        Token.Tag tag = new Token.Tag();
        tag.normalName = "normalName";
        assertEquals("normalName", tag.normalName());
    }

    @Test
    public void testTagIsSelfClosing() {
        Token.Tag tag = new Token.Tag();
        tag.selfClosing = true;
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testStartTagNameAttr() {
        Token.StartTag startTag = new Token.StartTag();
        Attributes attributes = mock(Attributes.class);
        startTag.nameAttr("name", attributes);

        assertEquals("name", startTag.tagName);
        assertEquals("name", startTag.normalName);
        assertEquals(attributes, startTag.attributes);
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tagName";
        assertEquals("<tagName>", startTag.toString());

        Attributes attributes = mock(Attributes.class);
        startTag.attributes = attributes;
        assertEquals("<tagName " + attributes.toString() + ">", startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "tagName";
        assertEquals("</tagName>", endTag.toString());
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");
        comment.bogus = true;

        Token.Comment resetComment = comment.reset();

        assertEquals("", resetComment.data.toString());
        assertFalse(resetComment.bogus);
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");

        assertEquals("data", comment.getData());
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "data";

        Token.Character resetCharacter = character.reset();

        assertEquals(null, resetCharacter.data);
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character();
        character.data = "data";

        assertEquals("data", character.getData());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        Token.EOF resetEOF = eof.reset();

        assertEquals(eof, resetEOF);
    }

    @Test
    public void testIsDoctype() {
        Token token = new Token.Doctype();
        assertTrue(token.isDoctype());

        token = new Token.EndTag();
        assertFalse(token.isDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token token = new Token.StartTag();
        assertTrue(token.isStartTag());

        token = new Token.Doctype();
        assertFalse(token.isStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token token = new Token.EndTag();
        assertTrue(token.isEndTag());

        token = new Token.StartTag();
        assertFalse(token.isEndTag());
    }

    @Test
    public void testIsComment() {
        Token token = new Token.Comment();
        assertTrue(token.isComment());

        token = new Token.Character();
        assertFalse(token.isComment());
    }

    @Test
    public void testIsCharacter() {
        Token token = new Token.Character();
        assertTrue(token.isCharacter());

        token = new Token.Doctype();
        assertFalse(token.isCharacter());
    }

    @Test
    public void testIsEOF() {
        Token token = new Token.EOF();
        assertTrue(token.isEOF());

        token = new Token.StartTag();
        assertFalse(token.isEOF());
    }
}
