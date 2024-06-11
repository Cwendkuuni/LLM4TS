import org.junit.Test;
import static org.junit.Assert.*;

public class UnionTypeTest {

    @Test
    public void testCreateUnionType() {
        // Typical use case
        UnionType unionType = new UnionType(null, ImmutableSet.of(JSType.TRUE_TYPE, JSType.FALSE_TYPE));

        // Edge case - UnionType with single alternate
        UnionType singleAlternateType = new UnionType(null, ImmutableSet.of(JSType.TRUE_TYPE));
        assertNotNull(singleAlternateType);
    }

    @Test
    public void testMatchesNumberContext() {
        // Typical use case - UnionType with numeric context
        UnionType numberUnionType = new UnionType(null, ImmutableSet.of(JSType.NUMBER_TYPE, JSType.STRING_TYPE));
        assertTrue(numberUnionType.matchesNumberContext());

        // Edge case - UnionType without numeric context
        UnionType stringUnionType = new UnionType(null, ImmutableSet.of(JSType.STRING_TYPE, JSType.BOOLEAN_TYPE));
        assertFalse(stringUnionType.matchesNumberContext());
    }

    @Test
    public void testMatchesStringContext() {
        // Typical use case - UnionType with string context
        UnionType stringUnionType = new UnionType(null, ImmutableSet.of(JSType.STRING_TYPE, JSType.BOOLEAN_TYPE));
        assertTrue(stringUnionType.matchesStringContext());

        // Edge case - UnionType without string context
        UnionType numberUnionType = new UnionType(null, ImmutableSet.of(JSType.NUMBER_TYPE, JSType.BOOLEAN_TYPE));
        assertFalse(numberUnionType.matchesStringContext());
    }

    @Test
    public void testMatchesObjectContext() {
        // Typical use case - UnionType with object context
        UnionType objectUnionType = new UnionType(null, ImmutableSet.of(JSType.OBJECT_TYPE, JSType.ARRAY_TYPE));
        assertTrue(objectUnionType.matchesObjectContext());

        // Edge case - UnionType without object context
        UnionType primitiveUnionType = new UnionType(null, ImmutableSet.of(JSType.STRING_TYPE, JSType.NUMBER_TYPE));
        assertFalse(primitiveUnionType.matchesObjectContext());
    }

    // Add additional test cases for other methods as needed

}