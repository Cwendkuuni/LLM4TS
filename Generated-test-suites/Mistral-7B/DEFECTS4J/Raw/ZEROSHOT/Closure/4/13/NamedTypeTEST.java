import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.*;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.analysis.Type;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.NamedType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.StaticSlot;
import com.google.javascript.rhino.jstype.TypeFactory;
import com.google.javascript.rhino.testing.BaseTest;
import com.google.javascript.rhino.testing.JSTypeTestUtils;

public class NamedTypeTest extends BaseTest {

  private static final ErrorReporter ERR_REPORTER = new ErrorReporter() {
    @Override
    public void warning(String message, String sourceName, int lineNo,
        int charPos) {
      System.out.println("warning: " + message);
    }

    @Override
    public void fatal(String message, String sourceName, int lineNo,
        int charPos) {
      fail("fatal: " + message);
    }

    @Override
    public void error(String message, String sourceName, int lineNo,
        int charPos) {
      fail("error: " + message);
    }

    @Override
    public void error(String message, int lineNo, int charPos) {
      fail("error: " + message);
    }
  };

  private static JSTypeRegistry registry = JSTypeTestUtils.createTypeRegistry();

  @BeforeClass
  public static void setup() {
    registry.init();
  }

  @AfterClass
  public static void teardown() {
    registry.shutdown();
  }

  @Test
  public void testNamedTypeCreation() {
    String reference = "myType";
    NamedType namedType = new NamedType(registry, reference, "MyFile.js", 1, 1);
    JSType expectedType = registry.getType(reference);
    assertEquals(expectedType, namedType.getReferencedType());
  }

  @Test
  public void testNamedTypeResolveViaRegistry() {
    String reference = "Object";
    String sourceName = "MyFile.js";
    int lineno = 1;
    int charno = 1;

    // Create the type that the NamedType refers to
    ObjectType referencedType = new ObjectType(registry, "MyObjectType");
    Type referencedTypeType = TypeFactory.createObjectType(
        registry, "MyObjectType", null, null);
    referencedType.setType(referencedTypeType);

    // Create the NamedType
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    // Simulate the registry lookup
    JSType resolvedType = registry.getType(reference);
    assertEquals(referencedTypeType, resolvedType.getType());
    assertEquals(referencedType, namedType.getReferencedType());
  }

  @Test
  public void testNamedTypeResolveViaProperties() {
    String reference = "myType.nestedType";
    String sourceName = "MyFile.js";
    int lineno = 1;
    int charno = 1;

    // Create the parent type
    ObjectType parentType = new ObjectType(registry, "MyParentType");
    Type parentTypeType = TypeFactory.createObjectType(
        registry, "MyParentType", null, null);
    parentType.setType(parentTypeType);

    // Create a property on the parent type
    String propertyName = "nestedType";
    Type propertyType = TypeFactory.createObjectType(
        registry, "MyNestedType", null, null);
    parentType.defineProperty(
        propertyName, propertyType, false, null);
    StaticSlot<JSType> slot = new StaticSlot<>(parentType, propertyName);

    // Create the NamedType
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    // Simulate the property lookup
    JSType resolvedType = namedType.resolveInternal(ERR_REPORTER, null);
    assertEquals(propertyType.getType(), resolvedType.getType());
    assertEquals(propertyType, namedType.getReferencedType());
  }

  @Test
  public void testNamedTypeResolveViaRegistryUnresolved() {
    String reference = "myType";
    String sourceName = "MyFile.js";
    int lineno = 1;
    int charno = 1;

    // Create the NamedType
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    // Simulate the registry lookup failure
    JSType resolvedType = null;
    assertEquals(NO_TYPE, resolvedType);
    assertEquals(NO_TYPE, namedType.getReferencedType());
  }

  @Test
  public void testNamedTypeResolveViaPropertiesUnresolved() {
    String reference = "myType.nestedType";
    String sourceName = "MyFile.js";
    int lineno = 1;
    int charno = 1;

    // Create the parent type
    ObjectType parentType = new ObjectType(registry, "MyParentType");

    // Create the NamedType
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    // Simulate the property lookup failure
    StaticScope<JSType> enclosing = null;
    JSType resolvedType = null;
    assertEquals(NO_TYPE, resolvedType);
    assertEquals(NO_TYPE, namedType.getReferencedType());
  }

  @Test
  public void testNamedTypeImplicitPrototypeCycle() {
    String reference = "ParentType.ChildType";
    String sourceNameParent = "ParentFile.js";
    int linenoParent = 1;
    int charnoParent = 1;

    String referenceChild = "ChildType.ParentType";
    String sourceNameChild = "ChildFile.js";
    int linenoChild = 1;
    int charnoChild = 1;

    // Create the first parent type
    ObjectType parentType = new ObjectType(registry, "ParentType");
    Type parentTypeType = TypeFactory.createObjectType(
        registry, "ParentType", null, null);
    parentType.setType(parentTypeType);

    // Create a property on the parent type
    String propertyNameChild = "ChildType";
    Type childType = TypeFactory.createObjectType(
        registry, "ChildType", null, null);
    parentType.defineProperty(
        propertyNameChild, childType, false, null);
    StaticSlot<JSType> slotParent = new StaticSlot<>(parentType, propertyNameChild);

    // Create the child type
    ObjectType childTypeObj = new ObjectType(registry, "ChildType");
    Type childTypeType = TypeFactory.createObjectType(
        registry, "ChildType", null, null);
    childTypeObj.setType(childTypeType);

    // Create a property on the child type
    String propertyNameParent = "ParentType";
    Type parentTypeObj = TypeFactory.createObjectType(
        registry, "ParentType", null, null);
    childTypeObj.defineProperty(
        propertyNameParent, parentTypeObj, false, null);
    StaticSlot<JSType> slotChild = new StaticSlot<>(childTypeObj, propertyNameParent);

    // Create the first NamedType
    NamedType namedParentType = new NamedType(registry, reference, sourceNameParent, linenoParent, charnoParent);

    // Create the second NamedType
    NamedType namedChildType = new NamedType(registry, referenceChild, sourceNameChild, linenoChild, charnoChild);

    // Simulate the property lookup on the first NamedType
    JSType resolvedParentType = namedParentType.resolveInternal(ERR_REPORTER, null);
    assertEquals(parentTypeType, resolvedParentType.getType());
    assertEquals(parentType, namedParentType.getReferencedType());

    // Set the referenced type on the first NamedType
    namedParentType.setReferencedTypeInternal(parentType);

    // Simulate the property lookup on the second NamedType
    JSType resolvedChildType = namedChildType.resolveInternal(ERR_REPORTER, null);
    assertEquals(childTypeType, resolvedChildType.getType());
    assertEquals(childType, namedChildType.getReferencedType());

    // Verify that a type cycle warning was emitted
    String expectedWarning = "Cycle detected in inheritance chain of type " + reference;
    assertTrue(ERR_REPORTER.getWarnings().contains(expectedWarning));
  }

  @Test
  public void testNamedTypeEnumElementCycle() {
    String reference = "MyEnum.MyEnumElement";
    String sourceName = "MyFile.js";
    int lineno = 1;
    int charno = 1;

    // Create the enum type
    EnumType enumType = new EnumType(registry, "MyEnum", null, null);
    Type enumTypeType = TypeFactory.createEnumType(
        registry, "MyEnum", null, null, null);
    enumType.setType(enumTypeType);

    // Create an enum element
    String enumElementName = "MyEnumElement";
    EnumElementType enumElementType = new EnumElementType(
        registry, enumType, enumElementName, null, null, null);
    Type enumElementTypeType = TypeFactory.createEnumElementType(
        registry, enumTypeType, enumElementName, null, null);
    enumElementType.setPrimitiveType(enumType);
    enumElementType.setType(enumElementTypeType);

    // Create the NamedType
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    // Simulate the property lookup
    JSType resolvedType = namedType.resolveInternal(ERR_REPORTER, null);
    assertEquals(enumElementTypeType, resolvedType.getType());
    assertEquals(enumElementType, namedType.getReferencedType());

    // Verify that an enum element cycle warning was emitted
    String expectedWarning = "Cycle detected in inheritance chain of type " + reference;
    assertTrue(ERR_REPORTER.getWarnings().contains(expectedWarning));
  }

  @Test
  public void testNamedTypeUnresolvedType() {
    String reference = "myType";
    String sourceName = "MyFile.js";
    int lineno = 1;
    int charno = 1;

    // Create the NamedType
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    // Simulate the unresolved type case
    JSType resolvedType = null;
    assertEquals(NO_RESOLVED_TYPE, resolvedType);
    assertEquals(NO_RESOLVED_TYPE, namedType.getReferencedType());
  }

  @Test
  public void testNamedTypeValidator() {
    String reference = "myType";
    String sourceName = "MyFile.js";
    int lineno = 1;
    int charno = 1;

    // Create a validator predicate
    Predicate<JSType> validator = type -> type instanceof FunctionType;

    // Create the NamedType
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    // Set the validator
    namedType.setValidator(validator);

    // Simulate the registry lookup
    JSType resolvedType = registry.getType(reference);
    if (resolvedType instanceof FunctionType) {
      // The validator passed, so the type is resolved
      assertEquals(resolvedType, namedType.getReferencedType());
    } else {
      // The validator failed, so the type is not resolved
      assertEquals(NO_RESOLVED_TYPE, namedType.getReferencedType());
    }
  }
}