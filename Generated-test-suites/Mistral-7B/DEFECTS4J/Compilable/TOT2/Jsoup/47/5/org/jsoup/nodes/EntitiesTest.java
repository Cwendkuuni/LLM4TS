package org.jsoup.nodes;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        // add more tests for other entities based on the given class
    }
}