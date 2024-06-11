import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> fullEntities = Entities.full;
        for (Map.Entry<String, Character> entry : fullEntities.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            boolean result = Entities.isNamedEntity(name);
            Assert.assertTrue("Named entity '" + name + "' should be recognized.", result);
            Assert.assertEquals("Named entity '" + name + "' should return corresponding character.", character, Entities.getCharacterByName(name));
        }
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<String, Character> baseEntities = Entities.base;
        for (Map.Entry<String, Character> entry : baseEntities.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            boolean result = Entities.isBaseNamedEntity(name);
            Assert.assertTrue("Base named entity '" + name + "' should be recognized.", result);
            Assert.assertEquals("Base named entity '" + name + "' should return corresponding character.", character, Entities.getCharacterByName(name));
        }

        // check for entities not in the base set
        for (Map.Entry<String, Character> entry : Entities.full.entrySet()) {
            String name = entry.getKey();
            Character character = entry.getValue();
            if (!Entities.isBaseNamedEntity(name)) {
                boolean result = Entities.isNamedEntity(name);
                Assert.assertTrue("Named entity '" + name + "' should be recognized even if not in base set.", result);
                Assert.assertEquals("Named entity '" + name + "' should return corresponding character.", character, Entities.getCharacterByName(name));
            }
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIsNamedEntityNullName() {
        String nullName = null;
        boolean result = Entities.isNamedEntity(nullName);
        Assert.assertFalse("isNamedEntity should throw an IllegalArgumentException for null name.", result);
    }

    @Test
    public void testGetCharacterByNameNullName() {
        String nullName = null;
        try {
            Character result = Entities.getCharacterByName(nullName);
            Assert.fail("getCharacterByName should throw a NullPointerException for null name.");
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testEscapeMethod() {
        StringBuilder expected = new StringBuilder("&amp;lt;quot;&nbsp;Hello&nbsp;World;&gt;&quot;");
        String input = "<quot>Hello World</quot>";
        String result = Entities.escape(input, null);
        Assert.assertEquals("Escape method should return expected result.", expected.toString(), result);

        // test edge cases
        String emptyString = "";
        String whitespace = " ";
        String whitespaceAndText = " Hello";
        String textOnly = "Text";
        String onlyAscii = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        String asciiAndSpecial = "abc&defghijklmnopqrstuvwxyzABC&DEFGHIJKLMNOPQRSTUVWXYZ0123456789";

        StringBuilder expectedEdgeCases = new StringBuilder();
        expectedEdgeCases.append("&amp;").append(Entities.getCharacterByName("quot")).append(";"); // empty string
        expectedEdgeCases.append("&nbsp;"); // whitespace
        expectedEdgeCases.append("&nbsp;").append(Entities.getCharacterByName("quot")).append(";").append("Hello").append("&nbsp;"); // whitespace and text
        expectedEdgeCases.append("&quot;").append("Text"); // text only
        expectedEdgeCases.append("&amp;#x61;").append("&amp;#x62;").append("&amp;#x63;").append("&amp;#x64;").append("&amp;#x65;").append("&amp;#x66;").append("&amp;#x67;").append("&amp;#x68;").append("&amp;#x69;").append("&amp;#x6A;").append("&amp;#x6B;").append("&amp;#x6C;").append("&amp;#x6D;").append("&amp;#x6E;").append("&amp;#x6F;").append("&amp;#x70;").append("&amp;#x71;").append("&amp;#x72;").append("&amp;#x73;").append("&amp;#x74;").append("&amp;#x75;").append("&amp;#x76;"); // ascii
        expectedEdgeCases.append("&amp;#x61;").append("&amp;#x62;").append("&amp;#x63;").append("&amp;#x64;").append("&amp;#x65;").append("&amp;#x66;").append("&amp;#x67;").append("&amp;#x68;").append("&amp;#x69;").append("&amp;#x6A;").append("&amp;#x6B;").append("&amp;#x6C;").append("&amp;#x6D;").append("&amp;#x6E;").append("&amp;#x6F;").append("&amp;#x70;").append("&amp;#x71;").append("&amp;#x72;").append("&amp;#x73;").append("&amp;#x74;").append("&amp;#x75;").append("&amp;#x76;").append("&amp;amp;").append("&amp;lt;").append("&amp;gt;").append("&amp;quot;").append("&nbsp;").append("Text"); // ascii and special

        for (String inputEdgeCase : new String[] { emptyString, whitespace, whitespaceAndText, textOnly, onlyAscii, asciiAndSpecial }) {
            StringBuilder actual = new StringBuilder();
            Entities.escape(actual, inputEdgeCase, null, false, false, false);
            Assert.assertEquals("Escape method should return expected result for edge cases.", expectedEdgeCases.toString(), actual.toString());
        }
    }

    @Test
    public void testUnescapeMethod() {
        String inputEscaped = "&amp;lt;quot;&nbsp;Hello&nbsp;World;&gt;&quot;";
        String expected = "<quot>Hello World</quot>";
        String result = Entities.unescape(inputEscaped);
        Assert.assertEquals("Unescape method should return expected result.", expected, result);
    }

    @Test
    public void testUnescapeWithStrictParameter() {
        String inputEscaped = "&amp;lt;quot;&nbsp;Hello&nbsp;World;&gt;&quot;";
        String expected = "<quot>Hello World</quot>";
        String result = Entities.unescape(inputEscaped, true);
        Assert.assertEquals("Unescape method should return expected result when strict parameter is true.", expected, result);

        String inputEscapedWithTrailingSemicolon = "&amp;lt;quot;&nbsp;Hello&nbsp;World;&gt;&quot;;&#x00;";
        String expectedWithTrailingSemicolon = "<quot>Hello World</quot>";
        String resultWithTrailingSemicolon = Entities.unescape(inputEscapedWithTrailingSemicolon, true);
        Assert.assertEquals("Unescape method should return expected result when strict parameter is true and input contains a trailing semicolon.", expectedWithTrailingSemicolon, resultWithTrailingSemicolon);
    }

    @Test
    public void testCanEncodeMethod() {
        CoreCharset ascii = CoreCharset.ascii;
        for (char c = 0; c < 0x7F; c++) {
            boolean result = Entities.canEncode(ascii, c, null);
            Assert.assertTrue("canEncode should return true for ASCII characters.", result);
        }

        CoreCharset utf = CoreCharset.utf;
        for (char c = 0; c < Character.MAX_VALUE; c++) {
            boolean result = Entities.canEncode(utf, c, null);
            Assert.assertTrue("canEncode should return true for all Unicode characters.", result);
        }

        // test edge cases
        char specialCharacter = '\uD800';
        Character highSurrogate = Character.toChars(specialCharacter)[0];
        char lowSurrogate = '\uDC00';
        Character lowSurrogateCharacter = Character.toChars(lowSurrogate)[0];
        CharsetEncoder fallback = null;

        boolean result1 = Entities.canEncode(CoreCharset.ascii, highSurrogate, fallback);
        Assert.assertFalse("canEncode should return false for high surrogate characters when using ASCII charset.", result1);

        boolean result2 = Entities.canEncode(CoreCharset.utf, highSurrogate, fallback);
        Assert.assertTrue("canEncode should return true for high surrogate characters when using UTF charset.", result2);

        boolean result3 = Entities.canEncode(CoreCharset.ascii, lowSurrogate, fallback);
        Assert.assertFalse("canEncode should return false for low surrogate characters when using ASCII charset.", result3);

        boolean result4 = Entities.canEncode(CoreCharset.utf, lowSurrogate, fallback);
        Assert.assertTrue("canEncode should return true for low surrogate characters when using UTF charset.", result4);

        boolean result5 = Entities.canEncode(CoreCharset.fallback, highSurrogate, null);
        Assert.assertTrue("canEncode should return true for high surrogate characters when using fallback charset.", result5);

        boolean result6 = Entities.canEncode(CoreCharset.fallback, lowSurrogate, null);
        Assert.assertTrue("canEncode should return true for low surrogate characters when using fallback charset.", result6);
    }
}