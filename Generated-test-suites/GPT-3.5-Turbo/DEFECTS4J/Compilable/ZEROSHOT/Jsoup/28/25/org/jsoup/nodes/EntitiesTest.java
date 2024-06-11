package org.jsoup.nodes;
import org.jsoup.nodes.Entities;
import org.junit.Assert;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        boolean result = Entities.isNamedEntity("lt");
        Assert.assertTrue(result);

        result = Entities.isNamedEntity("gt");
        Assert.assertTrue(result);

        result = Entities.isNamedEntity("amp");
        Assert.assertTrue(result);

        result = Entities.isNamedEntity("apos");
        Assert.assertTrue(result);

        result = Entities.isNamedEntity("quot");
        Assert.assertTrue(result);

        result = Entities.isNamedEntity("invalid");
        Assert.assertFalse(result);
    }

    @Test
    public void testGetCharacterByName() {
        Character character = Entities.getCharacterByName("lt");
        Assert.assertEquals(Character.valueOf('<'), character);

        character = Entities.getCharacterByName("gt");
        Assert.assertEquals(Character.valueOf('>'), character);

        character = Entities.getCharacterByName("amp");
        Assert.assertEquals(Character.valueOf('&'), character);

        character = Entities.getCharacterByName("apos");
        Assert.assertEquals(Character.valueOf('\''), character);

        character = Entities.getCharacterByName("quot");
        Assert.assertEquals(Character.valueOf('"'), character);

        character = Entities.getCharacterByName("invalid");
        Assert.assertNull(character);
    }

    @Test
    public void testEscape() {
        String input = "<>&'\"";
        String output = Entities.escape(input, null);
        Assert.assertEquals("&lt;&gt;&amp;&apos;&quot;", output);
    }

    @Test
    public void testUnescape() {
        String input = "&lt;&gt;&amp;&apos;&quot;";
        String output = Entities.unescape(input);
        Assert.assertEquals("<>&'\"", output);
    }

}