import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.Tag;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.helper.Validate;

public class TokenTest {

    @Test
    public void testDoctype_reset() {
        Doctype doctype = new Doctype();
        doctype.reset();
        assertTrue(doctype.name().isEmpty());
        assertTrue(doctype.getPublicIdentifier().isEmpty());
        assertTrue(doctype.getSystemIdentifier().isEmpty());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testDoctype_getName() {
        Doctype doctype = new Doctype();
        doctype.getName("html");
        assertEquals("html", doctype.name());
    }

    @Test
    public void testDoctype_getPublicIdentifier() {
        Doctype doctype = new Doctype();
        doctype.getPublicIdentifier().append("public");
        assertEquals("public", doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctype_getSystemIdentifier() {
        Doctype doctype = new Doctype();
        doctype.getSystemIdentifier().append("system");
        assertEquals("system", doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctype_isForceQuirks() {
        Doctype doctype = new Doctype();
        doctype.isForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
    }

    @Test(expected = AssertionError.class)
    public void testDoctype_getName_Null() {
        Doctype doctype = new Doctype();
        doctype.getName(null);
    }

    @Test(expected = AssertionError.class)
    public void testDoctype_getPublicIdentifier_Null() {
        Doctype doctype = new Doctype();
        doctype.getPublicIdentifier().setLength(0);
        doctype.getPublicIdentifier().append(null);
    }

    @Test(expected = AssertionError.class)
    public void testDoctype_getSystemIdentifier_Null() {
        Doctype doctype = new Doctype();
        doctype.getSystemIdentifier().setLength(0);
        doctype.getSystemIdentifier().append(null);
    }

    @Test
    public void testTag_reset() {
        Tag tag = new Tag();
        tag.reset();
        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        tag.reset(tag.pendingAttributeValue);
        assertTrue(tag.pendingAttributeValue.length() == 0);
        tag.pendingAttributeValueS = "test";
        assertTrue(tag.pendingAttributeValueS != null);
        tag.reset();
        assertNull(tag.pendingAttributeValueS);
    }

    @Test
    public void testTag_newAttribute() {
        Tag tag = new Tag();
        tag.pendingAttributeName = "test";
        tag.newAttribute();
        assertNotNull(tag.attributes);
        assertEquals(1, tag.attributes.size());
        Attribute attribute = tag.attributes.get("test");
        assertEquals("test", attribute.getName());
        assertEquals("", attribute.getValue());
    }

    @Test
    public void testTag_finaliseTag() {
        Tag tag = new Tag();
        tag.pendingAttributeName = "test";
        tag.newAttribute();
        tag.finaliseTag();
        assertNull(tag.pendingAttributeName);
        assertNull(tag.pendingAttributeValue);
        assertNull(tag.pendingAttributeValueS);
        assertNotNull(tag.attributes);
        assertEquals(1, tag.attributes.size());
    }

    @Test
    public void testTag_name() {
        Tag tag = new Tag();
        tag.name("test");
        assertEquals("test", tag.name());
    }

    @Test
    public void testTag_name_Null() {
        Tag tag = new Tag();
        tag.name(null);
        assertNull(tag.tagName);
    }

    @Test
    public void testTag_appendTagName() {
        Tag tag = new Tag();
        tag.appendTagName("test");
        assertEquals("test", tag.tagName);
        tag.appendTagName("test2");
        assertEquals("testtest2", tag.tagName);
    }

    @Test
    public void testTag_appendTagName_Char() {
        Tag tag = new Tag();
        tag.appendTagName('t');
        tag.appendTagName('e');
        tag.appendTagName('s');
        tag.appendTagName('t');
        assertEquals("test", tag.tagName);
    }

    @Test
    public void testTag_appendAttributeName() {
        Tag tag = new Tag();
        tag.appendAttributeName("test");
        assertEquals("test", tag.pendingAttributeName);
    }

    @Test
    public void testTag_appendAttributeName_Char() {
        Tag tag = new Tag();
        tag.appendAttributeName('t');
        tag.appendAttributeName('e');
        tag.appendAttributeName('s');
        tag.appendAttributeName('t');
        assertEquals("test", tag.pendingAttributeName);
    }

    @Test
    public void testTag_appendAttributeValue() {
        Tag tag = new Tag();
        tag.appendAttributeValue("test");
        assertEquals("test", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testTag_appendAttributeValue_Char() {
        Tag tag = new Tag();
        tag.appendAttributeValue('t');
        tag.appendAttributeValue('e');
        tag.appendAttributeValue('s');
        tag.appendAttributeValue('t');
        assertEquals("test", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testTag_appendAttributeValue_CharArray() {
        Tag tag = new Tag();
        char[] charArray = {'t', 'e', 's', 't'};
        tag.appendAttributeValue(charArray);
        assertEquals("test", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testTag_appendAttributeValue_IntArray() {
        Tag tag = new Tag();
        int[] intArray = {97, 101, 115, 116};
        tag.appendAttributeValue(intArray);
        assertEquals("test", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testTag_setEmptyAttributeValue() {
        Tag tag = new Tag();
        tag.setEmptyAttributeValue();
        assertTrue(tag.hasEmptyAttributeValue);
    }

    @Test
    public void testTag_ensureAttributeValue() {
        Tag tag = new Tag();
        tag.pendingAttributeValueS = "test";
        tag.ensureAttributeValue();
        assertEquals("test", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testStartTag_reset() {
        StartTag startTag = new StartTag();
        startTag.reset();
        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertNull(startTag.pendingAttributeValue);
        assertNull(startTag.pendingAttributeValueS);
        assertNull(startTag.hasEmptyAttributeValue);
        assertNull(startTag.hasPendingAttributeValue);
        assertNull(startTag.attributes);
    }

    @Test
    public void testStartTag_nameAttr() {
        StartTag startTag = new StartTag();
        startTag.nameAttr("test", null);
        assertEquals("test", startTag.tagName);
        assertEquals("test", startTag.normalName);
    }

    @Test(expected = AssertionError.class)
    public void testStartTag_nameAttr_NullName() {
        StartTag startTag = new StartTag();
        startTag.nameAttr(null, null);
    }

    @Test(expected = AssertionError.class)
    public void testStartTag_nameAttr_NullAttributes() {
        StartTag startTag = new StartTag();
        startTag.nameAttr("test", null);
        Attributes attributes = new Attributes();
        startTag.nameAttr("test", attributes);
        startTag.reset();
        assertNotNull(startTag.attributes);
        assertEquals(0, startTag.attributes.size());
    }

    @Test
    public void testStartTag_toString() {
        StartTag startTag = new StartTag();
        startTag.nameAttr("test", null);
        assertEquals("<test>", startTag.toString());
    }

    @Test
    public void testEndTag_reset() {
        EndTag endTag = new EndTag();
        endTag.reset();
        assertEquals("", endTag.tagName);
        assertNull(endTag.normalName);
        assertNull(endTag.pendingAttributeName);
        assertNull(endTag.pendingAttributeValue);
        assertNull(endTag.pendingAttributeValueS);
        assertNull(endTag.hasEmptyAttributeValue);
        assertNull(endTag.hasPendingAttributeValue);
        assertNull(endTag.attributes);
    }

    @Test
    public void testEndTag_toString() {
        EndTag endTag = new EndTag();
        assertEquals("</test>", endTag.toString());
    }

    @Test
    public void testComment_reset() {
        Comment comment = new Comment();
        comment.reset();
        assertTrue(comment.bogus == false);
        assertTrue(comment.data.length() == 0);
    }

    @Test
    public void testComment_getData() {
        Comment comment = new Comment();
        comment.getData().append("test");
        assertEquals("test", comment.getData().toString());
    }

    @Test
    public void testComment_toString() {
        Comment comment = new Comment();
        comment.getData().append("test");
        assertEquals("<!--test-->", comment.toString());
    }

    @Test
    public void testCharacter_reset() {
        Character character = new Character();
        character.reset();
        assertNull(character.getData());
    }

    @Test
    public void testCharacter_getData() {
        Character character = new Character();
        character.getData("test");
        assertEquals("test", character.getData().toString());
    }

    @Test
    public void testCharacter_toString() {
        Character character = new Character();
        character.getData("test");
        assertEquals("test", character.toString());
    }

    @Test
    public void testEOF_reset() {
        EOF eof = new EOF();
        eof.reset();
    }
}