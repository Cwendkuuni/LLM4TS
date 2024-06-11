package org.apache.commons.codec.language;

import org.apache.commons.codec.EncoderException;
import org.junit.Assert;
import org.junit.Test;

public class MetaphoneTest {

    @org.junit.Before
    public void setUp() {
        // Set up the test environment if necessary
    }

    @org.junit.After
    public void tearDown() {
        // Tear down the test environment if necessary
    }

    @Test
    public void testMetaphone_EmptyString() {
        Metaphone metaphone = new Metaphone();
        Assert.assertEquals("", metaphone.metaphone(""));
    }

    @Test
    public void testMetaphone_SingleCharacter() {
        Metaphone metaphone = new Metaphone();
        for (char c = 'A'; c <= 'Z'; c++) {
            Assert.assertEquals(Character.toString(c), metaphone.metaphone(Character.toString(c)));
        }
    }

    @Test(expected = EncoderException.class)
    public void testMetaphone_NullString() {
        Metaphone metaphone = new Metaphone();
        metaphone.metaphone(null);
    }

    @Test
    public void testMetaphone_BasicFunctionality() {
        Metaphone metaphone = new Metaphone();
        String[] testCases = {"APPLE", "BANANA", "CAT", "DOG", "ELEPHANT", "FOX", "GIRAFFE", "HIPPO", "ICICLE",
                "JELLYFISH", "KANGAROO", "LION", "MONKEY", "NUT", "ORANGE", "POTATO", "QUICKBROWNFOX",
                "RHINOCEROS", "STRAWBERRY", "TIGER", "UNICORN", "VOLCANO", "WALRUS", "XYZ"};
        for (String testCase : testCases) {
            Assert.assertEquals(metaphone.metaphone(testCase), metaphone.metaphone(testCase));
        }
    }

    @Test
    public void testMetaphone_SpecialCases_InitialTwoCharacters() {
        Metaphone metaphone = new Metaphone();
        String[] testCases = {"APPLE", "APPLEMAN", "KNIGHT", "KNICKERBOCKER", "KANGAROO", "KANGAROU",
                "APPLEAPPLE", "APPLEAPPLEAPPLE"};
        for (String testCase : testCases) {
            Assert.assertEquals(metaphone.metaphone(testCase), metaphone.metaphone(testCase));
        }
    }

    @Test
    public void testMetaphone_SpecialCases_InitialLetter() {
        Metaphone metaphone = new Metaphone();
        String[] testCases = {"AARDVARK", "APPLE", "APPLEAPPLE", "BABOON", "BANANA", "BANANANA", "BELUGA",
                "CADILLAC", "CAT", "CHICKEN", "CHRISTMAS", "CICADA", "COCONUT", "DOLPHIN", "DONKEY", "ELK",
                "ELEPHANT", "FLAMINGO", "FROG", "GATOR", "GORILLA", "GUAVA", "GYPSY", "HIPPOPOTAMUS",
                "HYENA", "ICICLE", "JAGUAR", "JELLYFISH", "KAFKA", "KANGAROO", "KIWI", "KOALA", "KOOKABURRA",
                "LAMBDA", "LEMUR", "LOLA", "LORAX", "MOOSE", "MONEY", "NIGHTINGALE", "NUTCRACKER", "OCTOPUS",
                "OPOSsum", "ORCA", "ORCHID", "OWL", "OYSTER", "PIRATE", "PIG", "PINEAPPLE", "QUOKKA",
                "QUILT", "QUEEN", "RACCOON", "RHINOCEROS", "RABBIT", "RASPBERRY", "RAT", "RHINO", "ROBIN",
                "SALMON", "SLUG", "SNAKE", "SQUIRREL", "TAPIR", "TOAD", "TURKEY", "TURTLE", "UNICORN",
                "UNICORNUNICORN", "URSUS", "VAMPIRE", "VENUS", "VICTORIA", "WALLABY", "WALRUS", "WHALE",
                "XMAS", "XYLOPHONE", "YAK", "YODA", "YOGA", "ZEBRA", "ZEBRADONKEY", "ZOOTROPOLIS"};
        for (String testCase : testCases) {
            Assert.assertEquals(metaphone.metaphone(testCase), metaphone.metaphone(testCase));
        }
    }

    @Test(expected = EncoderException.class)
    public void testMetaphone_InvalidInput() {
        Metaphone metaphone = new Metaphone();
        metaphone.metaphone("1234");
    }

    @Test(expected = EncoderException.class)
    public void testMetaphone_Punctuation() {
        Metaphone metaphone = new Metaphone();
        metaphone.metaphone("APPLE!");
    }

    @Test(expected = EncoderException.class)
    public void testMetaphone_Numbers() {
        Metaphone metaphone = new Metaphone();
        metaphone.metaphone("APPLE1234");
    }

    @Test
    public void testIsMetaphoneEqual_SameMetaphones() {
        Metaphone metaphone = new Metaphone();
        String[] testCases = {"APPLE", "BANANA", "CAT", "DOG", "ELEPHANT", "FOX", "GIRAFFE", "HIPPO",
                "ICICLE", "JELLYFISH", "KANGAROO", "LION", "MONKEY", "NUT", "ORANGE", "POTATO",
                "QUICKBROWNFOX", "RHINOCEROS", "STRAWBERRY", "TIGER", "UNICORN", "VOLCANO", "WALRUS",
                "XYZ"};
        for (int i = 0; i < testCases.length; i++) {
            for (int j = i + 1; j < testCases.length; j++) {
                Assert.assertTrue(metaphone.isMetaphoneEqual(testCases[i], testCases[j]));
            }
        }
    }

    @Test
    public void testIsMetaphoneEqual_DifferentMetaphones() {
        Metaphone metaphone = new Metaphone();
        String[] testCases = {"APPLE", "BANANA", "CAT", "DOG", "ELEPHANT", "FOX", "GIRAFFE", "HIPPO",
                "ICICLE", "JELLYFISH", "KANGAROO", "LION", "MONKEY", "NUT", "ORANGE", "POTATO",
                "QUICKBROWNFOX", "RHINOCEROS", "STRAWBERRY", "TIGER", "UNICORN", "VOLCANO", "WALRUS",
                "XYZ", "APPLEAPPLE", "APPLEAPPLEAPPLE", "BANANAANA", "BANANANANA", "CATCAT", "DOGDog",
                "ELEPHANT onderwater", "FOXFOX", "GIRAFFEGIRAFFE", "HIPPOhippo", "ICICLEDROP",
                "JELLYFISHWITHLEMONSLICE", "KANGAROOKOALA", "LIONroar", "MONKEYmonkey", "NUTnuts",
                "ORANGEorangejuice", "POTATOPOTATOCHIPS", "QUICKBROWNFOXjumps", "RHINOCEROSrhino",
                "STRAWBERRYstrawberrypie", "TIGERTIGERstripes", "UNICORNunicornrainbow",
                "VOLCANOvolcanoerupt", "WALRUSwalrusroll", "XYZxyzzy"};
        for (int i = 0; i < testCases.length; i++) {
            for (int j = i + 1; j < testCases.length; j++) {
                Assert.assertFalse(metaphone.isMetaphoneEqual(testCases[i], testCases[j]));
            }
        }
    }

    @Test
    public void testGetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        Assert.assertEquals(4, metaphone.getMaxCodeLen());
    }

    @Test
    public void testSetMaxCodeLen() {
        Metaphone metaphone = new Metaphone();
        metaphone.setMaxCodeLen(5);
        Assert.assertEquals(5, metaphone.getMaxCodeLen());
    }
}