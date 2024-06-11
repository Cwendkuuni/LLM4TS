import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.helper.Validate;

public class TokenTest {

    @Test
    public void testTokenResetDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.getName().equals(""); // initial value is empty
        doctype.reset();
        doctype.getName().equals(""); // reset() resets name to empty

        doctype.getName("doctypeName");
        doctype.reset();
        Assert.assertEquals("", doctype.getName());
    }

    @Test
    public void testTokenResetTag() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "tagName";
        tag.reset();
        Assert.assertNull(tag.tagName);

        tag.tagName = "tagName";
        tag.newAttribute();
        tag.reset();
        Assert.assertNull(tag.tagName);
        Assert.assertNull(tag.attributes);
    }

    @Test
    public void testTokenResetComment() {
        Token.Comment comment = new Token.Comment();
        comment.getData().equals(""); // initial value is empty
        comment.reset();
        comment.getData().equals(""); // reset() resets data to empty

        comment.getData("commentData");
        comment.reset();
        Assert.assertEquals("", comment.getData());
    }

    @Test
    public void testTokenResetCharacter() {
        Token.Character character = new Token.Character();
        character.getData().equals(""); // initial value is empty
        character.reset();
        character.getData().equals(""); // reset() resets data to empty

        character.getData("characterData");
        character.reset();
        Assert.assertEquals("", character.getData());
    }

    @Test(expected = AssertionError.class)
    public void testTokenResetNull() {
        Token token = null;
        token.reset();
    }

    @Test
    public void testTokenIsDoctype() {
        Token token = new Token.Doctype();
        assertTrue(token.isDoctype());

        Token tag = new Token.StartTag();
        assertFalse(tag.isDoctype());
    }

    @Test
    public void testTokenIsStartTag() {
        Token token = new Token.StartTag();
        assertTrue(token.isStartTag());

        Token doctype = new Token.Doctype();
        assertFalse(doctype.isStartTag());
    }

    @Test
    public void testTokenIsEndTag() {
        Token token = new Token.EndTag();
        assertTrue(token.isEndTag());

        Token doctype = new Token.Doctype();
        assertFalse(doctype.isEndTag());
    }

    @Test
    public void testTokenIsComment() {
        Token token = new Token.Comment();
        assertTrue(token.isComment());

        Token doctype = new Token.Doctype();
        assertFalse(doctype.isComment());
    }

    @Test
    public void testTokenIsCharacter() {
        Token token = new Token.Character();
        assertTrue(token.isCharacter());

        Token doctype = new Token.Doctype();
        assertFalse(doctype.isCharacter());
    }

    @Test
    public void testTokenIsEOF() {
        Token token = new Token.EOF();
        assertTrue(token.isEOF());

        Token doctype = new Token.Doctype();
        assertFalse(doctype.isEOF());
    }

    @Test
    public void testTokenDoctypeToString() {
        Token.Doctype doctype = new Token.Doctype();
        String expected = "<!DOCTYPE>";
        doctype.getName("doctypeName");
        doctype.reset();
        String actual = doctype.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testTokenStartTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        String expected = "<tagName>";
        startTag.name("tagName");
        String actual = startTag.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testTokenEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        String expected = "</tagName>";
        endTag.name("tagName");
        String actual = endTag.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testTokenCommentToString() {
        Token.Comment comment = new Token.Comment();
        String expected = "<!--commentData-->";
        comment.getData("commentData");
        String actual = comment.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testTokenCharacterToString() {
        Token.Character character = new Token.Character();
        String expected = "characterData";
        character.data("characterData");
        String actual = character.toString();
        assertEquals(expected, actual);
    }

    @Test
    public void testTokenEOFToString() {
        Token.EOF eof = new Token.EOF();
        String expected = "";
        String actual = eof.toString();
        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testTokenNewAttributeNullName() {
        Token.StartTag tag = new Token.StartTag();
        tag.newAttribute(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testTokenNewAttributeNullValue() {
        Token.StartTag tag = new Token.StartTag();
        tag.newAttribute();
        tag.hasPendingAttributeValue = true;
        tag.newAttribute();
    }

    @Test
    public void testTokenNewAttribute() {
        Token.StartTag tag = new Token.StartTag();
        tag.newAttribute();
        tag.newAttribute("attributeName");
        tag.newAttribute("attributeValue");
        tag.attributes.containsKey("attributeName");
        tag.attributes.get("attributeName").getValue().equals("attributeValue");
    }

    @Test
    public void testTokenNewAttributeWithEmptyValue() {
        Token.StartTag tag = new Token.StartTag();
        tag.newAttribute();
        tag.setEmptyAttributeValue();
        tag.attributes.size() == 1;
        tag.attributes.get("") != null;
        tag.attributes.get("").getValue().equals("");
    }
}