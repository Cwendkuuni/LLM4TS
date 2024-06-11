import org.junit.BeforeClass;
import org.junit.Test;
import org.jsoup.nodes.Entities;
import java.util.*;
import static org.junit.Assert.*;

public class EntitiesTest {

 private static Map<String, Character> full;
 private static Map<Character, String> xhtmlByVal;
 private static Map<Character, String> base;
 private static Map<Character, String> baseByVal;
 private static Map<Character, String> fullByVal;

 @BeforeClass
 public static void setup() {
 full = Entities.full;
 xhtmlByVal = Entities.xhtmlByVal;
 base = Entities.base;
 baseByVal = Entities.baseByVal;
 fullByVal = Entities.fullByVal;
 }

 @Test
 public void testIsNamedEntity() {
 // Test isNamedEntity method
 // True cases
 assertTrue(Entities.isNamedEntity("quot"));
 assertTrue(Entities.isNamedEntity("amp"));
 assertTrue(Entities.isNamedEntity("apos"));
 assertTrue(Entities.isNamedEntity("lt"));
 assertTrue(Entities.isNamedEntity("gt"));

 // False cases
 assertFalse(Entities.isNamedEntity(""));
 assertFalse(Entities.isNamedEntity("invalidEntity"));
 }

 @Test
 public void testGetCharacterByName() {
 // Test getCharacterByName method
 // Positive cases
 assertEquals('&', Entities.getCharacterByName("amp"));
 assertEquals('<', Entities.getCharacterByName("lt"));
 assertEquals('>', Entities.getCharacterByName("gt"));
 assertEquals("'", Entities.getCharacterByName("apos"));
 assertEquals('"', Entities.getCharacterByName("quot"));

 // Negative cases
 assertNull(Entities.getCharacterByName(""));
 assertNull(Entities.getCharacterByName("invalidEntity"));
 }

 @Test
 public void testEscape() {
 // Test escape method
 String inputString = "&lt; &amp; '&apos; &quot;";
 String expectedOutput = "&amp;lt; &amp;amp; &amp;apos; &amp;quot;";
 String outputString = Entities.escape(inputString, null, Entities.EscapeMode.base);
 assertEquals(expectedOutput, outputString);
 }

 @Test
 public void testUnescape() {
 // Test unescape method
 String inputString = "&amp;lt; &amp;amp; &amp;apos; &amp;quot;";
 String expectedOutput = "&lt; &amp; &apos; &quot;";
 String outputString = Entities.unescape(inputString);
 assertEquals(expectedOutput, outputString);
 }

 @Test
 public void testXhtmlArray() {
 // Test xhtmlArray initialization
 Character[] expectedXhtmlArray = {'\u00022', '\u00026', '\u00027', '\u0003C', '\u0003E'};
 assertArrayEquals(expectedXhtmlArray, xhtmlByVal.values().toArray(new Character[5]));
 }

 @Test
 public void testLoadEntities() {
 // Test loadEntities method
 Properties expectedProperties = new Properties();
 expectedProperties.put("quot", "00022");
 expectedProperties.put("amp", "00026");
 expectedProperties.put("apos", "00027");
 expectedProperties.put("lt", "0003C");
 expectedProperties.put("gt", "0003E");

 Map<String, Character> expectedEntities = new HashMap<>();
 expectedEntities.put("quot", '\u00022');
 expectedEntities.put("amp", '\u00026');
 expectedEntities.put("apos", '\u00027');
 expectedEntities.put("lt", '\u0003C');
 expectedEntities.put("gt", '\u0003E');

 Map<String, Character> loadedEntities = Entities.loadEntities("entities-base.properties");
 assertEquals(expectedEntities, loadedEntities);
 }

 @Test
 public void testToCharacterKey() {
 // Test toCharacterKey method
 Map<Character, String> expectedMap = new HashMap<>();
 expectedMap.put('\u00022', "quot");
 expectedMap.put('\u00026', "amp");
 expectedMap.put('\u00027', "apos");
 expectedMap.put('\u0003C', "lt");
 expectedMap.put('\u0003E', "gt");

 Map<Character, String> characterMap = Entities.toCharacterKey(Entities.base);
 assertEquals(expectedMap, characterMap);
 }
}