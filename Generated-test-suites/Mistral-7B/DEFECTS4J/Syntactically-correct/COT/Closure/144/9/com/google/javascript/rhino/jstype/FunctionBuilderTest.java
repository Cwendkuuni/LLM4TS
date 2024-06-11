import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.jstype.FunctionBuilder;
import com.google.javascript.rhino.jstype.FunctionParamBuilder;
import com.google.javascript.rhino.jstype.FunctionType;

public class FunctionBuilderTest {

 private JSTypeRegistry registry;
 private FunctionBuilder functionBuilder;

 @Test
 public void testFunctionBuilderWithNameAndReturnType() {
 functionBuilder = new FunctionBuilder(mock(JSTypeRegistry.class));
 functionBuilder.withName("testFunction");
 functionBuilder.withReturnType(mock(JSType.class));
 FunctionType actualFunctionType = functionBuilder.build();
 assertNotNull(actualFunctionType);
 }

 @Test
 public void testFunctionBuilderWithNameAndParameters() {
 FunctionParamBuilder functionParamBuilder = mock(FunctionParamBuilder.class);
 functionBuilder = new FunctionBuilder(mock(JSTypeRegistry.class));
 functionBuilder.withName("testFunction");
 functionBuilder.withParams(functionParamBuilder);
 FunctionType actualFunctionType = functionBuilder.build();
 assertNotNull(actualFunctionType);
 }

 @Test
 public void testFunctionBuilderWithNameAndInferredReturnType() {
 functionBuilder = new FunctionBuilder(mock(JSTypeRegistry.class));
 functionBuilder.withName("testFunction");
 functionBuilder.withInferredReturnType(mock(JSType.class));
 FunctionType actualFunctionType = functionBuilder.build();
 assertNotNull(actualFunctionType);
 }

 @Test
 public void testFunctionBuilderWithNameAndSourceNode() {
 Node sourceNode = mock(Node.class);
 functionBuilder = new FunctionBuilder(mock(JSTypeRegistry.class));
 functionBuilder.withName("testFunction");
 functionBuilder.withSourceNode(sourceNode);
 FunctionType actualFunctionType = functionBuilder.build();
 assertNotNull(actualFunctionType);
 }

 @Test
 public void testFunctionBuilderWithNameAndTemplateName() {
 functionBuilder = new FunctionBuilder(mock(JSTypeRegistry.class));
 functionBuilder.withName("testFunction");
 functionBuilder.withTemplateName("testTemplate");
 FunctionType actualFunctionType = functionBuilder.build();
 assertNotNull(actualFunctionType);
 }

 @Test
 public void testFunctionBuilderWithNameAndTypeOfThis() {
 ObjectType typeOfThis = mock(ObjectType.class);
 functionBuilder = new FunctionBuilder(mock(JSTypeRegistry.class));
 functionBuilder.withName("testFunction");
 functionBuilder.withTypeOfThis(typeOfThis);
 FunctionType actualFunctionType = functionBuilder.build();
 assertNotNull(actualFunctionType);
 }

 @Test
 public void testFunctionBuilderWithoutName() {
 functionBuilder = new FunctionBuilder(mock(JSTypeRegistry.class));
 FunctionType actualFunctionType = functionBuilder.build();
 assertNull(actualFunctionType.getReferenceName());
 }

 @Test(expected = IllegalStateException.class)
 public void testFunctionBuilderWithInvalidReturnType() {
 functionBuilder = new FunctionBuilder(mock(JSTypeRegistry.class));
 functionBuilder.withReturnType(null);
 functionBuilder.build();
 }

 @Test
 public void testFunctionBuilderWithNullParametersNode() {
 functionBuilder = new FunctionBuilder(mock(JSTypeRegistry.class));
 functionBuilder.withParamsNode(null);
 FunctionType actualFunctionType = functionBuilder.build();
 assertNotNull(actualFunctionType);
 }

 @Test
 public void testFunctionBuilderWithEmptyParametersNode() {
 Node emptyParametersNode = mock(Node.class);
 functionBuilder = new FunctionBuilder(mock(JSTypeRegistry.class));
 functionBuilder.withParamsNode(emptyParametersNode);
 FunctionType actualFunctionType = functionBuilder.build();
 assertNotNull(actualFunctionType.getParameters());
 assertEmpty(actualFunctionType.getParameters());
 }

 @Test
 public void testFunctionBuilderWithParametersNodeContainingOneParameter() {
 Node parametersNode = mock(Node.class);
 when(parametersNode.getFirstChild()).thenReturn(mock(Node.class));
 functionBuilder = new FunctionBuilder(mock(JSTypeRegistry.class));
 functionBuilder.withParamsNode(parametersNode);
 FunctionType actualFunctionType = functionBuilder.build();
 assertNotNull(actualFunctionType.getParameters());
 assertSize(1, actualFunctionType.getParameters());
 }

 @Test
 public void testFunctionBuilderWithParametersNodeContainingMultipleParameters() {
 Node parametersNode = mock(Node.class);
 Node firstChild = mock(Node.class);
 Node secondChild = mock(Node.class);
 when(parametersNode.getFirstChild()).thenReturn(firstChild);
 when(firstChild.getNextSibling()).thenReturn(secondChild);
 functionBuilder = new FunctionBuilder(mock(JSTypeRegistry.class));
 functionBuilder.withParamsNode(parametersNode);
 FunctionType actualFunctionType = functionBuilder.build();
 assertNotNull(actualFunctionType.getParameters());
 assertSize(2, actualFunctionType.getParameters());
 }

 @Test
 public void testFunctionBuilderWithInvalidTypeOfThis() {
 functionBuilder = new FunctionBuilder(mock(JSTypeRegistry.class));
 ObjectType invalidThisType = mock(ObjectType.class);
 functionBuilder.withTypeOfThis(invalidThisType);
 FunctionType actualFunctionType = functionBuilder.build();
 assertNotNull(actualFunctionType);
 }

 @Test
 public void testFunctionBuilderWithNativeType() {
 functionBuilder = new FunctionBuilder(mock(JSTypeRegistry.class)).forNativeType();
 FunctionType actualFunctionType = functionBuilder.build();
 assertTrue(actualFunctionType.isNativeObjectType());
 }

 @Test
 public void testFunctionBuilderWithConstructor() {
 functionBuilder = new FunctionBuilder(mock(JSTypeRegistry.class)).forConstructor();
 FunctionType actualFunctionType = functionBuilder.build();
 assertTrue(actualFunctionType.isConstructor());
 }

 @Test
 public void testFunctionBuilderCopyFromOtherFunction() {
 FunctionType otherFunctionType = mock(FunctionType.class);
 functionBuilder = new FunctionBuilder(mock(JSTypeRegistry.class));
 functionBuilder.copyFromOtherFunction(otherFunctionType);
 FunctionType actualFunctionType = functionBuilder.build();
 assertEquals(otherFunctionType.getReferenceName(), actualFunctionType.getReferenceName());
 assertEquals(otherFunctionType.getSource(), actualFunctionType.getSource());
 assertEquals(otherFunctionType.getParametersNode(), actualFunctionType.getParametersNode());
 assertEquals(otherFunctionType.getReturnType(), actualFunctionType.getReturnType());
 assertEquals(otherFunctionType.getTypeOfThis(), actualFunctionType.getTypeOfThis());
 assertEquals(otherFunctionType.getTemplateTypeName(), actualFunctionType.getTemplateTypeName());
 assertEquals(otherFunctionType.isConstructor(), actualFunctionType.isConstructor());
 assertEquals(otherFunctionType.isNativeObjectType(), actualFunctionType.isNativeObjectType());
 }

 private void assertEmpty(Object[] array) {
 assertNotNull(array);
 assertEquals(0, array.length);
 }

 private void assertSize(int expectedSize, Object[] array) {
 assertNotNull(array);
 assertEquals(expectedSize, array.length);
 }

 private void assertEquals(Object expected, Object actual) {
 assertNotNull(expected);
 assertNotNull(actual);
 assertEquals(expected, actual);
 }
}