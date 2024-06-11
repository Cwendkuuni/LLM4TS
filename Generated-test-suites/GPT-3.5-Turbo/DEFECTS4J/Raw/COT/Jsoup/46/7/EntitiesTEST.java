import org.jsoup.nodes.Entities;
import org.junit.Test;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        boolean result = Entities.isNamedEntity("lt");
        assert(result == true);

        result = Entities.isNamedEntity("amp");
        assert(result == true);

        result = Entities.isNamedEntity("foo");
        assert(result == false);
    }

    @Test
    public void testIsBaseNamedEntity() {
        boolean result = Entities.isBaseNamedEntity("lt");
        assert(result == true);

        result = Entities.isBaseNamedEntity("amp");
        assert(result == true);

        result = Entities.isBaseNamedEntity("foo");
        assert(result == false);
    }

    @Test
    public void testGetCharacterByName() {
        Character character = Entities.getCharacterByName("quot");
        assert(character == '\"');

        character = Entities.getCharacterByName("lt");
        assert(character == '<');

        character = Entities.getCharacterByName("foo");
        assert(character == null);
    }

    @Test
    public void testEscape() {
        String result = Entities.escape("&", Document.OutputSettings.xhtml());
        assert(result.equals("&amp;"));

        result = Entities.escape("<", Document.OutputSettings.xhtml());
        assert(result.equals("&lt;"));

        result = Entities.escape(">", Document.OutputSettings.xhtml());
        assert(result.equals("&gt;"));

        result = Entities.escape("\"", Document.OutputSettings.xhtml());
        assert(result.equals("&quot;"));

        result = Entities.escape("hello world", Document.OutputSettings.xhtml());
        assert(result.equals("hello world"));

        result = Entities.escape("©", Document.OutputSettings.xhtml());
        assert(result.equals("©"));
    }

    @Test
    public void testUnescape() {
        String result = Entities.unescape("&amp;");
        assert(result.equals("&"));

        result = Entities.unescape("&lt;");
        assert(result.equals("<"));

        result = Entities.unescape("&gt;");
        assert(result.equals(">"));

        result = Entities.unescape("&quot;");
        assert(result.equals("\""));

        result = Entities.unescape("hello &amp; world");
        assert(result.equals("hello & world"));

        result = Entities.unescape("©");
        assert(result.equals("©"));
    }

}