package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateWithValidEntities() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();

        String input = "&amp;&nbsp;&#10;&#13;&#32;&#xA0;&#x201C;&#x201D;&#x2018;&#x2019;&#x2212;&#x2211;&#x2603;&#x2605;&#x260E;&#x2611;&#x2660;&#x2661;&#x2663;&#x2664;&#x2665;&#x2666;&#xFF01;&#xFF02;&#xFFFD;&#x1F600;&#x1F601;&#x1F602;&#x1F603;&#x1F604;&#x1F647;&#x1F648;";
        int len = unescaper.translate(new CharSequence() {
            @Override
            public CharSequence subSequence(int start, int end) {
                return null;
            }

            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }
        }, 0, writer);

        String output = writer.getBuffer().toString();
        assertEquals("&amp;&nbsp;&#x000A;&#x000D;&#x0020;&#xA0;&#x201C;&#x201D;&#x2018;&#x2019;&#x2212;&#x2211;&#x2603;&#x2605;&#x260E;&#x2611;&#x2660;&#x2661;&#x2663;&#x2664;&#x2665;&#x2666;&#xFF01;&#xFF02;&#xFFFD;&#x1F600;&#x1F601;&#x1F602;&#x1F603;&#x1F604;&#x1F647;&#x1F648;", input);
        assertEquals("\u0001\u0002\r\n \u00A0“”‘’−±∩∂µ∫∯−⊕⊗⊘⊙⌈⌋⌐⌒⌓▼⌔⌕⌝⌞⌟⌠⌡⌢⌣⌤⌥⌦⌧⌨〈〉⌫⌬⌭⌮⌯⌰⌱⌲⌳⌴⌵⌶⌷⌸⌹⌺⌻⌼⌽⌾⌿Šāāáăąęèéěěìíįīïłłńńôóöőůùúůüųūÿẏẏ͊ẙẙ͋ẍẍ͊ẋẋ͋ḧḧ͊ḯḯ͋ḿḿ͋ṀṀ͇Ṁ̛ṄṄ͇Ṅ̛ṆṆ͇Ṇ̛ṈṈ͇Ṉ̛ṉṉ͇ṉ̛Ṋṋṋ͇ṋ̛ṋ̄ṋ͇̄Ṍṍṍ͇ỡ́Ṏṏṏ͇ỡ̈ṑṑ͇ơ̄́ṓ̄ṓ͇̄ṕṕ͇ṕ̛ṕ̄ṕ͇̄Ṗṗṗ͇ṗ̛ṙṙ͇ṙ̛ṛṛ͇ṛ̛ṝṞṞ͇ṝ̛ṡṡ͇ṡ̛ṡ̄ṡ͇̄Ṡṡṡ͇Ṡ̛ṢṢ͇Ṣ̛ṣṣ͇ṣ̛Ṥṥṥ͇ṥ̛Ṧṧṧ͇ṧ̛ṩṩ͇ṩ̛ṫṫ͇ṫ̛Ṭṭṭ͇ṭ̛Ṯṯṯ͇ṯ̛Ṱṱ͇ṱ̛ṱ̄ṱ͇̄Ṳṳṳ͇ư̤ṵṵ͇ư̰Ṷṷṷ͇ư̭Ṹṹṹ͇ữ́Ṻṻṻ͇ư̄̈Ṽṽṽ͇ṽ̛ṾṾ͇Ṿ̛ṿṿ͇ṿ̛⌈⌉⌊⌋⌌⌍⌎⌏⌭⌮⌯⌰⌱⌲⌳⌴⌵⌶⌷⌸⌹⌺⌻⌼⌽⌾⌿¼".toCharArray(), output.toCharArray(), input.length());

        assertEquals(input.length(), len);
    }

    @Test
    public void testTranslateWithInvalidEntity() throws Exception {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        Writer writer = new StringWriter();

        String input = "&%&#";
        int len = unescaper.translate(new CharSequence() {
            @Override
            public CharSequence subSequence(int start, int end) {
                return null;
            }

            @Override
            public int length() {
                return input.length();
            }

            @Override
            public char charAt(int index) {
                return input.charAt(index);
            }
        }, 0, writer);

        String output = writer.getBuffer().toString();
        assertFalse(output.isEmpty());
        assertTrue(output.contains("FAIL:"));
        assertEquals(0, len);
    }
}