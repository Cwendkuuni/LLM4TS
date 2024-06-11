import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.NamedType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.PropertyContinuation;
import com.google.javascript.rhino.jstype.PropertyDescriptor;
import com.google.javascript.rhino.jstype.StaticSlot;
import com.google.javascript.rhino.jstype.TypeFactory;
import com.google.javascript.rhino.jstype.errors.TypeError;
import com.google.javascript.rhino.nodes.PropertyName;
import com.google.javascript.rhino.nodes.PropertyNode;

public class NamedTypeTest {
  private ErrorReporter errorReporter;
  private StaticScope<JSType> enclosingScope;
  private JSTypeRegistry registry;
  private NamedType namedType;
  private PropertyDescriptor descriptor;

  @Before
  public void setUp() {
    errorReporter = new ErrorReporter();
    enclosingScope = new StaticScope<JSType>();
    registry = new JSTypeRegistry();
  }

  @Test
  public void testCreateNamedType() {
    String reference = "MyType";
    String sourceName = "test.js";
    int lineno = 1;
    int charno = 1;

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    assertEquals(reference, namedType.getReferenceName());
    assertEquals("MyType", namedType.toString());
  }

  @Test
  public void testCreateNamedTypeWithReferenceType() {
    String reference = "Object";
    String sourceName = "test.js";
    int lineno = 1;
    int charno = 1;

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    assertEquals(JSType.OBJECT, namedType.getReferencedType());
  }

  @Test
  public void testNullReferenceName() {
    String reference = null;
    String sourceName = "test.js";
    int lineno = 1;
    int charno = 1;

    try {
      new NamedType(registry, reference, sourceName, lineno, charno);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException ex) {
      assertEquals("reference is marked non-null but is null", ex.getMessage());
    }
  }

  @Test
  public void testDefinePropertyWithUnresolvedNamedType() {
    String reference = "MyType";
    String sourceName = "test.js";
    int lineno = 1;
    int charno = 1;

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    String propertyName = "property";
    JSType type = BOOLEAN;
    Node propertyNode = new PropertyNode(new PropertyName(propertyName));

    try {
      namedType.defineProperty(propertyName, type, false, propertyNode);
      fail("Expected IllegalArgumentException");
    } catch (IllegalArgumentException ex) {
      assertEquals("NamedTypes cannot be defined until they are resolved",
          ex.getMessage());
    }
  }

  @Test
  public void testResolveViaRegistry() {
    String reference = "MyType";
    String sourceName = "test.js";
    int lineno = 1;
    int charno = 1;

    JSType registryType = new ObjectType(registry, "MyType", sourceName, lineno, charno);
    registry.registerType(reference, registryType);

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    assertEquals(registryType, namedType.getReferencedType());
  }

  @Test
  public void testResolveViaProperties() {
    String reference = "MyType.Property";
    String sourceName = "test.js";
    int lineno = 1;
    int charno = 1;

    StaticScope<JSType> parentScope = new StaticScope<JSType>();
    JSType parentType = new ObjectType(registry, "MyType", sourceName, lineno, charno);
    parentScope.defineSlot("MyType", parentType);

    StaticSlot<JSType> slot = enclosingScope.defineSlot("MyType", null);
    slot.setType(parentType);

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    assertEquals(PROPERTY_TYPE, namedType.getReferencedType());
  }

  @Test
  public void testResolveViaPropertiesWithFunctionType() {
    String reference = "MyType.Function";
    String sourceName = "test.js";
    int lineno = 1;
    int charno = 1;

    StaticScope<JSType> parentScope = new StaticScope<JSType>();
    JSType parentType = new ObjectType(registry, "MyType", sourceName, lineno, charno);
    parentScope.defineSlot("MyType", parentType);

    JSType functionType = new FunctionType(registry, null, null, null, null, null, null, null);
    propertyContinuations = Lists.newArrayList();
    PropertyContinuation continuation = new PropertyContinuation(
        "Function", functionType, false, null);
    parentType.defineProperty("Function", functionType, false, null);
    propertyContinuations.add(continuation);

    StaticSlot<JSType> slot = enclosingScope.defineSlot("MyType", null);
    slot.setType(parentType);

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    assertEquals(functionType.getInstanceType(), namedType.getReferencedType());
  }

  @Test
  public void testResolveViaPropertiesWithEnumType() {
    String reference = "MyType.Enum";
    String sourceName = "test.js";
    int lineno = 1;
    int charno = 1;

    StaticScope<JSType> parentScope = new StaticScope<JSType>();
    JSType parentType = new ObjectType(registry, "MyType", sourceName, lineno, charno);
    parentScope.defineSlot("MyType", parentType);

    JSType enumType = new EnumType(registry, "MyType.Enum", sourceName, lineno, charno);
    PropertyDescriptor enumDescriptor = new PropertyDescriptor();
    enumDescriptor.setName("EnumElement");
    enumDescriptor.setType(INT);
    enumType.setProperty("EnumElement", enumDescriptor);

    StaticSlot<JSType> slot = enclosingScope.defineSlot("MyType", null);
    slot.setType(parentType);

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    assertEquals(enumType.getElementsType(), namedType.getReferencedType());
  }

  @Test
  public void testResolveViaPropertiesWithNoObjectType() {
    String reference = "MyType.NoObject";
    String sourceName = "test.js";
    int lineno = 1;
    int charno = 1;

    StaticScope<JSType> parentScope = new StaticScope<JSType>();
    JSType parentType = new ObjectType(registry, "MyType", sourceName, lineno, charno);
    parentScope.defineSlot("MyType", parentType);

    StaticSlot<JSType> slot = enclosingScope.defineSlot("MyType", null);
    slot.setType(parentType);

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    assertEquals(registry.getNativeFunctionType(NO_OBJECT_TYPE).getInstanceType(),
        namedType.getReferencedType());
  }

  @Test
  public void testResolveViaPropertiesWithUnknownType() {
    String reference = "MyType.Unknown";
    String sourceName = "test.js";
    int lineno = 1;
    int charno = 1;

    StaticScope<JSType> parentScope = new StaticScope<JSType>();
    JSType parentType = new ObjectType(registry, "MyType", sourceName, lineno, charno);
    parentScope.defineSlot("MyType", parentType);

    StaticSlot<JSType> slot = enclosingScope.defineSlot("MyType", null);
    slot.setType(parentType);

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    assertEquals(registry.getNativeObjectType(NO_RESOLVED_TYPE), namedType.getReferencedType());
  }

  @Test
  public void testTypeProperty() {
    String reference = "MyType";
    String sourceName = "test.js";
    int lineno = 1;
    int charno = 1;

    JSType registryType = new ObjectType(registry, "MyType", sourceName, lineno, charno);
    registry.registerType(reference, registryType);

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    assertEquals(registryType, namedType.getType());
  }

  @Test
  public void testSetValidator() {
    String reference = "MyType";
    String sourceName = "test.js";
    int lineno = 1;
    int charno = 1;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return input.isFunctionType();
      }
    };

    namedType.setValidator(validator);

    JSType functionType = new FunctionType(registry, null, null, null, null, null, null, null);
    assertEquals(functionType, namedType.getReferencedType());
  }

  @Test(expected = TypeError.class)
  public void testSetValidatorWithUnresolvedType() {
    String reference = "MyType";
    String sourceName = "test.js";
    int lineno = 1;
    int charno = 1;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return input.isFunctionType();
      }
    };

    namedType.setValidator(validator);

    namedType.resolveInternal(errorReporter, enclosingScope);
    fail("Expected TypeError");
  }

  @Test
  public void testIsNamedType() {
    String reference = "MyType";
    String sourceName = "test.js";
    int lineno = 1;
    int charno = 1;

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    assertTrue(namedType.isNamedType());
  }

  @Test
  public void testIsNominalType() {
    String reference = "MyType";
    String sourceName = "test.js";
    int lineno = 1;
    int charno = 1;

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    assertTrue(namedType.isNominalType());
  }

  @Test
  public void testHashCode() {
    String reference1 = "MyType1";
    String sourceName1 = "test1.js";
    int lineno1 = 1;
    int charno1 = 1;

    String reference2 = "MyType2";
    String sourceName2 = "test2.js";
    int lineno2 = 2;
    int charno2 = 2;

    namedType = new NamedType(registry, reference1, sourceName1, lineno1, charno1);
    int hashCode1 = namedType.hashCode();

    namedType = new NamedType(registry, reference2, sourceName2, lineno2, charno2);
    int hashCode2 = namedType.hashCode();

    assertNotEquals(hashCode1, hashCode2);
  }
}