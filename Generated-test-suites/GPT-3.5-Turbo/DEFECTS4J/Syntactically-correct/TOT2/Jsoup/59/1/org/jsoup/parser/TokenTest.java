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
        doctype.name.append("testName");
        doctype.pubSysKey = "testPubSysKey";
        doctype.publicIdentifier.append("testPublicIdentifier");
        doctype.systemIdentifier.append("testSystemIdentifier");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.name.toString());
        assertNull(doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.nameAttr("div", new Attributes());
        assertEquals("<div>", startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("div");
        assertEquals("</div>", endTag.toString());
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
    public void testCharacterData() {
        Token.Character character = new Token.Character();
        character.data("testData");
        assertEquals("testData", character.getData());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        assertEquals(eof, eof.reset());
    }
}