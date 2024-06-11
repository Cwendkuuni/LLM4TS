package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
import org.jsoup.parser.TokeniserState;

public class TokeniserTest {

	private static CharacterReader createMockReader(String input) {
		ByteArrayInputStream byteStream = new ByteArrayInputStream(input.getBytes());
		return new CharacterReader(byteStream);
	}

	private static ParseErrorList createMockErrors() {
		return new ParseErrorList();
	}

	@org.junit.Test
	public void testTokeniser() throws IOException {
		String input = "<html><head><title>Test</title></head><body><p>Hello World</p></body></html>";
		CharacterReader reader = createMockReader(input);
		ParseErrorList errors = createMockErrors();
		Tokeniser tokeniser = new Tokeniser(reader, errors);

		// Test initial state
		assertEquals(TokeniserState.Data, tokeniser.getState());

		// Test reading tokens
		for (int i = 0; i < input.length(); i++) {
			Token token = tokeniser.read();
			assertNotNull(token);
			switch (input.charAt(i)) {
				case '<':
					assertEquals(Token.TokenType.StartTag, token.type);
					break;
				case '>':
					assertEquals(Token.TokenType.EndTag, token.type);
					break;
				case '/':
					assertEquals(Token.TokenType.EndTag, token.type);
					break;
				case '!':
					assertEquals(Token.TokenType.Doctype, token.type);
					break;
				case ' ':
				case '\t':
					assertEquals(Token.TokenType.Whitespace, token.type);
					break;
				case '>':
					assertEquals(Token.TokenType.Character, token.type);
					assertEquals(Character.getText(), Character.toString(input.charAt(i)));
					break;
				default:
					assertEquals(Token.TokenType.Character, token.type);
					assertEquals(Character.getText(), Character.toString(input.charAt(i)));
					break;
			}
		}

		// Test reading end tag
		tokeniser.transition(TokeniserState.InEndTag);
		assertEquals(Token.TokenType.EndTag, tokeniser.read().type);

		// Test reading self-closing tag
		tokeniser.transition(TokeniserState.InSelfClosingTag);
		assertEquals(Token.TokenType.EndTag, tokeniser.read().type);

		// Test reading start tag
		tokeniser.transition(TokeniserState.InStartTag);
		assertEquals(Token.TokenType.StartTag, tokeniser.read().type);

		// Test reading comment
		tokeniser.transition(TokeniserState.InComment);
		assertEquals(Token.TokenType.Comment, tokeniser.read().type);

		// Test reading doctype
		tokeniser.transition(TokeniserState.InDoctype);
		assertEquals(Token.TokenType.Doctype, tokeniser.read().type);

		// Test error handling
		tokeniser.transition(TokeniserState.Data);
		tokeniser.error(TokeniserState.Data);
		assertNotNull(errors.get(0));

		// Test reading eof
		reader.close();
		assertEquals(Token.TokenType.Eof, tokeniser.read().type);
	}

	@org.junit.Test
	public void testCharacterReference() throws IOException {
		CharacterReader reader = createMockReader("&hellip;");
		ParseErrorList errors = createMockErrors();
		Tokeniser tokeniser = new Tokeniser(reader, errors);

		tokeniser.transition(TokeniserState.Data);
		Character charRef = tokeniser.consumeCharacterReference(null, false);
		assertEquals(Character.getText(), "…");
		assertNull(tokeniser.read());

		tokeniser.transition(TokeniserState.InAttribute);
		charRef = tokeniser.consumeCharacterReference('x', true);
		assertEquals(Character.getText(), "x");
		assertNull(tokeniser.read());

		reader.close();
		assertNull(tokeniser.read());
	}

	@org.junit.Test
	public void testNamedCharacterReference() throws IOException {
		CharacterReader reader = createMockReader("&nbsp;");
		ParseErrorList errors = createMockErrors();
		Tokeniser tokeniser = new Tokeniser(reader, errors);

		tokeniser.transition(TokeniserState.Data);
		assertEquals(Entities.getCharacterByName("nbsp"), tokeniser.read().type);
		assertNull(tokeniser.read());

		reader.close();
		assertNull(tokeniser.read());
	}

	@org.junit.Test
	public void testEmptyInput() throws IOException {
		CharacterReader reader = createMockReader("");
		ParseErrorList errors = createMockErrors();
		Tokeniser tokeniser = new Tokeniser(reader, errors);

		assertEquals(Token.TokenType.Eof, tokeniser.read().type);
		assertNull(tokeniser.read());
	}
}

class ParseErrorList extends ArrayList<ParseError> {
	private static final long serialVersionUID = 1L;

	public boolean canAddError() {
		return size() < 10;
	}
}

class ParseError {
	private int pos;
	private int lineNumber;
	private int columnNumber;
	private String errorMessage;

	public ParseError(int pos, String errorMessage) {
		this.pos = pos;
		this.lineNumber = (pos / 1024) + 1;
		this.columnNumber = (pos % 1024) + 1;
		this.errorMessage = errorMessage;
	}

	public int getPos() {
		return pos;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public int getColumnNumber() {
		return columnNumber;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void printError() {
		System.out.println(String.format("%d:%d:%d: %s", getLineNumber(), getColumnNumber(), getPos(), getErrorMessage()));
	}
}