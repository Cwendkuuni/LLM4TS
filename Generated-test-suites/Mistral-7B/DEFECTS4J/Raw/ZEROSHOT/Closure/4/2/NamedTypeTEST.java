import static org.junit.Assert.*;
import static com.google.javascript.rhino.JDTUtil.createErrorReporter;
import static com.google.javascript.rhino.JDTUtil.createStaticScope;
import static com.google.javascript.rhino.JSTypeNative.NO_RESOLVED_TYPE;
import static com.google.javascript.rhino.JSTypeNative.UNKNOWN_TYPE;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeFactory;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.error.ErrorReporter;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class NamedTypeTest {

  private ErrorReporter errorReporter;
  private StaticScope<JSType> scope;
  private JSTypeFactory typeFactory;

  @Before
  public void setUp() {
    errorReporter = createErrorReporter();
    scope = createStaticScope(typeFactory, errorReporter);
    typeFactory = new JSTypeFactory(errorReporter);
  }

  @Test
  public void testCreateNamedType() {
    String reference = "MyType";
    String sourceName = "MySourceFile.js";
    int lineno = 1;
    int charno = 1;

    JSTypeRegistry registry = new JSTypeRegistry();
    JSType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    assertEquals(reference, namedType.getReferenceName());
    assertEquals(UNKNOWN_TYPE, namedType.getBaseType());
    assertTrue(namedType.isNamedType());
    assertTrue(namedType.isNominalType());
  }

  @Test
  public void testCreateNamedTypeWithResolvedType() {
    String reference = "MyType";
    String sourceName = "MySourceFile.js";
    int lineno = 1;
    int charno = 1;
    JSType resolvedType = new ObjectType(typeFactory, "MyType", null, null);

    JSTypeRegistry registry = new JSTypeRegistry();
    JSType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    assertEquals(reference, namedType.getReferenceName());
    assertEquals(resolvedType, namedType.getReferencedType());
    assertEquals(resolvedType, namedType.getResolvedType());
    assertTrue(namedType.isNamedType());
    assertTrue(namedType.isNominalType());
  }

  @Test
  public void testCreateNamedTypeWithUnresolvedType() {
    String reference = "MyType";
    String sourceName = "MySourceFile.js";
    int lineno = 1;
    int charno = 1;

    JSTypeRegistry registry = new JSTypeRegistry();
    JSType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    assertEquals(UNKNOWN_TYPE, namedType.getReferencedType());
    assertEquals(UNKNOWN_TYPE, namedType.getResolvedType());
    assertTrue(namedType.isNamedType());
    assertTrue(namedType.isNominalType());
  }

  @Test
  public void testDefineProperty() {
    String reference = "MyType";
    String sourceName = "MySourceFile.js";
    int lineno = 1;
    int charno = 1;

    JSTypeRegistry registry = new JSTypeRegistry();
    JSType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    JSTypeRegistry registry2 = new JSTypeRegistry();
    JSTypeRegistry registry3 = new JSTypeRegistry();
    JSTypeRegistry registry4 = new JSTypeRegistry();

    JSType parentType1 = new ObjectType(typeFactory, "ParentType", null, null);
    JSType parentType2 = new ObjectType(typeFactory, "ParentType2", null, null);

    JSType typeToBeDefined = new ObjectType(typeFactory, "TypeToBeDefined", null, null);
    JSType resolvedType = new ObjectType(typeFactory, "ResolvedType", null, null);

    PropertyContinuation propertyContinuation = null;

    namedType.defineProperty("property", typeToBeDefined, false, null);
    assertEquals(typeToBeDefined, namedType.getPropertyType("property"));

    namedType.finishPropertyContinuations();
    assertEquals(typeToBeDefined, namedType.getPropertyType("property"));

    assertEquals(UNKNOWN_TYPE, namedType.getPropertyType("property2"));

    namedType.setReferencedType(resolvedType);
    namedType.finishPropertyContinuations();
    assertEquals(resolvedType, namedType.getPropertyType("property"));

    namedType.setValidator(jstype -> {
      propertyContinuation = (PropertyContinuation) jstype;
      assertEquals("property", propertyContinuation.propertyName);
      assertEquals(typeToBeDefined, propertyContinuation.type);
      assertEquals(false, propertyContinuation.inferred);
      assertEquals(null, propertyContinuation.propertyNode);
    });

    namedType.defineProperty("property2", new ObjectType(typeFactory, "PropertyType2", null, null), false, null);
    namedType.finishPropertyContinuations();
    assertEquals(new ObjectType(typeFactory, "PropertyType2", null, null), namedType.getPropertyType("property2"));
  }

  @Test
  public void testResolveViaRegistry() {
    String reference = "MyType";
    String sourceName = "MySourceFile.js";
    int lineno = 1;
    int charno = 1;

    JSTypeRegistry registry = new JSTypeRegistry();
    JSTypeRegistry registry2 = new JSTypeRegistry();

    JSType resolvedType = new ObjectType(typeFactory, "ResolvedType", null, null);
    registry.registerType(reference, resolvedType);

    JSType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    assertEquals(resolvedType, namedType.getReferencedType());
    assertEquals(resolvedType, namedType.getResolvedType());
  }

  @Test
  public void testResolveViaProperties() {
    String reference = "ParentType.ChildType";
    String sourceName = "MySourceFile.js";
    int lineno = 1;
    int charno = 1;

    JSTypeRegistry registry = new JSTypeRegistry();
    JSTypeRegistry registry2 = new JSTypeRegistry();

    ObjectType parentType = new ObjectType(typeFactory, "ParentType", null, null);
    ObjectType childType = new ObjectType(typeFactory, "ChildType", null, null);
    registry.registerType("ParentType", parentType);

    StaticScope<JSType> staticScope = createStaticScope(typeFactory, errorReporter);
    staticScope.define("ParentType", parentType);

    JSType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    assertEquals(childType, namedType.getReferencedType());
    assertEquals(childType, namedType.getResolvedType());
  }

  @Test
  public void testResolveViaPropertiesWithUnresolvedType() {
    String reference = "ParentType.ChildType";
    String sourceName = "MySourceFile.js";
    int lineno = 1;
    int charno = 1;

    JSTypeRegistry registry = new JSTypeRegistry();
    JSTypeRegistry registry2 = new JSTypeRegistry();

    ObjectType parentType = new ObjectType(typeFactory, "ParentType", null, null);
    registry.registerType("ParentType", parentType);

    StaticScope<JSType> staticScope = createStaticScope(typeFactory, errorReporter);
    staticScope.define("ParentType", parentType);

    JSType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    assertEquals(UNKNOWN_TYPE, namedType.getReferencedType());
    assertEquals(UNKNOWN_TYPE, namedType.getResolvedType());
  }

  @Test
  public void testResolveViaPropertiesWithUnrecognizedType() {
    String reference = "ParentType.ChildType";
    String sourceName = "MySourceFile.js";
    int lineno = 1;
    int charno = 1;

    JSTypeRegistry registry = new JSTypeRegistry();
    JSTypeRegistry registry2 = new JSTypeRegistry();

    ObjectType parentType = new ObjectType(typeFactory, "ParentType", null, null);
    registry.registerType("ParentType", parentType);

    StaticScope<JSType> staticScope = createStaticScope(typeFactory, errorReporter);
    staticScope.define("ParentType", parentType);

    JSType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    assertEquals(UNKNOWN_TYPE, namedType.getReferencedType());
    assertEquals(UNKNOWN_TYPE, namedType.getResolvedType());

    errorReporter.flush();
    List<String> messages = errorReporter.getMessages();
    assertEquals(1, messages.size());
    assertEquals("Type 'ParentType.ChildType' not found", messages.get(0));
  }

  @Test
  public void testResolveViaPropertiesWithCyclicTypeReference() {
    String reference = "ChildType.ParentType";
    String sourceName = "MySourceFile.js";
    int lineno = 1;
    int charno = 1;

    JSTypeRegistry registry = new JSTypeRegistry();
    JSTypeRegistry registry2 = new JSTypeRegistry();

    ObjectType childType = new ObjectType(typeFactory, "ChildType", null, null);
    ObjectType parentType = new ObjectType(typeFactory, "ParentType", null, null);
    registry.registerType("ParentType", parentType);
    registry.registerType("ChildType", childType);

    StaticScope<JSType> staticScope = createStaticScope(typeFactory, errorReporter);
    staticScope.define("ParentType", parentType);
    staticScope.define("ChildType", childType);

    JSType namedType = new NamedType(registry, reference, sourceName, lineno, charno);
    assertEquals(UNKNOWN_TYPE, namedType.getReferencedType());
    assertEquals(UNKNOWN_TYPE, namedType.getResolvedType());

    errorReporter.flush();
    List<String> messages = errorReporter.getMessages();
    assertEquals(1, messages.size());
    assertEquals("Cycle detected in inheritance chain of type " + reference, messages.get(0));
  }

  @Test
  public void testSetValidator() {
    String reference = "MyType";
    String sourceName = "MySourceFile.js";
    int lineno = 1;
    int charno = 1;

    JSTypeRegistry registry = new JSTypeRegistry();
    JSType namedType = new NamedType(registry, reference, sourceName, lineno, charno);

    Predicate<JSType> validator = jstype -> {
      assertEquals(reference, jstype.getReferenceName());
      return true;
    };

    namedType.setValidator(validator);
    JSType referencedType = namedType.getReferencedType();
    validator.apply(referencedType);
  }
}