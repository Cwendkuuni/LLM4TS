import org.junit.Before;
import org.junit.Test;

public class JsonPointerTest {

    private JsonPointer jsonPointer;

    @Before
    public void setUp() {
        jsonPointer = JsonPointer.EMPTY;
    }

    @Test
    public void testEmptyJsonPointer() {
        // Test matches method
        assert (jsonPointer.matches());

        // Test getMatchingProperty method
        assert ("".equals(jsonPointer.getMatchingProperty()));

        // Test getMatchingIndex method
        assert (jsonPointer.getMatchingIndex() == -1);

        // Test mayMatchProperty method
        assert (jsonPointer.mayMatchProperty());

        // Test mayMatchElement method
        assert (jsonPointer.mayMatchElement());

        // Test tail method
        assert (jsonPointer.tail() == null);

        // Test toString method
        assert ("".equals(jsonPointer.toString()));

        // Test hashCode method
        assert (jsonPointer.hashCode() == JsonPointer.EMPTY.hashCode());

        // Test equals method
        assert (jsonPointer.equals(JsonPointer.EMPTY));

        // Test matchProperty method
        assert (jsonPointer.matchProperty("property") == null);

        // Test matchElement method
        assert (jsonPointer.matchElement(0) == null);
    }

    @Test
    public void testNonEmptyJsonPointer() {
        JsonPointer jsonPointer = new JsonPointer("/property");
        JsonPointer nextSegment = new JsonPointer();
        // Test nextSegment field
        assert (jsonPointer._nextSegment.equals(nextSegment));

        // Test getMatchingProperty method
        assert ("property".equals(jsonPointer.getMatchingProperty()));

        // Test getMatchingIndex method
        assert (jsonPointer.getMatchingIndex() == -1);

        // Test mayMatchProperty method
        assert (jsonPointer.mayMatchProperty());

        // Test mayMatchElement method
        assert (jsonPointer.mayMatchElement());

        // Test tail method
        assert (jsonPointer.tail().equals(nextSegment));

        // Test toString method
        assert ("/property".equals(jsonPointer.toString()));

        // Test hashCode method
        assert ("/property".hashCode() == jsonPointer.hashCode());

        // Test equals method
        assert (jsonPointer.equals(jsonPointer));

        // Test matchProperty method
        assert (jsonPointer.matchProperty("property") == nextSegment);
        assert (jsonPointer.matchProperty("wrongProperty") == null);

        // Test matchElement method
        assert (jsonPointer.matchElement(0) == null);
    }

    @Test
    public void testJsonPointerWithIndex() {
        JsonPointer jsonPointer = new JsonPointer("/[0]");
        JsonPointer nextSegment = new JsonPointer();
        // Test nextSegment field
        assert (jsonPointer._nextSegment.equals(nextSegment));

        // Test getMatchingProperty method
        assert ("".equals(jsonPointer.getMatchingProperty()));

        // Test getMatchingIndex method
        assert (jsonPointer.getMatchingIndex() == 0);

        // Test mayMatchProperty method
        assert (!jsonPointer.mayMatchProperty());

        // Test mayMatchElement method
        assert (jsonPointer.mayMatchElement());

        // Test tail method
        assert (jsonPointer.tail().equals(nextSegment));

        // Test toString method
        assert ("/[0]".equals(jsonPointer.toString()));

        // Test hashCode method
        assert ("/[0]".hashCode() == jsonPointer.hashCode());

        // Test equals method
        assert (jsonPointer.equals(jsonPointer));

        // Test matchProperty method
        assert (jsonPointer.matchProperty("property") == null);

        // Test matchElement method
        assert (jsonPointer.matchElement(0) == nextSegment);
        assert (jsonPointer.matchElement(1) == null);
    }
}