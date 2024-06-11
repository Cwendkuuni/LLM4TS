import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;
import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphone() throws DecoderException {
        Metaphone metaphone = new Metaphone();

        String[] inputStrings = {"apple", "application", "approach", "append", "appetite", "artichoke", "asparagus", "baboon", "banana", "barbara", "beauty", "beaver", "beige", "belgian", "bicycle", "blonde", "boomerang", "boyfriend", "bread", "butter", "caramel", "carrot", "cat", "channel", "charlie", "cheese", "cheetah", "cherry", "chocolate", "christopher", "chrysanthemum", "chipmunk", "church", "citrus", "coffee", "coconut", "coyote", "cream", "cucumber", "daisy", "dolphin", "donut", "dynamite", "elastic", "elephant", "elope", "echo", "eggplant", "eggroll", "eiffel", "elvis", "embryo", "evaporate", "expedite", "fahrenheit", "fighter", "flamingo", "forty", "francis", "franklin", "frog", "giraffe", "guitar", "hamburger", "happiness", "hedgehog", "helicopter", "herring", "hickory", "hospital", "hotdog", "huckleberry", "hugo", "icecream", "ideal", "igloo", "illusion", "imagine", "indian", "iris", "island", "jacket", "jasmine", "jellybean", "judge", "jungle", "juneau", "jupiter", "kelp", "kiwi", "labrador", "lake", "lamb", "lantern", "lemon", "leopard", "little", "lizard", "mango", "marshmallow", "meadow", "muffin", "muse", "noodle", "nutmeg", "octopus", "olive", "orange", "orchid", "oslo", "otter", "pancake", "parrot", "peach", "peanut", "pepper", "pizza", "potato", "quilt", "queen", "quicksand", "rabbit", "rainbow", "raspberry", "rat", "refrigerator", "rhino", "robin", "rose", "rubber", "sailboat", "sausage", "scorpion", "shark", "shrimp", "skunk", "snake", "snowman", "spaghetti", "sparrow", "spear", "spider", "strawberry", "sunshine", "swan", "tiger", "tomato", "tortilla", "tropical", "turkey", "unicorn", "umbrella", "violet", "watermelon", "whiskey", "wilderness", "winter", "woodpecker", "yellow", "zebra"};
        String[] expectedCodes = {"AA0", "A000", "AH0", "AH1", "AA1", "AH2", "AH2", "AH0", "AA0", "AA0", "AH0", "AH0", "BH0", "BH0", "AH0", "AH0", "AH0", "AH1", "AA1", "AA1", "AH0", "AH0", "AH0", "AH0", "AH1", "AH0", "AH0", "AH0", "AH0", "AH0", "AH0", "AH0", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1", "AH1"};

        for (int i = 0; i < inputStrings.length; i++) {
            String result = metaphone.metaphone(inputStrings[i]);
            assertEquals("Test failed for input: " + inputStrings[i], expectedCodes[i], result);
        }
    }

    @Test
    public void testIsMetaphoneEqual() throws DecoderException {
        Metaphone metaphone = new Metaphone();

        String input1 = "apple";
        String input2 = "applet";

        assertTrue("Test failed for inputs: " + input1 + " and " + input2, metaphone.isMetaphoneEqual(input1, input2));

        input1 = "apple";
        input2 = "orange";

        assertFalse("Test failed for inputs: " + input1 + " and " + input2, metaphone.isMetaphoneEqual(input1, input2));
    }

    @Test
    public void testIsMetaphoneEqualWithMaxCodeLen() throws DecoderException {
        Metaphone metaphone = new Metaphone();
        metaphone.setMaxCodeLen(5);

        String input1 = "apple";
        String input2 = "applet";

        assertTrue("Test failed for inputs: " + input1 + " and " + input2, metaphone.isMetaphoneEqual(input1, input2));

        input1 = "apple";
        input2 = "orange";

        assertFalse("Test failed for inputs: " + input1 + " and " + input2, metaphone.isMetaphoneEqual(input1, input2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetMaxCodeLenNegative() throws DecoderException {
        Metaphone metaphone = new Metaphone();
        metaphone.setMaxCodeLen(-5);
    }
}