import org.junit.Test;
import org.junit.Assert.*;
import org.jsoup.nodes.Entities;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> fullEntities = Entities.full;
        for (Map.Entry<String, Character> entry : fullEntities.entrySet()) {
            String entityName = entry.getKey();
            Character entity = entry.getValue();
            assertTrue("Named entity '" + entityName + "' not found", Entities.isNamedEntity(entityName));
            assertEquals("Named entity '" + entityName + "' has wrong character representation", entity, Entities.getCharacterByName(entityName));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<Character, String> xhtmlEntities = Entities.xhtmlByVal;
        for (Map.Entry<Character, String> entry : xhtmlEntities.entrySet()) {
            Character entity = entry.getKey();
            String entityName = entry.getValue();
            assertTrue("Base named entity '" + entityName + "' not found", Entities.isBaseNamedEntity(entityName));
            assertEquals("Named entity '" + entityName + "' has wrong character representation", entity, Entities.getCharacterByName(entityName));
        }

        assertFalse("Entity 'abc' is not a base named entity", Entities.isBaseNamedEntity("abc"));
    }

    @Test
    public void testGetCharacterByName() {
        Map<String, Character> fullEntities = Entities.full;
        for (Map.Entry<String, Character> entry : fullEntities.entrySet()) {
            String entityName = entry.getKey();
            Character entity = entry.getValue();
            assertEquals("Named entity '" + entityName + "' has wrong character representation", entity, Entities.getCharacterByName(entityName));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsNamedEntityWithInvalidArgument() {
        Entities.isNamedEntity(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsNamedEntityWithEmptyString() {
        Entities.isNamedEntity("");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsBaseNamedEntityWithInvalidArgument() {
        Entities.isBaseNamedEntity(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsBaseNamedEntityWithEmptyString() {
        Entities.isBaseNamedEntity("");
    }

    @Test
    public void testEscape() {
        String string = "&amp;<lt>hello world&gt; \"&quot;";
        StringBuilder accum = new StringBuilder(string.length() * 2);
        Entities.escape(accum, string, null, false, false, false);
        String escaped = accum.toString();
        assertEquals("&amp;&amp;<amp>&lt;hello world&gt;&gt; &amp;&quot;", escaped);

        string = "&amp;<lt>hello world&gt; \"&quot;";
        accum.setLength(0);
        Entities.escape(accum, string, null, true, false, false);
        escaped = accum.toString();
        assertEquals("&amp;<lt>&lt;hello world&gt;&gt; &amp;&quot;", escaped);
    }

    @Test
    public void testUnescape() {
        String string = "&amp;<lt>hello world&gt; &quot;";
        String unescaped = Entities.unescape(string);
        assertEquals("&amp;<lt>hello world&gt; \"", unescaped);

        string = "&amp;<lt>hello world&gt; &quot;";
        unescaped = Entities.unescape(string, true);
        assertEquals("<hello world>", unescaped);
    }

    @Test
    public void testCanEncode() {
        CoreCharset coreCharset = CoreCharset.utf;
        CharsetEncoder encoder = Character.encodingCharset().newEncoder();
        for (char c = 0; c <= 0x10FFFF; c++) {
            boolean canEncode = Entities.canEncode(coreCharset, c, encoder);
            if (canEncode) {
                String encoded = Character.toString(c);
                assertEquals(encoded, encoder.encode(encoded).toString());
            }
        }
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = Entities.loadEntities("entities-full.properties");
        assertEquals(Entities.full.size(), entities.size());
        for (Map.Entry<String, Character> entry : Entities.full.entrySet()) {
            String name = entry.getKey();
            Character value = entry.getValue();
            assertTrue("Named entity '" + name + "' not loaded", entities.containsKey(name));
            assertEquals("Named entity '" + name + "' has wrong value", value, entities.get(name));
        }
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> entities = Entities.loadEntities("entities-full.properties");
        Map<Character, String> reversedEntities = Entities.toCharacterKey(entities);
        assertEquals(entities.size(), reversedEntities.size());
        for (Map.Entry<String, Character> entry : entities.entrySet()) {
            String name = entry.getKey();
            Character value = entry.getValue();
            assertTrue("Entity '" + value + "' not found", reversedEntities.containsKey(value));
            assertEquals("Entity '" + value + "' has wrong name", name, reversedEntities.get(value));
        }
    }
}