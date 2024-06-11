package org.jsoup.nodes;
import org.jsoup.nodes.Entities;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Basic test
        assertTrue(Entities.isNamedEntity("lt"));

        // Edge cases
        assertFalse(Entities.isNamedEntity(""));
        assertFalse(Entities.isNamedEntity("lt "));
        assertFalse(Entities.isNamedEntity("lt\n"));
        assertFalse(Entities.isNamedEntity("lt\t"));
        assertFalse(Entities.isNamedEntity("lt\r"));
        assertFalse(Entities.isNamedEntity("lt\f"));

        // Exception Handling
        try {
            Entities.isNamedEntity(null);
            fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Basic test
        assertTrue(Entities.isBaseNamedEntity("amp"));

        // Edge cases
        assertFalse(Entities.isBaseNamedEntity(""));
        assertFalse(Entities.isBaseNamedEntity("amp "));
        assertFalse(Entities.isBaseNamedEntity("amp\n"));
        assertFalse(Entities.isBaseNamedEntity("amp\t"));
        assertFalse(Entities.isBaseNamedEntity("amp\r"));
        assertFalse(Entities.isBaseNamedEntity("amp\f"));

        // Exception Handling
        try {
            Entities.isBaseNamedEntity(null);
            fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }

    // Add test methods for other public methods in the Entities class
}