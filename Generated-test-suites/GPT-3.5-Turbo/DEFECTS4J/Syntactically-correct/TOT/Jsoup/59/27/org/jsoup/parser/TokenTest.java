import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token.Doctype doctype = new Token.Doctype();
        Token.StartTag startTag = new Token.StartTag();
        Token.EndTag endTag = new Token.EndTag();
        Token.Comment comment = new Token.Comment();
        Token.Character character = new Token.Character();
        Token.EOF eof = new Token.EOF();

        assertEquals(Token.TokenType.Doctype, doctype.type);
        assertEquals(Token.TokenType.StartTag, startTag.type);
        assertEquals(Token.TokenType.EndTag, endTag.type);
        assertEquals(Token.TokenType.Comment, comment.type);
        assertEquals(Token.TokenType.Character, character.type);
        assertEquals(Token.TokenType.EOF, eof.type);
    }

    @Test
    public void testTokenTypeCast() {
        Token token = new Token.StartTag();
        assertTrue(token.isStartTag());
        assertNotNull(token.asStartTag());

        token = new Token.Doctype();
        assertTrue(token.isDoctype());
        assertNotNull(token.asDoctype());

        token = new Token.EndTag();
        assertTrue(token.isEndTag());
        assertNotNull(token.asEndTag());

        token = new Token.Comment();
        assertTrue(token.isComment());
        assertNotNull(token.asComment());

        token = new Token.Character();
        assertTrue(token.isCharacter());
        assertNotNull(token.asCharacter());

        token = new Token.EOF();
        assertTrue(token.isEOF());
        assertNotNull(token.asEOF());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("HTML");
        doctype.pubSysKey = "PUBLIC";
        doctype.publicIdentifier.append("//W3C//DTD HTML 4.01 Transitional//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/loose.dtd");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.name.toString());
        assertNull(doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testDoctypeGetters() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("HTML");
        doctype.pubSysKey = "PUBLIC";
        doctype.publicIdentifier.append("//W3C//DTD HTML 4.01 Transitional//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/loose.dtd");
        doctype.forceQuirks = true;

        assertEquals("HTML", doctype.getName());
        assertEquals("PUBLIC", doctype.getPubSysKey());
        assertEquals("//W3C//DTD HTML 4.01 Transitional//EN", doctype.getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/html4/loose.dtd", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "class";
        startTag.pendingAttributeValue.append("container");
        startTag.selfClosing = true;
        startTag.hasEmptyAttributeValue = true;

        startTag.reset();

        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);
    }

    @Test
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        endTag.normalName = "div";
        endTag.pendingAttributeName = "class";
        endTag.pendingAttributeValue.append("container");
        endTag.selfClosing = true;
        endTag.hasEmptyAttributeValue = true;

        endTag.reset();

        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
        assertNull(endTag.pendingAttributeName);
        assertEquals("", endTag.pendingAttributeValue.toString());
        assertFalse(endTag.selfClosing);
        assertNull(endTag.attributes);
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        comment.bogus = true;

        comment.reset();

        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "This is a character";

        character.reset();

        assertNull(character.data);
    }

    @Test
    public void testDoctypeToString() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("HTML");
        doctype.pubSysKey = "PUBLIC";
        doctype.publicIdentifier.append("//W3C//DTD HTML 4.01 Transitional//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/loose.dtd");
        doctype.forceQuirks = true;

        assertEquals("<!DOCTYPE HTML PUBLIC \"//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">",
                doctype.toString());
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.attributes = new Attributes();
        startTag.attributes.put("class", "container");
        startTag.attributes.put("id", "main");

        assertEquals("<div class=\"container\" id=\"main\">", startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        endTag.normalName = "div";

        assertEquals("</div>", endTag.toString());
    }

    @Test
    public void testCommentToString() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");

        assertEquals("<!--This is a comment-->", comment.toString());
    }

    @Test
    public void testCharacterToString() {
        Token.Character character = new Token.Character();
        character.data = "This is a character";

        assertEquals("This is a character", character.toString());
    }
    
    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        eof.reset();

        assertNull(eof.reset());
    }

    @Test
    public void testEOFInstance() {
        Token.EOF eof = new Token.EOF();

        assertTrue(eof.isEOF());
        assertNotNull(eof.asEOF());
    }

}