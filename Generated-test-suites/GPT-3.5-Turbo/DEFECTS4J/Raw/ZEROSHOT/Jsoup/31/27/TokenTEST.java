import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Attributes;

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
        character = new Token.Character("");
        eof = new Token.EOF();
    }

    @Test
    public void testDoctypeGetName() {
        // test initial value
        Assert.assertEquals("", doctype.getName());
        
        // test set value
        doctype.name.append("html");
        Assert.assertEquals("html", doctype.getName());
    }
    
    @Test
    public void testDoctypeGetPublicIdentifier() {
        // test initial value
        Assert.assertEquals("", doctype.getPublicIdentifier());
        
        // test set value
        doctype.publicIdentifier.append("publicId");
        Assert.assertEquals("publicId", doctype.getPublicIdentifier());
    }
    
    @Test
    public void testDoctypeIsForceQuirks() {
        // test initial value
        Assert.assertFalse(doctype.isForceQuirks());
        
        // test set value
        doctype.forceQuirks = true;
        Assert.assertTrue(doctype.isForceQuirks());
    }
    
    @Test
    public void testStartTagName() {
        // test initial value
        Assert.assertNull(startTag.tagName);
        
        // test set value
        startTag.name("div");
        Assert.assertEquals("div", startTag.name());
    }
    
    @Test
    public void testStartTagIsSelfClosing() {
        // test initial value
        Assert.assertFalse(startTag.isSelfClosing());
        
        // test set value
        startTag.selfClosing = true;
        Assert.assertTrue(startTag.isSelfClosing());
    }
    
    @Test
    public void testStartTagGetAttributes() {
        // test initial value
        Assert.assertNull(startTag.getAttributes());
        
        // test set value
        Attributes attributes = new Attributes();
        startTag.attributes = attributes;
        Assert.assertEquals(attributes, startTag.getAttributes());
    }
    
    @Test
    public void testEndTagName() {
        // test initial value
        Assert.assertNull(endTag.tagName);
        
        // test set value
        endTag.name("div");
        Assert.assertEquals("div", endTag.name());
    }
    
    @Test
    public void testCommentGetData() {
        // test initial value
        Assert.assertEquals("", comment.getData());
        
        // test set value
        comment.data.append("Hello World!");
        Assert.assertEquals("Hello World!", comment.getData());
    }
    
    @Test
    public void testCharacterGetData() {
        // test initial value
        Assert.assertEquals("", character.getData());
        
        // test set value
        Token.Character character = new Token.Character("Hello");
        Assert.assertEquals("Hello", character.getData());
    }
    
    @Test
    public void testEOF() {
        // test EOF token type
        Assert.assertEquals(Token.TokenType.EOF, eof.type);
    }
    
    @Test
    public void testIsDoctype() {
        Assert.assertTrue(doctype.isDoctype());
        Assert.assertFalse(startTag.isDoctype());
        Assert.assertFalse(endTag.isDoctype());
        Assert.assertFalse(comment.isDoctype());
        Assert.assertFalse(character.isDoctype());
        Assert.assertFalse(eof.isDoctype());
    }
    
    @Test
    public void testAsDoctype() {
        Assert.assertEquals(doctype, doctype.asDoctype());
        Assert.assertNotEquals(startTag, startTag.asDoctype());
        Assert.assertNotEquals(endTag, endTag.asDoctype());
        Assert.assertNotEquals(comment, comment.asDoctype());
        Assert.assertNotEquals(character, character.asDoctype());
        Assert.assertNotEquals(eof, eof.asDoctype());
    }
    
    @Test
    public void testIsStartTag() {
        Assert.assertFalse(doctype.isStartTag());
        Assert.assertTrue(startTag.isStartTag());
        Assert.assertFalse(endTag.isStartTag());
        Assert.assertFalse(comment.isStartTag());
        Assert.assertFalse(character.isStartTag());
        Assert.assertFalse(eof.isStartTag());
    }
    
    @Test
    public void testAsStartTag() {
        Assert.assertNotEquals(doctype, doctype.asStartTag());
        Assert.assertEquals(startTag, startTag.asStartTag());
        Assert.assertNotEquals(endTag, endTag.asStartTag());
        Assert.assertNotEquals(comment, comment.asStartTag());
        Assert.assertNotEquals(character, character.asStartTag());
        Assert.assertNotEquals(eof, eof.asStartTag());
    }
    
    @Test
    public void testIsEndTag() {
        Assert.assertFalse(doctype.isEndTag());
        Assert.assertFalse(startTag.isEndTag());
        Assert.assertTrue(endTag.isEndTag());
        Assert.assertFalse(comment.isEndTag());
        Assert.assertFalse(character.isEndTag());
        Assert.assertFalse(eof.isEndTag());
    }
    
    @Test
    public void testAsEndTag() {
        Assert.assertNotEquals(doctype, doctype.asEndTag());
        Assert.assertNotEquals(startTag, startTag.asEndTag());
        Assert.assertEquals(endTag, endTag.asEndTag());
        Assert.assertNotEquals(comment, comment.asEndTag());
        Assert.assertNotEquals(character, character.asEndTag());
        Assert.assertNotEquals(eof, eof.asEndTag());
    }
    
    @Test
    public void testIsComment() {
        Assert.assertFalse(doctype.isComment());
        Assert.assertFalse(startTag.isComment());
        Assert.assertFalse(endTag.isComment());
        Assert.assertTrue(comment.isComment());
        Assert.assertFalse(character.isComment());
        Assert.assertFalse(eof.isComment());
    }
    
    @Test
    public void testAsComment() {
        Assert.assertNotEquals(doctype, doctype.asComment());
        Assert.assertNotEquals(startTag, startTag.asComment());
        Assert.assertNotEquals(endTag, endTag.asComment());
        Assert.assertEquals(comment, comment.asComment());
        Assert.assertNotEquals(character, character.asComment());
        Assert.assertNotEquals(eof, eof.asComment());
    }
    
    @Test
    public void testIsCharacter() {
        Assert.assertFalse(doctype.isCharacter());
        Assert.assertFalse(startTag.isCharacter());
        Assert.assertFalse(endTag.isCharacter());
        Assert.assertFalse(comment.isCharacter());
        Assert.assertTrue(character.isCharacter());
        Assert.assertFalse(eof.isCharacter());
    }
    
    @Test
    public void testAsCharacter() {
        Assert.assertNotEquals(doctype, doctype.asCharacter());
        Assert.assertNotEquals(startTag, startTag.asCharacter());
        Assert.assertNotEquals(endTag, endTag.asCharacter());
        Assert.assertNotEquals(comment, comment.asCharacter());
        Assert.assertEquals(character, character.asCharacter());
        Assert.assertNotEquals(eof, eof.asCharacter());
    }
    
    @Test
    public void testIsEOF() {
        Assert.assertFalse(doctype.isEOF());
        Assert.assertFalse(startTag.isEOF());
        Assert.assertFalse(endTag.isEOF());
        Assert.assertFalse(comment.isEOF());
        Assert.assertFalse(character.isEOF());
        Assert.assertTrue(eof.isEOF());
    }

    @Test
    public void testTokenType() {
        // test token types
        Assert.assertEquals(Token.TokenType.Doctype, doctype.type);
        Assert.assertEquals(Token.TokenType.StartTag, startTag.type);
        Assert.assertEquals(Token.TokenType.EndTag, endTag.type);
        Assert.assertEquals(Token.TokenType.Comment, comment.type);
        Assert.assertEquals(Token.TokenType.Character, character.type);
        Assert.assertEquals(Token.TokenType.EOF, eof.type);
    }
}