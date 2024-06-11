import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        
        assertEquals(Token.TokenType.Doctype, token.type);
        
        token.type = Token.TokenType.StartTag;
        assertEquals(Token.TokenType.StartTag, token.type);
        
        token.type = Token.TokenType.EndTag;
        assertEquals(Token.TokenType.EndTag, token.type);
        
        token.type = Token.TokenType.Comment;
        assertEquals(Token.TokenType.Comment, token.type);
        
        token.type = Token.TokenType.Character;
        assertEquals(Token.TokenType.Character, token.type);
        
        token.type = Token.TokenType.EOF;
        assertEquals(Token.TokenType.EOF, token.type);
    }

    @Test
    public void testTokenReset() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        
        token.type = Token.TokenType.Doctype;
        Token resetToken = token.reset();
        
        assertNull(resetToken.type);
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        
        doctype.name.append("test");
        doctype.pubSysKey = "pubSysKey";
        doctype.publicIdentifier.append("publicIdentifier");
        doctype.systemIdentifier.append("systemIdentifier");
        doctype.forceQuirks = true;
        
        Token.Doctype resetDoctype = doctype.reset();
        
        assertEquals("", resetDoctype.name.toString());
        assertNull(resetDoctype.pubSysKey);
        assertEquals("", resetDoctype.publicIdentifier.toString());
        assertEquals("", resetDoctype.systemIdentifier.toString());
        assertFalse(resetDoctype.forceQuirks);
    }

    @Test
    public void testDoctypeGetters() {
        Token.Doctype doctype = new Token.Doctype();
        
        doctype.name.append("test");
        doctype.pubSysKey = "pubSysKey";
        doctype.publicIdentifier.append("publicIdentifier");
        doctype.systemIdentifier.append("systemIdentifier");
        doctype.forceQuirks = true;
        
        assertEquals("test", doctype.getName());
        assertEquals("pubSysKey", doctype.getPubSysKey());
        assertEquals("publicIdentifier", doctype.getPublicIdentifier());
        assertEquals("systemIdentifier", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        
        tag.tagName = "test";
        tag.normalName = "test";
        tag.pendingAttributeName = "attr";
        tag.pendingAttributeValue.append("value");
        tag.pendingAttributeValueS = null;
        tag.hasEmptyAttributeValue = true;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;
        tag.attributes = new Attributes();
        
        Token.Tag resetTag = tag.reset();
        
        assertNull(resetTag.tagName);
        assertNull(resetTag.normalName);
        assertNull(resetTag.pendingAttributeName);
        assertEquals("", resetTag.pendingAttributeValue.toString());
        assertNull(resetTag.pendingAttributeValueS);
        assertFalse(resetTag.hasEmptyAttributeValue);
        assertFalse(resetTag.hasPendingAttributeValue);
        assertFalse(resetTag.selfClosing);
        assertNull(resetTag.attributes);
    }

    @Test
    public void testTagSettersAndGetters() {
        Token.Tag tag = new Token.StartTag();
        
        tag.name("test");
        assertEquals("test", tag.name());
        assertEquals("test", tag.normalName());
        
        tag.appendTagName("ing");
        assertEquals("testing", tag.name());
        assertEquals("testing", tag.normalName());
        
        tag.appendAttributeName("attr");
        assertEquals("attr", tag.pendingAttributeName);
        
        tag.appendAttributeValue("value");
        assertEquals("value", tag.pendingAttributeValue.toString());
        
        tag.appendAttributeValue('s');
        assertEquals("values", tag.pendingAttributeValue.toString());
        
        tag.setEmptyAttributeValue();
        assertTrue(tag.hasEmptyAttributeValue);
        
        tag.ensureAttributeValue();
        assertFalse(tag.hasPendingAttributeValue);
        assertEquals(0, tag.pendingAttributeValue.length());
        assertNull(tag.pendingAttributeValueS);
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "test";
        startTag.attributes = new Attributes();
        
        String expected = "<test>";
        
        assertEquals(expected, startTag.toString());
        
        Attribute attr = new Attribute("attr", "value");
        startTag.attributes.put(attr);
        
        expected = "<test attr=\"value\">";
        
        assertEquals(expected, startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "test";
        
        String expected = "</test>";
        
        assertEquals(expected, endTag.toString());
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("test");
        comment.bogus = true;
        
        Token.Comment resetComment = comment.reset();
        
        assertEquals("", resetComment.data.toString());
        assertFalse(resetComment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "test";
        
        Token.Character resetCharacter = character.reset();
        
        assertNull(resetCharacter.data);
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character();
        character.data("test");
        
        assertEquals("test", character.getData());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        
        Token.EOF resetEOF = eof.reset();
        
        assertEquals(eof, resetEOF);
    }

}