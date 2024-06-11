import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.StaticSlot;
import com.google.javascript.rhino.testing.TypeEquivalenceTester;
import com.google.javascript.rhino.testing.TypeTester;
import com.google.javascript.rhino.testing.ValueFactory;
import com.google.javascript.rhino.type.ObjectType;
import com.google.javascript.rhino.type.ValueType;
import com.google.javascript.rhino.util.Reference;

import junit.framework.TestCase;

import java.util.List;

public class NamedTypeTest extends TestCase {

  private static final JSTypeNative UNKNOWN_TYPE = JSTypeNative.UNKNOWN_TYPE;
  private static final JSTypeNative NO_RESOLVED_TYPE = JSTypeNative.NO_RESOLVED_TYPE;
  private static final JSTypeNative NO_OBJECT_TYPE = JSTypeNative.NO_OBJECT_TYPE;

  private static final String REFERENCE_ONE = "TypeOne";
  private static final String REFERENCE_TWO = "TypeTwo";
  private static final String REFERENCE_THREE = "TypeThree";
  private static final String REFERENCE_FOUR = "TypeFour";

  private static final int LINE_NUMBER = 1;
  private static final int CHAR_NUMBER = 1;

  private static final StaticScope<JSType> ENCLOSING_SCOPE = new StaticScope<>();
  private static final ErrorReporter ERROR_REPORTER = new ErrorReporter() {};

  private static final JSTypeRegistry REGISTRY = new JSTypeRegistry() {
    @Override
    public JSType getType(String name) {
      if (name.equals(REFERENCE_ONE)) {
        return ObjectType.CAST(TypeTester.createType(ValueType.OBJECT, "TypeOne"));
      }
      if (name.equals(REFERENCE_TWO)) {
        return ObjectType.CAST(TypeTester.createType(ValueType.OBJECT, "TypeTwo"));
      }
      if (name.equals(REFERENCE_THREE)) {
        return ObjectType.CAST(TypeTester.createType(ValueType.OBJECT, "TypeThree"));
      }
      if (name.equals(REFERENCE_FOUR)) {
        return ObjectType.CAST(TypeTester.createType(ValueType.OBJECT, "TypeFour"));
      }
      return UNKNOWN_TYPE;
    }

    @Override
    public boolean isLastGeneration() {
      return false;
    }

    @Override
    public boolean isForwardDeclaredType(String name) {
      if (name.equals(REFERENCE_ONE)) {
        return true;
      }
      if (name.equals(REFERENCE_TWO)) {
        return true;
      }
      if (name.equals(REFERENCE_THREE)) {
        return true;
      }
      if (name.equals(REFERENCE_FOUR)) {
        return true;
      }
      return false;
    }
  };

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    REGISTRY.clear();
  }

  public void testNamedTypeRoundtrip() {
    String reference = REFERENCE_ONE;
    JSType namedType = new NamedType(REGISTRY, reference, "Test", LINE_NUMBER, CHAR_NUMBER);
    JSType resolvedType = namedType.resolveInternal(ERROR_REPORTER, ENCLOSING_SCOPE);

    TypeEquivalenceTester.assertEquals(
        "Test type one should be equal to its resolved type",
        resolvedType,
        ObjectType.CAST(TypeTester.createType(ValueType.OBJECT, "TypeOne")));

    // Check that the resolved type can be used to create a new NamedType
    JSType newNamedType = new NamedType(
        REGISTRY, reference, "Test", LINE_NUMBER, CHAR_NUMBER);
    JSType newResolvedType = newNamedType.resolveInternal(ERROR_REPORTER, ENCLOSING_SCOPE);

    TypeEquivalenceTester.assertEquals(
        "New NamedType should be equal to the original NamedType",
        newNamedType,
        namedType);

    TypeEquivalenceTester.assertEquals(
        "New resolved type should be equal to the original resolved type",
        newResolvedType,
        resolvedType);
  }

  public void testUnresolvedNamedType() {
    String reference = "UnknownType";
    JSType namedType = new NamedType(REGISTRY, reference, "Test", LINE_NUMBER, CHAR_NUMBER);
    JSType resolvedType = namedType.resolveInternal(ERROR_REPORTER, ENCLOSING_SCOPE);

    // The unresolved NamedType should be equal to the NO_RESOLVED_TYPE
    TypeEquivalenceTester.assertEquals(
        "Unresolved NamedType should be equal to NO_RESOLVED_TYPE",
        resolvedType,
        NO_RESOLVED_TYPE);

    // The unresolved NamedType should not be equal to any other type
    TypeEquivalenceTester.assertFalse(
        "Unresolved NamedType should not be equal to ObjectType",
        resolvedType.equals(ObjectType.CAST(ValueType.OBJECT)));

    // The unresolved NamedType should not be equal to itself
    TypeEquivalenceTester.assertFalse(
        "Unresolved NamedType should not be equal to itself",
        resolvedType.equals(resolvedType));

    // The unresolved NamedType should implement the NamedType interface
    assertTrue("Unresolved NamedType should implement NamedType interface",
        namedType instanceof NamedType);
  }

  public void testNamedTypeWithForwardDeclaration() {
    String reference = REFERENCE_ONE;

    // Create a forward declaration of the NamedType
    REGISTRY.registerForwardDeclaration(reference);

    JSType namedType = new NamedType(REGISTRY, reference, "Test", LINE_NUMBER, CHAR_NUMBER);
    JSType resolvedType = namedType.resolveInternal(ERROR_REPORTER, ENCLOSING_SCOPE);

    // The forward-declared NamedType should be equal to the resolved type
    TypeEquivalenceTester.assertEquals(
        "Forward-declared NamedType should be equal to resolved type",
        resolvedType,
        ObjectType.CAST(TypeTester.createType(ValueType.OBJECT, "TypeOne")));

    // The forward-declared NamedType should be equal to the NO_RESOLVED_TYPE
    // before it is registered
    JSType unregisteredNamedType = new NamedType(
        REGISTRY, reference, "Test", LINE_NUMBER, CHAR_NUMBER);
    JSType unregisteredResolvedType = unregisteredNamedType.resolveInternal(
        ERROR_REPORTER, ENCLOSING_SCOPE);

    TypeEquivalenceTester.assertEquals(
        "Unregistered forward-declared NamedType should be equal to NO_RESOLVED_TYPE",
        unregisteredResolvedType,
        NO_RESOLVED_TYPE);
  }

  public void testNamedTypeWithCycle() {
    String referenceOne = REFERENCE_ONE;
    String referenceTwo = REFERENCE_TWO;

    // Register the first type
    JSType typeOne = ObjectType.CAST(TypeTester.createType(ValueType.OBJECT, "TypeOne"));
    REGISTRY.registerType(referenceOne, typeOne);

    // Register the second type with a cycle reference
    JSType typeTwo = ObjectType.CAST(TypeTester.createType(ValueType.OBJECT, "TypeTwo"));
    typeTwo.setProperty("one", new NamedType(REGISTRY, referenceOne, "Test", LINE_NUMBER, CHAR_NUMBER));
    REGISTRY.registerType(referenceTwo, typeTwo);

    JSType namedType = new NamedType(REGISTRY, referenceTwo, "Test", LINE_NUMBER, CHAR_NUMBER);
    JSType resolvedType = namedType.resolveInternal(ERROR_REPORTER, ENCLOSING_SCOPE);

    // The cycle should be detected and the NamedType should be equal to the NO_RESOLVED_TYPE
    TypeEquivalenceTester.assertEquals(
        "NamedType with a cycle should be equal to NO_RESOLVED_TYPE",
        resolvedType,
        NO_RESOLVED_TYPE);
  }

  public void testNamedTypeWithMissingType() {
    String reference = "MissingType";

    JSType namedType = new NamedType(REGISTRY, reference, "Test", LINE_NUMBER, CHAR_NUMBER);
    JSType resolvedType = namedType.resolveInternal(ERROR_REPORTER, ENCLOSING_SCOPE);

    // The missing NamedType should be equal to the NO_RESOLVED_TYPE
    TypeEquivalenceTester.assertEquals(
        "Missing NamedType should be equal to NO_RESOLVED_TYPE",
        resolvedType,
        NO_RESOLVED_TYPE);

    // The missing NamedType should not be equal to any other type
    TypeEquivalenceTester.assertFalse(
        "Missing NamedType should not be equal to ObjectType",
        resolvedType.equals(ObjectType.CAST(ValueType.OBJECT)));

    // The missing NamedType should not be equal to itself
    TypeEquivalenceTester.assertFalse(
        "Missing NamedType should not be equal to itself",
        resolvedType.equals(resolvedType));
  }

  public void testNamedTypeWithUnsupportedType() {
    String reference = "FunctionType";

    JSType namedType = new NamedType(REGISTRY, reference, "Test", LINE_NUMBER, CHAR_NUMBER);
    JSType resolvedType = namedType.resolveInternal(ERROR_REPORTER, ENCLOSING_SCOPE);

    // The unsupported NamedType should be equal to the NO_RESOLVED_TYPE
    TypeEquivalenceTester.assertEquals(
        "Unsupported NamedType should be equal to NO_RESOLVED_TYPE",
        resolvedType,
        NO_RESOLVED_TYPE);

    // The unsupported NamedType should not be equal to any other type
    TypeEquivalenceTester.assertFalse(
        "Unsupported NamedType should not be equal to FunctionType",
        resolvedType.equals(FunctionType.createFunctionType(ValueType.VOID, new List<JSType>() {})));

    // The unsupported NamedType should not be equal to itself
    TypeEquivalenceTester.assertFalse(
        "Unsupported NamedType should not be equal to itself",
        resolvedType.equals(resolvedType));
  }

  public void testNamedTypeWithInterface() {
    String reference = "InterfaceType";

    JSType interfaceType = ObjectType.CAST(TypeTester.createType(ValueType.INTERFACE, "InterfaceType"));
    JSType namedType = new NamedType(REGISTRY, reference, "Test", LINE_NUMBER, CHAR_NUMBER);
    JSType resolvedType = namedType.resolveInternal(ERROR_REPORTER, ENCLOSING_SCOPE);

    // The interface NamedType should be equal to the NO_RESOLVED_TYPE
    TypeEquivalenceTester.assertEquals(
        "Interface NamedType should be equal to NO_RESOLVED_TYPE",
        resolvedType,
        NO_RESOLVED_TYPE);

    // The interface NamedType should not be equal to any other type
    TypeEquivalenceTester.assertFalse(
        "Interface NamedType should not be equal to ObjectType",
        resolvedType.equals(ObjectType.CAST(ValueType.OBJECT)));

    // The interface NamedType should not be equal to itself
    TypeEquivalenceTester.assertFalse(
        "Interface NamedType should not be equal to itself",
        resolvedType.equals(resolvedType));
  }

  public void testNamedTypeWithEnumElement() {
    String reference = "EnumType";

    JSType enumType = ObjectType.CAST(TypeTester.createType(ValueType.OBJECT, "EnumType"));
    JSType elementType = ObjectType.CAST(TypeTester.createType(ValueType.OBJECT, "EnumElementType"));
    JSType enumElementType = new NamedType(
        REGISTRY, "EnumElementType", "Test", LINE_NUMBER, CHAR_NUMBER);
    PropertyContinuation propertyContinuation = new PropertyContinuation(
        "one", elementType, true, null);

    enumType.setProperty("ONE", enumElementType);
    enumType.setPropertyContinuation("ONE", propertyContinuation);

    JSType namedType = new NamedType(REGISTRY, reference, "Test", LINE_NUMBER, CHAR_NUMBER);
    JSType resolvedType = namedType.resolveInternal(ERROR_REPORTER, ENCLOSING_SCOPE);

    // The enum NamedType should be equal to the EnumElementsType
    JSType expectedType = enumType.getProperty("ONE");
    TypeEquivalenceTester.assertEquals(
        "Enum NamedType should be equal to EnumElementType",
        resolvedType,
        expectedType);
  }

  public static class PropertyContinuation {
    private final String propertyName;
    private final JSType propertyType;
    private final boolean isInferred;
    private final Node propertyNode;

    public PropertyContinuation(String propertyName, JSType propertyType,
        boolean isInferred, Node propertyNode) {
      this.propertyName = propertyName;
      this.propertyType = propertyType;
      this.isInferred = isInferred;
      this.propertyNode = propertyNode;
    }

    public void commit(ObjectType target) {
      target.defineProperty(
          propertyName, propertyType, isInferred, propertyNode);
    }
  }
}