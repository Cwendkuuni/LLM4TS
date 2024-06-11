import org.junit.Test;
import java.util.Map;
import java.util.HashMap;
import java.util.Properties;
import java.io.InputStream;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        boolean result1 = Entities.isNamedEntity("lt");
        assert(result1 == true);

        boolean result2 = Entities.isNamedEntity("gt");
        assert(result2 == true);

        boolean result3 = Entities.isNamedEntity("amp");
        assert(result3 == true);
    }

    @Test
    public void testGetCharacterByName() {
        Character result1 = Entities.getCharacterByName("lt");
        assert(result1 == '<');

        Character result2 = Entities.getCharacterByName("gt");
        assert(result2 == '>');

        Character result3 = Entities.getCharacterByName("amp");
        assert(result3 == '&');
    }

    @Test
    public void testEscape() {
        String string1 = Entities.escape("<Hello World!>", Document.OutputSettings.base);
        assert(string1.equals("&lt;Hello World!&gt;"));

        String string2 = Entities.escape("\"Hello World!\"", Document.OutputSettings.xhtml);
        assert(string2.equals("&quot;Hello World!&quot;"));

        String string3 = Entities.escape("it's mine", Document.OutputSettings.base);
        assert(string3.equals("it's mine"));
    }

    @Test
    public void testUnescape() {
        String string1 = Entities.unescape("&lt;Hello World!&gt;");
        assert(string1.equals("<Hello World!>"));

        String string2 = Entities.unescape("&quot;Hello World!&quot;");
        assert(string2.equals("\"Hello World!\""));

        String string3 = Entities.unescape("it&apos;s mine");
        assert(string3.equals("it's mine"));
    }

    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        assert(entities.size() > 0);
    }

    @Test
    public void testToCharacterKey() {
        Map<String, Character> inMap = new HashMap<>();
        inMap.put("name1", 'a');
        inMap.put("name2", 'b');

        Map<Character, String> outMap = Entities.toCharacterKey(inMap);
        assert(outMap.containsKey('a'));
        assert(outMap.containsKey('b'));
    }
}