import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TokenTest {

    @Test
    public void testResetForDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("testName");
        doctype.publicIdentifier.append("testPublicIdentifier");
        doctype.systemIdentifier.append("testSystemIdentifier");
        doctype.forceQuirks = true;

        doctype.reset();

        assertEquals("", doctype.name.toString());
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
    }

    @Test
    public void testGetNameForDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("testName");

        String result = doctype.getName();

        assertEquals("testName", result);
    }

    @Test
    public void testGetPublicIdentifierForDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("testPublicIdentifier");

        String result = doctype.getPublicIdentifier();

        assertEquals("testPublicIdentifier", result);
    }

    @Test
    public void testGetSystemIdentifierForDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("testSystemIdentifier");

        String result = doctype.getSystemIdentifier();

        assertEquals("testSystemIdentifier", result);
    }

    @Test
    public void testIsForceQuirksForDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.forceQuirks = true;

        boolean result = doctype.isForceQuirks();

        assertTrue(result);
    }

    @Test
    public void testResetForStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "testTagName";
        startTag.normalName = "testNormalName";
        startTag.pendingAttributeName = "testAttributeName";
        startTag.pendingAttributeValue.append("testAttributeValue");
        startTag.pendingAttributeValueS = "testAttributeValueS";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();

        startTag.reset();

        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);
    }

    @Test
    public void testNewAttributeForStartTagWithPendingAttributeName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.attributes = new Attributes();
        startTag.pendingAttributeName = "testAttributeName";
        startTag.pendingAttributeValue.append("testAttributeValue");

        startTag.newAttribute();

        assertEquals(new Attribute("testAttributeName", "testAttributeValue"), startTag.attributes.get("testAttributeName"));
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testNewAttributeForStartTagWithEmptyAttributeName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.attributes = new Attributes();
        startTag.pendingAttributeName = "";

        startTag.newAttribute();

        assertEquals(new Attribute("", ""), startTag.attributes.get(""));
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testNewAttributeForStartTagWithBooleanAttribute() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.attributes = new Attributes();
        startTag.pendingAttributeName = "testAttributeName";
        startTag.hasPendingAttributeValue = false;
        startTag.hasEmptyAttributeValue = false;

        startTag.newAttribute();

        assertEquals(new BooleanAttribute("testAttributeName"), startTag.attributes.get("testAttributeName"));
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testFinaliseTagForStartTagWithPendingAttributeName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.pendingAttributeName = "testAttributeName";

        startTag.finaliseTag();

        assertNull(startTag.pendingAttributeName);
    }

    @Test
    public void testNameForStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "testTagName";

        String result = startTag.name();

        assertEquals("testTagName", result);
    }

    @Test
    public void testNormalNameForStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "testTagName";
        startTag.normalName = "testNormalName";

        String result = startTag.normalName();

        assertEquals("testNormalName", result);
    }

    @Test
    public void testAppendTagNameWithStringForStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = null;

        startTag.appendTagName("append");

        assertEquals("append", startTag.tagName);
    }

    @Test
    public void testAppendTagNameWithCharForStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = null;

        startTag.appendTagName('a');

        assertEquals("a", startTag.tagName);
    }

    @Test
    public void testAppendAttributeNameWithStringForStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.pendingAttributeName = null;

        startTag.appendAttributeName("append");

        assertEquals("append", startTag.pendingAttributeName);
    }

    @Test
    public void testAppendAttributeNameWithCharForStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.pendingAttributeName = null;

        startTag.appendAttributeName('a');

        assertEquals("a", startTag.pendingAttributeName);
    }

    @Test
    public void testAppendAttributeValueWithStringForStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.pendingAttributeValue = new StringBuilder();

        startTag.appendAttributeValue("append");

        assertEquals("append", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testAppendAttributeValueWithCharForStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.pendingAttributeValue = new StringBuilder();

        startTag.appendAttributeValue('a');

        assertEquals("a", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testAppendAttributeValueWithCharArrayForStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.pendingAttributeValue = new StringBuilder();

        startTag.appendAttributeValue(new char[]{'a', 'p', 'p', 'e', 'n', 'd'});

        assertEquals("append", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testAppendAttributeValueWithIntArrayForStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.pendingAttributeValue = new StringBuilder();

        startTag.appendAttributeValue(new int[]{97, 112, 112, 101, 110, 100});

        assertEquals("append", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testSetEmptyAttributeValueForStartTag() {
        Token.StartTag startTag = new Token.StartTag();

        startTag.setEmptyAttributeValue();

        assertTrue(startTag.hasEmptyAttributeValue);
    }

    @Test
    public void testGetNameForStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "testTagName";

        String result = startTag.name();

        assertEquals("testTagName", result);
    }

    @Test
    public void testIsSelfClosingForStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.selfClosing = true;

        boolean result = startTag.isSelfClosing();

        assertTrue(result);
    }
}