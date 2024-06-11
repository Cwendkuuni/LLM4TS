import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class TokenTest {
    
    private Token.Doctype doctype;
    private Token.StartTag startTag;
    private Token.EndTag endTag;
    private Token.Comment comment;
    private Token.Character character;
    private Token.EOF eof;
    
    @Before
    public void setUp() {
        doctype = new Token.Doctype();
        startTag = new Token.StartTag();
        endTag = new Token.EndTag();
        comment = new Token.Comment();
        character = new Token.Character();
        eof = new Token.EOF();
    }

    @Test
    public void testDoctypeReset() {
        assertNotNull(doctype.reset());
    }
    
    @Test
    public void testDoctypeGetName() {
        assertNotNull(doctype.getName());
    }
    
    @Test
    public void testDoctypeGetPubSysKey() {
        assertNotNull(doctype.getPubSysKey());
    }
    
    @Test
    public void testDoctypeGetPublicIdentifier() {
        assertNotNull(doctype.getPublicIdentifier());
    }
    
    @Test
    public void testDoctypeGetSystemIdentifier() {
        assertNotNull(doctype.getSystemIdentifier());
    }
    
    @Test
    public void testDoctypeIsForceQuirks() {
        assertNotNull(doctype.isForceQuirks());
    }
    
    @Test
    public void testStartTagReset() {
        assertNotNull(startTag.reset());
    }
    
    @Test
    public void testStartTagName() {
        assertNotNull(startTag.name("tag"));
    }
    
    @Test
    public void testStartTagIsSelfClosing() {
        assertNotNull(startTag.isSelfClosing());
    }
    
    @Test
    public void testStartTagGetAttributes() {
        assertNotNull(startTag.getAttributes());
    }
    
    @Test
    public void testEndTagReset() {
        assertNotNull(endTag.reset());
    }
    
    @Test
    public void testCommentReset() {
        assertNotNull(comment.reset());
    }
    
    @Test
    public void testCommentGetData() {
        assertNotNull(comment.getData());
    }
    
    @Test
    public void testCharacterReset() {
        assertNotNull(character.reset());
    }
    
    @Test
    public void testCharacterData() {
        assertNotNull(character.data("data"));
    }
    
    @Test
    public void testEOFReset() {
        assertNotNull(eof.reset());
    }
    
    @Test
    public void testIsDoctype() {
        assertNotNull(doctype.isDoctype());
    }
    
    @Test
    public void testAsDoctype() {
        assertNotNull(doctype.asDoctype());
    }
    
    @Test
    public void testIsStartTag() {
        assertNotNull(startTag.isStartTag());
    }
    
    @Test
    public void testAsStartTag() {
        assertNotNull(startTag.asStartTag());
    }
    
    @Test
    public void testIsEndTag() {
        assertNotNull(endTag.isEndTag());
    }
    
    @Test
    public void testAsEndTag() {
        assertNotNull(endTag.asEndTag());
    }
    
    @Test
    public void testIsComment() {
        assertNotNull(comment.isComment());
    }
    
    @Test
    public void testAsComment() {
        assertNotNull(comment.asComment());
    }
    
    @Test
    public void testIsCharacter() {
        assertNotNull(character.isCharacter());
    }
    
    @Test
    public void testAsCharacter() {
        assertNotNull(character.asCharacter());
    }
    
    @Test
    public void testIsEOF() {
        assertNotNull(eof.isEOF());
    }
    
    @Test
    public void testTokenType() {
        assertNotNull(Token.TokenType.Doctype);
        assertNotNull(Token.TokenType.StartTag);
        assertNotNull(Token.TokenType.EndTag);
        assertNotNull(Token.TokenType.Comment);
        assertNotNull(Token.TokenType.Character);
        assertNotNull(Token.TokenType.EOF);
    }
}