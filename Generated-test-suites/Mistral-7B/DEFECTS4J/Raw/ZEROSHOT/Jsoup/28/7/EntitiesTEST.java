package org.jsoup.nodes;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class EntitiesTest {

	@Test
	public void testIsNamedEntity() {
		Entities.EscapeMode escapeMode = Entities.EscapeMode.base;
		assertTrue(Entities.isNamedEntity("lt"));
		assertTrue(Entities.isNamedEntity("amp"));
		assertTrue(Entities.isNamedEntity("apos"));
		assertTrue(Entities.isNamedEntity("quot"));
		assertFalse(Entities.isNamedEntity("nonexistent"));
		assertFalse(Entities.isNamedEntity(""));
	}

	@Test
	public void testGetCharacterByName() {
		Entities.EscapeMode escapeMode = Entities.EscapeMode.base;
		assertEquals('&', Entities.getCharacterByName("amp"));
		assertEquals('<', Entities.getCharacterByName("lt"));
		assertEquals('>', Entities.getCharacterByName("gt"));
		assertEquals('\'', Entities.getCharacterByName("apos"));
		assertEquals('"', Entities.getCharacterByName("quot"));
		assertNull(Entities.getCharacterByName("nonexistent"));
	}

	@Test
	public void testEscapeStringWithDefaultOutputSettings() {
		String input = "This is a string with &, <, >, ' and \".";
		String expectedOutput = "This is a string with &amp,&lt;,&gt;,&apos;,&quot;.";
		String output = Entities.escape(input, null);
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testEscapeStringWithCustomOutputSettings() {
		String input = "This is a string with &, <, >, ' and \".";
		CharsetEncoder encoder = StandardCharsets.UTF_8.newEncoder();
		Entities.EscapeMode escapeMode = Entities.EscapeMode.extended;
		String expectedOutput = "This is a string with &amp;#38;,&lt;#x3C;,&gt;#x3E;,&apos;#x27;,&quot;#x22;.";
		String output = Entities.escape(input, encoder, escapeMode);
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testUnescapeStringWithDefaultOutputSettings() {
		String input = "This is a string with &amp;&lt;lt&gt;&gt;apos;apos;&quot;quot;&#38;.";
		String expectedOutput = "This is a string with <, >, ' , ' , \" and &.";
		String output = Entities.unescape(input);
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testUnescapeStringWithCustomOutputSettings() {
		String input = "This is a string with &amp;#x26;#x3C;#x3E;#x27;#x27;#x22;#x26;.";
		String expectedOutput = "This is a string with &, <, >, ' , ' , \" and &.";
		String output = Entities.unescape(input, false);
		assertEquals(expectedOutput, output);
	}

	@Test
	public void testLoadEntitiesFromFile() {
		Map<String, Character> expectedEntities = new HashMap<>();
		expectedEntities.put("quot", '"');
		expectedEntities.put("amp", '&');
		expectedEntities.put("apos", '\'');
		expectedEntities.put("lt", '<');
		expectedEntities.put("gt", '>');

		// Base entities
		Map<String, Character> baseEntities = Entities.loadEntities("entities-base.properties");
		assertEquals(expectedEntities, baseEntities);

		// Full entities
		Map<String, Character> fullEntities = Entities.loadEntities("entities-full.properties");
		assertEquals(expectedEntities, fullEntities);
	}

	@Test(expected = MissingResourceException.class)
	public void testLoadEntitiesFromNonexistentFile() {
		Entities.loadEntities("nonexistent.properties");
	}
}