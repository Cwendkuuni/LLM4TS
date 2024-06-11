package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.Attribute;
import org.jsoup.nodes.Token.Comment;
import org.jsoup.nodes.Token.Doctype;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.nodes.Token.Tag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;

import org.junit.Test;

/**
 * Test class for {@link Tokeniser}.
 */
public class TokeniserTest {

    @Test
    public void testTokeniser() {
        // Given
        CharacterReader reader = mock(CharacterReader.class);
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);

        // When
        tokeniser.read();
        Token token = tokeniser.read();

        // Then
        Validate.isFalse(tokeniser.isEmitPending(), "There is an unread token pending!");
        assertEquals(Token.TokenType.StartTag, token.type);
        assertSame(TokeniserState.Data, tokeniser.getState());
        assertNull(token.tagName);
        assertNull(token.data);
        assertNull(token.attributes);

        // Given
        String input = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\r\n<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"DTD/xhtml1-strict.dtd\">\r\n<html xmlns=\"http://www.w3.org/1999/xhtml\">\r\n<head>\r\n<title>Test</title>\r\n</head>\r\n<body>\r\n<p>Hello World!</p>\r\n</body>\r\n</html>";
        when(reader.current()).thenReturn((char) c -> c == input.charAt(0) ? input.charAt(0) : -1);
        when(reader.mark()).thenAnswer(invocation -> {
            reader.mark(1);
            return reader;
        });
        when(reader.consume()).thenAnswer(invocation -> {
            reader.consume(1);
            return input.charAt(reader.pos() - 1);
        });

        // When
        tokeniser.transition(TokeniserState.Data);
        tokeniser.emit(new Token(Token.TokenType.Doctype, null, null));
        tokeniser.advanceTransition(TokeniserState.TagSoup);
        tokeniser.emit("<?xml");
        tokeniser.consumeCharacterReference(null, false);
        tokeniser.emit("version");
        tokeniser.consumeCharacterReference("1", null);
        tokeniser.consumeCharacterReference("\"", null);
        tokeniser.consumeCharacterReference("UTF-8", null);
        tokeniser.consumeCharacterReference("\"", null);
        tokeniser.consumeCharacterReference("?", null);
        tokeniser.emit(">");
        tokeniser.consumeCharacterReference('!', false);
        tokeniser.consumeCharacterReference("/", false);
        tokeniser.consumeCharacterReference("D", false);
        tokeniser.consumeCharacterReference("O", false);
        tokeniser.consumeCharacterReference("C", false);
        tokeniser.consumeCharacterReference("T", false);
        tokeniser.consumeCharacterReference("Y", false);
        tokeniser.consumeCharacterReference(" ", false);
        tokeniser.consumeCharacterReference("P", false);
        tokeniser.consumeCharacterReference("U", false);
        tokeniser.consumeCharacterReference("B", false);
        tokeniser.consumeCharacterReference("L", false);
        tokeniser.consumeCharacterReference(" ", false);
        tokeniser.consumeCharacterReference("\"", false);
        tokeniser.consumeCharacterReference("-", false);
        tokeniser.consumeCharacterReference("//", false);
        tokeniser.consumeCharacterReference("W", false);
        tokeniser.consumeCharacterReference("3", false);
        tokeniser.consumeCharacterReference("C", false);
        tokeniser.consumeCharacterReference("/", false);
        tokeniser.consumeCharacterReference("D", false);
        tokeniser.consumeCharacterReference("T", false);
        tokeniser.consumeCharacterReference("D", false);
        tokeniser.consumeCharacterReference("/", false);
        tokeniser.consumeCharacterReference("x", false);
        tokeniser.consumeCharacterReference("h", false);
        tokeniser.consumeCharacterReference("t", false);
        tokeniser.consumeCharacterReference("m", false);
        tokeniser.consumeCharacterReference("l", false);
        tokeniser.consumeCharacterReference(" ", false);
        tokeniser.consumeCharacterReference("1", null);
        tokeniser.consumeCharacterReference(".", null);
        tokeniser.consumeCharacterReference("0", null);
        tokeniser.consumeCharacterReference("\"", null);
        tokeniser.consumeCharacterReference("?", null);
        tokeniser.consumeCharacterReference(">", null);
        tokeniser.transition(TokeniserState.TagSoup);
        tokeniser.emit("html");
        tokeniser.consumeCharacterReference("P", false);
        tokeniser.consumeCharacterReference("U", false);
        tokeniser.consumeCharacterReference("B", false);
        tokeniser.consumeCharacterReference("L", false);
        tokeniser.consumeCharacterReference("I", false);
        tokeniser.consumeCharacterReference("C", false);
        tokeniser.consumeCharacterReference(" ", false);
        tokeniser.consumeCharacterReference("/", false);
        tokeniser.consumeCharacterReference("D", false);
        tokeniser.consumeCharacterReference("O", false);
        tokeniser.consumeCharacterReference("C", false);
        tokeniser.consumeCharacterReference("T", false);
        tokeniser.consumeCharacterReference("Y", false);
        tokeniser.consumeCharacterReference(" ", false);
        tokeniser.consumeCharacterReference("P", false);
        tokeniser.consumeCharacterReference("U", false);
        tokeniser.consumeCharacterReference("B", false);
        tokeniser.consumeCharacterReference("L", false);
        tokeniser.consumeCharacterReference("I", false);
        tokeniser.consumeCharacterReference("C", false);
        tokeniser.consumeCharacterReference("/", false);
        tokeniser.consumeCharacterReference("X", false);
        tokeniser.consumeCharacterReference("H", false);
        tokeniser.consumeCharacterReference("T", false);
        tokeniser.consumeCharacterReference("M", false);
        tokeniser.consumeCharacterReference("L", false);
        tokeniser.consumeCharacterReference(" ", false);
        tokeniser.consumeCharacterReference("D", false);
        tokeniser.consumeCharacterReference("T", false);
        tokeniser.consumeCharacterReference("D", false);
        tokeniser.consumeCharacterReference("/", false);
        tokeniser.consumeCharacterReference("x", false);
        tokeniser.consumeCharacterReference("h", false);
        tokeniser.consumeCharacterReference("t", false);
        tokeniser.consumeCharacterReference("m", false);
        tokeniser.consumeCharacterReference("l", false);
        tokeniser.consumeCharacterReference(" ", false);
        tokeniser.consumeCharacterReference("1", null);
        tokeniser.consumeCharacterReference(".", null);
        tokeniser.consumeCharacterReference("0", null);
        tokeniser.consumeCharacterReference("\"", null);
        tokeniser.consumeCharacterReference(">", null);
        tokeniser.consumeCharacterReference(">", null);
        tokeniser.transition(TokeniserState.InTag);
        tokeniser.emit("html");
        tokeniser.consumeCharacterReference("/", false);
        tokeniser.createTagPending(true);
        tokeniser.consumeCharacterReference(">", false);
        tokeniser.transition(TokeniserState.InTag);
        tokeniser.emit("head");
        tokeniser.consumeCharacterReference("/", false);
        tokeniser.createTagPending(true);
        tokeniser.consumeCharacterReference(">", false);
        tokeniser.transition(TokeniserState.InTag);
        tokeniser.emit("title");
        tokeniser.consumeCharacterReference("/", false);
        tokeniser.createTagPending(true);
        tokeniser.consumeCharacterReference(">", false);
        tokeniser.transition(TokeniserState.InTag);
        tokeniser.emit("Test");
        tokeniser.emit("</title>");
        tokeniser.transition(TokeniserState.InTag);
        tokeniser.emit("body");
        tokeniser.consumeCharacterReference("/", false);
        tokeniser.createTagPending(true);
        tokeniser.consumeCharacterReference(">", false);
        tokeniser.transition(TokeniserState.InTag);
        tokeniser.emit("p");
        tokeniser.consumeCharacterReference("/", false);
        tokeniser.createTagPending(true);
        tokeniser.consumeCharacterReference(">", false);
        tokeniser.transition(TokeniserState.InTag);
        tokeniser.emit("Hello World!");
        tokeniser.emit("</p>");
        tokeniser.emit("</body>");
        tokeniser.emit("</html>");

        // Then
        List<Token> tokens = new ArrayList<>();
        Token tokenToAdd;
        while ((tokenToAdd = tokeniser.read()) != null) {
            tokens.add(tokenToAdd);
        }
        assertEquals(17, tokens.size());
        Token doctype = tokens.get(0);
        assertEquals(Token.TokenType.Doctype, doctype.type);
        assertSame(TokeniserState.Data, tokeniser.getState());
        assertNull(doctype.tagName);
        assertEquals("xml version=\"1.0\" encoding=\"UTF-8\"?", doctype.data);
        assertNull(doctype.attributes);

        Token html = tokens.get(1);
        assertEquals(Token.TokenType.StartTag, html.type);
        assertEquals("html", html.tagName);
        assertEquals(null, html.data);
        List<Attribute> htmlAttributes = html.attributes;
        assertEquals(0, htmlAttributes.size());

        Token head = tokens.get(2);
        assertEquals(Token.TokenType.StartTag, head.type);
        assertEquals("head", head.tagName);
        assertEquals(null, head.data);
        List<Attribute> headAttributes = head.attributes;
        assertEquals(0, headAttributes.size());

        Token title = tokens.get(3);
        assertEquals(Token.TokenType.StartTag, title.type);
        assertEquals("title", title.tagName);
        assertEquals("Test", title.data);
        List<Attribute> titleAttributes = title.attributes;
        assertEquals(0, titleAttributes.size());

        Token p = tokens.get(11);
        assertEquals(Token.TokenType.StartTag, p.type);
        assertEquals("p", p.tagName);
        assertEquals(null, p.data);
        List<Attribute> pAttributes = p.attributes;
        assertEquals(0, pAttributes.size());

        Token HelloWorld = tokens.get(13);
        assertEquals(Token.TokenType.Text, HelloWorld.type);
        assertEquals("Hello World!", HelloWorld.data);
        assertEquals(null, HelloWorld.tagName);
        assertEquals(null, HelloWorld.attributes);

        Token pEnd = tokens.get(14);
        assertEquals(Token.TokenType.EndTag, pEnd.type);
        assertEquals("p", pEnd.tagName);
        assertEquals(null, pEnd.data);
        assertEquals(null, pEnd.attributes);

        Token bodyEnd = tokens.get(15);
        assertEquals(Token.TokenType.EndTag, bodyEnd.type);
        assertEquals("body", bodyEnd.tagName);
        assertEquals(null, bodyEnd.data);
        assertEquals(null, bodyEnd.attributes);

        Token htmlEnd = tokens.get(16);
        assertEquals(Token.TokenType.EndTag, htmlEnd.type);
        assertEquals("html", htmlEnd.tagName);
        assertEquals(null, htmlEnd.data);
        assertEquals(null, htmlEnd.attributes);

        Token lastStartTag = tokens.get(11);
        Token lastEndTag = tokens.get(15);
        assertTrue(tokeniser.isAppropriateEndTagToken());
        assertEquals("p", tokeniser.appropriateEndTagName());

        // Clean up
        reader = mock(CharacterReader.class);
        tokeniser = new Tokeniser(reader, errors);
    }
}