import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Set;
import java.util.HashSet;

public class UnionTypeTest {

    @Test
    public void testGetAlternates() {
        Set<JSType> alternates = new HashSet<>();
        alternates.add(new JSType());
        
        UnionType unionType = new UnionType(null, alternates);
        
        assertEquals(alternates, unionType.getAlternates());
    }

    @Test
    public void testMatchesNumberContext() {
        Set<JSType> alternates = new HashSet<>();
        alternates.add(new JSType());
        
        UnionType unionType = new UnionType(null, alternates);
        
        assertFalse(unionType.matchesNumberContext());
    }

    @Test
    public void testMatchesStringContext() {
        Set<JSType> alternates = new HashSet<>();
        alternates.add(new JSType());
        
        UnionType unionType = new UnionType(null, alternates);
        
        assertFalse(unionType.matchesStringContext());
    }

    @Test
    public void testMatchesObjectContext() {
        Set<JSType> alternates = new HashSet<>();
        alternates.add(new JSType());
        
        UnionType unionType = new UnionType(null, alternates);
        
        assertFalse(unionType.matchesObjectContext());
    }

    // Add more test cases for other methods in UnionType
}