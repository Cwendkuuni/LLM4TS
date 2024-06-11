import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctype_getName() {
        Token token = new Token.Doctype();
        String name = token.asDoctype().getName();
        Assert.assertEquals("Doctype", name);
    }

    @Test
    public void testDoctype_getPublicIdentifier() {
        Token token = new Token.Doctype();
        String publicIdentifier = token.asDoctype().getPublicIdentifier();
        Assert.assertNull(publicIdentifier);
    }

    @Test
    public void testDoctype_getSystemIdentifier() {
        Token token = new Token.Doctype();
        String systemIdentifier = token.asDoctype().getSystemIdentifier();
        Assert.assertNull(systemIdentifier);
    }

    @Test
    public void testDoctype_isForceQuirks() {
        Token token = new Token.Doctype();
        boolean forceQuirks = token.asDoctype().isForceQuirks();
        Assert.assertFalse(forceQuirks);
    }

    @Test(expected = AssertionError.class)
    public void testDoctype_getName_Null() {
        Token token = new Token.Doctype();
        String name = token.asDoctype().getName();
        Assert.assertEquals("Doctype", name); // This test case should throw an AssertionError since getName() should not return null
    }

    @Test
    public void testTag_getName() {
        Token token = new Token.StartTag("tagName");
        String name = token.asStartTag().getName();
        Assert.assertEquals("tagName", name);
    }

    @Test
    public void testTag_isSelfClosing() {
        Token token = new Token.StartTag("tagName");
        boolean selfClosing = token.asStartTag().isSelfClosing();
        Assert.assertFalse(selfClosing);
    }

    @Test
    public void testTag_getAttributes() {
        Token token = new Token.StartTag("tagName");
        Attributes attributes = token.asStartTag().getAttributes();
        Assert.assertNull(attributes);
    }

    @Test
    public void testTag_appendTagName() {
        Token token = new Token.StartTag();
        token.asStartTag().appendTagName("tagName");
        String name = token.asStartTag().getName();
        Assert.assertEquals("tagName", name);
    }

    @Test
    public void testTag_appendAttributeName() {
        Token token = new Token.StartTag();
        token.asStartTag().appendAttributeName("attributeName");
        String attributeName = token.asStartTag().getPendingAttributeName();
        Assert.assertEquals("attributeName", attributeName);
    }

    @Test
    public void testTag_appendAttributeValue() {
        Token token = new Token.StartTag();
        token.asStartTag().appendAttributeValue("attributeValue");
        String attributeValue = token.asStartTag().getPendingAttributeValue().toString();
        Assert.assertEquals("attributeValue", attributeValue);
    }

    @Test
    public void testTag_newAttribute() {
        Token token = new Token.StartTag();
        token.asStartTag().newAttribute();
        Attributes attributes = token.asStartTag().getAttributes();
        Assert.assertNotNull(attributes);
    }

    @Test
    public void testTag_finaliseTag() {
        Token token = new Token.StartTag();
        token.asStartTag().name("tagName");
        token.asStartTag().newAttribute();
        token.asStartTag().appendAttributeName("attributeName");
        token.asStartTag().appendAttributeValue("attributeValue");
        token.asStartTag().finaliseTag();
        Attributes attributes = token.asStartTag().getAttributes();
        Assert.assertNotNull(attributes);
        Assert.assertEquals("attributeValue", attributes.toString().split(" ")[1].split("=")[1]);
    }

    @Test(expected = AssertionError.class)
    public void testTag_appendTagName_Null() {
        Token token = new Token.StartTag();
        token.asStartTag().appendTagName(null);
        String name = token.asStartTag().getName();
        Assert.assertNull(name); // This test case should throw an AssertionError since getName() should not return null
    }

    @Test(expected = AssertionError.class)
    public void testTag_appendAttributeName_Null() {
        Token token = new Token.StartTag();
        token.asStartTag().appendAttributeName(null);
        String attributeName = token.asStartTag().getPendingAttributeName();
        Assert.assertNull(attributeName); // This test case should throw an AssertionError since getPendingAttributeName() should not return null
    }

    @Test(expected = AssertionError.class)
    public void testTag_appendAttributeValue_Null() {
        Token token = new Token.StartTag();
        token.asStartTag().appendAttributeValue(null);
        String attributeValue = token.asStartTag().getPendingAttributeValue().toString();
        Assert.assertNull(attributeValue); // This test case should throw an AssertionError since getPendingAttributeValue() should not return null
    }

    @Test
    public void testStartTag_toString() {
        Token token = new Token.StartTag("tagName");
        String expected = "<tagName>";
        String result = token.toString();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testStartTag_toString_WithAttributes() {
        Token token = new Token.StartTag("tagName");
        token.asStartTag().newAttribute();
        token.asStartTag().appendAttributeName("attributeName");
        token.asStartTag().appendAttributeValue("attributeValue");
        String expected = "<tagName attributeName=\"attributeValue\">";
        String result = token.toString();
        Assert.assertEquals(expected, result);
    }

    @Test(expected = AssertionError.class)
    public void testStartTag_toString_WithoutName() {
        Token token = new Token.StartTag();
        String result = token.toString();
        Assert.assertNull(result); // This test case should throw an AssertionError since toString() should not return null when the token name is not set
    }

    @Test
    public void testEndTag_toString() {
        Token token = new Token.EndTag("tagName");
        String expected = "</tagName>";
        String result = token.toString();
        Assert.assertEquals(expected, result);
    }

    @Test(expected = AssertionError.class)
    public void testEndTag_toString_WithoutName() {
        Token token = new Token.EndTag();
        String result = token.toString();
        Assert.assertNull(result); // This test case should throw an AssertionError since toString() should not return null when the token name is not set
    }

    @Test
    public void testCharacter_getData() {
        Token token = new Token.Character("data");
        String result = token.asCharacter().getData();
        Assert.assertEquals("data", result);
    }

    @Test
    public void testCharacter_toString() {
        Token token = new Token.Character("data");
        String result = token.toString();
        Assert.assertEquals("data", result);
    }

    @Test
    public void testComment_getData() {
        Token token = new Token.Comment();
        String result = token.asComment().getData();
        Assert.assertNull(result);
    }

    @Test
    public void testComment_toString() {
        Token token = new Token.Comment();
        String expected = "<!-- -->";
        String result = token.toString();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testComment_getData_WithData() {
        Token token = new Token.Comment("data");
        String result = token.asComment().getData();
        Assert.assertEquals("data", result);
    }

    @Test
    public void testComment_toString_WithData() {
        Token token = new Token.Comment("data");
        String expected = "<!--data-->";
        String result = token.toString();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testEOF_toString() {
        Token token = new Token.EOF();
        String expected = "";
        String result = token.toString();
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testIsDoctype() {
        Token token1 = new Token.Doctype();
        Token token2 = new Token.StartTag("tagName");
        boolean result1 = token1.isDoctype();
        boolean result2 = token2.isDoctype();
        Assert.assertTrue(result1);
        Assert.assertFalse(result2);
    }

    @Test
    public void testIsStartTag() {
        Token token1 = new Token.Doctype();
        Token token2 = new Token.StartTag("tagName");
        boolean result1 = token1.isStartTag();
        boolean result2 = token2.isStartTag();
        Assert.assertFalse(result1);
        Assert.assertTrue(result2);
    }

    @Test
    public void testIsEndTag() {
        Token token1 = new Token.Doctype();
        Token token2 = new Token.EndTag("tagName");
        boolean result1 = token1.isEndTag();
        boolean result2 = token2.isEndTag();
        Assert.assertFalse(result1);
        Assert.assertTrue(result2);
    }

    @Test
    public void testIsComment() {
        Token token1 = new Token.Doctype();
        Token token2 = new Token.Comment();
        boolean result1 = token1.isComment();
        boolean result2 = token2.isComment();
        Assert.assertFalse(result1);
        Assert.assertTrue(result2);
    }

    @Test
    public void testIsCharacter() {
        Token token1 = new Token.Doctype();
        Token token2 = new Token.Character("data");
        boolean result1 = token1.isCharacter();
        boolean result2 = token2.isCharacter();
        Assert.assertFalse(result1);
        Assert.assertTrue(result2);
    }

    @Test
    public void testIsEOF() {
        Token token1 = new Token.Doctype();
        Token token2 = new Token.EOF();
        boolean result1 = token1.isEOF();
        boolean result2 = token2.isEOF();
        Assert.assertFalse(result1);
        Assert.assertTrue(result2);
    }
}