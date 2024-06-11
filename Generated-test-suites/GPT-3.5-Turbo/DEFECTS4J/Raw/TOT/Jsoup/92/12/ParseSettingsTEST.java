import org.jsoup.nodes.Attributes;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ParseSettingsTest {
    
    @Test
    public void testPreserveTagCase() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        
        boolean actualResult = parseSettings.preserveTagCase();
        
        assertEquals(true, actualResult);
    }
    
    @Test
    public void testPreserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        
        boolean actualResult = parseSettings.preserveAttributeCase();
        
        assertEquals(true, actualResult);
    }
    
    @Test
    public void testNormalizeTagWithPreserveTagCase() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        String tagName = "HtmlTag";
        String expectedTagName = tagName;
        
        String actualTagName = parseSettings.normalizeTag(tagName);
        
        assertEquals(expectedTagName, actualTagName);
    }
    
    @Test
    public void testNormalizeTagWithoutPreserveTagCase() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        String tagName = "HtmlTag";
        String expectedTagName = "htmltag";
        
        String actualTagName = parseSettings.normalizeTag(tagName);
        
        assertEquals(expectedTagName, actualTagName);
    }
    
    @Test
    public void testNormalizeAttributeWithPreserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        String attributeName = "AttrName";
        String expectedAttributeName = attributeName;
        
        String actualAttributeName = parseSettings.normalizeAttribute(attributeName);
        
        assertEquals(expectedAttributeName, actualAttributeName);
    }
    
    @Test
    public void testNormalizeAttributeWithoutPreserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        String attributeName = "AttrName";
        String expectedAttributeName = "attrname";
        
        String actualAttributeName = parseSettings.normalizeAttribute(attributeName);
        
        assertEquals(expectedAttributeName, actualAttributeName);
    }
    
    @Test
    public void testNormalizeAttributesWithPreserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(false, true);
        Attributes attributes = new Attributes();
        attributes.put("Attr1", "Value1");
        attributes.put("Attr2", "Value2");
        
        Attributes expectedAttributes = attributes;
        
        Attributes actualAttributes = parseSettings.normalizeAttributes(attributes);
        
        assertEquals(expectedAttributes, actualAttributes);
    }
    
    @Test
    public void testNormalizeAttributesWithoutPreserveAttributeCase() {
        ParseSettings parseSettings = new ParseSettings(true, false);
        Attributes attributes = new Attributes();
        attributes.put("Attr1", "Value1");
        attributes.put("Attr2", "Value2");
        
        attributes.normalize();
        
        Attributes expectedAttributes = attributes;
        
        Attributes actualAttributes = parseSettings.normalizeAttributes(attributes);
        
        assertEquals(expectedAttributes, actualAttributes);
    }
}