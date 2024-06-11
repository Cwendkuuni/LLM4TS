import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSType.*;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.util.ErrorReporter;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class NamedTypeTest {

  private ErrorReporter errorReporter;
  private StaticScope<JSType> scope;
  private NamedType namedType;

  @Before
  public void setUp() {
    errorReporter = new ErrorReporter();
    scope = new StaticScope<JSType>(null);
  }

  @Test
  public void testResolveViaRegistry() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType type = registry.getType("String");
    namedType = new NamedType(registry, "String", "Test", 1, 1);
    assertEquals(type, namedType.resolveInternal(errorReporter, scope));
  }

  @Test
  public void testResolveViaProperties() {
    ObjectType global = new ObjectType(JSTypeRegistry.EMPTY, null);
    global.setProperty("String", new StringType("String"));
    scope.setSlot("global", global);
    JSType type = new StringType("String");
    namedType = new NamedType(JSTypeRegistry.EMPTY, "String", "Test", 1, 1);
    assertEquals(type, namedType.resolveInternal(errorReporter, scope));
  }

  @Test(expected = AssertionError.class)
  public void testResolveCycle() {
    ObjectType cycle = new ObjectType(JSTypeRegistry.EMPTY, null);
    cycle.setProperty("cycle", namedType);
    namedType = new NamedType(JSTypeRegistry.EMPTY, "cycle", "Test", 1, 1);
    namedType.resolveInternal(errorReporter, scope);
  }

  @Test
  public void testResolveViaUnknownType() {
    namedType = new NamedType(JSTypeRegistry.EMPTY, "Unknown", "Test", 1, 1);
    assertNull(namedType.getReferencedType());
  }

  @Test
  public void testSetValidator() {
    JSTypeRegistry registry = new JSTypeRegistry();
    JSType type = new FunctionType(new NativeObjectType(JSTypeNative.FUNCTION),
        new List<JSType>(Lists.newArrayList()));
    namedType = new NamedType(registry, "MyType", "Test", 1, 1);
    Predicate<JSType> validator = new Predicate<JSType>() {
      @Override
      public boolean apply(JSType input) {
        return input.equals(type);
      }
    };
    namedType.setValidator(validator);
    assertEquals(type, namedType.getReferencedType());
  }

  @Test
  public void testDefineProperty() {
    ObjectType parent = new ObjectType(JSTypeRegistry.EMPTY, null);
    parent.setProperty("MyType", namedType);
    namedType = new NamedType(JSTypeRegistry.EMPTY, "MyType", "Test", 1, 1);
    PropertyContinuation continuation = new PropertyContinuation(
        "myProperty", ANY_TYPE, false, null);
    namedType.defineProperty("myProperty", ANY_TYPE, false, null);
    namedType.finishPropertyContinuations();
    assertEquals(namedType, parent.getPropertyType("MyType"));
    assertEquals(ANY_TYPE, parent.getPropertyType("MyType").getPropertyType("myProperty"));
  }
}