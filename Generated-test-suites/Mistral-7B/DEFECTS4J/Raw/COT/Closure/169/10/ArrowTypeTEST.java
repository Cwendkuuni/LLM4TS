import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeFactory.createArrowFunction;
import static com.google.javascript.rhino.NodeFactory.createJSTypeRegistry;
import static com.google.javascript.rhino.jstype.JSTypeNative.UNKNOWN_TYPE;

import org.junit.Test;

import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.NodeFactory;
import com.google.javascript.rhino.types.BooleanLiteralSet;

public class ArrowTypeTest {

 private JSTypeRegistry registry;
 private ErrorReporter errorReporter;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 registry = createJSTypeRegistry();
 errorReporter = new ErrorReporter();
 }

 @Test
 public void testConstructor_NullParameters_CreatesArrowTypeWithUnknownTypeParameters() {
 ArrowType arrowType = new ArrowType(registry, null, registry.createUnknownType());
 assertEquals(UNKNOWN_TYPE, arrowType.parameters.getJSType());
 }

 @Test
 public void testConstructor_EmptyParameters_CreatesArrowTypeWithEmptyParameterList() {
 ArrowType arrowType = new ArrowType(registry, NodeFactory.createList(), registry.createUnknownType());
 assertTrue(arrowType.parameters.getChildCount() == 0);
 }

 @Test
 public void testConstructor_SingleRequiredParameter_CreatesArrowTypeWithSingleParameter() {
 Node functionNode = createArrowFunction(new Node("int"), new Node("int"), NodeFactory.createString("test"));
 ArrowType arrowType = new ArrowType(registry, functionNode.getParameters(), registry.createIntType());
 Node parameterNode = arrowType.parameters.getFirstChild();
 assertEquals(registry.createIntType(), parameterNode.getJSType());
 }

 @Test
 public void testConstructor_SingleOptionalParameter_CreatesArrowTypeWithOptionalParameter() {
 Node functionNode = createArrowFunction(new Node("int"), new Node("int?"), NodeFactory.createString("test"));
 ArrowType arrowType = new ArrowType(registry, functionNode.getParameters(), registry.createIntType());
 Node parameterNode = arrowType.parameters.getFirstChild();
 assertTrue(parameterNode.isOptionalArg());
 }

 @Test
 public void testConstructor_VariableParameters_CreatesArrowTypeWithVariableParameters() {
 Node functionNode = createArrowFunction(new Node("int"), new Node("int, ..."), NodeFactory.createString("test"));
 ArrowType arrowType = new ArrowType(registry, functionNode.getParameters(), registry.createIntType());
 Node variableArgsNode = arrowType.parameters.getLastChild();
 assertTrue(variableArgsNode.isVarArgs());
 }

 @Test
 public void testConstructor_MultipleParameters_CreatesArrowTypeWithMultipleParameters() {
 Node functionNode = createArrowFunction(new Node("int"), new Node("int, int"), NodeFactory.createString("test"));
 ArrowType arrowType = new ArrowType(registry, functionNode.getParameters(), registry.createIntType(), false);
 assertEquals(2, arrowType.parameters.getChildCount());
 }

 @Test
 public void testConstructor_InferredReturnType_CreatesArrowTypeWithInferredReturnType() {
 ArrowType arrowType = new ArrowType(registry, NodeFactory.createList(), null);
 assertTrue(arrowType.returnTypeInferred);
 }

 @Test
 public void testConstructor_ExplicitReturnType_CreatesArrowTypeWithExplicitReturnType() {
 ArrowType arrowType = new ArrowType(registry, NodeFactory.createList(), registry.createStringType());
 assertFalse(arrowType.returnTypeInferred);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetLeastSupertype_UnsupportedOperationException() {
 ArrowType arrowType = new ArrowType(registry, NodeFactory.createList(), registry.createIntType(), false);
 arrowType.getLeastSupertype(registry.createStringType());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetGreatestSubtype_UnsupportedOperationException() {
 ArrowType arrowType = new ArrowType(registry, NodeFactory.createList(), registry.createIntType(), false);
 arrowType.getGreatestSubtype(registry.createStringType());
 }

 @Test
 public void testHasEqualParameters_SameParameters_ReturnsTrue() {
 ArrowType arrowType1 = new ArrowType(registry, NodeFactory.createList(
 NodeFactory.createParameter(registry.createIntType()),
 NodeFactory.createParameter(registry.createIntType())), registry.createIntType());
 ArrowType arrowType2 = new ArrowType(registry, NodeFactory.createList(
 NodeFactory.createParameter(registry.createIntType()),
 NodeFactory.createParameter(registry.createIntType())), registry.createIntType());
 assertTrue(arrowType1.hasEqualParameters(arrowType2, false));
 }

 @Test
 public void testHasEqualParameters_DifferentParameters_ReturnsFalse() {
 ArrowType arrowType1 = new ArrowType(registry, NodeFactory.createList(
 NodeFactory.createParameter(registry.createIntType()),
 NodeFactory.createParameter(registry.createIntType())), registry.createIntType());
 ArrowType arrowType2 = new ArrowType(registry, NodeFactory.createList(
 NodeFactory.createParameter(registry.createStringType()),
 NodeFactory.createParameter(registry.createIntType())), registry.createIntType());
 assertFalse(arrowType1.hasEqualParameters(arrowType2, false));
 }

 @Test
 public void testHasEqualParameters_TolerateUnknowns_ReturnsTrueIfUnknownTypesAreEqual() {
 ArrowType arrowType1 = new ArrowType(registry, NodeFactory.createList(
 NodeFactory.createParameter(registry.createIntType()),
 NodeFactory.createParameter(registry.createStringType())), registry.createIntType());
 ArrowType arrowType2 = new ArrowType(registry, NodeFactory.createList(
 NodeFactory.createParameter(registry.createUnknownType()),
 NodeFactory.createParameter(registry.createUnknownType())), registry.createIntType());
 assertTrue(arrowType1.hasEqualParameters(arrowType2, true));
 }

 @Test
 public void testCheckArrowEquivalenceHelper_SameTypes_ReturnsTrue() {
 ArrowType arrowType1 = new ArrowType(registry, NodeFactory.createList(
 NodeFactory.createParameter(registry.createIntType()),
 NodeFactory.createParameter(registry.createIntType())), registry.createIntType());
 ArrowType arrowType2 = new ArrowType(registry, NodeFactory.createList(
 NodeFactory.createParameter(registry.createIntType()),
 NodeFactory.createParameter(registry.createIntType())), registry.createIntType());
 assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));
 }

 @Test
 public void testCheckArrowEquivalenceHelper_DifferentTypes_ReturnsFalse() {
 ArrowType arrowType1 = new ArrowType(registry, NodeFactory.createList(
 NodeFactory.createParameter(registry.createIntType()),
 NodeFactory.createParameter(registry.createIntType())), registry.createIntType());
 ArrowType arrowType2 = new ArrowType(registry, NodeFactory.createList(
 NodeFactory.createParameter(registry.createStringType()),
 NodeFactory.createParameter(registry.createIntType())), registry.createIntType());
 assertFalse(arrowType1.checkArrowEquivalenceHelper(arrowType2, false));
 }

 @Test
 public void testCheckArrowEquivalenceHelper_TolerateUnknowns_ReturnsTrueIfUnknownTypesAreEqual() {
 ArrowType arrowType1 = new ArrowType(registry, NodeFactory.createList(
 NodeFactory.createParameter(registry.createIntType()),
 NodeFactory.createParameter(registry.createUnknownType())), registry.createIntType());
 ArrowType arrowType2 = new ArrowType(registry, NodeFactory.createList(
 NodeFactory.createParameter(registry.createUnknownType()),
 NodeFactory.createParameter(registry.createIntType())), registry.createIntType());
 assertTrue(arrowType1.checkArrowEquivalenceHelper(arrowType2, true));
 }

 @Test
 public void testIsSubtype_SameTypes_ReturnsTrue() {
 ArrowType arrowType1 = new ArrowType(registry, NodeFactory.createList(
 NodeFactory.createParameter(registry.createIntType()),
 NodeFactory.createParameter(registry.createIntType())), registry.createIntType());
 ArrowType arrowType2 = new ArrowType(registry, NodeFactory.createList(
 NodeFactory.createParameter(registry.createIntType()),
 NodeFactory.createParameter(registry.createIntType())), registry.createIntType());
 assertTrue(arrowType1.isSubtype(arrowType2));
 }

 @Test
 public void testIsSubtype_DifferentReturnTypes_ReturnsFalse() {
 ArrowType arrowType1 = new ArrowType(registry, NodeFactory.createList(
 NodeFactory.createParameter(registry.createIntType()),
 NodeFactory.createParameter(registry.createIntType())), registry.createIntType());
 ArrowType arrowType2 = new ArrowType(registry, NodeFactory.createList(
 NodeFactory.createParameter(registry.createIntType()),
 NodeFactory.createParameter(registry.createStringType())), registry.createIntType());
 assertFalse(arrowType1.isSubtype(arrowType2));
 }

 @Test
 public void testIsSubtype_DifferentParameterTypes_ReturnsFalse() {
 ArrowType arrowType1 = new ArrowType(registry, NodeFactory.createList(
 NodeFactory.createParameter(registry.createIntType()),
 NodeFactory.createParameter(registry.createIntType())), registry.createIntType());
 ArrowType arrowType2 = new ArrowType(registry, NodeFactory.createList(
 NodeFactory.createParameter(registry.createStringType()),
 NodeFactory.createParameter(registry.createIntType())), registry.createIntType());
 assertFalse(arrowType1.isSubtype(arrowType2));
 }

 @Test
 public void testIsSubtype_TolerateUnknowns_ReturnsTrueIfUnknownTypesAreEqual() {
 ArrowType arrowType1 = new ArrowType(registry, NodeFactory.createList(
 NodeFactory.createParameter(registry.createIntType()),
 NodeFactory.createParameter(registry.createUnknownType())), registry.createIntType());
 ArrowType arrowType2 = new ArrowType(registry, NodeFactory.createList(
 NodeFactory.createParameter(registry.createUnknownType()),
 NodeFactory.createParameter(registry.createIntType())), registry.createIntType());
 assertTrue(arrowType1.isSubtype(arrowType2));
 }

 @Test
 public void testIsSubtype_VariableParameters_VariableParametersAreIgnored() {
 ArrowType arrowType1 = new ArrowType(registry, NodeFactory.createList(
 NodeFactory.createParameter(registry.createIntType()),
 NodeFactory.createParameter(registry.createIntType()),
 NodeFactory.createVariableArgumentList()), registry.createIntType());
 ArrowType arrowType2 = new ArrowType(registry, NodeFactory.createList(
 NodeFactory.createParameter(registry.createIntType()),
 NodeFactory.createParameter(registry.createIntType()),
 NodeFactory.createVariableArgumentList()), registry.createStringType());
 assertTrue(arrowType1.isSubtype(arrowType2));
 }

 @Test
 public void testToStringHelper_ReturnsArrowTypeString() {
 ArrowType arrowType = new ArrowType(registry, NodeFactory.createList(), registry.createIntType());
 assertEquals("[ArrowType]", arrowType.toStringHelper(false));
 }

 @Test
 public void testToStringHelper_ForAnnotations_ReturnsArrowTypeWithAnnotationsString() {
 ArrowType arrowType = new ArrowType(registry, NodeFactory.createList(), registry.createIntType(), true, null, null, null);
 assertEquals("[ArrowType @annotation1 @annotation2]", arrowType.toStringHelper(true));
 }

 @Test
 public void testHasAnyTemplateInternal_NoTemplates_ReturnsFalse() {
 ArrowType arrowType = new ArrowType(registry, NodeFactory.createList(), registry.createIntType());
 assertFalse(arrowType.hasAnyTemplateInternal());
 }

 @Test
 public void testHasAnyTemplateInternal_WithTemplateParameter_ReturnsTrue() {
 ArrowType arrowType = new ArrowType(registry, NodeFactory.createList(
 NodeFactory.createParameter(registry.createTemplateParameterType("T"))), registry.createIntType());
 assertTrue(arrowType.hasAnyTemplateInternal());
 }

 @Test
 public void testHasAnyTemplateInternal_WithTemplateReturnType_ReturnsTrue() {
 ArrowType arrowType = new ArrowType(registry, NodeFactory.createList(), registry.createTemplateType("T"));
 assertTrue(arrowType.hasAnyTemplateInternal());
 }

 @Test
 public void testHasAnyTemplateInternal_WithTemplatedParametersAndReturnType_ReturnsTrue() {
 ArrowType arrowType = new ArrowType(registry, NodeFactory.createList(
 NodeFactory.createParameter(registry.createTemplateParameterType("T1")),
 NodeFactory.createParameter(registry.createTemplateParameterType("T2"))), registry.createTemplateType("U<T1, T2>"));
 assertTrue(arrowType.hasAnyTemplateInternal());
 }

 @Test
 public void testHashCode_SameTypes_ReturnsSameHashCode() {
 ArrowType arrowType1 = new ArrowType(registry, NodeFactory.createList(
 NodeFactory.createParameter(registry.createIntType()),
 NodeFactory.createParameter(registry.createIntType())), registry.createIntType());
 ArrowType arrowType2 = new ArrowType(registry, NodeFactory.createList(
 NodeFactory.createParameter(registry.createIntType()),
 NodeFactory.createParameter(registry.createIntType())), registry.createIntType());
 assertEquals(arrowType1.hashCode(), arrowType2.hashCode());
 }

 @Test
 public void testHashCode_DifferentTypes_ReturnsDifferentHashCodes() {
 ArrowType arrowType1 = new ArrowType(registry, NodeFactory.createList(
 NodeFactory.createParameter(registry.createIntType()),
 NodeFactory.createParameter(registry.createIntType())), registry.createIntType());
 ArrowType arrowType2 = new ArrowType(registry, NodeFactory.createList(
 NodeFactory.createParameter(registry.createStringType()),
 NodeFactory.createParameter(registry.createIntType())), registry.createIntType());
 assertNotEquals(arrowType1.hashCode(), arrowType2.hashCode());
 }
}