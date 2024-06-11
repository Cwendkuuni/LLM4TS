import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    private static Token.Doctype doctype;
    private static Token.StartTag startTag;
    private static Token.EndTag endTag;
    private static Token.Comment comment;
    private static Token.Character character;
    private static Token.EOF eof;

    @BeforeClass
    public static void setUp() {
        doctype = new Token.Doctype();
        startTag = new Token.StartTag();
        endTag = new Token.EndTag();
        comment = new Token.Comment();
        character = new Token.Character();
        eof = new Token.EOF();
    }

    @Test
    public void testDoctypeReset() {
        // Typical case
        doctype.name.append("html");
        doctype.pubSysKey = "-//W3C//DTD HTML 4.01 Transitional//EN";
        doctype.publicIdentifier.append("http://www.w3.org/TR/html4/loose.dtd");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/loose.dtd");
        doctype.forceQuirks = true;

        Token resetToken = doctype.reset();

        assertEquals("", resetToken.getName());
        assertNull(resetToken.getPubSysKey());
        assertEquals("", resetToken.getPublicIdentifier());
        assertEquals("", resetToken.getSystemIdentifier());
        assertFalse(resetToken.isForceQuirks());
    }

    @Test
    public void testStartTagReset() {
        // Typical case
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "class";
        startTag.pendingAttributeValue.append("container");
        startTag.selfClosing = true;

        Token resetToken = startTag.reset();

        assertNull(resetToken.tagName);
        assertNull(resetToken.normalName);
        assertNull(resetToken.pendingAttributeName);
        assertEquals(0, resetToken.pendingAttributeValue.length());
        assertFalse(resetToken.selfClosing);
        assertNull(resetToken.attributes);
    }

    @Test
    public void testEndTagReset() {
        // Typical case
        endTag.tagName = "div";
        endTag.normalName = "div";

        Token resetToken = endTag.reset();

        assertNull(resetToken.tagName);
        assertNull(resetToken.normalName);
    }

    @Test
    public void testCommentReset() {
        // Typical case
        comment.data.append("This is a comment.");
        comment.bogus = true;

        Token resetToken = comment.reset();

        assertEquals("", resetToken.getData());
        assertFalse(resetToken.bogus);
    }

    @Test
    public void testCharacterReset() {
        // Typical case
        character.data = "This is some character data.";

        Token resetToken = character.reset();

        assertNull(resetToken.getData());
    }

    @Test
    public void testEOFReset() {
        Token resetToken = eof.reset();

        assertNotNull(resetToken);
    }

}