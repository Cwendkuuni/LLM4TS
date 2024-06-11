import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/strict.dtd");
        doctype.forceQuirks = false;
        
        assertEquals("Doctype is not html", "html", doctype.getName());
        assertEquals("Public identifier is incorrect", "-//W3C//DTD HTML 4.01//EN", doctype.getPublicIdentifier());
        assertEquals("System identifier is incorrect", "http://www.w3.org/TR/html4/strict.dtd", doctype.getSystemIdentifier());
        assertFalse("Force quirks is not false", doctype.isForceQuirks());
    }

    @Test
    public void testStartTagToken() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        startTag.selfClosing = true;
        startTag.appendAttributeName("id");
        startTag.appendAttributeValue("myDiv");
        startTag.finaliseTag();
        
        assertEquals("Start tag name is incorrect", "div", startTag.name());
        assertTrue("Is self closing is not true", startTag.isSelfClosing());
        assertNotNull("Attributes are null", startTag.getAttributes());
        assertEquals("Attribute name is incorrect", "id", startTag.getAttributes().get("id").getKey());
        assertEquals("Attribute value is incorrect", "myDiv", startTag.getAttributes().get("id").getValue());
    }

    @Test
    public void testEndTagToken() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("div");
        
        assertEquals("End tag name is incorrect", "div", endTag.name());
    }

    @Test
    public void testCommentToken() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("This is a comment");
        
        assertEquals("Comment data is incorrect", "This is a comment", comment.getData());
    }

    @Test
    public void testCharacterToken() {
        Token.Character character = new Token.Character("A");
        
        assertEquals("Character data is incorrect", "A", character.getData());
    }

    @Test
    public void testEOFToken() {
        Token.EOF eof = new Token.EOF();
        
        assertTrue("Is EOF is not true", eof.isEOF());
    }
}