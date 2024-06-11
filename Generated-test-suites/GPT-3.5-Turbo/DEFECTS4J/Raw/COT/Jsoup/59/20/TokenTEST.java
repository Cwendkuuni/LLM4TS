import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token.TokenType expectedType = Token.TokenType.Doctype;
        Token token = new Token.Doctype();

        assertEquals(expectedType, token.type);
    }

    @Test
    public void testTokenTypeToString() {
        String expectedString = "EndTag";
        Token.TokenType tokenType = Token.TokenType.EndTag;

        String result = tokenType.toString();

        assertEquals(expectedString, result);
    }

    @Test
    public void testTokenReset() {
        Token token = new Token.Doctype().reset();

        assertEquals(null, token.getName());
        assertEquals(null, token.getPubSysKey());
        assertEquals(null, token.getPublicIdentifier());
        assertEquals(null, token.getSystemIdentifier());
        assertEquals(false, token.isForceQuirks());
    }

    @Test
    public void testDoctypeGetName() {
        String expectedName = "name";
        Token.Doctype token = new Token.Doctype();
        token.name.append(expectedName);

        String result = token.getName();

        assertEquals(expectedName, result);
    }

    @Test
    public void testDoctypeGetPubSysKey() {
        String expectedPubSysKey = "pubSysKey";
        Token.Doctype token = new Token.Doctype();
        token.pubSysKey = expectedPubSysKey;

        String result = token.getPubSysKey();

        assertEquals(expectedPubSysKey, result);
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        String expectedPublicIdentifier = "publicIdentifier";
        Token.Doctype token = new Token.Doctype();
        token.publicIdentifier.append(expectedPublicIdentifier);

        String result = token.getPublicIdentifier();

        assertEquals(expectedPublicIdentifier, result);
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        String expectedSystemIdentifier = "systemIdentifier";
        Token.Doctype token = new Token.Doctype();
        token.systemIdentifier.append(expectedSystemIdentifier);

        String result = token.getSystemIdentifier();

        assertEquals(expectedSystemIdentifier, result);
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        boolean expectedForceQuirks = true;
        Token.Doctype token = new Token.Doctype();
        token.forceQuirks = expectedForceQuirks;

        boolean result = token.isForceQuirks();

        assertEquals(expectedForceQuirks, result);
    }

    @Test
    public void testTagReset() {
        Token.Tag token = new Token.StartTag().reset();

        assertEquals(null, token.tagName);
        assertEquals(null, token.normalName);
        assertEquals(null, token.pendingAttributeName);
        assertEquals(0, token.pendingAttributeValue.length());
        assertEquals(null, token.pendingAttributeValueS);
        assertEquals(false, token.hasEmptyAttributeValue);
        assertEquals(false, token.hasPendingAttributeValue);
        assertEquals(false, token.selfClosing);
        assertEquals(null, token.attributes);
    }

    @Test
    public void testTagNewAttribute() {
        Token.Tag token = new Token.StartTag();
        token.attributes = new Attributes();
        token.pendingAttributeName = "attributeName";
        token.pendingAttributeValue.append("attributeValue");

        token.newAttribute();

        assertEquals("attributeName", token.attributes.get("attributeName"));
        assertEquals(null, token.pendingAttributeName);
        assertEquals(0, token.pendingAttributeValue.length());
        assertEquals(false, token.hasEmptyAttributeValue);
        assertEquals(false, token.hasPendingAttributeValue);
        assertEquals(null, token.pendingAttributeValueS);
    }

    @Test
    public void testTagFinaliseTag() {
        Token.Tag token = new Token.StartTag();
        token.pendingAttributeName = "attributeName";

        token.finaliseTag();

        assertEquals(null, token.pendingAttributeName);
    }

    @Test
    public void testTagName() {
        String expectedTagName = "tagName";
        Token.Tag token = new Token.StartTag();

        token.name(expectedTagName);

        assertEquals(expectedTagName, token.tagName);
        assertEquals(expectedTagName.toLowerCase(), token.normalName);
    }

    @Test
    public void testTagIsSelfClosing() {
        boolean expectedSelfClosing = true;
        Token.Tag token = new Token.StartTag();
        token.selfClosing = expectedSelfClosing;

        boolean result = token.isSelfClosing();

        assertEquals(expectedSelfClosing, result);
    }

    @Test
    public void testTagGetAttributes() {
        Attributes expectedAttributes = new Attributes();
        Token.Tag token = new Token.StartTag();
        token.attributes = expectedAttributes;

        Attributes result = token.getAttributes();

        assertEquals(expectedAttributes, result);
    }

    @Test
    public void testTagAppendTagName() {
        String expectedTagName = "name";
        Token.Tag token = new Token.StartTag();

        token.appendTagName(expectedTagName);

        assertEquals(expectedTagName, token.tagName);
        assertEquals(expectedTagName.toLowerCase(), token.normalName);
    }

    @Test
    public void testTagAppendAttributeName() {
        String expectedAttributeName = "name";
        Token.Tag token = new Token.StartTag();

        token.appendAttributeName(expectedAttributeName);

        assertEquals(expectedAttributeName, token.pendingAttributeName);
    }

    @Test
    public void testTagAppendAttributeValue() {
        String expectedAttributeValue = "value";
        Token.Tag token = new Token.StartTag();

        token.appendAttributeValue(expectedAttributeValue);

        assertEquals(expectedAttributeValue, token.pendingAttributeValue.toString());
    }

    @Test
    public void testTagSetEmptyAttributeValue() {
        Token.Tag token = new Token.StartTag();

        token.setEmptyAttributeValue();

        assertEquals(true, token.hasEmptyAttributeValue);
    }

    @Test
    public void testCommentReset() {
        Token.Comment token = new Token.Comment().reset();

        assertEquals(0, token.data.length());
        assertEquals(false, token.bogus);
    }

    @Test
    public void testCommentGetData() {
        String expectedData = "data";
        Token.Comment token = new Token.Comment();
        token.data.append(expectedData);

        String result = token.getData();

        assertEquals(expectedData, result);
    }

    @Test
    public void testCharacterReset() {
        Token.Character token = new Token.Character().reset();

        assertEquals(null, token.data);
    }

    @Test
    public void testCharacterData() {
        String expectedData = "data";
        Token.Character token = new Token.Character();

        token.data(expectedData);

        assertEquals(expectedData, token.data);
    }

    @Test
    public void testEOFReset() {
        Token.EOF token = new Token.EOF().reset();

        assertEquals(token, token.reset());
    }

    @Test
    public void testIsDoctype() {
        boolean expectedResult = true;
        Token token = new Token.Doctype();

        boolean result = token.isDoctype();

        assertEquals(expectedResult, result);
    }

    @Test
    public void testAsDoctype() {
        Token.Doctype expectedToken = new Token.Doctype();
        Token token = expectedToken;

        Token.Doctype result = token.asDoctype();

        assertEquals(expectedToken, result);
    }

    @Test
    public void testIsStartTag() {
        boolean expectedResult = true;
        Token token = new Token.StartTag();

        boolean result = token.isStartTag();

        assertEquals(expectedResult, result);
    }

    @Test
    public void testAsStartTag() {
        Token.StartTag expectedToken = new Token.StartTag();
        Token token = expectedToken;

        Token.StartTag result = token.asStartTag();

        assertEquals(expectedToken, result);
    }

    @Test
    public void testIsEndTag() {
        boolean expectedResult = true;
        Token token = new Token.EndTag();

        boolean result = token.isEndTag();

        assertEquals(expectedResult, result);
    }

    @Test
    public void testAsEndTag() {
        Token.EndTag expectedToken = new Token.EndTag();
        Token token = expectedToken;

        Token.EndTag result = token.asEndTag();

        assertEquals(expectedToken, result);
    }

    @Test
    public void testIsComment() {
        boolean expectedResult = true;
        Token token = new Token.Comment();

        boolean result = token.isComment();

        assertEquals(expectedResult, result);
    }

    @Test
    public void testAsComment() {
        Token.Comment expectedToken = new Token.Comment();
        Token token = expectedToken;

        Token.Comment result = token.asComment();

        assertEquals(expectedToken, result);
    }

    @Test
    public void testIsCharacter() {
        boolean expectedResult = true;
        Token token = new Token.Character();

        boolean result = token.isCharacter();

        assertEquals(expectedResult, result);
    }

    @Test
    public void testAsCharacter() {
        Token.Character expectedToken = new Token.Character();
        Token token = expectedToken;

        Token.Character result = token.asCharacter();

        assertEquals(expectedToken, result);
    }

    @Test
    public void testIsEOF() {
        boolean expectedResult = true;
        Token token = new Token.EOF();

        boolean result = token.isEOF();

        assertEquals(expectedResult, result);
    }
}