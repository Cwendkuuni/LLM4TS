import org.junit.Test;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertEquals;

public class EntitiesTest {
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertFalse(Entities.isBaseNamedEntity("lt"));
        assertFalse(Entities.isBaseNamedEntity("quot"));
    }

    @Test
    public void testGetCharacterByName() {
        assertEquals(Entities.getCharacterByName("lt"), '<');
        assertEquals(Entities.getCharacterByName("amp"), '&');
        assertEquals(Entities.getCharacterByName("quot"), '\"');
    }

    // Add test cases for other methods here
}