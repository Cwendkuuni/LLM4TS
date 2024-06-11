import org.apache.commons.codec.language.Soundex;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class SoundexTest {

 @Test
 public void testSoundexUSEnglish() {
 Soundex soundex = Soundex.US_ENGLISH;
 String[] testCases = new String[]{"apple", "appel", "application", "Approach", "April", "Banana", "Bean", "Bible", "Cat", "Caterpillar", "China", "Chinaman", "Christ", "Christmas", "Cobra", "Cod", "Codfish", "Coffee", "Coffin", "Cottage", "Dog", "Dogwood", "Elephant", "Eggplant", "Eggshell", "Eggwhite", "Eiffel", "Elk", "Elm", "Emu", "Envelope", "Euro", "Europe", "Fish", "Flower", "Fox", "Frog", "Grape", "Grapefruit", "Grapevine", "Hippopotamus", "Horse", "Ice", "Iceberg", "India", "Indian", "Iris", "Iron", "Ivy", "Jacket", "Jam", "Jasmine", "Jewel", "Jungle", "Kangaroo", "Kiwi", "Lemon", "Lime", "Lily", "Lion", "Log", "Loganberry", "Lotus", "Mango", "Marmalade", "Marmot", "Moose", "Mushroom", "Nutmeg", "Octopus", "Orange", "Orchid", "Olive", "Onion", "Peach", "Penguin", "Pine", "Pineapple", "Potato", "Pumpkin", "Quartz", "Raspberry", "Rat", "Raven", "Rose", "Rubber", "Ruby", "Sailor", "Salmon", "Sausage", "Scallion", "Scorpion", "Seaweed", "Shark", "Shoe", "Shrimp", "Skunk", "Sloth", "Snake", "Snowman", "Snowpea", "Soybean", "Spaghetti", "Sparrow", "Square", "Starry", "Sunflower", "Swan", "Tangerine", "Tiger", "Tomato", "Tulip", "Turnip", "Turtle", "Violet", "Watermelon", "Whale", "Wheat", "Wildcat", "Wildflower", "Wine", "Winter", "Woodpecker", "Yam", "Yellow", "Zebra"};
 for (String testCase : testCases) {
 String expected = testCase;
 String actual = soundex.soundex(testCase);
 assertEquals("Soundex code for test case '" + testCase + "' is incorrect.", expected, actual);
 }
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidCharacter() {
 Soundex soundex = new Soundex(Soundex.US_ENGLISH_MAPPING);
 String invalidCharacter = "\u0001";
 String input = "Test " + invalidCharacter;
 String expected = soundex.soundex(input.substring(0, input.length() - 1));
 String actual = null;
 try {
 actual = soundex.soundex(input);
 } catch (EncoderException e) {
 // EncoderException is thrown, so test passes
 }
 assertEquals("Soundex should have thrown an IllegalArgumentException for invalid character.", expected, actual);
 }

 @Test
 public void testDifference() {
 Soundex soundex = Soundex.US_ENGLISH;
 String testCase1 = "Application";
 String testCase2 = "Application.com";
 int difference = soundex.difference(testCase1, testCase2);
 assertEquals("Difference for test case 1 and 2 is incorrect.", 1, difference);

 testCase1 = "Approach";
 testCase2 = "Approachable";
 difference = soundex.difference(testCase1, testCase2);
 assertEquals("Difference for test case 1 and 2 is incorrect.", 2, difference);
 }

 @Test
 public void testEncodeDecode() {
 Soundex soundex = new Soundex(Soundex.US_ENGLISH_MAPPING);
 String input = "TestTest";
 String encoded = soundex.encode(input).toString();
 String decoded = soundex.soundex(input);
 assertEquals("Encode/Decode roundtrip test for test case 'TestTest' faile.", encoded, decoded);
 }
}