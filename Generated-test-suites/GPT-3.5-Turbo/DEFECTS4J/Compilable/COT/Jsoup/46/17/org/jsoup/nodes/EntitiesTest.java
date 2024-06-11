package org.jsoup.nodes;
import org.junit.*;


public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        boolean result = Entities.isNamedEntity("lt");
        Assert.assertTrue(result);

        result = Entities.isNamedEntity("gt");
        Assert.assertTrue(result);

        result = Entities.isNamedEntity("amp");
        Assert.assertTrue(result);

        result = Entities.isNamedEntity("quot");
        Assert.assertTrue(result);

        result = Entities.isNamedEntity("abc");
        Assert.assertFalse(result);
    }

    @Test
    public void testIsBaseNamedEntity() {
        boolean result = Entities.isBaseNamedEntity("lt");
        Assert.assertTrue(result);

        result = Entities.isBaseNamedEntity("gt");
        Assert.assertTrue(result);

        result = Entities.isBaseNamedEntity("amp");
        Assert.assertTrue(result);

        result = Entities.isBaseNamedEntity("quot");
        Assert.assertTrue(result);

        result = Entities.isBaseNamedEntity("abc");
        Assert.assertFalse(result);
    }

    @Test
    public void testGetCharacterByName() {
        Character result = Entities.getCharacterByName("lt");
        Assert.assertEquals(result, Character.valueOf('<'));

        result = Entities.getCharacterByName("gt");
        Assert.assertEquals(result, Character.valueOf('>'));

        result = Entities.getCharacterByName("amp");
        Assert.assertEquals(result, Character.valueOf('&'));

        result = Entities.getCharacterByName("quot");
        Assert.assertEquals(result, Character.valueOf('"'));

        result = Entities.getCharacterByName("abc");
        Assert.assertNull(result);
    }

    @Test
    public void testEscape() {
        String result = Entities.escape("<html>", null);
        Assert.assertEquals(result, "&lt;html&gt;");

        result = Entities.escape("AT&T", null);
        Assert.assertEquals(result, "AT&amp;T");

        result = Entities.escape("Hello&nbsp;World", null);
        Assert.assertEquals(result, "Hello&nbsp;World");

        result = Entities.escape("Hello", null);
        Assert.assertEquals(result, "Hello");
    }

    @Test
    public void testUnescape() {
        String result = Entities.unescape("&lt;html&gt;");
        Assert.assertEquals(result, "<html>");

        result = Entities.unescape("AT&amp;T");
        Assert.assertEquals(result, "AT&T");

        result = Entities.unescape("Hello&nbsp;World");
        Assert.assertEquals(result, "Hello World");

        result = Entities.unescape("Hello");
        Assert.assertEquals(result, "Hello");
    }

}