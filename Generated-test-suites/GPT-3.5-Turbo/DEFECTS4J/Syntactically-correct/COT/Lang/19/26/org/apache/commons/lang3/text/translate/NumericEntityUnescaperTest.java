import org.junit.Assert;
import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_NoEntity_ReturnsZero() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("No numeric entities");
        StringBuilder output = new StringBuilder();
        int translated = unescaper.translate(input, 0, output);
        Assert.assertEquals(0, translated);
        Assert.assertEquals("No numeric entities", output.toString());
    }

    @Test
    public void testTranslate_HexEntity_ReturnsTranslatedCharacter() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#x61;"); // Hex entity for 'a'
        StringBuilder output = new StringBuilder();
        int translated = unescaper.translate(input, 0, output);
        Assert.assertEquals(6, translated); // 2 (start tag) + 1 (hex indicator) + 1 (entity) + 1 (;) + 1 (end tag)
        Assert.assertEquals("a", output.toString());
    }

    @Test
    public void testTranslate_DecimalEntity_ReturnsTranslatedCharacter() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#97;"); // Decimal entity for 'a'
        StringBuilder output = new StringBuilder();
        int translated = unescaper.translate(input, 0, output);
        Assert.assertEquals(5, translated); // 2 (start tag) + 1 (entity) + 1 (;) + 1 (end tag)
        Assert.assertEquals("a", output.toString());
    }

    @Test
    public void testTranslate_HexEntityOutOfRange_ReturnsTwoTranslatedCharacters() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#x10000;"); // Hex entity beyond Unicode range
        StringBuilder output = new StringBuilder();
        int translated = unescaper.translate(input, 0, output);
        Assert.assertEquals(9, translated); // 2 (start tag) + 1 (hex indicator) + 5 (entity) + 1 (;) + 1 (end tag)
        Assert.assertEquals("\uD800\uDC00", output.toString()); // Surrogate pair for the entity
    }

    @Test
    public void testTranslate_DecimalEntityOutOfRange_ReturnsTranslatedCharacter() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder("&#65536;"); // Decimal entity beyond Unicode range
        StringBuilder output = new StringBuilder();
        int translated = unescaper.translate(input, 0, output);
        Assert.assertEquals(8, translated); // 2 (start tag) + 5 (entity) + 1 (;) + 1 (end tag)
        Assert.assertEquals("\uFFFD", output.toString()); // Replacement character for the entity
    }

}