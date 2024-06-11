import org.jsoup.nodes.Entities;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Entities entities = new Entities();
        Map<Character, String> fullMap =Entities.full;
        char lt = '<';
        Assert.assertTrue(entities.isNamedEntity(entities.getCharacterByName("lt").toString()));
        Assert.assertFalse(entities.isNamedEntity(""));
        Assert.assertFalse(entities.isNamedEntity(null));
        Assert.assertFalse(entities.isNamedEntity("ltx"));
        Assert.assertFalse(entities.isNamedEntity(" "));
    }

    @Test
    public void testIsBaseNamedEntity() {
        Entities entities = new Entities();
        Map<Character, String> baseMap =Entities.base;
        char amp = '&';
        Assert.assertTrue(entities.isBaseNamedEntity(entities.getCharacterByName("amp").toString()));
        Assert.assertFalse(entities.isBaseNamedEntity(""));
        Assert.assertFalse(entities.isBaseNamedEntity(null));
        Assert.assertFalse(entities.isBaseNamedEntity("ampx"));
        Assert.assertFalse(entities.isBaseNamedEntity(" "));
    }

    @Test
    public void testGetCharacterByName() {
        Entities entities = new Entities();
        Map<Character, String> fullMap =Entities.full;
        char lt = '<';
        char quot = '\"';
        Assert.assertEquals(lt, entities.getCharacterByName("lt"));
        Assert.assertEquals(quot, entities.getCharacterByName("quot"));
        Assert.assertNull(entities.getCharacterByName(""));
        Assert.assertNull(entities.getCharacterByName(null));
        Assert.assertNull(entities.getCharacterByName("unknownEntity"));
    }

    @Test(expected = NullPointerException.class)
    public void testEscapeNullString() {
        Entities entities = new Entities();
        String nullString = null;
        entities.escape(null, nullString, null, false, false, false);
    }

    @Test
    public void testEscapeWithDefaultSettings() {
        Entities entities = new Entities();
        StringBuilder accum = new StringBuilder();
        String string = "&<>\"";
        entities.escape(accum, string, null, false, false, false);
        String result = accum.toString();
        String expected = "&amp;&lt;&gt;&quot;";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testEscapeWithXHTMLSettings() {
        Entities entities = new Entities();
        StringBuilder accum = new StringBuilder();
        String string = "&<>\"";
        entities.escape(accum, string, null, true, false, false);
        String result = accum.toString();
        String expected = "&amp;<>&gt;\"";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testEscapeWithCustomSettings() {
        Entities entities = new Entities();
        StringBuilder accum = new StringBuilder();
        String string = "&<>\"";
        entities.escape(accum, string, null, false, true, true);
        String result = accum.toString();
        String expected = "&amp;lt;gt;amp;quot;";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testUnescape() {
        Entities entities = new Entities();
        String string = "&amp;lt;gt;amp;quot;";
        String result = entities.unescape(string);
        String expected = "<>\"";
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testCanEncodeASCII() {
        Entities entities = new Entities();
        CoreCharset ascii = Entities.CoreCharset.ascii;
        char c = 'A';
        Assert.assertTrue(entities.canEncode(ascii, c, null));
    }

    @Test
    public void testCanEncodeUTF() {
        Entities entities = new Entities();
        CoreCharset utf = Entities.CoreCharset.utf;
        char c = 'Ä';
        Assert.assertTrue(entities.canEncode(utf, c, null));
    }

    @Test
    public void testCanEncodeUnknownCharacter() {
        Entities entities = new Entities();
        CoreCharset fallback = Entities.CoreCharset.fallback;
        char c = '😍';
        Assert.assertFalse(entities.canEncode(fallback, c, null));
    }

    @Test
    public void testUnloadingEntities() {
        Map<Character, String> baseMap = new HashMap<>();
        Map<String, Character> baseEntityMap = new HashMap<>();
        Map<Character, String> fullMap = new HashMap<>();
        Map<String, Character> fullEntityMap = new HashMap<>();
        Entities entities = new Entities();
        entities.base = baseMap;
        entities.baseByVal = baseEntityMap;
        entities.full = fullMap;
        entities.fullByVal = fullEntityMap;
        entities = null;
        Assert.assertNull(entities.base);
        Assert.assertNull(entities.baseByVal);
        Assert.assertNull(entities.full);
        Assert.assertNull(entities.fullByVal);
    }

}