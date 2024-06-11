import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TokenTest {

    private Token doctypeToken;
    private Token startTagToken;
    private Token endTagToken;
    private Token commentToken;
    private Token characterToken;
    private Token cdataToken;
    private Token eofToken;

    @Before
    public void setUp() {
        doctypeToken = new Token.Doctype();
        startTagToken = new Token.StartTag();
        endTagToken = new Token.EndTag();
        commentToken = new Token.Comment();
        characterToken = new Token.Character();
        cdataToken = new Token.CData("");
        eofToken = new Token.EOF();
    }

    @Test
    public void testDoctypeToken() {
        Doctype doctype = (Doctype) doctypeToken;
        doctype.getName("html");
        doctype.setForceQuirks(true);
        assertEquals("Doctype", doctypeToken.tokenType());
        assertEquals("html", doctype.getName());
        assertTrue(doctype.isDoctype());
        assertFalse(doctype.isStartTag());
        assertFalse(doctype.isEndTag());
        assertFalse(doctype.isComment());
        assertFalse(doctype.isCharacter());
        assertFalse(doctype.isCData());
        assertFalse(doctype.isEOF());
        assertEquals(TokenType.Doctype, doctype.type);

        doctype.reset();
        assertNull(doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        StartTag startTag = (StartTag) startTagToken;
        startTag.name("html");
        startTag.attributes(new Attributes().put("class", "app"));
        assertEquals("StartTag", startTagToken.tokenType());
        assertEquals("html", startTag.name());
        assertTrue(startTag.isSelfClosing());
        assertFalse(startTag.isDoctype());
        assertFalse(startTag.isEndTag());
        assertFalse(startTag.isComment());
        assertFalse(startTag.isCharacter());
        assertFalse(startTag.isCData());
        assertFalse(startTag.isEOF());
        assertEquals(TokenType.StartTag, startTag.type);
        assertNotNull(startTag.getAttributes());
        assertEquals("class", startTag.getAttributes().get("class"));
        assertEquals("app", startTag.getAttributes().get("class").getValue());

        startTag.reset();
        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertNull(startTag.pendingAttributeValue);
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);
    }

    @Test
    public void testEndTagToken() {
        EndTag endTag = (EndTag) endTagToken;
        endTag.name("html");
        assertEquals("EndTag", endTagToken.tokenType());
        assertEquals("html", endTag.name());
        assertFalse(endTag.isDoctype());
        assertFalse(endTag.isStartTag());
        assertFalse(endTag.isEndTag());
        assertFalse(endTag.isComment());
        assertFalse(endTag.isCharacter());
        assertFalse(endTag.isCData());
        assertFalse(endTag.isEOF());
        assertEquals(TokenType.EndTag, endTag.type);

        endTag.reset();
        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
    }

    @Test
    public void testCommentToken() {
        Comment comment = (Comment) commentToken;
        comment.data("This is a comment");
        assertEquals("Comment", commentToken.tokenType());
        assertTrue(comment.isComment());
        assertFalse(comment.isDoctype());
        assertFalse(comment.isStartTag());
        assertFalse(comment.isEndTag());
        assertFalse(comment.isCharacter());
        assertFalse(comment.isCData());
        assertFalse(comment.isEOF());
        assertEquals(TokenType.Comment, comment.type);
        assertEquals("This is a comment", comment.getData());

        comment.reset();
        assertNull(comment.data);
        assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterToken() {
        Character character = (Character) characterToken;
        character.data("&amp;");
        assertEquals("Character", characterToken.tokenType());
        assertTrue(character.isCharacter());
        assertFalse(character.isDoctype());
        assertFalse(character.isStartTag());
        assertFalse(character.isEndTag());
        assertFalse(character.isComment());
        assertFalse(character.isCData());
        assertFalse(character.isEOF());
        assertEquals(TokenType.Character, character.type);
        assertEquals("&amp;", character.getData());

        character.reset();
        assertNull(character.data);
    }

    @Test
    public void testCDataToken() {
        CData cdata = (CData) cdataToken;
        cdata.data("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
        assertEquals("CData", cdataToken.tokenType());
        assertTrue(cdata.isCharacter());
        assertFalse(cdata.isDoctype());
        assertFalse(cdata.isStartTag());
        assertFalse(cdata.isEndTag());
        assertFalse(cdata.isComment());
        assertFalse(cdata.isCData());
        assertFalse(cdata.isEOF());
        assertEquals(TokenType.CData, cdata.type);
        assertEquals("<![CDATA[<?xml version=\"1.0\" encoding=\"UTF-8\"?>]]>", cdata.toString());
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", cdata.getData());

        cdata.reset();
        assertNull(cdata.data);
    }

    @Test
    public void testEOFToken() {
        EOF eof = (EOF) eofToken;
        assertEquals("EOF", eofToken.tokenType());
        assertFalse(eof.isDoctype());
        assertFalse(eof.isStartTag());
        assertFalse(eof.isEndTag());
        assertFalse(eof.isComment());
        assertFalse(eof.isCharacter());
        assertFalse(eof.isCData());
        assertTrue(eof.isEOF());
        assertEquals(TokenType.EOF, eof.type);

        eof.reset();
    }
}