import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.StaticScope;
import com.google.javascript.rhino.StaticSlot;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.NamedType;
import com.google.javascript.rhino.jstype.ObjectType;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class NamedTypeTest {

  @Test
  public void testNamedTypeCreation() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "MyNamedType";
    String sourceName = "MyClass.java";
    int lineno = 10;
    int charno = 20;

    NamedType myNamedType = new NamedType(registry, reference, sourceName, lineno, charno);

    // Basic functionality test
    assertEquals(reference, myNamedType.getReferenceName());
    assertEquals(true, myNamedType.isNamedType());
    assertEquals(true, myNamedType.isNominalType());

    // Test property definition continuation
    myNamedType.defineProperty("propertyName", JSType.INTEGER, false, null);
    ObjectType referencedObjType = myNamedType.getReferencedObjTypeInternal();
    assertNull(referencedObjType);
    myNamedType.finishPropertyContinuations();
    assertNotNull(referencedObjType);

    // Test getReferencedType
    JSType referencedType = myNamedType.getReferencedType();
    assertEquals(JSTypeNative.UNDEFINED, referencedType.getTypeCode());

    // Test resolution via registry
    registry.registerType(reference, JSType.BOOLEAN);
    referencedType = myNamedType.getReferencedType();
    assertEquals(JSType.BOOLEAN, referencedType);

    // Test resolution via properties
    StaticScope<JSType> enclosing = new StaticScope<>();
    StaticSlot<JSType> slot = enclosing.put(reference, JSType.STRING);
    myNamedType.resolveInternal(new ErrorReporter() {}, enclosing);
    referencedType = myNamedType.getReferencedType();
    assertEquals(JSType.STRING, referencedType);

    // Test getReferencedObjType
    referencedObjType = myNamedType.getReferencedObjTypeInternal();
    assertEquals(JSTypeNative.UNKNOWN_TYPE, referencedObjType.getTypeCode());

    // Test handleUnresolvedType
    myNamedType.handleUnresolvedType(new ErrorReporter() {}, false);
    referencedType = myNamedType.getReferencedType();
    assertEquals(JSTypeNative.NO_RESOLVED_TYPE, referencedType.getTypeCode());

    // Test resolveViaRegistry and resolveViaProperties
    registry.registerType(reference, ObjectType.cast(new Int32Type()));
    referencedType = myNamedType.resolveInternal(new ErrorReporter() {}, enclosing);
    assertEquals(ObjectType.cast(new Int32Type()), referencedType);
  }

  @Test
  public void testNamedTypeExceptionHandling() {
    JSTypeRegistry registry = new JSTypeRegistry();
    String reference = "MyNamedType";
    String sourceName = "MyClass.java";
    int lineno = 10;
    int charno = 20;

    NamedType myNamedType = new NamedType(registry, reference, sourceName, lineno, charno);

    // Test cycle detection
    myNamedType.setReferencedType(registry.getType("MyNamedType"));
    myNamedType.resolveInternal(new ErrorReporter() {}, new StaticScope<>());

    // Test null referenced type
    myNamedType.setReferencedType(null);
    myNamedType.resolveInternal(new ErrorReporter() {}, new StaticScope<>());

    // Test invalid referenced type
    myNamedType.setReferencedType(new Int32Type());
    myNamedType.resolveInternal(new ErrorReporter() {}, new StaticScope<>());

    // Test unregistered referenced type
    registry.registerType(reference, JSType.BOOLEAN);
    myNamedType.resolveInternal(new ErrorReporter() {}, new StaticScope<>());
    myNamedType.setReferencedType(registry.getType(reference));
    registry.unregisterType(reference);
    myNamedType.resolveInternal(new ErrorReporter() {}, new StaticScope<>());
  }
}