import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Tokenizer; // for EOF token

public class TokenTest {

    @Test
    public void testDoctypeConstructor() {
        Token doctypeToken = new Token.Doctype();
        assertEquals("Doctype", doctypeToken.tokenType());
        assertNull(doctypeToken.asDoctype().getName());
        assertNull(doctypeToken.asDoctype().getPublicIdentifier());
        assertNull(doctypeToken.asDoctype().getSystemIdentifier());
        assertFalse(doctypeToken.asDoctype().isForceQuirks());

        Token doctypeTokenWithName = new Token.Doctype();
        doctypeTokenWithName.asDoctype().getName().append("html");
        assertEquals("Doctype[html]", doctypeTokenWithName.tokenType());
        assertEquals("html", doctypeTokenWithName.asDoctype().getName());
        assertNull(doctypeTokenWithName.asDoctype().getPublicIdentifier());
        assertNull(doctypeTokenWithName.asDoctype().getSystemIdentifier());
        assertFalse(doctypeTokenWithName.asDoctype().isForceQuirks());

        Token doctypeTokenWithNamePublicIdentifier = new Token.Doctype();
        doctypeTokenWithNamePublicIdentifier.asDoctype().getName().append("html");
        doctypeTokenWithNamePublicIdentifier.asDoctype().getPublicIdentifier().append("HTML 4.01");
        assertEquals("Doctype[html]", doctypeTokenWithName.tokenType());
        assertEquals("html", doctypeTokenWithName.asDoctype().getName());
        assertEquals("HTML 4.01", doctypeTokenWithNamePublicIdentifier.asDoctype().getPublicIdentifier());
        assertNull(doctypeTokenWithNamePublicIdentifier.asDoctype().getSystemIdentifier());
        assertFalse(doctypeTokenWithNamePublicIdentifier.asDoctype().isForceQuirks());

        Token doctypeTokenWithNameSystemIdentifier = new Token.Doctype();
        doctypeTokenWithNameSystemIdentifier.asDoctype().getName().append("html");
        doctypeTokenWithNameSystemIdentifier.asDoctype().getSystemIdentifier().append("about:blank");
        assertEquals("Doctype[html]", doctypeTokenWithName.tokenType());
        assertEquals("html", doctypeTokenWithName.asDoctype().getName());
        assertNull(doctypeTokenWithName.asDoctype().getPublicIdentifier());
        assertEquals("about:blank", doctypeTokenWithNameSystemIdentifier.asDoctype().getSystemIdentifier());
        assertFalse(doctypeTokenWithNameSystemIdentifier.asDoctype().isForceQuirks());

        Token doctypeTokenWithForceQuirks = new Token.Doctype();
        doctypeTokenWithForceQuirks.asDoctype().getName().append("html");
        doctypeTokenWithForceQuirks.asDoctype().setForceQuirks(true);
        assertEquals("Doctype[html]", doctypeTokenWithName.tokenType());
        assertEquals("html", doctypeTokenWithName.asDoctype().getName());
        assertNull(doctypeTokenWithName.asDoctype().getPublicIdentifier());
        assertNull(doctypeTokenWithName.asDoctype().getSystemIdentifier());
        assertTrue(doctypeTokenWithForceQuirks.asDoctype().isForceQuirks());
    }

    @Test
    public void testStartTagConstructor() {
        Token startTagToken = new Token.StartTag();
        assertEquals("StartTag", startTagToken.tokenType());
        assertNull(startTagToken.asStartTag().name());
        assertFalse(startTagToken.asStartTag().isSelfClosing());
        assertNull(startTagToken.asStartTag().getAttributes());

        Token startTagTokenWithName = new Token.StartTag("div");
        assertEquals("StartTag[div]", startTagTokenWithName.tokenType());
        assertEquals("div", startTagTokenWithName.asStartTag().name());
        assertFalse(startTagTokenWithName.asStartTag().isSelfClosing());
        assertNull(startTagTokenWithName.asStartTag().getAttributes());

        Token startTagTokenWithAttributes = new Token.StartTag("div");
        startTagTokenWithAttributes.asStartTag().newAttribute();
        startTagTokenWithAttributes.asStartTag().appendAttributeName("class").appendAttributeValue("someClass");
        assertEquals("StartTag[div class='someClass']", startTagTokenWithAttributes.tokenType());
        assertEquals("div", startTagTokenWithAttributes.asStartTag().name());
        assertFalse(startTagTokenWithAttributes.asStartTag().isSelfClosing());
        assertEquals("class='someClass'", startTagTokenWithAttributes.asStartTag().getAttributes().toString());

        Token startTagTokenWithSelfClosing = new Token.StartTag("img");
        startTagTokenWithSelfClosing.asStartTag().selfClosing = true;
        assertEquals("StartTag[img]", startTagTokenWithSelfClosing.tokenType());
        assertEquals("img", startTagTokenWithSelfClosing.asStartTag().name());
        assertTrue(startTagTokenWithSelfClosing.asStartTag().isSelfClosing());
        assertNull(startTagTokenWithSelfClosing.asStartTag().getAttributes());
    }

    @Test
    public void testEndTagConstructor() {
        Token endTagToken = new Token.EndTag();
        assertEquals("EndTag", endTagToken.tokenType());
        assertNull(endTagToken.asEndTag().name());

        Token endTagTokenWithName = new Token.EndTag("div");
        assertEquals("EndTag[div]", endTagTokenWithName.tokenType());
        assertEquals("div", endTagTokenWithName.asEndTag().name());
    }

    @Test
    public void testCommentConstructor() {
        Token commentToken = new Token.Comment();
        assertEquals("Comment", commentToken.tokenType());
        assertNull(commentToken.asComment().getData());

        Token commentTokenWithData = new Token.Comment();
        commentTokenWithData.asComment().getData().append("This is a comment");
        assertEquals("Comment", commentTokenWithData.tokenType());
        assertEquals("This is a comment", commentTokenWithData.asComment().getData());
    }

    @Test
    public void testCharacterConstructor() {
        Token characterToken = new Token.Character('A');
        assertEquals("Character['A']", characterToken.tokenType());
        assertEquals("A", characterToken.asCharacter().getData());

        Token characterTokenWithMultipleCharacters = new Token.Character("AB");
        assertEquals("Character[AB]", characterTokenWithMultipleCharacters.tokenType());
        assertEquals("AB", characterTokenWithMultipleCharacters.asCharacter().getData());
    }

    @Test
    public void testEOFConstructor() {
        Token eofToken = new Token.EOF();
        assertEquals("EOF", eofToken.tokenType());

        Tokenizer tokenizer = new Tokenizer();
        Token nextToken = tokenizer.nextToken();
        Token previousToken = new Token.EOF();

        while (nextToken != null) {
            previousToken = nextToken;
            nextToken = tokenizer.nextToken();
        }

        assertEquals("EOF", previousToken.tokenType());
    }
}