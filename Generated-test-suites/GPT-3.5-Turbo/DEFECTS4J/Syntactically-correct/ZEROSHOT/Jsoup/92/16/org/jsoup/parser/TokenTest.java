import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenType() {
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

        doctype.name.append("name");
        doctype.pubSysKey = "pubSysKey";
        doctype.publicIdentifier.append("publicIdentifier");
        doctype.systemIdentifier.append("systemIdentifier");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.getName());
        assertEquals(null, doctype.getPubSysKey());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertEquals(false, doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.Tag() {
            @Override
            Token reset() {
                return null;
            }
        };

        tag.tagName = "tagName";
        tag.normalName = "normalName";
        tag.pendingAttributeName = "pendingAttributeName";
        tag.pendingAttributeValue.append("pendingAttributeValue");
        tag.pendingAttributeValueS = "pendingAttributeValueS";
        tag.hasEmptyAttributeValue = true;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;
        tag.attributes = new Attributes();

        tag.reset();

        assertEquals(null, tag.tagName);
        assertEquals(null, tag.normalName);
        assertEquals(null, tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue.toString());
        assertEquals(null, tag.pendingAttributeValueS);
        assertEquals(false, tag.hasEmptyAttributeValue);
        assertEquals(false, tag.hasPendingAttributeValue);
        assertEquals(false, tag.selfClosing);
        assertEquals(null, tag.getAttributes());
    }

    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();

        startTag.tagName = "tagName";
        startTag.normalName = "normalName";
        startTag.pendingAttributeName = "pendingAttributeName";
        startTag.pendingAttributeValue.append("pendingAttributeValue");
        startTag.pendingAttributeValueS = "pendingAttributeValueS";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();

        startTag.reset();

        assertEquals(null, startTag.tagName);
        assertEquals(null, startTag.normalName);
        assertEquals(null, startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertEquals(null, startTag.pendingAttributeValueS);
        assertEquals(false, startTag.hasEmptyAttributeValue);
        assertEquals(false, startTag.hasPendingAttributeValue);
        assertEquals(false, startTag.selfClosing);
        assertEquals(null, startTag.getAttributes());
    }

    @Test
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();

        endTag.tagName = "tagName";
        endTag.normalName = "normalName";
        endTag.pendingAttributeName = "pendingAttributeName";
        endTag.pendingAttributeValue.append("pendingAttributeValue");
        endTag.pendingAttributeValueS = "pendingAttributeValueS";
        endTag.hasEmptyAttributeValue = true;
        endTag.hasPendingAttributeValue = true;
        endTag.selfClosing = true;
        endTag.attributes = new Attributes();

        endTag.reset();

        assertEquals(null, endTag.tagName);
        assertEquals(null, endTag.normalName);
        assertEquals(null, endTag.pendingAttributeName);
        assertEquals("", endTag.pendingAttributeValue.toString());
        assertEquals(null, endTag.pendingAttributeValueS);
        assertEquals(false, endTag.hasEmptyAttributeValue);
        assertEquals(false, endTag.hasPendingAttributeValue);
        assertEquals(false, endTag.selfClosing);
        assertEquals(null, endTag.getAttributes());
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();

        comment.data.append("data");
        comment.bogus = true;

        comment.reset();

        assertEquals("", comment.getData());
        assertEquals(false, comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();

        character.data = "data";

        character.reset();

        assertEquals(null, character.getData());
    }

    @Test
    public void testCDataToString() {
        Token.CData cData = new Token.CData("data");

        assertEquals("<![CDATA[data]]>", cData.toString());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        eof.reset();

        assertNotNull(eof);
    }

    @Test
    public void testIsDoctype() {
        Token.Comment comment = new Token.Comment();

        assertFalse(comment.isDoctype());
    }

    @Test
    public void testAsDoctype() {
        Token.Comment comment = new Token.Comment();

        assertNull(comment.asDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token.Comment comment = new Token.Comment();

        assertFalse(comment.isStartTag());
    }

    @Test
    public void testAsStartTag() {
        Token.Comment comment = new Token.Comment();

        assertNull(comment.asStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token.Comment comment = new Token.Comment();

        assertFalse(comment.isEndTag());
    }

    @Test
    public void testAsEndTag() {
        Token.Comment comment = new Token.Comment();

        assertNull(comment.asEndTag());
    }

    @Test
    public void testIsComment() {
        Token.Comment comment = new Token.Comment();

        assertTrue(comment.isComment());
    }

    @Test
    public void testAsComment() {
        Token.Comment comment = new Token.Comment();

        assertNotNull(comment.asComment());
    }

    @Test
    public void testIsCharacter() {
        Token.Comment comment = new Token.Comment();

        assertFalse(comment.isCharacter());
    }

    @Test
    public void testIsCData() {
        Token.Character character = new Token.Character();

        assertFalse(character.isCData());
    }

    @Test
    public void testAsCharacter() {
        Token.Character character = new Token.Character();

        assertNull(character.asCharacter());
    }

    @Test
    public void testIsEOF() {
        Token.Comment comment = new Token.Comment();

        assertFalse(comment.isEOF());
    }
    
}