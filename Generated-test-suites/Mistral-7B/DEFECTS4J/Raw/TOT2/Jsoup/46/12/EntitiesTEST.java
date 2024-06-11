import org.junit.Test;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class EntitiesTest {

 private static final Entities entities = new Entities();

 @Test
 public void testIsNamedEntity_base() {
 String namedEntity = "amp";
 assertTrue(entities.isNamedEntity(namedEntity));
 }

 @Test
 public void testIsNamedEntity_xhtml() {
 String namedEntity = "lt";
 assertTrue(entities.isNamedEntity(namedEntity));
 }

 @Test
 public void testIsNamedEntity_notNamedEntity() {
 String notNamedEntity = "unknown";
 assertFalse(entities.isNamedEntity(notNamedEntity));
 }

 @Test
 public void testGetCharacterByName_base() {
 String namedEntity = "amp";
 Character character = entities.getCharacterByName(namedEntity);
 assertEquals(Character.forInt(0x00026), character);
 }

 @Test
 public void testGetCharacterByName_xhtml() {
 String namedEntity = "lt";
 Character character = entities.getCharacterByName(namedEntity);
 assertEquals(Character.forInt(0x0003C), character);
 }

 @Test
 public void testGetCharacterByName_notNamedEntity() {
 String notNamedEntity = "unknown";
 assertNull(entities.getCharacterByName(notNamedEntity));
 }

 @Test
 public void testCanEncode() {
 CoreCharset coreCharset = CoreCharset.utf;
 char character = 'a';
 assertTrue(entities.canEncode(coreCharset, character, null));
 }

 @Test
 public void testCanEncode_notEncodable() {
 CoreCharset coreCharset = CoreCharset.utf;
 char character = 0x1F600; // high surrogate
 assertFalse(entities.canEncode(coreCharset, character, null));
 }

 @Test
 public void testIsBaseNamedEntity_base() {
 String namedEntity = "amp";
 assertTrue(entities.isBaseNamedEntity(namedEntity));
 }

 @Test
 public void testIsBaseNamedEntity_xhtml() {
 String namedEntity = "lt";
 assertFalse(entities.isBaseNamedEntity(namedEntity));
 }

 @Test
 public void testIsBaseNamedEntity_notNamedEntity() {
 String notNamedEntity = "unknown";
 assertFalse(entities.isBaseNamedEntity(notNamedEntity));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testIsBaseNamedEntity_nullName() {
 String nullName = null;
 entities.isBaseNamedEntity(nullName);
 }

 @Test
 public void testEscape_emptyString() {
 StringBuilder accumulator = new StringBuilder();
 String input = "";
 Entities.escape(accumulator, input, null, false, false, false);
 assertEquals("", accumulator.toString());
 }

 @Test
 public void testEscape_singleCharacter() {
 StringBuilder accumulator = new StringBuilder();
 String input = "a";
 Entities.escape(accumulator, input, null, false, false, false);
 assertEquals("a", accumulator.toString());
 }

 @Test
 public void testEscape_singleCharacter_xhtml() {
 StringBuilder accumulator = new StringBuilder();
 String input = "a";
 Entities.escape(accumulator, input, Entities.EscapeMode.xhtml, false, false, false);
 assertEquals("a", accumulator.toString());
 }

 @Test
 public void testEscape_singleCharacter_notXhtml() {
 StringBuilder accumulator = new StringBuilder();
 String input = "a";
 Entities.escape(accumulator, input, Entities.EscapeMode.base, false, false, false);
 assertEquals("&a;", accumulator.toString());
 }

 @Test
 public void testEscape_multipleCharacters() {
 StringBuilder accumulator = new StringBuilder();
 String input = "abcdef";
 Entities.escape(accumulator, input, null, false, false, false);
 assertEquals("abcdef", accumulator.toString());
 }

 @Test
 public void testEscape_multipleCharacters_xhtml() {
 StringBuilder accumulator = new StringBuilder();
 String input = "abcdef";
 Entities.escape(accumulator, input, Entities.EscapeMode.xhtml, false, false, false);
 assertEquals("abcdef", accumulator.toString());
 }

 @Test
 public void testEscape_multipleCharacters_notXhtml() {
 StringBuilder accumulator = new StringBuilder();
 String input = "abcdef";
 Entities.escape(accumulator, input, Entities.EscapeMode.base, false, false, false);
 assertEquals("&a;&b;&c;&d;&e;&f;", accumulator.toString());
 }

 @Test
 public void testEscape_singleCharacter_quotedAttribute() {
 StringBuilder accumulator = new StringBuilder();
 String input = "&";
 Entities.escape(accumulator, input, null, true, false, false);
 assertEquals("&amp;", accumulator.toString());
 }

 @Test
 public void testEscape_singleCharacter_attributeWithQuotes() {
 StringBuilder accumulator = new StringBuilder();
 String input = "'&'";
 Entities.escape(accumulator, input, null, false, false, false);
 assertEquals("&apos;&amp;", accumulator.toString());
 }

 @Test
 public void testEscape_singleCharacter_attributeWithQuotesAndNormalization() {
 StringBuilder accumulator = new StringBuilder();
 String input = "&amp;\'";
 Entities.escape(accumulator, input, null, false, true, false);
 assertEquals("&amp;#39;", accumulator.toString());
 }

 @Test
 public void testEscape_multipleCharacters_quotedAttribute() {
 StringBuilder accumulator = new StringBuilder();
 String input = "&amp;&amp;";
 Entities.escape(accumulator, input, null, true, false, false);
 assertEquals("&amp;amp;", accumulator.toString());
 }

 @Test
 public void testEscape_multipleCharacters_attributeWithQuotes() {
 StringBuilder accumulator = new StringBuilder();
 String input = "&amp;&apos;";
 Entities.escape(accumulator, input, null, false, false, false);
 assertEquals("&amp;#38;#39;", accumulator.toString());
 }

 @Test
 public void testEscape_multipleCharacters_attributeWithQuotesAndNormalization() {
 StringBuilder accumulator = new StringBuilder();
 String input = "&amp;&apos;";
 Entities.escape(accumulator, input, null, false, true, false);
 assertEquals("&amp;#38;#39;", accumulator.toString());
 }

 @Test
 public void testUnescape_emptyString() {
 String input = "";
 String output = Entities.unescape(input);
 assertEquals("", output);
 }

 @Test
 public void testUnescape_singleCharacter() {
 String input = "&amp;";
 String output = Entities.unescape(input);
 assertEquals("&", output);
 }

 @Test
 public void testUnescape_singleCharacter_withTrailingSemicolon() {
 String input = "&amp;&#x3B;";
 String output = Entities.unescape(input, true);
 assertEquals("&amp;", output);
 }

 @Test
 public void testUnescape_multipleCharacters() {
 String input = "&lt;ab&gt;cd&efr";
 String output = Entities.unescape(input);
 assertEquals("<ab>cd<ef>", output);
 }

 @Test
 public void testUnescape_multipleCharacters_withTrailingSemicolon() {
 String input = "&lt;ab&gt;cd&efr;&#x3B;";
 String output = Entities.unescape(input, true);
 assertEquals("<ab>", output);
 }

 @Test
 public void testUnescape_multipleCharacters_withTrailingSemicolon_xhtml() {
 String input = "&lt;ab&gt;cd&efr;&#x3B;";
 String output = Entities.unescape(input, true, Entities.EscapeMode.xhtml);
 assertEquals("<ab>", output);
 }

 @Test
 public void testUnescape_multipleCharacters_withTrailingSemicolon_notXhtml() {
 String input = "&lt;ab&gt;cd&efr;&#x3B;";
 String output = Entities.unescape(input, true, Entities.EscapeMode.base);
 assertEquals("<ab>&cd<ef>&", output);
 }

 @Test
 public void testUnescape_singleCharacter_xmlDeclaration() {
 String input = "<?xml version='1.0' encoding='UTF-8' ?>";
 String output = Entities.unescape(input);
 assertEquals("<?xml version='1.0' encoding='UTF-8' ?>", output);
 }

 @Test
 public void testUnescape_singleCharacter_xmlDeclaration_stripLeadingWhite() {
 String input = " <?xml version='1.0' encoding='UTF-8' ?>";
 String output = Entities.unescape(input, false, false, true);
 assertEquals("<?xml version='1.0' encoding='UTF-8'?>", output);
 }

 @Test
 public void testUnescape_singleCharacter_xmlDeclaration_stripLeadingWhite_normalize() {
 String input = " 
n <?xml version='1.0' encoding='UTF-8' ?>\n ";
 String output = Entities.unescape(input, false, true, true);
 assertEquals("<?xml version='1.0' encoding='UTF-8'?>", output);
 }

 @Test
 public void testUnescape_multipleCharacter_xmlDeclaration() {
 String input = "<?xml version='1.0' encoding='UTF-8'?>";
 String output = Entities.unescape(input);
 assertEquals("<?xml version='1.0' encoding='UTF-8'?>", output);
 }

 @Test
 public void testUnescape_multipleCharacter_xmlDeclaration_stripLeadingWhite() {
 String input = " 
n <?xml version='1.0' encoding='UTF-8' ?>\n ";
 String output = Entities.unescape(input, false, false, true);
 assertEquals("<?xml version='1.0' encoding='UTF-8'?>", output);
 }

 @Test
 public void testUnescape_multipleCharacter_xmlDeclaration_normalize() {
 String input = " 
n <?xml version='1.0' encoding='UTF-8' ?>\n ";
 String output = Entities.unescape(input, false, true, true);
 assertEquals("<?xml version='1.0' encoding='UTF-8'?>", output);
 }

 @Test
 public void testUnescape_multipleCharacter_xmlDeclaration_stripLeadingWhite_normalize() {
 String input = " 
n <?xml version='1.0' encoding='UTF-8' ?>\n ";
 String output = Entities.unescape(input, true, true, true);
 assertEquals("<?xml version='1.0' encoding='UTF-8'?>", output);
 }
}