import org.junit.Test;
import org.jsoup.nodes.Entities;

import static org.junit.Assert.*;

public class EntitiesTest {

    
  
    @Test
    public void testIsNamedEntity_True() {
        assertTrue(Entities.isNamedEntity("lt"));
    }
  
    @Test
    public void testIsNamedEntity_False() {
        assertFalse(Entities.isNamedEntity("unknown"));
    }
  
    @Test
    public void testIsBaseNamedEntity_True() {
        assertTrue(Entities.isBaseNamedEntity("lt"));
    }
  
    @Test
    public void testIsBaseNamedEntity_False() {
        assertFalse(Entities.isBaseNamedEntity("unknown"));
    }
  
    @Test
    public void testGetCharacterByName() {
        assertEquals('<', Entities.getCharacterByName("lt").charValue());
    }
  
    @Test
    public void testEscape() {
        assertEquals("&lt;&gt;", Entities.escape("<>", new Document.OutputSettings()));
    }
  
    @Test
    public void testEscape_WithWhitespaceNormalization() {
        Document.OutputSettings settings = new Document.OutputSettings().normaliseWhitespace(true);
        assertEquals(" a  b", Entities.escape("a \n b", settings));
    }
  
    @Test
    public void testEscape_WithAttribute() {
        Document.OutputSettings settings = new Document.OutputSettings().escapeMode(Entities.EscapeMode.xhtml);
        assertEquals("<&quot;&gt;", Entities.escape("<\">", settings));
    }
  
    @Test
    public void testUnescape() {
        assertEquals("<>", Entities.unescape("&lt;&gt;"));
    }
  
    @Test
    public void testUnescape_Strict() {
        assertEquals("<>", Entities.unescape("&lt;&gt;", true));
    }
  
    

}