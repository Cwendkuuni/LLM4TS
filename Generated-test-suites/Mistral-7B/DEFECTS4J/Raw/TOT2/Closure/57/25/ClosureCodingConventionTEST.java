import static org.junit.Assert.assertThat;
import static com.google.common.collect.ImmutableSet.of;
import static com.google.javascript.jscomp.ClosureCodingConvention.OBJECTLIT_EXPECTED;

import org.junit.Test;

public class ClosureCodingConventionTest {

  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = new FunctionType("ParentCtor");
    FunctionType childCtor = new FunctionType("ChildCtor");
    SubclassType type = SubclassType.INHERITS;

    ClosureCodingConvention convention = new ClosureCodingConvention();
    convention.applySubclassRelationship(parentCtor, childCtor, type);

    assertThat(childCtor.getDeclaredProperties(), hasProperty("superClass_"));
    assertThat(childCtor.getPrototype().getDeclaredProperties(),
        hasProperty("constructor"));

    FunctionType checkCtor = new FunctionType("CheckCtor");
    assertThat(checkCtor.getPrototype().getDeclaredProperties(), empty());

    convention.applySubclassRelationship(parentCtor, checkCtor, type);
    assertThat(checkCtor.getPrototype().getDeclaredProperties(),
        hasProperty("constructor"));
  }

  @Test
  public void testApplySubclassRelationship_WithInvalidCall() {
    FunctionType parentCtor = new FunctionType("ParentCtor");
    FunctionType childCtor = new FunctionType("ChildCtor");

    ClosureCodingConvention convention = new ClosureCodingConvention();

    try {
      convention.applySubclassRelationship(parentCtor, childCtor, null);
      fail();
    } catch (IllegalArgumentException e) {
      // Expected
    }

    try {
      convention.applySubclassRelationship(parentCtor, childCtor, type);
      childCtor.setPrototype(null);
      convention.applySubclassRelationship(parentCtor, childCtor, type);
      fail();
    } catch (IllegalArgumentException e) {
      // Expected
    }
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = new Node("goog", "provide", ImmutableList.of(), true);
    String className = new ClosureCodingConvention().extractClassNameIfProvide(node, null);
    assertEquals("goog", className);

    node.setFirstChild(new Node("goog.my", null, ImmutableList.of(), true));
    className = new ClosureCodingConvention().extractClassNameIfProvide(node, null);
    assertEquals("goog.my", className);
  }

  // Add other test cases here...
}