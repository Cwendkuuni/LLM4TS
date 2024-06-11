import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token doctype = new Token.Doctype();
        doctype.getName().equals("doctype"); // assert name before reset

        Token.reset(doctype);

        assertNull(doctype.getName());
        assertFalse(doctype.isForceQuirks());
        assertNull(doctype.getPubSysKey());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
    }

    @Test
    public void testStartTagReset() {
        Token startTag = new Token.StartTag();
        startTag.name().equals("startTag"); // assert name before reset

        Token.reset(startTag);

        assertNull(startTag.name());
        assertNull(startTag.normalName());
        assertNull(startTag.getAttributes());
        assertFalse(startTag.isSelfClosing());
    }

    @Test
    public void testEndTagReset() {
        Token endTag = new Token.EndTag();
        endTag.name().equals("endTag"); // assert name before reset

        Token.reset(endTag);

        assertNull(endTag.name());
        assertNull(endTag.normalName());
        assertNull(endTag.getAttributes());
        assertFalse(endTag.isSelfClosing());
    }

    @Test
    public void testCommentReset() {
        Token comment = new Token.Comment();
        comment.getData().equals("comment"); // assert data before reset

        Token.reset(comment);

        assertNull(comment.getData());
        assertFalse(comment.isBogus());
    }

    @Test
    public void testCharacterReset() {
        Token character = new Token.Character();
        character.getData().equals("character"); // assert data before reset

        Token.reset(character);

        assertNull(character.getData());
    }

    @Test
    public void testCDataReset() {
        Token cData = new Token.CData("cData");
        cData.getData().equals("cData"); // assert data before reset

        Token.reset(cData);

        assertNull(cData.getData());
    }

    @Test
    public void testEOFReset() {
        Token eof = new Token.EOF();

        Token.reset(eof);

        assertSame(TokenType.EOF, eof.type);
    }

    @Test
    public void testDoctypeNewAttributes() {
        Token doctype = new Token.Doctype();
        doctype.newAttribute();
        doctype.newAttribute();

        assertEquals(2, doctype.attributes.size());
    }

    @Test
    public void testStartTagNewAttributes() {
        Token startTag = new Token.StartTag();
        startTag.name("testTag");
        startTag.newAttribute();
        startTag.newAttribute();

        assertEquals(2, startTag.attributes.size());
        assertEquals("testTag", startTag.name());
    }

    @Test
    public void testEndTagNewAttributes() {
        Token endTag = new Token.EndTag();
        endTag.name("testTag");
        endTag.newAttribute();
        endTag.newAttribute();

        assertEquals(2, endTag.attributes.size());
        assertEquals("testTag", endTag.name());
    }
}