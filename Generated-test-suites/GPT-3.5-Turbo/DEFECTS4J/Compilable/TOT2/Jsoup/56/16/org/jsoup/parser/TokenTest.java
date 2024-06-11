package org.jsoup.parser;
import static org.junit.Assert.*;
import org.junit.Test;

public class TokenTest {
    
    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());
    }
    
    @Test
    public void testDoctypeReset() {
        Token token = new Token.Doctype();
        ((Token.Doctype) token).name.append("test");
        token.reset();
        assertEquals("", ((Token.Doctype) token).name.toString());
    }
    
    @Test
    public void testTagNewAttribute() {
        Token.Tag tag = new Token.StartTag();
        tag.appendAttributeName("test");
        tag.newAttribute();
        assertNotNull(tag.getAttributes().get("test"));
    }
    
    @Test
    public void testCharacterData() {
        Token token = new Token.Character();
        ((Token.Character) token).data("Test Data");
        assertEquals("Test Data", ((Token.Character) token).getData());
    }

    // Add more test cases for other public methods and scenarios

}