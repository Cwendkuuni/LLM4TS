import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.Writer;

import org.apache.commons.lang3.text.translate.CharSequenceTranslator;
import org.junit.Before;
import org.junit.Test;

public class LookupTranslatorTest {
	
	private LookupTranslator translator;
	
	@Before
	public void setup() {
		translator = new LookupTranslator(
			new CharSequence[] { "a", "1" },
			new CharSequence[] { "b", "2" },
			new CharSequence[] { "c", "3" }
		);
	}
	
	@Test
	public void testConstructor() {
		assertEquals(3, translator.getLookupMap().size());
		assertEquals(1, translator.getShortest());
		assertEquals(1, translator.getLongest());
	}
	
	@Test
	public void testTranslate() throws IOException {
		CharSequence input = "abc";
		int index = 0;
		Writer out = mock(Writer.class);
		
		int result = translator.translate(input, index, out);
		verify(out).write("1");
		assertEquals(1, result);
		
		index += result;
		
		result = translator.translate(input, index, out);
		verify(out).write("2");
		assertEquals(1, result);
		
		index += result;
		
		result = translator.translate(input, index, out);
		verify(out).write("3");
		assertEquals(1, result);
		
		index += result;
		
		result = translator.translate(input, index, out);
		assertEquals(0, result);
	}
}