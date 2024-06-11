import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.testing.PolymerTestingRuntime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class NamedTypeTest {

  private StaticScope<JSType> staticScope;
  private ErrorReporter errorReporter;
  private PolymerTestingRuntime testingRuntime;

  @Before
  public void setUp() {
    testingRuntime = new PolymerTestingRuntime();
    errorReporter = mock(ErrorReporter.class);
    staticScope = testingRuntime.createStaticScope();
  }

  @Test
  public void testCreateNamedType() {
    JSTypeRegistry registry = testingRuntime.getRegistry();
    NamedType namedType = new NamedType(registry, "Foo", "test.js", 1, 1);
    JSType expectedType = registry.getType("Foo");

    namedType.setValidator(j -> expectedType.equals(j));

    JSType actualType = namedType.resolveInternal(errorReporter, staticScope);

    testingRuntime.assertTypesAreEqual(expectedType, actualType);
  }

  @Test
  public void testCreateNamedTypeWithValidator() {
    JSTypeRegistry registry = testingRuntime.getRegistry();
    String reference = "Foo";
    String sourceName = "test.js";
    int lineno = 1;
    int charno = 1;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    Predicate<JSType> validator = j -> j.isStringType();
    namedType.setValidator(validator);

    JSType expectedType = registry.getType("Bar"); // a string type

    JSType actualType = namedType.resolveInternal(errorReporter, staticScope);

    testingRuntime.assertTypesAreEqual(expectedType, actualType);
  }

  @Test(expected = AssertionError.class)
  public void testCreateNamedTypeWithoutValidator() {
    JSTypeRegistry registry = testingRuntime.getRegistry();
    String reference = "Foo";
    String sourceName = "test.js";
    int lineno = 1;
    int charno = 1;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    JSType actualType = namedType.resolveInternal(errorReporter, staticScope);

    testingRuntime.assertFailedExpectations();
  }

  @Test
  public void testDefinePropertyWithUnresolvedType() {
    JSTypeRegistry registry = testingRuntime.getRegistry();
    String reference = "Foo";
    String sourceName = "test.js";
    int lineno = 1;
    int charno = 1;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    PropertyContinuation continuation = new PropertyContinuation(
        "bar",
        testingRuntime.createStringType(),
        true,
        mock(Node.class)
    );

    namedType.defineProperty("bar", null, true, continuation.propertyNode);
  }

  @Test
  public void testFinishPropertyContinuations() {
    JSTypeRegistry registry = testingRuntime.getRegistry();
    String reference = "Foo";
    String sourceName = "test.js";
    int lineno = 1;
    int charno = 1;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    ObjectType referencedObjType = testingRuntime.createObjectType("Foo");

    namedType.finishPropertyContinuations();

    List<PropertyContinuation> propertyContinuations = namedType.getPropertyContinuations();

    testingRuntime.assertTypesAreEqual(referencedObjType, propertyContinuations.get(0).getType());
  }

  @Test
  public void testGetReferencedType() {
    JSTypeRegistry registry = testingRuntime.getRegistry();
    String reference = "Foo";
    String sourceName = "test.js";
    int lineno = 1;
    int charno = 1;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    JSType expectedType = registry.getType("Foo");

    JSType actualType = namedType.getReferencedType();

    testingRuntime.assertTypesAreEqual(expectedType, actualType);
  }

  @Test
  public void testGetReferencedTypeWithResolver() {
    JSTypeRegistry registry = testingRuntime.getRegistry();
    String reference = "Foo";
    String sourceName = "test.js";
    int lineno = 1;
    int charno = 1;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    JSType expectedType = registry.getType("Foo");

    when(staticScope.getScope(mock(JSType.class))).thenReturn(staticScope);

    JSType actualType = namedType.getReferencedTypeWithResolver(staticScope);

    testingRuntime.assertTypesAreEqual(expectedType, actualType);
  }

  @Test
  public void testGetReferencedTypeWithResolverAndNoType() {
    JSTypeRegistry registry = testingRuntime.getRegistry();
    String reference = "Foo";
    String sourceName = "test.js";
    int lineno = 1;
    int charno = 1;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    when(staticScope.getScope(mock(JSType.class))).thenReturn(mock(StaticScope.class));

    JSType actualType = namedType.getReferencedTypeWithResolver(staticScope);

    testingRuntime.assertTypesAreEqual(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), actualType);
  }

  @Test
  public void testGetReferencedAndResolvedType() {
    JSTypeRegistry registry = testingRuntime.getRegistry();
    String reference = "Foo";
    String sourceName = "test.js";
    int lineno = 1;
    int charno = 1;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    JSType expectedType = registry.getType("Foo");

    namedType.setReferencedType(expectedType);
    namedType.setResolvedType(expectedType);

    JSType actualType = namedType.getReferencedAndResolvedType();

    testingRuntime.assertTypesAreEqual(expectedType, actualType);
  }

  @Test
  public void testGetReferencedAndResolvedTypeWithResolver() {
    JSTypeRegistry registry = testingRuntime.getRegistry();
    String reference = "Foo";
    String sourceName = "test.js";
    int lineno = 1;
    int charno = 1;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    JSType expectedType = registry.getType("Foo");

    when(staticScope.getScope(mock(JSType.class))).thenReturn(staticScope);

    JSType actualType = namedType.getReferencedAndResolvedType();

    testingRuntime.assertTypesAreEqual(expectedType, actualType);
  }

  @Test
  public void testGetReferencedTypeWithValidator() {
    JSTypeRegistry registry = testingRuntime.getRegistry();
    String reference = "Foo";
    String sourceName = "test.js";
    int lineno = 1;
    int charno = 1;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    Predicate<JSType> validator = j -> j.isNumberType();

    namedType.setValidator(validator);

    JSType expectedType = registry.getType("Number");

    JSType actualType = namedType.getReferencedType();

    testingRuntime.assertTypesAreEqual(expectedType, actualType);
  }

  @Test
  public void testIsNamedType() {
    JSTypeRegistry registry = testingRuntime.getRegistry();
    String reference = "Foo";
    String sourceName = "test.js";
    int lineno = 1;
    int charno = 1;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    boolean actualResult = namedType.isNamedType();

    testingRuntime.assertThat(actualResult).isTrue();
  }

  @Test
  public void testIsNominalType() {
    JSTypeRegistry registry = testingRuntime.getRegistry();
    String reference = "Foo";
    String sourceName = "test.js";
    int lineno = 1;
    int charno = 1;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    boolean actualResult = namedType.isNominalType();

    testingRuntime.assertThat(actualResult).isTrue();
  }

  @Test
  public void testResolveInternal() {
    JSTypeRegistry registry = testingRuntime.getRegistry();
    String reference = "Foo";
    String sourceName = "test.js";
    int lineno = 1;
    int charno = 1;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    JSType expectedType = registry.getType("Foo");

    namedType.setReferencedType(expectedType);

    JSType actualType = namedType.resolveInternal(errorReporter, staticScope);

    testingRuntime.assertTypesAreEqual(expectedType, actualType);
  }

  @Test
  public void testResolveInternalWithResolver() {
    JSTypeRegistry registry = testingRuntime.getRegistry();
    String reference = "Foo";
    String sourceName = "test.js";
    int lineno = 1;
    int charno = 1;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    when(staticScope.getScope(mock(JSType.class))).thenReturn(staticScope);

    JSType expectedType = registry.getType("Foo");

    JSType actualType = namedType.resolveInternal(errorReporter, staticScope);

    testingRuntime.assertTypesAreEqual(expectedType, actualType);
  }

  @Test
  public void testResolveInternalWithResolverAndNoType() {
    JSTypeRegistry registry = testingRuntime.getRegistry();
    String reference = "Foo";
    String sourceName = "test.js";
    int lineno = 1;
    int charno = 1;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    when(staticScope.getScope(mock(JSType.class))).thenReturn(mock(StaticScope.class));

    JSType actualType = namedType.resolveInternal(errorReporter, staticScope);

    testingRuntime.assertTypesAreEqual(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), actualType);
  }

  @Test
  public void testResolveInternalWithTypeCycle() {
    JSTypeRegistry registry = testingRuntime.getRegistry();
    String reference = "Foo";
    String sourceName = "test.js";
    int lineno = 1;
    int charno = 1;

    NamedType namedTypeFoo = new NamedType(registry, reference, sourceName, lineno, charno);
    ObjectType objectTypeFoo = testingRuntime.createObjectType("Foo");

    namedTypeFoo.setReferencedType(objectTypeFoo);
    namedTypeFoo.setResolvedType(namedTypeFoo);

    when(staticScope.getScope(mock(JSType.class))).thenReturn(staticScope);

    ErrorReporter errorReporter = mock(ErrorReporter.class);

    namedTypeFoo.resolveInternal(errorReporter, staticScope);

    testingRuntime.assertStringContains("Cycle detected in inheritance chain of type ", errorReporter.getMessages());
  }

  @Test
  public void testToString() {
    JSTypeRegistry registry = testingRuntime.getRegistry();
    String reference = "Foo";
    String sourceName = "test.js";
    int lineno = 1;
    int charno = 1;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    String actualResult = namedType.toString();

    testingRuntime.assertThat(actualResult).isEqualTo(reference);
  }

  @Test
  public void testToStringHelper() {
    JSTypeRegistry registry = testingRuntime.getRegistry();
    String reference = "Foo";
    String sourceName = "test.js";
    int lineno = 1;
    int charno = 1;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    String actualResult = namedType.toStringHelper(true);

    testingRuntime.assertThat(actualResult).isEqualTo(reference);
  }

  @Test
  public void testToStringHelperFalse() {
    JSTypeRegistry registry = testingRuntime.getRegistry();
    String reference = "Foo";
    String sourceName = "test.js";
    int lineno = 1;
    int charno = 1;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    String actualResult = namedType.toStringHelper(false);

    testingRuntime.assertThat(actualResult).isNull();
  }

  @Test
  public void testUnresolvedType() {
    JSTypeRegistry registry = testingRuntime.getRegistry();
    String reference = "Foo";
    String sourceName = "test.js";
    int lineno = 1;
    int charno = 1;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    namedType.handleUnresolvedType(errorReporter, true);

    JSType actualType = namedType.getReferencedType();

    testingRuntime.assertTypesAreEqual(registry.getNativeObjectType(JSTypeNative.NO_RESOLVED_TYPE), actualType);
  }

  @Test
  public void testUnresolvedTypeWithResolver() {
    JSTypeRegistry registry = testingRuntime.getRegistry();
    String reference = "Foo";
    String sourceName = "test.js";
    int lineno = 1;
    int charno = 1;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    when(staticScope.getScope(mock(JSType.class))).thenReturn(staticScope);

    namedType.handleUnresolvedType(errorReporter, true);

    JSType actualType = namedType.getReferencedType();

    testingRuntime.assertTypesAreEqual(registry.getNativeObjectType(JSTypeNative.NO_RESOLVED_TYPE), actualType);
  }

  private static class PropertyContinuation {
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

    void commit(ObjectType target) {
      target.defineProperty(propertyName, type, inferred, propertyNode);
    }
  }
}