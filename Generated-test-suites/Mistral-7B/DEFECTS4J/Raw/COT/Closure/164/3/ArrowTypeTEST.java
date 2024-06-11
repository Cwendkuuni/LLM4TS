import static org.junit.Assert.*;
import static com.google.javascript.rhino.JSType.*;
import com.google.javascript.rhino.ErrorReporter;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.JSTypeRegistry;
import com.google.javascript.rhino.jstype.ArrowType;

public class ArrowTypeTest {

 private ErrorReporter errorReporter;
 private JSTypeRegistry typeRegistry;

 @Before
 public void setup() {
 errorReporter = new ErrorReporter();
 typeRegistry = new JSTypeRegistry(errorReporter);
 }

 @Test
 public void testArrowTypeConstructorWithNullParametersAndReturnType() {
 ArrowType arrowType = new ArrowType(typeRegistry, null, null);
 assertNull(arrowType.getParameters());
 assertNull(arrowType.getReturnType());
 }

 @Test
 public void testArrowTypeConstructorWithValidParametersAndReturnType() {
 Node nodeParameters = createNode("(a, b) => a + b");
 ArrowType arrowType = new ArrowType(typeRegistry, nodeParameters, UNKNOWN_TYPE);
 assertEquals(nodeParameters, arrowType.getParameters());
 assertNull(arrowType.getReturnTypeInferred());
 assertNull(arrowType.getReturnType());
 }

 @Test
 public void testArrowTypeConstructorWithValidParametersAndInferredReturnType() {
 Node nodeParameters = createNode("(a, b) => a + b");
 ArrowType arrowType = new ArrowType(typeRegistry, nodeParameters, null, true);
 assertEquals(nodeParameters, arrowType.getParameters());
 assertTrue(arrowType.isReturnTypeInferred());
 assertNull(arrowType.getReturnType());
 }

 @Test
 public void testArrowTypeConstructorWithValidParametersAndKnownReturnType() {
 Node nodeParameters = createNode("(a, b) => a + b");
 JSType knownReturnType = intType();
 ArrowType arrowType = new ArrowType(typeRegistry, nodeParameters, knownReturnType);
 assertEquals(nodeParameters, arrowType.getParameters());
 assertFalse(arrowType.isReturnTypeInferred());
 assertEquals(knownReturnType, arrowType.getReturnType());
 }

 @Test
 public void testArrowTypeConstructorWithVarArgsInParameters() {
 Node nodeParameters = createNode("(a, ...b) => a + b");
 ArrowType arrowType = new ArrowType(typeRegistry, nodeParameters, UNKNOWN_TYPE);
 Node varArgsParam = arrowType.getParameters().getLastChild();
 assertTrue(varArgsParam.isVarArgs());
 }

 @Test
 public void testArrowTypeIsSubtypeOfAnotherArrowTypeWithSameParametersAndReturnType() {
 ArrowType arrowType1 = createArrowTypeWithParametersAndReturnType("(a, b) => a + b", intType());
 ArrowType arrowType2 = createArrowTypeWithParametersAndReturnType("(a, b) => a + b", intType());
 assertTrue(arrowType1.isSubtype(arrowType2));
 }

 @Test
 public void testArrowTypeIsSubtypeOfAnotherArrowTypeWithDifferentReturnType() {
 ArrowType arrowType1 = createArrowTypeWithParametersAndReturnType("(a, b) => a + b", intType());
 ArrowType arrowType2 = createArrowTypeWithParametersAndReturnType("(a, b) => a + b", booleanType());
 assertFalse(arrowType1.isSubtype(arrowType2));
 }

 @Test
 public void testArrowTypeIsSubtypeOfAnotherArrowTypeWithDifferentParameters() {
 ArrowType arrowType1 = createArrowTypeWithParametersAndReturnType("(a, b) => a + b", intType());
 ArrowType arrowType2 = createArrowTypeWithParametersAndReturnType("(c, d) => c + d", intType());
 assertFalse(arrowType1.isSubtype(arrowType2));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetLeastSupertype() {
 ArrowType arrowType = createArrowTypeWithParametersAndReturnType("(a, b) => a + b", intType());
 arrowType.getLeastSupertype(null);
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetGreatestSubtype() {
 ArrowType arrowType = createArrowTypeWithParametersAndReturnType("(a, b) => a + b", intType());arrowType.getGreatestSubtype(null); }

 @Test
 public void testHasEqualParameters() {
 ArrowType arrowType1 = createArrowTypeWithParametersAndReturnType("(a, b) => a + b", intType());
 ArrowType arrowType2 = createArrowTypeWithParametersAndReturnType("(a, b) => a + b", intType());
 assertTrue(arrowType1.hasEqualParameters(arrowType2));
 }

 @Test
 public void testHasEqualParametersWithDifferentParameters() {
 ArrowType arrowType1 = createArrowTypeWithParametersAndReturnType("(a, b) => a + b", intType());
 ArrowType arrowType2 = createArrowTypeWithParametersAndReturnType("(c, d) => c + d", intType());
 assertFalse(arrowType1.hasEqualParameters(arrowType2));
 }

 @Test
 public void testIsEquivalentTo() {
 ArrowType arrowType1 = createArrowTypeWithParametersAndReturnType("(a, b) => a + b", intType());
 ArrowType arrowType2 = createArrowTypeWithParametersAndReturnType("(a, b) => a + b", intType());
 assertTrue(arrowType1.isEquivalentTo(arrowType2));
 }

 @Test
 public void testIsEquivalentToWithDifferentReturnType() {
 ArrowType arrowType1 = createArrowTypeWithParametersAndReturnType("(a, b) => a + b", intType());
 ArrowType arrowType2 = createArrowTypeWithParametersAndReturnType("(a, b) => a + b", booleanType());
 assertFalse(arrowType1.isEquivalentTo(arrowType2));
 }

 @Test
 public void testIsEquivalentToWithDifferentParameters() {
 ArrowType arrowType1 = createArrowTypeWithParametersAndReturnType("(a, b) => a + b", intType());
 ArrowType arrowType2 = createArrowTypeWithParametersAndReturnType("(c, d) => c + d", intType());
 assertFalse(arrowType1.isEquivalentTo(arrowType2));
 }

 private ArrowType createArrowTypeWithParametersAndReturnType(String arrowFunctionExpression, JSType returnType) {
 Node nodeParameters = createNode(arrowFunctionExpression);
 ArrowType arrowType = new ArrowType(typeRegistry, nodeParameters, returnType);
 return arrowType;
 }

 private Node createNode(String expression) {
 return typeRegistry.compile(expression, errorReporter).getRootNode();
 }
}