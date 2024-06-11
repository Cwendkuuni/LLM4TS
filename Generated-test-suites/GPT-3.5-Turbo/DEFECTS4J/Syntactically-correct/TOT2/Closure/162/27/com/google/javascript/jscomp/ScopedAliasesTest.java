import static org.junit.Assert.*;
import org.junit.Test;

public class ScopedAliasesTest {
  
  @Test
  public void testSCOPING_METHOD_NAME() {
    ScopedAliases scopedAliases = new ScopedAliases();
    assertEquals("goog.scope", ScopedAliases.SCOPING_METHOD_NAME);
  }
  
  @Test
  public void testAliasTransformationHandler() {
    ScopedAliases scopedAliases = new ScopedAliases();
    assertNotNull(scopedAliases.transformationHandler);
  }
  
  @Test
  public void testGOOG_SCOPE_USED_IMPROPERLY() {
    ScopedAliases scopedAliases = new ScopedAliases();
    assertFalse(ScopedAliases.GOOG_SCOPE_USED_IMPROPERLY.equals(null));
  }
  
  @Test
  public void testGOOG_SCOPE_HAS_BAD_PARAMETERS() {
    ScopedAliases scopedAliases = new ScopedAliases();
    assertFalse(ScopedAliases.GOOG_SCOPE_HAS_BAD_PARAMETERS.equals(null));
  }
  
  @Test
  public void testGOOG_SCOPE_REFERENCES_THIS() {
    ScopedAliases scopedAliases = new ScopedAliases();
    assertFalse(ScopedAliases.GOOG_SCOPE_REFERENCES_THIS.equals(null));
  }
  
  @Test
  public void testGOOG_SCOPE_USES_RETURN() {
    ScopedAliases scopedAliases = new ScopedAliases();
    assertFalse(ScopedAliases.GOOG_SCOPE_USES_RETURN.equals(null));
  }
  
  @Test
  public void testGOOG_SCOPE_USES_THROW() {
    ScopedAliases scopedAliases = new ScopedAliases();
    assertFalse(ScopedAliases.GOOG_SCOPE_USES_THROW.equals(null));
  }
  
  @Test
  public void testGOOG_SCOPE_ALIAS_REDEFINED() {
    ScopedAliases scopedAliases = new ScopedAliases();
    assertFalse(ScopedAliases.GOOG_SCOPE_ALIAS_REDEFINED.equals(null));
  }
  
  @Test
  public void testGOOG_SCOPE_NON_ALIAS_LOCAL() {
    ScopedAliases scopedAliases = new ScopedAliases();
    assertFalse(ScopedAliases.GOOG_SCOPE_NON_ALIAS_LOCAL.equals(null));
  }
}