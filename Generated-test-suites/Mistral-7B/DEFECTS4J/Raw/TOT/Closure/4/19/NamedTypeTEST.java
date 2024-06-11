import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.testing.TypeEquivalenceTester;
import com.google.javascript.rhino.testing.TypeTester;
import com.google.common.base.Predicates;
import com.google.javascript.rhino.testing.StaticScope;
import com.google.javascript.rhino.testing.TypeTestUtils;
import com.google.javascript.rhino.testing.type.JSTypeFactory;
import com.google.javascript.rhino.testing.type.ObjectTypeTester;

import java.util.List;
import java.util.ArrayList;

import static com.google.javascript.rhino.testing.TypeTestUtils.assertThrows;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class NamedTypeTest {

  private static final String TYPE_NAME = "MyType";
  private static final int LINE_NUMBER = 1;
  private static final int CHAR_NUMBER = 1;

  private static final NamedType createNamedType(JSTypeRegistry registry) {
    return new NamedType(
        registry, TYPE_NAME, "TestFile", LINE_NUMBER, CHAR_NUMBER);
  }

  @org.junit.Test
  public void testCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = createNamedType(registry);
    assertNotNull(namedType);
    assertEquals(TYPE_NAME, namedType.getReferenceName());
  }

  @org.junit.Test
  public void testResolver() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = createNamedType(registry);
    assertSame(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), namedType.getReferencedType());

    JSType referencedType = new ObjectType(registry, "MyReferencedType");
    registry.setType(TYPE_NAME, referencedType);
    namedType = createNamedType(registry);
    assertSame(referencedType, namedType.getReferencedType());
  }

  @org.junit.Test
  public void testResolverWithCycle() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = createNamedType(registry);
    namedType.setReferencedType(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    namedType.setResolvedType(namedType);

    registry.setType(TYPE_NAME, namedType);
    assertSame(namedType, registry.getType(TYPE_NAME).getReferencedType().getReferencedType());
  }

  @org.junit.Test
  public void testResolverWithoutTypeDefinition() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = createNamedType(registry);
    namedType.setResolvedType(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));

    assertNull(registry.getType(TYPE_NAME));
  }

  @org.junit.Test
  public void testResolverWithForwardDeclaredType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.setForwardDeclaredType(TYPE_NAME, true);
    NamedType namedType = createNamedType(registry);
    assertNotNull(namedType);
    assertEquals(TYPE_NAME, namedType.getReferenceName());
    assertSame(registry.getNativeObjectType(JSTypeNative.NO_RESOLVED_TYPE), namedType.getReferencedType());
  }

  @org.junit.Test
  public void testResolverWithTypeAndProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ObjectType baseType = new ObjectType(registry, "BaseType");
    registry.setType(TYPE_NAME, baseType);

    ObjectType propertyType = new ObjectType(registry, "PropertyType");
    String propertyName = "property";
    baseType.defineProperty(propertyName, propertyType, false, null);

    NamedType namedType = createNamedType(registry);
    namedType.setResolvedType(namedType);
    assertEquals(TYPE_NAME, namedType.getReferenceName());
    assertEquals(baseType, namedType.getReferencedType());
  }

  @org.junit.Test
  public void testDefinition() {
    JSTypeRegistry registry = new JSTypeRegistry();

    ObjectType baseType = new ObjectType(registry, "BaseType");
    ObjectType propertyType = new ObjectType(registry, "PropertyType");
    String propertyName = "property";

    NamedType namedType = createNamedType(registry);
    namedType.setReferencedType(baseType);

    List<PropertyContinuation> propertyContinuations = new ArrayList<>();
    namedType.setPropertyContinuations(propertyContinuations);

    propertyContinuations.add(new PropertyContinuation(propertyName, propertyType, false, null));

    StaticScope<JSType> scope = new StaticScope<>(registry.getRootScope());
    ObjectType target = new ObjectType(registry, "TargetType");
    assertEquals(0, target.getProperties().size());
    namedType.resolveInternal(new ErrorReporter() {}, scope);
    assertEquals(1, target.getProperties().size());
    assertEquals(propertyName, target.getPropertyNames().get(0));
    assertEquals(propertyType, target.getPropertyType(propertyName));
  }

  @org.junit.Test
  public void testTypeEquivalence() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ObjectType baseType = new ObjectType(registry, "BaseType");
    NamedType namedType = createNamedType(registry);
    namedType.setReferencedType(baseType);

    JSTypeFactory typeFactory = new JSTypeFactory(registry);
    JSType resolvedType = new ObjectType(registry, "ResolvedType");
    JSType expectedType = new ObjectType(registry, "ExpectedType");
    expectedType.setResolvedType(resolvedType);

    TypeEquivalenceTester.assertTypesEqual(namedType, expectedType);
    TypeEquivalenceTester.assertTypesEquivalent(namedType, expectedType);
    TypeEquivalenceTester.assertTypesNotEqual(namedType, baseType);
  }

  @org.junit.Test
  public void testTypeTest() {
    JSTypeRegistry registry = new JSTypeRegistry();

    ObjectType baseType = new ObjectType(registry, "BaseType");
    ObjectType propertyType = new ObjectType(registry, "PropertyType");
    String propertyName = "property";

    NamedType namedType = createNamedType(registry);
    namedType.setReferencedType(baseType);

    List<PropertyContinuation> propertyContinuations = new ArrayList<>();
    namedType.setPropertyContinuations(propertyContinuations);

    propertyContinuations.add(new PropertyContinuation(propertyName, propertyType, false, null));

    StaticScope<JSType> scope = new StaticScope<>(registry.getRootScope());
    ObjectType target = new ObjectType(registry, "TargetType");
    namedType.resolveInternal(new ErrorReporter() {}, scope);

    PropertyType property = (PropertyType) target.getPropertyType(propertyName);
    TypeTester.assertTypeMatchesPattern(
        TypeTestUtils.createPattern("property", propertyType), property);

    ObjectTypeTester.assertObjectTypeEquals(
        propertyType, property.getDefiningType());
  }

  @org.junit.Test
  public void testTypeCycle() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType1 = createNamedType(registry);
    namedType1.setReferencedType(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE));
    namedType1.setResolvedType(namedType1);

    NamedType namedType2 = createNamedType(registry);
    namedType2.setReferencedType(namedType1);
    namedType2.setResolvedType(namedType2);

    assertThrows(IllegalStateException.class, () -> {
      namedType1.resolveInternal(new ErrorReporter() {}, null);
    });
  }

  @org.junit.Test
  public void testTypeValidation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    NamedType namedType = createNamedType(registry);

    // Valid type
    Predicate<JSType> validator = Predicates.alwaysTrue();
    namedType.setValidator(validator);
    assertFalse(namedType.isValid());
    namesType.setResolvedType(new IntType(registry));
    assertTrue(namedType.isValid());

    // Invalid type
    namedType.setValidator(Predicates.not(Predicates.isInstance(IntType.class)));
    namedType.setReferencedType(new IntType(registry));
    assertFalse(namedType.isValid());
  }
}

class PropertyContinuation {
  private final String propertyName;
  private final JSType type;
  private final boolean inferred;
  private final Node propertyNode;

  PropertyContinuation(String propertyName, JSType type, boolean inferred, Node propertyNode) {
    this.propertyName = propertyName;
    this.type = type;
    this.inferred = inferred;
    this.propertyNode = propertyNode;
  }
}