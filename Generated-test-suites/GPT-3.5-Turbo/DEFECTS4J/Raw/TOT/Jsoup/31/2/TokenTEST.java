import org.junit.Test;
import org.junit.Assert;
import org.jsoup.helper.Validate;

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
        
        Token.Character character = new Token.Character("data");
        Assert.assertEquals("Character", character.tokenType());
        
        Token.EOF eof = new Token.EOF();
        Assert.assertEquals("EOF", eof.tokenType());
    }
    
    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        Assert.assertEquals("html", doctype.getName());
    }
    
    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("-//W3C//DTD XHTML 1.0 Transitional//EN");
        Assert.assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", doctype.getPublicIdentifier());
    }
    
    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
        Assert.assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", doctype.getSystemIdentifier());
    }
    
    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        Assert.assertFalse(doctype.isForceQuirks());
        
        doctype.forceQuirks = true;
        Assert.assertTrue(doctype.isForceQuirks());
    }
    
    @Test
    public void testStartTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        Assert.assertEquals("div", startTag.name());
        
        startTag.tagName = "";
        try {
            startTag.name();
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // do nothing
        }
    }
    
    @Test
    public void testStartTagIsSelfClosing() {
        Token.StartTag startTag = new Token.StartTag();
        Assert.assertFalse(startTag.isSelfClosing());
        
        startTag.selfClosing = true;
        Assert.assertTrue(startTag.isSelfClosing());
    }
    
    @Test
    public void testStartTagGetAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        Assert.assertNull(startTag.getAttributes());
        
        Token.Attributes attributes = new Token.Attributes();
        startTag.attributes = attributes;
        Assert.assertEquals(attributes, startTag.getAttributes());
    }
    
    @Test
    public void testEndTagName() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        Assert.assertEquals("div", endTag.name());
        
        endTag.tagName = "";
        try {
            endTag.name();
            Assert.fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // do nothing
        }
    }
    
    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        Assert.assertEquals("This is a comment", comment.getData());
    }
    
    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("data");
        Assert.assertEquals("data", character.getData());
    }
    
    @Test
    public void testIsDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        Assert.assertTrue(doctype.isDoctype());

        Token.StartTag startTag = new Token.StartTag();
        Assert.assertFalse(startTag.isDoctype());
        
        Token.EndTag endTag = new Token.EndTag();
        Assert.assertFalse(endTag.isDoctype());
        
        Token.Comment comment = new Token.Comment();
        Assert.assertFalse(comment.isDoctype());
        
        Token.Character character = new Token.Character("data");
        Assert.assertFalse(character.isDoctype());
        
        Token.EOF eof = new Token.EOF();
        Assert.assertFalse(eof.isDoctype());
    }
    
    @Test
    public void testIsStartTag() {
        Token.Doctype doctype = new Token.Doctype();
        Assert.assertFalse(doctype.isStartTag());

        Token.StartTag startTag = new Token.StartTag();
        Assert.assertTrue(startTag.isStartTag());
        
        Token.EndTag endTag = new Token.EndTag();
        Assert.assertFalse(endTag.isStartTag());
        
        Token.Comment comment = new Token.Comment();
        Assert.assertFalse(comment.isStartTag());
        
        Token.Character character = new Token.Character("data");
        Assert.assertFalse(character.isStartTag());
        
        Token.EOF eof = new Token.EOF();
        Assert.assertFalse(eof.isStartTag());
    }
    
    @Test
    public void testIsEndTag() {
        Token.Doctype doctype = new Token.Doctype();
        Assert.assertFalse(doctype.isEndTag());

        Token.StartTag startTag = new Token.StartTag();
        Assert.assertFalse(startTag.isEndTag());
        
        Token.EndTag endTag = new Token.EndTag();
        Assert.assertTrue(endTag.isEndTag());
        
        Token.Comment comment = new Token.Comment();
        Assert.assertFalse(comment.isEndTag());
        
        Token.Character character = new Token.Character("data");
        Assert.assertFalse(character.isEndTag());
        
        Token.EOF eof = new Token.EOF();
        Assert.assertFalse(eof.isEndTag());
    }
    
    @Test
    public void testIsComment() {
        Token.Doctype doctype = new Token.Doctype();
        Assert.assertFalse(doctype.isComment());

        Token.StartTag startTag = new Token.StartTag();
        Assert.assertFalse(startTag.isComment());
        
        Token.EndTag endTag = new Token.EndTag();
        Assert.assertFalse(endTag.isComment());
        
        Token.Comment comment = new Token.Comment();
        Assert.assertTrue(comment.isComment());
        
        Token.Character character = new Token.Character("data");
        Assert.assertFalse(character.isComment());
        
        Token.EOF eof = new Token.EOF();
        Assert.assertFalse(eof.isComment());
    }
    
    @Test
    public void testIsCharacter() {
        Token.Doctype doctype = new Token.Doctype();
        Assert.assertFalse(doctype.isCharacter());

        Token.StartTag startTag = new Token.StartTag();
        Assert.assertFalse(startTag.isCharacter());
        
        Token.EndTag endTag = new Token.EndTag();
        Assert.assertFalse(endTag.isCharacter());
        
        Token.Comment comment = new Token.Comment();
        Assert.assertFalse(comment.isCharacter());
        
        Token.Character character = new Token.Character("data");
        Assert.assertTrue(character.isCharacter());
        
        Token.EOF eof = new Token.EOF();
        Assert.assertFalse(eof.isCharacter());
    }
    
    @Test
    public void testIsEOF() {
        Token.Doctype doctype = new Token.Doctype();
        Assert.assertFalse(doctype.isEOF());

        Token.StartTag startTag = new Token.StartTag();
        Assert.assertFalse(startTag.isEOF());
        
        Token.EndTag endTag = new Token.EndTag();
        Assert.assertFalse(endTag.isEOF());
        
        Token.Comment comment = new Token.Comment();
        Assert.assertFalse(comment.isEOF());
        
        Token.Character character = new Token.Character("data");
        Assert.assertFalse(character.isEOF());
        
        Token.EOF eof = new Token.EOF();
        Assert.assertTrue(eof.isEOF());
    }
    
    // Additional test cases for other methods can be included here
    
}