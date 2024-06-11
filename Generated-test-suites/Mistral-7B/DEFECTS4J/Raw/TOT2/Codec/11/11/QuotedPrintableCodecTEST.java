package org.apache.commons.codec.net;

import org.apache.commons.codec.BinaryDecoder;
import org.apache.commons.codec.BinaryEncoder;
import org.apache.commons.codec.CharEncoding;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringDecoder;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.binary.StringUtils;
import org.apache.commons.codec.net.QuotedPrintableCodec;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.util.BitSet;

/**
 * Test class for QuotedPrintableCodec
 */
public class QuotedPrintableCodecTest {

 private QuotedPrintableCodec codec;
 private static final byte[] TEST_DATA = {0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09, 0x0A, 0x0B, 0x0C, 0x0D, 0x0E, 0x0F};

 @Before
 public void setUp() {
 codec = new QuotedPrintableCodec();
 }

 /**
 * Test encoding of regular data
 */
 @Test
 public void testEncode() throws EncoderException, UnsupportedEncodingException {
 byte[] expected = QuotedPrintableCodec.encodeQuotedPrintable(QuotedPrintableCodec.PRINTABLE_CHARS, TEST_DATA);
 byte[] actual = codec.encode(TEST_DATA);
 Assert.assertArrayEquals(expected, actual);
 }

 /**
 * Test encoding of data with whitespace
 */
 @Test
 public void testEncodeWithWhitespace() throws EncoderException, UnsupportedEncodingException {
 byte[] expected = new byte[]{'=','0','8','=','0','9','=',' ','0','1','=','0','2','=',' ','0','3','=',' ','0','4','=',' ','0','5','=',' ','0','6','=',' ','0','7','=',' ','0','8','=',' ','0','9','=',' ','0','A'};
 byte[] actual = codec.encode(TEST_DATA);
 Assert.assertArrayEquals(expected, actual);
 }

 /**
 * Test decoding of regular data
 */
 @Test
 public void testDecode() throws DecoderException, UnsupportedEncodingException {
 byte[] expected = TEST_DATA;
 byte[] actual = QuotedPrintableCodec.decodeQuotedPrintable(QuotedPrintableCodec.encodeQuotedPrintable(QuotedPrintableCodec.PRINTABLE_CHARS, TEST_DATA));
 Assert.assertArrayEquals(expected, actual);
 }

 /**
 * Test decoding of data with whitespace
 */
 @Test
 public void testDecodeWithWhitespace() throws DecoderException, UnsupportedEncodingException {
 byte[] expected = TEST_DATA;
 byte[] actual = codec.decode(StringUtils.getBytesUsAscii("=08=09 =01=02=03=04=05=06=07=08=09 =0A"));
 Assert.assertArrayEquals(expected, actual);
 }
}