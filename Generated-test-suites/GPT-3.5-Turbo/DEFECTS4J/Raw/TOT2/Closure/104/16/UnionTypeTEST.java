import org.junit.Test;
import static org.junit.Assert.*;
import java.util.HashSet;
import java.util.Set;

public class UnionTypeTest {

    @Test
    public void testCreateUnionType() {
        Set<JSType> alternates = new HashSet<>();
        alternates.add(new StringType());
        alternates.add(new BooleanType());
        UnionType unionType = new UnionType(new JSTypeRegistry(), alternates);

        assertNotNull(unionType);
        assertEquals(2, unionType.getAlternates().size());
    }

    @Test
    public void testMatchesNumberContext() {
        Set<JSType> alternates = new HashSet<>();
        alternates.add(new NumberType());
        alternates.add(new BooleanType());
        UnionType unionType = new UnionType(new JSTypeRegistry(), alternates);

        assertTrue(unionType.matchesNumberContext());
    }

    @Test
    public void testFindPropertyType() {
        Set<JSType> alternates = new HashSet<>();
        alternates.add(new ObjectType());
        alternates.add(new StringType());
        UnionType unionType = new UnionType(new JSTypeRegistry(), alternates);

        JSType propertyType = unionType.findPropertyType("test");
        assertNull(propertyType);
    }

    // Add more test cases for other methods in UnionType class

}