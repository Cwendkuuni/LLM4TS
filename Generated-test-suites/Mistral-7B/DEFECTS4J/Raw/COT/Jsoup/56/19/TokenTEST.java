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

    @Test
    public void testDoctypeReset() {
        TokenDoctype tokenDoctype = new Token.Doctype();
        tokenDoctype.newName("html");
        tokenDoctype.newPublicIdentifier("-//W3C//DTD XHTML 1.0//EN");
        tokenDoctype.newSystemIdentifier("xhtml1.dtd");
        tokenDoctype.setForceQuirks(true);

        TokenDoctype resetTokenDoctype = tokenDoctype.reset();
        assertSame(tokenDoctype, resetTokenDoctype);
        assertNull(resetTokenDoctype.getName());
        assertNull(resetTokenDoctype.getPublicIdentifier());
        assertNull(resetTokenDoctype.getSystemIdentifier());
        assertFalse(resetTokenDoctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        TokenTag tokenTag = new Token.StartTag();
        tokenTag.newName("div");
        tokenTag.newAttribute("class", "red");

        TokenTag resetTokenTag = tokenTag.reset();
        assertSame(tokenTag, resetTokenTag);
        assertNull(resetTokenTag.tagName);
        assertNull(resetTokenTag.normalName);
        assertNull(resetTokenTag.pendingAttributeName);
        assertNull(resetTokenTag.pendingAttributeValue);
        assertNull(resetTokenTag.pendingAttributeValueS);
        assertFalse(resetTokenTag.hasEmptyAttributeValue);
        assertFalse(resetTokenTag.hasPendingAttributeValue);
        assertFalse(resetTokenTag.selfClosing);
        assertNull(resetTokenTag.attributes);
    }

    @Test
    public void testCommentReset() {
        TokenComment tokenComment = new Token.Comment();
        tokenComment.newData("This is a comment");

        TokenComment resetTokenComment = tokenComment.reset();
        assertSame(tokenComment, resetTokenComment);
        assertNull(resetTokenComment.getData());
        assertFalse(resetTokenComment.isBogus());
    }

    @Test(expected = AssertionError.class)
    public void testCharacterReset() {
        TokenCharacter tokenCharacter = new Token.Character();
        tokenCharacter.newData("ABC");

        TokenCharacter resetTokenCharacter = tokenCharacter.reset();
        assertSame(tokenCharacter, resetTokenCharacter);
        assertNull(resetTokenCharacter.getData());
    }

    @Test
    public void testEOFReset() {
        TokenEOF tokenEOF = new Token.EOF();

        TokenEOF resetTokenEOF = tokenEOF.reset();
        assertSame(tokenEOF, resetTokenEOF);
    }
}