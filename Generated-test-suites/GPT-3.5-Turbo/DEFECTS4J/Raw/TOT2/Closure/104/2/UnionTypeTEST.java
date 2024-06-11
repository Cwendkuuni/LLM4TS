import static org.junit.Assert.*;
import org.junit.Test;

public class UnionTypeTest {

    @Test
    public void testConstructor() {
        // typical use case
        Set<JSType> alternates = new HashSet<>();
        alternates.add(new StringType());
        alternates.add(new BooleanType());
        UnionType unionType = new UnionType(alternates);
        
        assertNotNull(unionType);
        assertEquals(alternates, unionType.getAlternates());
    }
    
    @Test
    public void testMatchesNumberContext() {
        // typical use case
        Set<JSType> alternates = new HashSet<>();
        alternates.add(new NumberType());
        UnionType unionType = new UnionType(alternates);
        
        assertTrue(unionType.matchesNumberContext());
        
        // edge case
        Set<JSType> emptyAlternates = new HashSet<>();
        UnionType emptyUnionType = new UnionType(emptyAlternates);
        
        assertFalse(emptyUnionType.matchesNumberContext());
    }
    
    @Test
    public void testMatchesStringContext() {
        // typical use case
        Set<JSType> alternates = new HashSet<>();
        alternates.add(new StringType());
        UnionType unionType = new UnionType(alternates);
        
        assertTrue(unionType.matchesStringContext());
        
        // edge case
        Set<JSType> emptyAlternates = new HashSet<>();
        UnionType emptyUnionType = new UnionType(emptyAlternates);
        
        assertFalse(emptyUnionType.matchesStringContext());
    }
    
    // Add more test cases for other methods
    
    @Test
    public void testToString() {
        // typical use case
        Set<JSType> alternates = new HashSet<>();
        alternates.add(new StringType());
        alternates.add(new NumberType());
        UnionType unionType = new UnionType(alternates);
        
        assertEquals("(String|Number)", unionType.toString());
    }
}