import static org.junit.Assert.*;
import static com.google.javascript.rhino.jstype.JSType.*;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ArrowType;
import org.junit.Test;

public class ArrowTypeTest {
  @Test
  public void testArrowTypeConstruction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newObject();
    JSType returnType = INT;
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);
    assertEquals(INT.getBackingType(), arrowType.returnType.getBackingType());
    assertEquals(parameters, arrowType.parameters);
    assertFalse(arrowType.returnTypeInferred);

    Node otherParameters = Node.newArray(INT.getType(registry), STRING.getType(registry));
    JSType otherReturnType = BOOL;
    ArrowType otherArrowType = new ArrowType(registry, otherParameters, otherReturnType, true);
    assertNotSame(arrowType, otherArrowType);
    assertFalse(arrowType.isSubtype(otherArrowType));
  }

  @Test
  public void testArrowTypeWithInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node parameters = Node.newArray(INT.getType(registry), STRING.getType(registry));
    ArrowType arrowType = new ArrowType(registry, parameters, null, true);
    assertTrue(arrowType.returnTypeInferred);
  }

  @Test
  public void testArrowTypeIsSubtype() {
    JSTypeRegistry registry = new JSTypeRegistry();
    Node intParameters = Node.newArray(INT.getType(registry), INT.getType(registry));
    Node stringParameters = Node.newArray(INT.getType(registry), STRING.getType(registry));
    JSType intReturnType = INT;
    JSType stringReturnType = STRING;

    ArrowType intIntArrowType = new ArrowType(registry, intParameters, intReturnType, false);
    ArrowType intStringArrowType = new ArrowType(registry, intParameters, stringReturnType, false);
    ArrowType stringIntArrowType = new ArrowType(registry, stringParameters, intReturnType, false);
    ArrowType stringStringArrowType = new ArrowType(registry, stringParameters, stringReturnType, false);

    assertTrue(intIntArrowType.isSubtype(intIntArrowType));
    assertTrue(intIntArrowType.isSubtype(intStringArrowType)); // covariant
    assertFalse(intIntArrowType.isSubtype(stringIntArrowType)); // contravariant
    assertFalse(intIntArrowType.isSubtype(stringStringArrowType));

    assertTrue(stringIntArrowType.isSubtype(intIntArrowType)); // Promotion
    assertTrue(stringIntArrowType.isSubtype(intStringArrowType));
    assertFalse(stringIntArrowType.isSubtype(stringStringArrowType));

    assertTrue(stringStringArrowType.isSubtype(intIntArrowType)); // Promotion
    assertTrue(stringStringArrowType.isSubtype(intStringArrowType));
    assertTrue(stringStringArrowType.isSubtype(stringIntArrowType));
    assertTrue(stringStringArrowType.isSubtype(stringStringArrowType));
  }
}