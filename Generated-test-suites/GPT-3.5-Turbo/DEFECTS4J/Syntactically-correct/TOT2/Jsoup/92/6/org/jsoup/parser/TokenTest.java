import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals(Token.TokenType.Doctype, token.type);
    }

    @Test
    public void testResetToken() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("Test Name");
        doctype.pubSysKey = "Test Key";
        doctype.publicIdentifier.append("Test Public ID");
        doctype.systemIdentifier.append("Test System ID");
        doctype.forceQuirks = true;
        
        doctype.reset();
        
        assertEquals("", doctype.name.toString());
        assertNull(doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testNewAttribute() {
        Token.Tag tag = new Token.StartTag();
        tag.appendAttributeName("name");
        tag.appendAttributeValue("value");
        tag.newAttribute();

        assertEquals("value", tag.getAttributes().get("name"));
    }

    @Test
    public void testFinaliseTag() {
        Token.Tag tag = new Token.EndTag();
        tag.appendTagName("test");
        tag.finaliseTag();

        assertNull(tag.pendingAttributeName);
    }

    @Test
    public void testName() {
        Token.Tag tag = new Token.StartTag();
        tag.name("test");

        assertEquals("test", tag.name());
        assertEquals("test", tag.normalName());
    }

    @Test
    public void testIsSelfClosing() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.selfClosing = true;

        assertTrue(startTag.isSelfClosing());
    }

    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character();
        character.data("Test Data");

        assertEquals("Test Data", character.getData());
    }

    @Test
    public void testCommentData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("Test Comment");

        assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();

        assertEquals(Token.TokenType.EOF, eof.type);
    }
}