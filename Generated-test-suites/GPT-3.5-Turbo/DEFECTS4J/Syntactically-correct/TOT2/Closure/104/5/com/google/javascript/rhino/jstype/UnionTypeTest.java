import org.junit.Test;
import static org.junit.Assert.*;

public class UnionTypeTest {

    @Test
    public void testCreateUnionType() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Set<JSType> alternates = new HashSet<>();
        alternates.add(new StringType(registry));
        alternates.add(new BooleanType(registry));
        UnionType unionType = new UnionType(registry, alternates);

        assertNotNull(unionType);
    }

    @Test
    public void testGetAlternates() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Set<JSType> alternates = new HashSet<>();
        alternates.add(new StringType(registry));
        alternates.add(new BooleanType(registry));
        UnionType unionType = new UnionType(registry, alternates);

        assertEquals(2, unionType.getAlternates().size());
    }

    @Test
    public void testMatchesNumberContext() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Set<JSType> alternates = new HashSet<>();
        alternates.add(new NumberType(registry));
        alternates.add(new BooleanType(registry));
        UnionType unionType = new UnionType(registry, alternates);

        assertTrue(unionType.matchesNumberContext());
    }

    @Test
    public void testMatchesStringContext() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Set<JSType> alternates = new HashSet<>();
        alternates.add(new NumberType(registry));
        alternates.add(new StringType(registry));
        UnionType unionType = new UnionType(registry, alternates);

        assertTrue(unionType.matchesStringContext());
    }

    @Test
    public void testIsSubtype() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Set<JSType> alternates = new HashSet<>();
        alternates.add(new ObjectType(registry));
        alternates.add(new StringType(registry));
        UnionType unionType = new UnionType(registry, alternates);

        assertTrue(unionType.isSubtype(new ObjectType(registry)));
    }

    @Test
    public void testIsNullable() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Set<JSType> alternates = new HashSet<>();
        alternates.add(new NumberType(registry));
        alternates.add(new NullType(registry));
        UnionType unionType = new UnionType(registry, alternates);

        assertTrue(unionType.isNullable());
    }

    @Test
    public void testEquals() {
        JSTypeRegistry registry = new JSTypeRegistry();
        Set<JSType> alternates = new HashSet<>();
        alternates.add(new StringType(registry));
        alternates.add(new NumberType(registry));
        UnionType unionType1 = new UnionType(registry, alternates);

        Set<JSType> alternates2 = new HashSet<>();
        alternates2.add(new StringType(registry));
        alternates2.add(new NumberType(registry));
        UnionType unionType2 = new UnionType(registry, alternates2);

        assertEquals(unionType1, unionType2);
    }
}