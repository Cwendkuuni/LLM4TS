import org.junit.Test;
import static org.junit.Assert.*;

public class ClosureCodingConventionTest {
  @Test
  public void testApplySubclassRelationship() {
    FunctionType parentCtor = new FunctionType("ParentCtor", VOID_TYPE, ImmutableList.of());
    FunctionType childCtor = new FunctionType("ChildCtor", VOID_TYPE, ImmutableList.of());
    SubclassType type = SubclassType.INHERITS;
    ClosureCodingConvention convention = new ClosureCodingConvention();

    convention.applySubclassRelationship(parentCtor, childCtor, type);

    assertEquals("ParentCtor", childCtor.getPrototype().getDeclaredProperties().get("superClass_").getType().getName());
    assertEquals("ParentCtor", childCtor.getPrototype().getDeclaredProperties().get("constructor").getType().getName());
  }

  @Test
  public void testExtractClassNameIfProvide() {
    Node node = NodeFactory.createCall("goog.provide", ImmutableList.of("X"));
    String className = new ClosureCodingConvention().extractClassNameIfProvide(node, null);
    assertEquals("X", className);
  }

  @Test
  public void testIsSuperClassReference() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertTrue(convention.isSuperClassReference("superClass_"));
  }

  @Test
  public void testGetGlobalObject() {
    ClosureCodingConvention convention = new ClosureCodingConvention();
    assertEquals("goog.global", convention.getGlobalObject());
  }
}