package de.beiri22.stringincrementor.helper;
import org.junit.Test;

public class IndexedStringTest {

    // Test case for constructor
    @Test
    public void testIndexedStringConstructor() {
        IndexedString indexedString = new IndexedString("hello");
        // Add assertions here
    }

    // Test case for indexOf method
    @Test
    public void testIndexOf() {
        IndexedString indexedString = new IndexedString("hello");
        // Add assertions here
    }

    // Edge case: empty string in constructor
    @Test
    public void testIndexedStringConstructorEmptyString() {
        IndexedString indexedString = new IndexedString("");
        // Add assertions here
    }

    // Edge case: target array longer than string in indexOf method
    @Test
    public void testIndexOfLongTargetArray() {
        IndexedString indexedString = new IndexedString("hello");
        // Add assertions here
    }

    // Exception handling: NullPointerException in indexOf method
    @Test(expected = NullPointerException.class)
    public void testIndexOfNullTargetArray() {
        IndexedString indexedString = new IndexedString("hello");
        indexedString.indexOf(null);
    }
}