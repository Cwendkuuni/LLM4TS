package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class EntitiesTest {

	@Test
	public void testIsNamedEntity() {
		// Test isNamedEntity method
		assertTrue(Entities.isNamedEntity("lt"));
		assertTrue(Entities.isNamedEntity("amp"));
		assertFalse(Entities.isNamedEntity("unknown"));
	}

	@Test
	public void testGetCharacterByName() {
		// Test getCharacterByName method
		assertEquals('<', Entities.getCharacterByName("lt"));
		assertEquals('&', Entities.getCharacterByName("amp"));
		assertNull(Entities.getCharacterByName("unknown"));
	}

	@Test
	public void testEscape() {
		// Test escape method with default encoding and base escape mode
		String input = "&lt; &gt; &amp; &apos; &quot; &eacute; &copy;";
		String expected = "&amp;lt; &amp;gt; &amp;amp; &amp;apos; &amp;quot; &amp;#233; &amp;copy;";

		String result = Entities.escape(input, null, Entities.EscapeMode.base);
		assertEquals(expected, result);

		// Test escape method with custom encoding and extended escape mode
		CharsetEncoder encoder = java.nio.charset.StandardCharsets.UTF_8.newEncoder();
		String result2 = Entities.escape(input, encoder, Entities.EscapeMode.extended);
		String expected2 = "&amp;#x201C; &amp;#x201D; &amp;#x26; &amp;#x2242; &amp;#x2249; &amp;#x00E9; &amp;#x00A9;";
		assertEquals(expected2, result2);
	}

	@Test
	public void testUnescape() {
		// Test unescape method with default mode
		String input = "lt &gt; amp &apos; quot &eacute; &copy;";
		String expected = "< > & ' ' ├ę ┬ô";

		String result = Entities.unescape(input);
		assertEquals(expected, result);

		// Test unescape method with strict mode
		String result2 = Entities.unescape(input, true);
		assertEquals(expected, result2);
	}

	@Test
	public void testIsNamedEntity_xhtml() {
		// Test isNamedEntity method with xhtml escape mode
		assertTrue(Entities.isNamedEntity_xhtml("quot"));
		assertTrue(Entities.isNamedEntity_xhtml("amp"));
		assertFalse(Entities.isNamedEntity_xhtml("lt"));
	}

	@Test
	public void testGetCharacterByName_xhtml() {
		// Test getCharacterByName method with xhtml escape mode
		assertEquals('\u0022', Entities.getCharacterByName_xhtml("quot"));
		assertEquals('\u0026', Entities.getCharacterByName_xhtml("amp"));
		assertNull(Entities.getCharacterByName_xhtml("lt"));
	}

	@Test
	public void testLoadEntities() {
		// Test loadEntities method
		Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
		assertEquals(Entities.getCharacterByName(entities.get("quot")), '\u0022');
		assertEquals(Entities.getCharacterByName(entities.get("amp")), '\u0026');
	}

	@Test
	public void testToCharacterKey() {
		// Test toCharacterKey method
		Map<String, Character> entities = new HashMap<>();
		entities.put("quot", '\u0022');
		entities.put("amp", '\u0026');

		Map<Character, String> result = Entities.toCharacterKey(entities);
		assertEquals(Entities.getCharacterByName(result.get('\u0022')), '\u0022');
		assertEquals(Entities.getCharacterByName(result.get('\u0026')), '\u0026');
	}
}