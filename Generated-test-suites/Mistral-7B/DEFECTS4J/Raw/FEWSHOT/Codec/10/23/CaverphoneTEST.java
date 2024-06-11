package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphoneEncodesEmptyString() {
        assertEquals("1111111111", new Caverphone().caverphone(""));
    }

    @Test
    public void testCaverphoneEncodesLowerCaseString() {
        assertEquals("kfkfkfkfkfkfkfttkfttkfttkfttkf", new Caverphone().caverphone("hello world"));
    }

    @Test
    public void testCaverphoneHandlesStartOptions() {
        assertEquals("cou2f", new Caverphone().caverphone("cough"));
        assertEquals("rou2f", new Caverphone().caverphone("rough"));
        assertEquals("tou2f", new Caverphone().caverphone("tough"));
        assertEquals("enou2f", new Caverphone().caverphone("enough"));
        assertEquals("trou2f", new Caverphone().caverphone("trough")); // 2.0 only
        assertEquals("2n", new Caverphone().caverphone("gn"));
    }

    @Test
    public void testCaverphoneHandlesReplacements() {
        assertEquals("2q", new Caverphone().caverphone("cq"));
        assertEquals("si", new Caverphone().caverphone("ci"));
        assertEquals("se", new Caverphone().caverphone("ce"));
        assertEquals("sy", new Caverphone().caverphone("cy"));
        assertEquals("2ch", new Caverphone().caverphone("tch"));
        assertEquals("k", new Caverphone().caverphone("c"));
        assertEquals("k", new Caverphone().caverphone("q"));
        assertEquals("k", new Caverphone().caverphone("x"));
        assertEquals("f", new Caverphone().caverphone("v"));
        assertEquals("2g", new Caverphone().caverphone("dg"));
        assertEquals("sio", new Caverphone().caverphone("tio"));
        assertEquals("sia", new Caverphone().caverphone("tia"));
        assertEquals("t", new Caverphone().caverphone("d"));
        assertEquals("fh", new Caverphone().caverphone("ph"));
        assertEquals("p", new Caverphone().caverphone("b"));
        assertEquals("s2", new Caverphone().caverphone("sh"));
        assertEquals("s", new Caverphone().caverphone("z"));
        assertEquals("A", new Caverphone().caverphone("^[aeiou]"));
        assertEquals("3", new Caverphone().caverphone("[aeiou]"));
        //... add tests for 2.0 only replacements here
    }

    @Test
    public void testCaverphoneHandlesRemovalsAndAdditions() {
        assertEquals("", new Caverphone().caverphone("2"));
        assertEquals("A", new Caverphone().caverphone("3$"));
        assertEquals("", new Caverphone().caverphone("3"));
    }

    @Test
    public void testCaverphoneWithEncoderException() {
        String input = new StringBuffer("Hello").append(" World").toString();
        try {
            new Caverphone().encode(123);
        } catch (EncoderException e) {
            assertEquals("Parameter supplied to Caverphone encode is not of type java.lang.String", e.getMessage());
        }
        assertEquals("kfkfkfkfkfkfkfttkfttkfttkfttkf", new Caverphone().caverphone(input));
    }

    @Test
    public void testCaverphoneHandlesDifferentStrings() {
        assertNotEquals(new Caverphone().caverphone("hello world"), new Caverphone().caverphone("world hello"));
    }

}