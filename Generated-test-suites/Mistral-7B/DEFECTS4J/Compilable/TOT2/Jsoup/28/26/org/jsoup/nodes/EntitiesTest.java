package org.jsoup.nodes;
import org.jsoup.nodes.Entities;
import org.junit.Assert;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Test known named entities
        Assert.assertTrue(Entities.isNamedEntity("lt"));
        Assert.assertTrue(Entities.isNamedEntity("amp"));
        Assert.assertTrue(Entities.isNamedEntity("quot"));
        Assert.assertTrue(Entities.isNamedEntity("apos"));
        Assert.assertTrue(Entities.isNamedEntity("gt"));

        // Test unknown named entities
        Assert.assertFalse(Entities.isNamedEntity("unknowne"));
    }
}