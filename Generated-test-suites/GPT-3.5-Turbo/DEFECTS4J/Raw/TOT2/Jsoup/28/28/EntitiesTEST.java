import org.hamcrest.core.IsEqual;
import org.jsoup.nodes.Entities;
import org.junit.Test;

import static org.junit.Assert.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assertFalse(Entities.isNamedEntity("invalid"));
        assertTrue(Entities.isNamedEntity("amp"));
    }

    @Test
    public void testGetCharacterByName() {
        assertThat(Entities.getCharacterByName("lt"), IsEqual.equalTo('<'));
    }

    @Test
    public void testEscape() {
        String escaped = Entities.escape("hello&world", null, Entities.EscapeMode.xhtml);
        assertEquals("hello&amp;world", escaped);
    }

    @Test
    public void testUnescape() {
        String unescaped = Entities.unescape("hello&amp;world");
        assertEquals("hello&world", unescaped);
    }

    @Test
    public void testLoadEntities() {
        assertNotNull(Entities.loadEntities("entities-base.properties"));
    }

    @Test
    public void testToCharacterKey() {
        assertNotNull(Entities.toCharacterKey(Entities.full));
    }
}