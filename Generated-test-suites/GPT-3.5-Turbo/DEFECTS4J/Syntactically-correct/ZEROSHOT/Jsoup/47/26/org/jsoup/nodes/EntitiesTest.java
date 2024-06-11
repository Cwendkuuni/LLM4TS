import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.StringUtil;
import org.jsoup.parser.Parser;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.CharsetEncoder;
import java.util.*;

import static org.junit.Assert.*;

public class EntitiesTest {
    
    @Before
    public void setUp() {
        // Set up any required variables or objects before each test method
    }
    
    @Test
    public void testIsNamedEntity() {
        assertTrue(Entities.isNamedEntity("quot"));
        assertTrue(Entities.isNamedEntity("amp"));
        assertTrue(Entities.isNamedEntity("lt"));
        assertTrue(Entities.isNamedEntity("gt"));
        assertFalse(Entities.isNamedEntity("random"));
    }
    
    @Test
    public void testIsBaseNamedEntity() {
        assertTrue(Entities.isBaseNamedEntity("quot"));
        assertTrue(Entities.isBaseNamedEntity("amp"));
        assertTrue(Entities.isBaseNamedEntity("lt"));
        assertTrue(Entities.isBaseNamedEntity("gt"));
        assertFalse(Entities.isBaseNamedEntity("random"));
    }
    
    @Test
    public void testGetCharacterByName() {
        assertEquals(Character.valueOf('"'), Entities.getCharacterByName("quot"));
        assertEquals(Character.valueOf('&'), Entities.getCharacterByName("amp"));
        assertEquals(Character.valueOf('<'), Entities.getCharacterByName("lt"));
        assertEquals(Character.valueOf('>'), Entities.getCharacterByName("gt"));
        assertNull(Entities.getCharacterByName("random"));
    }
    
    @Test
    public void testEscape() {
        String input = "<div>This is a test &amp; example.</div>";
        String expectedOutput = "&lt;div&gt;This is a test &amp;amp; example.&lt;/div&gt;";
        assertEquals(expectedOutput, Entities.escape(input, documentOutputSettings));
    }
    
    @Test
    public void testUnescape() {
        String input = "&lt;div&gt;This is a test &amp;amp; example.&lt;/div&gt;";
        String expectedOutput = "<div>This is a test &amp; example.</div>";
        assertEquals(expectedOutput, Entities.unescape(input));
    }
    
    @Test
    public void testEscape_inAttribute() {
        String input = "<div attr=\"This is a test &amp; example.\"></div>";
        String expectedOutput = "&lt;div attr=&quot;This is a test &amp;amp; example.&quot;&gt;&lt;/div&gt;";
        assertEquals(expectedOutput, Entities.escape(input, documentOutputSettings, true, false, false));
    }
    
    @Test
    public void testUnescape_strict() {
        String input = "&lt;div&gt;This is a test &amp;amp example.&lt;div&gt;";
        String expectedOutput = "<div>This is a test &amp;amp example.<div>";
        assertEquals(expectedOutput, Entities.unescape(input, true));
    }
    
    @Test
    public void testEscape_normaliseWhite() {
        String input = "This    is    a     test   example.";
        String expectedOutput = "This is a test example.";
        assertEquals(expectedOutput, Entities.escape(input, documentOutputSettings, false, true, false));
    }
    
    @Test
    public void testEscape_stripLeadingWhite() {
        String input = "     This is a test example.";
        String expectedOutput = "This is a test example.";
        assertEquals(expectedOutput, Entities.escape(input, documentOutputSettings, false, false, true));
    }
    
    @Test
    public void testEscape_coreCharset_ascii() {
        Entities.CoreCharset coreCharset = Entities.CoreCharset.byName("US-ASCII");
        assertTrue(Entities.canEncode(coreCharset, 'A', encoder));
        assertFalse(Entities.canEncode(coreCharset, 'Ā', encoder));
    }
    
    @Test
    public void testEscape_coreCharset_utf() {
        Entities.CoreCharset coreCharset = Entities.CoreCharset.byName("UTF-8");
        assertTrue(Entities.canEncode(coreCharset, 'A', encoder));
        assertTrue(Entities.canEncode(coreCharset, 'Ā', encoder));
    }
    
    @Test
    public void testEscape_coreCharset_fallback() {
        Entities.CoreCharset coreCharset = Entities.CoreCharset.byName("ISO-8859-1");
        assertTrue(Entities.canEncode(coreCharset, 'A', encoder));
        assertFalse(Entities.canEncode(coreCharset, 'Ā', encoder));
    }
    
    @Test
    public void testLoadEntities() {
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        assertEquals(Character.valueOf('"'), entities.get("quot"));
        assertEquals(Character.valueOf('&'), entities.get("amp"));
        assertEquals(Character.valueOf('<'), entities.get("lt"));
        assertEquals(Character.valueOf('>'), entities.get("gt"));
    }
    
    @Test
    public void testToCharacterKey() {
        Map<String, Character> inputMap = new HashMap<>();
        inputMap.put("quot", Character.valueOf('"'));
        inputMap.put("amp", Character.valueOf('&'));
        inputMap.put("lt", Character.valueOf('<'));
        inputMap.put("gt", Character.valueOf('>'));
        
        Map<Character, String> outputMap = Entities.toCharacterKey(inputMap);
        assertEquals("quot", outputMap.get(Character.valueOf('"')));
        assertEquals("amp", outputMap.get(Character.valueOf('&')));
        assertEquals("lt", outputMap.get(Character.valueOf('<')));
        assertEquals("gt", outputMap.get(Character.valueOf('>')));
    }
}