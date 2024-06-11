import static org.junit.Assert.*;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        String namedEntity = "amp";
        assertTrue(Entities.isNamedEntity(namedEntity));
    }

    @Test
    public void testIsBaseNamedEntity() {
        String baseNamedEntity = "lt";
        assertTrue(Entities.isBaseNamedEntity(baseNamedEntity));
    }

    @Test
    public void testGetCharacterByName() {
        String namedEntity = "amp";
        Character character = Entities.getCharacterByName(namedEntity);
        assertEquals('&', character);
    }

    // Add test cases for the remaining methods

}