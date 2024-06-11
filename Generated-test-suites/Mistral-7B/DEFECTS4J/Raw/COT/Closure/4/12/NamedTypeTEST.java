import static org.junit.Assert.*;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.testing.BaseTest;
import com.google.javascript.rhino.testing.PolymerTestRunner;

public class NamedTypeTest extends BaseTest {

  private static final String REFERENCE = "TestReference";
  private static final String SOURCE_NAME = "NamedTypeTest";
  private static final int LINENO = 10;
  private static final int CHARNO = 20;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    JSTypeRegistry registry = new JSTypeRegistry();
    initTesting(registry);
  }

  @org.junit.Test
  public void testNamedTypeConstructor() {
    NamedType namedType = new NamedType(registry, REFERENCE, SOURCE_NAME, LINENO, CHARNO);
    assertNotNull(namedType);
    assertEquals(REFERENCE, namedType.getReferenceName());
    assertEquals(SOURCE_NAME, namedType.toStringHelper(false));
  }

  @org.junit.Test
  public void testDefineProperty() {
    NamedType namedType = new NamedType(registry, REFERENCE, SOURCE_NAME, LINENO, CHARNO);
    Node propertyNode = newNode("{foo: 1}");

    boolean isDefined = namedType.defineProperty("foo", intType, false, propertyNode);
    assertTrue(isDefined);
  }

  @org.junit.Test
  public void testFinishPropertyContinuations() {
    NamedType namedType = new NamedType(registry, REFERENCE, SOURCE_NAME, LINENO, CHARNO);
    PropertyContinuation continuation = new PropertyContinuation(
        "foo", intType, false, propertyNode("{foo: 1}"));

    namedType.defineProperty("foo", intType, false, propertyNode("{foo: 1}"));
    namedType.finishPropertyContinuations();

    ObjectType referencedObjType = namedType.getReferencedObjTypeInternal();
    assertNotNull(referencedObjType);
    assertFalse(referencedObjType.isUnknownType());
  }

  @org.junit.Test
  public void testGetReferencedType() {
    JSType referencedType = stringType;
    NamedType namedType = new NamedType(registry, REFERENCE, SOURCE_NAME, LINENO, CHARNO) {
      @Override
      JSType getReferencedTypeInternal() {
        return referencedType;
      }
    };

    assertEquals(referencedType, namedType.getReferencedType());
  }

  @org.junit.Test
  public void testResolveViaRegistry() {
    String reference = "String";
    NamedType namedType = new NamedType(registry, reference, SOURCE_NAME, LINENO, CHARNO);

    JSType type = registry.getType(reference);
    namedType.setReferencedAndResolvedType(type, t, enclosing);

    assertEquals(type, namedType.getReferencedType());
    assertEquals(type, namedType.getResolvedType());
  }

  @org.junit.Test
  public void testResolveViaProperties() {
    String reference = "Object.prototype.toString.call";
    NamedType namedType = new NamedType(registry, reference, SOURCE_NAME, LINENO, CHARNO);

    StaticScope<JSType> enclosing = new StaticScope<>();
    JSType value = ObjectType.prototype;

    namedType.resolveViaProperties(t, enclosing);

    FunctionType functionType = FunctionType.instance;
    assertEquals(functionType.getInstanceType(), namedType.getReferencedType());
    assertEquals(functionType.getInstanceType(), namedType.getResolvedType());
  }

  @org.junit.Test
  public void testSetValidator() {
    NamedType namedType = new NamedType(registry, REFERENCE, SOURCE_NAME, LINENO, CHARNO);

    Predicate<JSType> validator = jstype -> jstype instanceof StringType;

    namedType.setValidator(validator);

    JSType referencedType = intType;
    namedType.setReferencedAndResolvedType(referencedType, t, enclosing);

    assertFalse(validator.apply(referencedType));
  }

  @org.junit.Test
  public void testUnresolvedTypeWarning() {
    String reference = "UnresolvedType";
    NamedType namedType = new NamedType(registry, reference, SOURCE_NAME, LINENO, CHARNO);

    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> enclosing = new StaticScope<>();

    namedType.resolveInternal(t, enclosing);

    assertTrue(t.hasErrors());
    assertEquals(1, t.getErrorCount());
    String errorMessage = t.getError(0).getMessage();
    assertTrue(errorMessage.contains("Unresolved type " + reference));
  }

  @org.junit.Test
  public void testForwardDeclaredType() {
    String reference = "forwardDeclaredType";
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.registerForwardDeclaredType(reference, intType);

    NamedType namedType = new NamedType(registry, reference, SOURCE_NAME, LINENO, CHARNO);

    assertEquals(intType, namedType.getReferencedType());
    assertEquals(intType, namedType.getResolvedType());
  }

  @org.junit.Test
  public void testImplicitPrototypeCycle() {
    String reference = "PrototypeCycle";
    NamedType namedType = new NamedType(registry, reference, SOURCE_NAME, LINENO, CHARNO) {
      @Override
      JSType getReferencedTypeInternal() {
        return this;
      }
    };

    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> enclosing = new StaticScope<>();

    namedType.resolveInternal(t, enclosing);

    assertTrue(t.hasErrors());
    assertEquals(1, t.getErrorCount());
    String errorMessage = t.getError(0).getMessage();
    assertTrue(errorMessage.contains("Cycle detected in inheritance chain"));
  }

  @org.junit.Test
  public void testEnumElementCycle() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String enumReference = "EnumElementCycle";
    String elementReference = "ElementCycle";

    EnumType enumType = new EnumType(registry, "EnumType", null, null) {
      @Override
      EnumElementType getElementsType() {
        return new EnumElementType(registry, enumReference, null, null) {
          @Override
          JSType getPrimitiveType() {
            return new NamedType(registry, elementReference, SOURCE_NAME, LINENO, CHARNO) {
              @Override
              JSType getReferencedTypeInternal() {
                return this;
              }
            };
          }
        };
      }
    };

    NamedType namedType = new NamedType(registry, enumReference, SOURCE_NAME, LINENO, CHARNO) {
      @Override
      JSType getReferencedTypeInternal() {
        return enumType;
      }
    };

    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> enclosing = new StaticScope<>();

    namedType.resolveInternal(t, enclosing);

    assertTrue(t.hasErrors());
    assertEquals(1, t.getErrorCount());
    String errorMessage = t.getError(0).getMessage();
    assertTrue(errorMessage.contains("Cycle detected in inheritance chain"));
  }

  @org.junit.Test
  public void testSetReferencedAndResolvedType() {
    String reference = "SetReferencedAndResolvedType";
    NamedType namedType = new NamedType(registry, reference, SOURCE_NAME, LINENO, CHARNO);

    JSType referencedType = intType;
    namedType.setReferencedAndResolvedType(referencedType, t, enclosing);

    assertEquals(referencedType, namedType.getReferencedType());
    assertEquals(referencedType, namedType.getResolvedType());
  }

  @org.junit.Test
  public void testHandleTypeCycle() {
    String reference = "HandleTypeCycle";
    NamedType namedType = new NamedType(registry, reference, SOURCE_NAME, LINENO, CHARNO) {
      @Override
      JSType getReferencedTypeInternal() {
        return this;
      }
    };

    ErrorReporter t = new ErrorReporter();

    namedType.handleTypeCycle(t);

    assertTrue(t.hasErrors());
    assertEquals(1, t.getErrorCount());
    String errorMessage = t.getError(0).getMessage();
    assertTrue(errorMessage.contains("Cycle detected in inheritance chain"));
  }
}