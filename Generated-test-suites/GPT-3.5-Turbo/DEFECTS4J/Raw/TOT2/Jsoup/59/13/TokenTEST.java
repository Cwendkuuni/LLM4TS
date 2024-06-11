import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("TEST");
        doctype.pubSysKey = "TEST";
        doctype.publicIdentifier.append("TEST");
        doctype.systemIdentifier.append("TEST");
        doctype.forceQuirks = true;
        
        doctype.reset();

        assertEquals("", doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.nameAttr("test", null);
        startTag.appendTagName("TEST");
        startTag.appendAttributeName("TEST");
        startTag.setEmptyAttributeValue();
        
        startTag.reset();

        assertNull(startTag.name());
        assertNull(startTag.normalName());
        assertNull(startTag.getAttributes());
        assertFalse(startTag.isSelfClosing());
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("TEST");
        comment.bogus = true;
        
        comment.reset();

        assertEquals("", comment.getData());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data("TEST");
        
        character.reset();

        assertNull(character.getData());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        eof.reset();

        // No functionality to be tested in EOF reset method
    }
}