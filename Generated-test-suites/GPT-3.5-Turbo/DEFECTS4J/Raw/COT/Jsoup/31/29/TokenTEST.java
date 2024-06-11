import org.junit.Test;

public class TokenTest {
    
    @Test
    public void testTokenType(){
        Token token = new Token.Comment();
        assert token.tokenType().equals("Comment");
    }
    
    @Test
    public void testDoctypeGetName(){
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        assert doctype.getName().equals("html");
    }
    
    @Test
    public void testDoctypeGetPublicIdentifier(){
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.0 Transitional//EN");
        assert doctype.getPublicIdentifier().equals("-//W3C//DTD HTML 4.0 Transitional//EN");
    }
    
    @Test
    public void testDoctypeGetSystemIdentifier(){
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("http://www.w3.org/TR/REC-html40/loose.dtd");
        assert doctype.getSystemIdentifier().equals("http://www.w3.org/TR/REC-html40/loose.dtd");
    }
    
    @Test
    public void testDoctypeIsForceQuirks(){
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;
        assert doctype.isForceQuirks() == true;
    }
    
    @Test
    public void testTagNewAttribute(){
        Token.StartTag startTag = new Token.StartTag();
        startTag.newAttribute();
        assert startTag.attributes != null;
    }
    
    @Test
    public void testTagFinaliseTag(){
        Token.StartTag startTag = new Token.StartTag();
        startTag.finaliseTag();
        assert startTag.pendingAttributeName == null;
    }
    
    @Test
    public void testTagName(){
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        assert startTag.name().equals("div");
    }
    
    @Test
    public void testTagIsSelfClosing(){
        Token.StartTag startTag = new Token.StartTag();
        startTag.selfClosing = true;
        assert startTag.isSelfClosing() == true;
    }
    
    @Test
    public void testTagGetAttributes(){
        Token.StartTag startTag = new Token.StartTag();
        assert startTag.getAttributes() == null;
    }
    
    @Test
    public void testTagAppendTagName(){
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendTagName("div");
        assert startTag.tagName.equals("div");
    }
    
    @Test
    public void testTagAppendAttributeName(){
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendAttributeName("href");
        assert startTag.pendingAttributeName.equals("href");
    }
    
    @Test
    public void testTagAppendAttributeValue(){
        Token.StartTag startTag = new Token.StartTag();
        startTag.appendAttributeValue("http://example.com");
        assert startTag.pendingAttributeValue.toString().equals("http://example.com");
    }
    
    @Test
    public void testStartTagToString(){
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        assert startTag.toString().equals("<div>");
    }
    
    @Test
    public void testEndTagToString(){
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        assert endTag.toString().equals("</div>");
    }
    
    @Test
    public void testCommentGetData(){
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        assert comment.getData().equals("This is a comment");
    }
    
    @Test
    public void testCharacterGetData(){
        Token.Character character = new Token.Character("This is a character");
        assert character.getData().equals("This is a character");
    }
    
    @Test
    public void testEOF(){
        Token.EOF eof = new Token.EOF();
        assert eof.type == Token.TokenType.EOF;
    }
    
    @Test
    public void testIsDoctype(){
        Token.Doctype doctype = new Token.Doctype();
        assert doctype.isDoctype() == true;
    }
    
    @Test
    public void testAsDoctype(){
        Token.Doctype doctype = new Token.Doctype();
        Token token = doctype;
        assert token.isDoctype() == true;
    }
    
    @Test
    public void testIsStartTag(){
        Token.StartTag startTag = new Token.StartTag();
        assert startTag.isStartTag() == true;
    }
    
    @Test
    public void testAsStartTag(){
        Token.StartTag startTag = new Token.StartTag();
        Token token = startTag;
        assert token.isStartTag() == true;
    }
    
    @Test
    public void testIsEndTag(){
        Token.EndTag endTag = new Token.EndTag();
        assert endTag.isEndTag() == true;
    }
    
    @Test
    public void testAsEndTag(){
        Token.EndTag endTag = new Token.EndTag();
        Token token = endTag;
        assert token.isEndTag() == true;
    }
    
    @Test
    public void testIsComment(){
        Token.Comment comment = new Token.Comment();
        assert comment.isComment() == true;
    }
    
    @Test
    public void testAsComment(){
        Token.Comment comment = new Token.Comment();
        Token token = comment;
        assert token.isComment() == true;
    }
    
    @Test
    public void testIsCharacter(){
        Token.Character character = new Token.Character("c");
        assert character.isCharacter() == true;
    }
    
    @Test
    public void testAsCharacter(){
        Token.Character character = new Token.Character("c");
        Token token = character;
        assert token.isCharacter() == true;
    }
    
    @Test
    public void testIsEOF(){
        Token.EOF eof = new Token.EOF();
        assert eof.isEOF() == true;
    }
    
    @Test
    public void testTokenTypeValues(){
        Token.TokenType[] types = Token.TokenType.values();
        assert types.length == 6;
        assert types[0] == Token.TokenType.Doctype;
        assert types[1] == Token.TokenType.StartTag;
        assert types[2] == Token.TokenType.EndTag;
        assert types[3] == Token.TokenType.Comment;
        assert types[4] == Token.TokenType.Character;
        assert types[5] == Token.TokenType.EOF;
    }
    
}