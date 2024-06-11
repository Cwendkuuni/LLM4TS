import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SoundexUtilsTest {

  @Test
  public void testClean() {
    String input = "abc123DEF!@#";
    String expectedOutput = "ABCDEF";
    assertEquals(expectedOutput, SoundexUtils.clean(input));
  }

  @Test
  public void testCleanEmptyString() {
    String input = "";
    String expectedOutput = "";
    assertEquals(expectedOutput, SoundexUtils.clean(input));
  }

  @Test
  public void testDifference() {
    StringEncoder encoder = new SomeStringEncoder(); // Replace SomeStringEncoder with actual encoder implementation
    String input1 = "hello";
    String input2 = "hallo";
    int expectedOutput = 3;
    assertEquals(expectedOutput, SoundexUtils.difference(encoder, input1, input2));
  }

  @Test
  public void testDifferenceEncoded() {
    String input1 = "1234";
    String input2 = "12xx";
    int expectedOutput = 2;
    assertEquals(expectedOutput, SoundexUtils.differenceEncoded(input1, input2));
  }

  @Test(expected = EncoderException.class)
  public void testDifferenceEncoderException() throws EncoderException {
    StringEncoder encoder = new FaultyStringEncoder(); // Replace FaultyStringEncoder with an encoder that throws EncoderException
    String input1 = "hello";
    String input2 = "hallo";
    SoundexUtils.difference(encoder, input1, input2);
  }

}