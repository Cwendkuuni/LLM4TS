import org.junit.*;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        assertEquals("Token", token.tokenType());
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "KEY";
        doctype.publicIdentifier.append("PUBLIC");
        doctype.systemIdentifier.append("SYSTEM");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.name.toString());
        assertNull(doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");

        String name = doctype.getName();

        assertEquals("html", name);
    }

    @Test
    public void testDoctypeGetPubSysKey() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.pubSysKey = "KEY";

        String pubSysKey = doctype.getPubSysKey();

        assertEquals("KEY", pubSysKey);
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("PUBLIC");

        String publicIdentifier = doctype.getPublicIdentifier();

        assertEquals("PUBLIC", publicIdentifier);
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("SYSTEM");

        String systemIdentifier = doctype.getSystemIdentifier();

        assertEquals("SYSTEM", systemIdentifier);
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;

        boolean isForceQuirks = doctype.isForceQuirks();

        assertTrue(isForceQuirks);
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.Tag();

        tag.tagName = "div";
        tag.normalName = "div";
        tag.pendingAttributeName = "id";
        tag.pendingAttributeValue.append("1234");
        tag.pendingAttributeValueS = "5678";
        tag.hasEmptyAttributeValue = true;
        tag.hasPendingAttributeValue = true;
        tag.selfClosing = true;
        tag.attributes = new Attributes();

        tag.reset();

        assertNull(tag.tagName);
        assertNull(tag.normalName);
        assertNull(tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue.toString());
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
        assertFalse(tag.selfClosing);
        assertNull(tag.attributes);
    }

    @Test
    public void testTagNewAttribute() {
        Token.Tag tag = new Token.Tag();

        tag.attributes = null;
        tag.pendingAttributeName = "id";
        tag.pendingAttributeValue.append("1234");
        tag.pendingAttributeValueS = "5678";
        tag.hasPendingAttributeValue = true;
        tag.hasEmptyAttributeValue = true;

        tag.newAttribute();

        assertNotNull(tag.attributes);
        assertNull(tag.pendingAttributeName);
        assertEquals("", tag.pendingAttributeValue.toString());
        assertNull(tag.pendingAttributeValueS);
        assertFalse(tag.hasEmptyAttributeValue);
        assertFalse(tag.hasPendingAttributeValue);
    }

    @Test
    public void testTagFinaliseTag() {
        Token.Tag tag = new Token.Tag();

        tag.pendingAttributeName = "id";
        tag.newAttribute();

        tag.finaliseTag();

        assertNull(tag.pendingAttributeName);
    }

    @Test
    public void testTagName() {
        Token.Tag tag = new Token.Tag();
        tag.tagName = "div";

        String name = tag.name();

        assertEquals("div", name);
    }

    @Test
    public void testNormalName() {
        Token.Tag tag = new Token.Tag();
        tag.normalName = "div";

        String name = tag.normalName();

        assertEquals("div", name);
    }

    @Test
    public void testIsSelfClosing() {
        Token.Tag tag = new Token.Tag();
        tag.selfClosing = true;

        boolean isSelfClosing = tag.isSelfClosing();

        assertTrue(isSelfClosing);
    }

    @Test
    public void testGetAttributes() {
        Token.Tag tag = new Token.Tag();
        tag.attributes = new Attributes();

        Attributes attributes = tag.getAttributes();

        assertNotNull(attributes);
    }

    @Test
    public void testAppendTagNameString() {
        Token.Tag tag = new Token.Tag();
        tag.tagName = "di";

        tag.appendTagName("v");

        assertEquals("div", tag.tagName);
    }

    @Test
    public void testAppendTagNameChar() {
        Token.Tag tag = new Token.Tag();
        tag.tagName = "di";

        tag.appendTagName('v');

        assertEquals("div", tag.tagName);
    }

    @Test
    public void testAppendAttributeNameString() {
        Token.Tag tag = new Token.Tag();
        tag.pendingAttributeName = "id";

        tag.appendAttributeName("x");

        assertEquals("idx", tag.pendingAttributeName);
    }

    @Test
    public void testAppendAttributeNameChar() {
        Token.Tag tag = new Token.Tag();
        tag.pendingAttributeName = "id";

        tag.appendAttributeName('x');

        assertEquals("idx", tag.pendingAttributeName);
    }

    @Test
    public void testAppendAttributeValueString() {
        Token.Tag tag = new Token.Tag();
        tag.pendingAttributeValue.append("123");

        tag.appendAttributeValue("456");

        assertEquals("123456", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testAppendAttributeValueChar() {
        Token.Tag tag = new Token.Tag();
        tag.pendingAttributeValue.append("123");

        tag.appendAttributeValue('4');

        assertEquals("1234", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testAppendAttributeValueCharArray() {
        Token.Tag tag = new Token.Tag();
        tag.pendingAttributeValue.append("123");

        tag.appendAttributeValue(new char[]{'4', '5', '6'});

        assertEquals("123456", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testAppendAttributeValueIntArray() {
        Token.Tag tag = new Token.Tag();
        tag.pendingAttributeValue.append("123");

        tag.appendAttributeValue(new int[]{52, 53, 54});

        assertEquals("123456", tag.pendingAttributeValue.toString());
    }

    @Test
    public void testSetEmptyAttributeValue() {
        Token.Tag tag = new Token.Tag();

        tag.setEmptyAttributeValue();

        assertTrue(tag.hasEmptyAttributeValue);
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        character.data = "test";

        character.reset();

        assertNull(character.data);
    }

    @Test
    public void testCharacterData() {
        Token.Character character = new Token.Character();

        character.data("test");

        assertEquals("test", character.data);
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character();
        character.data = "test";

        String data = character.getData();

        assertEquals("test", data);
    }

    @Test
    public void testCDataToString() {
        Token.CData cdata = new Token.CData("test");

        String string = cdata.toString();

        assertEquals("<![CDATA[test]]>", string);
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        eof.reset();

        assertNotNull(eof);
    }
}