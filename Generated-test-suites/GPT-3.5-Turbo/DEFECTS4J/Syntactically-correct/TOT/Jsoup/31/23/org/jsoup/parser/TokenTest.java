import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Attributes;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token.Doctype doctype = new Token.Doctype();
        Assert.assertEquals("Doctype", doctype.tokenType());
        
        Token.StartTag startTag = new Token.StartTag();
        Assert.assertEquals("StartTag", startTag.tokenType());
        
        Token.EndTag endTag = new Token.EndTag();
        Assert.assertEquals("EndTag", endTag.tokenType());

        Token.Comment comment = new Token.Comment();
        Assert.assertEquals("Comment", comment.tokenType());
        
        Token.Character character = new Token.Character("test");
        Assert.assertEquals("Character", character.tokenType());

        Token.EOF eof = new Token.EOF();
        Assert.assertEquals("EOF", eof.tokenType());
    }

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("HTML");

        Assert.assertEquals("HTML", doctype.getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("public");

        Assert.assertEquals("public", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("system");

        Assert.assertEquals("system", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;

        Assert.assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagConstructorWithName() {
        Token.StartTag startTag = new Token.StartTag("div");

        Assert.assertEquals("div", startTag.name());
        Assert.assertFalse(startTag.isSelfClosing());
        Assert.assertNull(startTag.getAttributes());
    }

    @Test
    public void testStartTagConstructorWithNameAndAttributes() {
        Attributes attributes = new Attributes();
        Token.StartTag startTag = new Token.StartTag("div", attributes);

        Assert.assertEquals("div", startTag.name());
        Assert.assertFalse(startTag.isSelfClosing());
        Assert.assertEquals(attributes, startTag.getAttributes());
    }

    @Test
    public void testEndTagName() {
        Token.EndTag endTag = new Token.EndTag("div");

        Assert.assertEquals("div", endTag.name());
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");

        Assert.assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("test");

        Assert.assertEquals("test", character.getData());
    }

    @Test
    public void testIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();

        Assert.assertTrue(doctype.isDoctype());
        Assert.assertFalse(doctype.isStartTag());
        Assert.assertFalse(doctype.isEndTag());
        Assert.assertFalse(doctype.isComment());
        Assert.assertFalse(doctype.isCharacter());
        Assert.assertFalse(doctype.isEOF());
    }

    @Test
    public void testIsStartTag() {
        Token.StartTag startTag = new Token.StartTag();

        Assert.assertFalse(startTag.isDoctype());
        Assert.assertTrue(startTag.isStartTag());
        Assert.assertFalse(startTag.isEndTag());
        Assert.assertFalse(startTag.isComment());
        Assert.assertFalse(startTag.isCharacter());
        Assert.assertFalse(startTag.isEOF());
    }

    @Test
    public void testIsEndTag() {
        Token.EndTag endTag = new Token.EndTag();

        Assert.assertFalse(endTag.isDoctype());
        Assert.assertFalse(endTag.isStartTag());
        Assert.assertTrue(endTag.isEndTag());
        Assert.assertFalse(endTag.isComment());
        Assert.assertFalse(endTag.isCharacter());
        Assert.assertFalse(endTag.isEOF());
    }

    @Test
    public void testIsComment() {
        Token.Comment comment = new Token.Comment();

        Assert.assertFalse(comment.isDoctype());
        Assert.assertFalse(comment.isStartTag());
        Assert.assertFalse(comment.isEndTag());
        Assert.assertTrue(comment.isComment());
        Assert.assertFalse(comment.isCharacter());
        Assert.assertFalse(comment.isEOF());
    }

    @Test
    public void testIsCharacter() {
        Token.Character character = new Token.Character("test");

        Assert.assertFalse(character.isDoctype());
        Assert.assertFalse(character.isStartTag());
        Assert.assertFalse(character.isEndTag());
        Assert.assertFalse(character.isComment());
        Assert.assertTrue(character.isCharacter());
        Assert.assertFalse(character.isEOF());
    }

    @Test
    public void testIsEOF() {
        Token.EOF eof = new Token.EOF();

        Assert.assertFalse(eof.isDoctype());
        Assert.assertFalse(eof.isStartTag());
        Assert.assertFalse(eof.isEndTag());
        Assert.assertFalse(eof.isComment());
        Assert.assertFalse(eof.isCharacter());
        Assert.assertTrue(eof.isEOF());
    }
}