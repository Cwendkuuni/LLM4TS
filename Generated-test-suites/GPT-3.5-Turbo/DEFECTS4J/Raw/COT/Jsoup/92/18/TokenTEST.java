import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());

        token = new Token.StartTag();
        assertEquals("StartTag", token.tokenType());

        token = new Token.EndTag();
        assertEquals("EndTag", token.tokenType());

        token = new Token.Comment();
        assertEquals("Comment", token.tokenType());

        token = new Token.Character();
        assertEquals("Character", token.tokenType());

        token = new Token.EOF();
        assertEquals("EOF", token.tokenType());
    }

    // Edge Cases and Exception Handling tests

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "pubSysKey";
        doctype.publicIdentifier.append("//W3C//DTD HTML 4.01//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.getName());
        assertEquals(null, doctype.getPubSysKey());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertEquals(false, doctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "p";
        startTag.normalName = "p";
        startTag.pendingAttributeName = "class";
        startTag.pendingAttributeValue.append("paragraph");

        startTag.reset();

        assertEquals(null, startTag.tagName);
        assertEquals(null, startTag.normalName);
        assertEquals(null, startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertEquals(false, startTag.hasEmptyAttributeValue);
        assertEquals(false, startTag.hasPendingAttributeValue);
        assertEquals(false, startTag.selfClosing);
        assertEquals(null, startTag.attributes);
    }

    @Test
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "p";
        endTag.normalName = "p";

        endTag.reset();

        assertEquals(null, endTag.tagName);
        assertEquals(null, endTag.normalName);
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        comment.bogus = true;

        comment.reset();

        assertEquals("", comment.getData());
        assertEquals(false, comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "This is a character";

        character.reset();

        assertEquals(null, character.getData());
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        eof.reset();

        assertEquals(eof, eof.reset());
    }
}