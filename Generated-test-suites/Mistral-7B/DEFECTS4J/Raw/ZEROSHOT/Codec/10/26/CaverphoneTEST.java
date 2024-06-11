package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.Assert.*;

import static org.apache.commons.codec.language.Caverphone.*;

public class CaverphoneTest {

    @Test
    public void testEmptyString() {
        String result = caverphone("");
        assertEquals("1111111111", result);
    }

    @Test
    public void testNullString() {
        try {
            caverphone(null);
            fail("expected EncoderException");
        } catch (EncoderException e) {
            // expected
        }
    }

    @Test
    public void testLowerCase() {
        String result = caverphone("HelloWorld");
        assertEquals("He2ol2oW2or2ld", result);
    }

    @Test
    public void testRemoveNonLetters() {
        String result = caverphone("123HelloWorld456");
        assertEquals("He2ol2oW2or2ld", result);
    }

    @Test
    public void testRemoveFinalE() {
        String result = caverphone("caverphone2.0");
        assertEquals("caverpo2f2.0", result);
    }

    @Test
    public void testHandlesStartOptions() {
        String result = caverphone("enough");
        assertEquals("enou2f", result);

        result = caverphone("trough");
        assertEquals("trou2f", result);

        result = caverphone("gn");
        assertEquals("2n", result);

        result = caverphone("mb");
        assertEquals("m2", result);
    }

    @Test
    public void testHandlesReplacements() {
        String result = caverphone("cq");
        assertEquals("2q", result);

        result = caverphone("ci");
        assertEquals("si", result);

        result = caverphone("ce");
        assertEquals("se", result);

        result = caverphone("cy");
        assertEquals("sy", result);

        result = caverphone("tch");
        assertEquals("2ch", result);

        result = caverphone("c");
        assertEquals("k", result);

        result = caverphone("q");
        assertEquals("k", result);

        result = caverphone("x");
        assertEquals("k", result);

        result = caverphone("v");
        assertEquals("f", result);

        result = caverphone("dg");
        assertEquals("2g", result);

        result = caverphone("tio");
        assertEquals("sio", result);

        result = caverphone("tia");
        assertEquals("sia", result);

        result = caverphone("d");
        assertEquals("t", result);

        result = caverphone("ph");
        assertEquals("fh", result);

        result = caverphone("b");
        assertEquals("p", result);

        result = caverphone("sh");
        assertEquals("s2", result);

        result = caverphone("z");
        assertEquals("s", result);

        result = caverphone("w3");
        assertEquals("W3", result);

        result = caverphone("w$");
        assertEquals("3", result);

        result = caverphone("why");
        // unsupported in Caverphone 2.0, but is in 1.0
        //assertEquals("Why", result);

        result = caverphone("wh3");
        assertEquals("Wh3", result);

        result = caverphone("w");
        assertEquals("2", result);

        result = caverphone("h");
        assertEquals("A", result);

        result = caverphone("r3");
        assertEquals("R3", result);

        result = caverphone("r$");
        assertEquals("3", result);

        result = caverphone("ry");
        // unsupported in Caverphone 2.0, but is in 1.0
        //assertEquals("Ry", result);

        result = caverphone("r");
        assertEquals("2", result);

        result = caverphone("l3");
        assertEquals("L3", result);

        result = caverphone("l$");
        assertEquals("3", result);

        result = caverphone("ly");
        // unsupported in Caverphone 2.0, but is in 1.0
        //assertEquals("Ly", result);

        result = caverphone("l");
        assertEquals("2", result);

        result = caverphone("y3");
        // unsupported in Caverphone 2.0, but is in 1.0
        //assertEquals("Y3", result);

        result = caverphone("y");
        // unsupported in Caverphone 2.0, but is in 1.0
        //assertEquals("2", result);
    }

    @Test
    public void testHandlesRemovals() {
        String result = caverphone("2");
        assertEquals("", result);

        result = caverphone("3$");
        assertEquals("A", result);

        result = caverphone("3");
        assertEquals("", result);
    }

    @Test
    public void testAppend1111111111() {
        String result = caverphone("HelloWorld");
        assertEquals("He2ol2oW2or2ld1111111111", result);
    }

    @Test
    public void testTakeFirstTenCharacters() {
        String result = caverphone("HelloWorld");
        assertEquals("He2ol2oW2or2ld", result.substring(0, 10));
    }

    @Test
    public void testEncodeObject() throws EncoderException {
        String result = (String) encode("HelloWorld");
        assertEquals("He2ol2oW2or2ld", result);
    }

    @Test
    public void testIsCaverphoneEqual() {
        assertTrue(isCaverphoneEqual("HelloWorld", "HelloWorld"));
        assertFalse(isCaverphoneEqual("HelloWorld", "WorldHello"));
    }

}