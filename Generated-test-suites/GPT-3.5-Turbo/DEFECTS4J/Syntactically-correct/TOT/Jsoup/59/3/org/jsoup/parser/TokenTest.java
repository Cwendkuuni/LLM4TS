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

    // Test cases for Doctype class
    
    @Test
    public void testDoctypeReset() {
        doctype.name.append("HTML");
        doctype.pubSysKey = "PUBLIC";
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01 Transitional//EN");
        
        Token.Doctype resetDoctype = doctype.reset();
        
        assertEquals("", resetDoctype.getName());
        assertNull(resetDoctype.getPubSysKey());
        assertEquals("", resetDoctype.getPublicIdentifier());
        assertEquals("", resetDoctype.getSystemIdentifier());
        assertFalse(resetDoctype.isForceQuirks());
    }
    
    @Test
    public void testDoctypeGetName() {
        doctype.name.append("HTML");
        
        String name = doctype.getName();
        
        assertEquals("HTML", name);
    }
    
    @Test
    public void testDoctypeGetPubSysKey() {
        doctype.pubSysKey = "PUBLIC";
        
        String pubSysKey = doctype.getPubSysKey();
        
        assertEquals("PUBLIC", pubSysKey);
    }
    
    @Test
    public void testDoctypeGetPublicIdentifier() {
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01 Transitional//EN");
        
        String publicIdentifier = doctype.getPublicIdentifier();
        
        assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", publicIdentifier);
    }
    
    @Test
    public void testDoctypeGetSystemIdentifier() {
        doctype.systemIdentifier.append("https://www.example.com/dtd/HTML4.01-transitional.dtd");
        
        String systemIdentifier = doctype.getSystemIdentifier();
        
        assertEquals("https://www.example.com/dtd/HTML4.01-transitional.dtd", systemIdentifier);
    }
    
    @Test
    public void testDoctypeIsForceQuirks() {
        doctype.forceQuirks = true;
        
        assertTrue(doctype.isForceQuirks());
    }
    
    // Test cases for StartTag class
    
    @Test
    public void testStartTagReset() {
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "id";
        startTag.pendingAttributeValue.append("myDiv");
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();
        
        Token.StartTag resetStartTag = startTag.reset();
        
        assertNull(resetStartTag.tagName);
        assertNull(resetStartTag.normalName);
        assertNull(resetStartTag.pendingAttributeName);
        assertEquals("", resetStartTag.pendingAttributeValue.toString());
        assertFalse(resetStartTag.selfClosing);
        assertNull(resetStartTag.attributes);
    }
    
    @Test
    public void testStartTagName() {
        startTag.tagName = "div";
        
        String name = startTag.name();
        
        assertEquals("div", name);
    }
    
    @Test
    public void testStartTagNormalName() {
        startTag.normalName = "div";
        
        String normalName = startTag.normalName();
        
        assertEquals("div", normalName);
    }
    
    @Test
    public void testStartTagNameAttr() {
        startTag.tagName = "div";
        startTag.attributes = new Attributes();
        
        Token.StartTag startTagWithNameAttr = startTag.nameAttr("span", new Attributes());
        
        assertEquals("span", startTagWithNameAttr.tagName);
        assertEquals("span", startTagWithNameAttr.normalName);
        assertNotNull(startTagWithNameAttr.attributes);
    }
    
    // Test cases for EndTag class
    
    @Test
    public void testEndTagReset() {
        endTag.tagName = "div";
        endTag.normalName = "div";
        
        Token.EndTag resetEndTag = endTag.reset();
        
        assertNull(resetEndTag.tagName);
        assertNull(resetEndTag.normalName);
    }
    
    // Test cases for Comment class
    
    @Test
    public void testCommentReset() {
        comment.data.append("This is a comment.");
        comment.bogus = true;
        
        Token.Comment resetComment = comment.reset();
        
        assertEquals("", resetComment.getData());
        assertFalse(resetComment.bogus);
    }
    
    @Test
    public void testCommentGetData() {
        comment.data.append("This is a comment.");
        
        String data = comment.getData();
        
        assertEquals("This is a comment.", data);
    }
    
    // Test cases for Character class
    
    @Test
    public void testCharacterReset() {
        character.data = "A";
        
        Token.Character resetCharacter = character.reset();
        
        assertNull(resetCharacter.getData());
    }
    
    @Test
    public void testCharacterData() {
        Token.Character characterWithData = character.data("A");
        
        assertEquals("A", characterWithData.getData());
    }
    
    // Test cases for EOF class
    
    @Test
    public void testEOFReset() {
        Token.EOF resetEOF = eof.reset();
        
        assertNotNull(resetEOF);
    }

}