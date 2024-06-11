import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.NativeObject;
import com.google.javascript.rhino.testing.TypeChecker;
import com.google.javascript.rhino.testing.TypeCheckFailure;
import junit.framework.Assert;

import java.util.List;

public class NamedTypeTest {

  public static void main(String[] args) {
    TypeChecker typeChecker = new TypeChecker();

    JSTypeRegistry registry = typeChecker.getTypeRegistry();
    String reference = "MyType";
    String sourceName = "NamedTypeTest.java";
    int lineno = 10;
    int charno = 5;

    // Test creating a named type with an unresolved reference
    JSType unresolvedNamedType = new NamedType(registry, reference, sourceName, lineno, charno);
    Assert.assertFalse(unresolvedNamedType.isResolved());

    // Test that defining a property on an unresolved named type is deferred
    unresolvedNamedType.defineProperty("prop", NativeObject.PROTOTYPE, false, null);
    Assert.assertNull(unresolvedNamedType.get("prop"));

    // Test resolving a named type via the registry
    JSType resolvedNamedType = registry.getType(reference);
    if (resolvedNamedType != null) {
      JSType proxy = new NamedType(registry, reference, sourceName, lineno, charno);
      proxy.setReferencedType(resolvedNamedType);
      Assert.assertEquals(resolvedNamedType, proxy.getReferencedType());
      Assert.assertTrue(proxy.isResolved());
    }

    // Test resolving a named type via properties
    JSType parentType = new ObjectType(registry, new JSType[0]);
    parentType.defineProperty("child", unresolvedNamedType, false, null);
    JSType resolvedParentType = new ObjectType(registry, new JSType[0]);
    resolvedParentType.setReferencedAndResolvedType(parentType);

    JSType resolvedChildType = resolvedParentType.getPropertyType("child");
    Assert.assertNotNull(resolvedChildType);
    Assert.assertEquals(unresolvedNamedType, resolvedChildType.getReferencedType());
    Assert.assertTrue(resolvedChildType.isResolved());

    // Test that defining a property on a resolved named type is not deferred
    resolvedChildType.defineProperty("prop", NativeObject.PROTOTYPE, false, null);
    Assert.assertNotNull(resolvedChildType.get("prop"));

    // Test error cases
    try {
      unresolvedNamedType.resolveInternal(null, null);
      Assert.fail("Expected error when resolving unresolved named type.");
    } catch (TypeCheckFailure e) {
      // Expected
    }

    try {
      unresolvedNamedType.defineProperty("prop", resolvedNamedType, false, null);
      Assert.fail("Expected error when defining property on unresolved named type.");
    } catch (IllegalStateException e) {
      // Expected
    }

    try {
      unresolvedNamedType.getPropertyType("prop");
      Assert.fail("Expected error when accessing property on unresolved named type.");
    } catch (IllegalStateException e) {
      // Expected
    }
  }
}