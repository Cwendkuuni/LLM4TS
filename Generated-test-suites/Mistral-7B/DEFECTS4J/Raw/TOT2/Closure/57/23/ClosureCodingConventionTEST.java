import static org.junit.Assert.*;
import static com.google.javascript.jscomp.ClosureCodingConvention.*;

public class ClosureCodingConventionTest {
  @Test
  public void testApplySubclassRelationship_Inherits() {
    FunctionType parentCtor = new FunctionType("ParentCtor", ImmutableSet.of(), new ObjectType("ParentClass"));
    FunctionType childCtor = new FunctionType("ChildCtor", ImmutableSet.of(), new ObjectType("ChildClass"));
    SubclassType type = SubclassType.INHERITS;
    applySubclassRelationship(parentCtor, childCtor, type);
    assertEquals(parentCtor, childCtor.getPrototype().getConstructor());
    assertEquals(parentCtor, childCtor.getPrototype().getSuperclass());
  }

  @Test
  public void testApplySubclassRelationship_Mixin() {
    FunctionType parentCtor = new FunctionType("ParentCtor", ImmutableSet.of(), new ObjectType("ParentClass"));
    FunctionType childCtor = new FunctionType("ChildCtor", ImmutableSet.of(), new ObjectType("ChildClass"));
    SubclassType type = SubclassType.MIXIN;
    applySubclassRelationship(parentCtor, childCtor, type);
    assertEquals(parentCtor.getPrototype(), childCtor.getPrototype());
  }

  @Test(expected = AssertionError.class)
  public void testApplySubclassRelationship_InvalidSyntax() {
    FunctionType parentCtor = new FunctionType("ParentCtor", ImmutableSet.of(), new ObjectType("ParentClass"));
    FunctionType childCtor = new FunctionType("ChildCtor", ImmutableSet.of(), new ObjectType("ChildClass"));
    applySubclassRelationship(parentCtor, childCtor, null);
  }
}