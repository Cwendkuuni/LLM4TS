import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ExtendedBufferedReaderTest {

private ExtendedBufferedReader reader;

@Before
public void setUp() {
    Reader mockReader = // Create a mock reader
    reader = new ExtendedBufferedReader(mockReader);
}

@Test
public void testRead() throws IOException {
    int expected = 'a';
    int actual = reader.read();
    assertEquals(expected, actual);
}

@Test
public void testReadAgain() {
    int expected = 'a';
    reader.read();
    int actual = reader.readAgain();
    assertEquals(expected, actual);
}

@Test
public void testReadCharArray() throws IOException {
    char[] buf = new char[5];
    int expected = 3;
    int actual = reader.read(buf, 0, 5);
    assertEquals(expected, actual);
}

@Test
public void testReadLine() throws IOException {
    String expected = "Hello, World!";
    String actual = reader.readLine();
    assertEquals(expected, actual);
}

@Test
public void testLookAhead() throws IOException {
    int expected = 'b';
    int actual = reader.lookAhead();
    assertEquals(expected, actual);
}

@Test
public void testGetLineNumber() {
    int expected = 5;
    reader.readLine();
    int actual = reader.getLineNumber();
    assertEquals(expected, actual);
}

}