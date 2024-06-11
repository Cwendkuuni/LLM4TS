import static org.junit.Assert.*;
import org.junit.Test;

public class UnionTypeTest {

  @Test
  public void testCreateUnionType() {
    Set<JSType> alternates = new HashSet<>();
    JSType type1 = new JSType();
    JSType type2 = new JSType();
    alternates.add(type1);
    alternates.add(type2);

    UnionType unionType = new UnionType(registry, alternates);
    
    assertNotNull(unionType);
  }

  @Test
  public void testGetAlternates() {
    Set<JSType> alternates = new HashSet<>();
    JSType type1 = new JSType();
    JSType type2 = new JSType();
    alternates.add(type1);
    alternates.add(type2);

    UnionType unionType = new UnionType(registry, alternates);
    
    Iterable<JSType> result = unionType.getAlternates();
    
    assertNotNull(result);
    assertTrue(result.iterator().hasNext());
  }

  @Test
  public void testFindPropertyType() {
    Set<JSType> alternates = new HashSet<>();
    JSType type1 = new JSType();
    JSType type2 = new JSType();
    alternates.add(type1);
    alternates.add(type2);

    UnionType unionType = new UnionType(registry, alternates);
    
    JSType result = unionType.findPropertyType("testProperty");
    
    assertNull(result);
  }

  @Test
  public void testCanAssignTo() {
    Set<JSType> alternates = new HashSet<>();
    JSType type1 = new JSType();
    JSType type2 = new JSType();
    alternates.add(type1);
    alternates.add(type2);

    UnionType unionType = new UnionType(registry, alternates);
    
    JSType otherType = new JSType();
    
    boolean result = unionType.canAssignTo(otherType);
    
    assertFalse(result);
  }

  @Test
  public void testMatchesNumberContext() {
    Set<JSType> alternates = new HashSet<>();
    JSType type1 = new JSType();
    JSType type2 = new JSType();
    alternates.add(type1);
    alternates.add(type2);

    UnionType unionType = new UnionType(registry, alternates);
    
    boolean result = unionType.matchesNumberContext();
    
    assertFalse(result);
  }

  @Test
  public void testMatchesStringContext() {
    Set<JSType> alternates = new HashSet<>();
    JSType type1 = new JSType();
    JSType type2 = new JSType();
    alternates.add(type1);
    alternates.add(type2);

    UnionType unionType = new UnionType(registry, alternates);
    
    boolean result = unionType.matchesStringContext();
    
    assertFalse(result);
  }

  @Test
  public void testMatchesObjectContext() {
    Set<JSType> alternates = new HashSet<>();
    JSType type1 = new JSType();
    JSType type2 = new JSType();
    alternates.add(type1);
    alternates.add(type2);

    UnionType unionType = new UnionType(registry, alternates);
    
    boolean result = unionType.matchesObjectContext();
    
    assertFalse(result);
  }

  @Test
  public void testIsNullable() {
    Set<JSType> alternates = new HashSet<>();
    JSType type1 = new JSType();
    JSType type2 = new JSType();
    alternates.add(type1);
    alternates.add(type2);

    UnionType unionType = new UnionType(registry, alternates);
    
    boolean result = unionType.isNullable();
    
    assertFalse(result);
  }
}