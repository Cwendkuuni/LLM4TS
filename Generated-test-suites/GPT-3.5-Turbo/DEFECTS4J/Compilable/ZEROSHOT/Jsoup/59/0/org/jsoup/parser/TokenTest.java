package org.jsoup.parser;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.jsoup.parser.Token.*;

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
    public void testDoctype_reset() {
        doctype.name.append("name");
        doctype.pubSysKey = "pubSysKey";
        doctype.publicIdentifier.append("publicIdentifier");
        doctype.systemIdentifier.append("systemIdentifier");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.name.toString());
        assertEquals(null, doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertEquals(false, doctype.forceQuirks);
    }

    @Test
    public void testDoctype_getName() {
        doctype.name.append("name");

        String name = doctype.getName();

        assertEquals("name", name);
    }

    @Test
    public void testDoctype_getPubSysKey() {
        doctype.pubSysKey = "pubSysKey";

        String pubSysKey = doctype.getPubSysKey();

        assertEquals("pubSysKey", pubSysKey);
    }

    @Test
    public void testDoctype_getPublicIdentifier() {
        doctype.publicIdentifier.append("publicIdentifier");

        String publicIdentifier = doctype.getPublicIdentifier();

        assertEquals("publicIdentifier", publicIdentifier);
    }

    @Test
    public void testDoctype_getSystemIdentifier() {
        doctype.systemIdentifier.append("systemIdentifier");

        String systemIdentifier = doctype.getSystemIdentifier();

        assertEquals("systemIdentifier", systemIdentifier);
    }

    @Test
    public void testDoctype_isForceQuirks() {
        doctype.forceQuirks = true;

        boolean forceQuirks = doctype.isForceQuirks();

        assertEquals(true, forceQuirks);
    }
  
    // More test methods for other classes in Token
    
}