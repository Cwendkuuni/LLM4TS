package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.StringWriter;
import java.util.HashMap;

import org.apache.commons.lang3.text.translate.LookupTranslator;
import org.junit.Test;

/**
 * Tests for {@link LookupTranslator} class
 *
 * @author John Doe
 */
public class LookupTranslatorTest {

    @Test
    public void testLookupTranslator_EmptyConstructor() {
        LookupTranslator translator = new LookupTranslator();
        assertEquals(0, translator.getShortest());
        assertEquals(0, translator.getLongest());
        assertNull(translator.lookupMap);
    }

    @Test
    public void testLookupTranslator_ConstructorWithEmptyLookupTable() {
        CharSequence[][] lookup = new CharSequence[0][2];
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals(0, translator.getShortest());
        assertEquals(0, translator.getLongest());
        assertNull(translator.lookupMap);
    }

    @Test
    public void testLookupTranslator_ConstructorWithValidLookupTable() {
        CharSequence[][] lookup = {{"Hello", "Hi"}, {"World", "Earth"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals(5, translator.getShortest());
        assertEquals(5, translator.getLongest());
        HashMap<CharSequence, CharSequence> expectedMap = new HashMap<>();
        expectedMap.put(CharSequence.valueOf("Hello"), CharSequence.valueOf("Hi"));
        expectedMap.put(CharSequence.valueOf("World"), CharSequence.valueOf("Earth"));
        assertEquals(expectedMap, translator.lookupMap);
    }

    @Test(expected = NullPointerException.class)
    public void testLookupTranslator_ConstructorWithNullLookupTable() {
        CharSequence[][] lookup = null;
        new LookupTranslator(lookup);
    }

    @Test
    public void testTranslate_NullInput() {
        CharSequence[][] lookup = {{"Hello", "Hi"}, {"World", "Earth"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertNull(translator.translate(null, 0, new StringWriter()));
    }

    @Test
    public void testTranslate_EmptyInput() {
        CharSequence[][] lookup = {{"Hello", "Hi"}, {"World", "Earth"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals("", translator.translate(CharSequence.EMPTY, 0, new StringWriter()));
    }

    @Test
    public void testTranslate_ShortInputLessThanShortestKey() {
        CharSequence[][] lookup = {{"Hello", "Hi"}, {"World", "Earth"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals("", translator.translate(CharSequence.valueOf("He"), 0, new StringWriter()));
    }

    @Test
    public void testTranslate_ShortInputEqualShortestKey() {
        CharSequence[][] lookup = {{"Hello", "Hi"}, {"World", "Earth"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals("Hi", translator.translate(CharSequence.valueOf("Hello"), 0, new StringWriter()));
    }

    @Test
    public void testTranslate_ShortInputLongerThanShortestKey() {
        CharSequence[][] lookup = {{"Hello", "Hi"}, {"World", "Earth"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals("", translator.translate(CharSequence.valueOf("HelloWorld"), 0, new StringWriter()));
    }

    @Test
    public void testTranslate_InputEqualsShortestKey() {
        CharSequence[][] lookup = {{"Hello", "Hi"}, {"World", "Earth"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals("Hi", translator.translate(CharSequence.valueOf("Hello"), 0, new StringWriter()));
    }

    @Test
    public void testTranslate_InputEqualsKeyInLookupTable() {
        CharSequence[][] lookup = {{"Hello", "Hi"}, {"World", "Earth"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals("Hi", translator.translate(CharSequence.valueOf("Hello"), 0, new StringWriter()));
    }

    @Test
    public void testTranslate_InputLongerThanLongestKey() {
        CharSequence[][] lookup = {{"Hello", "Hi"}, {"World", "Earth"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals("", translator.translate(CharSequence.valueOf("HelloWorldHello"), 0, new StringWriter()));
    }

    @Test
    public void testTranslate_InputLongerThanLongestKeyButContainsShortestKey() {
        CharSequence[][] lookup = {{"Hello", "Hi"}, {"World", "Earth"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals("Hi", translator.translate(CharSequence.valueOf("HelloWorldHello"), 0, new StringWriter()));
    }

    @Test
    public void testTranslate_InputLongerThanLongestKeyButDoesntContainShortestKey() {
        CharSequence[][] lookup = {{"Hello", "Hi"}, {"World", "Earth"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        assertEquals("", translator.translate(CharSequence.valueOf("HelloWorldHelloX"), 0, new StringWriter()));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testTranslate_IndexOutOfBounds() {
        Charsequence[][] lookup = {{"Hello", "Hi"}, {"World", "Earth"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        translator.translate(CharSequence.valueOf("Hello"), 1, new StringWriter());
    }

    @Test(expected = IOException.class)
    public void testTranslate_IOException() {
        Charsequence[][] lookup = {{"Hello", "Hi"}, {"World", "Earth"}};
        LookupTranslator translator = new LookupTranslator(lookup);
        StringWriter out = new StringWriter();
        translator.translate(CharSequence.valueOf("Hello"), 0, null);
    }

}