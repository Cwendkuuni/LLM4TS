import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSTypeNative.*;
import com.google.common.collect.Lists;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.StaticSlot;
import com.google.javascript.rhino.testing.BaseTestCase;
import com.google.javascript.rhino.testing.AssertType;
import com.google.javascript.rhino.testing.TypeFactory;

import java.util.List;

public class NamedTypeTest extends BaseTestCase {
  private static final String TEST_NAME = "NamedTypeTest";

  @Override
  public String getTestName() {
    return TEST_NAME;
  }

  @Override
  protected ErrorReporter errorReporter() {
    return null;
  }

  private static JSTypeRegistry registry = new JSTypeRegistry();

  private NamedType namedType;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    namedType = new NamedType(
        registry, "MyType", "Test", 1, 1);
  }

  @Test
  public void testGetReferenceName() {
    assertEquals("MyType", namedType.getReferenceName());
  }

  @Test
  public void testIsNamedType() {
    assertTrue(namedType.isNamedType());
  }

  @Test
  public void testIsNominalType() {
    assertTrue(namedType.isNominalType());
  }

  @Test
  public void testIsResolved() {
    assertFalse(namedType.isResolved());
  }

  @Test
  public void testDefineProperty() {
    namedType.defineProperty("prop1", TypeFactory.createStringType(), false, null);
    List<PropertyContinuation> propertyContinuations = namedType.getPropertyContinuations();
    assertEquals(1, propertyContinuations.size());
    PropertyContinuation propertyContinuation = propertyContinuations.get(0);
    assertEquals("prop1", propertyContinuation.getPropertyName());
    assertEquals(TypeFactory.createStringType(), propertyContinuation.getType());
    assertFalse(propertyContinuation.isInferred());
    assertNull(propertyContinuation.getPropertyNode());
  }

  @Test
  public void testFinishPropertyContinuations() {
    JSType referencedObjType = new ObjectType(registry, "MyObjectType", null, 1, 1);
    namedType.finishPropertyContinuations();
    ObjectType actualObjType = (ObjectType) namedType.getReferencedObjTypeInternal();
    assertEquals(referencedObjType, actualObjType);
  }

  @Test
  public void testGetReferencedType() {
    assertNull(namedType.getReferencedType());
  }

  @Test
  public void testGetReferencedObjTypeInternal() {
    assertNull(namedType.getReferencedObjTypeInternal());
  }

  @Test
  public void testResolveInternal() {
    StaticScope<JSType> enclosing = new StaticScope<JSType>() {
      @Override
      public StaticSlot<JSType> getSlot(String name) {
        if (name.equals("MyObjectType")) {
          return new StaticSlot<JSType>() {
            @Override
            public JSType getType() {
              return TypeFactory.createObjectType(registry, "MyObjectType", null, 1, 1);
            }
          };
        }
        return null;
      }
    };

    namedType.resolveInternal(errorReporter(), enclosing);

    JSType actualType = namedType.getReferencedType();
    assertEquals("MyObjectType", actualType.getReferenceName());
    assertTrue(actualType.isResolved());
  }

  @Test
  public void testResolveInternal_viaRegistry() {
    registry.registerType("MyType", NO_OBJECT_TYPE);
    namedType = new NamedType(registry, "MyType", "Test", 1, 1);

    namedType.resolveInternal(errorReporter(), null);

    JSType actualType = namedType.getReferencedType();
    assertEquals(NO_OBJECT_TYPE, actualType.getReferenceName());
    assertTrue(actualType.isResolved());
  }

  @Test
  public void testResolveInternal_viaProperties() {
    registry.registerType("MyObjectType", NO_OBJECT_TYPE);
    StaticScope<JSType> enclosing = new StaticScope<JSType>() {
      @Override
      public StaticSlot<JSType> getSlot(String name) {
        if (name.equals("MyObjectType")) {
          return new StaticSlot<JSType>() {
            @Override
            public JSType getType() {
              return TypeFactory.createObjectType(registry, "MyObjectType", null, 1, 1);
            }
          };
        }
        return null;
      }
    };

    JSType myObjectType = TypeFactory.createObjectType(registry, "MyObjectType", null, 1, 1);
    myObjectType.setPropertyType("prop1", TypeFactory.createStringType());

    namedType = new NamedType(registry, "MyObjectType.prop1", "Test", 1, 1);

    namedType.resolveInternal(errorReporter(), enclosing);

    JSType actualType = namedType.getReferencedType();
    assertEquals(String.class.getName(), actualType.getReferenceName());
    assertTrue(actualType.isResolved());
  }

  @Test
  public void testResolveInternal_viaProperties_cycle() {
    registry.registerType("MyType", new NamedType(registry, "MyType", null, 1, 1));

    namedType = new NamedType(registry, "MyType.MyType", "Test", 1, 1);

    try {
      namedType.resolveInternal(errorReporter(), null);
      fail();
    } catch (AssertionError expected) {
      // expected
      String expectedMessage = "Cycle detected in inheritance chain of type MyType.MyType";
      String actualMessage = expected.getMessage();
      assertEquals(expectedMessage, actualMessage);
    }
  }

  @Test
  public void testResolveInternal_viaProperties_enum() {
    registry.registerType("MyEnum", new EnumType(registry, "MyEnum", null, 1, 1));
    StaticScope<JSType> enclosing = new StaticScope<JSType>() {
      @Override
      public StaticSlot<JSType> getSlot(String name) {
        if (name.equals("MyEnum")) {
          return new StaticSlot<JSType>() {
            @Override
            public JSType getType() {
              return TypeFactory.createEnumType(registry, "MyEnum", null, 1, 1);
            }
          };
        }
        return null;
      }
    };

    namedType = new NamedType(registry, "MyEnum.MY_ENUM_VALUE", "Test", 1, 1);

    namedType.resolveInternal(errorReporter(), enclosing);

    JSType actualType = namedType.getReferencedType();
    assertEquals(Integer.TYPE.getName(), actualType.getReferenceName());
    assertTrue(actualType.isResolved());
  }

  @Test
  public void testSetValidator() {
    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return input.isFunctionType();
      }
    };

    namedType.setValidator(validator);
    JSType referencedType = namedType.getReferencedType();

    assertFalse(validator.apply(referencedType));

    // Now resolve the type
    StaticScope<JSType> enclosing = new StaticScope<JSType>() {
      @Override
      public StaticSlot<JSType> getSlot(String name) {
        if (name.equals("MyFunctionType")) {
          return new StaticSlot<JSType>() {
            @Override
            public JSType getType() {
              return TypeFactory.createFunctionType(
                  registry, null, null, true, 0, null, null);
            }
          };
        }
        return null;
      }
    };

    namedType.resolveInternal(errorReporter(), enclosing);

    referencedType = namedType.getReferencedType();

    assertTrue(validator.apply(referencedType));
  }
}