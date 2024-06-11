import org.junit.Test;
import static org.junit.Assert.*;

public class UnionTypeTest {

    @Test
    public void testCreateUnionType() {
        // Test creating a union type with two alternates
        Set<JSType> alternates = new HashSet<>();
        alternates.add(new StringType());
        alternates.add(new BooleanType());

        UnionType unionType = new UnionType(new JSTypeRegistry(), alternates);

        assertNotNull(unionType);
    }

    @Test
    public void testGetAlternates() {
        // Test getting alternate types from a union type
        Set<JSType> alternates = new HashSet<>();
        alternates.add(new StringType());
        alternates.add(new NumberType());

        UnionType unionType = new UnionType(new JSTypeRegistry(), alternates);

        Iterable<JSType> result = unionType.getAlternates();

        assertNotNull(result);
    }

    @Test
    public void testMatchesNumberContext() {
        // Test matching number context for a union type
        Set<JSType> alternates = new HashSet<>();
        alternates.add(new NumberType());
        alternates.add(new StringType());

        UnionType unionType = new UnionType(new JSTypeRegistry(), alternates);

        boolean result = unionType.matchesNumberContext();

        assertTrue(result);
    }

    @Test
    public void testMatchesStringContext() {
        // Test matching string context for a union type
        Set<JSType> alternates = new HashSet<>();
        alternates.add(new StringType());
        alternates.add(new ObjectType());

        UnionType unionType = new UnionType(new JSTypeRegistry(), alternates);

        boolean result = unionType.matchesStringContext();

        assertTrue(result);
    }

    @Test
    public void testMatchesObjectContext() {
        // Test matching object context for a union type
        Set<JSType> alternates = new HashSet<>();
        alternates.add(new ArrayType());
        alternates.add(new ObjectType());

        UnionType unionType = new UnionType(new JSTypeRegistry(), alternates);

        boolean result = unionType.matchesObjectContext();

        assertTrue(result);
    }
}