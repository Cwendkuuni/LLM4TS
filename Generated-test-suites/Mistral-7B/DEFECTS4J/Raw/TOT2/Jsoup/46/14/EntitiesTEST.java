import org.junit.Assert;
import org.junit.Test;
import org.jsoup.nodes.Entities;
import org.jsoup.helper.StringUtil;
import java.util.HashMap;
import java.util.Map;

public class EntitiesTest {

    @Test
    public void testIsNamedEntity() {
        Map<String, Character> entities = Entities.full;
        String namedEntity = "amp";
        boolean expectedResult = true;
        boolean actualResult = Entities.isNamedEntity(namedEntity);
        Assert.assertEquals("Expected named entity 'amp' to be present in Entities.full map", expectedResult, actualResult);
    }

    @Test
    public void testIsBaseNamedEntity() {
        Map<String, Character> entities = Entities.base;
        String namedEntity = "lt";
        boolean expectedResult = true;
        boolean actualResult = Entities.isBaseNamedEntity(namedEntity);
        Assert.assertEquals("Expected named entity 'lt' to be present in Entities.base map", expectedResult, actualResult);
    }

    @Test
    public void testGetCharacterByName() {
        Character expectedResult = '<';
        String namedEntity = "lt";
        Character actualResult = Entities.getCharacterByName(namedEntity);
        Assert.assertEquals("Expected named entity 'lt' to map to character '<'", expectedResult, actualResult);
    }

    @Test
    public void testEscapeBasicCase() {
        String input = "This is a test.";
        String expectedOutput = "This is a test.";
        Document.OutputSettings settings = new Document.OutputSettings();
        StringBuilder actualOutput = new StringBuilder();
        Entities.escape(actualOutput, input, settings, false, false, false);
        Assert.assertEquals("Expected basic escape to have no effect on the input string", expectedOutput, actualOutput.toString());
    }

    @Test
    public void testEscapeAttribute() {
        String input = "<test attr='<value'>";
        String expectedOutput = "&lt;test attr='&lt;value&gt;'";
        Document.OutputSettings settings = new Document.OutputSettings();
        StringBuilder actualOutput = new StringBuilder();
        Entities.escape(actualOutput, input, settings, true, false, false);
        Assert.assertEquals("Expected attribute to be escaped", expectedOutput, actualOutput.toString());
    }

    @Test
    public void testEscapeNormalizeWhite() {
        String input = "This   is   a test.";
        String expectedOutput = "This is a test.";
        Document.OutputSettings settings = new Document.OutputSettings();
        StringBuilder actualOutput = new StringBuilder();
        Entities.escape(actualOutput, input, settings, false, true, false);
        Assert.assertEquals("Expected normalizeWhite to remove extra whitespaces", expectedOutput, actualOutput.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEscapeInvalidCharacter() {
        String input = "☹";
        String expectedOutput = "&#x2639;";
        Document.OutputSettings settings = new Document.OutputSettings();
        StringBuilder actualOutput = new StringBuilder();
        Entities.escape(actualOutput, input, settings, false, false, false);
        Assert.assertEquals("Expected an IllegalArgumentException when encountering an unsupported character", expectedOutput, actualOutput.toString());
    }

    @Test
    public void testCanEncodeASCIICharacter() {
        CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();
        char character = 'A';
        boolean expectedResult = true;
        boolean actualResult = Entities.canEncode(Entities.CoreCharset.utf, character, encoder);
        Assert.assertEquals("Expected the character 'A' to be encodable", expectedResult, actualResult);
    }

    @Test
    public void testCanEncodeNonASCIICharacter() {
        CharsetEncoder encoder = Charset.forName("UTF-8").newEncoder();
        char character = '\u00A0';
        boolean expectedResult = true;
        boolean actualResult = Entities.canEncode(Entities.CoreCharset.utf, character, encoder);
        Assert.assertEquals("Expected the non-ASCII character '\u00A0' to be encodable", expectedResult, actualResult);
    }

    @Test(expected = UnsupportedEncodingException.class)
    public void testCannotEncodeNonSupportedCharacter() {
        CharsetEncoder encoder = Charset.forName("US-ASCII").newEncoder();
        char character = '\u00A0';
        boolean expectedResult = false;
        boolean actualResult = Entities.canEncode(Entities.CoreCharset.ascii, character, encoder);
        Assert.assertEquals("Expected an UnsupportedEncodingException when encountering a non-ASCII character", expectedResult, actualResult);
    }
}