
package org.apache.commons.codec.net;

import org.junit.Assert;
import org.junit.Test;

import java.io.UnsupportedEncodingException;

public class QuotedPrintableCodecTest {

    @Test
    public void encodeDecodeTest() throws UnsupportedEncodingException {
        String original = "Hello World!";

        QuotedPrintableCodec codec = new QuotedPrintableCodec();

        byte[] encodedBytes = codec.encode(original.getBytes());
        String encodedString = new String(encodedBytes, QuotedPrintableCodec.DEFAULT_CHARSET);

        String decodedString = codec.decode(encodedString);

        Assert.assertEquals(original, decodedString);
    }

}
