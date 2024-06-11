import org.junit.Test;
import org.jsoup.nodes.Entities;
import static org.junit.Assert.assertEquals;

public class EntitiesTest {

 @Test
 public void testIsNamedEntity() {
 // Test for known named entities in the full entity set
 assertEquals("lt should be a named entity", true, Entities.isNamedEntity("lt"));
 // Test for unknown named entity
 assertEquals("x should not be a named entity", false, Entities.isNamedEntity("x"));
 }

 @Test
 public void testGetCharacterByName() {
 // Test for known named entities in the full entity set
 assertEquals("lt character should be '<'", '\u003C', Entities.getCharacterByName("lt"));
 // Test for unknown named entity
 assertEquals("x should not be a named entity", '\u0000', Entities.getCharacterByName("x"));
 }

 @Test
 public void testEscape() {
 // Test for escaping a simple string
 assertEquals("'<hello>' should become '&lt;hello&gt;'", "&lt;hello&gt;", Entities.escape("<<hello>", Entities.EscapeMode.extended));
 // Test for escaping a string with special characters
 assertEquals("'<abc&def>' should become '&lt;abc&amp;amp;def&gt;'", "&lt;abc&amp;def&gt;", Entities.escape("<abc&def>", Entities.EscapeMode.extended));
 // Test for escaping an empty string
 assertEquals("An empty string should remain empty", "", Entities.escape("", Entities.EscapeMode.extended));
 }

 @Test
 public void testUnescape() {
 // Test for unescaping a simple entity
 assertEquals("'&lt;hello&gt;' should become '<hello>'", "<hello>", Entities.unescape("&lt;hello&gt;"));
 // Test for unescaping a string with entities
 assertEquals("'&lt;abc&amp;amp;def&gt;' should become '<abc&def>'", "<abc&def>", Entities.unescape("&lt;abc&amp;def&gt;"));
 // Test for unescaping a string without entities
 assertEquals("An empty string should remain empty", "", Entities.unescape(""));
 }
}