package org.jsoup.nodes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        for (Map.Entry<String, Character> entry : Entities.full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue(Entities.isNamedEntity(name));
            assertEquals(character, Entities.getCharacterByName(name));
        }
        assertFalse(Entities.isNamedEntity(""));
        assertFalse(Entities.isNamedEntity(null));
    }

    @Test
    public void testIsBaseNamedEntity() {
        for (Map.Entry<String, Character> entry : Entities.base.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertTrue(Entities.isBaseNamedEntity(name));
            assertEquals(character, Entities.getCharacterByName(name));
        }
        assertFalse(Entities.isBaseNamedEntity(""));
        assertFalse(Entities.isBaseNamedEntity(null));
        assertFalse(Entities.isBaseNamedEntity("amp-sharp")); // not in base set
    }

    @Test
    public void testGetCharacterByName() {
        for (Map.Entry<String, Character> entry : Entities.full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            assertEquals(character, Entities.getCharacterByName(name));
        }
        assertNull(Entities.getCharacterByName(""));
        assertNull(Entities.getCharacterByName(null));
    }

    @Test
    public void testEscape() {
        String input = "&lt;lt&gt; &amp; &apos; &quot; &apos;a&apos; &quot;b&quot; &quot;&quot; &quot;c&quot;d &quot;e&quot;f &quot;g&quot;h &quot;i&quot;j &quot;k&quot; &quot;l&quot;m &quot;n&quot; &quot;o&quot;n &quot;p&quot; &quot;q&quot;r &quot;s&quot;s &quot;t&quot; &quot;u&quot;u &quot;v&quot; &quot;w&quot;x &quot;y&quot; &quot;z&quot;z &quot;A&quot; &quot;B&quot; &quot;C&quot; &quot;D&quot; &quot;E&quot; &quot;F&quot; &quot;G&quot; &quot;H&quot; &quot;I&quot; &quot;J&quot; &quot;K&quot; &quot;L&quot; &quot;M&quot; &quot;N&quot; &quot;O&quot; &quot;P&quot; &quot;Q&quot; &quot;R&quot; &quot;S&quot; &quot;T&quot; &quot;U&quot; &quot;V&quot; &quot;W&quot; &quot;X&quot; &quot;Y&quot; &quot;Z&quot; &quot;a&quot; &quot;b&quot; &quot;c&quot; &quot;d&quot; &quot;e&quot; &quot;f&quot; &quot;g&quot; &quot;h&quot; &quot;i&quot; &quot;j&quot; &quot;k&quot; &quot;l&quot; &quot;m&quot; &quot;n&quot; &quot;o&quot; &quot;p&quot; &quot;q&quot; &quot;r&quot; &quot;s&quot; &quot;t&quot; &quot;u&quot; &quot;v&quot; &quot;w&quot; &quot;x&quot; &quot;y&quot; &quot;z&quot; &quot;A&quot; &quot;B&quot; &quot;C&quot; &quot;D&quot; &quot;E&quot; &quot;F&quot; &quot;G&quot; &quot;H&quot; &quot;I&quot; &quot;J&quot; &quot;K&quot; &quot;L&quot; &quot;M&quot; &quot;N&quot; &quot;O&quot; &quot;P&quot; &quot;Q&quot; &quot;R&quot; &quot;S&quot; &quot;T&quot; &quot;U&quot; &quot;V&quot; &quot;W&quot; &quot;X&quot; &quot;Y&quot; &quot;Z&quot; &quot;&amp;&quot; &quot;&apos;&quot; &quot;&quot;&quot;";
        String expectedOutput = "&amp;lt;lt&gt; &amp;amp; &amp;apos; &amp;quot; &amp;apos;a&apos; &amp;quot;b&quot; &amp;quot;&quot; &amp;quot;c&quot;d &amp;quot;e&quot;f &amp;quot;g&quot; &amp;gt;h &amp;quot;i&quot;j &amp;quot;k&quot; &amp;quot;l&quot; &amp;quot;m&quot; &amp;quot;n&quot; &amp;quot;o&quot; &amp;quot;o&quot; &amp;quot;p&quot; &amp;quot;q&quot; &amp;quot;r&quot; &amp;quot;s&quot;s &amp;quot;t&quot; &amp;quot;u&quot; &amp;quot;u&quot; &amp;quot;v&quot; &amp;quot;w&quot; &amp;quot;x&quot; &amp;quot;y&quot; &amp;quot;z&quot; &amp;quot;A&quot; &amp;quot;B&quot; &amp;quot;C&quot; &amp;quot;D&quot; &amp;quot;E&quot; &amp;quot;F&quot; &amp;quot;G&quot; &amp;quot;H&quot; &amp;quot;I&quot; &amp;quot;J&quot; &amp;quot;K&quot; &amp;quot;L&quot; &amp;quot;M&quot; &amp;quot;N&quot; &amp;quot;O&quot; &amp;quot;P&quot; &amp;quot;Q&quot; &amp;quot;R&quot; &amp;quot;S&quot; &amp;quot;T&quot; &amp;quot;U&quot; &amp;quot;V&quot; &amp;quot;W&quot; &amp;quot;X&quot; &amp;quot;Y&quot; &amp;quot;Z&quot; &amp;quot;&#x26;&quot; &amp;quot;&#x27;&quot; &amp;quot;&#x22;&#x22;&quot;";
        String output = Entities.escape(input, null);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt;lt&gt; &amp;amp; &amp;apos; &amp;quot; &amp;apos;a&apos; &amp;quot;b&quot; &amp;quot;&quot; &amp;quot;c&quot;d &amp;quot;e&quot;f &amp;quot;g&quot; &amp;gt;h &amp;quot;i&quot;j &amp;quot;k&quot; &amp;quot;l&quot; &amp;quot;m&quot; &amp;quot;n&quot; &amp;quot;o&quot; &amp;quot;o&quot; &amp;quot;p&quot; &amp;quot;q&quot; &amp;quot;r&quot; &amp;quot;s&quot;s &amp;quot;t&quot; &amp;quot;u&quot; &amp;quot;u&quot; &amp;quot;v&quot; &amp;quot;w&quot; &amp;quot;x&quot; &amp;quot;y&quot; &amp;quot;z&quot; &amp;quot;&#x26;&quot; &amp;quot;&#x27;&quot; &amp;quot;&#x22;&#x22;&quot;";
        String expectedOutput = "&lt;lt&gt; &amp; &apos; &quot; &apos;a&apos; &quot;b&quot; &quot;&quot; &quot;c&quot;d &quot;e&quot;f &quot;g&quot; &gt;h &quot;i&quot;j &quot;k&quot; &quot;l&quot; &quot;m&quot; &quot;n&quot; &quot;o&quot; &quot;o&quot; &quot;p&quot; &quot;q&quot; &quot;r&quot; &quot;s&quot; &quot;t&quot; &quot;u&quot; &quot;u&quot; &quot;v&quot; &quot;w&quot; &quot;x&quot; &quot;y&quot; &quot;z&quot; &quot;&amp;&quot; &quot;&apos;&quot; &quot;&quot;&quot;";
        String output = Entities.unescape(input);
        assertEquals(expectedOutput, output);
    }
}