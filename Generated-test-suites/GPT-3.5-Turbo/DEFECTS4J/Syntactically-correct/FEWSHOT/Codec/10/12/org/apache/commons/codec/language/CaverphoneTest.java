
package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CaverphoneTest {
    private StringEncoder encoder;

    @Before
    public void setUp() {
        encoder = new Caverphone();
    }

    @Test
    public void testCaverphone() {
        assertEquals("1111111111", encoder.encode(null));
        assertEquals("c11111", encoder.encode("cough"));
        assertEquals("r11111", encoder.encode("rough"));
        assertEquals("t11111", encoder.encode("tough"));
        assertEquals("e111111111", encoder.encode("enough"));
        assertEquals("t11111", encoder.encode("trough"));
        assertEquals("en2n", encoder.encode("en"));
        assertEquals("cm2x", encoder.encode("cmx"));
        assertEquals("ci", encoder.encode("ci"));
        assertEquals("se", encoder.encode("se"));
        assertEquals("sy", encoder.encode("sy"));
        assertEquals("2ch", encoder.encode("tch"));
        assertEquals("k", encoder.encode("c"));
        assertEquals("k", encoder.encode("q"));
        assertEquals("k", encoder.encode("x"));
        assertEquals("f", encoder.encode("v"));
        assertEquals("2g", encoder.encode("dg"));
        assertEquals("sio", encoder.encode("tio"));
        assertEquals("sia1", encoder.encode("tia"));
        assertEquals("t", encoder.encode("d"));
        assertEquals("fh", encoder.encode("ph"));
        assertEquals("p", encoder.encode("b"));
        assertEquals("s2", encoder.encode("sh"));
        assertEquals("s", encoder.encode("z"));
        assertEquals("A", encoder.encode("a"));
        assertEquals("3", encoder.encode("e"));
        assertEquals("y", encoder.encode("j"));
        assertEquals("Y3", encoder.encode("y3"));
        assertEquals("A", encoder.encode("y"));
        assertEquals("3gh3", encoder.encode("3gh3"));
        assertEquals("22", encoder.encode("gh"));
        assertEquals("k", encoder.encode("g"));
        assertEquals("S", encoder.encode("s"));
        assertEquals("T", encoder.encode("t"));
        assertEquals("P", encoder.encode("p"));
        assertEquals("K", encoder.encode("k"));
        assertEquals("F", encoder.encode("f"));
        assertEquals("M", encoder.encode("m"));
        assertEquals("N", encoder.encode("n"));
        assertEquals("W3", encoder.encode("w3"));
        assertEquals("Wh3", encoder.encode("wh3"));
        assertEquals("K", encoder.encode("w"));
        assertEquals("A", encoder.encode("h"));
        assertEquals("2", encoder.encode("r"));
        assertEquals("L3", encoder.encode("l3"));
        assertEquals("2", encoder.encode("l"));
        assertEquals("A", encoder.encode("j"));
        assertEquals("A", encoder.encode("y3"));
        assertEquals("3kh3", encoder.encode("3gh3"));
        assertEquals("2", encoder.encode("y"));
        assertEquals("1111111111", encoder.decode("c[m1"));
        assertEquals("1111111111", encoder.decode(null));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeWithNonStringObject() throws EncoderException {
        encoder.encode(123);
    }

    @Test
    public void testEncodeWithStringObject() {
        String encoded = encoder.encode((String) "test");
        assertEquals("t111111111", encoded);
    }

    @Test
    public void testIsCaverphoneEqual() {
        assertEquals(true, encoder.isCaverphoneEqual("foo", "foo"));
        assertEquals(false, encoder.isCaverphoneEqual("foo", "bar"));
    }
}
