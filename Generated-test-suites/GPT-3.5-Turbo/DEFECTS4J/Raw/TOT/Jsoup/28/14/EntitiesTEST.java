
import org.jsoup.nodes.Entities;
import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;
import java.nio.charset.CharsetEncoder;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity_validNamedEntity_returnsTrue() {
        boolean result = Entities.isNamedEntity("lt");
        Assert.assertTrue(result);
    }

    @Test
    public void testIsNamedEntity_invalidNamedEntity_returnsFalse() {
        boolean result = Entities.isNamedEntity("fake");
        Assert.assertFalse(result);
    }

    @Test
    public void testGetCharacterByName_validNamedEntity_returnsCharacter() {
        Character character = Entities.getCharacterByName("lt");
        Assert.assertEquals('<', character.charValue());
    }

    @Test
    public void testGetCharacterByName_invalidNamedEntity_returnsNull() {
        Character character = Entities.getCharacterByName("fake");
        Assert.assertNull(character);
    }

    @Test
    public void testEscape_noSpecialCharacters_returnsInputString() {
        String input = "Hello World";
        String result = Entities.escape(input, null, null);
        Assert.assertEquals(input, result);
    }

    @Test
    public void testEscape_containsSpecialCharactersInMap_returnsEscapedString() {
        String input = "< & > \" '";
        String result = Entities.escape(input, null, Entities.EscapeMode.xhtml);
        Assert.assertEquals("&lt; &amp; &gt; &quot; &apos;", result);
    }

    @Test
    public void testEscape_containsSpecialCharactersNotInMap_returnsNumericEntityString() {
        String input = "$ @ %";
        String result = Entities.escape(input, null, Entities.EscapeMode.xhtml);
        Assert.assertEquals("&#36; &#64; &#37;", result);
    }

    @Test
    public void testUnescape_noEscapedCharacters_returnsInputString() {
        String input = "Hello World";
        String result = Entities.unescape(input);
        Assert.assertEquals(input, result);
    }

    @Test
    public void testUnescape_containsEscapedCharacters_returnsUnescapedString() {
        String input = "&lt; &amp; &gt; &quot; &apos; &#36; &#64; &#37;";
        String result = Entities.unescape(input);
        Assert.assertEquals("< & > \" ' $ @ %", result);
    }

    @Test
    public void testUnescape_containsInvalidNumericEntity_returnsInputString() {
        String input = "&#999999;";
        String result = Entities.unescape(input);
        Assert.assertEquals(input, result);
    }
    
    @Test
    public void testLoadEntities_validFilename_returnsValidEntities() {
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        Assert.assertFalse(entities.isEmpty());
    }

    @Test(expected = MissingResourceException.class)
    public void testLoadEntities_invalidFilename_throwsMissingResourceException() {
        Entities.loadEntities("fake.properties");
    }

    @Test
    public void testToCharacterKey_duplicateEntities_returnsLowerCaseVersion() {
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        Map<Character, String> characterKeyMap = Entities.toCharacterKey(entities);
        Assert.assertEquals("lt", characterKeyMap.get('<'));
    }

    @Test
    public void testToCharacterKey_nonDuplicateEntities_returnsOriginalVersion() {
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        entities.put("fake", 'F');
        Map<Character, String> characterKeyMap = Entities.toCharacterKey(entities);
        Assert.assertEquals("fake", characterKeyMap.get('F'));
    }

}
