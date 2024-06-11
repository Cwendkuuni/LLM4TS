import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("Test Name");
        doctype.pubSysKey = "Test PubSysKey";
        doctype.publicIdentifier.append("Test Public Identifier");
        doctype.systemIdentifier.append("Test System Identifier");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.getName());
        assertNull(doctype.pubSysKey);
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character();
        character.data("Test Data");

        assertEquals("Test Data", character.getData());
    }

    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.attributes = new org.jsoup.nodes.Attributes();
        startTag.nameAttr("Test Name", startTag.attributes);

        startTag.reset();

        assertNull(startTag.tagName);
        assertNull(startTag.attributes);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.selfClosing);
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("Test Data");
        comment.bogus = true;

        comment.reset();

        assertEquals("", comment.getData());
        assertFalse(comment.bogus);
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("TestName");

        assertEquals("</TestName>", endTag.toString());
    }

    // Additional test cases for edge cases and error scenarios could be added here

}