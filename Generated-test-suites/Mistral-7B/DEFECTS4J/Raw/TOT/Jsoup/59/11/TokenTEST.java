import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Doctype doctype = new Doctype();
        String name = "html";
        doctype.getName().equals(name); // assert initial state
        doctype.reset();
        assertNull(doctype.getName());
    }

    @Test
    public void testDoctypeResetWithData() {
        Doctype doctype = new Doctype();
        String name = "html";
        doctype.name(name);
        String pubSysKey = "pubsyskey";
        doctype.setPubSysKey(pubSysKey);
        String publicIdentifier = "publicid";
        doctype.setPublicIdentifier(publicIdentifier);
        String systemIdentifier = "systemid";
        doctype.setSystemIdentifier(systemIdentifier);
        doctype.setForceQuirks(true);
        assertEquals(name, doctype.getName());
        assertEquals(pubSysKey, doctype.getPubSysKey());
        assertEquals(publicIdentifier, doctype.getPublicIdentifier());
        assertEquals(systemIdentifier, doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
        doctype.reset();
        assertNull(doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Tag tag = new StartTag();
        String tagName = "div";
        tag.name(tagName);
        tag.newAttribute();
        tag.appendAttributeName("class");
        tag.appendAttributeValue("test");
        tag.newAttribute();
        tag.appendAttributeName("id");
        tag.appendAttributeValue("myId");
        tag.finaliseTag();
        assertEquals(tagName, tag.name());
        assertEquals("test", tag.getAttributes().getClass().get(0).getValue());
        assertEquals("myId", tag.getAttributes().getClass().get(1).getValue());
        tag.reset();
        assertNull(tag.name());
        assertNull(tag.getAttributes());
    }

    @Test
    public void testTagResetWithEmptyAttributeValue() {
        StartTag tag = new StartTag();
        tag.name("img");
        tag.newAttribute();
        tag.appendAttributeName("src");
        tag.setEmptyAttributeValue();
        tag.finaliseTag();
        assertEquals("img", tag.name());
        assertTrue(tag.getAttributes().isEmpty());
        tag.reset();
        assertNull(tag.name());
        assertNull(tag.getAttributes());
    }

    @Test
    public void testTagResetWithAttributeValue() {
        StartTag tag = new StartTag();
        tag.name("img");
        tag.newAttribute();
        tag.appendAttributeName("src");
        tag.appendAttributeValue("image.jpg");
        tag.finaliseTag();
        assertEquals("img", tag.name());
        Attribute src = tag.getAttributes().getClass().get(0);
        assertEquals("src", src.getName());
        assertEquals("image.jpg", src.getValue());
        tag.reset();
        assertNull(tag.name());
        assertNull(tag.getAttributes());
    }

    @Test
    public void testTagResetWithMultipleAttributes() {
        StartTag tag = new StartTag();
        tag.name("input");
        tag.newAttribute();
        tag.appendAttributeName("type");
        tag.appendAttributeValue("text");
        tag.newAttribute();
        tag.appendAttributeName("name");
        tag.appendAttributeValue("nameInput");
        tag.finaliseTag();
        assertEquals("input", tag.name());
        Attribute type = tag.getAttributes().getClass().get(0);
        assertEquals("type", type.getName());
        assertEquals("text", type.getValue());
        Attribute name = tag.getAttributes().getClass().get(1);
        assertEquals("name", name.getName());
        assertEquals("nameInput", name.getValue());
        tag.reset();
        assertNull(tag.name());
        assertNull(tag.getAttributes());
    }

    @Test
    public void testTokenTypeIdentification() {
        Token token = new Doctype();
        assertTrue(token.isDoctype());
        token = new StartTag();
        assertFalse(token.isDoctype());
        token = new EndTag();
        assertFalse(token.isDoctype());
        token = new Comment();
        assertFalse(token.isDoctype());
        token = new Character();
        assertFalse(token.isDoctype());
        token = new EOF();
        assertFalse(token.isDoctype());
        token = new Doctype();
        assertTrue(token.isDoctype());
    }
}