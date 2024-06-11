package org.jsoup.parser;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {
    
    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("TestName");
        doctype.pubSysKey = "TestPubSysKey";
        doctype.publicIdentifier.append("TestPublicIdentifier");
        doctype.systemIdentifier.append("TestSystemIdentifier");
        
        Token resetToken = doctype.reset();
        assertEquals("", doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagName() {
        Token.Tag tag = new Token.StartTag();
        tag.name("div");
        assertEquals("div", tag.name());
        assertEquals("div", tag.normalName());
    }

    // Add more test cases for other methods and scenarios as needed

}