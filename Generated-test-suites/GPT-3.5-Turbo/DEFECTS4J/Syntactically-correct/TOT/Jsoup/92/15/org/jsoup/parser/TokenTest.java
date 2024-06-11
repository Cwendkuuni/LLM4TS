import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

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
        cdata = new Token.CData();
        eof = new Token.EOF();
    }
    
    @Test
    public void testDoctypeReset() {
        doctype.name.append("html");
        doctype.pubSysKey = "-//W3C//DTD XHTML 1.0 Strict//EN";
        doctype.publicIdentifier.append("-//W3C//DTD XHTML 1.0 Strict//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
        doctype.forceQuirks = true;
        
        doctype.reset();
        
        assertEquals("", doctype.name.toString());
        assertNull(doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }
    
    @Test
    public void testStartTagReset() {
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "class";
        startTag.pendingAttributeValue.append("container");
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();
        startTag.attributes.put("class", "container");
        
        startTag.reset();
        
        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);
    }
    
    @Test
    public void testEndTagReset() {
        endTag.tagName = "div";
        endTag.normalName = "div";
        
        endTag.reset();
        
        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
    }
    
    @Test
    public void testCommentReset() {
        comment.data.append("This is a comment");
        comment.bogus = true;
        
        comment.reset();
        
        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }
    
    @Test
    public void testCharacterReset() {
        character.data = "Hello";
        
        character.reset();
        
        assertNull(character.data);
    }
    
    @Test
    public void testCDataReset() {
        cdata.data("This is CDATA");
        
        cdata.reset();
        
        assertNull(cdata.getData());
    }
    
    @Test
    public void testEOFReset() {
        EOF resetEOF = eof.reset();
        
        assertSame(eof, resetEOF);
    }
    
    @Test
    public void testDoctypeGetName() {
        doctype.name.append("html");
        
        String name = doctype.getName();
        
        assertEquals("html", name);
    }
    
    @Test
    public void testDoctypeGetPubSysKey() {
        doctype.pubSysKey = "-//W3C//DTD XHTML 1.0 Strict//EN";
        
        String pubSysKey = doctype.getPubSysKey();
        
        assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", pubSysKey);
    }
    
    @Test
    public void testDoctypeGetPublicIdentifier() {
        doctype.publicIdentifier.append("-//W3C//DTD XHTML 1.0 Strict//EN");
        
        String publicIdentifier = doctype.getPublicIdentifier();
        
        assertEquals("-//W3C//DTD XHTML 1.0 Strict//EN", publicIdentifier);
    }
    
    @Test
    public void testDoctypeGetSystemIdentifier() {
        doctype.systemIdentifier.append("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd");
        
        String systemIdentifier = doctype.getSystemIdentifier();
        
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd", systemIdentifier);
    }
    
    @Test
    public void testDoctypeIsForceQuirks() {
        doctype.forceQuirks = true;
        
        boolean isForceQuirks = doctype.isForceQuirks();
        
        assertTrue(isForceQuirks);
    }
    
    @Test
    public void testStartTagName() {
        startTag.tagName = "div";
        
        String tagName = startTag.name();
        
        assertEquals("div", tagName);
    }
    
    @Test
    public void testStartTagNormalName() {
        startTag.tagName = "DIV";
        startTag.normalName = "div";
        
        String normalName = startTag.normalName();
        
        assertEquals("div", normalName);
    }
    
    @Test
    public void testStartTagIsSelfClosing() {
        startTag.selfClosing = true;
        
        boolean isSelfClosing = startTag.isSelfClosing();
        
        assertTrue(isSelfClosing);
    }
    
    @Test
    public void testStartTagGetAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("id", "main");
        
        startTag.attributes = attributes;
        
        Attributes retrievedAttributes = startTag.getAttributes();
        
        assertSame(attributes, retrievedAttributes);
    }
    
    @Test
    public void testStartTagNewAttribute() {
        startTag.newAttribute();
        
        assertNotNull(startTag.attributes);
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertFalse(startTag.hasPendingAttributeValue);
    }
    
    @Test
    public void testStartTagNewAttributeWithPendingAttributeName() {
        startTag.pendingAttributeName = "class";
        
        startTag.newAttribute();
        
        assertNotNull(startTag.attributes);
        assertNull(startTag.pendingAttributeName);
        assertFalse(startTag.hasPendingAttributeValue);
        assertEquals("", startTag.pendingAttributeValue.toString());
    }
    
    @Test
    public void testStartTagFinaliseTag() {
        startTag.pendingAttributeName = "class";
        startTag.pendingAttributeValue.append("container");
        startTag.hasPendingAttributeValue = true;
        
        startTag.finaliseTag();
        
        assertNotNull(startTag.attributes);
        assertNull(startTag.pendingAttributeName);
        assertFalse(startTag.hasPendingAttributeValue);
        assertEquals("container", startTag.attributes.get("class"));
    }
    
    @Test
    public void testEndTagName() {
        endTag.tagName = "div";
        
        String tagName = endTag.name();
        
        assertEquals("div", tagName);
    }
    
    @Test
    public void testCharsetName() {
        character.data = "Hello";
        
        String data = character.getData();
        
        assertEquals("Hello", data);
    }
    
    @Test
    public void testCDataToString() {
        cdata.data("This is CDATA");
        
        String cdataString = cdata.toString();
        
        assertEquals("<![CDATA[This is CDATA]]>", cdataString);
    }
    
    @Test
    public void testEOFToString() {
        String eofString = eof.toString();
        
        assertEquals("", eofString);
    }
    
}