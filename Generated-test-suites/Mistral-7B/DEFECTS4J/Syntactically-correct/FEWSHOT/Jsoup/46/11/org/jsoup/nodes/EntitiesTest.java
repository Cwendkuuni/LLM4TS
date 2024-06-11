package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Properties;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertEquals(true, Entities.isNamedEntity("lt"));
        assertEquals(true, Entities.isNamedEntity("amp"));
        assertEquals(true, Entities.isNamedEntity("quot"));
        assertEquals(false, Entities.isNamedEntity("UnknownEntity"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertEquals(true, Entities.isBaseNamedEntity("lt"));
        assertEquals(true, Entities.isBaseNamedEntity("amp"));
        assertEquals(true, Entities.isBaseNamedEntity("quot"));
        assertEquals(false, Entities.isBaseNamedEntity("gt"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals('<', Entities.getCharacterByName("lt"));
        assertEquals('&', Entities.getCharacterByName("amp"));
        assertEquals('"', Entities.getCharacterByName("quot"));
        assertNull(Entities.getCharacterByName("UnknownEntity"));
    }

    @Test
    public void testEscapeBasic() {
        String input = "& < > \"";
        String output = Entities.escape(input, Document.OutputSettings.base());
        assertEquals("&amp; &lt; &gt; &quot;", output);
    }

    @Test
    public void testEscapeAttributes() {
        String input = "<script>alert('Hello, World!');</script>";
        String output = Entities.escape(input, Document.OutputSettings.xhtml());
        assertEquals("&lt;script&gt;alert(&apos;Hello, World!&apos;)&lt;/script&gt;", output);
    }

    @Test
    public void testEscapeUnicode() {
        String input = "😃";
        String output = Entities.escape(input, Document.OutputSettings.utf());
        assertEquals("&#x1F600;", output);
    }

    @Test
    public void testUnescapeBasic() {
        String input = "&amp; &lt; &gt; &quot;";
        String output = Entities.unescape(input);
        assertEquals("& < > \"", output);
    }

    @Test
    public void testUnescapeAttributes() {
        String input = "&lt;script&gt;alert(&apos;Hello, World!&apos;)&lt;/script&gt;";
        String output = Entities.unescape(input);
        assertEquals("<script>alert('Hello, World!');</script>", output);
    }

    @Test
    public void testCanEncode() {
        assertTrue(Entities.canEncode(CoreCharset.utf(), 'A', StandardCharsets.UTF_8));
        assertTrue(Entities.canEncode(CoreCharset.utf(), '😃', StandardCharsets.UTF_8));
        assertFalse(Entities.canEncode(CoreCharset.ascii, '😃', StandardCharsets.UTF_8));
    }

    @Test
    public void testLoadEntities() throws IOException {
        Properties properties = new Properties();
        properties.load(Entities.class.getResourceAsStream("/entities-base.properties"));
        Map<String, Character> entities = new HashMap<>();
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            Character value = Character.valueOf((char) Integer.parseInt((String) entry.getValue(), 16));
            entities.put((String) entry.getKey(), value);
        }
        assertEquals(Entities.base.get(0), entities.get("amp"));
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> map = Entities.loadEntities("entities-base.properties");
        Map<Character, String> reversedMap = Entities.toCharacterKey(map);
        assertEquals("amp", reversedMap.get('&'));
    }
}