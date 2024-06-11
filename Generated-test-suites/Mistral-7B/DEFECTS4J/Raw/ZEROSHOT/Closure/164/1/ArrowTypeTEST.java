import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.JSTYPE;
import static com.google.javascript.rhino.JSType.asType;
import static com.google.javascript.rhino.JSType.getNativeType;
import static com.google.javascript.rhino.JSType.createParametersWithVarArgs;
import static com.google.javascript.rhino.JSType.createFunctionType;

import org.junit.Test;

import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.StaticScope;

public class ArrowTypeTest {

  @Test
  public void testArrowTypeConstruction() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<JSType>(registry);

    Node parameters = createParametersWithVarArgs(getNativeType(JSTYPE.NUMBER));
    JSType returnType = getNativeType(JSTYPE.STRING);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertEquals(JSTYPE.ARROW, arrowType.getType());
    assertEquals(parameters, arrowType.getParameters());
    assertEquals(returnType, arrowType.getReturnType());
    assertFalse(arrowType.isReturnTypeInferred());
  }

  @Test
  public void testArrowTypeWithInferredReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<JSType>(registry);

    Node parameters = createParametersWithVarArgs(getNativeType(JSTYPE.NUMBER));
    ArrowType arrowType = new ArrowType(registry, parameters, null, true);

    assertEquals(JSTYPE.ARROW, arrowType.getType());
    assertEquals(parameters, arrowType.getParameters());
    assertNull(arrowType.getReturnType());
    assertTrue(arrowType.isReturnTypeInferred());
  }

  @Test
  public void testArrowTypeWithNullReturnType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<JSType>(registry);

    Node parameters = createParametersWithVarArgs(getNativeType(JSTYPE.NUMBER));
    ArrowType arrowType = new ArrowType(registry, parameters, null, false);

    assertEquals(JSTYPE.ARROW, arrowType.getType());
    assertEquals(parameters, arrowType.getParameters());
    assertNull(arrowType.getReturnType());
    assertFalse(arrowType.isReturnTypeInferred());
  }

  @Test
  public void testArrowTypeIsSubtypeOfArrowType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<JSType>(registry);

    Node parameters1 = createParametersWithVarArgs(getNativeType(JSTYPE.NUMBER));
    JSType returnType1 = getNativeType(JSTYPE.STRING);
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);

    Node parameters2 = createParametersWithVarArgs(getNativeType(JSTYPE.NUMBER));
    JSType returnType2 = getNativeType(JSTYPE.NUMBER);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

    assertTrue(arrowType1.isSubtype(arrowType1));
    assertTrue(arrowType1.isSubtype(arrowType2));
    assertTrue(arrowType2.isSubtype(arrowType1));
    assertTrue(arrowType2.isSubtype(arrowType2));
  }

  @Test
  public void testArrowTypeIsNotSubtypeOfFunctionType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<JSType>(registry);

    Node parameters = createParametersWithVarArgs(getNativeType(JSTYPE.NUMBER));
    JSType returnType = getNativeType(JSTYPE.STRING);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    JSType functionType = createFunctionType(JSTYPE.VOID, createParametersWithVarArgs(getNativeType(JSTYPE.NUMBER)), returnType);

    assertFalse(arrowType.isSubtype(functionType));
    assertFalse(functionType.isSubtype(arrowType));
  }

  @Test
  public void testArrowTypeHasEqualParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<JSType>(registry);

    Node parameters1 = createParametersWithVarArgs(getNativeType(JSTYPE.NUMBER));
    JSType returnType1 = getNativeType(JSTYPE.STRING);
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);

    Node parameters2 = createParametersWithVarArgs(getNativeType(JSTYPE.NUMBER));
    JSType returnType2 = getNativeType(JSTYPE.STRING);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

    assertTrue(arrowType1.hasEqualParameters(arrowType2));

    parameters2 = createParametersWithVarArgs(getNativeType(JSTYPE.NUMBER), getNativeType(JSTYPE.STRING));
    assertFalse(arrowType1.hasEqualParameters(new ArrowType(registry, parameters2, returnType1)));
  }

  @Test
  public void testArrowTypeIsEquivalentToArrowType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<JSType>(registry);

    Node parameters1 = createParametersWithVarArgs(getNativeType(JSTYPE.NUMBER));
    JSType returnType1 = getNativeType(JSTYPE.STRING);
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType1);

    Node parameters2 = createParametersWithVarArgs(getNativeType(JSTYPE.NUMBER));
    JSType returnType2 = getNativeType(JSTYPE.STRING);
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType2);

    ArrowType arrowType3 = new ArrowType(registry, parameters1, returnType1);

    assertEquals(arrowType1, arrowType3);
    assertEquals(arrowType1.hashCode(), arrowType3.hashCode());
    assertTrue(arrowType1.isEquivalentTo(arrowType1));
    assertTrue(arrowType1.isEquivalentTo(arrowType3));
    assertTrue(arrowType3.isEquivalentTo(arrowType1));
    assertTrue(arrowType1.isEquivalentTo(arrowType2));
    assertTrue(arrowType2.isEquivalentTo(arrowType1));
  }

  @Test
  public void testArrowTypeIsNotEquivalentToFunctionType() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<JSType>(registry);

    Node parameters = createParametersWithVarArgs(getNativeType(JSTYPE.NUMBER));
    JSType returnType = getNativeType(JSTYPE.STRING);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    JSType functionType = createFunctionType(getNativeType(JSTYPE.VOID), createParametersWithVarArgs(getNativeType(JSTYPE.NUMBER)), returnType);

    assertFalse(arrowType.isEquivalentTo(functionType));
  }

  @Test
  public void testArrowTypeIsNotEquivalentToDifferentReturnTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<JSType>(registry);

    Node parameters = createParametersWithVarArgs(getNativeType(JSTYPE.NUMBER));
    JSType returnType1 = getNativeType(JSTYPE.STRING);
    JSType returnType2 = getNativeType(JSTYPE.NUMBER);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType1);

    assertFalse(arrowType.isEquivalentTo(new ArrowType(registry, parameters, returnType2)));
  }

  @Test
  public void testArrowTypeIsNotEquivalentToDifferentParameters() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<JSType>(registry);

    Node parameters1 = createParametersWithVarArgs(getNativeType(JSTYPE.NUMBER));
    JSType returnType = getNativeType(JSTYPE.STRING);
    ArrowType arrowType1 = new ArrowType(registry, parameters1, returnType);

    Node parameters2 = createParametersWithVarArgs(getNativeType(JSTYPE.STRING));
    ArrowType arrowType2 = new ArrowType(registry, parameters2, returnType);

    assertFalse(arrowType1.isEquivalentTo(arrowType2));
  }

  @Test
  public void testArrowTypeIsSubtypeOfSelf() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<JSType>(registry);

    Node parameters = createParametersWithVarArgs(getNativeType(JSTYPE.NUMBER));
    JSType returnType = getNativeType(JSTYPE.STRING);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    assertTrue(arrowType.isSubtype(arrowType));
  }

  @Test
  public void testArrowTypeWithUnknownParamsOrReturnTypeIsSubtypeOfArrowTypeWithKnownTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<JSType>(registry);

    Node parameters = createParametersWithVarArgs(getNativeType(JSTYPE.NUMBER));
    JSType returnType = getNativeType(JSTYPE.STRING);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    ArrowType arrowTypeWithUnknownTypes = new ArrowType(registry, parameters, null);

    assertTrue(arrowType.isSubtype(arrowTypeWithUnknownTypes));
    assertTrue(arrowTypeWithUnknownTypes.isSubtype(arrowType));

    // Test for subtyping with a return type that is unknown
    JSType arrowTypeWithUnknownReturnType = new ArrowType(registry, null, returnType);

    assertTrue(arrowType.isSubtype(arrowTypeWithUnknownReturnType));
    assertTrue(arrowTypeWithUnknownReturnType.isSubtype(arrowType));

    // Test for subtyping with a parameter that is unknown
    Node unknownParameters = createParametersWithVarArgs(getNativeType(JSTYPE.UNKNOWN_TYPE));
    ArrowType arrowTypeWithUnknownParameters = new ArrowType(registry, unknownParameters, returnType);

    assertTrue(arrowType.isSubtype(arrowTypeWithUnknownParameters));
    assertTrue(arrowTypeWithUnknownParameters.isSubtype(arrowType));
  }

  @Test
  public void testArrowTypeWithUnknownParamsOrReturnTypeIsNotSubtypeOfArrowTypeWithKnownTypes() {
    JSTypeRegistry registry = new JSTypeRegistry();
    ErrorReporter t = new ErrorReporter();
    StaticScope<JSType> scope = new StaticScope<JSType>(registry);

    Node parameters = createParametersWithVarArgs(getNativeType(JSTYPE.NUMBER));
    JSType returnType = getNativeType(JSTYPE.STRING);
    ArrowType arrowType = new ArrowType(registry, parameters, returnType);

    ArrowType arrowTypeWithUnknownTypes = new ArrowType(registry, parameters, null);

    assertFalse(arrowTypeWithUnknownTypes.isSubtype(arrowType));
    assertFalse(arrowType.isSubtype(arrowTypeWithUnknownTypes));

    // Test for subtyping with a return type that is unknown
    JSType arrowTypeWithUnknownReturnType = new ArrowType(registry, parameters, null);

    assertFalse(arrowType.isSubtype(arrowTypeWithUnknownReturnType));
    assertFalse(arrowTypeWithUnknownReturnType.isSubtype(arrowType));

    // Test for subtyping with a parameter that is unknown
    Node unknownParameters = createParametersWithVarArgs(getNativeType(JSTYPE.UNKNOWN_TYPE));
    ArrowType arrowTypeWithUnknownParameters = new ArrowType(registry, unknownParameters, returnType);

    assertFalse(arrowTypeWithUnknownParameters.isSubtype(arrowType));
    assertFalse(arrowType.isSubtype(arrowTypeWithUnknownParameters));
  }
}