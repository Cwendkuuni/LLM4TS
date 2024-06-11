package org.apache.commons.lang3.text.translate;

import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Test class for LookupTranslator
 */
public class LookupTranslatorTest {

    /**
     * Test method for {@link LookupTranslator#LookupTranslator(CharSequence[]...)}
     */
    @Test
    public void testConstructor() {
        CharSequence[][] lookup = new CharSequence[][]{{"Hello", "Hi"}, {"World", "Earth"}, {"Java", "Python"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        Map<String, String> expectedMap = new HashMap<>();
        expectedMap.put("Hello", "Hi");
        expectedMap.put("World", "Earth");
        expectedMap.put("Java", "Python");
        Assert.assertEquals(expectedMap, translator.lookupMap);
        Assert.assertEquals(3, translator.shortest);
        Assert.assertEquals(6, translator.longest);
    }

    /**
     * Test method for {@link LookupTranslator#translate(CharSequence, int, Writer)}
     * with valid input and key in the lookup map
     */
    @Test
    public void testTranslateValidInputWithKeyInLookupMap() throws UnsupportedEncodingException, IOException {
        CharSequence[][] lookup = new CharSequence[][]{{"Hello", "Hi"}, {"World", "Earth"}, {"Java", "Python"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        CharSequence input = "Hello World Java";
        translator.translate(input, 0, out);
        String expectedOutput = "Hi Earth Python";
        Assert.assertEquals(expectedOutput, out.toString());
    }

    /**
     * Test method for {@link LookupTranslator#translate(CharSequence, int, Writer)}
     * with valid input and key not in the lookup map
     */
    @Test
    public void testTranslateValidInputWithKeyNotInLookupMap() throws UnsupportedEncodingException, IOException {
        CharSequence[][] lookup = new CharSequence[][]{{"Hello", "Hi"}, {"World", "Earth"}, {"Java", "Python"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        CharSequence input = "Apache Commons";
        int result = translator.translate(input, 0, out);
        Assert.assertEquals(0, result);
        String expectedOutput = "";
        Assert.assertEquals(expectedOutput, out.toString());
    }

    /**
     * Test method for {@link LookupTranslator#translate(CharSequence, int, Writer)}
     * with empty input
     */
    @Test
    public void testTranslateEmptyInput() throws UnsupportedEncodingException, IOException {
        CharSequence[][] lookup = new CharSequence[][]{{"Hello", "Hi"}, {"World", "Earth"}, {"Java", "Python"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        CharSequence input = "";
        int result = translator.translate(input, 0, out);
        Assert.assertEquals(0, result);
        String expectedOutput = "";
        Assert.assertEquals(expectedOutput, out.toString());
    }

    /**
     * Test method for {@link LookupTranslator#translate(CharSequence, int, Writer)}
     * with input containing more characters than the longest key in the lookup map
     */
    @Test
    public void testTranslateInputWithMoreCharactersThanLongestKey() throws UnsupportedEncodingException, IOException {
        CharSequence[][] lookup = new CharSequence[][]{{"Hello", "Hi"}, {"World", "Earth"}, {"Java", "Python"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        CharSequence input = "Hello World Java Hello World Java";
        int result = translator.translate(input, 0, out);
        String expectedOutput = "Hi Earth Python Hi Earth Python";
        Assert.assertEquals(expectedOutput, out.toString());
        Assert.assertEquals(8, result);
    }

    /**
     * Test method for {@link LookupTranslator#translate(CharSequence, int, Writer)}
     * with input containing a key that overlaps with another key in the lookup map
     */
    @Test
    public void testTranslateInputWithOverlappingKeys() throws UnsupportedEncodingException, IOException {
        CharSequence[][] lookup = new CharSequence[][]{{"Hello", "Hi"}, {"World", "Earth"}, {"Java", "Python Java"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        CharSequence input = "Java Java";
        int result = translator.translate(input, 0, out);
        String expectedOutput = "Python Python";
        Assert.assertEquals(expectedOutput, out.toString());
        Assert.assertEquals(4, result);
    }
}