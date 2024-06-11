package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Tokeniser;
import org.junit.Test;

public class TokeniserTest {

    @Test
    public void testTransitionStates() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        assertEquals(TokeniserState.Data, tokeniser.getState());

        tokeniser.transition(TokeniserState.Script);
        assertEquals(TokeniserState.Script, tokeniser.getState());

        tokeniser.transition(TokeniserState.Style);
        assertEquals(TokeniserState.Style, tokeniser.getState());

        tokeniser.transition(TokeniserState.TagStart);
        assertEquals(TokeniserState.TagStart, tokeniser.getState());

        tokeniser.transition(TokeniserState.TagName);
        assertEquals(TokeniserState.TagName, tokeniser.getState());

        tokeniser.transition(TokeniserState.TagEnd);
        assertEquals(TokeniserState.TagEnd, tokeniser.getState());

        tokeniser.transition(TokeniserState.Text);
        assertEquals(TokeniserState.Text, tokeniser.getState());

        tokeniser.transition(TokeniserState.Doctype);
        assertEquals(TokeniserState.Doctype, tokeniser.getState());

        tokeniser.transition(TokeniserState.Error);
        assertEquals(TokeniserState.Error, tokeniser.getState());

        tokeniser.transition(TokeniserState.Ignore);
        assertEquals(TokeniserState.Ignore, tokeniser.getState());
    }

    @Test
    public void testCharacterConsumption() {
        Tokeniser tokeniser = new Tokeniser(null, null);

        // Test normal character consumption
        tokeniser.emit("abc");
        assertEquals("abc", tokeniser.charBuffer.toString());

        // Test character reference consumption
        tokeniser.emit("&a;");
        assertEquals('a', tokeniser.charBuffer.charAt(0));

        tokeniser.emit("&#x1F345;");
        assertEquals('\uD835\uDE45', tokeniser.charBuffer.getCodePointAt(0));

        tokeniser.emit("&nbsp;");
        assertEquals('\u00A0', tokeniser.charBuffer.charAt(0));

        tokeniser.emit("&amp;");
        assertEquals('&', tokeniser.charBuffer.charAt(0));

        tokeniser.emit("&quot;");
        assertEquals('"', tokeniser.charBuffer.charAt(0));

        tokeniser.emit("&gt;");
        assertEquals('>', tokeniser.charBuffer.charAt(0));

        tokeniser.emit("&lt;");
        assertEquals('<', tokeniser.charBuffer.charAt(0));

        // Test invalid character reference consumption
        tokeniser.emit("&foo;");
        assertTrue(tokeniser.charBuffer.length() == 0);
    }

    @Test
    public void testTagConsumption() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createTagPending(true);

        tokeniser.emit("<html>");
        tokeniser.emitTagPending();
        assertEquals("html", tokeniser.tagPending.tagName);

        tokeniser.createTagPending(false);
        tokeniser.emit("</html>");
        tokeniser.emitCommentPending();
        tokeniser.emitCommentPending();
        tokeniser.emitEndTag();
        tokeniser.emitEndTag();
        tokeniser.emitTagPending();
        tokeniser.emitTagPending();
        tokeniser.emit(new Character(""));

        assertTrue(tokeniser.isAppropriateEndTagToken());
        assertEquals("html", tokeniser.appropriateEndTagName());
    }

    @Test
    public void testDoctypeConsumption() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.createDoctypePending();

        tokeniser.emit("<!DOCTYPE html>");
        tokeniser.emitDoctypePending();
        assertEquals("html", tokeniser.doctypePending.publicId);
        assertEquals("", tokeniser.doctypePending.systemId);
    }

    @Test
    public void testErrorHandling() {
        Tokeniser tokeniser = new Tokeniser(null, null);
        tokeniser.transition(TokeniserState.Error);

        tokeniser.emit("foo");
        assertEquals("foo", tokeniser.charBuffer.toString());

        tokeniser.error(TokeniserState.Error);
        assertTrue(tokeniser.charBuffer.length() == 0);
    }

    @Test
    public void testConsumeCharacterReferenceIgnoringInAttribute() {
        Tokeniser tokeniser = new Tokeniser(null, null);

        tokeniser.emit("&a; <input type='text' name='foo&b;'>");
        assertEquals('a', tokeniser.charBuffer.charAt(0));

        tokeniser.advanceTransition(TokeniserState.TagName);
        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.consumeCharacterReference(null, true);
        assertEquals('b', tokeniser.charBuffer.charAt(0));
    }
}