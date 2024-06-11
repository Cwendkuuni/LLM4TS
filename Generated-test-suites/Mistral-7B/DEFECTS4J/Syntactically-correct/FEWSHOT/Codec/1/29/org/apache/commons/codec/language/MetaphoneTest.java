package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import org.junit.Test;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        String input = "apple";
        String expected = "A0P0L0E";
        String result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "banana";
        expected = "B2N1A0N1A0";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "orange";
        expected = "O1R1A0N1G1E";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "grape";
        expected = "G1R1A0P1E";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "elephant";
        expected = "E1L1P1H1A0T";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "rhubarb";
        expected = "R1H1U1B1A0R1B";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "sherbet";
        expected = "S1H1E0R1B1E0T";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "caramel";
        expected = "K1R1A0M1E1L";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "pineapple";
        expected = "P1N1E1A0P1L1E";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "quicksand";
        expected = "K1W1I1C1K1S1A0N1D";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "octopus";
        expected = "O1T1O1P1U1S";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "aluminum";
        expected = "A1L1U1M1I1N1U1M";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "cinnamon";
        expected = "K1N1I1N1A0M1O1N";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "ugly";
        expected = "U1G1L1Y";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "beautiful";
        expected = "B1A1U1T1F1U1L";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "mosquito";
        expected = "M1O1S1Q1I1T1O";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "pneumonia";
        expected = "P1N1U1M1O1N1A";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "asparagus";
        expected = "A1S1P1R1A1G1U1S";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "spaghetti";
        expected = "S1P1A1G1H1E1T1I";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "pistachio";
        expected = "P1S1T1A1S1H1I0";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "watermelon";
        expected = "W1T1A1R1M1E1L1O1N";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "mango";
        expected = "M1A1N1G1O";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "kiwi";
        expected = "K1I1W1I";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "pomegranate";
        expected = "P1O1M1E1G1R1A1N1A1T";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "blueberry";
        expected = "B1U1L1E1B1R1Y";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "strawberry";
        expected = "S1T1R1A1W1B1E1R1Y";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "cranberry";
        expected = "K1R1A1N1B1E1R1Y";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "raspberry";
        expected = "R1A1S1P1B1E1R1Y";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "blackberry";
        expected = "B1A1L1K1B1E1R1Y";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "strawberries";
        expected = "S1T1R1A1W1B1E1R1Y1S";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "raspberries";
        expected = "R1A1S1P1B1E1R1Y1S";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "blackberries";
        expected = "B1A1L1K1B1E1R1Y1S";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "raspberry pie";
        expected = "R1A1S1P1B1E1R1Y1P1E1";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "strawberry pie";
        expected = "S1T1R1A1W1B1E1R1Y1P1E1";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "blueberry pie";
        expected = "B1U1L1E1B1R1Y1P1E1";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "pomegranate juice";
        expected = "P1O1M1E1G1R1A1N1A1T1J1U1C1E1";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "mango juice";
        expected = "M1A1N1G1O1J1U1C1E1";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "kiwi juice";
        expected = "K1I1W1I1J1U1C1E1";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "watermelon juice";
        expected = "W1T1A1R1M1E1L1O1N1J1U1C1E1";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "cranberry juice";
        expected = "K1R1A1N1B1E1R1Y1J1U1C1E1";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "strawberry shortcake";
        expected = "S1T1R1A1W1B1E1R1Y1S1T1O1R1C1A1K1E";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "raspberry shortcake";
        expected = "R1A1S1P1B1E1R1Y1S1T1O1R1C1A1K1E";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "blackberry shortcake";
        expected = "B1A1L1K1B1E1R1Y1S1T1O1R1C1A1K1E";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "pineapple upside down cake";
        expected = "P1N1E1A0P1L1E1U1P1S1I1D1K1A1K1E";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "apple pie a la mode";
        expected = "A1P1E1P1A1L1A1M1O1D1";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "banana split";
        expected = "B1N1A1N1A0S1P1L1I1T";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "cherry on top";
        expected = "C1H1E1R1R1O1N1T1P";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "ice cream sundae";
        expected = "I1S1C1E1C1R1E1S1U1N1D1E";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "hot fudge sundae";
        expected = "H1O1T1F1U1J1D1S1U1N1D1E";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);

        input = "waffle cone";
        expected = "W1A1F1F1L1C1O1N";
        result = metaphone.metaphone(input);
        assertEquals(expected, result);
    }

    @Test
    public void testEncodeAndDecode() {
        Metaphone metaphone = new Metaphone();
        String input = "apple";
        String encoded = metaphone.encode(input);
        String decoded = metaphone.decode(encoded);
        assertEquals(input, decoded);
    }

    @Test(expected = EncoderException.class)
    public void testEncodeInvalidType() {
        Metaphone metaphone = new Metaphone();
        Object invalidInput = new Object();
        metaphone.encode(invalidInput);
    }
}