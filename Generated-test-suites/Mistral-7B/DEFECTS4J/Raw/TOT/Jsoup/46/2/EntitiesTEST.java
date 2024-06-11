import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Entities;
import java.util.Map;
import static org.junit.Assert.assertEquals;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> fullEntities = Entities.full;
        for (Map.Entry<String, Character> entry : fullEntities.entrySet()) {
            String entityName = entry.getKey();
            Character entity = entry.getValue();
            assertTrue(Entities.isNamedEntity(entityName));
            assertEquals(entity, Entities.getCharacterByName(entityName));
        }
    }

    @Test
    public void testIsNamedEntityWithInvalidEntityName() {
        String invalidEntityName = "test";
        assertFalse(Entities.isNamedEntity(invalidEntityName));
        assertNull(Entities.getCharacterByName(invalidEntityName));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<Character, String> baseEntities = Entities.baseByVal;
        for (Map.Entry<Character, String> entry : baseEntities.entrySet()) {
            Character entity = entry.getKey();
            String entityName = entry.getValue();
            assertTrue(Entities.isBaseNamedEntity(entityName));
            assertEquals(entity, Entities.getCharacterByName(entityName));
        }
    }

    @Test
    public void testEscapeAndUnescape() {
        String input = "This is a & test & with < entities >, & quot;s & &amp; &amp;amp; &nbsp; &lt;!DOCTYPE html&gt;";
        String expectedOutput = "This is a &amp;amp; test &amp;amp;amp; &quot;s &amp;amp;amp; &amp;nbsp; &amp;lt;!DOCTYPE html&gt;";

        StringBuilder accum = new StringBuilder();
        Entities.escape(accum, input, null, false, false, false);

        String output = accum.toString();
        assertEquals(expectedOutput, output);

        String unescapedOutput = Entities.unescape(output, false);
        assertEquals(input, unescapedOutput);
    }

    @Test
    public void testCanEncode() {
        CoreCharset charset = CoreCharset.utf;
        for (char c = 0; c <= 0x10FFFF; c++) {
            boolean canEncode = Entities.canEncode(charset, c, null);
            if (Character.isSupplementaryCodePoint(c)) {
                // Supplementary characters are not supported by UTF-8 encoding
                if (charset == CoreCharset.utf) {
                    assertFalse(canEncode);
                } else {
                    assertTrue(canEncode);
                }
            } else {
                assertTrue(canEncode);
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testLoadEntitiesWithInvalidFileName() {
        Entities.loadEntities("invalidFileName");
    }
}