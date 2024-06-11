package org.apache.commons.codec.language;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class SoundexTest {

    private final String input;
    private final String expectedOutput;

    public SoundexTest(String input, String expectedOutput) {
        this.input = input;
        this.expectedOutput = expectedOutput;
    }

    @Parameterized.Parameters(name = "input[{index}], output[{index}]")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {"apple", "A113"},
                {"application", "A113P000"},
                {"baboon", "B162"},
                {"batch", "B63"},
                {"banana", "B261"},
                {"barrack", "B662"},
                {"beautiful", "B533T5"},
                {"beaver", "B51"},
                {"beauty", "B533"},
                {"business", "B701S5"},
                {"button", "B8197"},
                {"calculator", "K32L71"},
                {"chocolate", "CH632T3"},
                {"computer", "K32P55"},
                {"daffodil", "D161F2"},
                {"desktop", "D260P0"},
                {"dolphin", "D361F"},
                {"elephant", "E224L6"},
                {"engineer", "E364N3"},
                {"express", "E710S"},
                {"fireplace", "F321R5"},
                {"gentleman", "G326T5"},
                {"harbor", "H162B7"},
                {"happiness", "H113P5N5"},
                {"hospital", "H664P5"},
                {"hourglass", "H226R7"},
                {"honey", "H555N1"},
                {"iowa", "I5663"},
                {"jacket", "J161K3"},
                {"jellyfish", "J325L5F5"},
                {"kangaroo", "K361G2R"},
                {"laptop", "L160P0"},
                {"lemonade", "L462M3N3D3"},
                {"library", "L625R5B5"},
                {"lion", "L223N1"},
                {"lighthouse", "L564T5H5"},
                {"love", "L220V"},
                {"mango", "M261NG3"},
                {"mansion", "M361N5S1"},
                {"marmalade", "M462M3R3M3"},
                {"movie", "M663V5"},
                {"november", "N162V53"},
                {"octopus", "O362K6P3"},
                {"orange", "O532R5J3"},
                {"orchid", "O731R2C3D3"},
                {"penguin", "P562N3G2"},
                {"pineapple", "P532N1P1"},
                {"pizza", "P261Z3"},
                {"potato", "P635T2"},
                {"queen", "Q211N1"},
                {"quilt", "Q562L1T1"},
                {"raspberry", "R662P3B5"},
                {"robin", "R121B1"},
                {"rubber", "R322B2B"},
                {"sailboat", "S161L3B1"},
                {"sandwich", "S323D3N3C3"},
                {"seagull", "S262G3L"},
                {"shark", "S462R2K"},
                {"snake", "S462N2K"},
                {"strawberry", "S663R3W3B3"},
                {"sunflower", "S631U2N1F3L3"},
                {"telephone", "T364L2F5"},
                {"thankyou", "T261H2N3K2"},
                {"tiger", "T323G2R"},
                {"tomato", "T635M2T1"},
                {"turkey", "T663R3K3"},
                {"tulip", "T731L2P3"},
                {"university", "U653V1R5I5T1"},
                {"watermelon", "W363T3M2L3N1"},
                {"whale", "W361H2L3"},
                {"wolf", "W662F3L"},
                {"zebra", "Z362B2R2"},
        });
    }

    @Test
    public void testSoundex() throws EncoderException {
        Soundex soundex = new Soundex();
        assertEquals(expectedOutput, soundex.soundex(input));
    }

    @Test(expected = EncoderException.class)
    public void testSoundexWithNullInput() throws EncoderException {
        Soundex soundex = new Soundex();
        soundex.soundex(null);
    }

    @Test
    public void testMapCharacter() throws EncoderException {
        Soundex soundex = new Soundex(Soundex.US_ENGLISH_MAPPING);
        assertEquals('1', soundex.map('A'));
        assertEquals('3', soundex.map('S'));
        assertEquals('0', soundex.map(' '));
        assertEquals('0', soundex.map('X'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMapCharacterInvalid() throws EncoderException {
        Soundex soundex = new Soundex(Soundex.US_ENGLISH_MAPPING);
        soundex.map('Z');
    }

}