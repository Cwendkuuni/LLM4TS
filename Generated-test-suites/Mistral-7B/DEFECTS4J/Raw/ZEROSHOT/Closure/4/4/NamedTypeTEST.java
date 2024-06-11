import com.google.javascript.jscomp.test.JSTestCase;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.repl.StaticScope;
import com.google.javascript.rhino.jstype.NamedType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.common.collect.Lists;

public class NamedTypeTest extends JSTestCase {

  private static JSTypeRegistry registry;
  private static NamedType namedType;

  @Override
  public void setUp() throws Exception {
    super.setUp();
    registry = new JSTypeRegistry();
  }

  @Override
  public void tearDown() throws Exception {
    super.tearDown();
    if (namedType != null) {
      namedType.dispose();
      namedType = null;
    }
  }

  public void testCreateNamedType() {
    String reference = "MyType";
    String sourceName = "NamedTypeTest.java";
    int lineno = 10;
    int charno = 20;

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    assertEquals(reference, namedType.getReferenceName());
    assertEquals(JSType.UNKNOWN_TYPE, namedType.getType());
  }

  public void testDefinePropertyOnUnresolvedNamedType() {
    String reference = "MyType";
    String sourceName = "NamedTypeTest.java";
    int lineno = 10;
    int charno = 20;

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    String propertyName = "prop";
    JSType propertyType = registry.getType("int");
    Node propertyNode = new Node();

    namedType.defineProperty(propertyName, propertyType, false, propertyNode);

    List<PropertyContinuation> propertyContinuations = namedType.getPropertyContinuations();
    assertEquals(1, propertyContinuations.size());
    PropertyContinuation propertyContinuation = propertyContinuations.get(0);
    assertEquals(propertyName, propertyContinuation.getPropertyName());
    assertEquals(propertyType, propertyContinuation.getType());
    assertFalse(propertyContinuation.isInferred());
    assertEquals(propertyNode, propertyContinuation.getPropertyNode());
  }

  public void testResolveNamedTypeViaRegistry() {
    String reference = "MyType";
    String sourceName = "NamedTypeTest.java";
    int lineno = 10;
    int charno = 20;
    JSType myType = new JSType() {
      @Override
      public String toString() {
        return "MyType";
      }
    };
    registry.register(reference, myType);

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    assertEquals(myType, namedType.getType());
  }

  public void testResolveNamedTypeViaProperties() {
    String reference = "MyType.MyComponentType";
    String sourceName = "NamedTypeTest.java";
    int lineno = 10;
    int charno = 20;
    JSType myType = new JSType() {
      @Override
      public String toString() {
        return "MyType";
      }
    };
    JSType myComponentType = new JSType() {
      @Override
      public String toString() {
        return "MyComponentType";
      }
    };

    registry.register(reference.substring(0, reference.lastIndexOf(".")), myType);
    StaticScope<JSType> staticScope = new StaticScope<>();
    staticScope.put("MyType", myType);
    staticScope.put("MyType.prototype", myType.getInstanceType());
    staticScope.put("MyComponentType", myComponentType);

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    assertEquals(myComponentType, namedType.getType());
  }

  public void testResolveNamedTypeWithCycle() {
    String reference = "MyType";
    String sourceName = "NamedTypeTest.java";
    int lineno = 10;
    int charno = 20;

    registry.register(reference, registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE));

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    assertEquals(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), namedType.getType());

    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> enclosing = new StaticScope<>();
    namedType.resolve(errorReporter, enclosing);

    assertTrue(errorReporter.hasErrors());
  }

  public void testResolveNamedTypeWithImplicitPrototypeCycle() {
    String reference = "MyType";
    String sourceName = "NamedTypeTest.java";
    int lineno = 10;
    int charno = 20;

    JSType myType = new JSType() {
      @Override
      public String toString() {
        return "MyType";
      }

      @Override
      public JSType getPrototype() {
        return this;
      }
    };

    registry.register(reference, myType);

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    assertEquals(myType, namedType.getType());

    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> enclosing = new StaticScope<>();
    namedType.resolve(errorReporter, enclosing);

    assertTrue(errorReporter.hasErrors());
  }

  public void testResolveNamedTypeWithUnresolvedType() {
    String reference = "MyType";
    String sourceName = "NamedTypeTest.java";
    int lineno = 10;
    int charno = 20;

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> enclosing = new StaticScope<>();
    namedType.resolve(errorReporter, enclosing);

    assertTrue(errorReporter.hasErrors());
  }

  public void testResolveNamedTypeWithUnknownType() {
    String reference = "MyType";
    String sourceName = "NamedTypeTest.java";
    int lineno = 10;
    int charno = 20;

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> enclosing = new StaticScope<>();
    namedType.resolve(errorReporter, enclosing);

    assertTrue(errorReporter.hasErrors());

    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_RESOLVED_TYPE), namedType.getType());
  }

  public void testNamedTypeIsNamedType() {
    String reference = "MyType";
    String sourceName = "NamedTypeTest.java";
    int lineno = 10;
    int charno = 20;

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    assertTrue(namedType.isNamedType());
  }

  public void testNamedTypeIsNominalType() {
    String reference = "MyType";
    String sourceName = "NamedTypeTest.java";
    int lineno = 10;
    int charno = 20;

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    assertTrue(namedType.isNominalType());
  }

  public void testNamedTypeHashCode() {
    String reference = "MyType";
    String sourceName = "NamedTypeTest.java";
    int lineno = 10;
    int charno = 20;

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    assertEquals(reference.hashCode(), namedType.hashCode());
  }

  public void testNamedTypeToString() {
    String reference = "MyType";
    String sourceName = "NamedTypeTest.java";
    int lineno = 10;
    int charno = 20;

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    assertEquals(reference, namedType.toString());
  }
}