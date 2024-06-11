package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.nodes.Token;
import org.jsoup.nodes.Token.Attribute;
import org.jsoup.nodes.Token.Character;
import org.jsoup.nodes.Token.Comment;
import org.jsoup.nodes.Token.Doctype;
import org.jsoup.nodes.Token.EndTag;
import org.jsoup.nodes.Token.StartTag;
import org.jsoup.parser.Tokeniser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    @Test
    public void testTokeniser() {
        String html = "<html lang=\"en\"><head><title>Test</title></head><body><h1>Hello, world!</h1></body></html>";
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes());
        ParseErrorList errors = new ArrayList<>();

        Tokeniser tokeniser = new Tokeniser(new CharacterReader(inputStream), errors);

        Token token;
        while ((token = tokeniser.read()) != null) {
            System.out.println(token.toString());
        }

        assertEquals(2, errors.size());

        Tokeniser tokeniserError = new Tokeniser(new CharacterReader(new ByteArrayInputStream("Invalid character".getBytes())), errors);
        tokeniserError.read();

        assertEquals(1, errors.size());
        assertEquals("Unexpected character 'I' in input state [Data]", errors.get(0).getMessage());

        // Test getState and transition
        assertEquals(TokeniserState.Data, tokeniser.getState());
        tokeniser.transition(TokeniserState.Script);
        assertEquals(TokeniserState.Script, tokeniser.getState());

        // Test acknowledgeSelfClosingFlag
        tokeniser.acknowledgeSelfClosingFlag();
        assertFalse(tokeniser.selfClosingFlagAcknowledged);

        tokeniser.transition(TokeniserState.Script);
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.transition(TokeniserState.Data);
        assertTrue(tokeniser.selfClosingFlagAcknowledged);

        // Test emit and read
        tokeniser.emit(new StartTag("html", null, null, null, null));
        token = tokeniser.read();
        assertEquals(new StartTag("html", null, null, null, null), token);

        tokeniser.emit(new Character(" "));
        tokeniser.emit(new Character('l'));
        tokeniser.emit(new Character('a'));
        tokeniser.emit(new Character('n'));
        tokeniser.emit(new Character('g'));
        tokeniser.emit(new Character('='));
        tokeniser.emit(new Character('e'));
        tokeniser.emit(new Character('n'));
        tokeniser.emit(new Character('>'));

        token = tokeniser.read();
        assertEquals(new Attribute("lang", "en"), token);

        tokeniser.emit(new Character(' '));
        tokeniser.emit(new Character('<'));
        tokeniser.emit(new Character('/'));
        tokeniser.emit(new Character('h'));
        tokeniser.emit(new Character('t'));
        tokeniser.emit(new Character('m'));
        tokeniser.emit(new Character('l'));
        tokeniser.emit(new Character('>'));

        token = tokeniser.read();
        assertEquals(new EndTag("html"), token);

        // Test consumeCharacterReference
        tokeniser.transition(TokeniserState.CharacterReference);
        assertEquals(Character.class, tokeniser.getClassOfNextToken().getComponentType());

        Character c1 = tokeniser.consumeCharacterReference(null, false);
        assertNull(c1);

        Character c2 = tokeniser.consumeCharacterReference('&', false);
        assertNull(c2);

        Character c3 = tokeniser.consumeCharacterReference(null, true);
        assertNull(c3);

        Character c4 = tokeniser.consumeCharacterReference('&', true);
        assertEquals(Entities.getCharacterByName("amp"), c4);

        Character c5 = tokeniser.consumeCharacterReference("#x27", false);
        assertEquals(Character.valueOf('\''), c5);

        Character c6 = tokeniser.consumeCharacterReference("#x27", true);
        assertEquals(Character.valueOf('\''), c6);

        Character c7 = tokeniser.consumeCharacterReference("&nbsp;", false);
        assertEquals(Character.valueOf(' '), c7);

        Character c8 = tokeniser.consumeCharacterReference("&nbsp;", true);
        assertEquals(Character.valueOf(' '), c8);

        Character c9 = tokeniser.consumeCharacterReference("#128;", false);
        assertEquals(Character.valueOf('\u007f'), c9);

        Character c10 = tokeniser.consumeCharacterReference("#128;", true);
        assertEquals(Character.valueOf('\u007f'), c10);

        Character c11 = tokeniser.consumeCharacterReference("&#xFFFD;", false);
        assertEquals(Character.valueOf(Character.replacementChar()), c11);

        Character c12 = tokeniser.consumeCharacterReference("&#xFFFD;", true);
        assertEquals(Character.valueOf(Character.replacementChar()), c12);

        // Test createTagPending, emitTagPending, createCommentPending, emitCommentPending
        tokeniser.transition(TokeniserState.StartTag);
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        assertEquals(new StartTag("html", null, null, null, null), lastStartTag);

        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        assertEquals(new Comment(""), tokeniser.read());

        // Test createDoctypePending and emitDoctypePending
        tokeniser.transition(TokeniserState.Doctype);
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        assertEquals(new Doctype("html", null, null), tokeniser.read());

        // Test createTempBuffer
        tokeniser.transition(TokeniserState.Data);
        tokeniser.createTempBuffer();
        tokeniser.emit("script src=\"myscript.js\" type=\"text/javascript\">");
        tokeniser.emit("script src=\"anotherscript.js\" type=\"application/javascript\" charset=\"UTF-8\"");
        tokeniser.emit("/script>");
        tokeniser.emit("/script>");
        tokeniser.emit("</script>");
        tokeniser.transition(TokeniserState.Script);
        tokeniser.emit(new StartTag("script", null, null, null, null));
        tokeniser.emit(new Character('s'));
        tokeniser.emit(new Character('c'));
        tokeniser.emit(new Character('r'));
        tokeniser.emit(new Character('i'));
        tokeniser.emit(new Character('p'));
        tokeniser.emit(new Character('t'));
        tokeniser.emit(new Character(' '));
        tokeniser.emit(new Character('s'));
        tokeniser.emit(new Character('o'));
        tokeniser.emit(new Character('r'));
        tokeniser.emit(new Character('='));
        tokeniser.emit(new Character('"'));
        tokeniser.emit(new Character('m'));
        tokeniser.emit(new Character('y'));
        tokeniser.emit(new Character('s'));
        tokeniser.emit(new Character('c'));
        tokeniser.emit(new Character('r'));
        tokeniser.emit(new Character('i'));
        tokeniser.emit(new Character('p'));
        tokeniser.emit(new Character('t'));
        tokeniser.emit(new Character('/'));
        tokeniser.emit(new Character('s'));
        tokeniser.emit(new Character('c'));
        tokeniser.emit(new Character('r'));
        tokeniser.emit(new Character('i'));
        tokeniser.emit(new Character('p'));
        tokeniser.emit(new Character('t'));
        tokeniser.emit(new Character('/'));
        tokeniser.emit(new Character('/'));
        tokeniser.emit(new Character('s'));
        tokeniser.emit(new Character('c'));
        tokeniser.emit(new Character('r'));
        tokeniser.emit(new Character('i'));
        tokeniser.emit(new Character('p'));
        tokeniser.emit(new Character('t'));
        tokeniser.emit(new Character('>'));
        tokeniser.transition(TokeniserState.Data);
        List<Token> scriptTags = new ArrayList<>();
        while ((token = tokeniser.read()) != null && token.tagName != null) {
            if (token.tagName.equals("script")) {
                scriptTags.add(token);
            }
        }
        assertEquals(2, scriptTags.size());
        assertEquals("myscript.js", scriptTags.get(0).attributes.get("src").getValue());
        assertEquals("application/javascript", scriptTags.get(0).attributes.get("type").getValue());
        assertEquals("UTF-8", scriptTags.get(1).attributes.get("charset").getValue());
    }
}