import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Entities;
import java.util.*;

public class EntitiesTest {

 private final Entities entities = new Entities();

 @Test
 public void testIsNamedEntity() {
 // Check if known named entities are identified correctly
 for (Character namedEntity : Entities.Entities.full.keySet()) {
 assertTrue(entities.isNamedEntity(namedEntity.toString()));
 }

 // Check if unknown named entities are identified as not named entities
 for (char c = 0; c <= 0xFFFF; c++) {
 if (!Entities.Entities.full.containsKey(Character.toString(c))) {
 assertFalse(entities.isNamedEntity(Character.toString(c)));
 }
 }
 }

 @Test
 public void testGetCharacterByName() {
 // Check if named entities are returned correctly
 for (Character namedEntity : Entities.Entities.full.keySet()) {
 assertEquals(namedEntity, entities.getCharacterByName(namedEntity.toString()));
 }

 // Check if unknown named entities return null
 for (char c = 0; c <= 0xFFFF; c++) {
 if (!Entities.Entities.full.containsKey(Character.toString(c))) {
 Assert.assertNull(entities.getCharacterByName(Character.toString(c)));
 }
 }
 }

 @Test
 public void testEscape() {
 // Test escaping using different output settings and default charset
 String input = "test<test&test>";
 String xhtmlOutput = entities.escape(input, Entities.Entities.EscapeMode.xhtml, null);
 String baseOutput = entities.escape(input, null, null);
 String extendedOutput = entities.escape(input, null, Entities.Entities.EscapeMode.extended);

 // Check if each output matches expected escape sequences
 Assert.assertEquals("test&lt;test&amp;test", xhtmlOutput);
 Assert.assertEquals("test&#60;test&#38;test", baseOutput);
 Assert.assertEquals("test&#x3C;test&#x26;test", extendedOutput);

 // Test escaping with custom charset
 CharsetEncoder encoder = StandardCharsets.UTF_16.newEncoder();
 String customOutput = entities.escape(input, encoder, null);
 Assert.assertEquals("test&#x3C;test&#x26;test", customOutput);
 }

 @Test
 public void testUnescape() {
 // Test unescaping with default settings
 String input = "test&lt;test&amp;test";
 String output = entities.unescape(input);
 Assert.assertEquals("test<test&test", output);

 // Test unescaping with strict settings
 String strictInput = "test&lt;test&amp;test;";
 String strictOutput = entities.unescape(strictInput, true);
 Assert.assertEquals("test<test&test", strictOutput);

 // Test unescaping with unknown entities
 String unknownInput = "test&xfa;test";
 String unknownOutput = entities.unescape(unknownInput);
 Assert.assertEquals("test&xfa;test", unknownOutput); // no change, since 'xfa' is not a known entity
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEscapeWithNullInput() {
 entities.escape(null, null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnescapeWithNullInput() {
 entities.unescape(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnescapeWithNullStrictInput() {
 entities.unescape(null, true);
 }
}