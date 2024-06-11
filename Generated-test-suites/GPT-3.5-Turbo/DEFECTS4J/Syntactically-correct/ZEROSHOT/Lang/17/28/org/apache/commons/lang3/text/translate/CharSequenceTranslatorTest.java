package org.apache.commons.lang3.text.translate;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.Writer;
import java.util.Locale;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class CharSequenceTranslatorTest {
	
	private CharSequenceTranslator translator;
	
	@Before
	public void setup() {
		translator = mock(CharSequenceTranslator.class);
	}
	
	@Test
	public void testTranslate_withNullInput_returnsNull() {
		String input = null;
		String expectedOutput = null;
		
		String result = translator.translate(input);
		
		assertEquals(expectedOutput, result);
	}
	
	@Test
	public void testTranslate_withEmptyInput_returnsEmptyString() {
		String input = "";
		String expectedOutput = "";
		
		String result = translator.translate(input);
		
		assertEquals(expectedOutput, result);
	}
	
	@Test
	public void testTranslate_withValidInput_callsTranslateMethod() throws IOException {
		String input = "input";
		StringWriter writer = new StringWriter();
		
		translator.translate(input, writer);
		
		verify(translator).translate(input, 0, writer);
	}
	
	@Test
	public void testTranslate_withNullWriter_throwsIllegalArgumentException() {
		String input = "input";
		Writer writer = null;
		
		try {
			translator.translate(input, writer);
		} catch (IllegalArgumentException e) {
			assertEquals("The Writer must not be null", e.getMessage());
		} catch (IOException e) {
			// Do nothing
		}
	}
	
	@Test
	public void testTranslate_withNullIOException_throwsRuntimeException() throws IOException {
		String input = "input";
		StringWriter writer = new StringWriter();
		
		when(translator.translate(input, 0, writer)).thenThrow(new IOException());
		
		try {
			translator.translate(input, writer);
		} catch (RuntimeException e) {
			assertEquals(IOException.class, e.getCause().getClass());
		}
	}

	@Test
	public void testTranslate_withInvalidCodePoint_callsWriteMethodForEachCharacter() throws IOException {
		String input = "input";
		StringWriter writer = new StringWriter();
		
		translator.translate(input, writer);
		
		verify(writer, times(5)).write(any(char[].class));
	}
	
	@Test
	public void testTranslate_withSurrogatePair_callsWriteMethodOnceForSurrogatePair() throws IOException {
		String input = "\uD83D\uDE00";
		StringWriter writer = new StringWriter();
		
		translator.translate(input, writer);
		
		verify(writer, times(1)).write(any(char[].class));
	}
	
	@Test
	public void testHex_withValidCodepoint_returnsExpectedResult() {
		int codepoint = 65;
		String expectedOutput = "41";
		
		String result = CharSequenceTranslator.hex(codepoint);
		
		assertEquals(expectedOutput, result);
	}
	
	@Test
	public void testHex_withValidCodepoint_returnsExpectedResultIgnoreCase() {
		int codepoint = 97;
		String expectedOutput = "61";
		
		String result = CharSequenceTranslator.hex(codepoint);
		
		assertEquals(expectedOutput, result);
	}
}