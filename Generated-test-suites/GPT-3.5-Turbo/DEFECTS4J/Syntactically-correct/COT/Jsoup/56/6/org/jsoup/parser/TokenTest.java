import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType(){
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        assertEquals(null, token.type);
    }

    @Test
    public void testTokenTypeString(){
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        assertEquals("Token", token.tokenType());
    }

    @Test
    public void testResetDoctype(){
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("name");
        doctype.publicIdentifier.append("public");
        doctype.systemIdentifier.append("system");
        doctype.forceQuirks = true;
        doctype.reset();

        assertEquals("", doctype.name.toString());
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testGetName(){
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("name");
        assertEquals("name", doctype.getName());
    }

    @Test
    public void testGetPublicIdentifier(){
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("public");
        assertEquals("public", doctype.getPublicIdentifier());
    }

    @Test
    public void testGetSystemIdentifier(){
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("system");
        assertEquals("system", doctype.getSystemIdentifier());
    }

    @Test
    public void testIsForceQuirks(){
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testResetStartTag(){
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tag";
        startTag.normalName = "tag";
        startTag.pendingAttributeName = "name";
        startTag.pendingAttributeValue.append("value");
        startTag.pendingAttributeValueS = "valueS";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();
        
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
    public void testNewAttribute(){
        Token.StartTag startTag = new Token.StartTag();
        startTag.pendingAttributeName = "name";
        startTag.attributes = new Attributes();

        startTag.newAttribute();

        assertNull(startTag.pendingAttributeName);
        assertNotNull(startTag.attributes);
        assertEquals("name", startTag.attributes.get("name"));
    }

    @Test
    public void testFinaliseTag(){
        Token.StartTag startTag = new Token.StartTag();
        startTag.pendingAttributeName = "name";
        startTag.finaliseTag();

        assertNull(startTag.pendingAttributeName);

        startTag.attributes = new Attributes();
        startTag.pendingAttributeName = "name";
        startTag.hasPendingAttributeValue = true;
        startTag.pendingAttributeValue.append("value");
        startTag.finaliseTag();

        assertNull(startTag.pendingAttributeName);
        assertEquals("name", startTag.attributes.get("name"));
        assertEquals("value", startTag.attributes.get("value"));
    }

    @Test
    public void testName(){
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "tag";
        assertEquals("tag", startTag.name());
    }

    @Test
    public void testNormalName(){
        Token.StartTag startTag = new Token.StartTag();
        startTag.normalName = "tag";
        assertEquals("tag", startTag.normalName());
    }

    @Test
    public void testIsSelfClosing(){
        Token.StartTag startTag = new Token.StartTag();
        startTag.selfClosing = true;
        assertTrue(startTag.isSelfClosing());
    }

}