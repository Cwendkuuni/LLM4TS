import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    // Test cases for tokenType() method
    @Test
    public void testTokenType() {
        Token token = new Token.Doctype();
        assertEquals("Doctype", token.tokenType());

        token = new Token.StartTag();
        assertEquals("StartTag", token.tokenType());

        token = new Token.EndTag();
        assertEquals("EndTag", token.tokenType());

        token = new Token.Comment();
        assertEquals("Comment", token.tokenType());

        token = new Token.Character();
        assertEquals("Character", token.tokenType());

        token = new Token.EOF();
        assertEquals("EOF", token.tokenType());
    }

    // Test cases for Doctype class
    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "PUBLIC";
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.name.toString());
        assertNull(doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testDoctypeGetters() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "PUBLIC";
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        doctype.forceQuirks = true;

        assertEquals("html", doctype.getName());
        assertEquals("PUBLIC", doctype.getPubSysKey());
        assertEquals("-//W3C//DTD HTML 4.01//EN", doctype.getPublicIdentifier());
        assertEquals("http://www.w3.org/TR/html4/strict.dtd", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    // Test cases for StartTag class
    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "class";
        startTag.pendingAttributeValue.append("container");
        startTag.selfClosing = false;
        startTag.attributes = new Attributes();

        startTag.reset();

        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);
    }

    @Test
    public void testStartTagNameAttr() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.nameAttr("div", new Attributes().put("class", "container"));

        assertEquals("div", startTag.tagName);
        assertEquals("div", startTag.normalName);
        assertEquals("class", startTag.pendingAttributeName);
        assertEquals("container", startTag.pendingAttributeValue.toString());
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNotNull(startTag.attributes);
        assertEquals(1, startTag.attributes.size());
        assertEquals("container", startTag.attributes.get("class"));
    }

    // Test cases for EndTag class
    @Test
    public void testEndTagReset() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "div";
        endTag.normalName = "div";

        endTag.reset();

        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
        assertNull(endTag.pendingAttributeName);
        assertEquals("", endTag.pendingAttributeValue.toString());
        assertFalse(endTag.hasEmptyAttributeValue);
        assertFalse(endTag.hasPendingAttributeValue);
        assertFalse(endTag.selfClosing);
        assertNull(endTag.attributes);
    }

    // Test cases for Comment class
    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        comment.bogus = true;

        comment.reset();

        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");

        assertEquals("This is a comment", comment.getData());
    }

    // Test cases for Character class
    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "This is a character";

        character.reset();

        assertNull(character.data);
    }

    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character();
        character.data("This is a character");

        assertEquals("This is a character", character.getData());
    }

    // Test cases for CData class
    @Test
    public void testCData() {
        Token.CData cdata = new Token.CData("This is a CDATA");

        assertEquals("This is a CDATA", cdata.getData());
    }

    // Test case for EOF class
    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        assertSame(eof, eof.reset());
    }

    // Additional test cases for edge cases and exception handling scenarios

    // Test case for token with null type
    @Test
    public void testNullType() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };

        assertNotNull(token.reset());
    }

    // Test case for empty Doctype name
    @Test
    public void testEmptyDoctypeName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("");
        doctype.reset();
        assertEquals("", doctype.getName());
    }

    // Test case for empty StartTag name
    @Test
    public void testEmptyStartTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "";
        startTag.reset();
        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
    }

    // Test case for empty EndTag name
    @Test
    public void testEmptyEndTagName() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "";
        endTag.reset();
        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
    }

    // Test case for empty Comment data
    @Test
    public void testEmptyCommentData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("");
        comment.reset();
        assertEquals("", comment.getData());
    }

    // Test case for empty Character data
    @Test
    public void testEmptyCharacterData() {
        Token.Character character = new Token.Character();
        character.data("");
        character.reset();
        assertNull(character.getData());
    }

    // Test case for null Character data
    @Test
    public void testNullCharacterData() {
        Token.Character character = new Token.Character();
        character.reset();
        assertNull(character.getData());
    }

    // Test case for null CData data
    @Test
    public void testNullCDataData() {
        Token.CData cdata = new Token.CData(null);

        assertNull(cdata.getData());
    }

    

    // Include all the test methods generated above

    
}