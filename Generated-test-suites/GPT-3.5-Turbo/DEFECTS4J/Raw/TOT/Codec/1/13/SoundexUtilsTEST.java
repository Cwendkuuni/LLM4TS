import static org.junit.Assert.*;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.language.Soundex;
import org.apache.commons.codec.language.SoundexUtils;
import org.junit.Test;

public class SoundexUtilsTest {
	
	@Test
	public void clean_ReturnsCleanString_WhenValidStringInput() {
		String input = "Test123";
		String expectedOutput = "TEST";
		
		String actualOutput = SoundexUtils.clean(input);
		
		assertEquals(expectedOutput, actualOutput);
	}
	
	@Test
	public void clean_ReturnsEmptyString_WhenNullInput() {
		String input = null;
		String expectedOutput = null;
		
		String actualOutput = SoundexUtils.clean(input);
		
		assertEquals(expectedOutput, actualOutput);
	}
	
	@Test
	public void clean_ReturnsEmptyString_WhenEmptyInput() {
		String input = "";
		String expectedOutput = "";
		
		String actualOutput = SoundexUtils.clean(input);
		
		assertEquals(expectedOutput, actualOutput);
	}
	
	@Test
	public void difference_ReturnsCorrectSimilarity_WhenValidStrings() throws EncoderException {
		Soundex soundex = new Soundex();
		String s1 = "Test";
		String s2 = "Tstt";
		int expectedOutput = 3;
		
		int actualOutput = SoundexUtils.difference(soundex, s1, s2);
		
		assertEquals(expectedOutput, actualOutput);
	}
	
	@Test
	public void difference_ReturnsZero_WhenOneInputIsNull() throws EncoderException {
		Soundex soundex = new Soundex();
		String s1 = "Test";
		String s2 = null;
		int expectedOutput = 0;
		
		int actualOutput = SoundexUtils.difference(soundex, s1, s2);
		
		assertEquals(expectedOutput, actualOutput);
	}
	
	@Test
	public void difference_ReturnsSameLength_WhenOneInputIsShorter() throws EncoderException {
		Soundex soundex = new Soundex();
		String s1 = "Test";
		String s2 = "Te";
		int expectedOutput = 2;
		
		int actualOutput = SoundexUtils.difference(soundex, s1, s2);
		
		assertEquals(expectedOutput, actualOutput);
	}
	
	@Test
	public void difference_ReturnsZero_WhenBothInputsAreNull() throws EncoderException {
		Soundex soundex = new Soundex();
		String s1 = null;
		String s2 = null;
		int expectedOutput = 0;
		
		int actualOutput = SoundexUtils.difference(soundex, s1, s2);
		
		assertEquals(expectedOutput, actualOutput);
	}
	
	@Test
	public void differenceEncoded_ReturnsCorrectSimilarity_WhenValidStrings() {
		String es1 = "1234";
		String es2 = "1334";
		int expectedOutput = 3;
		
		int actualOutput = SoundexUtils.differenceEncoded(es1, es2);
		
		assertEquals(expectedOutput, actualOutput);
	}
	
	@Test
	public void differenceEncoded_ReturnsZero_WhenOneInputIsNull() {
		String es1 = "1234";
		String es2 = null;
		int expectedOutput = 0;
		
		int actualOutput = SoundexUtils.differenceEncoded(es1, es2);
		
		assertEquals(expectedOutput, actualOutput);
	}
	
	@Test
	public void differenceEncoded_ReturnsSameLength_WhenOneInputIsShorter() {
		String es1 = "1234";
		String es2 = "12";
		int expectedOutput = 2;
		
		int actualOutput = SoundexUtils.differenceEncoded(es1, es2);
		
		assertEquals(expectedOutput, actualOutput);
	}
	
	@Test
	public void differenceEncoded_ReturnsZero_WhenBothInputsAreNull() {
		String es1 = null;
		String es2 = null;
		int expectedOutput = 0;
		
		int actualOutput = SoundexUtils.differenceEncoded(es1, es2);
		
		assertEquals(expectedOutput, actualOutput);
	}
}