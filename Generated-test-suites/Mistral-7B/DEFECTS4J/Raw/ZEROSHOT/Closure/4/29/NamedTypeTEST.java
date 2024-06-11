import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.base.Predicates;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JsType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.test.BaseTestCase;
import com.google.javascript.rhino.types.ObjectType;
import com.google.javascript.rhino.types.PropertyContinuation;
import com.google.javascript.rhino.types.PropertyDescriptor;
import com.google.javascript.rhino.types.ReferenceType;
import com.google.javascript.rhino.types.StaticScope;
import com.google.javascript.rhino.types.Type;
import com.google.javascript.rhino.types.TypeFactory;
import com.google.javascript.rhino.types.TypeName;
import com.google.javascript.rhino.types.typedef.TypedefType;

import java.util.Arrays;
import java.util.List;

public class NamedTypeTest extends BaseTestCase {

  private static ErrorReporter errorReporter = new ErrorReporter() {
    @Override
    public void fatal(String message, int line, int charPositionInLine) {
      fail(message);
    }

    @Override
    public void warning(String message, String sourceName, int line, int charPositionInLine) {
      System.out.println(message);
    }
  };

  private static final TypeFactory typeFactory = new TypeFactory();

  @org.junit.BeforeClass
  public static void setup() {
    setTypeFactoryForTest(typeFactory);
  }

  // Test case to check if a named type can be resolved using registry
  public void testResolveNamedTypeViaRegistry() {
    String reference = "MyType";
    String sourceName = "MySource.js";
    int lineno = 1;
    int charno = 1;

    // Create a registry containing the type MyType
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.define("MyType", new ObjectType(registry, "MyType"));

    // Create a named type based on the reference
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    // Resolve the named type using the registry
    Type resolvedType = namedType.resolve(errorReporter, null);

    // Check if the resolved type is the same as the one in the registry
    Type expectedType = ObjectType.cast(registry.getType(reference));
    assertEquals(expectedType, resolvedType);
  }

  // Test case to check if a named type can be resolved using properties
  public void testResolveNamedTypeViaProperties() {
    String reference = "MyType.property.subProperty";
    String sourceName = "MySource.js";
    int lineno = 1;
    int charno = 1;

    // Create a static scope with some properties
    StaticScope<JSType> staticScope = new StaticScope<>();
    staticScope.defineSlot("MyType", null);
    staticScope.defineSlot("MyType.property", null);
    staticScope.defineSlot("MyType.property.subProperty", null);

    // Create a named type based on the reference
    NamedType namedType = new NamedType(null, reference, sourceName, lineno, charno);

    // Resolve the named type using properties
    Type resolvedType = namedType.resolve(errorReporter, staticScope);

    // Check if the resolved type is the expected type
    Type expectedType = staticScope.getSlot("MyType.property.subProperty").getType();
    assertSame(expectedType, resolvedType);
  }

  // Test case to check if a named type with unresolved reference is saved as a property continuation
  public void testNamedTypeAsPropertyContinuation() {
    String reference = "MyType.property.subProperty";
    String sourceName = "MySource.js";
    int lineno = 1;
    int charno = 1;

    // Create a named type based on the reference
    NamedType namedType = new NamedType(null, reference, sourceName, lineno, charno);

    // Define a property continuation for the named type
    PropertyContinuation propertyContinuation = new PropertyContinuation(
        "subProperty", typeFactory.createStringType(), true, null);

    ObjectType objectType = new ObjectType(null, "MyType");
    objectType.defineProperty("property", null, null, null);

    // Set the property continuation for the named type
    namedType.setPropertyContinuations(Arrays.asList(propertyContinuation));

    // Check if the property continuation is set correctly
    List<PropertyContinuation> propertyContinuations = namedType.getPropertyContinuations();
    assertEquals(1, propertyContinuations.size());
    assertEquals(propertyContinuation, propertyContinuations.get(0));

    // Define the property with the resolved type
    PropertyDescriptor propertyDescriptor = new PropertyDescriptor();
    propertyDescriptor.setName("subProperty");
    propertyDescriptor.setType(typeFactory.createStringType());
    propertyDescriptor.setWritable(true);
    propertyDescriptor.setEnumerable(true);
    propertyDescriptor.setConfigurable(true);

    objectType.defineProperty("property", propertyDescriptor, false, null);

    // Resolve the named type again and check if the property is defined
    Type resolvedType = namedType.resolve(errorReporter, null);
    assertEquals(objectType, resolvedType);

    // Check if the property is defined in the resolved type
    assertEquals(1, objectType.getOwnPropertyNames().size());
    ObjectPropertyDescriptor property = objectType.getOwnPropertyDescriptor("property");
    assertNotNull(property.getValue());
    assertEquals("", property.getDescription());
    assertEquals("subProperty", property.getName());
    assertTrue(property.isWritable());
    assertTrue(property.isEnumerable());
    assertTrue(property.isConfigurable());
  }

  // Test case to check if a named type can be resolved from a forward declaration
  public void testResolveNamedTypeFromForwardDeclaration() {
    String reference = "MyType";
    String sourceName = "MySource.js";
    int lineno = 1;
    int charno = 1;

    // Define a forward declaration for the type MyType
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.define("MyType", null, Predicates.notNull());

    // Create a named type based on the reference
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    // Resolve the named type using the registry
    Type resolvedType = namedType.resolve(errorReporter, null);

    // Check if the resolved type is the expected type (null in this case as it's a forward declaration)
    assertNull(resolvedType);
  }

  // Test case to check if a named type can be resolved from a typdef
  public void testResolveNamedTypeFromTypedef() {
    String reference = "MyType";
    String sourceName = "MySource.js";
    int lineno = 1;
    int charno = 1;

    // Define a typedef for the type MyType
    JSTypeRegistry registry = new JSTypeRegistry();
    TypedefType typedefType = new TypedefType(null, "MyType", null, null);
    registry.define("MyType", typedefType);

    // Create a named type based on the reference
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    // Resolve the named type using the registry
    Type resolvedType = namedType.resolve(errorReporter, null);

    // Check if the resolved type is the same as the typedef type
    TypedefType expectedType = TypedefType.cast(registry.getType(reference));
    assertEquals(expectedType, resolvedType);
  }

  // Test case to check if a named type with an unresolved reference is handled correctly
  public void testHandleUnresolvedNamedType() {
    String reference = "MyType";
    String sourceName = "MySource.js";
    int lineno = 1;
    int charno = 1;

    // Create a named type based on the reference
    NamedType namedType = new NamedType(null, reference, sourceName, lineno, charno);

    // Set a validator for the named type
    Predicate<Type> validator = Predicates.isInstance(String.class);
    namedType.setValidator(validator);

    // Resolve the named type and check if the validator is applied
    Type resolvedType = namedType.resolve(errorReporter, null);
    assertEquals(typeFactory.getUnknownType(), resolvedType);
    assertTrue(validator.apply(resolvedType));

    // Check if the named type is marked as resolved
    assertTrue(namedType.isResolved());
  }

  // Test case to check if a named type with a cycle in its inheritance chain is handled correctly
  public void testHandleNamedTypeCycle() {
    String reference = "MyType";
    String sourceName = "MySource.js";
    int lineno = 1;
    int charno = 1;

    // Create a registry containing two types with a cycle in their inheritance chain
    JSTypeRegistry registry = new JSTypeRegistry();
    Type type1 = new ObjectType(registry, "Type1");
    Type type2 = new ObjectType(registry, "Type2");
    type1.setPrototypeOf(type2);
    type2.setPrototypeOf(type1);

    // Create a named type based on the reference
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    // Resolve the named type and check if the type cycle is detected and handled correctly
    Type resolvedType = namedType.resolve(errorReporter, null);
    assertSame(typeFactory.getUnknownType(), resolvedType);
    assertTrue(errorReporter.hasWarnings());
  }

  // Test case to check if a named type with an unresolved EnumElementType is handled correctly
  public void testHandleNamedTypeWithUnresolvedEnumElementType() {
    String reference = "MyEnum";
    String sourceName = "MySource.js";
    int lineno = 1;
    int charno = 1;

    // Create an EnumType with an unresolved EnumElementType
    JSTypeRegistry registry = new JSTypeRegistry();
    EnumType enumType = new EnumType(registry, "MyEnum", null, null);
    EnumElementType enumElementType = new EnumElementType(
        registry, "MyEnum.MyValue", enumType, null, null);
    registry.define("MyEnum", enumType);
    registry.define("MyEnum.MyValue", enumElementType);

    // Create a named type based on the reference
    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    // Resolve the named type and check if the unresolved EnumElementType is handled correctly
    Type resolvedType = namedType.resolve(errorReporter, null);
    assertSame(enumType, resolvedType);
    assertTrue(errorReporter.hasWarnings());
  }
}