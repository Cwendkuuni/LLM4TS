package org.apache.commons.codec.net;

import static org.junit.Assert.*;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.net.QuotedPrintableCodec;

import java.nio.charset.StandardCharsets;
import java.util.BitSet;

public class QuotedPrintableCodecTest {

    private QuotedPrintableCodec codec = new QuotedPrintableCodec();

    @Test
    public void testEncodeDecodeByteArray() throws Exception {
        byte[] bytes = {0x61, 0x62, 0x63};
        byte[] encoded = codec.encode(bytes);
        byte[] decoded = codec.decode(encoded);
        assertArrayEquals(bytes, decoded);
    }

    @Test
    public void testEncodeDecodeString() throws Exception {
        String str = "abc";
        String encoded = codec.encode(str, StandardCharsets.UTF_8);
        String decoded = codec.decode(encoded, StandardCharsets.UTF_8);
        assertEquals(str, decoded);
    }

    @Test
    public void testEncodeDecodeStringWithDefaultCharset() throws Exception {
        String str = "abc";
        String encoded = codec.encode(str);
        String decoded = codec.decode(encoded);
        assertEquals(str, decoded);
    }

    @Test
    public void testEncodeDecodeObject() throws Exception {
        String str = "abc";
        Object encodedObj = codec.encode(str);
        Object decodedObj = codec.decode(encodedObj);
        assertEquals(str, decodedObj);
    }

    @Test
    public void testEncodeDecodeObjectWithByteArray() throws Exception {
        byte[] bytes = {0x61, 0x62, 0x63};
        Object encodedObj = codec.encode(bytes);
        Object decodedObj = codec.decode(encodedObj);
        assertArrayEquals(bytes, ((byte[]) decodedObj));
    }

    @Test
    public void testEncodeDecodeObjectWithString() throws Exception {
        String str = "abc";
        Object encodedObj = codec.encode(str);
        Object decodedObj = codec.decode(encodedObj.toString());
        assertEquals(str, decodedObj);
    }

    @Test
    public void testEncodeDecodeCharArray() throws Exception {
        char[] chars = {'a', 'b', 'c'};
        byte[] encoded = codec.encode(chars);
        byte[] decoded = codec.decode(encoded);
        assertArrayEquals(chars, StringUtils.toCharArray(new String(decoded, StandardCharsets.UTF_8)));
    }

    @Test
    public void testEncodeDecodeBitSet() throws Exception {
        BitSet printable = new BitSet(256);
        for (int i = 33; i <= 126; i++) {
            printable.set(i);
        }
        printable.set(9);
        printable.set(32);

        byte[] encoded = codec.encodeQuotedPrintable(printable, new byte[] {});
        BitSet decoded = new BitSet(256);
        decoded = codec.decodeQuotedPrintable(encoded);
        assertTrue(printable.equals(decoded));
    }

    @Test
    public void testEncodeDecodeBitSetWithDefaultCharset() throws Exception {
        BitSet printable = new BitSet(256);
        for (int i = 33; i <= 126; i++) {
            printable.set(i);
        }
        printable.set(9);
        printable.set(32);

        byte[] encoded = codec.encodeQuotedPrintable(printable);
        BitSet decoded = codec.decodeQuotedPrintable(encoded);
        assertTrue(printable.equals(decoded));
    }

    @Test(expected = DecoderException.class)
    public void testDecodeInvalidInput() throws Exception {
        byte[] bytes = {0x61};
        codec.decodeQuotedPrintable(bytes);
    }
}