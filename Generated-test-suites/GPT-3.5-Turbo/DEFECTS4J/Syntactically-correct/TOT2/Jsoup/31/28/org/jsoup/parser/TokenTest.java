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
        character = new Token.Character("test");
        eof = new Token.EOF();
    }

    @Test
    public void testGetTokenType() {
        assertEquals("Doctype", doctype.tokenType());
        assertEquals("StartTag", startTag.tokenType());
        assertEquals("EndTag", endTag.tokenType());
        assertEquals("Comment", comment.tokenType());
        assertEquals("Character", character.tokenType());
        assertEquals("EOF", eof.tokenType());
    }

    @Test
    public void testDoctypeMethods() {
        // Test Doctype methods
    }

    @Test
    public void testStartTagMethods() {
        // Test StartTag methods
    }

    @Test
    public void testEndTagMethods() {
        // Test EndTag methods
    }

    @Test
    public void testCommentMethods() {
        // Test Comment methods
    }

    @Test
    public void testCharacterMethods() {
        // Test Character methods
    }

    @Test
    public void testEOFMethods() {
        // Test EOF methods
    }

    // Add more test cases for edge cases and error scenarios

}