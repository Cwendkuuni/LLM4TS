import org.junit.Test;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Token;
import static org.junit.Assert.assertEquals;

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
        tag.reset();
        assertEquals(null, tag.tagName);
        assertEquals(null, tag.normalName);
        assertEquals(null, tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue.toString());
        assertEquals(null, tag.pendingAttributeValueS);
        assertEquals(false, tag.hasEmptyAttributeValue);
        assertEquals(false, tag.hasPendingAttributeValue);
        assertEquals(false, tag.selfClosing);
        assertEquals(null, tag.attributes);
    }

    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.reset();
        assertEquals(null, startTag.tagName);
        assertEquals(null, startTag.normalName);
        assertEquals(null, startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertEquals(null, startTag.pendingAttributeValueS);
        assertEquals(false, startTag.hasEmptyAttributeValue);
        assertEquals(false, startTag.hasPendingAttributeValue);
        assertEquals(false, startTag.selfClosing);
        assertEquals(null, startTag.attributes);
    }

    @Test
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.reset();
        assertEquals(null, endTag.tagName);
        assertEquals(null, endTag.normalName);
        assertEquals(null, endTag.pendingAttributeName);
        assertEquals("", endTag.pendingAttributeValue.toString());
        assertEquals(null, endTag.pendingAttributeValueS);
        assertEquals(false, endTag.hasEmptyAttributeValue);
        assertEquals(false, endTag.hasPendingAttributeValue);
        assertEquals(false, endTag.selfClosing);
        assertEquals(null, endTag.attributes);
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.reset();
        assertEquals("", comment.data.toString());
        assertEquals(false, comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character() {
            @Override
            Token reset() {
                return null;
            }
        };
        character.reset();
        assertEquals(null, character.getData());
    }

    @Test
    public void testCData() {
        Token.CData cdata = new Token.CData("data");
        assertEquals("data", cdata.getData());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        eof.reset();
        assertEquals(null, eof.reset());
    }

    @Test
    public void testIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        assertEquals(true, doctype.isDoctype());
    }

    @Test
    public void testIsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        assertEquals(true, startTag.isStartTag());
    }

    @Test
    public void testIsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        assertEquals(true, endTag.isEndTag());
    }

    @Test
    public void testIsComment() {
        Token.Comment comment = new Token.Comment();
        assertEquals(true, comment.isComment());
    }

    @Test
    public void testIsCharacter() {
        Token.Character character = new Token.Character();
        assertEquals(true, character.isCharacter());
    }

    @Test
    public void testIsCData() {
        Token.CData cdata = new Token.CData("data");
        assertEquals(true, cdata.isCData());
    }

    @Test
    public void testIsEOF() {
        Token.EOF eof = new Token.EOF();
        assertEquals(true, eof.isEOF());
    }

}