import com.fasterxml.jackson.core.JsonPointer;
import org.junit.Test;

public class JsonPointerTest {

    // Test method: matches()
    @Test
    public void testMatches() {
        JsonPointer pointer = JsonPointer.compile("/");
        assert(pointer.matches());

        pointer = JsonPointer.compile("/foo");
        assert(!pointer.matches());
    }

    // Test method: getMatchingProperty()
    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assert(pointer.getMatchingProperty().equals("foo"));

        pointer = JsonPointer.compile("/bar");
        assert(pointer.getMatchingProperty().equals("bar"));
    }

    // Test method: getMatchingIndex()
    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = JsonPointer.compile("/0");
        assert(pointer.getMatchingIndex() == 0);

        pointer = JsonPointer.compile("/1");
        assert(pointer.getMatchingIndex() == 1);
    }

    // Test method: mayMatchProperty()
    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assert(pointer.mayMatchProperty());

        pointer = JsonPointer.compile("/bar");
        assert(pointer.mayMatchProperty());

        pointer = JsonPointer.compile("/0");
        assert(!pointer.mayMatchProperty());
    }

    // Test method: mayMatchElement()
    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assert(!pointer.mayMatchElement());

        pointer = JsonPointer.compile("/bar");
        assert(!pointer.mayMatchElement());

        pointer = JsonPointer.compile("/0");
        assert(pointer.mayMatchElement());
    }

    // Test method: matchProperty(String name)
    @Test
    public void testMatchProperty() {
        JsonPointer pointer = JsonPointer.compile("/foo");
        assert(pointer.matchProperty("foo") == pointer);

        pointer = JsonPointer.compile("/bar");
        assert(pointer.matchProperty("bar") == pointer);

        assert(pointer.matchProperty("foo") == null);
    }

    // Test method: matchElement(int index)
    @Test
    public void testMatchElement() {
        JsonPointer pointer = JsonPointer.compile("/0");
        assert(pointer.matchElement(0) == pointer);

        pointer = JsonPointer.compile("/1");
        assert(pointer.matchElement(1) == pointer);

        assert(pointer.matchElement(0) == null);
    }

    // Test method: tail()
    @Test
    public void testTail() {
        JsonPointer pointer = JsonPointer.compile("/");
        assert(pointer.tail() == null);

        pointer = JsonPointer.compile("/foo");
        assert(pointer.tail().toString().equals("/"));

        pointer = JsonPointer.compile("/bar");
        assert(pointer.tail().toString().equals("/"));
    }

    // Test method: toString()
    @Test
    public void testToString() {
        JsonPointer pointer = JsonPointer.compile("/");
        assert(pointer.toString().equals("/"));

        pointer = JsonPointer.compile("/foo");
        assert(pointer.toString().equals("/foo"));

        pointer = JsonPointer.compile("/bar");
        assert(pointer.toString().equals("/bar"));
    }

    // Test method: hashCode()
    @Test
    public void testHashCode() {
        JsonPointer pointer1 = JsonPointer.compile("/");
        JsonPointer pointer2 = JsonPointer.compile("/");
        assert(pointer1.hashCode() == pointer2.hashCode());

        pointer1 = JsonPointer.compile("/foo");
        pointer2 = JsonPointer.compile("/foo");
        assert(pointer1.hashCode() == pointer2.hashCode());

        pointer1 = JsonPointer.compile("/bar");
        pointer2 = JsonPointer.compile("/bar");
        assert(pointer1.hashCode() == pointer2.hashCode());
    }

    // Test method: equals(Object o)
    @Test
    public void testEquals() {
        JsonPointer pointer1 = JsonPointer.compile("/");
        JsonPointer pointer2 = JsonPointer.compile("/");
        assert(pointer1.equals(pointer2));

        pointer1 = JsonPointer.compile("/foo");
        pointer2 = JsonPointer.compile("/foo");
        assert(pointer1.equals(pointer2));

        pointer1 = JsonPointer.compile("/bar");
        pointer2 = JsonPointer.compile("/bar");
        assert(pointer1.equals(pointer2));

        pointer1 = JsonPointer.compile("/foo");
        pointer2 = JsonPointer.compile("/bar");
        assert(!pointer1.equals(pointer2));

        pointer1 = JsonPointer.compile("/");
        pointer2 = JsonPointer.compile("/foo");
        assert(!pointer1.equals(pointer2));
    }
}