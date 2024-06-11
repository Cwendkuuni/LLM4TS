import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Entities;
import org.jsoup.helper.StringUtil;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> fullEntities = Entities.full;
        for (Map.Entry<String, Character> entry : fullEntities.entrySet()) {
            String entityName = entry.getKey();
            Character entity = entry.getValue();
            assertTrue("Named entity '" + entityName + "' should be in the full entity map", Entities.isNamedEntity(entityName));
            assertEquals("Named entity '" + entityName + "' should map to character " + entity, entity, Entities.getCharacterByName(entityName));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<String, Character> baseEntities = Entities.base;
        for (Map.Entry<String, Character> entry : baseEntities.entrySet()) {
            String entityName = entry.getKey();
            Character entity = entry.getValue();
            assertTrue("Named entity '" + entityName + "' should be in the base entity map", Entities.isBaseNamedEntity(entityName));
            assertEquals("Named entity '" + entityName + "' should map to character " + entity, entity, Entities.getCharacterByName(entityName));
        }

        assertFalse("Character 'a' should not be a base named entity", Entities.isBaseNamedEntity("a"));
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> fullEntities = Entities.full;
        for (Map.Entry<String, Character> entry : fullEntities.entrySet()) {
            String entityName = entry.getKey();
            Character entity = entry.getValue();
            assertEquals("Named entity '" + entityName + "' should map to character " + entity, entity, Entities.getCharacterByName(entityName));
        }

        // Test invalid entity name
        assertNull("getCharacterByName should return null for invalid entity name", Entities.getCharacterByName("invalid"));
    }

    @Test
    public void testEscape() {
        String stringToEscape = "This is a & test & of the & Entities & class.";
        StringBuilder expectedOutput = new StringBuilder();
        Entities.escape(expectedOutput, stringToEscape, null, false, false, false);
        String output = Entities.escape(stringToEscape, null);
        assertEquals("Escaped string should match expected output", expectedOutput.toString(), output);
    }

    @Test
    public void testEscapeInAttribute() {
        StringBuilder expectedOutput = new StringBuilder();
        Entities.escape(expectedOutput, "This is an < attribute & test >", null, true, false, false);
        String output = Entities.escape("This is an < attribute & test >", null, true);
        assertEquals("Escaped attribute value should match expected output", expectedOutput.toString(), output);
    }

    @Test
    public void testUnescape() {
        String stringToUnescape = "&amp;lt;a&amp;gt;test&amp;quot; &amp;#160; &amp;#xA0;";
        String output = Entities.unescape(stringToUnescape);
        String expectedOutput = "<a>test " + StringUtil.SPACE + " ";
        assertEquals("Unescaped string should match expected output", expectedOutput, output);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCanEncodeInvalidCharset() {
        Entities.canEncode(null, (char) 0x0001, null);
    }

    @Test
    public void testCanEncode() {
        for (char c = 0; c < 0x10000; c++) {
            boolean canEncode = Entities.canEncode(Entities.CoreCharset.utf, c, null);
            assertTrue("Character " + c + " should be encodable", canEncode);
        }

        // Test a character that is not encodable
        char invalidCharacter = 0x110000;
        assertFalse("Character " + invalidCharacter + " should not be encodable", Entities.canEncode(Entities.CoreCharset.utf, invalidCharacter, null));
    }
}