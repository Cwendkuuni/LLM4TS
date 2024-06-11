import com.google.javascript.rhino.*;
import static com.google.javascript.rhino.Node.*;
import static com.google.javascript.rhino.JSType.*;
import static org.junit.Assert.*;

import java.util.List;

public class NamedTypeTest {

  @SuppressWarnings("unchecked")
  @org.junit.Test
  public void testNamedTypeConstructor() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "java.lang.String";
    String sourceName = "test.java";
    int lineno = 10;
    int charno = 20;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    assertEquals(reference, namedType.getReferenceName());
    assertEquals(sourceName, namedType.getSourceName());
    assertEquals(lineno, namedType.getLineNumber());
    assertEquals(charno, namedType.getCharNumber());
  }

  @SuppressWarnings("unchecked")
  @org.junit.Test
  public void testNamedTypeDefineProperty() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "test";
    String sourceName = "test.java";
    int lineno = 10;
    int charno = 20;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    JSType type = new ObjectType(registry);
    PropertyContinuation propertyContinuation = new PropertyContinuation(
        "propertyName", type, false, newNameExpression("name"));

    namedType.defineProperty("propertyName", type, false, null);

    List<PropertyContinuation> propertyContinuations = namedType.getPropertyContinuations();

    assertEquals(1, propertyContinuations.size());
    assertEquals(propertyContinuation, propertyContinuations.get(0));
  }

  @SuppressWarnings("unchecked")
  @org.junit.Test
  public void testNamedTypeFinishPropertyContinuations() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "java.lang.String";
    String sourceName = "test.java";
    int lineno = 10;
    int charno = 20;

    NamedType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    ObjectType referencedObjType = new ObjectType(registry);
    referencedObjType.setReferencedType(registry.getType("java.lang.Object"));

    namedType.finishPropertyContinuations();

    assertEquals(referencedObjType, namedType.getReferencedObjTypeInternal());
  }

  @SuppressWarnings("unchecked")
  @org.junit.Test
  public void testNamedTypeGetReferencedType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "java.lang.String";

    NamedType namedType = new NamedType(registry, reference, null, 0, 0);

    assertEquals(registry.getType(reference), namedType.getReferencedType());
  }

  @SuppressWarnings("unchecked")
  @org.junit.Test
  public void testNamedTypeResolveInternal() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter errorReporter = new ErrorReporter();
    StaticScope<JSType> enclosing = new StaticScope<>();

    String reference = "java.lang.String";

    NamedType namedType = new NamedType(registry, reference, null, 0, 0);

    // Test resolveViaRegistry
    assertFalse(namedType.resolveInternal(errorReporter, enclosing).isResolved());
    assertEquals(registry.getType(reference), namedType.getReferencedType());

    // Test resolveViaProperties (simple case)
    registry.addType(new ObjectType(registry));
    registry.addType(new EnumType(registry, "EnumType", new ObjectType(registry)));

    JSType referencedType = registry.getType("EnumType.EnumConstant");
    referencedType.setProperty("constantName", new ObjectType(registry));

    namedType.setProperty(" EnumType ", registry.getType("EnumType"));
    namedType.setProperty("EnumConstant", referencedType);

    assertTrue(namedType.resolveInternal(errorReporter, enclosing).isResolved());
    assertEquals(referencedType, namedType.getReferencedType());

    // Test resolveViaProperties (implicit prototype cycle)
    namedType = new NamedType(registry, "Object", null, 0, 0);
    namedType.setProperty(" prototype ", registry.getPrototypeType());
    namedType.setProperty(" __proto__ ", registry.getPrototypeType());

    namedType.resolveInternal(errorReporter, enclosing);

    assertEquals(registry.getPrototypeType(), namedType.getReferencedType());

    // Test resolveViaProperties (explicit prototype cycle)
    namedType = new NamedType(registry, "ObjectWithCycle", null, 0, 0);
    ObjectType objType = new ObjectType(registry);
    objType.setPrototype(namedType);
    namedType.setProperty("prototype", objType);

    namedType.resolveInternal(errorReporter, enclosing);

    assertEquals(namedType, namedType.getReferencedType());

    // Test resolveViaProperties (unknown type)
    namedType = new NamedType(registry, "UnknownType", null, 0, 0);
    namedType.setProperty("prototype", registry.getPrototypeType());
    namedType.resolveInternal(errorReporter, enclosing);

    assertFalse(namedType.isResolved());

    // Test handleUnresolvedType
    errorReporter.reset();
    namedType.handleUnresolvedType(errorReporter, true);

    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_RESOLVED_TYPE),
        namedType.getReferencedType());

    // Test handleUnresolvedType (ignoreForwardReferencedTypes = false)
    errorReporter.reset();
    namedType.handleUnresolvedType(errorReporter, false);

    assertEquals(registry.getNativeObjectType(JSTypeNative.NO_TYPE),
        namedType.getReferencedType());

    errorReporter.warning("", null, 0, 0);
    List<String> warnings = errorReporter.getAllWarnings();

    assertEquals(1, warnings.size());
    assertTrue(warnings.get(0).startsWith("Bad type annotation. Unknown type"));
  }

  @org.junit.Test
  public void testResolveViaRegistry() {
    JSTypeRegistry registry = new JSTypeRegistry();

    assertFalse(NamedType.resolveViaRegistry(null, null).isResolved());

    JSType type = registry.getType("String");
    registry.addType(type);

    String reference = "String";

    NamedType namedType = new NamedType(registry, reference, null, 0, 0);

    assertTrue(NamedType.resolveViaRegistry(null, null).isResolved());
    assertEquals(type, namedType.getReferencedType());
  }

  @org.junit.Test
  public void testResolveViaProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType type1 = new ObjectType(registry);
    String propertyName1 = "property1";
    type1.setProperty(propertyName1, new ObjectType(registry));

    JSType type2 = new ObjectType(registry);
    JSType type3 = new ObjectType(registry);
    type2.setPrototype(type1);
    type2.setProperty(propertyName1, type3);

    String reference = "type2." + propertyName1;

    NamedType namedType = new NamedType(registry, reference, null, 0, 0);

    namedType.setProperty("type1", type1);
    namedType.setProperty("type2", type2);

    assertTrue(NamedType.resolveViaProperties(null, null).isResolved());
    assertEquals(type3, namedType.getReferencedType());
  }

  @org.junit.Test
  public void testLookupViaProperties() {
    JSTypeRegistry registry = new JSTypeRegistry();

    JSType type1 = new ObjectType(registry);
    String propertyName1 = "property1";
    type1.setProperty(propertyName1, new ObjectType(registry));

    JSType type2 = new ObjectType(registry);
    JSType type3 = new ObjectType(registry);
    type2.setPrototype(type1);
    type2.setProperty(propertyName1, type3);

    String reference = "type2." + propertyName1;

    NamedType namedType = new NamedType(registry, reference, null, 0, 0);

    namedType.setProperty("type1", type1);

    JSType resolvedType = NamedType.lookupViaProperties(null, null);

    assertNull(resolvedType);

    namedType.setProperty("type2", type2);

    resolvedType = NamedType.lookupViaProperties(null, null);

    assertEquals(type3, resolvedType);
  }

  @org.junit.Test
  public void testSetReferencedAndResolvedType() {
    JSTypeRegistry registry = new JSTypeRegistry();

    String reference = "String";

    NamedType namedType = new NamedType(registry, reference, null, 0, 0);

    JSType type = registry.getType(reference);

    namedType.setReferencedAndResolvedType(type, null, null);

    assertEquals(type, namedType.getReferencedType());
  }

  @org.junit.Test
  public void testCheckEnumElementCycle() {
    JSTypeRegistry registry = new JSTypeRegistry();

    String reference = "EnumType.EnumConstant";

    NamedType namedType = new NamedType(registry, reference, null, 0, 0);

    EnumType enumType = new EnumType(registry, "EnumType", new ObjectType(registry));
    EnumElementType enumElementType = new EnumElementType(registry, "EnumConstant", enumType);

    namedType.setReferencedAndResolvedType(enumElementType, null, null);

    namedType.checkEnumElementCycle(null);

    assertTrue(NamedType.detectImplicitPrototypeCycle(null));
  }

  @org.junit.Test
  public void testHandleTypeCycle() {
    JSTypeRegistry registry = new JSTypeRegistry();

    String reference = "Object";

    NamedType namedType = new NamedType(registry, reference, null, 0, 0);

    namedType.handleTypeCycle(null);

    assertEquals(registry.getNativeObjectType(JSTypeNative.UNKNOWN_TYPE), namedType.getReferencedType());
  }

}