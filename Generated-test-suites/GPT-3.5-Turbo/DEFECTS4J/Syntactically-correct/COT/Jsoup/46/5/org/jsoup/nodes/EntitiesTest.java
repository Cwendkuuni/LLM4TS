import org.junit.Test;
import org.junit.Assert;
import org.jsoup.nodes.Entities;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.CharsetEncoder;
import java.util.*;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        // test cases should cover both existing and non-existing named entities
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        Assert.assertTrue(Entities.isNamedEntity("amp"));
        Assert.assertFalse(Entities.isNamedEntity("xyz"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // test cases should cover both existing and non-existing named entities in the base set
        Assert.assertTrue(Entities.isBaseNamedEntity("lt"));
        Assert.assertTrue(Entities.isBaseNamedEntity("amp"));
        Assert.assertFalse(Entities.isBaseNamedEntity("xyz"));
    }

    @Test
    public void testGetCharacterByName() {
        // test cases should cover both existing and non-existing named entities
        Assert.assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        Assert.assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        Assert.assertNull(Entities.getCharacterByName("xyz"));
    }

    @Test
    public void testEscape() {
        // test cases should cover different scenarios for escaping HTML entities
        Assert.assertEquals("&lt;&amp;&gt;&quot;", Entities.escape("<&>\""));
        Assert.assertEquals("&lt;&nbsp;&gt;", Entities.escape("<\u00A0>")); // testing non-breakable space
    }

    @Test
    public void testEscape_withOutputSettings() {
        // additional test cases can be added here to cover specific output settings scenarios
    }

    @Test
    public void testUnescape() {
        // test cases should cover different scenarios for unescaping HTML entities
        Assert.assertEquals("<&>\"", Entities.unescape("&lt;&amp;&gt;&quot;"));
        Assert.assertEquals("<\u00A0>", Entities.unescape("&lt;&nbsp;&gt;")); // testing non-breakable space
    }

    @Test
    public void testUnescape_strict() {
        // additional test cases can be added here to cover strict unescaping scenarios
    }
}