import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

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
        doctype.reset();
        
        assertNull(doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
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
    }

    @Test
    public void testEndTagReset() {
        endTag.reset();
        
        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
        assertNull(endTag.pendingAttributeName);
        assertEquals("", endTag.pendingAttributeValue.toString());
        assertNull(endTag.pendingAttributeValueS);
        assertFalse(endTag.hasEmptyAttributeValue);
        assertFalse(endTag.hasPendingAttributeValue);
        assertFalse(endTag.selfClosing);
        assertNull(endTag.attributes);
    }

    @Test
    public void testCommentReset() {
        comment.reset();
        
        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCharacterReset() {
        character.reset();
        
        assertNull(character.getData());
    }

    @Test
    public void testEOFRest() {
        eof.reset();
    }

    @Test
    public void testDoctypeGetterMethods() {
        doctype.name.append("name");
        doctype.pubSysKey = "pubSys";
        doctype.publicIdentifier.append("publicId");
        doctype.systemIdentifier.append("systemId");
        doctype.forceQuirks = true;
        
        assertEquals("name", doctype.getName());
        assertEquals("pubSys", doctype.getPubSysKey());
        assertEquals("publicId", doctype.getPublicIdentifier());
        assertEquals("systemId", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagNameAttr() {
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("attr", "value"));
        
        startTag.nameAttr("name", attributes);
        
        assertEquals("name", startTag.tagName);
        assertEquals("name", startTag.normalName);
        assertEquals("attr", startTag.attributes.get("attr"));
    }

    @Test
    public void testStartTagToString() {
        Attributes attributes = new Attributes();
        attributes.put(new Attribute("attr", "value"));
        
        startTag.nameAttr("name", attributes);
        
        assertEquals("<name attr=\"value\">", startTag.toString());
    }

    @Test
    public void testEndTagToString() {
        endTag.name("name");
        
        assertEquals("</name>", endTag.toString());
    }

    @Test
    public void testCommentGetData() {
        comment.data.append("data");
        
        assertEquals("data", comment.getData());
    }

    @Test
    public void testCharacterGetData() {
        character.data("data");
        
        assertEquals("data", character.getData());
    }
}