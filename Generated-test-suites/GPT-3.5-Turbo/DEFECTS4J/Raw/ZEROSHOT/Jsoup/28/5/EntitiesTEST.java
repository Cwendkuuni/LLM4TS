package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        Assert.assertTrue(Entities.isNamedEntity("gt"));
        Assert.assertTrue(Entities.isNamedEntity("amp"));
        Assert.assertTrue(Entities.isNamedEntity("apos"));
        Assert.assertTrue(Entities.isNamedEntity("quot"));
        Assert.assertFalse(Entities.isNamedEntity("foobar"));
    }

    @Test
    public void testGetCharacterByName() {
        Assert.assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        Assert.assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        Assert.assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        Assert.assertEquals(Character.valueOf('\''), Entities.getCharacterByName("apos"));
        Assert.assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        Assert.assertNull(Entities.getCharacterByName("foobar"));
    }

    @Test
    public void testEscape() {
        String string = "<html>";
        String expected = "&lt;html&gt;";
        String actual = Entities.escape(string, Document.OutputSettings.html());
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testUnescape() {
        String string = "&lt;html&gt;";
        String expected = "<html>";
        String actual = Entities.unescape(string);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testUnescapeStrict() {
        String string = "&lt;html&gt;";
        String expected = "<html>";
        String actual = Entities.unescape(string, true);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> expected = new HashMap<>();
        expected.put("lt", '<');
        expected.put("gt", '>');
        expected.put("amp", '&');
        expected.put("apos", '\'');
        expected.put("quot", '\"');
        Map<String, Character> actual = Entities.loadEntities("entities-base.properties");
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> inputMap = new HashMap<>();
        inputMap.put("lt", '<');
        inputMap.put("LT", '<');
        inputMap.put("gt", '>');
        inputMap.put("amp", '&');
        inputMap.put("apos", '\'');
        inputMap.put("quot", '\"');
        Map<Character, String> expected = new HashMap<>();
        expected.put('<', "lt");
        expected.put('>', "gt");
        expected.put('&', "amp");
        expected.put('\'', "apos");
        expected.put('\"', "quot");
        Map<Character, String> actual = Entities.toCharacterKey(inputMap);
        Assert.assertEquals(expected, actual);
    }
}