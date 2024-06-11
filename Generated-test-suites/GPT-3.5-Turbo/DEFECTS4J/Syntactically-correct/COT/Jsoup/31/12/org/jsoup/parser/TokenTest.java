import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
    }
    
    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("test");
        assertEquals("test", doctype.getName());
    }
    
    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("public");
        assertEquals("public", doctype.getPublicIdentifier());
    }
    
    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("system");
        assertEquals("system", doctype.getSystemIdentifier());
    }
    
    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        assertFalse(doctype.isForceQuirks());
    }
    
    @Test
    public void testTagNewAttribute() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.newAttribute();
        assertNotNull(startTag.attributes);
        assertEquals(0, startTag.attributes.size());
    }
    
    @Test
    public void testTagFinaliseTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.newAttribute();
        startTag.finaliseTag();
        assertNotNull(startTag.attributes);
        assertEquals(1, startTag.attributes.size());
    }
    
    @Test
    public void testTagName() {
        Token.Tag tag = new Token.StartTag();
        tag.name("test");
        assertEquals("test", tag.name());
    }
    
    @Test
    public void testTagIsSelfClosing() {
        Token.Tag tag = new Token.StartTag();
        assertFalse(tag.isSelfClosing());
    }
    
    @Test
    public void testTagGetAttributes() {
        Token.Tag tag = new Token.StartTag();
        assertNotNull(tag.getAttributes());
    }
    
    @Test
    public void testTagAppendTagName() {
        Token.Tag tag = new Token.StartTag();
        tag.appendTagName("test");
        assertEquals("test", tag.tagName);
    }
    
    @Test
    public void testTagAppendAttributeName() {
        Token.Tag tag = new Token.StartTag();
        tag.appendAttributeName("test");
        assertEquals("test", tag.pendingAttributeName);
    }
    
    @Test
    public void testTagAppendAttributeValue() {
        Token.Tag tag = new Token.StartTag();
        tag.appendAttributeValue("test");
        assertEquals("test", tag.pendingAttributeValue.toString());
    }
    
    @Test
    public void testStartTagToStringWithAttribute() {
        Token.StartTag startTag = new Token.StartTag("test");
        startTag.attributes.put("attr", "value");
        assertEquals("<test attr=\"value\">", startTag.toString());
    }
    
    @Test
    public void testStartTagToStringWithoutAttribute() {
        Token.StartTag startTag = new Token.StartTag("test");
        assertEquals("<test>", startTag.toString());
    }
    
    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag("test");
        assertEquals("</test>", endTag.toString());
    }
    
    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("comment");
        assertEquals("comment", comment.getData());
    }
    
    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("data");
        assertEquals("data", character.getData());
    }
    
    @Test
    public void testEOFIsEOF() {
        Token.EOF eof = new Token.EOF();
        assertTrue(eof.isEOF());
    }
    
}