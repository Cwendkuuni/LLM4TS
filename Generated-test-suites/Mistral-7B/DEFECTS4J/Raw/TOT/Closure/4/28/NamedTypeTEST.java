import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.registry.JSTypeRegistry;
import com.google.javascript.rhino.testing.PolymerTestBase;

public class NamedTypeTest extends PolymerTestBase {

  private static final String NAMED_TYPE_TEST_FILE = "NamedTypeTest.js";
  private static final int NAMED_TYPE_TEST_LINENO = 123;
  private static final int NAMED_TYPE_TEST_CHARNO = 45;

  @Override
  protected String getSourceFileName() {
    return NAMED_TYPE_TEST_FILE;
  }

  @Override
  protected int getSourceLineNumber() {
    return NAMED_TYPE_TEST_LINENO;
  }

  @Override
  protected int getSourceCharacterNumber() {
    return NAMED_TYPE_TEST_CHARNO;
  }

  public void testNamedTypeWithResolvedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String resolvedTypeName = "String";
    String reference = "myStringType";
    String sourceName = "NamedTypeTest";

    JSType resolvedType = registry.getType(resolvedTypeName);
    NamedType namedType = new NamedType(registry, reference, sourceName, 1, 1);

    assertEquals(resolvedType, namedType.getReferencedType());
  }

  public void testNamedTypeWithUnresolvedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "myUnknownType";
    String sourceName = "NamedTypeTest";

    NamedType namedType = new NamedType(registry, reference, sourceName, 1, 1);

    assertNull(namedType.getReferencedType());
  }

  public void testNamedTypeWithUnresolvedTypeButRegisteredLater() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "myUnknownType";
    String sourceName = "NamedTypeTest";

    NamedType namedType = new NamedType(registry, reference, sourceName, 1, 1);
    JSType resolvedType = new StringType(registry, "String");

    registry.registerTypeName(reference, resolvedType);

    assertEquals(resolvedType, namedType.getReferencedType());
  }

  public void testNamedTypeWithUnresolvedTypeAndInvalidType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "myUnknownType";
    String sourceName = "NamedTypeTest";

    NamedType namedType = new NamedType(registry, reference, sourceName, 1, 1);

    // Simulate the case where the type is found but it is not a valid type
    JSType invalidType = registry.getType("InvalidType");
    namedType.setReferencedType(invalidType);

    assertNotNull(namedType.getReferencedType());
    assertFalse(invalidType.isFunctionType());
    assertFalse(invalidType.isInterface());
    assertFalse(invalidType.isEnumType());
  }

  public void testNamedTypeWithUnresolvedTypeAndCycle() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "myCyclicType";
    String sourceName = "NamedTypeTest";

    JSType typeA = new ObjectType(registry, null);
    JSType typeB = new ObjectType(registry, null);
    typeA.setPropertyType("myProperty", typeB);
    typeB.setPropertyType("myProperty", typeA);

    NamedType namedTypeA = new NamedType(registry, reference, sourceName, 1, 1);
    NamedType namedTypeB = new NamedType(registry, reference, sourceName, 1, 1);

    namedTypeA.setReferencedType(namedTypeB);
    namedTypeB.setReferencedType(namedTypeA);

    assertNotNull(namedTypeA.getReferencedType());
    assertNotNull(namedTypeB.getReferencedType());
  }

  public void testNamedTypeWithUnresolvedTypeAndError() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "myUnknownType";
    String sourceName = "NamedTypeTest";

    NamedType namedType = new NamedType(registry, reference, sourceName, 1, 1);

    // Simulate the case where the type is never registered

    try {
      namedType.resolve(context, null);
      fail("Expected an error");
    } catch (TypeError e) {
      // Expected
    }

    assertNull(namedType.getReferencedType());
  }

  public void testDefinePropertyWithUnresolvedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "myUnknownType";
    String sourceName = "NamedTypeTest";
    int lineNumber = 123;
    int characterNumber = 45;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineNumber, characterNumber);

    // Define a property with an unresolved type
    Node propertyNode = context.mkName("myProperty");
    JSType type = new NumberType(registry, "Number");
    namedType.defineProperty("myProperty", type, false, propertyNode);

    // Resolve the type and check that the property was defined
    JSType resolvedType = new StringType(registry, "String");
    registry.registerTypeName(reference, resolvedType);
    namedType.resolve(context, null);

    JSType property = namedType.getPropertyType("myProperty");
    assertEquals(resolvedType, property.getValueType());
  }

  public void testDefinePropertyWithUnresolvedTypeAndError() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "myUnknownType";
    String sourceName = "NamedTypeTest";
    int lineNumber = 123;
    int characterNumber = 45;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineNumber, characterNumber);

    // Define a property with an unresolved type and expect an error
    Node propertyNode = context.mkName("myProperty");
    JSType type = new NumberType(registry, "Number");
    namedType.defineProperty("myProperty", type, false, propertyNode);

    try {
      namedType.resolve(context, null);
      fail("Expected an error");
    } catch (TypeError e) {
      // Expected
    }

    assertNull(namedType.getReferencedType());
  }
}