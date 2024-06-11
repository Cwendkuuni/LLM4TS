package org.jsoup.nodes;

import org.jsoup.nodes.Entities;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

	@Test
	public void testIsNamedEntity() {
		assertTrue(Entities.isNamedEntity("lt"));
		assertTrue(Entities.isNamedEntity("gt"));
		assertTrue(Entities.isNamedEntity("amp"));
		assertTrue(Entities.isNamedEntity("quot"));
		assertFalse(Entities.isNamedEntity("unknown"));
	}

	@Test
	public void testIsBaseNamedEntity() {
		assertTrue(Entities.isBaseNamedEntity("lt"));
		assertTrue(Entities.isBaseNamedEntity("gt"));
		assertTrue(Entities.isBaseNamedEntity("amp"));
		assertFalse(Entities.isBaseNamedEntity("quot"));
		assertFalse(Entities.isBaseNamedEntity("unknown"));
	}

	@Test
	public void testGetCharacterByName() {
		assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
		assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
		assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
		assertEquals(null, Entities.getCharacterByName("unknown"));
	}

	@Test
	public void testEscape() {
		String string = "Hello < World > & \"";
		String expected = "Hello &lt; World &gt; &amp; &quot;";
		assertEquals(expected, Entities.escape(string, null));
	}

	@Test
	public void testUnescape() {
		String string = "Hello &lt; World &gt; &amp; &quot;";
		String expected = "Hello < World > & \"";
		assertEquals(expected, Entities.unescape(string, false));
	}

	@Test
	public void testEscapeWithAttribute() {
		String string = "Hello < World > & \"";
		String expected = "Hello < World > &amp; &quot;";
		assertEquals(expected, Entities.escape(string, null));
	}

	@Test
	public void testUnescapeStrict() {
		String string = "Hello &lt; World &gt; &amp; &quot;";
		String expected = "Hello < World > & \"";
		assertEquals(expected, Entities.unescape(string, true));
	}
}

