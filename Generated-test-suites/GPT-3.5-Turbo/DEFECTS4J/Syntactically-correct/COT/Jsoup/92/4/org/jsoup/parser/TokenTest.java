import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new TokenImpl();
        TokenType tokenType = token.type;
        assert(tokenType != null);
    }

    @Test
    public void testReset() {
        Token token = new TokenImpl();
        Token resetToken = token.reset();
        assert(resetToken != null);
    }

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        String name = doctype.getName();
        assert(name != null);
    }

    @Test
    public void testDoctypeGetPubSysKey() {
        Token.Doctype doctype = new Token.Doctype();
        String pubSysKey = doctype.getPubSysKey();
        assert(pubSysKey != null);
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        String publicIdentifier = doctype.getPublicIdentifier();
        assert(publicIdentifier != null);
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        String systemIdentifier = doctype.getSystemIdentifier();
        assert(systemIdentifier != null);
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        boolean forceQuirks = doctype.isForceQuirks();
        assert(!forceQuirks);
    }

    @Test
    public void testStartTagName() {
        Token.StartTag startTag = new Token.StartTag();
        String tagName = startTag.tagName;
        assert(tagName == null);
    }

    @Test
    public void testStartTagNormalName() {
        Token.StartTag startTag = new Token.StartTag();
        String normalName = startTag.normalName;
        assert(normalName == null);
    }

    @Test
    public void testStartTagSelfClosing() {
        Token.StartTag startTag = new Token.StartTag();
        boolean selfClosing = startTag.selfClosing;
        assert(!selfClosing);
    }

    @Test
    public void testStartTagAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        Attributes attributes = startTag.attributes;
        assert(attributes != null);
    }

    @Test
    public void testEndTagName() {
        Token.EndTag endTag = new Token.EndTag();
        String tagName = endTag.tagName;
        assert(tagName == null);
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        String data = comment.getData();
        assert(data != null);
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character();
        String data = character.getData();
        assert(data == null);
    }

    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();
        assert(eof != null);
    }

    private class TokenImpl extends Token {
        @Override
        Token reset() {
            return null;
        }
    }
}