import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity_typicalUseCase() {
        assertTrue(Entities.isNamedEntity("lt"));
    }

    @Test
    public void testIsNamedEntity_edgeCase() {
        assertFalse(Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testIsBaseNamedEntity_typicalUseCase() {
        assertTrue(Entities.isBaseNamedEntity("amp"));
    }

    @Test
    public void testIsBaseNamedEntity_edgeCase() {
        assertFalse(Entities.isBaseNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName_typicalUseCase() {
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
    }
    
    @Test
    public void testGetCharacterByName_edgeCase() {
        assertNull("Expected null", Entities.getCharacterByName("unknown"));
    }

    @Test
    public void testEscape_typicalUseCase() {
        String expected = "&lt;Hello&gt;";
        String result = Entities.escape("<Hello>");
        assertEquals(expected, result);
    }

    @Test
    public void testEscape_edgeCase() {
        String expected = "&amp;";
        String result = Entities.escape("&");
        assertEquals(expected, result);
    }

    @Test
    public void testUnescape_typicalUseCase() {
        String expected = "<Hello>";
        String result = Entities.unescape("&lt;Hello&gt;");
        assertEquals(expected, result);
    }

    @Test
    public void testUnescape_edgeCase() {
        String expected = "&";
        String result = Entities.unescape("&amp;");
        assertEquals(expected, result);
    }
}