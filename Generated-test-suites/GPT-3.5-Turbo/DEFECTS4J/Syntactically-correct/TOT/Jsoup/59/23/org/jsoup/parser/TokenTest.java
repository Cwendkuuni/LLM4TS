// TokenTest.java

import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testToken_reset() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.pubSysKey = "KEY";
        doctype.publicIdentifier.append("public");
        doctype.systemIdentifier.append("system");
        doctype.forceQuirks = true;
        
        Token resetToken = doctype.reset();
        
        assertEquals("", doctype.name.toString());
        assertNull(doctype.pubSysKey);
        assertEquals("", doctype.publicIdentifier.toString());
        assertEquals("", doctype.systemIdentifier.toString());
        assertFalse(doctype.forceQuirks);
        assertEquals(doctype, resetToken);
    }

    @Test
    public void testToken_getName() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        
        String name = doctype.getName();
        
        assertEquals("html", name);
    }

    @Test
    public void testToken_getPubSysKey() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.pubSysKey = "KEY";
        
        String pubSysKey = doctype.getPubSysKey();
        
        assertEquals("KEY", pubSysKey);
    }

    @Test
    public void testToken_getPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.publicIdentifier.append("public");
        
        String publicIdentifier = doctype.getPublicIdentifier();
        
        assertEquals("public", publicIdentifier);
    }

    @Test
    public void testToken_getSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.systemIdentifier.append("system");
        
        String systemIdentifier = doctype.getSystemIdentifier();
        
        assertEquals("system", systemIdentifier);
    }

    @Test
    public void testToken_isForceQuirks() {
        Token.Doctype doctype1 = new Token.Doctype(); // default value
        Token.Doctype doctype2 = new Token.Doctype();
        doctype2.forceQuirks = true;
        
        assertFalse(doctype1.isForceQuirks());
        assertTrue(doctype2.isForceQuirks());
    }
    
    @Test
    public void testTag_reset() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "class";
        startTag.pendingAttributeValue.append("container");
        startTag.pendingAttributeValueS = "link";
        startTag.hasEmptyAttributeValue = true;
        startTag.hasPendingAttributeValue = true;
        startTag.selfClosing = true;
        startTag.attributes = new org.jsoup.nodes.Attributes();
        
        Token resetToken = startTag.reset();
        
        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.selfClosing);
        assertNull(startTag.attributes);
        assertEquals(startTag, resetToken);
    }

    @Test
    public void testTag_newAttribute() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.attributes = new org.jsoup.nodes.Attributes();
        
        startTag.pendingAttributeName = "class";
        startTag.newAttribute();
        
        assertEquals("class", startTag.attributes.get("class").getKey());
        
        startTag.pendingAttributeName = "id";
        startTag.pendingAttributeValue.append("container");
        startTag.newAttribute();
        
        assertEquals("container", startTag.attributes.get("id").getValue());
        
        startTag.pendingAttributeName = "required";
        startTag.hasEmptyAttributeValue = true;
        startTag.newAttribute();
        
        assertTrue(startTag.attributes.get("required") instanceof org.jsoup.nodes.BooleanAttribute);
        
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertEquals(null, startTag.pendingAttributeValueS);
    }

    @Test
    public void testTag_finaliseTag() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.attributes = new org.jsoup.nodes.Attributes();
        
        startTag.pendingAttributeName = "class";
        startTag.finaliseTag();
        
        startTag.pendingAttributeName = "id";
        startTag.pendingAttributeValue.append("container");
        startTag.finaliseTag();
        
        assertNull(startTag.pendingAttributeName);
        assertEquals("", startTag.pendingAttributeValue.toString());
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertEquals(null, startTag.pendingAttributeValueS);
    }

    @Test
    public void testTag_name() {
        Token.StartTag startTag = new Token.StartTag();
        
        Token.StartTag tag = startTag.name("div");
        
        assertEquals("div", startTag.tagName);
        assertEquals("div", startTag.normalName);
        assertEquals(startTag, tag);
    }

    @Test
    public void testTag_isSelfClosing() {
        Token.StartTag startTag1 = new Token.StartTag(); // default value
        Token.StartTag startTag2 = new Token.StartTag();
        startTag2.selfClosing = true;
        
        assertFalse(startTag1.isSelfClosing());
        assertTrue(startTag2.isSelfClosing());
    }

    @Test
    public void testTag_appendTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "d";
        startTag.normalName = "d";
        
        startTag.appendTagName("i");
        
        assertEquals("di", startTag.tagName);
        assertEquals("di", startTag.normalName);
    }

    @Test
    public void testTag_appendAttributeName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.pendingAttributeName = "c";
        
        startTag.appendAttributeName("l");
        
        assertEquals("cl", startTag.pendingAttributeName);
    }

    @Test
    public void testTag_appendAttributeValue() {
        Token.StartTag startTag = new Token.StartTag();

        startTag.appendAttributeValue("con");
        
        assertEquals("con", startTag.pendingAttributeValue.toString());
        
        startTag.appendAttributeValue("ta");
        
        assertEquals("contact", startTag.pendingAttributeValue.toString());
    }

    @Test
    public void testTag_setEmptyAttributeValue() {
        Token.StartTag startTag = new Token.StartTag();
        
        startTag.setEmptyAttributeValue();
        
        assertTrue(startTag.hasEmptyAttributeValue);
    }

    @Test
    public void testComment_reset() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("html");
        comment.bogus = true;
        
        Token resetToken = comment.reset();
        
        assertEquals("", comment.data.toString());
        assertFalse(comment.bogus);
        assertEquals(comment, resetToken);
    }

    @Test
    public void testComment_getData() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("html");
        
        String data = comment.getData();
        
        assertEquals("html", data);
    }

    @Test
    public void testCharacter_reset() {
        Token.Character character = new Token.Character();
        character.data("data");
        
        Token resetToken = character.reset();
        
        assertNull(character.data);
        assertEquals(character, resetToken);
    }

    @Test
    public void testCharacter_data() {
        Token.Character character = new Token.Character();
        
        Token.Character charData = character.data("data");
        
        assertEquals("data", character.data);
        assertEquals(character, charData);
    }

    @Test
    public void testCharacter_getData() {
        Token.Character character = new Token.Character();
        character.data("data");
        
        String data = character.getData();
        
        assertEquals("data", data);
    }

}