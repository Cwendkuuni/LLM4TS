import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.jsoup.parser.Token.*;

import static org.junit.Assert.*;

public class TokenTest {

    private Doctype doctype;
    private StartTag startTag;
    private EndTag endTag;
    private Comment comment;
    private Character character;
    private EOF eof;

    @Before
    public void setUp() {
        doctype = new Doctype();
        startTag = new StartTag();
        endTag = new EndTag();
        comment = new Comment();
        character = new Character("test");
        eof = new EOF();
    }

    @Test
    public void testToken_getTokenClass() {
        assertEquals("Doctype", doctype.tokenType());
    }

    @Test
    public void testToken_Doctype_attributesEmpty() {
        assertEquals(0, doctype.attributes.size());
    }

    @Test
    public void testToken_Doctype_getName() {
        doctype.name.append("testName");
        assertEquals("testName", doctype.getName());
    }

    @Test
    public void testToken_Doctype_getPublicIdentifier() {
        doctype.publicIdentifier.append("testPublicIdentifier");
        assertEquals("testPublicIdentifier", doctype.getPublicIdentifier());
    }

    @Test
    public void testToken_Doctype_getSystemIdentifier() {
        doctype.systemIdentifier.append("testSystemIdentifier");
        assertEquals("testSystemIdentifier", doctype.getSystemIdentifier());
    }

    @Test
    public void testToken_Doctype_isForceQuirks() {
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testToken_StartTag_name() {
        startTag.tagName = "testTagName";
        assertEquals("testTagName", startTag.name());
    }

    @Test
    public void testToken_StartTag_isSelfClosing() {
        assertFalse(startTag.isSelfClosing());
    }

    @Test
    public void testToken_StartTag_getAttributes_withAttributes() {
        Attribute attribute = new Attribute("testAttributeName", "testAttributeValue");
        startTag.attributes.put(attribute);
        assertEquals(1, startTag.attributes.size());
    }

    @Test
    public void testToken_StartTag_getAttributes_withoutAttributes() {
        assertNull(startTag.getAttributes());
    }

    @Test
    public void testToken_StartTag_appendTagName() {
        startTag.appendTagName("testAppendTag");
        assertEquals("testAppendTag", startTag.tagName);
    }

    @Test
    public void testToken_StartTag_appendAttributeName() {
        startTag.appendAttributeName("testAppendAttributeName");
        assertEquals("testAppendAttributeName", startTag.pendingAttributeName);
    }

    @Test
    public void testToken_StartTag_appendAttributeValue() {
        startTag.appendAttributeValue("testAppendAttributeValue");
        assertEquals("testAppendAttributeValue", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testToken_EndTag_toString() {
        endTag.tagName = "testTagName";
        assertEquals("</testTagName>", endTag.toString());
    }

    @Test
    public void testToken_Comment_getData() {
        comment.data.append("testData");
        assertEquals("testData", comment.getData());
    }

    @Test
    public void testToken_Comment_toString() {
        comment.data.append("testData");
        assertEquals("<!--testData-->", comment.toString());
    }

    @Test
    public void testToken_Character_getData() {
        assertEquals("test", character.getData());
    }

    @Test
    public void testToken_Character_toString() {
        assertEquals("test", character.toString());
    }

    @Test
    public void testToken_isDoctype() {
        assertTrue(doctype.isDoctype());
        assertFalse(startTag.isDoctype());
        assertFalse(endTag.isDoctype());
        assertFalse(comment.isDoctype());
        assertFalse(character.isDoctype());
    }

    @Test
    public void testToken_asDoctype() {
        assertEquals(doctype, doctype.asDoctype());
        assertNull(startTag.asDoctype());
        assertNull(endTag.asDoctype());
        assertNull(comment.asDoctype());
        assertNull(character.asDoctype());
    }

    @Test
    public void testToken_isStartTag() {
        assertFalse(doctype.isStartTag());
        assertTrue(startTag.isStartTag());
        assertFalse(endTag.isStartTag());
        assertFalse(comment.isStartTag());
        assertFalse(character.isStartTag());
    }

    @Test
    public void testToken_asStartTag() {
        assertNull(doctype.asStartTag());
        assertEquals(startTag, startTag.asStartTag());
        assertNull(endTag.asStartTag());
        assertNull(comment.asStartTag());
        assertNull(character.asStartTag());
    }

    @Test
    public void testToken_isEndTag() {
        assertFalse(doctype.isEndTag());
        assertFalse(startTag.isEndTag());
        assertTrue(endTag.isEndTag());
        assertFalse(comment.isEndTag());
        assertFalse(character.isEndTag());
    }

    @Test
    public void testToken_asEndTag() {
        assertNull(doctype.asEndTag());
        assertNull(startTag.asEndTag());
        assertEquals(endTag, endTag.asEndTag());
        assertNull(comment.asEndTag());
        assertNull(character.asEndTag());
    }

    @Test
    public void testToken_isComment() {
        assertFalse(doctype.isComment());
        assertFalse(startTag.isComment());
        assertFalse(endTag.isComment());
        assertTrue(comment.isComment());
        assertFalse(character.isComment());
    }

    @Test
    public void testToken_asComment() {
        assertNull(doctype.asComment());
        assertNull(startTag.asComment());
        assertNull(endTag.asComment());
        assertEquals(comment, comment.asComment());
        assertNull(character.asComment());
    }

    @Test
    public void testToken_isCharacter() {
        assertFalse(doctype.isCharacter());
        assertFalse(startTag.isCharacter());
        assertFalse(endTag.isCharacter());
        assertFalse(comment.isCharacter());
        assertTrue(character.isCharacter());
    }

    @Test
    public void testToken_asCharacter() {
        assertNull(doctype.asCharacter());
        assertNull(startTag.asCharacter());
        assertNull(endTag.asCharacter());
        assertNull(comment.asCharacter());
        assertEquals(character, character.asCharacter());
    }

    @Test
    public void testToken_isEOF() {
        assertFalse(doctype.isEOF());
        assertFalse(startTag.isEOF());
        assertFalse(endTag.isEOF());
        assertFalse(comment.isEOF());
        assertFalse(character.isEOF());
        assertTrue(eof.isEOF());
    }
}