import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.junit.Before;
import org.junit.Test;

public class ParseSettingsTest {
    private ParseSettings htmlDefault;
    private ParseSettings preserveCase;

    @Before
    public void setup() {
        htmlDefault = ParseSettings.htmlDefault;
        preserveCase = ParseSettings.preserveCase;
    }

    @Test
    public void testPreserveTagCase() {
        assertTrue(htmlDefault.preserveTagCase());
        assertTrue(preserveCase.preserveTagCase());
    }

    @Test
    public void testPreserveAttributeCase() {
        assertFalse(htmlDefault.preserveAttributeCase());
        assertTrue(preserveCase.preserveAttributeCase());
    }

    @Test
    public void testNormalizeTag() {
        assertEquals("p", htmlDefault.normalizeTag("P"));
        assertEquals("div", preserveCase.normalizeTag("div"));
        assertEquals("span", preserveCase.normalizeTag("SPAN"));
    }

    @Test
    public void testNormalizeAttribute() {
        assertEquals("class", htmlDefault.normalizeAttribute("CLASS"));
        assertEquals("id", preserveCase.normalizeAttribute("id"));
        assertEquals("data-attr", preserveCase.normalizeAttribute("DATA-ATTR"));
    }

    @Test
    public void testNormalizeAttributes() {
        Attributes attributes = new Attributes();
        attributes.put("class", "example");
        attributes.put("ID", "123");
        
        Attributes normalizedAttrs = htmlDefault.normalizeAttributes(attributes);
        assertEquals("example", normalizedAttrs.get("class"));
        assertEquals("123", normalizedAttrs.get("id"));
        
        normalizedAttrs = preserveCase.normalizeAttributes(attributes);
        assertEquals("example", normalizedAttrs.get("class"));
        assertEquals("123", normalizedAttrs.get("ID"));
    }
}