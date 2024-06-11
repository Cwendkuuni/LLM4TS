import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.Tag;
import org.jsoup.parser.Token.Tag.StartTag;
import org.jsoup.parser.Token.Tag.EndTag;
import org.jsoup.parser.Token.Tag.Comment;
import org.jsoup.parser.Token.Tag.Character;
import org.jsoup.parser.Token.Tag.Character.CharacterData;

public class TokenTest {

    @Test
    public void testTokenTypeDoctype() {
        Doctype doctype = new Doctype();
        assertEquals(TokenType.Doctype, doctype.type);
    }

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        doctype.name.append("html");
        doctype.publicIdentifier.append("public id");
        doctype.systemIdentifier.append("system id");
        doctype.forceQuirks = true;
        Doctype resetDoctype = doctype.reset();
        assertEquals(TokenType.Doctype, resetDoctype.type);
        assertEquals("", doctype.name.toString());
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Tag tag = new StartTag();
        assertEquals(TokenType.StartTag, tag.type);
        tag.tagName = "div";
        tag.normalName = "div";
        tag.selfClosing = false;
        tag.reset();
        assertEquals(TokenType.StartTag, tag.type);
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertFalse(tag.selfClosing);
    }

    @Test
    public void testTagNewAttribute() {
        StartTag tag = new StartTag();
        tag.newAttribute();
        Attribute attribute = tag.attributes.get("class");
        assertNull(attribute);
    }

    @Test
    public void testTagFinaliseTag() {
        StartTag tag = new StartTag();
        tag.name("div");
        tag.newAttribute().name("class").value("test");
        tag.finaliseTag();
        Attribute attribute = tag.attributes.get("class");
        assertEquals("test", attribute.getValue());
    }

    @Test
    public void testTagName() {
        StartTag tag = new StartTag();
        tag.name("div");
        assertEquals("div", tag.name());
        tag.name("span");
        assertEquals("span", tag.name());
    }

    @Test
    public void testTagNormalName() {
        StartTag tag = new StartTag();
        tag.name("DIV");
        tag.normalName("div");
        assertEquals("div", tag.normalName());
        tag.name("SPAN");
        tag.normalName("span");
        assertEquals("span", tag.normalName());
    }

    @Test
    public void testTagIsSelfClosing() {
        StartTag tag = new StartTag();
        tag.selfClosing = true;
        assertTrue(tag.isSelfClosing());
        tag.selfClosing = false;
        assertFalse(tag.isSelfClosing());
    }

    @Test
    public void testTagGetAttributes() {
        StartTag tag = new StartTag();
        tag.name("div");
        tag.newAttribute().name("class").value("test");
        Attributes attributes = tag.getAttributes();
        assertEquals(1, attributes.size());
        Attribute attribute = attributes.get("class");
        assertEquals("test", attribute.getValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testTagNewAttributeAfterFinaliseTag() {
        StartTag tag = new StartTag();
        tag.name("div");
        tag.finaliseTag();
        tag.newAttribute();
    }

    @Test
    public void testCommentReset() {
        Comment comment = new Comment();
        comment.data.append("This is a comment");
        Comment resetComment = comment.reset();
        assertEquals(TokenType.Comment, resetComment.type);
        assertEquals("", comment.data.toString());
    }

    @Test
    public void testCommentToString() {
        Comment comment = new Comment();
        comment.data.append("<!--This is a comment-->");
        assertEquals("<!--This is a comment-->", comment.toString());
    }

    @Test
    public void testCharacterDataReset() {
        Character character = new Character();
        character.data = "This is a character data";
        CharacterData resetCharacter = character.reset();
        assertEquals(TokenType.Character, resetCharacter.type);
        assertNull(character.data);
    }

    @Test
    public void testCharacterDataToString() {
        Character character = new Character();
        character.data("This is a character data");
        assertEquals("This is a character data", character.getData());
    }

}