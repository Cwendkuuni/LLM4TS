package org.jsoup.nodes;

import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        Assert.assertTrue(Entities.isNamedEntity("amp"));
        Assert.assertFalse(Entities.isNamedEntity("abc"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Assert.assertTrue(Entities.isBaseNamedEntity("lt"));
        Assert.assertTrue(Entities.isBaseNamedEntity("amp"));
        Assert.assertFalse(Entities.isBaseNamedEntity("abc"));
    }

    @Test
    public void testGetCharacterByName() {
        Assert.assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        Assert.assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        Assert.assertNull(Entities.getCharacterByName("abc"));
    }

    @Test
    public void testEscape() {
        String input = "<span>&amp;</span>";
        String expectedOutput = "&lt;span&gt;&amp;amp;&lt;/span&gt;";
        Assert.assertEquals(expectedOutput, Entities.escape(input, null));
    }

    @Test
    public void testUnescape() {
        String input = "&lt;span&gt;&amp;amp;&lt;/span&gt;";
        String expectedOutput = "<span>&amp;</span>";
        Assert.assertEquals(expectedOutput, Entities.unescape(input));
    }
    
}
