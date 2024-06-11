import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
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
        character = new Token.Character();
        eof = new Token.EOF();
    }

    @Test
    public void reset_Doctype_shouldResetAllFields() {
        doctype.name.append("test");
        doctype.pubSysKey = "key";
        doctype.publicIdentifier.append("pub");
        doctype.systemIdentifier.append("sys");
        doctype.forceQuirks = true;

        doctype.reset();

        assertTrue(doctype.name.toString().isEmpty());
        assertNull(doctype.pubSysKey);
        assertTrue(doctype.publicIdentifier.toString().isEmpty());
        assertTrue(doctype.systemIdentifier.toString().isEmpty());
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void getName_Doctype_shouldReturnCorrectName() {
        doctype.name.append("test");

        assertEquals("test", doctype.getName());
    }

    @Test
    public void getPubSysKey_Doctype_shouldReturnCorrectPubSysKey() {
        doctype.pubSysKey = "key";

        assertEquals("key", doctype.getPubSysKey());
    }

    @Test
    public void getPublicIdentifier_Doctype_shouldReturnCorrectPublicIdentifier() {
        doctype.publicIdentifier.append("pub");


        assertEquals("pub", doctype.getPublicIdentifier());
    }

    @Test
    public void getSystemIdentifier_Doctype_shouldReturnCorrectSystemIdentifier() {
        doctype.systemIdentifier.append("sys");

        assertEquals("sys", doctype.getSystemIdentifier());
    }

    @Test
    public void isForceQuirks_Doctype_shouldReturnCorrectForceQuirksValue() {
        doctype.forceQuirks = true;

        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void reset_StartTag_shouldResetAllFields() {
        startTag.tagName = "test";
        startTag.normalName = "test";
        startTag.pendingAttributeName = "attr";
        startTag.pendingAttributeValue.append("value");
        startTag.pendingAttributeValueS = "value";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();

        startTag.reset();

        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertTrue(startTag.pendingAttributeValue.toString().isEmpty());
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);
    }

    @Test
    public void newAttribute_StartTag_shouldAddNewAttributeToAttributes() {
        startTag.attributes = null;
        startTag.pendingAttributeName = "attr";
        startTag.pendingAttributeValue.append("value");

        startTag.newAttribute();

        assertNotNull(startTag.attributes);
        assertEquals(startTag.pendingAttributeValue.toString(), startTag.attributes.get("attr"));
    }

    @Test
    public void finaliseTag_StartTag_shouldCallNewAttribute() {
        startTag.pendingAttributeName = "attr";

        startTag.finaliseTag();

        assertNotNull(startTag.attributes);
    }

    @Test
    public void name_StartTag_shouldReturnCorrectName() {
        startTag.tagName = "test";

        assertEquals("test", startTag.name());
    }

    @Test
    public void normalName_StartTag_shouldReturnCorrectNormalName() {
        startTag.normalName = "test";

        assertEquals("test", startTag.normalName());
    }

    @Test
    public void isSelfClosing_StartTag_shouldReturnCorrectSelfClosingValue() {
        startTag.selfClosing = true;

        assertTrue(startTag.isSelfClosing());
    }

    @Test
    public void getAttributes_StartTag_shouldReturnCorrectAttributes() {
        Attributes attributes = new Attributes();
        startTag.attributes = attributes;

        assertSame(attributes, startTag.getAttributes());
    }

    @Test
    public void appendTagName_StartTag_shouldAppendTagNameCorrectly() {
        startTag.tagName = "test";

        startTag.appendTagName("123");

        assertEquals("test123", startTag.tagName);
        assertEquals("test123", startTag.normalName);
    }

    @Test
    public void appendAttributeName_StartTag_shouldAppendAttributeNameCorrectly() {
        startTag.pendingAttributeName = "attr";

        startTag.appendAttributeName("123");

        assertEquals("attr123", startTag.pendingAttributeName);
    }

    @Test
    public void appendAttributeValue_StartTag_shouldAppendAttributeValueCorrectly() {
        startTag.pendingAttributeValue.append("value");

        startTag.appendAttributeValue("123");

        assertEquals("value123", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void appendAttributeValue_StartTag_shouldSetPendingAttributeValueCorrectly() {
        startTag.pendingAttributeValueS = "value";

        startTag.appendAttributeValue("123");

        assertNull(startTag.pendingAttributeValueS);
    }

    @Test
    public void setEmptyAttributeValue_StartTag_shouldSetEmptyAttributeValueToTrue() {
        startTag.setEmptyAttributeValue();

        assertTrue(startTag.hasEmptyAttributeValue);
    }

    @Test
    public void ensureAttributeValue_StartTag_shouldSetHasPendingAttributeValueToTrue() {
        startTag.ensureAttributeValue();

        assertTrue(startTag.hasPendingAttributeValue);
    }

    @Test
    public void ensureAttributeValue_StartTag_shouldAppendAttributeValueSCorrectly() {
        startTag.pendingAttributeValueS = "value";

        startTag.ensureAttributeValue();

        assertEquals("value", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
    }

    @Test
    public void reset_EndTag_shouldResetAllFields() {
        endTag.tagName = "test";
        endTag.normalName = "test";
        endTag.pendingAttributeName = "attr";
        endTag.pendingAttributeValue.append("value");
        endTag.pendingAttributeValueS = "value";
        endTag.hasEmptyAttributeValue = true;
        endTag.hasPendingAttributeValue = true;
        endTag.selfClosing = true;
        endTag.attributes = new Attributes();

        endTag.reset();

        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
        assertNull(endTag.pendingAttributeName);
        assertTrue(endTag.pendingAttributeValue.toString().isEmpty());
        assertNull(endTag.pendingAttributeValueS);
        assertFalse(endTag.hasEmptyAttributeValue);
        assertFalse(endTag.hasPendingAttributeValue);
        assertFalse(endTag.selfClosing);
        assertNull(endTag.attributes);
    }

    @Test
    public void toString_StartTag_shouldReturnCorrectStringRepresentationWithAttributes() {
        startTag.tagName = "test";
        startTag.attributes = new Attributes();
        startTag.attributes.put("attr", "value");

        assertEquals("<test attr=\"value\">", startTag.toString());
    }

    @Test
    public void toString_StartTag_shouldReturnCorrectStringRepresentationWithoutAttributes() {
        startTag.tagName = "test";
        startTag.attributes = null;

        assertEquals("<test>", startTag.toString());
    }

    @Test
    public void toString_EndTag_shouldReturnCorrectStringRepresentation() {
        endTag.tagName = "test";

        assertEquals("</test>", endTag.toString());
    }

    @Test
    public void reset_Comment_shouldResetAllFields() {
        comment.data.append("test");
        comment.bogus = true;

        comment.reset();

        assertTrue(comment.data.toString().isEmpty());
        assertFalse(comment.bogus);
    }

    @Test
    public void getData_Comment_shouldReturnCorrectData() {
        comment.data.append("test");

        assertEquals("test", comment.getData());
    }

    @Test
    public void toString_Comment_shouldReturnCorrectStringRepresentation() {
        comment.data.append("test");

        assertEquals("<!--test-->", comment.toString());
    }

    @Test
    public void reset_Character_shouldResetData() {
        character.data = "test";

        character.reset();

        assertNull(character.data);
    }

    @Test
    public void data_Character_shouldSetDataCorrectly() {
        character.data("test");

        assertEquals("test", character.getData());
    }

    @Test
    public void toString_Character_shouldReturnCorrectStringRepresentation() {
        character.data = "test";

        assertEquals("test", character.toString());
    }

    @Test
    public void reset_EOF_shouldResetAllFields() {
        eof.reset();

        assertNotNull(eof);
    }
}