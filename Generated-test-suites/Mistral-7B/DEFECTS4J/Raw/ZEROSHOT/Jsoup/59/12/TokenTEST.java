import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        assertEquals("Doctype", doctypeToken.tokenType());
        assertEquals(TokenType.Doctype, doctypeToken.type);

        doctypeToken.reset();
        assertNull(doctypeToken.getName());
        assertNull(doctypeToken.getPubSysKey());
        assertNull(doctypeToken.getPublicIdentifier());
        assertNull(doctypeToken.getSystemIdentifier());
        assertFalse(doctypeToken.isForceQuirks());

        doctypeToken.getName("html");
        assertEquals("html", doctypeToken.getName());
        assertEquals("html", doctypeToken.getNormalName());

        doctypeToken.setPubSysKey("PSK");
        assertEquals("PSK", doctypeToken.getPubSysKey());

        doctypeToken.setPublicIdentifier("PID");
        assertEquals("PID", doctypeToken.getPublicIdentifier());

        doctypeToken.setSystemIdentifier("SID");
        assertEquals("SID", doctypeToken.getSystemIdentifier());

        doctypeToken.setForceQuirks(true);
        assertTrue(doctypeToken.isForceQuirks());

        doctypeToken.reset();
        assertFalse(doctypeToken.isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();
        assertEquals("StartTag", startTagToken.tokenType());
        assertEquals(TokenType.StartTag, startTagToken.type);

        startTagToken.reset();
        assertNull(startTagToken.getName());
        assertNull(startTagToken.getAttributes());
        assertFalse(startTagToken.isSelfClosing());

        startTagToken.name("div");
        assertEquals("div", startTagToken.getName());
        assertEquals("div", startTagToken.getNormalName());

        startTagToken.getAttributes().put("id", "testId");
        assertEquals("id", startTagToken.getPendingAttributeName());
        assertEquals("testId", startTagToken.getAttributes().get("id").getValue());

        startTagToken.appendAttributeValue("class");
        assertEquals("class", startTagToken.getPendingAttributeName());

        startTagToken.appendAttributeValue("class1");
        startTagToken.appendAttributeValue(" class2");
        assertEquals("class1 class2", startTagToken.getPendingAttributeValue().toString());

        startTagToken.newAttribute();
        assertEquals(1, startTagToken.getAttributes().size());
        assertSame(startTagToken.getAttributes().get("id"), new Attribute("id", "testId"));

        startTagToken.newAttribute();
        assertSame(startTagToken.getAttributes().get("class"), new Attribute("class", "class1 class2"));

        startTagToken.finaliseTag();
        assertSame(startTagToken.getAttributes().get("id"), new Attribute("id", "testId"));
        assertSame(startTagToken.getAttributes().get("class"), new Attribute("class", "class1 class2"));

        startTagToken.reset();
        assertNull(startTagToken.getAttributes());

        startTagToken.name("img");
        startTagToken.setSelfClosing(true);
        assertTrue(startTagToken.isSelfClosing());
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();
        assertEquals("EndTag", endTagToken.tokenType());
        assertEquals(TokenType.EndTag, endTagToken.type);

        endTagToken.reset();
        assertNull(endTagToken.getName());

        endTagToken.name("div");
        assertEquals("div", endTagToken.getName());
        assertEquals("div", endTagToken.getNormalName());
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        assertEquals("Comment", commentToken.tokenType());
        assertEquals(TokenType.Comment, commentToken.type);

        commentToken.reset();
        assertNull(commentToken.getData());

        commentToken.getData("This is a comment");
        assertEquals("This is a comment", commentToken.getData());
        assertEquals("This is a comment", commentToken.toString());
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        assertEquals("Character", characterToken.tokenType());
        assertEquals(TokenType.Character, characterToken.type);

        characterToken.reset();
        assertNull(characterToken.getData());

        characterToken.data("A");
        assertEquals("A", characterToken.getData());
        assertEquals("A", characterToken.toString());
    }

    @Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();
        assertEquals("EOF", eofToken.tokenType());
        assertEquals(
            TokenType.EOF,
            eofToken.type
        );

        eofToken.reset();
    }
}