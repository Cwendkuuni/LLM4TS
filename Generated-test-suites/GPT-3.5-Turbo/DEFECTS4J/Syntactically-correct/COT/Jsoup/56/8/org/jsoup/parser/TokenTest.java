
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.EOF;
import org.jsoup.parser.Token.TokenType;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new StartTag();
        assertEquals(TokenType.StartTag, token.type);
        token = new EndTag();
        assertEquals(TokenType.EndTag, token.type);
        token = new Comment();
        assertEquals(TokenType.Comment, token.type);
        token = new Character();
        assertEquals(TokenType.Character, token.type);
        token = new EOF();
        assertEquals(TokenType.EOF, token.type);
    }

    @Test
    public void testTokenReset() {
        Token token = new StartTag();
        assertEquals(TokenType.StartTag, token.type);
        token.reset();
        assertNull(token.tagName);
        assertNull(token.normalName);
        assertNull(token.pendingAttributeName);
        assertEquals(0, token.pendingAttributeValue.length());
        assertNull(token.pendingAttributeValueS);
        assertFalse(token.hasEmptyAttributeValue);
        assertFalse(token.hasPendingAttributeValue);
        assertFalse(token.selfClosing);
        assertNull(token.attributes);

        token = new EndTag();
        assertEquals(TokenType.EndTag, token.type);
        token.reset();
        assertNull(token.tagName);
        assertNull(token.normalName);
        assertNull(token.pendingAttributeName);
        assertEquals(0, token.pendingAttributeValue.length());
        assertNull(token.pendingAttributeValueS);
        assertFalse(token.hasEmptyAttributeValue);
        assertFalse(token.hasPendingAttributeValue);
        assertFalse(token.selfClosing);
        assertNull(token.attributes);

        token = new Comment();
        assertEquals(TokenType.Comment, token.type);
        token.reset();
        assertEquals(0, token.data.length());
        assertFalse(token.bogus);

        token = new Character();
        assertEquals(TokenType.Character, token.type);
        token.reset();
        assertNull(token.data);

        token = new EOF();
        assertEquals(TokenType.EOF, token.type);
        token.reset();
        assertEquals(TokenType.EOF, token.type);
    }

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.reset();
        assertNull(doctype.name.toString());
        assertNull(doctype.publicIdentifier.toString());
        assertNull(doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testStartTagReset() {
        StartTag startTag = new StartTag();
        startTag.reset();
        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertEquals(0, startTag.pendingAttributeValue.length());
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertNull(startTag.attributes);
    }

    @Test
    public void testEndTagReset() {
        EndTag endTag = new EndTag();
        endTag.reset();
        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
        assertNull(endTag.pendingAttributeName);
        assertEquals(0, endTag.pendingAttributeValue.length());
        assertNull(endTag.pendingAttributeValueS);
        assertFalse(endTag.hasEmptyAttributeValue);
        assertFalse(endTag.hasPendingAttributeValue);
        assertNull(endTag.attributes);
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.reset();
        assertEquals(0, comment.data.length());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        Character character = new Character();
        character.reset();
        assertNull(character.data);
    }

    @Test
    public void testEOFReset() {
        EOF eof = new EOF();
        eof.reset();
        assertEquals(TokenType.EOF, eof.type);
    }

    @Test
    public void testTagName() {
        StartTag startTag = new StartTag();
        startTag.name("div");
        assertEquals("div", startTag.tagName);
        assertEquals("div", startTag.normalName);
    }

    @Test
    public void testAppendingTagName() {
        StartTag startTag = new StartTag();
        startTag.appendTagName("d");
        startTag.appendTagName("i");
        startTag.appendTagName("v");
        assertEquals("div", startTag.tagName);
        assertEquals("div", startTag.normalName);
    }

    @Test
    public void testAttributeName() {
        StartTag startTag = new StartTag();
        startTag.appendAttributeName("attr");
        assertEquals("attr", startTag.pendingAttributeName);
    }

    @Test
    public void testAppendingAttributeName() {
        StartTag startTag = new StartTag();
        startTag.appendAttributeName("a");
        startTag.appendAttributeName("t");
        startTag.appendAttributeName("t");
        startTag.appendAttributeName("r");
        assertEquals("attr", startTag.pendingAttributeName);
    }

    @Test
    public void testAttributeValue() {
        StartTag startTag = new StartTag();
        startTag.appendAttributeValue("value");
        assertEquals("value", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testAppendingAttributeValue() {
        StartTag startTag = new StartTag();
        startTag.appendAttributeValue("va");
        startTag.appendAttributeValue("lue");
        assertEquals("value", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testAppendingAttributeValueChar() {
        StartTag startTag = new StartTag();
        startTag.appendAttributeValue('v');
        startTag.appendAttributeValue('a');
        startTag.appendAttributeValue('l');
        startTag.appendAttributeValue('u');
        startTag.appendAttributeValue('e');
        assertEquals("value", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testAppendingAttributeValueCharArray() {
        StartTag startTag = new StartTag();
        startTag.appendAttributeValue(new char[] {'v', 'a', 'l', 'u', 'e'});
        assertEquals("value", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testAppendingAttributeValueCodepoints() {
        StartTag startTag = new StartTag();
        startTag.appendAttributeValue(new int[] {118, 97, 108, 117, 101});
        assertEquals("value", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testEmptyAttributeValue() {
        StartTag startTag = new StartTag();
        startTag.setEmptyAttributeValue();
        assertTrue(startTag.hasEmptyAttributeValue);
    }
}