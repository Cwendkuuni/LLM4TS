package org.jsoup.parser;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    private Token.Doctype doctypeToken;
    private Token.StartTag startTagToken;
    private Token.EndTag endTagToken;
    private Token.Comment commentToken;
    private Token.Character characterToken;
    private Token.EOF eofToken;

    @Before
    public void setUp() {
        doctypeToken = new Token.Doctype();
        startTagToken = new Token.StartTag();
        endTagToken = new Token.EndTag();
        commentToken = new Token.Comment();
        characterToken = new Token.Character();
        eofToken = new Token.EOF();
    }

    @Test
    public void testDoctypeReset() {
        doctypeToken.name.append("testName");
        doctypeToken.pubSysKey = "testPubSysKey";
        doctypeToken.publicIdentifier.append("testPublicIdentifier");
        doctypeToken.systemIdentifier.append("testSystemIdentifier");
        doctypeToken.forceQuirks = true;

        doctypeToken.reset();

        assertEquals("", doctypeToken.name.toString());
        assertNull(doctypeToken.pubSysKey);
        assertEquals("", doctypeToken.publicIdentifier.toString());
        assertEquals("", doctypeToken.systemIdentifier.toString());
        assertFalse(doctypeToken.forceQuirks);
    }

    @Test
    public void testDoctypeGetName() {
        doctypeToken.name.append("testName");

        String name = doctypeToken.getName();

        assertEquals("testName", name);
    }

    @Test
    public void testDoctypeGetPubSysKey() {
        doctypeToken.pubSysKey = "testPubSysKey";

        String pubSysKey = doctypeToken.getPubSysKey();

        assertEquals("testPubSysKey", pubSysKey);
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        doctypeToken.publicIdentifier.append("testPublicIdentifier");

        String publicIdentifier = doctypeToken.getPublicIdentifier();

        assertEquals("testPublicIdentifier", publicIdentifier);
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        doctypeToken.systemIdentifier.append("testSystemIdentifier");

        String systemIdentifier = doctypeToken.getSystemIdentifier();

        assertEquals("testSystemIdentifier", systemIdentifier);
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        doctypeToken.forceQuirks = true;

        boolean isForceQuirks = doctypeToken.isForceQuirks();

        assertTrue(isForceQuirks);
    }

    // TODO: More Doctype test cases

    @Test
    public void testStartTagReset() {
        startTagToken.tagName = "testTagName";
        startTagToken.normalName = "testNormalName";
        startTagToken.pendingAttributeName = "testAttributeName";
        startTagToken.pendingAttributeValue.append("testAttributeValue");
        startTagToken.hasEmptyAttributeValue = true;
        startTagToken.hasPendingAttributeValue = true;
        startTagToken.selfClosing = true;
        startTagToken.attributes = new Attributes();

        startTagToken.reset();

        assertNull(startTagToken.tagName);
        assertNull(startTagToken.normalName);
        assertNull(startTagToken.pendingAttributeName);
        assertEquals("", startTagToken.pendingAttributeValue.toString());
        assertNull(startTagToken.pendingAttributeValueS);
        assertFalse(startTagToken.hasEmptyAttributeValue);
        assertFalse(startTagToken.hasPendingAttributeValue);
        assertFalse(startTagToken.selfClosing);
        assertNull(startTagToken.attributes);
    }

    @Test
    public void testStartTagName() {
        String name = startTagToken.name();

        assertNull(name);
    }

    // TODO: More StartTag test cases

    @Test
    public void testEndTagReset() {
        endTagToken.tagName = "testTagName";
        endTagToken.normalName = "testNormalName";
        endTagToken.pendingAttributeName = "testAttributeName";
        endTagToken.pendingAttributeValue.append("testAttributeValue");
        endTagToken.hasEmptyAttributeValue = true;
        endTagToken.hasPendingAttributeValue = true;
        endTagToken.selfClosing = true;
        endTagToken.attributes = new Attributes();

        endTagToken.reset();

        assertNull(endTagToken.tagName);
        assertNull(endTagToken.normalName);
        assertNull(endTagToken.pendingAttributeName);
        assertEquals("", endTagToken.pendingAttributeValue.toString());
        assertNull(endTagToken.pendingAttributeValueS);
        assertFalse(endTagToken.hasEmptyAttributeValue);
        assertFalse(endTagToken.hasPendingAttributeValue);
        assertFalse(endTagToken.selfClosing);
        assertNull(endTagToken.attributes);
    }

    @Test
    public void testEndTagName() {
        String name = endTagToken.name();

        assertNull(name);
    }

    // TODO: More EndTag test cases

    @Test
    public void testCommentReset() {
        commentToken.data.append("testData");
        commentToken.bogus = true;

        commentToken.reset();

        assertEquals("", commentToken.data.toString());
        assertFalse(commentToken.bogus);
    }

    @Test
    public void testCommentGetData() {
        commentToken.data.append("testData");

        String data = commentToken.getData();

        assertEquals("testData", data);
    }

    // TODO: More Comment test cases

    @Test
    public void testCharacterReset() {
        characterToken.data = "testData";

        characterToken.reset();

        assertNull(characterToken.data);
    }

    @Test
    public void testCharacterData() {
        characterToken.data("testData");

        String data = characterToken.getData();

        assertEquals("testData", data);
    }

    // TODO: More Character test cases

    // TODO: More EOF test cases

    // TODO: More isDoctype test cases

    // TODO: More asDoctype test cases

    // TODO: More isStartTag test cases

    // TODO: More asStartTag test cases

    // TODO: More isEndTag test cases

    // TODO: More asEndTag test cases

    // TODO: More isComment test cases

    // TODO: More asComment test cases

    // TODO: More isCharacter test cases

    // TODO: More asCharacter test cases

    // TODO: More isEOF test cases

    // TODO: More TokenType test cases
  
}