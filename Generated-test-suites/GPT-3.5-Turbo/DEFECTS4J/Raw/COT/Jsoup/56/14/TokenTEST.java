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
        assert(token != resetToken);
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
        assert(tag != resetTag);
    }
    
    @Test
    public void testTagGetName() {
        Token.Tag tag = new Token.StartTag();
        tag.name("test");
        assert(tag.name().equals("test"));
    }
    
    @Test
    public void testTagNormalName() {
        Token.Tag tag = new Token.StartTag();
        tag.name("Test");
        assert(tag.normalName().equals("test"));
    }
    
    @Test
    public void testTagIsSelfClosing() {
        Token.Tag tag = new Token.StartTag();
        assert(!tag.isSelfClosing());
    }
    
    @Test
    public void testTagGetAttributes() {
        Token.Tag tag = new Token.StartTag();
        assert(tag.getAttributes() == null);
    }
    
    @Test
    public void testTagAppendTagName() {
        Token.Tag tag = new Token.StartTag();
        tag.appendTagName("test");
        assert(tag.name().equals("test"));
    }
    
    @Test
    public void testTagAppendAttributeName() {
        Token.Tag tag = new Token.StartTag();
        tag.appendAttributeName("test");
        assert(tag.pendingAttributeName.equals("test"));
    }
    
    @Test
    public void testTagAppendAttributeValue() {
        Token.Tag tag = new Token.StartTag();
        tag.appendAttributeValue("test");
        assert(tag.pendingAttributeValue.toString().equals("test"));
    }
    
    @Test
    public void testTagAppendAttributeValueChar() {
        Token.Tag tag = new Token.StartTag();
        tag.appendAttributeValue('t');
        assert(tag.pendingAttributeValue.toString().equals("t"));
    }
    
    @Test
    public void testTagAppendAttributeValueCharArray() {
        Token.Tag tag = new Token.StartTag();
        tag.appendAttributeValue(new char[]{'t','e','s','t'});
        assert(tag.pendingAttributeValue.toString().equals("test"));
    }
    
    @Test
    public void testTagAppendAttributeValueCodepoints() {
        Token.Tag tag = new Token.StartTag();
        tag.appendAttributeValue(new int[]{116, 101, 115, 116});
        assert(tag.pendingAttributeValue.toString().equals("test"));
    }
    
    @Test
    public void testTagSetEmptyAttributeValue() {
        Token.Tag tag = new Token.StartTag();
        tag.setEmptyAttributeValue();
        assert(tag.hasEmptyAttributeValue);
    }
    
    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        Token.StartTag resetStartTag = startTag.reset();
        assert(startTag != resetStartTag);
    }
    
    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag();
        String str = startTag.toString();
        assert(str.equals("<>"));
    }
    
    @Test
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        Token.EndTag resetEndTag = endTag.reset();
        assert(endTag != resetEndTag);
    }
    
    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        String str = endTag.toString();
        assert(str.equals("</>"));
    }
    
    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        Token.Comment resetComment = comment.reset();
        assert(comment != resetComment);
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
        assert(character != resetCharacter);
    }
    
    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character();
        character.data("test");
        assert(character.getData().equals("test"));
    }
    
    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();
        Token.EOF resetEof = eof.reset();
        assert(eof == resetEof);
    }
}