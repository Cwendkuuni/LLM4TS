import org.jsoup.nodes.Attributes;
import org.junit.Test;
import org.junit.Assert;

public class TokenTest {

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("test");
        Assert.assertEquals("test", doctype.getName());
    }

    @Test
    public void testStartTagToStringWithAttributes() {
        Token.StartTag startTag = new Token.StartTag("div");
        startTag.appendAttributeName("id");
        startTag.appendAttributeValue("testId");
        Assert.assertEquals("<div id=\"testId\">", startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag("div");
        Assert.assertEquals("</div>", endTag.toString());
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("Test comment data");
        Assert.assertEquals("Test comment data", comment.getData());
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("Test character data");
        Assert.assertEquals("Test character data", character.getData());
    }

    @Test
    public void testEOFToken() {
        Token.EOF eof = new Token.EOF();
        Assert.assertEquals(Token.TokenType.EOF, eof.type);
    }

}