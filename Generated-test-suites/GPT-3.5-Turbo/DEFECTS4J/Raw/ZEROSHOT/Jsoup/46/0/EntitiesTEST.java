import org.junit.Test;
import java.util.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        // Test for known named entity
        assert(Entities.isNamedEntity("lt"));
        // Test for unknown named entity
        assert(!Entities.isNamedEntity("unknown"));
    }

    @Test
    public void testIsBaseNamedEntity() {
        // Test for known named entity in the base entity set
        assert(Entities.isBaseNamedEntity("lt"));
        // Test for unknown named entity in the base entity set
        assert(!Entities.isBaseNamedEntity("unknown"));
    }

    @Test
    public void testGetCharacterByName() {
        // Test for getting character value of the named entity
        assert(Entities.getCharacterByName("lt") == Character.valueOf('<'));
        // Test for getting character value of unknown named entity
        assert(Entities.getCharacterByName("unknown") == null);
    }

    @Test
    public void testEscape() {
        // Test escaping HTML string
        String escaped = Entities.escape("<html>", new Document.OutputSettings());
        assert(escaped.equals("&lt;html&gt;"));
    }

    @Test
    public void testUnescape() {
        // Test unescaping HTML string
        String unescaped = Entities.unescape("&lt;html&gt;");
        assert(unescaped.equals("<html>"));
    }
}