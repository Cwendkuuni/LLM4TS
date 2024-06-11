import org.jsoup.helper.Validate;
import org.junit.Test;
import org.junit.Assert;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token.Doctype doctype = new Token.Doctype();
        Assert.assertEquals(doctype.type, Token.TokenType.Doctype);
        
        Token.StartTag startTag = new Token.StartTag();
        Assert.assertEquals(startTag.type, Token.TokenType.StartTag);
        
        Token.EndTag endTag = new Token.EndTag();
        Assert.assertEquals(endTag.type, Token.TokenType.EndTag);
        
        Token.Comment comment = new Token.Comment();
        Assert.assertEquals(comment.type, Token.TokenType.Comment);
        
        Token.Character character = new Token.Character("data");
        Assert.assertEquals(character.type, Token.TokenType.Character);
        
        Token.EOF eof = new Token.EOF();
        Assert.assertEquals(eof.type, Token.TokenType.EOF);
    }

    @Test
    public void testGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("doctype");
        Assert.assertEquals(doctype.getName(), "doctype");
    }
    
    @Test
    public void testGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("public");
        Assert.assertEquals(doctype.getPublicIdentifier(), "public");
    }
    
    @Test
    public void testGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("system");
        Assert.assertEquals(doctype.getSystemIdentifier(), "system");
    }
    
    @Test
    public void testIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        Assert.assertFalse(doctype.isForceQuirks());
        doctype.forceQuirks = true;
        Assert.assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testSelfClosing() {
        Token.StartTag startTag = new Token.StartTag();
        Assert.assertFalse(startTag.isSelfClosing());
        startTag.selfClosing = true;
        Assert.assertTrue(startTag.isSelfClosing());
    }

    @Test
    public void testAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        Assert.assertNull(startTag.attributes);
        
        Token.Attributes attributes = new Token.Attributes();
        startTag.attributes = attributes;
        Assert.assertEquals(startTag.getAttributes(), attributes);
    }

    @Test
    public void testNewAttribute() {
        Token.StartTag startTag = new Token.StartTag();
        Assert.assertNull(startTag.attributes);
        
        startTag.newAttribute();
        Assert.assertNotNull(startTag.attributes);
        
        startTag.pendingAttributeName = "name";
        startTag.pendingAttributeValue = new StringBuilder("value");
        startTag.newAttribute();
        
        Assert.assertEquals(startTag.attributes.size(), 1);
        Assert.assertEquals(startTag.attributes.get("name"), "value");
    }

    @Test
    public void testFinaliseTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.pendingAttributeName = "name";
        startTag.pendingAttributeValue = new StringBuilder("value");
        startTag.finaliseTag();
        
        Assert.assertEquals(startTag.attributes.size(), 1);
        Assert.assertEquals(startTag.attributes.get("name"), "value");
    }

    @Test
    public void testName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tag";
        Assert.assertEquals(startTag.name(), "tag");
        
        startTag.tagName = "";
        Validate.isFalse(startTag.tagName.length() == 0);
    }

    @Test
    public void testAppendTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = null;
        startTag.appendTagName("a");
        Assert.assertEquals(startTag.tagName, "a");
        
        startTag.tagName = "b";
        startTag.appendTagName('c');
        Assert.assertEquals(startTag.tagName, "bc");
    }

    @Test
    public void testAppendAttributeName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.pendingAttributeName = null;
        startTag.appendAttributeName("a");
        Assert.assertEquals(startTag.pendingAttributeName, "a");
        
        startTag.pendingAttributeName = "b";
        startTag.appendAttributeName('c');
        Assert.assertEquals(startTag.pendingAttributeName, "bc");
    }

    @Test
    public void testAppendAttributeValue() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.pendingAttributeValue = null;
        startTag.appendAttributeValue("a");
        Assert.assertEquals(startTag.pendingAttributeValue.toString(), "a");
        
        startTag.pendingAttributeValue = new StringBuilder("b");
        startTag.appendAttributeValue('c');
        Assert.assertEquals(startTag.pendingAttributeValue.toString(), "bc");
    }

    @Test
    public void testStartTagToString() {
        Token.StartTag startTag = new Token.StartTag("tag");
        Assert.assertEquals(startTag.toString(), "<tag>");
        
        startTag.attributes.put("attr", "value");
        Assert.assertEquals(startTag.toString(), "<tag attr=\"value\">");
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag("tag");
        Assert.assertEquals(endTag.toString(), "</tag>");
    }

    @Test
    public void testGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("data");
        Assert.assertEquals(comment.getData(), "data");
    }

    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character("data");
        Assert.assertEquals(character.getData(), "data");
    }
}