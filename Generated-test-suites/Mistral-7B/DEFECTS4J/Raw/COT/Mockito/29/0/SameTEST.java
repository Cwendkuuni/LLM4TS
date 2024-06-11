import org.junit.Test;
import org.junit.Assert.*;
import org.mockito.internal.matchers.Same;

public class SameTest {

    @Test
    public void testSameInstance() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        Object actual = wanted;
        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testDifferentInstances() {
        Object wanted = new Object();
        Same sameMatcher = new Same(wanted);
        Object actual = new Object();
        assertFalse(sameMatcher.matches(actual));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullWanted() {
        Same sameMatcher = new Same(null);
    }

    @Test
    public void testStringWanted() {
        String wanted = "test";
        Same sameMatcher = new Same(wanted);
        String actual = "test";
        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testDifferentStrings() {
        String wanted = "test1";
        Same sameMatcher = new Same(wanted);
        String actual = "test2";
        assertFalse(sameMatcher.matches(actual));
    }

    @Test
    public void testCharacterWanted() {
        char wanted = 'a';
        Same sameMatcher = new Same(wanted);
        char actual = 'a';
        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testDifferentCharacters() {
        char wanted = 'a';
        Same sameMatcher = new Same(wanted);
        char actual = 'b';
        assertFalse(sameMatcher.matches(actual));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullActual() {
        Same sameMatcher = new Same(new Object());
        sameMatcher.matches(null);
    }

    @Test
    public void testSerializableObject() {
        Object wanted = new SerializableObject();
        Same sameMatcher = new Same(wanted);
        SerializableObject actual = new SerializableObject();
        assertTrue(sameMatcher.matches(actual));
    }

    @Test
    public void testDifferentSerializableObjects() {
        Object wanted = new SerializableObject();
        Same sameMatcher = new Same(wanted);
        SerializableObject actual = new SerializableObject();
        actual.setName("different");
        assertFalse(sameMatcher.matches(actual));
    }

}

class SerializableObject implements Serializable {
    private String name;

    public SerializableObject() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}