
import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testToken_reset() {
        Token.Doctype doctype = new Token.Doctype();
        Token.Tag tag = new Token.StartTag();
        Token.Comment comment = new Token.Comment();
        Token.Character character = new Token.Character();
        Token.EOF eof = new Token.EOF();

        doctype.name.append("name");
        doctype.publicIdentifier.append("publicIdentifier");
        doctype.systemIdentifier.append("systemIdentifier");
        doctype.forceQuirks = true;

        tag.tagName = "tagName";
        tag.normalName = "normalName";
        tag.pendingAttributeName = "attributeName";
        tag.pendingAttributeValue.append("attributeValue");
        tag.pendingAttributeValueS = "attributeValueS";
        tag.hasEmptyAttributeValue = true;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;

        comment.data.append("commentData");
        comment.bogus = true;

        character.data = "characterData";

        doctype.reset();
        tag.reset();
        comment.reset();
        character.reset();
        eof.reset();

        assertEquals("", doctype.name.toString());
        assertNull(doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);

        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue.toString());
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.selfClosing);
        assertNull(tag.attributes);

        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);

        assertNull(character.data);

        assertTrue(eof.isEOF());
    }

    @Test
    public void testDoctype_Getters() {
        Token.Doctype doctype = new Token.Doctype();

        doctype.name.append("name");
        doctype.pubSysKey = "pubSysKey";
        doctype.publicIdentifier.append("publicIdentifier");
        doctype.systemIdentifier.append("systemIdentifier");
        doctype.forceQuirks = true;

        assertEquals("name", doctype.getName());
        assertEquals("pubSysKey", doctype.getPubSysKey());
        assertEquals("publicIdentifier", doctype.getPublicIdentifier());
        assertEquals("systemIdentifier", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTag_newAttribute() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.newAttribute();
        assertNotNull(startTag.attributes);

        startTag.attributes.put(new Attribute("attributeName", "attributeValue"));
        startTag.pendingAttributeName = "anotherAttributeName";
        startTag.newAttribute();
        assertEquals(2, startTag.attributes.size());
        assertEquals("attributeValue", startTag.attributes.get("attributeName"));
        assertNull(startTag.pendingAttributeName);

        startTag.newAttribute();
        assertEquals(2, startTag.attributes.size());
        assertNull(startTag.pendingAttributeName);
    }

    @Test
    public void testStartTag_finaliseTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.attributes = new Attributes();
        startTag.pendingAttributeName = "attributeName";
        startTag.finaliseTag();
        assertEquals(1, startTag.attributes.size());
        assertEquals("attributeName", startTag.attributes.get(0).getKey());

        startTag.attributes = null;
        startTag.pendingAttributeName = "attributeName";
        startTag.finaliseTag();
        assertNull(startTag.attributes);
        assertNull(startTag.pendingAttributeName);
    }

    @Test
    public void testStartTag_name() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("tagName");
        assertEquals("tagName", startTag.tagName);
        assertEquals("tagname", startTag.normalName);
    }

    @Test
    public void testEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("tagName");
        assertEquals("</tagName>", endTag.toString());
    }

    @Test
    public void testComment_reset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("commentData");
        comment.bogus = true;

        comment.reset();

        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
    }

    @Test
    public void testCharacter_data() {
        Token.Character character = new Token.Character();
        character.data("characterData");
        assertEquals("characterData", character.getData());
    }

    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();
        assertTrue(eof.isEOF());
    }
}
