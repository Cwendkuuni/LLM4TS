package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.nio.charset.StandardCharsets;

import org.junit.Before;
import org.junit.Test;

public class TokenTest {

    private Doctype doctype;
    private StartTag startTag;
    private EndTag endTag;
    private Comment comment;
    private Character character;
    private StringBuilder sb;

    @Before
    public void initializeTokens() {
        doctype = new Doctype();
        startTag = new StartTag().name("html");
        endTag = new EndTag().name("html");
        comment = new Comment().data("This is a comment");
        character = new Character().data("A");
        sb = new StringBuilder();
    }

    @Test
    public void testDoctypeReset() {
        doctype.getName().equals("Doctype");
        doctype.reset();
        assertNull(doctype.getName());
    }

    @Test
    public void testDoctypeGetters() {
        doctype.getName("HTML");
        doctype.getPubSysKey("dummy");
        doctype.getPublicIdentifier("dummy");
        doctype.setSystemIdentifier("dummy.html");
        doctype.isForceQuirks(true);

        assertEquals("HTML", doctype.getName());
        assertEquals("dummy", doctype.getPubSysKey());
        assertEquals("dummy", doctype.getPublicIdentifier());
        assertEquals("dummy.html", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        startTag.name("html").attributes().put("id", "root");
        endTag.name("html");

        startTag.reset();
        endTag.reset();

        assertSame(TokenType.StartTag, startTag.type);
        assertSame(TokenType.EndTag, endTag.type);
        assertNull(startTag.tagName);
        assertNull(endTag.tagName);
        assertNull(startTag.attributes);
        assertNull(endTag.attributes);
    }

    @Test
    public void testTagNewAttribute() {
        startTag.name("html").attributes().put("id", "root");

        startTag.newAttribute();
        startTag.newAttribute().name("class").value("test");

        assertEquals(2, startTag.getAttributes().size());
        assertTrue(startTag.getAttributes().keySet().contains("class"));
        assertEquals("test", startTag.getAttributes().get("class").getValue());
    }

    @Test
    public void testTagFinaliseTag() {
        startTag.name("html").attributes().put("id", "root");

        startTag.finaliseTag();

        assertEquals(2, startTag.getAttributes().size());
        assertFalse(startTag.getAttributes().isEmpty());
    }

    @Test
    public void testTagNameAttributeValue() {
        startTag.name("html").attributes().put("id", "root").put("class", "test");

        assertEquals("html", startTag.name());
        assertEquals("root", startTag.getAttributes().get("id").getValue());
        assertEquals("test", startTag.getAttributes().get("class").getValue());

        assertFalse(startTag.isSelfClosing());
    }

    @Test
    public void testTagIsSelfClosing() {
        startTag.name("img").selfClosing(true);

        assertTrue(startTag.isSelfClosing());
    }

    @Test
    public void testTagGetAttributes() {
        startTag.name("html").attributes().put("id", "root").put("class", "test");

        assertEquals(2, startTag.getAttributes().size());
        assertFalse(startTag.getAttributes().isEmpty());
        assertSame(Attributes.class, startTag.getAttributes().getClass());
    }

    @Test
    public void testCommentReset() {
        comment.getData().equals("This is a comment");
        comment.reset();
        assertNull(comment.getData());
    }

    @Test
    public void testCharacterReset() {
        character.getData().equals("A");
        character.reset();
        assertNull(character.getData());
    }

    @Test
    public void testCharacterData() {
        character.data("B");
        assertEquals("B", character.getData());
    }

    @Test
    public void testBuilderReset() {
        sb.append("A");
        sb.reset();
        assertEquals("", sb.toString());
    }

    @Test
    public void testTokenTypes() {
        assertSame(TokenType.Doctype, doctype.type);
        assertSame(TokenType.StartTag, startTag.type);
        assertSame(TokenType.EndTag, endTag.type);
        assertSame(TokenType.Comment, comment.type);
        assertSame(TokenType.Character, character.type);
        assertSame(TokenType.EOF, new EOF().type);
    }

    @Test
    public void testTokenTypesParse() {
        String xml = "<html><head><title>Test</title></head><body><p>Hello World!</p></body></html>";
        String[] tokens = Tokenizer.tokenize(xml, StandardCharsets.UTF_8);

        for (Token token : tokens) {
            assertEquals(1, tokens.length);
            switch (token.type) {
                case Doctype:
                    Doctype doctype = (Doctype) token;
                    assertSame(Doctype.class, doctype.getClass());
                    break;
                case StartTag:
                    StartTag startTag = (StartTag) token;
                    assertSame(StartTag.class, startTag.getClass());
                    break;
                case EndTag:
                    EndTag endTag = (EndTag) token;
                    assertSame(EndTag.class, endTag.getClass());
                    break;
                case Comment:
                    Comment comment = (Comment) token;
                    assertSame(Comment.class, comment.getClass());
                    break;
                case Character:
                    Character character = (Character) token;
                    assertSame(Character.class, character.getClass());
                    break;
                case EOF:
                    EOF eof = (EOF) token;
                    assertSame(EOF.class, eof.getClass());
                    break;
                default:
                    throw new IllegalStateException("Unexpected token type: " + token.type);
            }
        }
    }

    @Test
    public void testTokenToString() {
        assertEquals("<html>", startTag.toString());
        assertEquals("</html>", endTag.toString());
        assertEquals("<!--This is a comment-->", comment.toString());
        assertEquals("A", character.toString());
    }
}