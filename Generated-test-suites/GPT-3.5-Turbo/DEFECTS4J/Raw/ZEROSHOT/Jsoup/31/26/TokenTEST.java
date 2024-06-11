import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

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
        character = new Token.Character("data");
        eof = new Token.EOF();
    }
    
    @Test
    public void testTokenType() {
        Assert.assertEquals("Doctype", doctype.tokenType());
        Assert.assertEquals("StartTag", startTag.tokenType());
        Assert.assertEquals("EndTag", endTag.tokenType());
        Assert.assertEquals("Comment", comment.tokenType());
        Assert.assertEquals("Character", character.tokenType());
        Assert.assertEquals("EOF", eof.tokenType());
    }
    
    @Test
    public void testDoctypeMethods() {
        doctype.forceQuirks = false;
        Assert.assertFalse(doctype.isForceQuirks());
        
        doctype.name.append("name");
        Assert.assertEquals("name", doctype.getName());
        
        doctype.publicIdentifier.append("publicIdentifier");
        Assert.assertEquals("publicIdentifier", doctype.getPublicIdentifier());
        
        doctype.systemIdentifier.append("systemIdentifier");
        Assert.assertEquals("systemIdentifier", doctype.getSystemIdentifier());
    }
    
    @Test
    public void testTagMethods() {
        startTag.tagName = "tagName";
        Assert.assertEquals("tagName", startTag.name());
        Assert.assertFalse(startTag.isSelfClosing());
        
        startTag.selfClosing = true;
        Assert.assertTrue(startTag.isSelfClosing());
        
        startTag.attributes = new Attributes();
        startTag.newAttribute();
        Assert.assertNotNull(startTag.getAttributes());
        
        startTag.attributes = null;
        startTag.pendingAttributeName = "attributeName";
        startTag.pendingAttributeValue = new StringBuilder("attributeValue");
        startTag.newAttribute();
        Assert.assertNotNull(startTag.getAttributes());
    }
    
    @Test
    public void testStartTagToString() {
        startTag.tagName = "tagName";
        Assert.assertEquals("<tagName>", startTag.toString());
        
        startTag.attributes = new Attributes();
        startTag.attributes.put(new Attribute("attribute1", "value1"));
        startTag.attributes.put(new Attribute("attribute2", "value2"));
        Assert.assertEquals("<tagName attribute1=\"value1\" attribute2=\"value2\">", startTag.toString());
    }
    
    @Test
    public void testEndTagToString() {
        endTag.tagName = "tagName";
        Assert.assertEquals("</tagName>", endTag.toString());
    }
    
    @Test
    public void testCommentMethods() {
        comment.data.append("data");
        Assert.assertEquals("data", comment.getData());
    }
    
    @Test
    public void testCharacterMethods() {
        Assert.assertEquals("data", character.getData());
    }
    
    @Test
    public void testEOF() {
        Assert.assertTrue(eof.isEOF());
    }
    
    @Test
    public void testType() {
        Assert.assertTrue(doctype.isDoctype());
        Assert.assertTrue(startTag.isStartTag());
        Assert.assertTrue(endTag.isEndTag());
        Assert.assertTrue(comment.isComment());
        Assert.assertTrue(character.isCharacter());
    }
}