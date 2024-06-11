import org.jsoup.nodes.*;
import org.junit.Test;
import java.util.*;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        assert(Entities.isNamedEntity("lt") == true);
        assert(Entities.isNamedEntity("gt") == true);
        assert(Entities.isNamedEntity("amp") == true);
        assert(Entities.isNamedEntity("apos") == true);
        assert(Entities.isNamedEntity("quot") == true);
        assert(Entities.isNamedEntity("invalid") == false);
    }

    @Test
    public void testGetCharacterByName() {
        assert(Entities.getCharacterByName("lt").equals('<'));
        assert(Entities.getCharacterByName("gt").equals('>'));
        assert(Entities.getCharacterByName("amp").equals('&'));
        assert(Entities.getCharacterByName("apos").equals('\''));
        assert(Entities.getCharacterByName("quot").equals('\"'));
        assert(Entities.getCharacterByName("invalid") == null);
    }

    @Test
    public void testEscape() {
        Document.OutputSettings settings = new Document.OutputSettings();
        settings.escapeMode = Entities.EscapeMode.xhtml;
        assert(Entities.escape("hello", settings).equals("hello"));
        assert(Entities.escape("<div>hello</div>", settings).equals("&lt;div&gt;hello&lt;/div&gt;"));
        assert(Entities.escape("&#60;&#62;&#38;&#39;&#34;", settings).equals("&lt;&gt;&amp;&apos;&quot;"));
    }

    // More test methods for other methods in Entities class

}