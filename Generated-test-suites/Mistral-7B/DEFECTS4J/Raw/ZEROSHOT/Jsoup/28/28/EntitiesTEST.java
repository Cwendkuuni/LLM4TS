package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class EntitiesTest {

 private static final Map<String, Character> full = new HashMap<>();
 private static final Map<Character, String> xhtmlByVal = new HashMap<>();
 private static final Map<Character, String> base = new HashMap<>();
 private static final Map<Character, String> baseByVal = new HashMap<>();
 private static final Map<Character, String> fullByVal = new HashMap<>();
 private static final Map<Character, String> unescapedValues = new HashMap<>();

 static {
 full.put("quot", (char) 0x00022);
 full.put("amp", (char) 0x00026);
 full.put("apos", (char) 0x00027);
 full.put("lt", (char) 0x0003C);
 full.put("gt", (char) 0x0003E);
 // Add other entities from the class here
 }

 static {
 xhtmlByVal.put((char) 0x0003C, "lt");
 xhtmlByVal.put((char) 0x0003E, "gt");
 // Add other xhtmlByVal entities here
 }

 static {
 base.putAll(full); // base = full by default
 baseByVal = toCharacterKey(base);
 fullByVal = toCharacterKey(full);
 }

 @Test
 public void testIsNamedEntity() {
 // Test isNamedEntity method
 assertTrue(Entities.isNamedEntity("quot"));
 assertTrue(Entities.isNamedEntity("lt"));
 assertFalse(Entities.isNamedEntity("test"));
 }

 @Test
 public void testGetCharacterByName() {
 // Test getCharacterByName method
 assertEquals(Entities.getCharacterByName("quot"), (char) 0x00022);
 assertEquals(Entities.getCharacterByName("lt"), (char) 0x0003C);
 assertNull(Entities.getCharacterByName("test"));
 }

 @Test
 public void testEscape() {
 // Test escape method
 assertEquals("&quot;&amp;&apos;&lt;&gt;", Entities.escape("\"&'<>\n", null, Entities.EscapeMode.base));
 assertEquals("&quot;&#x26;&#x27;&#x22;&#x23;&#x25;&#x20;", Entities.escape("\"&'<>\n", null, Entities.EscapeMode.extended));
 }

 @Test
 public void testUnescape() {
 // Test unescape method
 assertEquals("\"&amp;&apos;&lt;&gt;\n", Entities.unescape("&quot;&amp;&apos;&lt;&gt;\n", false));
 assertEquals("\"&amp;quot;&#xA0;&#xA0;&#x22;&#x3C;&#x3E;\n", Entities.unescape("&quot;&#xA0;&#xA0;&#x22;&amp;quot;&#x3C;&#x3E;\n", false));
 assertNull(Entities.unescape("&test;", false));
 assertEquals("\"&quot;&quot;\n", Entities.unescape("&quot;&quot;\n", true)); // strict unescape
 }

 @Test
 public void testXhtmlArray() {
 // Test xhtmlArray initialization
 assertSame(Entities.xhtmlByVal.get((char) 0x0003C), "lt");
 assertSame(Entities.xhtmlByVal.get((char) 0x0003E), "gt");
 }

 @Test
 public void testLoadEntities() {
 // Test loadEntities method
 Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
 assertEquals(Entities.base.size(), entities.size());
 for (Map.Entry<String, Character> entry : Entities.base.entrySet()) {
 Character key = entry.getValue();
 String value = Character.toString(key);
 assertTrue(entities.containsKey(value));
 assertEquals(key, entities.get(value));
 }
 }

 @Test
 public void testToCharacterKey() {
 // Test toCharacterKey method
 Map<Character, String> characters = Entities.toCharacterKey(full);
 assertEquals(Entities.full.size(), characters.size());
 for (Map.Entry<Character, String> entry : Entities.full.entrySet()) {
 Character key = entry.getKey();
 String value = entry.getValue();
 assertEquals(value, characters.get(key));
 }
 }

}