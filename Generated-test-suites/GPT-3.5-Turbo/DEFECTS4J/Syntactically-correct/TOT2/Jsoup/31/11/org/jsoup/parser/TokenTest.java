import org.junit.Test;
import org.junit.Assert;

public class TokenTest {

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("Test Doctype");
        Assert.assertEquals("Test Doctype", doctype.getName());
    }

    @Test
    public void testTagIsSelfClosing() {
        Token.Tag tag = new Token.StartTag();
        tag.selfClosing = true;
        Assert.assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testTagAppendAttributeValue() {
        Token.Tag tag = new Token.StartTag();
        tag.appendAttributeValue("test_value");
        Assert.assertEquals("test_value", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("Test Comment");
        Assert.assertEquals("Test Comment", comment.getData());
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("Test Character");
        Assert.assertEquals("Test Character", character.getData());
    }

    @Test
    public void testEOFToken() {
        Token.EOF eof = new Token.EOF();
        Assert.assertNotNull(eof);
    }

    @Test
    public void testTokenIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        Assert.assertTrue(doctype.isDoctype());
    }

    @Test
    public void testTokenIsStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        Assert.assertTrue(startTag.isStartTag());
    }

    @Test
    public void testTokenIsEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        Assert.assertTrue(endTag.isEndTag());
    }

    @Test
    public void testTokenIsComment() {
        Token.Comment comment = new Token.Comment();
        Assert.assertTrue(comment.isComment());
    }

    @Test
    public void testTokenIsCharacter() {
        Token.Character character = new Token.Character("Test Character");
        Assert.assertTrue(character.isCharacter());
    }

    @Test
    public void testTokenIsEOF() {
        Token.EOF eof = new Token.EOF();
        Assert.assertTrue(eof.isEOF());
    }
}