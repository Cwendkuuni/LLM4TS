package org.apache.commons.codec.language.test;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.apache.commons.codec.language.Metaphone;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for Metaphone class.
 */
public class MetaphoneTest {

    /**
     * Test for metaphone encoding of correct strings.
     */
    @Test
    public void testEncodeCorrect() throws EncoderException {
        Metaphone metaphone = new Metaphone();
        String expected[] = { "BAKE", "BAG", "BARRACK", "BARRACUD", "BARRINGTON", "BASS", "BATH", "BATE", "BATON",
                "BEACH", "BEACHING", "BEAR", "BEAUTY", "BECAUSE", "BEET", "BEG", "BELLA", "BEND", "BENEDICT", "BENEDICTAR",
                "BENEDICK", "BERATE", "BEST", "BETTY", "BEYOND", "BICYCLE", "BILL", "BIOLOGY", "BIRCH", "BIRD", "BITTER",
                "BITTERLY", "BLAND", "BLARE", "BLEND", "BLINK", "BLOCK", "BLOOM", "BLOOMS", "BLUE", "BLUFF", "BOARD",
                "BOARDS", "BODY", "BONE", "BOUNCE", "BOUNTY", "BOW", "BOWELS", "BOWL", "BOWMAN", "BOY", "BRADY", "BRAIN",
                "BRAINIAC", "BRAINY", "BRAMBLE", "BRAND", "BRAVE", "BRIGHT", "BRIEF", "BRIEFING", "BRING", "BROAD",
                "BROADCAST", "BROCCOLI", "BROKEN", "BROKER", "BROOM", "BROOMS", "BROWN", "BRUCE", "BRUNCH", "BRUNET",
                "BRUNETTE", "BRUSH", "BRUSHING", "BUCKET", "BUCKETS", "BUD", "BUDDY", "BUFFALO", "BUG", "BUILD", "BUILDER",
                "BULLET", "BULLETIN", "BUNNY", "BURGLAR", "BURNT", "BURY", "BUS", "BUSINESS", "BUSINESSMAN", "BUT", "BUTTER",
                "BUTTERFLY", "BUTTERS", "BUTTERY", "BUTTON", "BUTTONS", "BUTTERCREAM", "BUTTONHOLE", "BUY", "BYE" };

        for (String str : expected) {
            String encoded = metaphone.encode(str);
            Assert.assertEquals(str, encoded);
        }
    }

    /**
     * Test for metaphone encoding of incorrect strings.
     */
    @Test(expected = EncoderException.class)
    public void testEncodeIncorrect1() throws EncoderException {
        Metaphone metaphone = new Metaphone();
        metaphone.encode(null);
    }

    /**
     * Test for metaphone encoding of incorrect strings.
     */
    @Test(expected = EncoderException.class)
    public void testEncodeIncorrect2() throws EncoderException {
        Metaphone metaphone = new Metaphone();
        metaphone.encode("");
    }

    /**
     * Test for metaphone encoding of incorrect strings.
     */
    @Test(expected = EncoderException.class)
    public void testEncodeIncorrect3() throws EncoderException {
        Metaphone metaphone = new Metaphone();
        metaphone.encode("12345");
    }

    /**
     * Test for metaphone encoding of incorrect strings.
     */
    @Test(expected = EncoderException.class)
    public void testEncodeIncorrect4() throws EncoderException {
        Metaphone metaphone = new Metaphone();
        metaphone.encode("ABCDEFGHIJKLMNOPQRSTUVWXYZ");
    }

    /**
     * Test for metaphone encoding and decoding.
     */
    @Test
    public void testEncodeDecode() throws EncoderException, DecoderException {
        Metaphone metaphone = new Metaphone();
        String encoded = metaphone.encode("BARRACK");
        String decoded = (String) metaphone.decode(encoded);
        Assert.assertEquals("BARRACK", decoded);
    }

    /**
     * Test for metaphone encoding and decoding with incorrect inputs.
     */
    @Test(expected = DecoderException.class)
    public void testEncodeDecodeIncorrect() throws EncoderException, DecoderException {
        Metaphone metaphone = new Metaphone();
        String encoded = metaphone.encode("BARRACK");
        String decoded = (String) metaphone.decode(null);
    }

    /**
     * Test for metaphone encoding and decoding with incorrect inputs.
     */
    @Test(expected = DecoderException.class)
    public void testEncodeDecodeIncorrect2() throws EncoderException, DecoderException {
        Metaphone metaphone = new Metaphone();
        String encoded = metaphone.encode("BARRACK");
        String decoded = (String) metaphone.decode("");
    }

    /**
     * Test for metaphone encoding and decoding with incorrect inputs.
     */
    @Test(expected = DecoderException.class)
    public void testEncodeDecodeIncorrect3() throws EncoderException, DecoderException {
        Metaphone metaphone = new Metaphone();
        String encoded = metaphone.encode("BARRACK");
        String decoded = (String) metaphone.decode("12345");
    }

    /**
     * Test for metaphone encoding and decoding with incorrect inputs.
     */
    @Test(expected = DecoderException.class)
    public void testEncodeDecodeIncorrect4() throws EncoderException, DecoderException {
        Metaphone metaphone = new Metaphone();
        String encoded = metaphone.encode("BARRACK");
        String decoded = (String) metaphone.decode("ABCDEFGHIJ");
    }
}