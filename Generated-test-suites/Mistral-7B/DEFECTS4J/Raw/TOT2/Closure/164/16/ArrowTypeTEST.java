
import static org.junit.Assert.*;
import static com.google.javascript.rhino.type.TypeFactory.*;

import com.google.javascript.rhino.jstype.*;
import org.junit.Test;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeConstructor() {
    ArrowType arrowType = new ArrowType(TypeFactory.getRegistry(), null, null);
    assertTrue(arrowType.returnTypeInferred);
    assertEquals(UNKNOWN_TYPE, arrowType.returnType.getType());
    assertNull(arrowType.parameters);
  }

  @Test
  public void testArrowTypeConstructorWithArguments() {
    JSType returnType = createStringType();
    Node parametersNode = createListType(TypeFactory.getRegistry(), createNumberType());
    ArrowType arrowType = new ArrowType(TypeFactory.getRegistry(), parametersNode, returnType);
    assertFalse(arrowType.returnTypeInferred);
    assertEquals(returnType, arrowType.returnType);
    assertNotNull(arrowType.parameters);
    assertEquals(createNumberType(), arrowType.parameters.getFirstChild().getJSType());
  }

  @Test
  public void testArrowTypeIsSubtype() {
    JSType returnType1 = createStringType();
    JSType returnType2 = createNumberType();
    ArrowType arrowType1 = new ArrowType(TypeFactory.getRegistry(), null, returnType1);
    ArrowType arrowType2 = new ArrowType(TypeFactory.getRegistry(), null, returnType2);
    assertTrue(arrowType1.isSubtype(arrowType2));

    JSType paramType1 = createNumberType();
    JSType paramType2 = createStringType();
    ArrowType arrowType1WithParams = new ArrowType(TypeFactory.getRegistry(), paramType1, returnType1);
    ArrowType arrowType2WithParams = new ArrowType(TypeFactory.getRegistry(), paramType2, returnType2);
    assertFalse(arrowType1WithParams.isSubtype(arrowType2WithParams));

    // Edge case: arrow types with the same return type and same number of parameters, but different parameter types
    ArrowType arrowType1SameParams = new ArrowType(TypeFactory.getRegistry(), paramType1, returnType1);
    ArrowType arrowType2SameParams = new ArrowType(TypeFactory.getRegistry(), createBooleanType(), returnType1);
    assertFalse(arrowType1SameParams.isSubtype(arrowType2SameParams));
  }

  @Test
  public void testArrowTypeHasEqualParameters() {
    JSType paramType1 = createNumberType();
    JSType paramType2 = createNumberType();
    ArrowType arrowType1 = new ArrowType(TypeFactory.getRegistry(), paramType1, null);
    ArrowType arrowType2 = new ArrowType(TypeFactory.getRegistry(), paramType2, null);
    ArrowType arrowType3 = new ArrowType(TypeFactory.getRegistry(), paramType1, null);
    assertTrue(arrowType1.hasEqualParameters(arrowType1));
    assertTrue(arrowType1.hasEqualParameters(arrowType3));
    assertFalse(arrowType1.hasEqualParameters(arrowType2));
  }

  @Test
  public void testArrowTypeHasEqualParametersWithDifferentReturnTypes() {
    JSType paramType = createNumberType();
    ArrowType arrowType1 = new ArrowType(TypeFactory.getRegistry(), paramType, createStringType());
    ArrowType arrowType2 = new ArrowType(TypeFactory.getRegistry(), paramType, createNumberType());
    assertFalse(arrowType1.hasEqualParameters(arrowType2));
  }

  @Test
  public void testArrowTypeConstructorWithNullParameters() {
    assertThrows(IllegalArgumentException.class, () -> new ArrowType(TypeFactory.getRegistry(), null, null));
  }

  @Test
  public void testArrowTypeConstructorWithNullReturnType() {
    assertThrows(IllegalArgumentException.class, () -> new ArrowType(TypeFactory.getRegistry(), null, null));
  }

  @Test
  public void testArrowTypeConstructorWithInvalidReturnType() {
    Node invalidNode = createInvalidNode();
    assertThrows(IllegalArgumentException.class, () -> new ArrowType(TypeFactory.getRegistry(), null, invalidNode));
  }
}