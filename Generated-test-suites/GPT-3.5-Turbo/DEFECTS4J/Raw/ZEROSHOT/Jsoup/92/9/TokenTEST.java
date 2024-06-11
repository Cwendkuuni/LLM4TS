import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenTest {

    private Token.Doctype doctype;
    private Token.StartTag startTag;
    private Token.EndTag endTag;
    private Token.Comment comment;
    private Token.Character character;
    private Token.CData cdata;
    private Token.EOF eof;

    @Before
    public void setUp() {
        doctype = new Token.Doctype();
        startTag = new Token.StartTag();
        endTag = new Token.EndTag();
        comment = new Token.Comment();
        character = new Token.Character();
        cdata = new Token.CData("test cdata");
        eof = new Token.EOF();
    }
    
    @Test
    public void testDoctype() {
        doctype.reset();
        assertEquals("Token.Doctype", doctype.tokenType());
        
        doctype.name.append("test name");
        doctype.pubSysKey = "test pubSysKey";
        doctype.publicIdentifier.append("test public identifier");
        doctype.systemIdentifier.append("test system identifier");
        doctype.forceQuirks = true;
        
        assertEquals("test name", doctype.getName());
        assertEquals("test pubSysKey", doctype.getPubSysKey());
        assertEquals("test public identifier", doctype.getPublicIdentifier());
        assertEquals("test system identifier", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }
    
    @Test
    public void testStartTag() {
        startTag.reset();
        assertEquals("Token.StartTag", startTag.tokenType());
        
        startTag.tagName = "test tag";
        startTag.normalName = "test normal tag";
        startTag.pendingAttributeName = "test attribute";
        startTag.pendingAttributeValue.append("test value");
        startTag.pendingAttributeValueS = "test value s";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.appendTagName('a');
        startTag.appendAttributeName("class");
        startTag.appendAttributeValue("test class");
        
        assertEquals("test tag", startTag.name());
        assertEquals("test normal tag", startTag.normalName());
        assertTrue(startTag.isSelfClosing());
        assertEquals("test tag test normal tag test attribute a test value test value s test", startTag.toString());
        
        assertNull(startTag.attributes.get("test attribute"));
        assertNull(startTag.attributes.get("class"));
        startTag.newAttribute();
        assertEquals("test class", startTag.attributes.get("class"));
        assertNull(startTag.attributes.get("test attribute"));
    }
    
    @Test
    public void testEndTag() {
        endTag.reset();
        assertEquals("Token.EndTag", endTag.tokenType());
        
        endTag.tagName = "test tag";
        endTag.normalName = "test normal tag";
        
        assertEquals("test tag", endTag.name());
        assertEquals("test normal tag", endTag.normalName());
        assertEquals("</test tag>", endTag.toString());
    }
    
    @Test
    public void testComment() {
        comment.reset();
        assertEquals("Token.Comment", comment.tokenType());
        
        comment.data.append("test comment");
        comment.bogus = true;
        
        assertEquals("test comment", comment.getData());
        assertTrue(comment.bogus);
        assertEquals("<!--test comment-->", comment.toString());
    }
    
    @Test
    public void testCharacter() {
        character.reset();
        assertEquals("Token.Character", character.tokenType());
        
        character.data("test character");
        
        assertEquals("test character", character.getData());
        assertEquals("test character", character.toString());
    }
    
    @Test
    public void testCData() {
        assertEquals("Token.CData", cdata.tokenType());
        assertEquals("test cdata", cdata.getData());
        assertEquals("<![CDATA[test cdata]]>", cdata.toString());
    }
    
    @Test
    public void testEOF() {
        eof.reset();
        assertEquals("Token.EOF", eof.tokenType());
    }
}