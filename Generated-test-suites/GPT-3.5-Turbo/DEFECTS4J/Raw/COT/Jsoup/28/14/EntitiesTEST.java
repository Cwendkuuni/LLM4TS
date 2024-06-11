import org.junit.Assert;
import org.junit.Test;

public class EntitiesTest {
    
    @Test
    public void testIsNamedEntity() {
        boolean result = Entities.isNamedEntity("lt");
        Assert.assertTrue(result);
        
        result = Entities.isNamedEntity("gt");
        Assert.assertTrue(result);
        
        result = Entities.isNamedEntity("amp");
        Assert.assertTrue(result);
        
        result = Entities.isNamedEntity("apos");
        Assert.assertTrue(result);
        
        result = Entities.isNamedEntity("quot");
        Assert.assertTrue(result);
        
        result = Entities.isNamedEntity("test");
        Assert.assertFalse(result);
    }
    
    @Test
    public void testGetCharacterByName() {
        Character result = Entities.getCharacterByName("lt");
        Assert.assertEquals(result, Character.valueOf('<'));
        
        result = Entities.getCharacterByName("gt");
        Assert.assertEquals(result, Character.valueOf('>'));
        
        result = Entities.getCharacterByName("amp");
        Assert.assertEquals(result, Character.valueOf('&'));
        
        result = Entities.getCharacterByName("apos");
        Assert.assertEquals(result, Character.valueOf('\''));
        
        result = Entities.getCharacterByName("quot");
        Assert.assertEquals(result, Character.valueOf('\"'));
        
        result = Entities.getCharacterByName("test");
        Assert.assertNull(result);
    }
    
    @Test
    public void testEscape() {
        String result = Entities.escape("hello < > & \" '", Document.OutputSettings.html());
        Assert.assertEquals(result, "hello &lt; &gt; &amp; &quot; &#39;");
    }
    
    @Test
    public void testUnescape() {
        String result = Entities.unescape("hello &lt; &gt; &amp; &quot; &#39;");
        Assert.assertEquals(result, "hello < > & \" '");
        
        result = Entities.unescape("hello &lt; &gt; &amp;");
        Assert.assertEquals(result, "hello < > &");
    }
}