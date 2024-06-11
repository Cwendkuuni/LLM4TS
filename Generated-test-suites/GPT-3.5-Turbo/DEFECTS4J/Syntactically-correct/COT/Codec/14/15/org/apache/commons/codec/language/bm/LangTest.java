
import org.junit.Test;
import static org.junit.Assert.*;

public class LangTest {

	@Test
	public void testGuessLanguage() {
		Lang lang = Lang.instance(NameType.GENERIC);
		
		// Test basic functionality
		String text = "hello";
		String result = lang.guessLanguage(text);
		assertEquals("eng", result);
		
		text = "hola";
		result = lang.guessLanguage(text);
		assertEquals("spa", result);
		
		// Test edge cases
		text = "";
		result = lang.guessLanguage(text);
		assertEquals("und", result);
		
		text = null;
		result = lang.guessLanguage(text);
		assertEquals("und", result);
	}

	@Test
	public void testGuessLanguages() {
		Lang lang = Lang.instance(NameType.GENERIC);
		
		// Test basic functionality
		String text = "hello";
		Languages.LanguageSet result = lang.guessLanguages(text);
		assertTrue(result.contains("eng"));
		
		text = "hola";
		result = lang.guessLanguages(text);
		assertTrue(result.contains("spa"));
		
		// Test edge cases
		text = "";
		result = lang.guessLanguages(text);
		assertTrue(result.contains("und"));
		
		text = null;
		result = lang.guessLanguages(text);
		assertTrue(result.contains("und"));
	}

}
