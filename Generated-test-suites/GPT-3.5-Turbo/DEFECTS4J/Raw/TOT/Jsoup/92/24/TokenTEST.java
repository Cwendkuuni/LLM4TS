import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.Token.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TokenTest {
    
    private Doctype doctypeToken;
    private StartTag startTagToken;
    private EndTag endTagToken;
    private Comment commentToken;
    private Character characterToken;
    private CData cdataToken;
    private EOF eofToken;
    
    @Before
    public void setUp() {
        doctypeToken = new Doctype();
        startTagToken = new StartTag();
        endTagToken = new EndTag();
        commentToken = new Comment();
        characterToken = new Character();
        cdataToken = new CData();
        eofToken = new EOF();
    }
    
    @Test
    public void testDoctypeReset() {
        doctypeToken.reset();
        assertEquals("", doctypeToken.getName());
        assertEquals(null, doctypeToken.getPubSysKey());
        assertEquals("", doctypeToken.getPublicIdentifier());
        assertEquals("", doctypeToken.getSystemIdentifier());
        assertFalse(doctypeToken.isForceQuirks());
    }
    
    @Test
    public void testStartTagReset() {
        startTagToken.reset();
        assertEquals(null, startTagToken.tagName);
        assertEquals(null, startTagToken.normalName);
    }
    
    @Test
    public void testEndTagReset() {
        endTagToken.reset();
        assertEquals(null, endTagToken.tagName);
        assertEquals(null, endTagToken.normalName);
    }
    
    @Test
    public void testCommentReset() {
        commentToken.reset();
        assertEquals("", commentToken.getData());
        assertFalse(commentToken.bogus);
    }
    
    @Test
    public void testCharacterReset() {
        characterToken.reset();
        assertEquals(null, characterToken.getData());
    }
    
    @Test
    public void testCDataReset() {
        cdataToken.reset();
        assertEquals(null, cdataToken.getData());
    }
    
    @Test
    public void testEOFReset() {
        eofToken.reset();
        assertTrue(eofToken.isEOF());
    }
    
    // Add more test cases for other methods if needed
    
}