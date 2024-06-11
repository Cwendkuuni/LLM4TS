import org.junit.Test;
import static org.junit.Assert.*;

public class NumberInputTest {

 @Test
 public void testParseIntWithValidInput() {
 char[] digitChars = {'1', '2', '3', '4', '5'};
 int expected = 12345;
 int actual = NumberInput.parseInt(digitChars, 0, 5);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseIntWithMinusSign() {
 char[] digitChars = {'-', '1', '2', '3', '4', '5'};
 int expected = -12345;
 int actual = NumberInput.parseInt(digitChars, 0, 6);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseIntWithLengthGreaterThanExpected() {
 char[] digitChars = {'1', '2', '3', '4', '5', '6'};
 int expected = 12345;
 int actual = NumberInput.parseInt(digitChars, 0, 5);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseIntWithNegativeLength() {
 char[] digitChars = {'1', '2', '3', '4', '5'};
 int actual = NumberInput.parseInt(digitChars, 5, -5);
 fail("Expected IllegalArgumentException but got: " + actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseIntWithInvalidInput() {
 char[] digitChars = {'1', 'a', '3', '4', '5'};
 int actual = NumberInput.parseInt(digitChars, 0, 5);
 fail("Expected NumberFormatException but got: " + actual);
 }

 @Test
 public void testParseLongWithValidInput() {
 char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
 long expected = 1234567890L;
 long actual = NumberInput.parseLong(digitChars, 0, 11);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLongWithMinusSign() {
 char[] digitChars = {'-', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
 long expected = -1234567890L;
 long actual = NumberInput.parseLong(digitChars, 0, 12);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseLongWithLengthLessThanExpected() {
 char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8'};
 long expected = 12345678L;
 long actual = NumberInput.parseLong(digitChars, 0, 9);
 assertEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseLongWithNegativeLength() {
 char[] digitChars = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};
 long actual = NumberInput.parseLong(digitChars, 12, -1);
 fail("Expected IllegalArgumentException but got: " + actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseLongWithInvalidInput() {
 char[] digitChars = {'1', '2', '3', '4', '5', 'a'};
 long actual = NumberInput.parseLong(digitChars, 0, 6);
 fail("Expected NumberFormatException but got: " + actual);
 }

 @Test
 public void testParseAsIntWithValidInput() {
 String input = "12345";
 int expected = 12345;
 int actual = NumberInput.parseAsInt(input, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsIntWithDefaultValue() {
 String input = "";
 int expected = 0;
 int actual = NumberInput.parseAsInt(input, expected);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsIntWithNegativeInput() {
 String input = "-12345";
 int expected = -12345;
 int actual = NumberInput.parseAsInt(input, 0);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseAsIntWithInvalidInput() {
 String input = "1a2b3";
 int actual = NumberInput.parseAsInt(input, 0);
 fail("Expected NumberFormatException but got: " + actual);
 }

 @Test
 public void testParseAsLongWithValidInput() {
 String input = "1234567890";
 long expected = 1234567890L;
 long actual = NumberInput.parseAsLong(input, 0);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsLongWithDefaultValue() {
 String input = "";
 long expected = 0;
 long actual = NumberInput.parseAsLong(input, expected);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseAsLongWithNegativeInput() {
 String input = "-1234567890";
 long expected = -1234567890L;
 long actual = NumberInput.parseAsLong(input, 0);
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseAsLongWithInvalidInput() {
 String input = "1a2b3";
 long actual = NumberInput.parseAsLong(input, 0);
 fail("Expected NumberFormatException but got: " + actual);
 }

 @Test
 public void testParseBigDecimalWithValidInput() {
 String numStr = "12345.6789";
 BigDecimal expected = new BigDecimal("12345.6789");
 BigDecimal actual = NumberInput.parseBigDecimal(numStr);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBigDecimalWithValidInputLong() {
 char[] buffer = {'1', '2', '3', '4', '5', '.', '6', '7', '8', '9', '0'};
 int offset = 11;
 int len = 1;
 BigDecimal expected = new BigDecimal("12345.6789");
 BigDecimal actual = NumberInput.parseBigDecimal(buffer, offset, len);
 assertEquals(expected, actual);
 }

 @Test
 public void testParseBigDecimalWithValidInputCharArray() {
 char[] buffer = {'1', '2', '3', '4', '5', '.', '6', '7', '8', '9', '0'};
 BigDecimal actual = NumberInput.parseBigDecimal(buffer);
 BigDecimal expected = new BigDecimal("12345.6789");
 assertEquals(expected, actual);
 }

 @Test(expected = NumberFormatException.class)
 public void testParseBigDecimalWithInvalidInput() {
 String numStr = "1a2b3";
 BigDecimal actual = NumberInput.parseBigDecimal(numStr);
 fail("Expected NumberFormatException but got: " + actual);
 }
}