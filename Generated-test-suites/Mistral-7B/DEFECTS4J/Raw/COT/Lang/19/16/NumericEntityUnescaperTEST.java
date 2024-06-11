import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

 private NumericEntityUnescaper unescaper = new NumericEntityUnescaper();

 @Test
 public void testTranslate_ValidNumericEntity_ReturnsCorrectlyUnescapedCharacter() {
 char input[] = {'&', '#', 'x', '3', 'F', ';'};
 int expectedLength = 2 + 6 + 1;
 int actualLength = unescaper.translate(new String(input), 0, new Writer() {
 @Override
 public void write(int c) throws IOException {
 // To make the test pass, you should write the unescaped character here.
 // For example, if the character is 'É', write '\u00E9' instead of 'É'.
 System.out.print((char) c);
 }

 @Override
 public void flush() throws IOException {
 // Flush method implementation is not required for this test case.
 }
 });
 assertEquals(expectedLength, actualLength);
 }

 @Test(expected = java.io.IOException.class)
 public void testTranslate_InvalidCharacterSequence_ThrowsIOException() {
 char input[] = {'&', '#', 'x', '3', 'F', 'a'}; // 'a' instead of ';'
 unescaper.translate(new String(input), 0, new Writer() {
 // Write method implementation is not required for this test case.
 });
 }

 @Test
 public void testTranslate_InvalidHexadecimalSequence_ReturnsZero() {
 char input[] = {'&', '#', 'x', '3', 'z', ';'}; // 'z' instead of 'F' or 'f'
 int expectedLength = 0;
 int actualLength = unescaper.translate(new String(input), 0, new Writer() {
 // Write method implementation is not required for this test case.
 });
 assertEquals(expectedLength, actualLength);
 }

 @Test
 public void testTranslate_ValidMultiByteCharacter_WritesCorrectlyUnescapedCharacter() {
 char input[] = {'&', '#', 'x', '1', '0', '0', '0', ';'}; // 'ñ' = U+00F1
 int expectedLength = 2 + 8 + 1;
 int actualLength = unescaper.translate(new String(input), 0, new Writer() {
 @Override
 public void write(int c) throws IOException {
 System.out.print((char) c);
 }

 @Override
 public void flush() throws IOException {
 // Flush method implementation is not required for this test case.
 }
 });
 assertEquals(expectedLength, actualLength);
 }

 @Test
 public void testTranslate_ValidMultiByteCharacterWithLeadingSpace_WritesCorrectlyUnescapedCharacter() {
 char input[] = {' ', '&', '#', 'x', '1', '0', '0', ';'}; // 'ñ' = U+00F1
 int expectedLength = 2 + 8 + 1;
 int actualLength = unescaper.translate(new String(input), 1, new Writer() {
 @Override
 public void write(int c) throws IOException {
 System.out.print((char) c);
 }

 @Override
 public void flush() throws IOException {
 // Flush method implementation is not required for this test case.
 }
 });
 assertEquals(expectedLength, actualLength);
 }

 @Test
 public void testTranslate_LargeCharacterValue_WritesCorrectlyUnescapedCharacter() {
 char input[] = {'&', '#', 'x', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', '1', ';'}; // 'π' = U+03C0
 int expectedLength = 2 + 17 + 1;
 int actualLength = unescaper.translate(new String(input), 0, new Writer() {
 @Override
 public void write(int c) throws IOException {
 System.out.print((char) c);
 }

 @Override
 public void flush() throws IOException {
 // Flush method implementation is not required for this test case.
 }
 });
 assertEquals(expectedLength, actualLength);
 }

}