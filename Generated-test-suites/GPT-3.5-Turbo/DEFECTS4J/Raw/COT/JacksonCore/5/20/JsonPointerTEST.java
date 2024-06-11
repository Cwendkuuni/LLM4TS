import org.junit.Test;

public class JsonPointerTest {

    @Test
    public void testMatches() {
        JsonPointer pointer = new JsonPointer();
        assert pointer.matches();
    }

    @Test
    public void testGetMatchingProperty() {
        JsonPointer pointer = new JsonPointer();
        assert pointer.getMatchingProperty().equals("");
    }

    @Test
    public void testGetMatchingIndex() {
        JsonPointer pointer = new JsonPointer();
        assert pointer.getMatchingIndex() == -1;
    }

    @Test
    public void testMayMatchProperty() {
        JsonPointer pointer = new JsonPointer();
        assert pointer.mayMatchProperty();
    }

    @Test
    public void testMayMatchElement() {
        JsonPointer pointer = new JsonPointer();
        assert !pointer.mayMatchElement();
    }

    @Test
    public void testMatchProperty() {
        String name = "name";
        JsonPointer pointer = new JsonPointer();
        assert pointer.matchProperty(name) == null;
    }

    @Test
    public void testMatchElement() {
        int index = 1;
        JsonPointer pointer = new JsonPointer();
        assert pointer.matchElement(index) == null;
    }

    @Test
    public void testTail() {
        JsonPointer pointer = new JsonPointer();
        assert pointer.tail() == null;
    }

    @Test
    public void testToString() {
        JsonPointer pointer = new JsonPointer();
        assert pointer.toString().equals("");
    }

    @Test
    public void testHashCode() {
        JsonPointer pointer = new JsonPointer();
        assert pointer.hashCode() == 0;
    }

    @Test
    public void testEquals() {
        JsonPointer pointer1 = new JsonPointer();
        JsonPointer pointer2 = new JsonPointer();
        assert pointer1.equals(pointer2);
    }
}