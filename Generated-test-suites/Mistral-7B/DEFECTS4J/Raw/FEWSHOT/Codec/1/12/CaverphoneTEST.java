package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.StringUtils;
import org.junit.Test;

public class CaverphoneTest {

    private static final String TEST_DATA_1 = "This is a test string.";
    private static final String TEST_DATA_2 = "Caverphone is a cool algorithm.";
    private static final String TEST_DATA_3 = "";
    private static final String TEST_DATA_4 = null;

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();

        String testData1Caverphone = caverphone.caverphone(TEST_DATA_1);
        String testData2Caverphone = caverphone.caverphone(TEST_DATA_2);

        assertEquals("12131331122331112111112133222111111111", testData1Caverphone);
        assertEquals("11231323121212122311132111232111121111", testData2Caverphone);
    }

    @Test
    public void testEncode() throws EncoderException {
        Caverphone caverphone = new Caverphone();

        String testData1 = "This is a test string.";
        String testData1Caverphone = caverphone.caverphone(testData1);

        assertEquals(testData1Caverphone, caverphone.encode(testData1));
    }

    @Test(expected = EncoderException.class)
    public void testEncodeInvalidDataType() throws EncoderException {
        Caverphone caverphone = new Caverphone();

        Integer testData1 = 42;
        caverphone.encode(testData1);
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();

        String testData1 = "This is a test string.";
        String testData2 = "Caverphone is a cool algorithm.";
        String testData1Caverphone = caverphone.caverphone(testData1);
        String testData2Caverphone = caverphone.caverphone(testData2);

        assertFalse(caverphone.isCaverphoneEqual(testData1, testData2));

        String testData3 = "This is a test string.";
        String testData4 = testData3;

        assertTrue(caverphone.isCaverphoneEqual(testData3, testData4));
    }

    @Test
    public void testEmptyString() {
        Caverphone caverphone = new Caverphone();

        assertEquals("1111111111", caverphone.caverphone(StringUtils.EMPTY));
    }

    @Test
    public void testNull() {
        Caverphone caverphone = new Caverphone();

        assertNull(caverphone.caverphone(null));
    }
}