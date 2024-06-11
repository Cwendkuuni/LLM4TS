import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        assertEquals("Doctype", doctypeToken.tokenType());

        Token resetDoctypeToken = doctypeToken.reset();
        assertEquals("Doctype", resetDoctypeToken.tokenType());

        TokenType doctypeType = resetDoctypeToken.isDoctype();
        assertTrue(doctypeType == TokenType.Doctype);

        String name = resetDoctypeToken.asDoctype().getName();
        assertEquals("", name);

        String publicIdentifier = resetDoctypeToken.asDoctype().getPublicIdentifier();
        assertEquals("", publicIdentifier);

        String systemIdentifier = resetDoctypeToken.asDoctype().getSystemIdentifier();
        assertEquals("", systemIdentifier);

        boolean forceQuirks = resetDoctypeToken.asDoctype().isForceQuirks();
        assertFalse(forceQuirks);
    }

    @Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();
        assertEquals("StartTag", startTagToken.tokenType());

        Token resetStartTagToken = startTagToken.reset();
        assertEquals("StartTag", resetStartTagToken.tokenType());

        TokenType startTagType = resetStartTagToken.isStartTag();
        assertTrue(startTagType == TokenType.StartTag);

        String name = resetStartTagToken.asStartTag().name();
        assertNull(name);

        String normalName = resetStartTagToken.asStartTag().normalName();
        assertNull(normalName);

        boolean selfClosing = resetStartTagToken.asStartTag().isSelfClosing();
        assertFalse(selfClosing);

        Attributes attributes = resetStartTagToken.asStartTag().getAttributes();
        assertNull(attributes);
    }

    @Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();
        assertEquals("EndTag", endTagToken.tokenType());

        Token resetEndTagToken = endTagToken.reset();
        assertEquals("EndTag", resetEndTagToken.tokenType());

        TokenType endTagType = resetEndTagToken.isEndTag();
        assertTrue(endTagType == TokenType.EndTag);

        String name = resetEndTagToken.asEndTag().name();
        assertNull(name);

        Attributes attributes = resetEndTagToken.asEndTag().getAttributes();
        assertNull(attributes);
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        assertEquals("Comment", commentToken.tokenType());

        Token resetCommentToken = commentToken.reset();
        assertEquals("Comment", resetCommentToken.tokenType());

        TokenType commentType = resetCommentToken.isComment();
        assertTrue(commentType == TokenType.Comment);

        String data = resetCommentToken.asComment().getData();
        assertEquals("", data);
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        assertEquals("Character", characterToken.tokenType());

        Token resetCharacterToken = characterToken.reset();
        assertEquals("Character", resetCharacterToken.tokenType());

        String data = resetCharacterToken.asCharacter().getData();
        assertEquals("", data);
    }

    @Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();
        assertEquals("EOF", eofToken.tokenType());

        Token resetEOFToken = eofToken.reset();
        assertEquals("EOF", resetEOFToken.tokenType());

        TokenType eofType = resetEOFToken.isEOF();
        assertTrue(eofType == TokenType.EOF);
    }

    @Test
    public void testDoctypeTokenWithName() {
        Token doctypeToken = new Token.Doctype().name("html");
        assertEquals("Doctype", doctypeToken.tokenType());

        Token resetDoctypeToken = doctypeToken.reset();
        assertEquals("Doctype", resetDoctypeToken.tokenType());

        String name = resetDoctypeToken.asDoctype().getName();
        assertEquals("html", name);
    }

    @Test
    public void testDoctypeTokenWithNamePublicIdentifier() {
        Token doctypeToken = new Token.Doctype().name("html").publicIdentifier("_public");
        assertEquals("Doctype", doctypeToken.tokenType());

        Token resetDoctypeToken = doctypeToken.reset();
        assertEquals("Doctype", resetDoctypeToken.tokenType());

        String name = resetDoctypeToken.asDoctype().getName();
        assertEquals("html", name);

        String publicIdentifier = resetDoctypeToken.asDoctype().getPublicIdentifier();
        assertEquals("_public", publicIdentifier);
    }

    @Test
    public void testDoctypeTokenWithNameSystemIdentifier() {
        Token doctypeToken = new Token.Doctype().name("html").systemIdentifier("_system");
        assertEquals("Doctype", doctypeToken.tokenType());

        Token resetDoctypeToken = doctypeToken.reset();
        assertEquals("Doctype", resetDoctypeToken.tokenType());

        String name = resetDoctypeToken.asDoctype().getName();
        assertEquals("html", name);

        String systemIdentifier = resetDoctypeToken.asDoctype().getSystemIdentifier();
        assertEquals("_system", systemIdentifier);
    }

    @Test
    public void testDoctypeTokenWithNamePublicIdentifierSystemIdentifier() {
        Token doctypeToken = new Token.Doctype().name("html").publicIdentifier("_public").systemIdentifier("_system");
        assertEquals("Doctype", doctypeToken.tokenType());

        Token resetDoctypeToken = doctypeToken.reset();
        assertEquals("Doctype", resetDoctypeToken.tokenType());

        String name = resetDoctypeToken.asDoctype().getName();
        assertEquals("html", name);

        String publicIdentifier = resetDoctypeToken.asDoctype().getPublicIdentifier();
        assertEquals("_public", publicIdentifier);

        String systemIdentifier = resetDoctypeToken.asDoctype().getSystemIdentifier();
        assertEquals("_system", systemIdentifier);
    }

    @Test
    public void testDoctypeTokenWithForceQuirksTrue() {
        Token doctypeToken = new Token.Doctype().name("html").forceQuirks(true);
        assertEquals("Doctype", doctypeToken.tokenType());

        Token resetDoctypeToken = doctypeToken.reset();
        assertEquals("Doctype", resetDoctypeToken.tokenType());

        boolean forceQuirks = resetDoctypeToken.asDoctype().isForceQuirks();
        assertTrue(forceQuirks);
    }

    @Test
    public void testStartTagTokenWithNameAttributes() {
        Token startTagToken = new Token.StartTag().name("p").attributes(new Attributes().put("class", "text"));
        assertEquals("StartTag", startTagToken.tokenType());

        Token resetStartTagToken = startTagToken.reset();
        assertEquals("StartTag", resetStartTagToken.tokenType());

        String name = resetStartTagToken.asStartTag().name();
        assertEquals("p", name);

        Attributes attributes = resetStartTagToken.asStartTag().getAttributes();
        assertNotNull(attributes);
        assertEquals(1, attributes.size());
        assertEquals("text", attributes.get("class").getValue());
    }

    @Test
    public void testStartTagTokenWithNameAttributesSelfClosing() {
        Token startTagToken = new Token.StartTag().name("img").attributes(new Attributes().put("src", "image.jpg")).selfClosing(true);
        assertEquals("StartTag", startTagToken.tokenType());

        Token resetStartTagToken = startTagToken.reset();
        assertEquals("StartTag", resetStartTagToken.tokenType());

        String name = resetStartTagToken.asStartTag().name();
        assertEquals("img", name);

        Attributes attributes = resetStartTagToken.asStartTag().getAttributes();
        assertNotNull(attributes);
        assertEquals(1, attributes.size());
        assertEquals("image.jpg", attributes.get("src").getValue());

        boolean selfClosing = resetStartTagToken.asStartTag().isSelfClosing();
        assertTrue(selfClosing);
    }

    @Test
    public void testStartTagTokenWithNameAttributesSelfClosingAttributes() {
        Token startTagToken = new Token.StartTag().name("input").attributes(new Attributes().put("type", "text").put("name", "username"));
        assertEquals("StartTag", startTagToken.tokenType());

        Token resetStartTagToken = startTagToken.reset();
        assertEquals("StartTag", resetStartTagToken.tokenType());

        String name = resetStartTagToken.asStartTag().name();
        assertEquals("input", name);

        Attributes attributes = resetStartTagToken.asStartTag().getAttributes();
        assertNotNull(attributes);
        assertEquals(2, attributes.size());
        assertEquals("text", attributes.get("type").getValue());
        assertEquals("username", attributes.get("name").getValue());
    }

    @Test
    public void testStartTagTokenWithNameAttributesSelfClosingAttributesValue() {
        Token startTagToken = new Token.StartTag().name("input").attributes(new Attributes().put("type", "text").put("name", "username").put("value", "John Doe"));
        assertEquals("StartTag", startTagToken.tokenType());

        Token resetStartTagToken = startTagToken.reset();
        assertEquals("StartTag", resetStartTagToken.tokenType());

        String name = resetStartTagToken.asStartTag().name();
        assertEquals("input", name);

        Attributes attributes = resetStartTagToken.asStartTag().getAttributes();
        assertNotNull(attributes);
        assertEquals(3, attributes.size());
        assertEquals("text", attributes.get("type").getValue());
        assertEquals("username", attributes.get("name").getValue());
        assertEquals("John Doe", attributes.get("value").getValue());
    }

    @Test
    public void testStartTagTokenWithNameAttributesSelfClosingAttributesValueBuilder() {
        Token startTagToken = new Token.StartTag().name("input").attributes(new Attributes().put("type", "text").put("name", "username").put("value", new StringBuilder("John Doe")));
        assertEquals("StartTag", startTagToken.tokenType());

        Token resetStartTagToken = startTagToken.reset();
        assertEquals("StartTag", resetStartTagToken.tokenType());

        String name = resetStartTagToken.asStartTag().name();
        assertEquals("input", name);

        Attributes attributes = resetStartTagToken.asStartTag().getAttributes();
        assertNotNull(attributes);
        assertEquals(3, attributes.size());
        assertEquals("text", attributes.get("type").getValue());
        assertEquals("username", attributes.get("name").getValue());
        assertEquals("John Doe", attributes.get("value").getValue());
    }

    @Test
    public void testEndTagTokenWithName() {
        Token endTagToken = new Token.EndTag().name("p");
        assertEquals("EndTag", endTagToken.tokenType());

        Token resetEndTagToken = endTagToken.reset();
        assertEquals("EndTag", resetEndTagToken.tokenType());

        String name = resetEndTagToken.asEndTag().name();
        assertEquals("p", name);
    }

    @Test
    public void testCharacterTokenWithData() {
        Token characterToken = new Token.Character().data("A");
        assertEquals("Character", characterToken.tokenType());

        Token resetCharacterToken = characterToken.reset();
        assertEquals("Character", resetCharacterToken.tokenType());

        String data = resetCharacterToken.asCharacter().getData();
        assertEquals("A", data);
    }

    @Test
    public void testCharacterTokenWithDataBuilder() {
        Token characterToken = new Token.Character().data(new StringBuilder("ABC"));
        assertEquals("Character", characterToken.tokenType());

        Token resetCharacterToken = characterToken.reset();
        assertEquals("Character", resetCharacterToken.tokenType());

        String data = resetCharacterToken.asCharacter().getData();
        assertEquals("ABC", data);
    }

    @Test
    public void testCharacterTokenWithDataCodepoint() {
        Token characterToken = new Token.Character().data('A');
        assertEquals("Character", characterToken.tokenType());

        Token resetCharacterToken = characterToken.reset();
        assertEquals("Character", resetCharacterToken.tokenType());

        String data = resetCharacterToken.asCharacter().getData();
        assertEquals("A", data);
    }

    @Test
    public void testCharacterTokenWithDataCodepoints() {
        Token characterToken = new Token.Character().data(new int[]{0x41, 0x42, 0x43});
        assertEquals("Character", characterToken.tokenType());

        Token resetCharacterToken = characterToken.reset();
        assertEquals("Character", resetCharacterToken.tokenType());

        String data = resetCharacterToken.asCharacter().getData();
        assertEquals("ABC", data);
    }

    @Test
    public void testCommentTokenWithData() {
        Token commentToken = new Token.Comment().data("This is a comment.");
        assertEquals("Comment", commentToken.tokenType());

        Token resetCommentToken = commentToken.reset();
        assertEquals("Comment", resetCommentToken.tokenType());

        String data = resetCommentToken.asComment().getData();
        assertEquals("This is a comment.", data);
    }
}