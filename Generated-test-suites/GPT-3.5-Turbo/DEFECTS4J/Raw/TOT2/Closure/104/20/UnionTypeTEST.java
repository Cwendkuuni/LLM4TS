import static org.junit.Assert.*;
import org.junit.Test;

public class UnionTypeTest {

  @Test
  public void testCreateUnionType() {
    JSType type1 = new ObjectType();
    JSType type2 = new BooleanType();
    Set<JSType> alternates = new HashSet<>();
    alternates.add(type1);
    alternates.add(type2);
    UnionType unionType = new UnionType(alternates);

    assertNotNull(unionType);
    assertEquals(type1, unionType.getAlternates().get(0));
    assertEquals(type2, unionType.getAlternates().get(1));
  }

  @Test
  public void testMatchesNumberContext() {
    JSType type1 = new NumberType();
    JSType type2 = new StringType();
    Set<JSType> alternates = new HashSet<>();
    alternates.add(type1);
    alternates.add(type2);
    UnionType unionType = new UnionType(alternates);

    assertTrue(unionType.matchesNumberContext());
  }

  @Test
  public void testMatchesStringContext() {
    JSType type1 = new StringType();
    JSType type2 = new ObjectType();
    Set<JSType> alternates = new HashSet<>();
    alternates.add(type1);
    alternates.add(type2);
    UnionType unionType = new UnionType(alternates);

    assertTrue(unionType.matchesStringContext());
  }

  @Test
  public void testMatchesObjectContext() {
    JSType type1 = new ObjectType();
    JSType type2 = new NullType();
    Set<JSType> alternates = new HashSet<>();
    alternates.add(type1);
    alternates.add(type2);
    UnionType unionType = new UnionType(alternates);

    assertTrue(unionType.matchesObjectContext());
  }

  // Add more test cases for other methods in UnionType

}