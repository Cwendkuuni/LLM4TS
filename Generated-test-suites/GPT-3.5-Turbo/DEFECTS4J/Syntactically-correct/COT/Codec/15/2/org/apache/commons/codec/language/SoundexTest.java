import static org.junit.Assert.*;
import org.junit.Test;

public class SoundexTest {

	@Test
	public void testDifference() throws Exception {
		Soundex soundex = new Soundex();
		
		// Test case 1: Both strings are empty
		assertEquals(0, soundex.difference("", ""));
		
		// Test case 2: Both strings are the same
		assertEquals(4, soundex.difference("hello", "hello"));
		
		// Test case 3: Both strings have common characters but in different positions
		assertEquals(3, soundex.difference("hello", "hlelo"));
		
		// Test case 4: Both strings have common characters but in different positions and case insensitive
		assertEquals(3, soundex.difference("HELLO", "hlElO"));
	}
	
	@Test(expected=EncoderException.class)
	public void testEncode_Object_InvalidType() throws Exception {
		Soundex soundex = new Soundex();
		
		soundex.encode(123);
	}
	
	@Test
	public void testEncode_Object_ValidType() throws Exception {
		Soundex soundex = new Soundex();
		
		assertEquals("H400", soundex.encode("Hello"));
	}
	
	@Test
	public void testEncode_String() throws Exception {
		Soundex soundex = new Soundex();
		
		assertEquals("H400", soundex.encode("Hello"));
	}
	
	@Test
	public void testGetMaxLength() {
		Soundex soundex = new Soundex();
		
		assertEquals(4, soundex.getMaxLength());
	}
	
	@Test
	public void testSetMaxLength() {
		Soundex soundex = new Soundex();
		
		soundex.setMaxLength(5);
		
		assertEquals(5, soundex.getMaxLength());
	}
	
	@Test
	public void testSoundex() {
		Soundex soundex = new Soundex();
		
		// Test case 1: Empty string
		assertEquals("", soundex.soundex(""));
		
		// Test case 2: Single character mapping
		assertEquals("H", soundex.soundex("H"));
		
		// Test case 3: Single character mapping with multiple occurrences
		assertEquals("H", soundex.soundex("Hhhhh"));
		
		// Test case 4: Mapping with different characters
		assertEquals("H100", soundex.soundex("Haaal"));
		
		// Test case 5: Mapping with different characters and multiple occurrences
		assertEquals("H100", soundex.soundex("Haaaallll"));
	}
}