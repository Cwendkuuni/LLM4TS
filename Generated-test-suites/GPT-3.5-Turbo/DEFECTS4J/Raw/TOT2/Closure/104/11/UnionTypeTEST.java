import static org.junit.Assert.*;
import org.junit.Test;

public class UnionTypeTest {

    @Test
    public void testGetAlternates() {
        JSType stringType = new JSType("String");
        JSType booleanType = new JSType("boolean");
        Set<JSType> alternates = new HashSet<>();
        alternates.add(stringType);
        alternates.add(booleanType);

        UnionType unionType = new UnionType(alternates);
        assertEquals(alternates, unionType.getAlternates());
    }

    @Test
    public void testForgiveUnknownNames() {
        // Test case for forgiving unknown names
    }

    // Add more test cases for other public methods

    @Test
    public void testIsSubtype() {
        JSType stringType = new JSType("String");
        JSType nullType = new JSType("null");

        Set<JSType> alternates = new HashSet<>();
        alternates.add(stringType);
        alternates.add(nullType);

        UnionType unionType = new UnionType(alternates);

        assertTrue(unionType.isSubtype(stringType));
        assertTrue(unionType.isSubtype(nullType));
    }

    // Add more test cases for edge cases and error scenarios

}