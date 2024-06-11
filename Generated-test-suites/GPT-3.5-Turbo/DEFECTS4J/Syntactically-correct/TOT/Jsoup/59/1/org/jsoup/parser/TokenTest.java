import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {

    // Doctype test case
    @Test
    public void testDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        // Set values for Doctype object
        doctype.name.append("html");
        doctype.pubSysKey = "-//W3C//DTD HTML 4.0//EN";
        doctype.publicIdentifier.append("http://www.w3.org/TR/REC-html40/strict.dtd");
        doctype.systemIdentifier.append("http://www.w3.org/TR/REC-html40/strict.dtd");
        doctype.forceQuirks = true;
        
        // Test reset method
        doctype.reset();
        assertEquals("", doctype.name.toString());
        assertNull(doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
        
        // Test getters
        assertNull(doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    // StartTag test case
    @Test
    public void testStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        // Set values for StartTag object
        startTag.name("div");
        startTag.selfClosing = false;
        startTag.attributes = new Attributes();
        startTag.attributes.put("id", "container");
        startTag.attributes.put("class", "wrapper");
        startTag.attributes.put("data-name", "John Doe");
        
        // Test reset method
        startTag.reset();
        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);
        
        // Test getters
        assertNull(startTag.name());
        assertNull(startTag.normalName());
        assertFalse(startTag.isSelfClosing());
        assertNull(startTag.getAttributes());
    }

    // EndTag test case
    @Test
    public void testEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        // Set values for EndTag object
        endTag.tagName = "body";
        endTag.normalName = "body";
        
        // Test reset method
        endTag.reset();
        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
        
        // Test toString method
        assertEquals("</body>", endTag.toString());
    }

    // Comment test case
    @Test
    public void testComment() {
        Token.Comment comment = new Token.Comment();
        // Set values for Comment object
        comment.data.append("This is a comment");
        comment.bogus = false;
        
        // Test reset method
        comment.reset();
        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
        
        // Test getters
        assertNull(comment.getData());
        
        // Test toString method
        assertEquals("<!--This is a comment-->", comment.toString());
    }

    // Character test case
    @Test
    public void testCharacter() {
        Token.Character character = new Token.Character();
        // Set values for Character object
        character.data = "This is a character";
        
        // Test reset method
        character.reset();
        assertNull(character.data);
        
        // Test getData method
        assertNull(character.getData());
        
        // Test toString method
        assertNull(character.toString());
    }

    // EOF test case
    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();
        
        // Test reset method
        eof.reset();
        assertEquals(eof, eof.reset());
    }
}