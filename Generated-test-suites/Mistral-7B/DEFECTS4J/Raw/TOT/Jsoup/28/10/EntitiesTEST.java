import org.junit.Test;
import org.jsoup.nodes.Entities;

public class EntitiesTest {
    

    @Test
    public void testIsNamedEntity() {
        assert(Entities.isNamedEntity("lt"));
        assert(Entities.isNamedEntity("amp"));
        assert(Entities.isNamedEntity("quot"));
        assert(!Entities.isNamedEntity("x"));
    }

    @Test
    public void testGetCharacterByName() {
        assert(Entities.getCharacterByName("lt") == '<');
        assert(Entities.getCharacterByName("amp") == '&');
        assert(Entities.getCharacterByName("apos") == '\'');
        assert(Entities.getCharacterByName("quot") == '\"');
        assert(Entities.getCharacterByName("gt") == '>');
    }

    @Test
    public void testEscape() {
        String input = "&lt;Hello&amp; World!&gt;";
        String output = Entities.escape(input, null, Entities.Entities.extended);
        assert(output.equals("&amp;lt;Hello&amp;amp;amp;amp;amp;amp;amp;lt;Hello&amp;amp;amp;amp;amp;amp;gt;Hello&amp;amp;amp;amp;amp;amp;gt;World!&amp;gt;"));
    }

    @Test
    public void testUnescape() {
        String input = "&amp;lt;Hello&amp;amp;amp;amp;amp;amp;amp;lt;Hello&amp;amp;gt;World!&amp;gt;";
        String output = Entities.unescape(input);
        assert(output.equals("<Hello<Hello>World!"));
    }

    
}