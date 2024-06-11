import static org.junit.Assert.*;
import org.junit.Test;

public class UnionTypeTest {

    @Test
    public void testUnionTypeCreation() {
        // Given
        JSTypeRegistry registry = new JSTypeRegistry();
        Set<JSType> alternates = new HashSet<>();
        alternates.add(new JSType("String"));
        alternates.add(new JSType("boolean"));
        
        // When
        UnionType unionType = new UnionType(registry, alternates);
        
        // Then
        assertNotNull(unionType);
    }

    @Test
    public void testGetAlternates() {
        // Given
        JSTypeRegistry registry = new JSTypeRegistry();
        Set<JSType> alternates = new HashSet<>();
        alternates.add(new JSType("String"));
        alternates.add(new JSType("boolean"));
        UnionType unionType = new UnionType(registry, alternates);
        
        // When
        Iterable<JSType> result = unionType.getAlternates();
        
        // Then
        assertNotNull(result);
        assertTrue(result.iterator().hasNext());
    }

    @Test
    public void testMatchesNumberContext() {
        // Given
        JSTypeRegistry registry = new JSTypeRegistry();
        Set<JSType> alternates = new HashSet<>();
        alternates.add(new JSType("Number"));
        UnionType unionType = new UnionType(registry, alternates);
        
        // When
        boolean result = unionType.matchesNumberContext();
        
        // Then
        assertTrue(result);
    }

    // Add more test cases for other methods...

}