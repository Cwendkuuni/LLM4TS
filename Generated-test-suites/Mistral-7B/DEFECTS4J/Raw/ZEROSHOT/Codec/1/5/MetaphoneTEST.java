package org.apache.commons.codec.language;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.commons.codec.EncoderException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class MetaphoneTest {

    private final String input;
    private final String expectedOutput;

    public MetaphoneTest(String input, String expectedOutput) {
        this.input = input;
        this.expectedOutput = expectedOutput;
    }

    @Parameterized.Parameters(name = "{index}: input={0} expectedOutput={1}")
    public static Collection<Object[]> data() {
        Object[][] data = new Object[][] {
                {"apple", "APEL"},
                {"appetizer", "APITZR"},
                {"banana", "BANA"},
                {"bananana", "BANANA"},
                {"cat", "KAT"},
                {"caterpillar", "KATRPLR"},
                {"dog", "DOG"},
                {"doggy", "DOGI"},
                {"elephant", "ELPHT"},
                {"elephants", "ELPHTS"},
                {"figure", "FGER"},
                {"figures", "FGERZ"},
                {"hello", "HELO"},
                {"helicopter", "HLPTPR"},
                {"hippopotamus", "HPPAWTMS"},
                {"hippopotami", "HPPAWTMI"},
                {"how", "HOU"},
                {"humus", "HUMS"},
                {"isosceles", "ISOSCLS"},
                {"jazz", "JAS"},
                {"jingle", "JNGL"},
                {"judge", "JUDJ"},
                {"jump", "JUMP"},
                {"kangaroo", "KNGPRU"},
                {"lamp", "LAMP"},
                {"lemon", "LEMON"},
                {"leper", "LEPR"},
                {"leprosy", "LEPRS"},
                {"letter", "LTR"},
                {"library", "LBRY"},
                {"metaphone", "MTAFON"},
                {"metaphones", "MTAFONS"},
                {"nut", "NUT"},
                {"nutmeg", "NUTMGG"},
                {"octopus", "OKTPUS"},
                {"octopuses", "OKTPUSZ"},
                {"pencil", "PNSL"},
                {"penguin", "PNGWIN"},
                {"pineapple", "PNAPL"},
                {"pizza", "PZZ"},
                {"pizzas", "PZZS"},
                {"quagga", "KAWGA"},
                {"quagmire", "KAWGMR"},
                {"queen", "QYN"},
                {"queens", "QYNZ"},
                {"rabbit", "RBIT"},
                {"rabbits", "RBITZ"},
                {"raccoon", "RKUN"},
                {"raccoons", "RKUNZ"},
                {"sausage", "SOSJS"},
                {"sausages", "SOSJSZ"},
                {"sheep", "SFP"},
                {"shepherd", "SFPHTD"},
                {"sheet", "SIT"},
                {"sheets", "SITS"},
                {"squirrel", "SKWRRL"},
                {"squirrels", "SKWRRLZ"},
                {"tiger", "TGRR"},
                {"tigers", "TGRRZ"},
                {"whiskey", "HSKI"},
                {"whisky", "HSKI"},
                {"wolf", "WOLF"},
                {"wolves", "WOLFZ"},
                {"zebra", "ZEBR"},
                {"zebras", "ZEBRZ"},
                {"zucchini", "ZKCHNI"},
                {"zucchinis", "ZKCHNIZ"}
        };
        return Arrays.asList(data);
    }

    @Test
    public void testMetaphone() throws EncoderException {
        Metaphone metaphone = new Metaphone();
        String result = metaphone.metaphone(input);
        assertEquals(expectedOutput, result);
    }

    @Test(expected = EncoderException.class)
    public void testIllegalArgumentException() throws EncoderException {
        Metaphone metaphone = new Metaphone();
        metaphone.encode(null);
    }

    @Test
    public void testIsMetaphoneEqual() throws EncoderException {
        Metaphone metaphone = new Metaphone();
        assertTrue(metaphone.isMetaphoneEqual("apple", "apple"));
        assertFalse(metaphone.isMetaphoneEqual("apple", "banana"));
    }

    @Test
    public void testGetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        assertEquals(4, metaphone.getMaxCodeLen());
    }

    @Test
    public void testSetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        metaphone.setMaxCodeLen(5);
        assertEquals(5, metaphone.getMaxCodeLen());
    }
}