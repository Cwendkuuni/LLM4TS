
import org.junit.*;
import static org.junit.Assert.*;

public class SplitterTest {

    private Splitter splitter;

    @Before
    public void setUp() {
        splitter = Splitter.on(",");
    }

    @Test
    public void testOn_CharSeparator_SeparateString_ReturnsArrayOfStrings() {
        String str = "Hello,World";
        String[] expected = {"Hello", "World"};
        assertArrayEquals(expected, splitter.split(str));
    }

    @Test
    public void testOn_CharSeparator_EmptyString_ReturnsEmptyArray() {
        String str = "";
        String[] expected = {};
        assertArrayEquals(expected, splitter.split(str));
    }

    @Test
    public void testOn_CharSeparator_LeadingAndTrailingDelimiters_ReturnsTrimmedStrings() {
        String str = ",,Hello,,World,,";
        String[] expected = {"Hello", "World"};
        assertArrayEquals(expected, splitter.split(str));
    }

    @Test
    public void testOn_StringSeparator_SeparateString_ReturnsArrayOfStrings() {
        String str = "Hello,World";
        String[] expected = {"Hello", "World"};
        assertArrayEquals(expected, Splitter.on(",").split(str));
    }

    @Test
    public void testOn_StringSeparator_EmptyString_ThrowsIllegalArgumentException() {
        String str = "";
        assertThrows(IllegalArgumentException.class, () -> Splitter.on(",").split(str));
    }

    // ... additional tests for other methods ...

}

