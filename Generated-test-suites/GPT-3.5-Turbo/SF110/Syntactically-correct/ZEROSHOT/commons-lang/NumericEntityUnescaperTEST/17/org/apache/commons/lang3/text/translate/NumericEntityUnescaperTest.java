import org.junit.*;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {

@Test
public void testIsSet() {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
    assertEquals(true, unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
}

@Test
public void testTranslate() {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
    StringBuilder input = new StringBuilder("&#65;");
    StringBuilder output = new StringBuilder();
    try {
        unescaper.translate(input, 0, output);
        assertEquals("A", output.toString());
    } catch (Exception e) {
        fail("Exception should not be thrown");
    }
}

@Test
public void testTranslateWithSemiColonRequired() {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
    StringBuilder input = new StringBuilder("&#65");
    StringBuilder output = new StringBuilder();
    try {
        unescaper.translate(input, 0, output);
        fail("Exception should be thrown");
    } catch (IllegalArgumentException e) {
        assertEquals("Semi-colon required at end of numeric entity", e.getMessage());
    } catch (Exception e) {
        fail("IllegalArgumentException should be thrown");
    }
}

@Test
public void testTranslateWithSemiColonOptional() {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
    StringBuilder input = new StringBuilder("&#65");
    StringBuilder output = new StringBuilder();
    try {
        unescaper.translate(input, 0, output);
        assertEquals("A", output.toString());
    } catch (Exception e) {
        fail("Exception should not be thrown");
    }
}

@Test
public void testTranslateWithHex() {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
    StringBuilder input = new StringBuilder("&#x41;");
    StringBuilder output = new StringBuilder();
    try {
        unescaper.translate(input, 0, output);
        assertEquals("A", output.toString());
    } catch (Exception e) {
        fail("Exception should not be thrown");
    }
}

}