import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class EntitiesTest {
    private Entities.EscapeMode escapeMode;
    
    @Before
    public void setUp() {
        escapeMode = Entities.EscapeMode.xhtml;
    }
    
    @Test
    public void testIsNamedEntity() {
        boolean result = Entities.isNamedEntity("lt");
        assertTrue(result);
    }
    
    @Test
    public void testIsBaseNamedEntity() {
        boolean result = Entities.isBaseNamedEntity("amp");
        assertTrue(result);
    }
    
    @Test
    public void testGetCharacterByName() {
        Character character = Entities.getCharacterByName("gt");
        assertEquals('>', character.charValue());
    }
    
    @Test
    public void testEscape() {
        String string = "Hello&nbsp;World";
        Entities.escape(string, Document.OutputSettings.html());
        // Add assertions for the expected output
    }
    
    @Test
    public void testEscape_inAttribute() {
        String string = "&";
        Entities.escape(string, Document.OutputSettings.html().escapeMode(Entities.EscapeMode.xhtml));
        // Add assertions for the expected output
    }
    
    @Test
    public void testUnescape() {
        String string = "Hello&apos;World";
        Entities.unescape(string);
        // Add assertions for the expected output
    }
    
    @Test
    public void testUnescape_strict() {
        String string = "Hello&";
        Entities.unescape(string, true);
        // Add assertions for the expected output
    }
    
    @Test
    public void testCanEncode_ascii() {
        boolean result = Entities.canEncode(CoreCharset.ascii, 'A', null);
        assertTrue(result);
    }
    
    @Test
    public void testCanEncode_utf() {
        boolean result = Entities.canEncode(CoreCharset.utf, '♦', null);
        assertTrue(result);
    }
    
    @Test
    public void testCanEncode_fallback() {
        boolean result = Entities.canEncode(CoreCharset.fallback, '\u00A9', null);
        assertTrue(result);
    }
    
    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        // Add assertions for the expected entities
    }
    
    @Test
    public void testToCharacterKey() {
        Map<String, Character> inMap = new HashMap<>();
        inMap.put("lt", '<');
        Map<Character, String> outMap = Entities.toCharacterKey(inMap);
        // Add assertions for the expected outMap
    }
}