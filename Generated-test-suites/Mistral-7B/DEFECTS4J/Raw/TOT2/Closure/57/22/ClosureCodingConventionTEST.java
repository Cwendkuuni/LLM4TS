import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class ClosureCodingConventionTest {

  private ClosureCodingConvention codingConvention;
  private FunctionType parentCtor;
  private FunctionType childCtor;

  @Before
  public void setup() {
    codingConvention = new ClosureCodingConvention();
    parentCtor = mock(FunctionType.class);
    childCtor = mock(FunctionType.class);
  }

  @Test
  public void testApplySubclassRelationship_Inherits() {
    codingConvention.applySubclassRelationship(parentCtor, childCtor, SubclassType.INHERITS);
    assertNotNull(childCtor.getProperty("superClass_"));
    assertNotNull(childCtor.getProperty("constructor"));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testApplySubclassRelationship_NullParentCtor() {
    codingConvention.applySubclassRelationship(null, childCtor, SubclassType.INHERITS);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testApplySubclassRelationship_NullChildCtor() {
    codingConvention.applySubclassRelationship(parentCtor, null, SubclassType.INHERITS);
  }
}