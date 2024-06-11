package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Entities.EscapeMode;
import org.jsoup.nodes.Entities.EscapeMode.*;
import org.junit.Test;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("apos"));
        assertTrue(Entities.isNamedEntity("quot"));
        assertFalse(Entities.isNamedEntity("foo"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('\''), Entities.getCharacterByName("apos"));
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("foo"));
    }

    @Test
    public void testEscape() {
        String string = "This is a test string";
        Document.OutputSettings out = new Document.OutputSettings();

        assertEquals("This is a test string", Entities.escape(string, out));
        assertEquals("&lt;This is a test string", Entities.escape("<This is a test string", out));
        assertEquals("This is a test string&gt;", Entities.escape("This is a test string>", out));
        assertEquals("&lt;This is a test string&gt;", Entities.escape("<This is a test string>", out));
    }

    @Test
    public void testEscapeWithEncoderAndEscapeMode() {
        String string = "This is a test string";
        CharsetEncoder encoder = null;
        EscapeMode escapeMode = new EscapeMode(xhtmlByVal);

        assertEquals("This is a test string", Entities.escape(string, encoder, escapeMode));
        assertEquals("quot;This is a test string", Entities.escape("\"This is a test string", encoder, escapeMode));
        assertEquals("This is a test stringgt;", Entities.escape("This is a test string>", encoder, escapeMode));
        assertEquals("quot;This is a test stringgt;", Entities.escape("\"This is a test string>", encoder, escapeMode));
    }

    @Test
    public void testUnescape() {
        String string = "This is a test string";

        assertEquals("This is a test string", Entities.unescape(string));
        assertEquals("<This is a test string", Entities.unescape("&lt;This is a test string"));
        assertEquals("This is a test string>", Entities.unescape("This is a test string&gt;"));
        assertEquals("<This is a test string>", Entities.unescape("&lt;This is a test string&gt;"));
    }

    @Test
    public void testUnescapeWithStrictTrue() {
        String string = "This is a test string";

        assertEquals("This is a test string", Entities.unescape(string, true));
        assertEquals("&lt;This is a test string", Entities.unescape("&lt;This is a test string", true));
        assertEquals("This is a test string&gt;", Entities.unescape("This is a test string&gt;", true));
        assertEquals("&lt;This is a test string&gt;", Entities.unescape("&lt;This is a test string&gt;", true));
    }

    @Test
    public void testUnescapePattern() {
        assertTrue(Entities.unescapePattern.matcher("&lt;").find());
        assertTrue(Entities.unescapePattern.matcher("&#60;").find());
        assertTrue(Entities.unescapePattern.matcher("&gt;").find());
        assertTrue(Entities.unescapePattern.matcher("&amp;").find());
        assertTrue(Entities.unescapePattern.matcher("&apos;").find());
        assertTrue(Entities.unescapePattern.matcher("&quot;").find());
    }

    @Test
    public void testStrictUnescapePattern() {
        assertTrue(Entities.strictUnescapePattern.matcher("&lt;").find());
        assertFalse(Entities.strictUnescapePattern.matcher("&#60;").find());
        assertTrue(Entities.strictUnescapePattern.matcher("&gt;").find());
        assertTrue(Entities.strictUnescapePattern.matcher("&amp;").find());
        assertTrue(Entities.strictUnescapePattern.matcher("&apos;").find());
        assertTrue(Entities.strictUnescapePattern.matcher("&quot;").find());
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> expectedEntities = new HashMap<String, Character>();
        expectedEntities.put("aa", Character.valueOf((char) 8364));
        expectedEntities.put("ab", Character.valueOf((char) 33));
        expectedEntities.put("ac", Character.valueOf((char) 8707));

        Map<String, Character> loadedEntities = Entities.loadEntities("entities-base.properties");

        assertEquals(expectedEntities, loadedEntities);
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> inMap = new HashMap<String, Character>();
        inMap.put("aa", Character.valueOf((char) 8364));
        inMap.put("aA", Character.valueOf((char) 33));
        inMap.put("Ab", Character.valueOf((char) 8707));

        Map<Character, String> expectedMap = new HashMap<Character, String>();
        expectedMap.put(Character.valueOf((char) 8364), "aa");
        expectedMap.put(Character.valueOf((char) 33), "aA");
        expectedMap.put(Character.valueOf((char) 8707), "Ab");

        Map<Character, String> outMap = Entities.toCharacterKey(inMap);

        assertEquals(expectedMap, outMap);
    }
}