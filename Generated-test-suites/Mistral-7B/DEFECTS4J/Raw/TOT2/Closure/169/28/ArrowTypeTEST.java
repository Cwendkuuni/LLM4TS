import static org.junit.Assert.*;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeNative;
import com.google.javascript.rhino.jstype.ArrowType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSTypeRegistryFactory;
import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeWithValidParametersAndReturnType() {
    // Given
    JSTypeRegistry registry = JSTypeRegistryFactory.create();
    Node parameters = registry.createParametersWithVarArgs(
        registry.createNamedType("Number"));
    JSType returnType = registry.createNamedType("String");

    // When
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    // Then
    assertTrue(arrowType.isSubtype(arrowType));
  }

  @Test
  public void testArrowTypeWithEmptyParametersAndReturnType() {
    // Given
    JSTypeRegistry registry = JSTypeRegistryFactory.create();

    // When
    ArrowType arrowType = new ArrowType(registry, null, null);

    // Then
    assertTrue(arrowType.isSubtype(arrowType));
  }

  @Test
  public void testArrowTypeWithNullParametersAndReturnType() {
    // Given
    JSTypeRegistry registry = JSTypeRegistryFactory.create();

    // When
    ArrowType arrowType = new ArrowType(registry, null, null);

    // Then
    assertFalse(arrowType.isSubtype(new FunctionType(new JSType[0])));
  }

  @Test(expected = IllegalArgumentException.class)
  public void testArrowTypeWithInvalidParametersAndReturnType() {
    // Given
    JSTypeRegistry registry = JSTypeRegistryFactory.create();
    Node parameters = registry.createParametersWithVarArgs(
        registry.createNamedType("Number"));
    JSType returnType = registry.createNamedType("String");

    // When and Then
    new ArrowType(registry, null, returnType);
  }

  @Test
  public void testArrowTypeWithEmptyParametersAndUnknownReturnType() {
    // Given
    JSTypeRegistry registry = JSTypeRegistryFactory.create();

    // When
    ArrowType arrowType = new ArrowType(registry, null, null);

    // Then
    assertTrue(arrowType.isSubtype(arrowType));
  }

  @Test
  public void testArrowTypeWithParametersAndUnknownReturnType() {
    // Given
    JSTypeRegistry registry = JSTypeRegistryFactory.create();
    Node parameters = registry.createParametersWithVarArgs(
        registry.createNamedType("Number"));

    // When
    ArrowType arrowType = new ArrowType(registry, parameters, null);

    // Then
    assertTrue(arrowType.isSubtype(arrowType));
  }

  @Test
  public void testArrowTypeWithParametersAndInferredReturnType() {
    // Given
    JSTypeRegistry registry = JSTypeRegistryFactory.create();
    Node parameters = registry.createParametersWithVarArgs(
        registry.createNamedType("Number"));

    // When
    ArrowType arrowType = new ArrowType(registry, parameters, null, true);

    // Then
    assertTrue(arrowType.isSubtype(arrowType));
  }
}