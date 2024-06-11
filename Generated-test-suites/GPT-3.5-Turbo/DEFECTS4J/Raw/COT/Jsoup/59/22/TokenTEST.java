
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {
    
    private Token.Doctype doctype;
    private Token.StartTag startTag;
    private Token.EndTag endTag;
    private Token.Comment comment;
    
    @Before
    public void setUp() {
        doctype = new Token.Doctype();
        startTag = new Token.StartTag();
        endTag = new Token.EndTag();
        comment = new Token.Comment();
    }
    
    @Test
    public void testDoctypeReset() {
        doctype.name.append("html");
        doctype.pubSysKey = "PUBLIC";
        doctype.publicIdentifier.append("publicID");
        doctype.systemIdentifier.append("systemID");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }
    
    @Test
    public void testStartTagNameAttribute() {
        startTag.name("div");
        startTag.attributes = new Attributes();
        startTag.attributes.put("id", "myId");

        assertEquals("div", startTag.name());
        assertEquals("DIV", startTag.normalName());
        assertEquals("id", startTag.pendingAttributeName);
        assertEquals("myId", startTag.pendingAttributeValue.toString());
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertNotNull(startTag.attributes);
    }
    
    @Test
    public void testEndTagName() {
        endTag.name("div");

        assertEquals("div", endTag.name());
        assertEquals("DIV", endTag.normalName());
    }
    
    @Test
    public void testCommentReset() {
        comment.data.append("This is a comment");
        comment.bogus = true;

        comment.reset();

        assertEquals("", comment.getData());
        assertFalse(comment.bogus);
    }
    
}
