import static org.junit.Assert.*;
import org.junit.Test;

public class UnionTypeTest {

  @Test
  public void testCreateUnionType() {
    // Test creating a UnionType with alternate types
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType stringType = new JSType(registry);
    JSType booleanType = new JSType(registry);
    
    Set<JSType> alternates = new HashSet<>();
    alternates.add(stringType);
    alternates.add(booleanType);
    
    UnionType unionType = new UnionType(registry, alternates);
    
    assertNotNull(unionType);
  }

  @Test
  public void testGetAlternates() {
    // Test getting the alternate types of a UnionType
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType stringType = new JSType(registry);
    JSType numberType = new JSType(registry);
    
    Set<JSType> alternates = new HashSet<>();
    alternates.add(stringType);
    alternates.add(numberType);
    
    UnionType unionType = new UnionType(registry, alternates);
    
    Iterable<JSType> result = unionType.getAlternates();
    
    assertNotNull(result);
    assertEquals(2, Iterables.size(result)); // Check that there are 2 alternates
  }

  @Test
  public void testMatchesNumberContext() {
    // Test matching types in a numeric context
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType numberType = new JSType(registry);
    
    Set<JSType> alternates = new HashSet<>();
    alternates.add(numberType);
    
    UnionType unionType = new UnionType(registry, alternates);
    
    assertTrue(unionType.matchesNumberContext());
  }

  // Add more test cases for other public methods of UnionType
  
}