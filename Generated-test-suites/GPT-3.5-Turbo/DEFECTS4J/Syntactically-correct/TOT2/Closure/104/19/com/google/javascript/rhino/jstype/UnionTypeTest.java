import org.junit.Test;
import static org.junit.Assert.*;

public class UnionTypeTest {
  
  @Test
  public void testCreateUnionType() {
    // Test creating a UnionType with multiple alternates
    JSType stringType = new StringType();
    JSType numberType = new NumberType();
    Set<JSType> alternates = new HashSet<>();
    alternates.add(stringType);
    alternates.add(numberType);
    
    UnionType unionType = new UnionType(alternates);
    
    assertNotNull(unionType);
    assertEquals(2, unionType.getAlternates().size());
  }
  
  @Test
  public void testMatchesNumberContext() {
    // Test matching number context for a UnionType with number and string alternates
    JSType stringType = new StringType();
    JSType numberType = new NumberType();
    Set<JSType> alternates = new HashSet<>();
    alternates.add(stringType);
    alternates.add(numberType);
    
    UnionType unionType = new UnionType(alternates);
    
    assertTrue(unionType.matchesNumberContext());
  }
  
  @Test
  public void testMatchesStringContext() {
    // Test matching string context for a UnionType with boolean and string alternates
    JSType booleanType = new BooleanType();
    JSType stringType = new StringType();
    Set<JSType> alternates = new HashSet<>();
    alternates.add(booleanType);
    alternates.add(stringType);
    
    UnionType unionType = new UnionType(alternates);
    
    assertTrue(unionType.matchesStringContext());
  }
  
  @Test
  public void testCanAssignTo() {
    // Test assigning a UnionType with null and boolean alternates to a BooleanType
    JSType nullType = new NullType();
    JSType booleanType = new BooleanType();
    Set<JSType> alternates = new HashSet<>();
    alternates.add(nullType);
    alternates.add(booleanType);
    
    UnionType unionType = new UnionType(alternates);
    BooleanType targetType = new BooleanType();
    
    assertTrue(unionType.canAssignTo(targetType));
  }
}