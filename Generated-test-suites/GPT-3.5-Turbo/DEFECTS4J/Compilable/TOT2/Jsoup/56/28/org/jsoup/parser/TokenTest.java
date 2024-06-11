package org.jsoup.parser;
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token.Character();
        assertEquals("Character", token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("name");
        doctype.publicIdentifier.append("public id");
        doctype.systemIdentifier.append("system id");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.name.toString());
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }

    // Continue adding test cases for other methods and scenarios...

}