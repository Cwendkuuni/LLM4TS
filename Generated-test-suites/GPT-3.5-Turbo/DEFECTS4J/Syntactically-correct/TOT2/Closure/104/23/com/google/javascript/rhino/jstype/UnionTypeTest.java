import static org.junit.Assert.*;
import org.junit.Test;

public class UnionTypeTest {

  @Test
  public void testForgiveUnknownNames() {
    // Test case for forgiveUnknownNames
    // Create UnionType instance
    UnionType unionType = new UnionType(registry, ImmutableSet.of(JSType.UNKNOWN));
    unionType.forgiveUnknownNames();
    
    // Assert that forgiveUnknownNames method works correctly
    assertTrue(unionType.matchesNumberContext());
    assertTrue(unionType.matchesStringContext());
    assertTrue(unionType.matchesObjectContext());
  }

  @Test
  public void testMatchesNumberContext() {
    // Test case for matchesNumberContext
    // Create UnionType instance
    UnionType unionType = new UnionType(registry, ImmutableSet.of(JSType.NUMBER));
    
    // Assert that matchesNumberContext returns true for NUMBER type
    assertTrue(unionType.matchesNumberContext());
  }

  @Test
  public void testMatchesStringContext() {
    // Test case for matchesStringContext
    // Create UnionType instance
    UnionType unionType = new UnionType(registry, ImmutableSet.of(JSType.STRING));
    
    // Assert that matchesStringContext returns true for STRING type
    assertTrue(unionType.matchesStringContext());
  }
  
  // Additional test cases for remaining methods can be added here

}