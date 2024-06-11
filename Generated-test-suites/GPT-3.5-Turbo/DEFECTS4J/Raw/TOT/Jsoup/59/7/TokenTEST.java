
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testResetMethodInDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "html";
        doctype.publicIdentifier.append("//W3C//DTD HTML 4.01//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.name.toString());
        assertNull(doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testGetterMethodsInDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "html";
        doctype.publicIdentifier.append("//W3C//DTD HTML 4.01//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        doctype.forceQuirks = true;

        assertEquals("html", doctype.getName());
        assertEquals("html", doctype.getPubSysKey());
        assertEquals("//W3C//DTD HTML 4.01//EN", doctype.getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testResetMethodInTag() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "div";
        tag.normalName = "div";
        tag.pendingAttributeName = "id";
        tag.pendingAttributeValue.append("div1");
        tag.pendingAttributeValueS = null;
        tag.hasEmptyAttributeValue = false;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;
        tag.attributes = new org.jsoup.nodes.Attributes();

        tag.reset();

        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue.toString());
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.selfClosing);
        assertNull(tag.attributes);
    }

    @Test
    public void testMethodsInTag() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "div";
        tag.normalName = "div";
        tag.pendingAttributeName = "id";
        tag.pendingAttributeValue.append("div1");
        tag.pendingAttributeValueS = null;
        tag.hasEmptyAttributeValue = false;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;
        tag.attributes = new org.jsoup.nodes.Attributes();

        assertEquals("div", tag.name());
        assertEquals("div", tag.normalName());
        assertTrue(tag.isSelfClosing());
        assertNull(tag.getAttributes());

        tag.appendTagName('X');
        assertEquals("divX", tag.tagName);
        assertEquals("divx", tag.normalName);

        tag.appendAttributeName('Y');
        assertEquals("idY", tag.pendingAttributeName);

        tag.appendAttributeValue("value");
        assertEquals("div1value", tag.pendingAttributeValue.toString());

        tag.setEmptyAttributeValue();
        assertTrue(tag.hasEmptyAttributeValue);
    }

    @Test
    public void testResetMethodInStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "id";
        startTag.pendingAttributeValue.append("div1");
        startTag.pendingAttributeValueS = null;
        startTag.hasEmptyAttributeValue = false;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new org.jsoup.nodes.Attributes();

        startTag.reset();

        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);
    }

    @Test
    public void testResetMethodInEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        endTag.normalName = "div";
        endTag.pendingAttributeName = "id";
        endTag.pendingAttributeValue.append("div1");
        endTag.pendingAttributeValueS = null;
        endTag.hasEmptyAttributeValue = false;
        endTag.hasPendingAttributeValue = true;
        endTag.selfClosing = true;
        endTag.attributes = new org.jsoup.nodes.Attributes();

        endTag.reset();

        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
        assertNull(endTag.pendingAttributeName);
        assertEquals("", endTag.pendingAttributeValue.toString());
        assertNull(endTag.pendingAttributeValueS);
        assertFalse(endTag.hasEmptyAttributeValue);
        assertFalse(endTag.hasPendingAttributeValue);
        assertFalse(endTag.selfClosing);
        assertNull(endTag.attributes);
    }

    @Test
    public void testResetMethodInComment() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        comment.bogus = true;

        comment.reset();

        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }

    @Test
    public void testGetterMethodInComment() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        comment.bogus = true;

        assertEquals("This is a comment", comment.getData());
        assertTrue(comment.bogus);
    }

    @Test
    public void testResetMethodInCharacter() {
        Token.Character character = new Token.Character();
        character.data = "Sample Character Data";

        character.reset();

        assertNull(character.data);
    }

    @Test
    public void testGetterMethodInCharacter() {
        Token.Character character = new Token.Character();
        character.data = "Sample Character Data";

        assertEquals("Sample Character Data", character.getData());
    }

    @Test
    public void testResetMethodInEOF() {
        Token.EOF eof = new Token.EOF();

        eof.reset();

        assertEquals(Token.TokenType.EOF, eof.type);
    }

    @Test
    public void testIsDoctypeMethod() {
        Token.Doctype doctype = new Token.Doctype();
        Token.Tag tag = new Token.StartTag();
        Token.Comment comment = new Token.Comment();
        Token.Character character = new Token.Character();
        Token.EOF eof = new Token.EOF();

        assertTrue(doctype.isDoctype());
        assertFalse(tag.isDoctype());
        assertFalse(comment.isDoctype());
        assertFalse(character.isDoctype());
        assertFalse(eof.isDoctype());
    }

    @Test
    public void testIsStartTagMethod() {
        Token.Doctype doctype = new Token.Doctype();
        Token.Tag tag = new Token.StartTag();
        Token.Comment comment = new Token.Comment();
        Token.Character character = new Token.Character();
        Token.EOF eof = new Token.EOF();

        assertFalse(doctype.isStartTag());
        assertTrue(tag.isStartTag());
        assertFalse(comment.isStartTag());
        assertFalse(character.isStartTag());
        assertFalse(eof.isStartTag());
    }

    @Test
    public void testIsEndTagMethod() {
        Token.Doctype doctype = new Token.Doctype();
        Token.Tag tag = new Token.StartTag();
        Token.Comment comment = new Token.Comment();
        Token.Character character = new Token.Character();
        Token.EOF eof = new Token.EOF();

        assertFalse(doctype.isEndTag());
        assertFalse(tag.isEndTag());
        assertFalse(comment.isEndTag());
        assertFalse(character.isEndTag());
        assertFalse(eof.isEndTag());
    }

    @Test
    public void testIsCommentMethod() {
        Token.Doctype doctype = new Token.Doctype();
        Token.Tag tag = new Token.StartTag();
        Token.Comment comment = new Token.Comment();
        Token.Character character = new Token.Character();
        Token.EOF eof = new Token.EOF();

        assertFalse(doctype.isComment());
        assertFalse(tag.isComment());
        assertTrue(comment.isComment());
        assertFalse(character.isComment());
        assertFalse(eof.isComment());
    }

    @Test
    public void testIsCharacterMethod() {
        Token.Doctype doctype = new Token.Doctype();
        Token.Tag tag = new Token.StartTag();
        Token.Comment comment = new Token.Comment();
        Token.Character character = new Token.Character();
        Token.EOF eof = new Token.EOF();

        assertFalse(doctype.isCharacter());
        assertFalse(tag.isCharacter());
        assertFalse(comment.isCharacter());
        assertTrue(character.isCharacter());
        assertFalse(eof.isCharacter());
    }

    @Test
    public void testIsEOFMethod() {
        Token.Doctype doctype = new Token.Doctype();
        Token.Tag tag = new Token.StartTag();
        Token.Comment comment = new Token.Comment();
        Token.Character character = new Token.Character();
        Token.EOF eof = new Token.EOF();

        assertFalse(doctype.isEOF());
        assertFalse(tag.isEOF());
        assertFalse(comment.isEOF());
        assertFalse(character.isEOF());
        assertTrue(eof.isEOF());
    }

}

