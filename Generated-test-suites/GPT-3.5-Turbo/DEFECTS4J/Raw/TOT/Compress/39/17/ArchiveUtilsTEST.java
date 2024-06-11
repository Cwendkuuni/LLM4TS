import org.junit.Test;
import static org.junit.Assert.*;

public class ArchiveUtilsTest {


//Test case for the toString method
@Test
public void testToString(){
    //Typical use case
    ArchiveEntry entry1 = new ArchiveEntry("main.c");
    entry1.setDirectory(false);
    entry1.setSize(2000);
    assertEquals("-    2000 main.c", ArchiveUtils.toString(entry1));

    //Edge case
    ArchiveEntry entry2 = new ArchiveEntry("");
    entry2.setDirectory(true);
    entry2.setSize(100);
    assertEquals("d     100 ", ArchiveUtils.toString(entry2));

    //Error scenario
    assertNull(ArchiveUtils.toString(null));
}

//Test case for the matchAsciiBuffer method
@Test
public void testMatchAsciiBuffer(){
    //Typical use case
    byte[] buffer1 = {65, 66, 67, 68, 69};
    assertTrue(ArchiveUtils.matchAsciiBuffer("ABCDE", buffer1));

    //Edge case
    byte[] buffer2 = {};
    assertTrue(ArchiveUtils.matchAsciiBuffer("", buffer2));

    //Error scenario
    byte[] buffer3 = {65, 66, 67, 68, 69};
    assertFalse(ArchiveUtils.matchAsciiBuffer("XYZ", buffer3));
}

//Test case for the toAsciiBytes method
@Test
public void testToAsciiBytes(){
    //Typical use case
    byte[] expected1 = {65, 66, 67, 68, 69};
    assertArrayEquals(expected1, ArchiveUtils.toAsciiBytes("ABCDE"));

    //Edge case
    byte[] expected2 = {};
    assertArrayEquals(expected2, ArchiveUtils.toAsciiBytes(""));

    //Error scenario
    assertNull(ArchiveUtils.toAsciiBytes(null));
}

//Test case for the toAsciiString method
@Test
public void testToAsciiString(){
    //Typical use case
    byte[] inputBytes1 = {65, 66, 67, 68, 69};
    assertEquals("ABCDE", ArchiveUtils.toAsciiString(inputBytes1));

    //Edge case
    byte[] inputBytes2 = {};
    assertEquals("", ArchiveUtils.toAsciiString(inputBytes2));

    //Error scenario
    byte[] inputBytes3 = {65, 66, 67, 68, 69};
    assertNotEquals("XYZ", ArchiveUtils.toAsciiString(inputBytes3));
}

//Test case for the isEqual method
@Test
public void testIsEqual(){
    //Typical use case
    byte[] buffer1 = {65, 66, 67, 68, 69};
    byte[] buffer2 = {65, 66, 67, 68, 69};
    assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));

    //Edge case
    byte[] buffer3 = {};
    byte[] buffer4 = {};
    assertTrue(ArchiveUtils.isEqual(buffer3, buffer4));

    //Error scenario
    byte[] buffer5 = {65, 66, 67, 68, 69};
    byte[] buffer6 = {70, 71, 72, 73, 74};
    assertFalse(ArchiveUtils.isEqual(buffer5, buffer6));
}

//Test case for the isEqualWithNull method
@Test
public void testIsEqualWithNull(){
    //Typical use case
    byte[] buffer1 = {65, 66, 67, 68, 69};
    byte[] buffer2 = {65, 66, 67, 68, 69};
    assertTrue(ArchiveUtils.isEqualWithNull(buffer1, 0, 5, buffer2, 0, 5));

    //Edge case
    byte[] buffer3 = {};
    byte[] buffer4 = {};
    assertTrue(ArchiveUtils.isEqualWithNull(buffer3, 0, 0, buffer4, 0, 0));

    //Error scenario
    byte[] buffer5 = {65, 66, 67, 68, 69};
    byte[] buffer6 = {70, 71, 72, 73, 74};
    assertFalse(ArchiveUtils.isEqualWithNull(buffer5, 0, 5, buffer6, 0, 5));
}

//Test case for the isArrayZero method
@Test
public void testIsArrayZero(){
    //Typical use case
    byte[] a1 = {65, 66, 0, 68, 69};
    assertFalse(ArchiveUtils.isArrayZero(a1, 5));

    //Edge case
    byte[] a2 = {};
    assertTrue(ArchiveUtils.isArrayZero(a2, 0));

    //Error scenario
    byte[] a3 = {65, 66, 67, 68, 69};
    assertFalse(ArchiveUtils.isArrayZero(a3, 5));
}

//Test case for the sanitize method
@Test
public void testSanitize(){
    //Typical use case
    String s1 = "Hello!#$%^&*()_+=-0987";
    assertEquals("Hello!#$%^&*()_+=-0987", ArchiveUtils.sanitize(s1));

    //Edge case
    String s2 = "";
    assertEquals("", ArchiveUtils.sanitize(s2));

    //Error scenario
    String s3 = "Hello!#$%^&*()_+=-0987";
    assertNotEquals("ABC", ArchiveUtils.sanitize(s3));
}

}