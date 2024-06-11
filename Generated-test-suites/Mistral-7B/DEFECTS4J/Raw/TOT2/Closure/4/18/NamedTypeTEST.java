import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.testing.TypeEquivalenceTester;
import com.google.javascript.rhino.testing.TypeTestingUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NamedTypeTest {

  private StaticScope<JSType> enclosingScope;
  private ErrorReporter t;
  private NamedType namedType;

  @Before
  public void setUp() {
    t = new ErrorReporter() {
      @Override
      public void fatalError(String msg, String file, int line, int charPos) {
        throw new RuntimeException(msg);
      }

      @Override
      public void warning(String msg, String file, int line, int charPos) {
        // Ignore warnings
      }
    };

    enclosingScope = new StaticScope<>(new JSTypeRegistry());
  }

  @Test
  public void testNamedTypeCreation() {
    String reference = "TypeA";
    String sourceName = "testFile.js";
    int lineno = 10;
    int charno = 20;

    JSTypeRegistry registry = enclosingScope.getRoot();
    namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    assertEquals(reference, namedType.getReferenceName());
  }

  @Test
  public void testUnresolvedNamedType() {
    String reference = "TypeX";
    String sourceName = "testFile.js";
    int lineno = 10;
    int charno = 20;

    JSTypeRegistry registry = enclosingScope.getRoot();
    namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    assertNull(namedType.getReferencedType());
  }

  @Test
  public void testNamedTypeWithValidatedType() {
    String reference = "NamedType";
    String sourceName = "testFile.js";
    int lineno = 10;
    int charno = 20;

    JSTypeRegistry registry = new JSTypeRegistry();
    registry.setType(reference, TypeTestingUtils.getFunctionType(
        new NodeFactory(), new ObjectType("Object"), null));

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    assertEquals(registry.getType(reference), namedType.getReferencedType());
  }

  @Test(expected = RuntimeException.class)
  public void testNamedTypeWithInvalidatedType() {
    String reference = "NamedType";
    String sourceName = "testFile.js";
    int lineno = 10;
    int charno = 20;

    JSTypeRegistry registry = new JSTypeRegistry();
    registry.setType(reference, TypeTestingUtils.getFunctionType(
        new NodeFactory(), new ObjectType("Object"), null));

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    TypeEquivalenceTester.expectError(
        new IllegalArgumentException("Could not validate type"),
        () -> namedType.setValidator(
            type -> assertFalse(type.isFunctionType())));
  }

  @Test
  public void testNamedTypeResolutionViaRegistry() {
    String reference = "FunctionType";
    String sourceName = "testFile.js";
    int lineno = 10;
    int charno = 20;

    JSTypeRegistry registry = new JSTypeRegistry();
    registry.setType(reference, TypeTestingUtils.getFunctionType(
        new NodeFactory(), new ObjectType("Object"), null));

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    assertEquals(registry.getType(reference), namedType.getReferencedType());
  }

  @Test
  public void testNamedTypeResolutionViaProperties() {
    String reference = "ObjectType.property";
    String sourceName = "testFile.js";
    int lineno = 10;
    int charno = 20;

    JSType registryType = TypeTestingUtils.getObjectType(new NodeFactory(), "ObjectType");
    registryType.putProperty("property", TypeTestingUtils.getFunctionType(
        new NodeFactory(), new ObjectType("Object"), null));
    JSTypeRegistry registry = new JSTypeRegistry();
    registry.setType("ObjectType", registryType);

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    assertEquals(registry.getType("ObjectType").getPropertyType("property"),
        namedType.getReferencedType());
  }

  @Test(expected = RuntimeException.class)
  public void testNamedTypeResolutionViaPropertiesWithNullType() {
    String reference = "ObjectType.property";
    String sourceName = "testFile.js";
    int lineno = 10;
    int charno = 20;

    JSTypeRegistry registry = new JSTypeRegistry();
    JSType registryType = registry.getType("ObjectType");
    registryType.putProperty("property", null);

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    // This should result in a null reference exception when trying to get the property
    namedType.getReferencedType();
  }

  @Test
  public void testNamedTypeWithCycle() {
    String reference = "NamedTypeCycle";
    String sourceName = "testFile.js";
    int lineno = 10;
    int charno = 20;

    JSTypeRegistry registry = new JSTypeRegistry();
    registry.setType(reference, new NamedType(registry, reference, sourceName, lineno, charno));
    namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    assertEquals(registry.getType(reference), namedType.getReferencedType());
  }

  @Test
  public void testNamedTypeWithEnumElementCycle() {
    String reference = "EnumType.Element";
    String sourceName = "testFile.js";
    int lineno = 10;
    int charno = 20;

    JSTypeRegistry registry = new JSTypeRegistry();
    registry.setType(
        "EnumType",
        new EnumType(
            new EnumType.ElementType(
                "EnumType",
                new ObjectType("Object"),
                new NodeFactory(),
                "EnumElement"),
            new ObjectType("Object"),
            new NodeFactory()));

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    assertEquals(registry.getType("EnumType").getElementsType(), namedType.getReferencedType());
  }

  @Test
  public void testNamedTypeWithForwardDeclaration() {
    String reference = "TypeA";
    String sourceName = "testFile.js";
    int lineno = 10;
    int charno = 20;

    JSTypeRegistry registry = new JSTypeRegistry();
    registry.setForwardDeclaredType(reference, true);

    namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    assertNull(namedType.getReferencedType());
  }
}