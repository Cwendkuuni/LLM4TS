import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Character("test");
        assert token.tokenType().equals("Character");
    }

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("Test");
        assert doctype.getName().equals("Test");
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag("test");
        assert startTag.toString().equals("<test>");
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag("test");
        assert endTag.toString().equals("</test>");
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("Test");
        assert comment.getData().equals("Test");
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("test");
        assert character.getData().equals("test");
    }

    @Test
    public void testEOFToken() {
        Token.EOF eof = new Token.EOF();
        assert eof.type == Token.TokenType.EOF;
    }

}