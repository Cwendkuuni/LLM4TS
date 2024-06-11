import org.junit.Test;

public class TokenTest {
    
    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assert(token.tokenType().equals("Doctype"));
    }
    
    @Test
    public void testReset() {
        Token token = new Token.Doctype();
        Token resetToken = token.reset();
        assert(resetToken instanceof Token.Doctype);
    }
    
    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        assert(doctype.getName().equals(""));
    }
    
    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        assert(doctype.getPublicIdentifier().equals(""));
    }
    
    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        assert(doctype.getSystemIdentifier().equals(""));
    }
    
    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        assert(!doctype.isForceQuirks());
    }
    
    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        Token.Tag resetTag = tag.reset();
        assert(resetTag instanceof Token.StartTag);
    }
    
    @Test
    public void testTagName() {
        Token.Tag tag = new Token.StartTag();
        tag.name("div");
        assert(tag.name().equals("div"));
        assert(tag.normalName().equals("div"));
    }
    
    @Test
    public void testTagIsSelfClosing() {
        Token.Tag tag = new Token.StartTag();
        assert(!tag.isSelfClosing());
    }
    
    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        Token.Comment resetComment = comment.reset();
        assert(resetComment instanceof Token.Comment);
    }
    
    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        assert(comment.getData().equals(""));
    }
    
    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        Token.Character resetCharacter = character.reset();
        assert(resetCharacter instanceof Token.Character);
    }
    
    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character();
        assert(character.getData() == null);
    }
    
    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        Token.EOF resetEOF = eof.reset();
        assert(resetEOF instanceof Token.EOF);
    }
}