import org.junit.*;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.*;

public class EntitiesTest {

 @BeforeClass
 public static void setup() {
 System.out.println("Setting up class-level resources...");
 }

 @AfterClass
 public static void teardown() {
 System.out.println("Cleaning up class-level resources...");
 }

 @Before
 public void setupEach() {
 System.out.println("Setting up test-level resources...");
 }

 @After
 public void teardownEach() {
 System.out.println("Cleaning up test-level resources...");
 }

 @Test
 public void testIsNamedEntity() {
 // Test known named entities
 assertTrue(Entities.isNamedEntity("lt"));
 assertTrue(Entities.isNamedEntity("amp"));
 // Test unknown named entities
 assertFalse(Entities.isNamedEntity("xxxx"));
 }

 @Test
 public void testGetCharacterByName() {
 // Test known named entities
 assertEquals('<', Entities.getCharacterByName("lt"));
 assertEquals('&', Entities.getCharacterByName("amp"));
 // Test unknown named entities
 assertNull(Entities.getCharacterByName("xxxx"));
 }

 @Test
 public void testEscape_Default() {
 // Test simple String
 String string = "Hello, World!";
 String escaped = Entities.escape(string, null, Entities.EscapeMode.base);
 assertEquals("Hello, World!", string);
 assertEquals("Hello, World!", escaped);
 // Test special characters
 String stringWithSpecialChars = "Hello < World!";
 String escapedWithSpecialChars = Entities.escape(stringWithSpecialChars, null, Entities.EscapeMode.base);
 assertEquals("Hello < World!", stringWithSpecialChars);
 assertEquals("Hello &lt; World!", escapedWithSpecialChars);
}

 @Test
 public void testEscape_XHTML() {
 // Test simple String
 String string = "Hello, World!";
 String escaped = Entities.escape(string, null, Entities.EscapeMode.xhtml);
 assertEquals("Hello, World!", string);
 assertEquals("Hello, World!", escaped);
 // Test special characters
 String stringWithSpecialChars = "Hello < World!";
 String escapedWithSpecialChars = Entities.escape(stringWithSpecialChars, null, Entities.EscapeMode.xhtml);
 assertEquals("Hello < World!", stringWithSpecialChars);
 assertEquals("Hello &lt; World!", escapedWithSpecialChars);
}

 @Test
 public void testEscape_Extended() {
 // Test simple String
 String string = "Hello, World!";
 String escaped = Entities.escape(string, null, Entities.EscapeMode.extended);
 assertEquals("Hello, World!", string);
 assertEquals("Hello, World!", escaped);
 // Test special characters
 String stringWithSpecialChars = "Hello < World!";
 String escapedWithSpecialChars = Entities.escape(stringWithSpecialChars, null, Entities.EscapeMode.extended);
 assertEquals("Hello < World!", stringWithSpecialChars);
 assertEquals("Hello &lt; World!", escapedWithSpecialChars);
 // Test unicode characters
 String stringWithUnicodeChars = "Hello 你好 World!";
 String escapedWithUnicodeChars = Entities.escape(stringWithUnicodeChars, null, Entities.EscapeMode.extended);
 assertEquals("Hello 你好 World!", stringWithUnicodeChars);
 assertEquals("Hello &#23545;&#37416; World!", escapedWithUnicodeChars);
}

 @Test
 public void testUnescape() {
 // Test unescaped String
 String unescaped = "Hello &lt; World!";
 String unescaped2 = Entities.unescape(unescaped);
 assertEquals("Hello < World!", unescaped);
 assertEquals("Hello < World!", unescaped2);
 // Test unescaped String with strict mode
 String unescapedStrict = "Hello &lt; World!";
 String unescapedStrict2 = Entities.unescape(unescapedStrict, true);
 assertEquals("Hello < World!", unescapedStrict);
 assertEquals("Hello < World!", unescapedStrict2);
 // Test escaped String
 String escaped = "Hello &lt; World!";
 String unescapedEscaped = Entities.unescape(escaped);
 assertEquals("Hello < World!", escaped);
 assertEquals("Hello < World!", unescapedEscaped);
}
}