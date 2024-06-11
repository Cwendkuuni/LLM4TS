import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;
import org.jsoup.helper.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> fullEntities = Entities.full;
        String namedEntity = "amp";
        boolean expectedResult = true;
        boolean actualResult = Entities.isNamedEntity(namedEntity);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<String, Character> baseEntities = Entities.base;
        String baseNamedEntity = "lt";
        boolean expectedResult = true;
        boolean actualResult = Entities.isBaseNamedEntity(baseNamedEntity);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testGetCharacterByName() {
        Map<Character, String> fullEntitiesByVal = Entities.fullByVal;
        Character namedEntity = Character.forName("'#x26'");
        Character expectedResult = Entities.getCharacterByName("amp");
        Character actualResult = fullEntitiesByVal.get(namedEntity);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testEscapeMethodBasic() {
        StringBuilder accumulator = new StringBuilder();
        String testString = "&amp;lt;hello&quot; world&gt;";
        Entities.escape(accumulator, testString, null, false, false, false);
        String expectedResult = "&amp;amp;&amp;lt;hello&amp;quot; &amp;quot; world&amp;gt;";
        String actualResult = accumulator.toString();
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testEscapeMethodEdgeCases() {
        StringBuilder accumulator = new StringBuilder();
        String testString = "&amp;amp;lt;hello&quot; world&gt;&#x304;"; // combining character (diacritic) is added
        Entities.escape(accumulator, testString, null, false, false, false);
        String expectedResult = "&amp;amp;amp;lt;hello&amp;quot; &amp;quot; world&#x304;";
        String actualResult = accumulator.toString();
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeWithInvalidInput() {
        StringBuilder accumulator = new StringBuilder();
        String testString = null;
        Entities.escape(accumulator, testString, null, false, false, false);
    }

    @Test
    public void testUnescapeMethodBasic() {
        String testString = "&amp;amp;";
        String expectedResult = "&amp;";
        String actualResult = Entities.unescape(testString, false);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testUnescapeMethodWithTrailingSemicolon() {
        String testString = "&amp;lt; &quot;world&quot; &gt; &amp;";
        String expectedResult = "< world>";
        String actualResult = Entities.unescape(testString, true);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testCanEncodeMethodBasic() {
        CoreCharset charset = CoreCharset.ascii;
        char testChar = 'A';
        boolean expectedResult = true;
        boolean actualResult = Entities.canEncode(charset, testChar, null);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testCanEncodeMethodEdgeCases() {
        CoreCharset charset = CoreCharset.fallback;
        char testChar = Character.MIN_VALUE;
        boolean expectedResult = false;
        boolean actualResult = Entities.canEncode(charset, testChar, null);
        Assert.assertEquals(expectedResult, actualResult);
    }

    @Test
    public void testLoadEntitiesMethod() {
        Map<String, Character> entities = Entities.loadEntities("entities-base.properties");
        Character namedEntity = Character.forName("'#x26'");
        Character expectedResult = Entities.getCharacterByName("amp");
        Character actualResult = entities.get(namedEntity.toString());
        Assert.assertEquals(expectedResult, actualResult);
    }
}