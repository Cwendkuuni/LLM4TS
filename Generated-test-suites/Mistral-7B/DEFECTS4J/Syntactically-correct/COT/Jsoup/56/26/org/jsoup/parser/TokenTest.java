import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.EF;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        String name = "html";
        doctype.getName().equals(name); // check initial state
        doctype.reset();
        Assert.assertEquals(null, doctype.getName());
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Doctype doctype = new Doctype();
        String publicIdentifier = "publicId";
        doctype.getPublicIdentifier().equals(publicIdentifier); // check initial state
        doctype.reset();
        Assert.assertEquals(null, doctype.getPublicIdentifier());
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Doctype doctype = new Doctype();
        String systemIdentifier = "systemId";
        doctype.getSystemIdentifier().equals(systemIdentifier); // check initial state
        doctype.reset();
        Assert.assertEquals(null, doctype.getSystemIdentifier());
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Doctype doctype = new Doctype();
        doctype.isForceQuirks(); // check initial state
        doctype.reset();
        Assert.assertEquals(false, doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        String tagName = "div";
        tag.name().equals(tagName); // check initial state
        tag.reset();
        Assert.assertEquals(null, tag.name());
    }

    @Test
    public void testTagNewAttribute() {
        Token.Tag tag = new Token.StartTag();
        String attributeName = "class";
        tag.newAttribute();
        Assert.assertNull(tag.getAttributes());

        tag.newAttribute(); // second call, attributes should be created
        Attribute attribute = tag.getAttributes().get("class");
        assertNotNull(attribute);
    }

    @Test
    public void testTagFinaliseTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        startTag.newAttribute();
        startTag.newAttribute("class", "test");
        startTag.finaliseTag();

        Assert.assertNotNull(startTag.getAttributes());
        assertEquals("div", startTag.name());
        assertEquals("test", startTag.getAttributes().get("class").getValue());
    }

    @Test(expected = AssertionError.class)
    public void testTagNewAttributeWithPendingValue() {
        Token.Tag tag = new Token.StartTag();
        String attributeName = "class";
        String attributeValue = "test";
        tag.newAttribute();
        tag.appendAttributeValue(attributeValue);
        tag.newAttribute();

        // should throw an AssertionError, as newAttribute() should clear the pending attribute
        tag.newAttribute(attributeName);
    }

    @Test
    public void testTagSetEmptyAttributeValue() {
        Token.Tag tag = new Token.StartTag();
        tag.setEmptyAttributeValue();
        Assert.assertTrue(tag.hasEmptyAttributeValue);
        tag.reset();
        Assert.assertFalse(tag.hasEmptyAttributeValue);
    }

    @Test
    public void testCharacterReset() {
        Character character = new Character();
        String data = "test";
        character.getData().equals(data); // check initial state
        character.reset();
        Assert.assertEquals(null, character.getData());
    }

    @Test
    public void testEOFReset() {
        EF eof = new EF();
        eof.reset();
        // EOF doesn't have any data, so there's nothing to check
    }
}