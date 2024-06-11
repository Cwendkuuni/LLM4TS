import org.junit.Test;
import org.jsoup.parser.Token;

import static org.junit.Assert.*;

public class TokenTest {

    // 

    @Test
    public void testTokenType(){
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };

        assertEquals("Token", token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("testName");
        doctype.publicIdentifier.append("testPublicIdentifier");
        doctype.systemIdentifier.append("testSystemIdentifier");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.getName());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "testTagName";
        startTag.normalName = "testNormalName";
        startTag.pendingAttributeName = "testAttributeName";
        startTag.pendingAttributeValue.append("testAttributeValue");
        startTag.pendingAttributeValueS = "testAttributeValueS";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = null;

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
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "testTagName";
        endTag.normalName = "testNormalName";
        endTag.pendingAttributeName = "testAttributeName";
        endTag.pendingAttributeValue.append("testAttributeValue");
        endTag.pendingAttributeValueS = "testAttributeValueS";
        endTag.hasEmptyAttributeValue = true;
        endTag.hasPendingAttributeValue = true;
        endTag.selfClosing = true;
        endTag.attributes = null;

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

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("testData");
        comment.bogus = true;

        comment.reset();

        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "testData";

        character.reset();

        assertNull(character.data);
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        eof.reset();

        assertEquals(Token.EOF.class, eof.reset().getClass());
    }

    @Test
    public void testIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();

        assertTrue(doctype.isDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token.StartTag startTag = new Token.StartTag();

        assertTrue(startTag.isStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token.EndTag endTag = new Token.EndTag();

        assertTrue(endTag.isEndTag());
    }

    @Test
    public void testIsComment() {
        Token.Comment comment = new Token.Comment();

        assertTrue(comment.isComment());
    }

    @Test
    public void testIsCharacter() {
        Token.Character character = new Token.Character();

        assertTrue(character.isCharacter());
    }

    @Test
    public void testIsEOF() {
        Token.EOF eof = new Token.EOF();

        assertTrue(eof.isEOF());
    }

    // 
}