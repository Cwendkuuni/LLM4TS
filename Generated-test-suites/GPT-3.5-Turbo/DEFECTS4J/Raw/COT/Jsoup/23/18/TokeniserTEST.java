
import static org.junit.Assert.*;
import org.junit.Test;

public class TokeniserTest {
	
	@Test
	public void testRead() {
		CharacterReader reader = new CharacterReader("Test Input");
		ParseErrorList errors = new ParseErrorList();
		Tokeniser tokeniser = new Tokeniser(reader, errors);
		
		Token token = tokeniser.read();
		assertEquals(new Token.Character("Test Input"), token);
	}
	
	@Test
	public void testEmit() {
		Token.StartTag startTag = new Token.StartTag();
		Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
		
		tokeniser.emit(startTag);
		assertTrue(tokeniser.isEmitPending);
	}
	
	@Test
	public void testEmitString() {
		Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
		
		tokeniser.emit("Test");
		assertEquals("Test", tokeniser.charBuffer.toString());
	}
	
	@Test
	public void testEmitChar() {
		Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
		
		tokeniser.emit('T');
		assertEquals("T", tokeniser.charBuffer.toString());
	}
	
	@Test
	public void testGetState() {
		TokeniserState state = TokeniserState.Data;
		Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
		
		assertEquals(state, tokeniser.getState());
	}
	
	@Test
	public void testTransition() {
		TokeniserState state = TokeniserState.Rcdata;
		Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
		
		tokeniser.transition(state);
		assertEquals(state, tokeniser.getState());
	}
	
	@Test
	public void testAdvanceTransition() {
		TokeniserState state = TokeniserState.Rcdata;
		Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
		
		tokeniser.advanceTransition(state);
		assertEquals(state, tokeniser.getState());
	}
	
	@Test
	public void testAcknowledgeSelfClosingFlag() {
		Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
		
		tokeniser.acknowledgeSelfClosingFlag();
		assertTrue(tokeniser.selfClosingFlagAcknowledged);
	}
	
	@Test
	public void testConsumeCharacterReference() {
		// Test when reader is empty
		CharacterReader reader1 = new CharacterReader("");
		Tokeniser tokeniser1 = new Tokeniser(reader1, new ParseErrorList());
		
		assertNull(tokeniser1.consumeCharacterReference(null, false));
		
		// Test when additionalAllowedCharacter is matched
		CharacterReader reader2 = new CharacterReader("Test");
		Tokeniser tokeniser2 = new Tokeniser(reader2, new ParseErrorList());
		
		assertNull(tokeniser2.consumeCharacterReference('T', false));
		
		// Test when current character is any of \t, \n, \f, , <, &
		CharacterReader reader3 = new CharacterReader("&Test");
		Tokeniser tokeniser3 = new Tokeniser(reader3, new ParseErrorList());
		
		assertNull(tokeniser3.consumeCharacterReference(null, false));
		
		// Test when it is a numbered reference and it matches
		CharacterReader reader4 = new CharacterReader("&#65;");
		Tokeniser tokeniser4 = new Tokeniser(reader4, new ParseErrorList());
		
		assertEquals('A', tokeniser4.consumeCharacterReference(null, false));
		
		// Test when it is a named reference and it matches
		CharacterReader reader5 = new CharacterReader("&amp;");
		Tokeniser tokeniser5 = new Tokeniser(reader5, new ParseErrorList());
		
		assertEquals('&', tokeniser5.consumeCharacterReference(null, false));
		
		// Test when it is a named reference with invalid character following it
		CharacterReader reader6 = new CharacterReader("&ampA");
		Tokeniser tokeniser6 = new Tokeniser(reader6, new ParseErrorList());
		
		assertNull(tokeniser6.consumeCharacterReference(null, true));
		
		// Test when it is a named reference and the name reference does not match
		CharacterReader reader7 = new CharacterReader("&random;");
		Tokeniser tokeniser7 = new Tokeniser(reader7, new ParseErrorList());
		
		assertNull(tokeniser7.consumeCharacterReference(null, false));
	}
	
	@Test
	public void testCreateTagPending() {
		boolean start = true;
		Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
		
		assertTrue(tokeniser.createTagPending(start) instanceof Token.StartTag);
		assertEquals(tokeniser.tagPending, tokeniser.createTagPending(start));
	}
	
	@Test
	public void testEmitTagPending() {
		Token.StartTag startTag = new Token.StartTag();
		Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
		tokeniser.tagPending = startTag;
		
		tokeniser.emitTagPending();
		assertTrue(tokeniser.isEmitPending);
	}
	
	@Test
	public void testCreateCommentPending() {
		Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
		
		tokeniser.createCommentPending();
		assertTrue(tokeniser.commentPending instanceof Token.Comment);
	}
	
	@Test
	public void testEmitCommentPending() {
		Token.Comment commentPending = new Token.Comment();
		Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
		tokeniser.commentPending = commentPending;
		
		tokeniser.emitCommentPending();
		assertTrue(tokeniser.isEmitPending);
	}
	
	@Test
	public void testCreateDoctypePending() {
		Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
		
		tokeniser.createDoctypePending();
		assertTrue(tokeniser.doctypePending instanceof Token.Doctype);
	}
	
	@Test
	public void testEmitDoctypePending() {
		Token.Doctype doctypePending = new Token.Doctype();
		Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
		tokeniser.doctypePending = doctypePending;
		
		tokeniser.emitDoctypePending();
		assertTrue(tokeniser.isEmitPending);
	}
	
	@Test
	public void testCreateTempBuffer() {
		Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
		
		tokeniser.createTempBuffer();
		assertTrue(tokeniser.dataBuffer instanceof StringBuilder);
	}
	
	@Test
	public void testIsAppropriateEndTagToken() {
		Token.StartTag startTag = new Token.StartTag();
		Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
		tokeniser.tagPending = startTag;
		tokeniser.lastStartTag = startTag;
		
		assertTrue(tokeniser.isAppropriateEndTagToken());
	}
	
	@Test
	public void testAppropriateEndTagName()  {
		Token.StartTag startTag = new Token.StartTag();
		Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
		tokeniser.lastStartTag = startTag;
		tokeniser.tagPending = startTag;
	
		assertEquals(tokeniser.lastStartTag.tagName, tokeniser.appropriateEndTagName());
	}
	
	@Test
	public void testError() {
		TokeniserState state = TokeniserState.Data;
		Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
		
		tokeniser.error(state);
		assertTrue(tokeniser.errors.get(0).getErrorMsg().contains("Unexpected character '%s' in input state"));
	}
	
	@Test
	public void testEofError() {
		TokeniserState state = TokeniserState.Data;
		Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
		
		tokeniser.eofError(state);
		assertTrue(tokeniser.errors.get(0).getErrorMsg().contains("Unexpectedly reached end of file (EOF) in input state"));
	}
	
	@Test
	public void testCharacterReferenceError() {
		Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
		
		tokeniser.characterReferenceError("error");
		assertTrue(tokeniser.errors.get(0).getErrorMsg().contains("Invalid character reference"));
	}
	
	@Test
	public void testErrorString() {
		Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
		
		tokeniser.error("error");
		assertTrue(tokeniser.errors.get(0).getErrorMsg().contains("error"));
	}
	
	@Test
	public void testCurrentNodeInHtmlNS() {
		Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), new ParseErrorList());
		
		assertTrue(tokeniser.currentNodeInHtmlNS());
	}
}

