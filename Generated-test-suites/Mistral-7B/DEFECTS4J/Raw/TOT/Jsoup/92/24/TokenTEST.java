import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        Token resetDoctypeToken = doctypeToken.reset();
        assertEquals("Doctype", resetDoctypeToken.tokenType());

        doctypeToken.name("html");
        assertEquals("html", doctypeToken.getName());

        doctypeToken.pubSysKey("_pub");
        assertEquals("_pub", doctypeToken.getPubSysKey());

        doctypeToken.publicIdentifier("DTD XHTML 1.0");
        assertEquals("DTD XHTML 1.0", doctypeToken.getPublicIdentifier());

        doctypeToken.systemIdentifier("/myfile.xhtml");
        assertEquals("/myfile.xhtml", doctypeToken.getSystemIdentifier());

        doctypeToken.forceQuirks(true);
        assertTrue(doctypeToken.isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();
        Token resetStartTagToken = startTagToken.reset();
        assertEquals("StartTag", resetStartTagToken.tokenType().name());

        startTagToken.name("p").selfClosing(true);
        assertEquals("p", startTagToken.name());
        assertTrue(startTagToken.isSelfClosing());

        startTagToken.newAttribute().name("class").value("intro");
        assertEquals("class", startTagToken.pendingAttributeName);
        assertEquals("intro", startTagToken.getAttributes().getValue("class"));

        startTagToken.finaliseTag();
        assertNull(startTagToken.pendingAttributeName);
        assertNull(startTagToken.pendingAttributeValue);
        assertNull(startTagToken.pendingAttributeValueS);
        assertFalse(startTagToken.hasEmptyAttributeValue);
        assertFalse(startTagToken.hasPendingAttributeValue);
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();
        Token resetEndTagToken = endTagToken.reset();
        assertEquals("EndTag", resetEndTagToken.tokenType().name());

        endTagToken.name("p");
        assertEquals("p", endTagToken.name());

        endTagToken.finaliseTag();
        assertTrue(endTagToken.isSelfClosing());

        String html = "<html><body><p class=\"intro\"></p></body></html>";
        TokenParser parser = new TokenParser(new StringReader(html));
        Token[] tokens = parser.parse();

        Token startTagToken = null;
        Token endTagToken1 = null;
        Token endTagToken2 = null;
        for (Token token : tokens) {
            if (token.isStartTag() && token.name().equals("p")) {
                startTagToken = token;
            }
            if (token.isEndTag() && token.name().equals("p")) {
                endTagToken1 = token;
            }
            if (token.isEndTag() && token.name().equals("body")) {
                endTagToken2 = token;
            }
        }
        assertEquals(startTagToken, endTagToken1.asStartTag().getPrevious());
        assertEquals(endTagToken1, endTagToken2.asEndTag().getNext());
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        Token resetCommentToken = commentToken.reset();
        assertEquals("Comment", resetCommentToken.tokenType().name());

        commentToken.data("This is a test comment /* and this is another */");
        assertEquals("This is a test comment /* and this is another */", commentToken.getData());
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        Token resetCharacterToken = characterToken.reset();
        assertEquals("Character", resetCharacterToken.tokenType().name());

        characterToken.data("&amp;");
        assertEquals("&amp;", characterToken.getData());

        characterToken.data("&apos;");
        assertEquals("&apos;", characterToken.getData());

        characterToken.data("&quot;");
        assertEquals("&quot;", characterToken.getData());

        characterToken.data("&gt;");
        assertEquals("&gt;", characterToken.getData());

        characterToken.data("&lt;");
        assertEquals("&lt;", characterToken.getData());
    }

    @Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();
        Token resetEofToken = eofToken.reset();
        assertEquals("EOF", resetEofToken.tokenType().name());

        assertFalse(eofToken.isDoctype());
        assertFalse(eofToken.isStartTag());
        assertFalse(eofToken.isEndTag());
        assertFalse(eofToken.isComment());
        assertFalse(eofToken.isCharacter());
        assertTrue(eofToken.isEOF());
    }
}