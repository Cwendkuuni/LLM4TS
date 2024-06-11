import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.utils.ArchiveUtils;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class ArchiveUtilsTest {

 @Test
 public void testToStringMethod() {
 ArchiveEntry archiveEntry = new ArchiveEntry("main.c");
 archiveEntry.setSize(2000L);
 archiveEntry.setDirectory(false);

 String result = ArchiveUtils.toString(archiveEntry);
 String expected = "- 2000 main.c";
 assertEquals(expected, result);
 }

 @Test
 public void testMatchAsciiBufferMethod() {
 byte[] buffer1 = "expectedString".getBytes();
 byte[] buffer2 = "sameBuffer".getBytes();

 boolean result = ArchiveUtils.matchAsciiBuffer("expectedString", buffer1, 0, buffer1.length);
 boolean expected = true;
 assertEquals(expected, result);

 result = ArchiveUtils.matchAsciiBuffer("sameBuffer", buffer2, 0, buffer2.length);
 expected = true;
 assertEquals(expected, result);

 result = ArchiveUtils.matchAsciiBuffer("expectedString", buffer2, 0, buffer2.length);
 expected = false;
 assertEquals(expected, result);
 }

 @Test
 public void testIsEqualMethod() {
 byte[] buffer1 = {0, 1, 2, 3, 4, 5, 0, 0, 0};
 byte[] buffer2 = {0, 1, 2, 3, 4, 5, 0, 0, 0};
 byte[] buffer3 = {0, 1, 2, 3, 4, 5, 6, 0, 0};

 boolean result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
 boolean expected = true;
 assertEquals(expected, result);

 result = ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer3, 0, buffer3.length);
 expected = false;
 assertEquals(expected, result);
 }

 @Test
 public void testIsEqualWithNullMethod() {
 byte[] buffer1 = {0, 1, 2, 3, 0, 0, 0};
 byte[] buffer2 = {0, 1, 2, 3, 4, 5, 0, 0, 0};

 boolean result = ArchiveUtils.isEqualWithNull(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length);
 boolean expected = true;
 assertEquals(expected, result);
 }

 @Test
 public void testIsArrayZeroMethod() {
 byte[] buffer = {0, 0, 0, 0, 0};

 boolean result = ArchiveUtils.isArrayZero(buffer, 5);
 boolean expected = true;
 assertEquals(expected, result);
 }

 @Test
 public void testSanitizeMethod() {
 String result = ArchiveUtils.sanitize("testString\\with/special/characters/and\\long\\filename.txt");
 String expected = "testString??with??special??characters??and??long??filename.txt";
 assertEquals(expected, result);
 }
}