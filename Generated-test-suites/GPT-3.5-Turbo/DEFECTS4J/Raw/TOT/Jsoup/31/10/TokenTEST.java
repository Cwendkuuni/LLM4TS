
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("testName");
        
        assertEquals("testName", doctype.getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("testPublicIdentifier");
        
        assertEquals("testPublicIdentifier", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("testSystemIdentifier");
        
        assertEquals("testSystemIdentifier", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;
        
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTagNewAttribute() {
        Token.Tag tag = new Token.StartTag();
        tag.newAttribute();
        
        assertNotNull(tag.attributes);
    }

    @Test
    public void testTagFinaliseTagWithPendingAttributeName() {
        Token.Tag tag = new Token.StartTag();
        tag.pendingAttributeName = "testAttribute";
        
        tag.finaliseTag();
        
        assertNull(tag.pendingAttributeName);
    }
    
    @Test
    public void testTagFinaliseTagWithoutPendingAttributeName() {
        Token.Tag tag = new Token.StartTag();
        
        tag.finaliseTag();
        
        assertNull(tag.pendingAttributeName);
    }

    @Test
    public void testTagGetName() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "testTag";
        
        assertEquals("testTag", tag.name());
    }

    @Test
    public void testTagIsSelfClosing() {
        Token.Tag tag = new Token.StartTag();
        tag.selfClosing = true;
        
        assertTrue(tag.isSelfClosing());
    }

    @Test
    public void testTagGetAttributes() {
        Token.Tag tag = new Token.StartTag();
        tag.attributes = new Attributes();
        
        assertNotNull(tag.getAttributes());
    }

    @Test
    public void testTagAppendTagNameWithString() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "test";
        
        tag.appendTagName("Append");
        
        assertEquals("testAppend", tag.tagName);
    }
    
    @Test
    public void testTagAppendTagNameWithChar() {
        Token.Tag tag = new Token.StartTag();
        tag.tagName = "test";
        
        tag.appendTagName('A');
        
        assertEquals("testA", tag.tagName);
    }

    @Test
    public void testTagAppendAttributeNameWithString() {
        Token.Tag tag = new Token.StartTag();
        tag.pendingAttributeName = "testAttribute";
        
        tag.appendAttributeName("Append");
        
        assertEquals("testAttributeAppend", tag.pendingAttributeName);
    }

    @Test
    public void testTagAppendAttributeNameWithChar() {
        Token.Tag tag = new Token.StartTag();
        tag.pendingAttributeName = "testAttribute";
        
        tag.appendAttributeName('A');
        
        assertEquals("testAttributeA", tag.pendingAttributeName);
    }

    @Test
    public void testTagAppendAttributeValueWithString() {
        Token.Tag tag = new Token.StartTag();
        tag.pendingAttributeValue = new StringBuilder("testValue");
        
        tag.appendAttributeValue("Append");
        
        assertEquals("testValueAppend", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testTagAppendAttributeValueWithChar() {
        Token.Tag tag = new Token.StartTag();
        tag.pendingAttributeValue = new StringBuilder("testValue");
        
        tag.appendAttributeValue('A');
        
        assertEquals("testValueA", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testStartTagToStringWithAttributes() {
        Token.StartTag startTag = new Token.StartTag("testTag");
        Attribute attribute = new Attribute("testAttribute", "testValue");
        startTag.attributes.put(attribute);
        
        assertEquals("<testTag testAttribute=\"testValue\">", startTag.toString());
    }

    @Test
    public void testStartTagToStringWithoutAttributes() {
        Token.StartTag startTag = new Token.StartTag("testTag");
        
        assertEquals("<testTag>", startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag("testTag");
        
        assertEquals("</testTag>", endTag.toString());
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("testData");
        
        assertEquals("testData", comment.getData());
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character("testData");
        
        assertEquals("testData", character.getData());
    }

    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();
        
        assertTrue(eof.isEOF());
    }
}
