import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokenType;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

    private CharacterReader reader;
    private ParseErrorList errors;
    private Tokeniser tokeniser;

    @Before
    public void setup() {
        reader = Mockito.mock(CharacterReader.class);
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testReadEmptyInput() {
        Mockito.when(reader.current()).thenReturn((char) 0);
        Mockito.when(reader.isEmpty()).thenReturn(true);

        Token token = tokeniser.read();
        assertNull(token);
        Mockito.verify(reader).advance();
    }

    @Test
    public void testReadSingleCharacter() {
        Mockito.when(reader.current()).thenReturn('a');
        Mockito.when(reader.matchConsume(ArgumentMatchers.any())).thenReturn(true);

        Token token = tokeniser.read();
        Token.Character characterToken = (Token.Character) token;
        assertEquals("a", characterToken.data);
        assertEquals(TokenType.Character, token.type);
        assertEquals("", characterToken.tagName);
        assertFalse(characterToken.selfClosing);
        Mockito.verify(reader).advance();
    }

    @Test
    public void testReadSelfClosingStartTag() {
        String input = "<img src=\"image.jpg\" alt=\"Image\" />";
        Mockito.when(reader.current()).thenReturn('i');
        Mockito.when(reader.matchConsume("<")).thenReturn(true);
        Mockito.when(reader.matchConsume('/')).thenReturn(true);
        Mockito.when(reader.matchConsume('>')).thenReturn(true);

        TokeniserState state = new TokeniserStateData();
        tokeniser.transition(state);

        Token startTag = tokeniser.createTagPending(true);
        tokeniser.emitTagPending();

        Mockito.verify(reader, Mockito.times(3)).matchConsume(ArgumentMatchers.anyChar());
        Mockito.verify(reader).advance();

        Token token = tokeniser.read();
        Token.Character characterToken = (Token.Character) token;
        assertEquals("i", characterToken.data);
        assertEquals(TokenType.Character, token.type);
        assertEquals("img", startTag.tagName);
        assertTrue(startTag.selfClosing);
    }

    @Test
    public void testReadStartTagWithAttributes() {
        String input = "<div id='container' class='box' title='Hello'>Content</div>";
        Mockito.when(reader.current()).thenReturn('d');
        Mockito.when(reader.matchConsume("<")).thenReturn(true);

        TokeniserState state = new TokeniserStateDiv();
        tokeniser.transition(state);

        Token startTag = tokeniser.createTagPending(true);
        tokeniser.emitTagPending();

        Mockito.verify(reader, Mockito.times(2)).matchConsume(ArgumentMatchers.anyChar());
        Mockito.verify(reader).advance();

        tokeniser.emit(" id='container'");
        tokeniser.emit(" class='box'");
        tokeniser.emit(" title='Hello'>");

        Token token = tokeniser.read();
        Token.StartTag tag = (Token.StartTag) token;
        assertEquals("div", tag.tagName);
        assertEquals("container", tag.attributes.get("id"));
        assertEquals("box", tag.attributes.get("class"));
        assertNull(tag.attributes.get("title"));
        assertEquals(1, tag.attributes.size());
        assertFalse(tag.selfClosing);

        Mockito.verify(reader).matchConsumeIgnoreCase("Content");
        Mockito.verify(reader).matchConsume('>');
    }

    @Test
    public void testReadEndTag() {
        String input = "<div id='container'>Content<div id='nested'></div></div>";
        Mockito.when(reader.current()).thenReturn('d');
        Mockito.when(reader.matchConsume("<")).thenReturn(true);

        TokeniserState state = new TokeniserStateDiv();
        tokeniser.transition(state);

        Token startTag1 = tokeniser.createTagPending(true);
        tokeniser.emitTagPending();

        Mockito.when(reader).matchConsumeIgnoreCase("div").thenReturn(true);
        Mockito.when(reader).matchConsume('/').thenReturn(true);
        Mockito.when(reader).matchConsume('>').thenReturn(true);

        Token startTag2 = tokeniser.createTagPending(true);
        tokeniser.emitTagPending();

        Mockito.when(reader).matchConsumeIgnoreCase("div").thenReturn(true);
        Mockito.when(reader).matchConsume('/').thenReturn(true);
        Mockito.when(reader).matchConsume('>').thenReturn(true);

        tokeniser.emit(startTag2.tagName);

        Token endTag = tokeniser.read();
        Token.EndTag tag = (Token.EndTag) endTag;
        assertEquals("div", tag.tagName);
        assertEquals(1, tag.attributes.size());
        assertTrue(tag.selfClosing);

        Mockito.verify(reader).matchConsumeIgnoreCase("div");
        Mockito.verify(reader).matchConsume('/');
        Mockito.verify(reader).matchConsume('>');
    }

    @Test
    public void testReadInvalidCharacterReference() {
        String input = "&invalid;";
        Mockito.when(reader.current()).thenReturn('&');

        tokeniser.consumeCharacterReference(null, false);

        Mockito.verify(reader).rewindToMark();
        Mockito.verify(tokeniser.errors).add(Mockito.any(ParseError.class));
    }

    @Test
    public void testReadCharacterReference() {
        String input = "&alpha;";
        Mockito.when(reader.current()).thenReturn('&');

        tokeniser.consumeCharacterReference(null, false);
        assertEquals('α', tokeniser.read().getData().charAt(0));
    }

    @Test
    public void testReadCharacterReferenceWithAdditionalAllowedCharacter() {
        String input = "&eacute;&#233;";
        Mockito.when(reader.current()).thenReturn('&');

        tokeniser.consumeCharacterReference('c', false);
        assertEquals('é', tokeniser.read().getData().charAt(0));
        Mockito.when(reader.current()).thenReturn('#');
        tokeniser.consumeCharacterReference(null, false);
        assertEquals('ĺ', tokeniser.read().getData().charAt(0));
    }

    @Test
    public void testReadCharacterReferenceWithLeadingWhitespace() {
        String input = " &alpha; ";
        Mockito.when(reader.current()).thenReturn(' ');

        tokeniser.consumeCharacterReference(null, false);
        assertEquals('α', tokeniser.read().getData().charAt(0));
    }

    @Test
    public void testReadCharacterReferenceWithTrailingWhitespace() {
        String input = "&alpha; ";
        Mockito.when(reader.current()).thenReturn(' ');

        tokeniser.consumeCharacterReference(null, false);
        assertEquals('α', tokeniser.read().getData().charAt(0));
        Mockito.verify(reader).advance();
        assertEquals(' ', reader.current());
        Mockito.verify(reader).matchConsume(';');
    }

    @Test
    public void testReadCharacterReferenceWithTrailingSemicolon() {
        String input = "&alpha;&#x233;";
        Mockito.when(reader.current()).thenReturn('&');

        tokeniser.consumeCharacterReference(null, false);
        assertEquals('α', tokeniser.read().getData().charAt(0));
        Mockito.when(reader.current()).thenReturn(';');

        tokeniser.consumeCharacterReference(null, false);
        assertEquals('ĺ', tokeniser.read().getData().charAt(0));
    }

    @Test
    public void testReadNamedCharacterReference() {
        String input = "&Agrave;";
        Mockito.when(reader.current()).thenReturn('&');

        tokeniser.consumeCharacterReference(null, false);
        assertEquals('À', tokeniser.read().getData().charAt(0));
    }

    @Test
    public void testReadNamedCharacterReferenceWithTrailingWhitespace() {
        String input = " &Agrave; ";
        Mockito.when(reader.current()).thenReturn(' ');

        tokeniser.consumeCharacterReference(null, false);
        assertEquals('À', tokeniser.read().getData().charAt(0));
    }

    @Test
    public void testReadInvalidNamedCharacterReference() {
        String input = "&Invalid;";
        Mockito.when(reader.current()).thenReturn('&');

        tokeniser.consumeCharacterReference(null, false);

        Mockito.verify(reader).rewindToMark();
        Mockito.verify(tokeniser.errors).add(Mockito.any(ParseError.class));
    }

    @Test
    public void testReadInvalidEndTagName() {
        String input = "<div><divabc></div>";
        Mockito.when(reader.current()).thenReturn('a');

        tokeniser.transition(new TokeniserStateData());
        tokeniser.emit("<div");
        tokeniser.emit("abc");
        tokeniser.error(TokeniserState.Data);

        Mockito.verify(tokeniser.errors).add(Mockito.any(ParseError.class));
    }

    @Test
    public void testReadEndTagMismatch() {
        String input = "<div><div></p>";
        Mockito.when(reader.current()).thenReturn('p');

        TokeniserState state = new TokeniserStateDiv();
        tokeniser.transition(state);

        tokeniser.emit("<div");
        tokeniser.emit(">");
        tokeniser.emit("<p");
        tokeniser.emit("/");

        Mockito.verify(tokeniser.errors).add(Mockito.any(ParseError.class));
    }

    @Test
    public void testReadEOF() {
        String input = "<div>";
        Mockito.when(reader.current()).thenReturn('d');

        TokeniserState state = new TokeniserStateDiv();
        tokeniser.transition(state);

        tokeniser.emit("<div");
        tokeniser.emit(">");

        Mockito.when(reader.isEmpty()).thenReturn(true);
        Mockito.when(reader.matchConsumeAny(ArgumnetMatchers.array('\t', '\n', '\f', ' '))).thenReturn(true);

        tokeniser.eofError(TokeniserState.Data);

        Mockito.verify(tokeniser.errors).add(Mockito.any(ParseError.class));
    }
}

class TokeniserStateDiv extends TokeniserState {
    @Override
    public void read(Tokeniser tokeniser, CharacterReader reader) {
        if (reader.matchConsume("<")) {
            tokeniser.transition(new TokeniserStateDivTag());
        } else {
            tokeniser.transition(TokeniserState.Data);
        }
    }
}

class TokeniserStateDivTag extends TokeniserState {
    @Override
    public void read(Tokeniser tokeniser, CharacterReader reader) {
        if (reader.matchConsume("/")) {
            tokeniser.transition(new TokeniserStateDivEndTag());
        } else {
            tokeniser.emit(reader.current());
            reader.advance();
        }
    }
}

class TokeniserStateData extends TokeniserState {
    @Override
    public void read(Tokeniser tokeniser, CharacterReader reader) {
        if (reader.matchConsume("<")) {
            tokeniser.transition(new TokeniserStateStartTag());
        } else {
            tokeniser.emit(reader.current());
            reader.advance();
        }
    }
}

class TokeniserStateStartTag extends TokeniserState {
    @Override
    public void read(Tokeniser tokeniser, CharacterReader reader) {
        if (reader.matchConsume('/')) {
            tokeniser.transition(new TokeniserStateEndTag());
        } else {
            tokeniser.emitTagPending();
            List<String> attributes = new ArrayList<>();
            String attributeName = null;
            tokeniser.emit("/");
            reader.advance();
            tokeniser.acknowledgeSelfClosingFlag();

            while (!reader.isEmpty() && reader.current() != '>') {
                if (reader.matchConsume('=')) {
                    tokeniser.consumeCharacterReference(null, true);
                    String attributeValue = tokeniser.read().getData();
                    attributes.add(attributeName + "=\"" + attributeValue + "\"");
                    attributeName = null;
                } else {
                    if (attributeName == null) {
                        attributeName = reader.consumeLetterSequence();
                    } else {
                        attributeName += reader.consumeLetterSequence();
                    }
                }
                reader.advance();
            }
            tokeniser.emitTagPending().attributes = attributes;
        }
    }
}

class TokeniserStateEndTag extends TokeniserState {
    @Override
    public void read(Tokeniser tokeniser, CharacterReader reader) {
        if (reader.matchConsume('>')) {
            tokeniser.transition(tokeniser.getState());
        } else {
            tokeniser.error(tokeniser.getState());
        }
    }
}